/**
 * Created by cwd on 2018/1/17.
 */
layui.define(['fly','table','element','laypage'],function (exports) {
    var $ = layui.jquery;
    var layer = layui.layer;
    var util = layui.util;
    var laytpl = layui.laytpl;
    var form = layui.form;
    var laypage = layui.laypage;
    var fly = layui.fly;
    var element = layui.element;
    var table = layui.table;

    if($('#articleCollectioncard')[0]){
        fly.json('/collection/find/article', function(res){
            table.render({
                elem: '#articleCollectioncard'
                ,data: res.data
                ,cols: [[
                    {field: 'articleTitle', title: '文章标题', minWidth: 300, templet: '<div><a href="/article/{{d.topicId}}" target="_blank" class="layui-table-link">{{d.articleTitle}}</a></div>'
                    }
                    ,{field: 'collectTime', title: '收藏时间', width: 200, align: 'center', templet: '<div>{{d.collectTime}}</div>'
                    }
                ]]
                ,page: true
                ,skin: 'line'
            });
        });
    }
    if($('#thoughtCollectioncard')[0]){
        fly.json('/collection/find/thought', function(res){
            table.render({
                elem: '#thoughtCollectioncard'
                ,data: res.data
                ,cols: [[
                    {field: 'content', title: '想法详情', minWidth: 300, templet: '<div><a href="/thought/{{d.topicId}}" target="_blank" class="layui-table-link">{{d.content}}</a></div>'
                    }
                    ,{field: 'collectTime', title: '收藏时间', width: 200, align: 'center', templet: '<div>{{d.collectTime}}</div>'
                    }
                ]]
                ,page: true
                ,skin: 'line'
            });
        });
    }

    exports('collection',null)
})
