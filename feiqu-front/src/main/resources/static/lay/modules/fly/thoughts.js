layui.define(['fly','flow','laypage'], function(exports){
    var $ = layui.jquery;
    var layer = layui.layer,form = layui.form,laypage = layui.laypage
        ,fly = layui.fly,flow=layui.flow,upload=layui.upload,laytpl=layui.laytpl;
    var editor = $('#thoughtContent');
    $('.detail-body').each(function(){
        var othis = $(this), html = othis.html();
        othis.html(twemoji.parse(fly.content(html)));
    });
    flow.lazyimg();
    $('.collect').on('click',function () {
        var othis = $(this), type = othis.data('type'),div=othis.parents('li');
        fly.json('/thought/collect/'+type, {tid:div.data('id')}, function(res){
            if(type === 'add'){
                othis.data('type', 'remove').html('<i class="fa fa-heart"></i>取消收藏');
            } else if(type === 'remove'){
                othis.data('type', 'add').html('<i class="fa fa-heart-o"></i>收藏');
            }
        });
    })
    $('.zan').on('click',function () {
        var othis = $(this), tid = othis.parents('li').data('id');
        var numEle = othis.find('cite');
        fly.json('/thought/like?thoughtId='+tid,{},function(res){
            numEle.html(res.data)
            othis.addClass('zanok')
            layer.msg('点赞成功')
        })
    })
    $('.comment-show').click(function () {
        var showBelow = $(this).closest('.c-list-info').find('#commentInputId')
        if (showBelow[0]){
            showBelow.closest('.c-reply-block').remove()
            return
        }
        var tid = $(this).attr('lay-data')
        var elem = $('#commentInputId')
        if(elem[0]){
            $('#commentInputId').closest('.c-reply-block').remove()
        }
        var commentBlock = '<div class="c-reply-block"><div class="reply-input">'+
                '<span type="face" title="插入表情"><i class="iconfont icon-yxj-expression"></i></span>'+
            '<input id="commentInputId" type="text" placeholder="评论下这个想法吧~" class="layui-input">'+
            ' <button id="commentBtn" class="layui-btn layui-btn-warm">评论</button></div></div>'
        var reply = $(this).parent().after(commentBlock)
        $('#commentInputId').focus()

        $('.reply-input span').on('click', function(event){
            var str = '', ul, face = fly.faces;
            for (var key in face) {
                str += '<li title="' + key + '"><img src="' + face[key] + '"></li>';
            }
            str = '<ul id="LAY-editface" class="layui-clear">' + str + '</ul>';
            layer.tips(str, this, {
                tips: 1
                , time: 0
                , skin: 'layui-edit-face'
            });
            $(document).on('click', function () {
                layer.closeAll('tips');
            });
            $('#LAY-editface li').on('click', function () {
                var title = $(this).attr('title') + ' ';
                layui.focusInsert($('#commentInputId')[0], 'face' + title);
            });
            event.stopPropagation()
        })

        $('#commentBtn').on('click',function () {
            var li = $(this).parents('li')
            var thoughtId = li.data('id'), numElem=li.find('.comment-num')
            var commentData = {
                topicId: thoughtId,
                content: $('#commentInputId').val()
            };
            fly.json('/thought/comment',commentData,function (res) {
                numElem.html(res.data)
                layer.tips('回复成功啦', numElem, {
                    tips: [1, '#FF5722']
                });
                $('#commentInputId').closest('.c-reply-block').remove()
            })
        })
    })

    $('.comments').on('click',function () {
        var count = parseInt($(this).children('cite').html() || 0)
        if (count == 0) {
            layer.msg('暂无评论~');return
        }
        location.href='/thought/'+ $(this).closest('li').data('id')
    })
    $('.del-t').on('click',function () {
        var li = $(this).parents('li');
        layer.confirm('确认删除这个想法吗?', {icon: 3, title:'提示'}, function(index){
            var tid = li.data('id');
            fly.json('/thought/delete?thoughtId='+tid,function (result) {
                    layer.msg('删除成功');
                    li.fadeOut(1000,function(){
                        this.remove();
                    })
            })
            layer.close(index);
        });
    })
    laypage.render({
        elem: 'pagesplit'
        , curr:layui.cache.pageInfo.curr
        , count:layui.cache.pageInfo.count
        , limit:layui.cache.pageInfo.limit
        , layout: ['count', 'prev', 'page', 'next']
        , jump: function (obj, first) {
            if (!first) {
                location.href = "/thought/my?page=" + obj.curr
            }
        }
    });
    exports('thoughts', {});
});