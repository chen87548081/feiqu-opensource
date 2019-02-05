layui.define(['layer', 'laytpl', 'laydate', 'form', 'laypage', 'element', 'code'], function (exports) {
    var $ = layui.jquery
        , layer = layui.layer
        , laytpl = layui.laytpl
        , code = layui.code({ about: false })
        , laydate = layui.laydate
        , form = layui.form
        , laypage = layui.laypage
        , element = layui.element;

    function key(e) {
        var keynum;
        if (window.event) {
            keynum = e.keyCode;
        } else if (e.which) {
            keynum = e.which;
        }

        if (keynum == 17) {
            return false;
        }
    }

    //禁用右键、文本选择、复制
    $(document).bind("contextmenu", function (e) { return false; });
    $(document).bind("selectstart", function () { return false; });
    $(document).keydown(function () { return key(arguments[0]) });

    var pathname = location.pathname.toLowerCase();
    console.log(pathname);
    var pathsplit = pathname.split('/');
    if (pathsplit.length > 2) {
        $("#Left_" + pathsplit[1]).removeClass("layui-hide").siblings().addClass("layui-hide");
    } else {
        pathsplit[1] = "home";
        $("#Left_" + pathsplit[1]).removeClass("layui-hide").siblings().addClass("layui-hide");
    }

    $("ul[id='Left_" + pathsplit[1] + "'] li a").each(function () {
        if (pathname == $(this).attr("href").toLowerCase()) {
            $(this).parent().addClass("layui-this");
        }
    });

    $("li[id='Head_li_" + pathsplit[1] + "']").addClass("layui-this");


    if ($("ul[id='Encrypt_Home']") && $("ul[id='Encrypt_Home']").length > 0) {
        $("ul[id='Encrypt_Home'] li a").each(function () {
            if (pathname == $(this).attr("href").toLowerCase()) {
                $(this).parent().addClass("layui-this");
            } else if (pathname == "/encrypt/index") {
                $("ul[id='Encrypt_Home'] li:eq(0)").addClass("layui-this");
            }
            $("ul[id='Left_" + pathsplit[1] + "'] li dl dd:eq(0)").addClass("layui-this");
        });
    }

    $("ul[id='Left_" + pathsplit[1] + "']").find("a[href='" + pathname + "']").parent().addClass("layui-this");

    if ($("ul[id='Encrypt_Home']") && $("ul[id='Encrypt_Home']").length > 0) {
        $("ul[id='Encrypt_Home'] li a").each(function () {
            $("ul[id='Left_" + pathsplit[1] + "'] li dl dd:eq(0)").addClass("layui-this");
        });
    }

    if ($("ul[id='Crypt_Home']") && $("ul[id='Crypt_Home']").length > 0) {
        $("ul[id='Crypt_Home'] li a").each(function () {
            $("ul[id='Left_" + pathsplit[1] + "'] li dl dd:eq(14)").addClass("layui-this");
        });
    }

    $("li[id='Head_li_" + pathsplit[1] + "']").addClass("layui-this");
    $("ul[id='Encrypt_Home'] li").find("a[href='" + pathname + "']").parent().addClass("layui-this");
    $("ul[id='Crypt_Home'] li").find("a[href='" + pathname + "']").parent().addClass("layui-this");

    var gather = {
        postJSON: function (optionstype, url, data, async, doneback, failback, alwaysback) {
            return $.ajax({
                url: url,
                type: optionstype,
                data: data,
                dataType: 'json',
                async: async,
                beforeSend: function () {
                    layer.load(0);
                },
                timeout: 10000
            }).done(function (data) {
                if (doneback != undefined) {
                    doneback(data);
                }
                layer.closeAll('loading');
            }).fail(function (data) {
                layer.closeAll('loading');
                layer.msg("失败！", { icon: 2, time: 1500, shade: [0.5, '#000'] });
                if (failback != undefined) {
                    failback(data);
                }
            }).always(function (data) {
                if (alwaysback != undefined) {
                    alwaysback(data);
                }
            });
        }
        , getJSON: function (optionstype, url, data, async, doneback, failback, alwaysback) {
            return $.ajax({
                url: url,
                type: optionstype,
                data: data,
                dataType: 'json',
                contentType: "application/json; charset=utf-8",
                async: async,
                beforeSend: function () {
                    layer.load(0);
                }
            }).done(function (data) {
                if (doneback != undefined) {
                    doneback(data);
                }
                layer.closeAll('loading');
            }).fail(function (data) {
                layer.closeAll('loading');
                layer.msg("失败！", { icon: 2, time: 1500, shade: [0.5, '#000'] });
                if (failback != undefined) {
                    failback(data);
                }
            }).always(function (data) {
                if (alwaysback != undefined) {
                    alwaysback(data);
                }
            });
        }
        , guarduiOpen: function (obj, type, res, yescallback, successback) {
            var width = $(obj).attr("guardui-openwidth") || res.width;
            var height = $(obj).attr("guardui-openheight") || res.height;
            var okname = $(obj).attr("guardui-openbtn") || res.okname;
            if (type == 1) {
                layer.open({
                    id: 'layer-openLogin',
                    type: 2,
                    skin: 'layui-layer-demo',
                    closeBtn: 1,
                    anim: 2,
                    title: $(obj).attr("guardui-opentitle") || res.title,
                    area: [width, height],
                    content: $(obj).attr("guardui-openurl") || res.url
                });
            } else if (type == 2) {
                layer.open({
                    id: 'layer-openLogin',
                    type: 2,
                    skin: 'layui-layer-demo',
                    closeBtn: 1,
                    anim: 2,
                    title: $(obj).attr("guardui-opentitle") || res.title,
                    area: [width, height],
                    content: $(obj).attr("guardui-openurl") || res.url,
                    btn: [okname, '取消'],
                    yes: function (index, layero) {
                        if (yescallback != undefined) {
                            yescallback(index, layero);
                        }
                    },
                    success: function (layero, index) {
                        if (successback != undefined) {
                            successback(layero, index);
                        }
                    }
                });
            }
        }
        , guarduiPage: function (totalcount, pagesize, curr, callback) {
            var totalpg = totalcount % pagesize == 0 ? totalcount / pagesize : totalcount / pagesize + 1;
            laypage({
                cont: "Page", //容器。值支持id名、原生dom对象，jquery对象,
                pages: totalpg, //总页数
                skin: 'molv',//皮肤
                skip: false,//是否开启跳页
                curr: curr || 1,//当前页
                jump: function (e, first) { //触发分页后的回调
                    if (!first) { //点击跳页触发函数自身，并传递当前页：e.curr
                        callback(e.curr);
                    }
                }
            });
        }
        , successmsg: function (message, fn) {
            layer.msg(message, { icon: 1, time: 1500, shade: [0.5, '#000'] }, function () {
                if (fn != undefined) {
                    fn();
                }
            });
        }
        , errmsg: function (message, fn) {
            layer.msg(message, { icon: 2, time: 1500, shade: [0.5, '#000'] }, function () {
                if (fn != undefined) {
                    fn();
                }
            });
        }
        , copyArticle: function (id) {
            const range = document.createRange();
            range.selectNode(document.getElementById(id));

            const selection = window.getSelection();
            if (selection.rangeCount > 0) selection.removeAllRanges();
            selection.addRange(range);

            document.execCommand('copy');
            layer.msg("复制成功", { icon: 1, time: 1500, shade: [0.5, '#000'] });
        }
        , Receivables: function (obj) {
            gather.guarduiOpen(obj, 1, {
                title: "赞助",
                url: "/Receivables.html",
                width: "400px",
                height: "600px",
                okname: ""
            });
        }
        , indexNew: function (obj) {
            gather.guarduiOpen(obj, 1, {
                title: "JSON在线解析（new）",
                url: "/index_new.html",
                width: "100%",
                height: "100%",
                okname: ""
            });
        }
        , ExportJson: function (id) {
            var saveval = $("#" + id).val();
            var blob = new Blob([saveval], { type: "text/plain;charset=utf-8" });
            saveAs(blob, "guardui_out_code.json");
        }
        , download: function (id, filename) {
            var content = $('#' + id).val();
            var blob = new Blob([content], { type: "text/plain;charset=utf-8" });
            saveAs(blob, filename);
        }
        , downloadmd: function (content, filename) {
            var blob = new Blob([content], { type: 'text/x-markdown;charset=utf-8' });
            saveAs(blob, filename);
        }
        , downloadhtml: function (content, filename) {
            var text = '<!doctype html>'
                + '<html>'
                + '<head><meta charset="utf-8"><title>GuiTools在线工具</title><meta http-equiv="X-UA-Compatible" content="IE=edge">'
                + '<meta name="viewport" content="width=device-width, initial-scale=1.0">'
                + '<link href="http://tools.guardui.net/Content/plugin/mkd/css/editormd.css" rel="stylesheet" /></head>'
                + '<body style="width:95%;"><div id="container" class="markdown-body editormd-preview-container">'
                + content
                + '</div></body></html>';

            var blob = new Blob([text], { type: 'text/html;charset=utf-8' });
            saveAs(blob, filename);
        }
    };

    //复制
    if ($("#btnCopyCode") && $("#btnCopyCode").length > 0) {
        function copyArticle(event) {
            const range = document.createRange();
            range.selectNode(document.getElementById($("#btnCopyCode").attr("data-clipboard-target")));

            const selection = window.getSelection();
            if (selection.rangeCount > 0) selection.removeAllRanges();
            selection.addRange(range);

            document.execCommand('copy');
            layer.msg("复制成功", { icon: 1, time: 1500, shade: [0.5, '#000'] });
        }

        document.getElementById("btnCopyCode").addEventListener('click', copyArticle, false);
    };

    exports('guardui', gather);
});/**
 * Created by Administrator on 2017/12/31.
 */
