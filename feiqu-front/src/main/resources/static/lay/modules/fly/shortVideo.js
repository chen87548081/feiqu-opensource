//一般直接写在一个js文件中
layui.define(['fly'],function (exports) {
    var $ = layui.jquery,layer = layui.layer, fly = layui.fly, form = layui.form
        ,laypage=layui.laypage
        ,flow = layui.flow
        ,upload = layui.upload,device=layui.device;

    $(".video-list").on('click',function () {
        var me = $(this);
        var url = me.attr('data-video');
        var li = me.parents('li');
        var media = li.find('.media-box');
        media.html('<video src="'+url+'" controls style="background:#000"></video>');
    })
    $('.video-hide').on('click',function () {
        var me = $(this);
        var li = me.parents('li');
        var media = li.find('.media-box');
        media.html('');
    })

    var accessid = '', accesskey = '', host = '', policyBase64 = '', signature = '', callbackbody = '',
        filename = '', key = '', expire = 0, g_object_name = '', myDomain = '', now = timestamp = Date.parse(new Date()) / 1000;

    var uploadAdmin = {
        get_signature:function () {
            now = timestamp = Date.parse(new Date()) / 1000;
            if (expire < now + 3)
            {
                $.ajax({url: "/aliyun/oss/policy", type: "GET",dataType:'json', async: false,success:function (res) {
                    obj = res;
                    host = obj['host']
                    policyBase64 = obj['policy']
                    accessid = obj['OSSAccessKeyId']
                    signature = obj['signature']
                    expire = parseInt(obj['expire'])
                    callbackbody = obj['callback']
                    key = obj['dir']
                    myDomain = obj['myDomain']
                    return true;
                }});
            }
            return false;
        },
        get_suffix:function(filename) {
            pos = filename.lastIndexOf('.');
            suffix = '';
            if (pos !== -1) {
                suffix = filename.substring(pos)
            }
            return suffix;
        },
        calculate_object_name:function (filename) {
            g_object_name += '${filename}'
        },
        get_uploaded_object_name:function(filename)
        {
            tmp_name = g_object_name;
            tmp_name = tmp_name.replace("${filename}", filename);
            return tmp_name;
        },
        set_upload_param:function (up, filename, ret){
            if (ret === false)
            {
                ret = this.get_signature()
            }
            g_object_name = key;
            if (filename !== '') {
                suffix = this.get_suffix(filename)
                this.calculate_object_name(filename)
            }
            var new_multipart_params = {
                'key' : g_object_name,
                'policy': policyBase64,
                'OSSAccessKeyId': accessid,
                'success_action_status' : '200',
                'callback' : callbackbody,
                'signature': signature,
            };
            up.setOption({
                'url': host,
                'multipart_params': new_multipart_params
            });
            up.start();
        }
    }

    var uploader = new plupload.Uploader({
        runtimes : 'html5,flash,silverlight,html4',
        browse_button : 'shortVideo',
        container: document.getElementById('uploadContainer'),
        flash_swf_url : 'lib/plupload-2.1.2/js/Moxie.swf',
        silverlight_xap_url : 'lib/plupload-2.1.2/js/Moxie.xap',
        url : 'http://oss.aliyuncs.com',
        filters: {
            mime_types : [
                { title : "视频文件", extensions : "avi,mp4,wma,rmvb,rm,flash,3gp,flv" }
            ],
            max_file_size : '20mb',
            prevent_duplicates : true
        },

        init: {
            PostInit: function() {
                document.getElementById('postfiles').onclick = function() {
                    uploadAdmin.set_upload_param(uploader, '', false);
                    return false;
                };
            },

            FilesAdded: function(up, files) {
                plupload.each(files, function(file) {
                    document.getElementById('videoProgress').innerHTML += '<div id="' + file.id + '">' + file.name + ' (' + plupload.formatSize(file.size) + ')<b></b>'
                        +'<div class="progress"><div class="progress-bar" style="width: 0%"></div></div>'
                        +'</div>';
                });
            },

            BeforeUpload: function(up, file) {
                uploadAdmin.set_upload_param(up, file.name, true);
            },

            UploadProgress: function(up, file) {
                var d = document.getElementById(file.id);
                d.getElementsByTagName('b')[0].innerHTML = '<span>' + file.percent + "%</span>";
                var prog = d.getElementsByTagName('div')[0];
                var progBar = prog.getElementsByTagName('div')[0]
                progBar.style.width= 2*file.percent+'px';
                progBar.setAttribute('aria-valuenow', file.percent);
            },
            FileUploaded: function(up, file, info) {
                if (info.status === 200)
                {
                    var videoUrl = host+'/'+uploadAdmin.get_uploaded_object_name(file.name);
                    $('#uploadedVideo').html('<video controls src="'+videoUrl+'"></video>');
                    var data = {
                        url:videoUrl,
                        title:$('#videoTitle').val()
                    }
                    fly.json('/shortVideo/add',data,function (res) {
                        layer.msg('上传成功！',{icon:6})
                    })
                }
                else
                {
                    layer.msg(info.response,{icon: 6});
                }
            },
            Error: function(up, err) {
                if (err.code === -600) {
                    layer.msg('选择的文件太大了,可以根据应用情况，在upload.js 设置一下上传的最大大小',{icon: 5});
                }
                else if (err.code === -601) {
                    layer.msg('选择的文件后缀不对,可以根据应用情况，在upload.js进行设置可允许的上传文件类型',{icon: 5});
                }
                else if (err.code === -602) {
                    layer.msg('这个文件已经上传过一遍了',{icon: 5});
                }
                else
                {
                    layer.msg("Error xml:" + err.response,{icon: 5});
                }
            }
        }
    });
    uploader.init();
    $('#videoTitle').focus();

    exports('shortVideo',null);
});/**
 * Created by chenweidong on 2018/10/28.
 */
