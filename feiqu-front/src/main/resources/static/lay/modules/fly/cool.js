/**
 @Name: flyfun社区主入口 向贤心致敬
 */
layui.define(['layer', 'laytpl', 'form', 'element', 'upload', 'util'], function(exports){

    var $ = layui.jquery
        ,layer = layui.layer
        ,laytpl = layui.laytpl
        ,form = layui.form
        ,element = layui.element
        ,upload = layui.upload
        ,util = layui.util
        ,device = layui.device()
        ,DISABLED = 'layui-btn-disabled';

    //阻止IE7以下访问
    if(device.ie && device.ie < 8){
        layer.alert('如果您非得使用 IE 浏览器访问飞趣社区，那么请使用 IE8+');
    }

    layui.focusInsert = function(obj, str){
        var result, val = obj.value;
        obj.focus();
        if(document.selection){ //ie
            result = document.selection.createRange();
            document.selection.empty();
            result.text = str;
        } else {
            result = [val.substring(0, obj.selectionStart), str, val.substr(obj.selectionEnd)];
            obj.focus();
            obj.value = result.join('');
        }
    };

    //数字前置补零
    layui.laytpl.digit = function(num, length, end){
        var str = '';
        num = String(num);
        length = length || 2;
        for(var i = num.length; i < length; i++){
            str += '0';
        }
        return num < Math.pow(10, length) ? str + (num|0) : num;
    };

    var fly = {

        dir: layui.cache.host + 'lay/modules/fly/' //模块路径

        ,HTMLDecode:function(text) {
            var temp = document.createElement("div");
            temp.innerHTML = text;
            var output = temp.innerText || temp.textContent;
            temp = null;
            return output;
        }

        ,json: function(url, data, success, options){
            var that = this, type = typeof data === 'function';

            if(type){
                options = success
                success = data;
                data = {};
            }

            options = options || {};

            return $.ajax({
                type: options.type || 'post',
                dataType: options.dataType || 'json',
                data: data,
                url: url,
                success: function(res){
                    if(res.code === '0000') {
                        success && success(res);
                    } else {
                        layer.msg(res.message || res.code, {shift: 6});
                        options.error && options.error();
                    }
                }, error: function(e){
                    layer.msg('请求异常，请重试', {shift: 6});
                    options.error && options.error(e);
                }
            });
        }
        ,charLen: function(val){
            var arr = val.split(''), len = 0;
            for(var i = 0; i <  val.length ; i++){
                arr[i].charCodeAt(0) < 299 ? len++ : len += 2;
            }
            return len;
        }
        ,form: {}
        ,layEditor: function(options){
            var html = ['<div class="layui-unselect fly-edit">'
                ,'<span type="face" title="插入表情"><i class="iconfont icon-yxj-expression" style="top: 1px;"></i></span>'
                ,'<span type="picture" title="插入图片：img[src]"><i class="iconfont icon-tupian"></i></span>'
                ,'<span type="href" title="超链接格式：a(href)[text]"><i class="iconfont icon-lianjie"></i></span>'
                ,'<span type="code" title="插入代码或引用"><i class="iconfont icon-emwdaima" style="top: 1px;"></i></span>'
                ,'<span type="bold" title="插入加粗字体"><i class="iconfont icon-jiacu"></i></span>'
                ,'<span type="center" title="居中"><i class="layui-icon layedit-tool-center">&#xe647;</i></span>'
                ,'<span type="hr" title="插入水平线">hr</span>'
                ,'<span type="yulan" title="预览"><i class="iconfont icon-yulan1"></i></span>'
                ,'</div>'].join('');

            var face2 = function(editor, self){ //插入表情
                var str = '', ul, face = fly.faces;
                for(var key in face){
                    str += '<li title="'+ key +'"><img src="'+ face[key] +'"></li>';
                }
                str = '<ul id="LAY-editface" class="layui-clear">'+ str +'</ul>';
                layer.tips(str, self, {
                    tips: 3
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
            };
            var log = {}, mod = {
                face: face2
                ,picture: function(editor){
                    layer.open({
                        type: 1
                        ,id: 'fly-jie-upload'
                        ,title: '插入图片'
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
                                ,size: 800
                                ,done: function(res){
                                    if(res.code === '0000'){
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
                ,href: function(editor){ //超链接
                    layer.prompt({
                        title: '请输入合法链接'
                        ,shade: false
                        ,fixed: false
                        ,id: 'LAY_flyedit_href'
                        ,offset: [
                            editor.offset().top - $(window).scrollTop() + 'px'
                            ,editor.offset().left + 'px'
                        ]
                    }, function(val, index, elem){
                        if(!/^http(s*):\/\/[\S]/.test(val)){
                            layer.tips('这根本不是个链接，不要骗我。', elem, {tips:1})
                            return;
                        }
                        layui.focusInsert(editor[0], ' a('+ val +')['+ val + '] ');
                        layer.close(index);
                    });
                }
                ,code: function(editor){ //插入代码
                    layer.prompt({
                        title: '请贴入代码或任意文本'
                        ,formType: 2
                        ,maxlength: 10000
                        ,shade: false
                        ,id: 'LAY_flyedit_code'
                        ,area: ['800px', '360px']
                    }, function(val, index, elem){
                        layui.focusInsert(editor[0], '[pre]\n'+ val + '\n[/pre]');
                        layer.close(index);
                    });
                }
                ,hr: function(editor){ //插入水平分割线
                    layui.focusInsert(editor[0], '[hr]');
                },
                bold:function (editor) {
                    layer.prompt({
                        title: '请输入需要加粗的文本'
                        ,formType: 2
                        ,maxlength: 10000
                        ,shade: false
                        ,id: 'LAY_flyedit_bold'
                        ,area: ['400px', '150px']
                    }, function(val, index, elem){
                        layui.focusInsert(editor[0], '[b]'+ val + '[/b]');
                        layer.close(index);
                    });
                },
                center:function (editor) {
                    layer.prompt({
                        title: '请输入需要居中的文本'
                        ,formType: 2
                        ,maxlength: 100
                        ,shade: false
                        ,id: 'LAY_flyedit_center'
                        ,area: ['400px', '150px']
                    }, function(val, index, elem){
                        layui.focusInsert(editor[0],' center['+ val + ']');
                        layer.close(index);
                    });
                }
                ,yulan: function(editor){ //预览
                    var content = editor.val();

                    content = /^\{html\}/.test(content)
                        ? content.replace(/^\{html\}/, '')
                        : fly.content(content);

                    layer.open({
                        type: 1
                        ,title: '预览'
                        ,shade: false
                        ,area: ['100%', '100%']
                        ,scrollbar: false
                        ,content: '<div class="detail-body" style="margin:20px;">'+ content +'</div>'
                    });
                }
            };

            layui.use('face', function(face){
                options = options || {};
                fly.faces = face;
                $(options.elem).each(function(index){
                    var that = this, othis = $(that), parent = othis.parent();
                    parent.prepend(html);
                    parent.find('.fly-edit span').on('click', function(event){
                        var type = $(this).attr('type');
                        mod[type].call(that, othis, this);
                        if(type === 'face'){
                            event.stopPropagation()
                        }
                    });
                });
            });
        }

        ,escape: function(html){
            return String(html||'').replace(/&(?!#?[a-zA-Z0-9]+;)/g, '&amp;')
                .replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/'/g, '&#39;').replace(/"/g, '&quot;');
        }

        ,content: function(content){
            var html = function(end){
                return new RegExp('\\['+ (end||'') +'(b|pre|hr|div|span|p|table|thead|th|tbody|tr|td|ul|li|ol|li|dl|dt|dd|h2|h3|h4|h5)([\\s\\S]*?)\\]', 'g');
            };
            content = fly.escape(content||'') //XSS
                .replace(/img\[([^\s]+?)\]/g, function(img){  //转义图片
                    return '<img src="' + img.replace(/(^img\[)|(\]$)/g, '') + '">';
                }).replace(/video\[([^\s]+?)\]/g, function(video){  //转义视频
                    return '<video controls src="' + video.replace(/(^video\[)|(\]$)/g, '') + '">您的浏览器不支持HTML5</video>';
                }).replace(/@(\S+)(\s+?|$)/g, '@<a href="javascript:;" class="fly-aite">$1</a>$2') //转义@
                .replace(/face\[([^\s\[\]]+?)\]/g, function(face){  //转义表情
                    var alt = face.replace(/^face/g, '');
                    return '<img alt="'+ alt +'" title="'+ alt +'" src="' + fly.faces[alt] + '">';
                }).replace(/a\([\s\S]+?\)\[[\s\S]*?\]/g, function(str){ //转义链接
                    var href = (str.match(/a\(([\s\S]+?)\)\[/)||[])[1];
                    var text = (str.match(/\)\[([\s\S]*?)\]/)||[])[1];
                    if(!href) return str;
                    var rel =  /^(http(s)*:\/\/)\b(?!(\w+\.)*(sentsin.com|layui.com))\b/.test(href.replace(/\s/g, ''));
                    return '<a href="'+ href +'" target="_blank"'+ (rel ? ' rel="nofollow"' : '') +'>'+ (text||href) +'</a>';
                }).replace(/center\[[\s\S]*?\]/g,function (str) {//转义居中
                    var centerStr = str.replace(/^center\[/g,'').replace(/\]/,'')
                    return '<p style="text-align: center">'+centerStr+'</p>'
                }).replace(html(), '\<$1 $2\>').replace(html('/'), '\</$1\>') //转移HTML代码
                .replace(/\n/g, '<br>') //转义换行
            return content;
        }

        ,newmsg: function(){
            var elemUser = $('.cwd-nav-user');
            if(layui.cache.user.userId !== 0 && elemUser[0]){
               fly.json('/u/msgsCount', function (result) {
                    if (result && result.code == '0000') {
                        if (result.data && result.data > 0) {
                            var msg = $('<a class="fly-nav-msg" href="javascript:;">'+ result.data +'</a>');
                            elemUser.append(msg);
                            msg.on('click', function(){
                                fly.json('/message/read', {}, function(res){
                                    if(res.code === '0000'){
                                        location.href = '/message/dialogs';
                                    }
                                });
                            });
                            layer.tips('你有 '+ result.data +' 条未读消息', msg, {
                                tips: 3
                                ,tipsMore: true
                                ,fixed: true
                            });
                            msg.on('mouseenter', function(){
                                layer.closeAll('tips');
                            })
                        }
                    }
                })
            }
            return arguments.callee;
        }
    };

    //加载扩展模块
    layui.config({
        base: fly.dir
    }).extend({
        face: 'face'
    });

    //签到
    var tplSignin = ['{{# if(d.signed){ }}'
        ,'<button class="layui-btn layui-btn-disabled">今日已签到</button>'
        ,'<span>获得了<cite>{{ d.experience }}</cite>趣豆</span>'
        ,'{{# } else { }}'
        ,'<button class="layui-btn layui-btn-danger" id="LAY_signin">今日签到</button>'
        ,'<span>可获得<cite>{{ d.experience }}</cite>趣豆</span>'
        ,'{{# } }}'].join('')
        ,tplSigninDay = '已连续签到<cite>{{ d.days }}</cite>天,本月漏签<cite>{{ d.forgetDays }}</cite>天'

        ,signRender = function(data){
            laytpl(tplSignin).render(data, function(html){
                elemSigninMain.html(html);
            });
            laytpl(tplSigninDay).render(data, function(html){
                elemSigninDays.html(html);
            });
            var signdaysHtml = '<table><tr>'
            layui.each(data.signDays,function (index,item) {
                if(index > 0 && index % 7 === 0){
                    signdaysHtml +='</tr><tr>'
                }
                signdaysHtml+='<td>'+item+'</td>';
            })
            signdaysHtml+='</tr></table>'
            $('#FQ_signdays').html(signdaysHtml)
        }

        ,elemSigninHelp = $('#LAY_signinHelp')
        ,elemSigninTop = $('#LAY_signinTop')
        ,elemSigninMain = $('.fly-signin-main')
        ,elemSigninDays = $('.fly-signin-days');

    if(elemSigninMain[0]){
        fly.json('/sign/status',function(res){
            if(!res.data) return;
            signRender(res.data);
        },{
        });
    }

    var commentInput = $('#fq-comment');
    if(commentInput[0]){
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
    }
    $('body').on('click', '#LAY_signin', function(){
        var othis = $(this);
        if(othis.hasClass(DISABLED)) return;
        fly.json('/sign/in', function(res){
            signRender(res.data);
        }, {
            error: function(){
                othis.removeClass(DISABLED);
            }
        });
        othis.addClass(DISABLED);
    });

    //签到说明
    elemSigninHelp.on('click', function(){
        layer.open({
            type: 1
            ,title: '签到说明'
            ,area: '300px'
            ,shade: 0.8
            ,shadeClose: true
            ,content: ['<div class="layui-text" style="padding: 20px;">'
                ,'<blockquote class="layui-elem-quote">“签到”可获得社区趣豆，规则如下</blockquote>'
                ,'<table class="layui-table">'
                ,'<thead>'
                ,'<tr><th>连续签到天数</th><th>每天可获趣豆</th></tr>'
                ,'</thead>'
                ,'<tbody>'
                ,'<tr><td>＜5</td><td>1</td></tr>'
                ,'<tr><td>≥5</td><td>5</td></tr>'
                ,'<tr><td>≥15</td><td>10</td></tr>'
                ,'<tr><td>≥30</td><td>20</td></tr>'
                ,'</tbody>'
                ,'</table>'
                ,'<ul>'
                ,'<li>中间若有间隔，则连续天数重新计算</li>'
                ,'<li style="color: #FF5722;">不可利用程序自动签到，否则趣豆清零</li>'
                ,'</ul>'
                ,'</div>'].join('')
        });
    });

    //签到活跃榜
    var tplSigninTop = ['{{# layui.each(d.data, function(index, item){ }}'
        ,'<li>'
        ,'<a href="/u/{{item.userId}}/peopleIndex" target="_blank">'
        ,'<img src="{{item.icon}}">'
        ,'<cite class="fly-link">{{item.nickname}}</cite>'
        ,'</a>'
        ,'{{# var date = new Date(item.time); if(d.index < 2){ }}'
        ,'<span class="fly-grey">签到于 {{ item.signTime }}</span>'
        ,'{{# } else { }}'
        ,'<span class="fly-grey">已连续签到 <i>{{ item.days }}</i> 天</span>'
        ,'{{# } }}'
        ,'</li>'
        ,'{{# }); }}'
        ,'{{# if(d.data.length === 0) { }}'
        ,'{{# if(d.index < 2) { }}'
        ,'<li class="fly-none fly-grey">今天还没有人签到</li>'
        ,'{{# } else { }}'
        ,'<li class="fly-none fly-grey">还没有签到记录</li>'
        ,'{{# } }}'
        ,'{{# } }}'].join('');

    elemSigninTop.on('click', function(){
        var loadIndex = layer.load(1, {shade: 0.8});
        fly.json('/sign/top/',{},function(res){
            var tpl = $(['<div class="layui-tab layui-tab-brief" style="margin: 5px 0 0;">'
                ,'<ul class="layui-tab-title">'
                ,'<li class="layui-this">最新签到</li>'
                ,'<li>今日最快</li>'
                ,'<li>总签到榜</li>'
                ,'</ul>'
                ,'<div class="layui-tab-content fly-signin-list" id="LAY_signin_list">'
                ,'<ul class="layui-tab-item layui-show"></ul>'
                ,'<ul class="layui-tab-item">2</ul>'
                ,'<ul class="layui-tab-item">3</ul>'
                ,'</div>'
                ,'</div>'].join(''))
                ,signinItems = tpl.find('.layui-tab-item');
            layer.close(loadIndex);
            layui.each(signinItems, function(index, item){
                var html = laytpl(tplSigninTop).render({
                    data: res.data[index]
                    ,index: index
                });
                $(item).html(html);
            });

            layer.open({
                type: 1
                ,title: '签到活跃榜 - TOP 20'
                ,area: '300px'
                ,shade: 0.8
                ,shadeClose: true
                ,id: 'layer-pop-signintop'
                ,content: tpl.prop('outerHTML')
            });
        },{
            error:function () {
                layer.close(loadIndex);
            }
        });
    });
    //回帖榜
    var tplReply = ['{{# layui.each(d.data, function(index, item){ }}'
        ,'<dd>'
        ,'<a href="/u/{{item.uid}}/peopleIndex">'
        ,'<img src="{{item.user.avatar}}">'
        ,'<cite>{{item.user.username}}</cite>'
        ,'<i>{{item["count(*)"]}}次回答</i>'
        ,'</a>'
        ,'</dd>'
        ,'{{# }); }}'].join('')
        ,elemReply = $('#LAY_replyRank');

    if(elemReply[0]){
        fly.json('/top/reply/', {
            limit: 20
        }, function(res){
            var html = laytpl(tplReply).render(res);
            elemReply.find('dl').html(html);
        });
    };

    var tplVisit = ['{{# layui.each(d.data, function(index, item){ }}'
        ,'<li>'
        ,'<a href="/u/{{item.visitUserId}}/peopleIndex" target="_blank">'
        ,'<img src="{{item.visitorIcon}}">'
        ,'<cite class="c-fly-link">{{item.visitorName}}</cite>'
        ,'</a>'
        ,'{{# var date = new Date(item.visitTime); }}'
        ,'<span class="fly-grey"> {{ layui.laytpl.digit(date.getMonth()+1) + "月" + layui.laytpl.digit(date.getDate())+"日" }}</span>'
        ,'</li>'
        ,'{{# }); }}'
        ,'{{# if(d.data.length === 0) { }}'
        ,'<li class="fly-none fly-grey">还没有人访问哦</li>'
        ,'{{# } }}'].join('')
        ,elemVisit = $('#visit');

    if(elemVisit[0]){
        fly.json('/visit/records/1',{},function(res){
            var list = res.data.list,total = res.data.total;
            var html = laytpl(tplVisit).render({
                data: list
            });
            $('.visitNum').html(total)
            $('#visit').html(html)
        },{type:"get"})
    }

    //相册
    if($(window).width() > 750){
        layer.photos({
            photos: '.photos'
            ,zIndex: 9999999999
            ,anim: 5
        });
    } else {
        $('body').on('click', '.photos img', function(){
            window.open(this.src);
        });
    }

    //新消息通知
    fly.newmsg();

    //发送激活邮件
    fly.activate = function(email){
        fly.json('/api/activate/', {}, function(res){
            if(res.status === 0){
                layer.alert('已成功将激活链接发送到了您的邮箱，接受可能会稍有延迟，请注意查收。', {
                    icon: 1
                });
            };
        });
    };
    $('#LAY-activate').on('click', function(){
        fly.activate($(this).attr('email'));
    });

    $('body').on('click', '.fly-aite', function(){
        var othis = $(this), text = othis.text();
        if(othis.attr('href') !== 'javascript:;'){
            return;
        }
        text = text.replace(/^@|（[\s\S]+?）/g, '');
        othis.attr({
            href: '/jump?username='+ text
            ,target: '_blank'
        });
    });

    form.on('submit(*)', function(data){
        var action = $(data.form).attr('action'), button = $(data.elem);
        fly.json(action, data.field, function(res){
            var end = function(){
                if(res.action){
                    location.href = res.action;
                } else {
                    fly.form[action||button.attr('key')](data.field, data.form);
                }
            };
            if(res.code === '0000'){
                button.attr('alert') ? layer.alert(res.message, {
                    icon: 1,
                    time: 10*1000,
                    end: end
                }) : end();
            };
        });
        return false;
    });

    //加载特定模块
    if(layui.cache.page && layui.cache.page !== 'index'){
        var extend = {};
        extend[layui.cache.page] = layui.cache.page;
        layui.extend(extend);
        layui.use(layui.cache.page);
    }

    //加载IM
    if(!device.android && !device.ios){
        //layui.use('im');
    }

    //加载编辑器
    fly.layEditor({
        elem: '.fly-editor'
    });

    //手机设备的简单适配
    var treeMobile = $('.site-tree-mobile')
        ,shadeMobile = $('.site-mobile-shade')

    treeMobile.on('click', function(){
        $('body').addClass('site-mobile');
    });

    shadeMobile.on('click', function(){
        $('body').removeClass('site-mobile');
    });

    //固定Bar
    util.fixbar({
        bar1: true
        ,bgcolor: '#009688'
        ,click: function(type){
            if(type === 'bar1'){
                location.href = '/question';
            }
        }
    });

    exports('fly', fly);
});

