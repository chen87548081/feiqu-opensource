layui.define(['fly','face'],function (exports) {
    var $ = layui.jquery;
    var layer = layui.layer;
    var form = layui.form;
    var fly = layui.fly;
    var face = layui.face;
    var gather = {}, editor = $('.fly-editor')
    if(sessionStorage.getItem("commentContent")){
        editor.val(sessionStorage.getItem("commentContent"))
    }
    form.on('submit(formDemo)', function(data){
        sessionStorage.setItem("commentContent",data.field.content);
        fly.json('/article/comment',data.field,function (result) {
            layer.msg("评论成功~");
            var newCom = result.data;
            var liNew = '<li><div class="comment-detail detail-about">'+
                '<a class="fly-avatar" href="/u/'+layui.cache.user.userId+'/peopleIndex">'+
                '<img src="'+layui.cache.user.icon+'">'+
                '</a>'+
                '<div class="comment-detail-user"><a class="c-fly-link" href="/u/'+layui.cache.user.userId+'/peopleIndex">'+layui.cache.user.username+'</a>'+
                '</div>'+
                ' <div class="detail-time"><span>刚刚</span></div></div>' +
                '<div class="comment-body">'+fly.content(newCom.content)+'</div>' +
                '</li>';
            $(".comment-list").append(liNew)
            editor.val('')
        })
        return false;
    });

    gather.articleAdmin={
        collect:function (div) {
            var othis = $(this), type = othis.data('type');
            fly.json('/article/collect/'+type, {aid:div.data('id')}, function(res){
                if(type === 'add'){
                    othis.data('type', 'remove').html('取消收藏').addClass('layui-btn-danger');
                } else if(type === 'remove'){
                    othis.data('type', 'add').html('收藏').removeClass('layui-btn-danger');
                }
            });
        }
        ,like:function (li) {
            var othis = $(this), ok = othis.hasClass('zanok');
            fly.json('/comment/like', {
                ok: ok
                ,commentId: li.data('id')
            }, function(res){
                if(res.status === 0){
                    var zans = othis.find('em').html()|0;
                    othis[ok ? 'removeClass' : 'addClass']('zanok');
                    othis.find('em').html(ok ? (--zans) : (++zans));
                } else {
                    layer.msg(res.msg);
                }
            });
        }
    }

    var asyncRender = function(){
        var articleAdmin = $('#article_admin');
        //查询帖子是否收藏
        if(articleAdmin[0] && layui.cache.user.userId != -1){
            fly.json('/article/collection/find', {
                aid: articleAdmin.data('id')
            }, function(res){
                articleAdmin.append('<span class="layui-btn layui-btn-xs collect-btn '+ (res.data ? 'layui-btn-danger' : '') +'" type="collect" data-type="'+ (res.data ? 'remove' : 'add') +'">'+ (res.data ? '取消收藏' : '收藏') +'</span>');
            });
        }
    }();

    gather.jiedaActive = {
        zan: function(li){ //赞
            var othis = $(this), ok = othis.hasClass('zanok');
            fly.json('/comment/like', {
                commentId: li.data('id')
            }, function(res){
                var zans = othis.find('em').html()|0;
                othis[ok ? 'removeClass' : 'addClass']('zanok');
                othis.find('em').html(ok ? (--zans) : (++zans));
            });
        }
        ,reply: function(li){ //回复
            var val = editor.val();
            var aite = '@'+ li.find('.comment-detail-user cite').text().replace(/\s/g, '');
            editor.focus()
            if(val.indexOf(aite) !== -1) return;
            editor.val(aite +' ' + val);
        }
        ,del: function(li){ //删除
            layer.confirm('确认删除该回复么？', function(index){
                layer.close(index);
                fly.json('/article/deleteComment', {
                    commentId: li.data('id')
                }, function(res){
                    li.remove();
                });
            });
        }
    };

    $('.c-reply span').on('click', function(){
        var othis = $(this), type = othis.attr('type');
        gather.jiedaActive[type].call(this, othis.parents('li'));
    });

    $('body').on('click', '.collect-btn', function(){
        var othis = $(this), type = othis.attr('type');
        gather.articleAdmin[type] && gather.articleAdmin[type].call(this, othis.parent());
    });

    $('.comment-dir').on('click',function () {
        $('html,body').animate({scrollTop:$('.fly-editor').offset().top}, 800);
        editor.focus()
    })

    if(articleType === 1){
        $('.art-content').each(function(){
            var othis = $(this), html = othis.html();
            othis.html(fly.content(html));
        });
    }
    $('.comment-body').each(function(){
        var othis = $(this), html = othis.html();
        othis.html(fly.content(html));
    });

    fly.json('/article/clickCount/'+$('#article_admin').data('id'),function () {
        return;
    })

    exports('article',null);
})