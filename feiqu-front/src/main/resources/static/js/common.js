/**
 * Created by Administrator on 2017/10/28.
 */
//share
function share(){
    document.writeln("<div class=\"bdsharebuttonbox\" style=\"margin-top:3px;\"><a href=\"#\" class=\"bds_more\" data-cmd=\"more\"></a><a href=\"#\" class=\"bds_qzone\" data-cmd=\"qzone\" title=\"分享到QQ空间\"></a><a href=\"#\" class=\"bds_tsina\" data-cmd=\"tsina\" title=\"分享到新浪微博\"></a><a href=\"#\" class=\"bds_tqq\" data-cmd=\"tqq\" title=\"分享到腾讯微博\"></a><a href=\"#\" class=\"bds_tieba\" data-cmd=\"tieba\" title=\"分享到百度贴吧\"></a><a href=\"#\" class=\"bds_weixin\" data-cmd=\"weixin\" title=\"分享到微信\"></a><a class=\"bds_count\" data-cmd=\"count\"></a></div><script>window._bd_share_config={\"common\":{\"bdSnsKey\":{},\"bdText\":\"\",\"bdMini\":\"1\",\"bdMiniList\":false,\"bdPic\":\"\",\"bdStyle\":\"0\",\"bdSize\":\"16\"},\"share\":{}};with(document)0[(getElementsByTagName('head')[0]||body).appendChild(createElement('script')).src='http://bdimg.share.baidu.com/static/api/js/share.js?v=89860593.js?cdnversion='+~(-new Date()/36e5)];</script>");
}


var elemUser = $('.fly-nav-user');
if(layui.cache.user.uid !== -1 && elemUser[0]){
    fly.json('/message/nums/', {
        _: new Date().getTime()
    }, function(res){
        if(res.status === 0 && res.count > 0){
            var msg = $('<a class="fly-nav-msg" href="javascript:;">'+ res.count +'</a>');
            elemUser.append(msg);
            msg.on('click', function(){
                fly.json('/message/read', {}, function(res){
                    if(res.status === 0){
                        location.href = '/user/message/';
                    }
                });
            });
            layer.tips('你有 '+ res.count +' 条未读消息', msg, {
                tips: 3
                ,tipsMore: true
                ,fixed: true
            });
            msg.on('mouseenter', function(){
                layer.closeAll('tips');
            })
        }
    });
}




var commentIframeIndex = layer.open({
            title: '评论列表',
            type: 2,
            area: ['70%', '70%'],
            content: '${ctxPath}/thought/commentList?thoughtId=' + obj
        })
        layer.iframeAuto(commentIframeIndex)