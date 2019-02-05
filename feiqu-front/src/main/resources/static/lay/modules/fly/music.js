layui.define(['fly'],function (exports) {
    var $ = layui.jquery,layer = layui.layer, fly = layui.fly;
    var active = {
        praise: function(othis){
            var PRIMARY = 'layui-btn-primary'
                ,unpraise = !othis.hasClass(PRIMARY)
                ,numElem = $('.fly-beauty-nums')
            fly.json('/music/like', {
                musicId: $(othis).parent().data('id')
                ,unpraise: unpraise ? true : null
            }, function(res){
                numElem.html(res.data);
                if(unpraise){
                    othis.addClass(PRIMARY).html('点赞');
                    layer.tips('少了个赞囖', numElem, {
                        tips: 1
                    });
                } else {
                    othis.removeClass(PRIMARY).html('已赞');
                    layer.tips('成功获得个赞', numElem, {
                        tips: [1, '#FF5722']
                    });
                }
            });
        }
    },mp3 = $('.c-mp3'),progressbar=$('.progressbar'),bar = $('.bar'),currTime = $('.currTime'),totalTime = $('.totalTime')

    mp3.bind("timeupdate", function () {
        var currentTime = this.currentTime,duration = this.duration;
        var value = currentTime / duration * 100;
        bar.css({width:value+"%"})
        currTime.html(parseTime(currentTime));
        totalTime.html(parseTime(duration))
    });

    $('body').on('click', '.fly-case-active', function(){
        var othis = $(this), type = othis.data('type');
        active[type] && active[type].call(this, othis);
    });

    var played = false;

    var vedioControl = {
        play:function (video) {
            var othis = $(this);
            if(!played){
                fly.json('/music/play',{musicId:othis.parent().data('id')})
                $('.fly-beauty-nums').html(parseInt($('.fly-beauty-nums').html())+1);
            }
            played = true;
            video.volume = 0.5;
            video.play();
            video.loop = true;
            othis.attr('type','pause');
            othis.html('暂停')
        }
        ,pause:function (video) {
            video.pause();
            var othis = $(this);
            othis.attr('type','play');
            othis.html('播放')
        }
        ,addVolume:function (video) {
            var volume = video.volume;
            if(volume !== 1.0){
                if(volume >= 0.9 && volume <=1){ video.volume = 1;return}
                video.volume = volume + 0.1
            }
        }
        ,reduceVolume:function (video) {
            var volume = video.volume;
            if(volume !== 0.0){
                if(volume >= 0 && volume <=0.1){ video.volume = 0;return}
                video.volume = volume - 0.1
            }
        }
        ,mute:function (video) {
            var othis = $(this)
            video.muted = true
            othis.attr('type','cancelMute')
            othis.html('取消静音')
        }
        ,cancelMute:function (video) {
            var othis = $(this)
            video.muted = false
            othis.attr('type','mute')
            othis.html('静音')
        }
    }
    $('.control span').on('click',function () {
        var othis = $(this),type=othis.attr('type')
        vedioControl[type].call(this,mp3[0])
    })
    function parseTime(time) {
        var min = String(parseInt(time / 60)),
            sec = String(parseInt(time % 60));
        if (min.length == 1) min = "0" + min;
        if (sec.length == 1) sec = "0" + sec;
        return min + ":" + sec;
    }
    $('.lyric').each(function(){
        var othis = $(this), html = othis.html();
        othis.html(fly.content(html));
    });
    exports('music',null);
})