/**
 @Name: home 模块
 */
layui.define(['fly','flow','laypage'], function(exports){
    var $ = layui.jquery;
    var layer = layui.layer,form = layui.form,laypage = layui.laypage
        ,fly = layui.fly,flow=layui.flow,upload=layui.upload,laytpl=layui.laytpl;
    var device = layui.device();
    var editor = $('#thoughtContent');
    $('.thought-text').each(function(){
        var othis = $(this), html = othis.html();
        var content = twemoji.parse(fly.content(html));
        var linesArray = content.split('<br>');
        var lines = linesArray.length;
        if(lines > 10){
            var shortstr = '',hiddenStr = '';
            for(var i = 0;i<lines;i++){
                if(i <= 10){
                    shortstr += linesArray[i]+"<br>";
                }else {
                    if(i === lines-1){
                        hiddenStr +=linesArray[i];
                    }else {
                        hiddenStr +=linesArray[i]+"<br>";
                    }
                }
            }
            shortstr += '<div class="layui-hide">'+hiddenStr+'</div><a cmd="expand" href="javascript:;" class="toggle-btn">展示全文</a>';
            othis.html(shortstr);
            var $toggleBtn = othis.find('.toggle-btn');
            $toggleBtn.on('click',function () {
                var $toggle = $(this);
                if($toggle.attr('cmd') === 'expand'){
                    $toggle.siblings('div').removeClass('layui-hide');
                    $toggle.attr('cmd','hide');$toggle.html('收起');
                }else {
                    $toggle.siblings('div').addClass('layui-hide');
                    $toggle.attr('cmd','expand');$toggle.html('展示全部');
                }
            })
        }else {
            othis.html(content);
        }
    });

    $('#closeLayerPic').on('click',function () {
        if(thoughtPics.length > 0){
            layer.confirm('真的要放弃上传图片么', function(index){
                closeLayerPic();
                layer.close(index);
            });
        }else closeLayerPic();
    })
    var closeLayerPic = function () {
        $('.layer-pic-list').addClass("layui-hide");
        var $lis = $('.to-upload-pictures').find('li');
        layui.each($lis,function (index,item) {
            if(!$(item).hasClass('upload-img-add')){
                $(item).remove();
            }
        })
        thoughtPics = [];
    }
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
    $('.handTop').on('click',function () {
        var othis = $(this), tid = othis.parents('li').data('id');
        fly.json('/thought/handTop/'+tid,{},function(res){
            layer.msg('置顶成功,将持续24个小时！');
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
        var reply = $(this).parent().after(commentBlock);
        var commentInput = $('#commentInputId');
        commentInput.focus();
        commentInput.on('input',function (eve) {
            if($(this).val() === '@'){
                fly.json('/api/findActiveUserNames',function (res) {
                    if(res.data){
                        var names = res.data;
                        var str = "";
                        layui.each(names,function (index,item) {
                            if(layui.cache.user.username !== item){
                                str+='<li>'+item+'</li>'
                            }
                        })
                        str = '<ul id="AITE-nicknames" class="layui-clear">'+ str +'</ul>';
                        layer.tips(str, commentInput, {
                            tips: 3
                            ,time: 0
                            ,skin: 'layer-names'
                        });
                        $(document).on('click', function(){
                            layer.closeAll('tips');
                        });
                        $('#AITE-nicknames li').on('click', function(){
                            var name = $(this).html() + ' ';
                            layui.focusInsert(commentInput[0], name);
                        });
                    }
                })
            }
        })

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
                layui.focusInsert(commentInput[0], 'face' + title);
            });
            event.stopPropagation()
        })

        $('#commentBtn').on('click',function () {
            var li = $(this).parents('li')
            var thoughtId = li.data('id'), numElem=li.find('.comment-num')
            var commentData = {
                topicId: thoughtId,
                content: commentInput.val()
            };
            fly.json('/thought/comment',commentData,function (res) {
                numElem.html(res.data)
                layer.tips('回复成功啦', numElem, {
                    tips: [1, '#FF5722']
                });
                commentInput.closest('.c-reply-block').remove()
            })
        })
    })

    var html = ['<div class="layui-unselect fly-edit">'
        ,'<span type="face" title="插入表情"><i class="layui-icon">&#xe6af;</i>表情</span>'
        ,'<span type="picture" title="插入图片"><i class="layui-icon">&#xe64a;</i>图片</span>'].join('');
    $('.kind').html(html);
    var thoughtPics = [];
    var uploadedPicNum = 0;
    var loadIndex;
    var $picList = $('input[name="picList"]');
    upload.render({
        elem: '.addOnePic'
        ,url: '/api/upload/'
        ,size: 10000
        ,acceptMime:'image/*'
        ,data:{picNum:function () {
            return thoughtPics.length;
        }}
        ,before:function (obj) {
            loadIndex = layer.load(2, {shade: 0.8})
        }
        ,done: function(res){
            layer.close(loadIndex);
            if(res.code === '0000'){
                thoughtPics.push(res.data);
                $('.uploader-meta').html('共 '+thoughtPics.length+' 张，还能上传 '+(9-thoughtPics.length)+' 张')
                $('.to-upload-pictures').append('<li class="upload-img-item"><div class="img-wrap"><img src="'+res.data+'"></div>' +
                    '<i class="layui-icon layui-icon-close remove-img"></i></li>');
                layui.each($('.remove-img'),function (index,item) {
                    $(item).on('click',function () {
                        var $li = $(this).parent('li');
                        $li.fadeOut(1000,function () {
                            $li.remove();
                            thoughtPics.splice(index,1);
                            $('.uploader-meta').html('共 '+thoughtPics.length+' 张，还能上传 '+(9-thoughtPics.length)+' 张')
                        });
                    })
                })
            } else {
                layer.msg(res.message, {icon: 5});
            }
        }
        ,error: function(){
            layer.close(loadIndex);
        }
    });

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
            var $layerPic = $('.layer-pic-list');
            if($layerPic.hasClass('layui-hide')){
                $layerPic.removeClass('layui-hide');
            }else $layerPic.addClass('layui-hide');
        }else if(type === 'video'){
            layer.open({
                type: 1
                ,id: 'home-video-upload'
                ,title: '插入视频'
                ,shade: false
                ,area: (device.ios || device.android) ? ($(window).width() + 'px') : '660px'
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
                    ,'<input required name="video" placeholder="支持直接粘贴远程视频地址" value="" class="layui-input">'
                    ,'</div>'
                    ,'</li>'
                    ,'<li class="layui-form-item" style="text-align: center;">'
                    ,'<button type="button" lay-submit lay-filter="uploadVideo" class="layui-btn">确认</button>'
                    ,'</li>'
                    ,'</ul>'].join('')
                ,success: function(layero, index){
                    var video =  layero.find('input[name="video"]');
                    var loadIndex;
                    upload.render({
                        elem: '#uploadVideo'
                        ,url: '/api/uploadVideo'
                        ,size: 20000
                        ,accept:'video'
                        ,before:function (obj) {
                            loadIndex = layer.load(2, {shade: 0.8})
                        }
                        ,done: function(res){
                            layer.close(loadIndex);
                            if(res.code == '0000'){
                                video.val(res.data);
                            } else {
                                layer.msg(res.message, {icon: 5});
                            }
                        }
                        ,error: function(){
                            layer.close(loadIndex);
                        }
                    });
                    form.on('submit(uploadVideo)', function(data){
                        var field = data.field;
                        if(!field.video) return video.focus();
                        layui.focusInsert(editor[0], 'video['+ field.video + '] ');
                        layer.close(index);
                    });
                }
            });
        }
    });
    form.on('submit(tform)', function (data) {
        var action = $(data.form).attr('action');
        data.field.picList = thoughtPics.join(",");
        fly.json(action,data.field,function (res) {
            layer.msg("发布成功~");
            closeLayerPic();
            $('#thoughtContent').val('');
            var picList = res.data.picList,picsHtml = '';
            if(picList !== ''){
                var picSplit = picList.split(",");
                layui.each(picSplit,function (index, item) {
                    picsHtml+='<img src="'+item+'"/>'
                })
            }
            $('#thoughtList').prepend('<li><div class="detail-about">'+
                '<a href="/u/'+layui.cache.user.userId+'/peopleIndex" class="fly-avatar"><img src="'+layui.cache.user.icon+'"></a>'+
                '<div class="fly-detail-user"><a href="/u/'+layui.cache.user.userId+'/peopleIndex" class="c-fly-link"><cite>'+layui.cache.user.username+'</cite></a></div>'+
                '<div class="detail-hits"><span class="t-area">'+res.data.area+'</span ><span>刚刚</span></div></div><div class="detail-body thought-body photos">'+
                '<div class="thought-text">'+twemoji.parse(fly.content(res.data.thoughtContent))+'</div>' +
                '<div class="thought-pic">'+picsHtml+'</div></div><div class="c-list-info"></div></li>')
        })
        return false
    });

    $('.comments').on('click',function () {
        location.href='/thought/'+ $(this).closest('li').data('id')
    })
    laypage.render({
        elem: 'pagesplit'
        ,theme: 'custom'
        , curr:layui.cache.pageInfo.curr
        , count:layui.cache.pageInfo.count
        , limit:layui.cache.pageInfo.limit
        , layout: ['count', 'prev', 'page', 'next']
        , jump: function (obj, first) {
            if (!first) {
                location.href = "/u/"+layui.cache.user.userId+"/home?p=" + obj.curr
            }
        }
    });
    laypage.render({
        elem: 'pagesplit-head'
        ,theme: 'custom'
        , curr:layui.cache.pageInfo.curr
        , count:layui.cache.pageInfo.count
        , limit:layui.cache.pageInfo.limit
        ,groups:3
        , layout: [ 'page']
        , jump: function (obj, first) {
            if (!first) {
                location.href = "/u/"+layui.cache.user.userId+"/home?p=" + obj.curr
            }
        }
    });
    flow.lazyimg();
    exports('home', {});
});