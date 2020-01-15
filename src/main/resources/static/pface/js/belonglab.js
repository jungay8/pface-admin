
function delLab(_id){
    console.log(_id);
    //var myid=_id;
    layer.confirm('您确定要删除吗?', {btn: ['确定', '取消'], title: "提示"}, function () {
        console.log(_id);
        var ids = [];
        ids.push(_id)
        $.ajax({
            type: "POST",
            url: CONTEXT_PATH+'/front/belonglab/logic-delete-batch',
            async: true,   //是否为异步请求
            cache: false,  //是否缓存结果
            data: {
                //id:ids.toString()
               id:ids
            },
            traditional: true,
            dataType:"json",
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
                //'Content-Type': 'application/json;charset=UTF-8'
            },
            success:function(data){
                console.log(data);
                if(data.success){
                    //storageSessionObj("user",res.data.data);
                    layer.msg("删除成功");
                    //window.location.href = CONTEXT_PATH+res.data.extra.url;
                    qry($("#pageNumKey").val(),$("#pageSizeKey").val());
                }else{
                    //$.alert(data.msg);
                    layer.msg("删除失败");
                }
            },
            error:function (res) {
                console.log(res);
                layer.msg("删除失败");
            }
        });
    });

}
//弹出窗口表单样式pageNum
layui.use(['form','layer','laypage'], function(){
    var $ = layui.jquery,
        form = layui.form,
        layer=layui.layer,
        laypage = layui.laypage;
//自定义首页、尾页、上一页、下一页文本
    laypage.render({
        elem: 'pageNum'
        ,limit:10
        ,limits:[10, 20, 30, 40, 50]
        ,curr:page
        ,count: totalRecords
        ,layout: ['count', 'prev', 'page', 'next', 'limit', 'refresh', 'skip']
        ,first: '首页'
        ,last: '尾页'
        ,prev: '<em>上一页</em>'
        ,next: '<em>下一页</em>'
        ,jump: function(obj, first) {
            //obj包含了当前分页的所有参数，比如：
            //console.log(obj.curr); //得到当前页，以便向服务端请求对应页的数据。
            //console.log(obj.limit); //得到每页显示的条数
            //首次不执行
            if (!first) {
                //do something
                qry(obj.curr,obj.limit);

            }
        }
    });
});
function qry(_curr,_limit){
    var $eleForm = $("#listpage");
    $eleForm.attr("action", CONTEXT_PATH + '/front/attribution');
    $("#pageNumKey").val(_curr);
    $("#pageSizeKey").val(_limit);
    $eleForm.submit();
}
function newLabel(){
    //弹出层
    $("#id").val(-1);
    $("#label").val('');
    $("#price").val('');
    $("#labelDesc").val('');
    layer.open({
        skin: 'Ldemo-class',
        type: 1
        ,title: '新增标签'
        ,btnAlign: 'l' //按钮居左
        ,shade: 0.65
        ,content:$('#newsedit')
        ,btn: ['保存','取消']
        ,yes: function(){
            //alert("a")
            layui.form.on('submit(fromVerify)', function(data){
                //layer.msg(JSON.stringify(data.field));
                //修改或保存
                $.ajax({
                    type: "POST",
                    url: CONTEXT_PATH+'/front/belonglab/createLabel',
                    async: true,   //是否为异步请求
                    cache: false,  //是否缓存结果
                    //data: {id:ids.toString()},
                    data:$("#belongForm").serialize(),
                    dataType:"json",
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                        //'Content-Type': 'application/json;charset=UTF-8'
                    },
                    success:function(data){
                        console.log(data);
                        if(data.success){
                            //storageSessionObj("user",res.data.data);
                            layer.msg("新增成功");
                           window.location.href = CONTEXT_PATH+data.extra.url;
                            layer.close(layer.index);
                        }else{
                            //$.alert(data.msg);
                            layer.msg("新增失败");
                        }
                    },
                    error:function (res) {
                        console.log(res);
                        layer.msg("新增失败");
                    }
                });
                return false;
            });
        }
        ,zIndex: layer.zIndex //重点1
        ,success: function(layero){
            layero.addClass("layui-form");
            layero.find(".layui-layer-btn0").attr("lay-filter","fromVerify").attr("lay-submit","");
            layui.form.render();
        }
    });
}

function editLabel(_id){
    $("#id").val(_id);
    //弹出层
    $.ajax({
        type: "GET",
        url: CONTEXT_PATH+'/front/belonglab/get',
        async: true,   //是否为异步请求
        cache: false,  //是否缓存结果
        data: {id:_id},
        //data:$("#priceForm").serialize(),
        dataType:"json",
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
            //'Content-Type': 'application/json;charset=UTF-8'
        },
        success:function(data){
            console.log(data);
            if(data.success){
                //storageSessionObj("user",res.data.data);
               layer.msg("数据加载成功");
               $("#label").val(data.data.label);
                $("#price").val(data.data.price);
                $("#labelDesc").val(data.data.labelDesc);
                //window.location.href = CONTEXT_PATH+res.data.extra.url;
                editshowPage(_id);
            }else{
                //$.alert(data.msg);
                layer.msg("数据加载失败");
            }
        },
        error:function (res) {
            console.log(res);
            layer.msg("新增失败");
        }
    });

}

function editshowPage(_id){
    layer.open({
        skin: 'Ldemo-class',
        type: 1
        ,title: '修改标签'
        ,btnAlign: 'l' //按钮居左
        ,shade: 0.65
        ,content:$('#newsedit')
        ,btn: ['保存','取消']
        ,yes: function(){
            //alert("a")
            layui.form.on('submit(fromVerify)', function(data){
                //layer.msg(JSON.stringify(data.field));
                //修改或保存
                $.ajax({
                    type: "POST",
                    url: CONTEXT_PATH+'/front/belonglab/update',
                    async: true,   //是否为异步请求
                    cache: false,  //是否缓存结果
                    //data: {id:ids.toString()},
                    data:$("#belongForm").serialize(),
                    dataType:"json",
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                        //'Content-Type': 'application/json;charset=UTF-8'
                    },
                    success:function(data){
                        console.log(data);
                        if(data.success){
                            //storageSessionObj("user",res.data.data);
                            layer.msg("修改成功");
                            window.location.href = CONTEXT_PATH+data.extra.url;
                            layer.close(layer.index);

                        }else{
                            //$.alert(data.msg);
                            layer.msg("修改失败");
                        }
                    },
                    error:function (res) {
                        console.log(res);
                        layer.msg("修改失败");
                    }
                });
                return false;
            });
        }
        ,zIndex: layer.zIndex //重点1
        ,success: function(layero){
            layero.addClass("layui-form");
            layero.find(".layui-layer-btn0").attr("lay-filter","fromVerify").attr("lay-submit","");
            layui.form.render();
        }
    });
}