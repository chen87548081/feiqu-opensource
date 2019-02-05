/**

 @Name: home 模块

 */

layui.define('fly', function(exports){

    var $ = layui.jquery;
    var layer = layui.layer,form = layui.form,laypage = layui.laypage
        ,fly = layui.fly,flow=layui.flow,upload=layui.upload,laytpl=layui.laytpl;
    var editor = $('#thoughtContent');
    $('.detail-body').each(function(){
        var othis = $(this), html = othis.html();
        if(html.indexOf('<img>') == -1){
            othis.html(fly.content(html));
        }
        othis.html(fly.content(html));
    });
    flow.lazyimg();
    $('.zan').on('click',function () {
        var id = $(this).attr('lay-data')
        var zanNum = parseInt($(this).find('span').html()||0)
        var that = $(this)
        fly.json('${ctxPath}/thought/like?thoughtId='+id,{},function(result){
            that.find('span').html(zanNum+1)
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
            '<input id="commentInputId" type="text" placeholder="评论下这个想法吧~" class="layui-input">'+
            ' <button onclick="commentReal('+tid+')" class="layui-btn layui-btn-warm">评论</button></div></div>'
        var reply = $(this).parent().after(commentBlock)
        $('#commentInputId').focus()
    })
    var html = ['<div class="layui-unselect fly-edit">'
        ,'<span type="face" title="插入表情"><i class="iconfont icon-yxj-expression"></i>表情</span>'
        ,'<span type="picture" title="插入图片：img[src]"><i class="iconfont icon-tupian"></i>图片</span>','</div>'].join('');
    $('.kind').html(html)
    $('.fly-edit span').on('click', function(event){
        var type = $(this).attr('type');
        if(type === 'face'){
            var str = '', ul, face = fly.faces;
            for(var key in face){
                str += '<li title="'+ key +'"><img src="'+ face[key] +'"></li>';
            }
            str = '<ul id="LAY-editface" class="layui-clear">'+ str +'</ul>';
            layer.tips(str, this, {
                tips: 1
                ,time: 0
                ,skin: 'layui-edit-face'
            });
            $(document).on('click', function(){
                layer.closeAll('tips');
            });
            $('#LAY-editface li').on('click', function(){
                var title = $(this).attr('title') + ' ';
                layui.focusInsert(editor[0], 'face' + title);
            });
            event.stopPropagation()
        }else if(type === 'picture'){
            layer.open({
                type: 1
                ,id: 'fly-jie-upload'
                ,title: '插入图片'
                ,area: 'auto'
                ,shade: false
                ,area: '465px'
                ,fixed: false
                ,offset: [
                    editor.offset().top - $(window).scrollTop() + 'px'
                    ,editor.offset().left + 'px'
                ]
                ,skin: 'layui-layer-border'
                ,content: ['<ul class="layui-form layui-form-pane" style="margin: 20px;">'
                    ,'<li class="layui-form-item">'
                    ,'<label class="layui-form-label">URL</label>'
                    ,'<div class="layui-input-inline">'
                    ,'<input required name="image" placeholder="支持直接粘贴远程图片地址" value="" class="layui-input">'
                    ,'</div>'
                    ,'<button type="button" class="layui-btn layui-btn-primary" id="uploadImg"><i class="layui-icon">&#xe67c;</i>上传图片</button>'
                    ,'</li>'
                    ,'<li class="layui-form-item" style="text-align: center;">'
                    ,'<button type="button" lay-submit lay-filter="uploadImages" class="layui-btn">确认</button>'
                    ,'</li>'
                    ,'</ul>'].join('')
                ,success: function(layero, index){
                    var image =  layero.find('input[name="image"]');
                    upload.render({
                        elem: '#uploadImg'
                        ,url: '/api/upload/'
                        ,size: 300
                        ,done: function(res){
                            if(res.code == '0000'){
                                image.val(res.data);
                            } else {
                                layer.msg(res.message, {icon: 5});
                            }
                        }
                    });
                    form.on('submit(uploadImages)', function(data){
                        var field = data.field;
                        if(!field.image) return image.focus();
                        layui.focusInsert(editor[0], 'img['+ field.image + '] ');
                        layer.close(index);
                    });
                }
            });
        }
    });
    form.on('submit(tform)', function (data) {
        fly.json('${ctxPath}/u/postThoughts',data.field,function (res) {
            layer.msg("发布成功~"+res.message);
            $('#thoughtContent').val('')
            var data = res.data;
            $('#thoughtList').prepend('<li><div class="detail-about">'+
                '<a href="/u/${user.id!}/peopleIndex" class="fly-avatar"><img src="${user.icon!}"></a>'+
                '<div class="fly-detail-user"><a href="/u/8/peopleIndex" class="c-fly-link"><cite>${user.nickname!}</cite></a></div>'+
                '<div class="detail-hits"><span>刚刚</span></div></div><div class="detail-body thought-body photos">'+fly.content(data.thoughtContent)+'</div>'+
                '<div class="c-list-info"></div></li>')
        })
        return false
    });

    exports('home', null);
});