layui.define(['guardui'], function (exports) {
    var $ = layui.jquery;
    var element = layui.element;
    var guardui = layui.guardui;
    var form = layui.form;

    var CSSPacker = {
        format: function (s) {//格式化代码
            s = s.replace(/\s*([\{\}\:\;\,])\s*/g, "$1");
            s = s.replace(/;\s*;/g, ";"); //清除连续分号
            s = s.replace(/\,[\s\.\#\d]*{/g, "{");
            s = s.replace(/([^\s])\{([^\s])/g, "$1 {\n\t$2");
            s = s.replace(/([^\s])\}([^\n]*)/g, "$1\n}\n$2");
            s = s.replace(/([^\s]);([^\s\}])/g, "$1;\n\t$2");
            return s;
        },
        pack: function (s) {//高级
            s = s.replace(/\/\*(.|\n)*?\*\//g, ""); //删除注释
            s = s.replace(/\s*([\{\}\:\;\,])\s*/g, "$1");
            s = s.replace(/\,[\s\.\#\d]*\{/g, "{"); //容错处理
            s = s.replace(/;\s*;/g, ";"); //清除连续分号
            s = s.replace(/;\s*}/g, "}"); //清除末尾分号和大括号
            s = s.match(/^\s*(\S+(\s+\S+)*)\s*$/); //去掉首尾空白
            return (s == null) ? "" : s[1];
        },
        packNor: function (s) {//普通
            s = s.replace(/\/\*(.|\n)*?\*\//g, ""); //删除注释
            s = s.replace(/\s*([\{\}\:\;\,])\s*/g, "$1");
            s = s.replace(/\,[\s\.\#\d]*\{/g, "{"); //容错处理
            s = s.replace(/;\s*;/g, ";"); //清除连续分号
            s = s.replace(/;\s*}/g, "}"); //清除末尾分号和大括号
            s = s.replace(/([^\s])\{([^\s])/g, "$1{$2");
            s = s.replace(/([^\s])\}([^\n]s*)/g, "$1}\n$2");
            return s;
        }
    }

    var gatherformat = {
        empty: function () {
            $("input[type='text']").val("");
            $("textarea").val("");
            form.render();
        }
        , do_js_beautify: function () {
            js_source = $('#json_input').val().replace(/^\s+/, '');
            tabsize = $('#tabsize').val();
            tabchar = ' ';
            if (tabsize == 1) {
                tabchar = '\t';
            }
            if (js_source && js_source.charAt(0) === '<') {
                $("#json_input").val(style_html(js_source, tabsize, tabchar, 80));
            } else {
                $("#json_input").val(js_beautify(js_source, tabsize, tabchar));
            }
        }
        , pack_js: function (base64) {
            var input = $('#json_input').val().replace(/^\s+|\s+$/g, "");
            if (input == '') {
                guardui.errmsg('请输入需要压缩的内容!');
                return;
            }

            var packer = new Packer;
            if (base64) {
                var output = packer.pack(input, 1, 0);
            } else {
                var output = packer.pack(input, 0, 0);
            }
            $("#json_input").val(output);
        }
        , CSS: function (s) {
            $("#json_input").val(CSSPacker[s]($("#json_input").val()));
        }
        , demo_xml: function () {
            $("#json_input").val('<?xml version="1.0" encoding="UTF-8"?><PARAM><DBID>35</DBID><SEQUENCE>atgtca</SEQUENCE><MAXNS>10</MAXNS><MINIDENTITIES>90</MINIDENTITIES><MAXEVALUE>10</MAXEVALUE><USERNAME>admin</USERNAME><PASSWORD>111111</PASSWORD><TYPE>P</TYPE><RETURN_TYPE>2</RETURN_TYPE></PARAM>');
        }
        , format_xml: function () {
            $('#json_input').format({ method: 'xml' });
        }
        , yasuo_xml: function () {
            $('#json_input').format({ method: 'xmlmin' });
        }
        , jsbeauty: function () {
            var source = jQuery('#txtInitCode').val().trim(),
                output,
                opts = {};

            opts.indent_size = 4;
            opts.indent_char = ' ';
            opts.max_preserve_newlines = 5;
            opts.preserve_newlines = opts.max_preserve_newlines !== "-1";
            opts.keep_array_indentation = false;
            opts.break_chained_methods = false;
            opts.indent_scripts = 'normal';
            opts.brace_style = 'collapse';
            opts.space_before_conditional = true;
            opts.unescape_strings = false;
            opts.jslint_happy = false;
            opts.wrap_line_length = 0;
            opts.space_after_anon_function = true;
            source = gatherformat.unpacker_filter(source);
            output = js_beautify(source, opts);
            $('#txtResultCode').val(output);
            if ($("#txtResultCode").val()) $("#txtResultCode").siblings("b").hide();
        }
        , filtercomment: function () {
            var code = $('#txtInitCode').val().trim();
            var tmp1 = ':\/\/';
            var regTmp1 = /:\/\//g;
            var tmp2 = '@:@/@/@';
            var regTmp2 = /@:@\/@\/@/g;
            code = code.replace(regTmp1, tmp2);
            var reg = /(\/\/.*)?|(\/\*[\s\S]*?\*\/)/g;
            code = code.replace(reg, '');
            result = code.replace(regTmp2, tmp1);
            $("#txtResultCode").val(result);
        }
        , basiccompress: function () {
            var input = $('#txtInitCode').val().replace(/^\s+|\s+$/g, "");
            if (input == '') {
                guardui.errmsg('请输入需要压缩的内容!');
                return;
            }
            var packer = new Packer;
            var output = packer.pack(input, 0, 0);
            $("#txtResultCode").val(output);
        }
        , encodecompress: function () {
            var input = $('#txtInitCode').val().replace(/^\s+|\s+$/g, "");
            if (input == '') {
                guardui.errmsg('请输入需要压缩加密的内容!');
                return;
            }
            var packer = new Packer;
            var output = packer.pack(input, 1, 0);
            $("#txtResultCode").val(output);
        }
        , decodebeauty: function () {
            var source = $('#txtInitCode').val().trim(),
                output,
                opts = {};

            opts.indent_size = 4;
            opts.indent_char = ' ';
            opts.max_preserve_newlines = 5;
            opts.preserve_newlines = opts.max_preserve_newlines !== "-1";
            opts.keep_array_indentation = false;
            opts.break_chained_methods = false;
            opts.indent_scripts = 'normal';
            opts.brace_style = 'collapse';
            opts.space_before_conditional = true;
            opts.unescape_strings = false;
            opts.jslint_happy = false;
            opts.wrap_line_length = 0;
            opts.space_after_anon_function = true;
            source = gatherformat.unpacker_filter(source);
            output = js_beautify(source, opts);
            $('#txtResultCode').val(output);
            if ($("#txtResultCode").val()) $("#txtResultCode").siblings("b").hide();
        }
        , unpacker_filter: function (source) {
            var trailing_comments = '',
                comment = '',
                unpacked = '',
                found = false;

            do {
                found = false;
                if (/^\s*\/\*/.test(source)) {
                    found = true;
                    comment = source.substr(0, source.indexOf('*/') + 2);
                    source = source.substr(comment.length).replace(/^\s+/, '');
                    trailing_comments += comment + "\n";
                } else if (/^\s*\/\//.test(source)) {
                    found = true;
                    comment = source.match(/^\s*\/\/.*/)[0];
                    source = source.substr(comment.length).replace(/^\s+/, '');
                    trailing_comments += comment + "\n";
                }
            } while (found);

            var unpackers = [P_A_C_K_E_R, Urlencoded, MyObfuscate];
            for (var i = 0; i < unpackers.length; i++) {
                if (unpackers[i].detect(source)) {
                    unpacked = unpackers[i].unpack(source);
                    if (unpacked != source) {
                        source = this.unpacker_filter(unpacked);
                    }
                }
            }
            return trailing_comments + source;
        }
    };

    exports('formatmoudle', gatherformat);
});/**
 * Created by Administrator on 2017/12/31.
 */
