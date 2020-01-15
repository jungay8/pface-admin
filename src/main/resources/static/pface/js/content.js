
//弹出窗口表单样式pageNum
layui.use(['form','layer','laypage'], function(){
    var $ = layui.jquery,
        form = layui.form,
        layer=layui.layer,
        laypage = layui.laypage;
//自定义首页、尾页、上一页、下一页文本
    laypage.render({
        elem: 'pageNum'
        ,limit:pageSize
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

function qry2(){
    qry($("#pageNumKey").val(),$("#pageSizeKey").val());
}
function qry(_curr,_limit){
    var $eleForm = $("#listpage");
    $eleForm.attr("action", CONTEXT_PATH + '/front/content');
    $("#pageNumKey").val(_curr);
    $("#pageSizeKey").val(_limit);
    //媒体类型
    //jquery获取复选框值
    var chk_value =[];//定义一个数组
    $('input[name="mediaType"]:checked').each(function(){//遍历每一个名字为interest的复选框，其中选中的执行函数
        chk_value.push($(this).val());//将选中的值添加到数组chk_value中
    });

    //所属
    var belongvalue = [];
    $('input[name="contentBelongLabelName"]:checked').each(function(){
        belongvalue.push($(this).val());
    });
    $("#hcontentBelongLabel").val(belongvalue.join(","));

    //分类
    $("#hcontentMCatalogue").val($("#qcontentMCatalogueName").find("option:selected").val());

    //审核
    $("#hqcontentMAuditStatus").val($("#qcontentMAuditStatus").find("option:selected").val());
    console.log("提交的审核状态："+$("#hqcontentMAuditStatus").val());
    $("#mediaTypes").val(chk_value.join(","));
    //商品状态
    $("#goodsStatus").val($('#goods').val());
    $("#hKeyword").val($('#qKeywordId').val());


    $eleForm.submit();
}


function checkAllBox(){
    $("input[name='conterList']").attr("checked",true);
}

function goodsOn(){
    //jquery获取复选框值
    var chk_value =[];//定义一个数组
    $('input[name="conterList"]:checked').each(function(){//遍历每一个名字为interest的复选框，其中选中的执行函数
        chk_value.push($(this).val());//将选中的值添加到数组chk_value中
    });
    //alert(JSON.stringify(chk_value));
    if(chk_value.length<=0){
        layer.msg("请选择至少一项");
        return;
    }

    var isOnOk = true;

    for(var i=0; i<chk_value.length; i++){
        var itemArray = chk_value[i].split("#");
        var mediaStatus = itemArray[3];
        var goodsStatus = itemArray[4];
        if (mediaStatus == 'AUDITED' || goodsStatus=='OFF') {
          continue;
        }else {
            isOnOk = false;
            layer.msg("选择的内容，必须全部通过审核或者是下架的内容，才能上架");
            break;
        }

    }
    console.log('isOnok= ' + isOnOk)
    if (isOnOk) {
        layer.confirm('您确定要上架吗?', {btn: ['确定', '取消'], title: "提示"}, function (_this, _id) {
            //var ids = [];
            //ids.push(_id)


            $.ajax({
                type: "POST",
                url: CONTEXT_PATH + '/front/content/goodsOn-batch',
                async: true,   //是否为异步请求
                cache: false,  //是否缓存结果
                data: {
                    //id:ids.toString()
                    id: chk_value
                },
                traditional: true,
                dataType: "json",
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                    //'Content-Type': 'application/json;charset=UTF-8'
                },
                success: function (data) {
                    console.log(data);
                    if (data.success) {
                        //storageSessionObj("user",res.data.data);
                        layer.msg("上架成功");
                        //window.location.href = CONTEXT_PATH+res.data.extra.url;
                        //$(_this).parents("li").remove();
                        qry($("#pageNumKey").val(), $("#pageSizeKey").val());
                    } else {
                        //$.alert(data.msg);
                        layer.msg("上架失败");
                    }
                },
                error: function (res) {
                    console.log(res);
                    layer.msg("上架失败");
                }
            });

        });
    }
}

function goodsOff(){
    //jquery获取复选框值
    var chk_value =[];//定义一个数组
    $('input[name="conterList"]:checked').each(function(){//遍历每一个名字为interest的复选框，其中选中的执行函数
        chk_value.push($(this).val());//将选中的值添加到数组chk_value中
    });
    //alert(JSON.stringify(chk_value));
    if(chk_value.length==0){
        layer.msg("请选择至少一项");
        return;
    }

    var isOffOk = true;
    for(var i=0; i<chk_value.length; i++){
        var itemArray = chk_value[i].split("#");
        var mediaStatus = itemArray[3];
        var goodsStatus = itemArray[4];
        if (goodsStatus != 'ON') {
            isOffOk = false;
            layer.msg("选择的内容，必须全部是上架的，才能下架");
            break;
        }else {
            console;
        }
    }
    console.log("isOffOk = " + isOffOk);
    if (isOffOk) {
        layer.confirm('您确定要下架吗?', {btn: ['确定', '取消'], title: "提示"}, function (_this, _id) {
            //var ids = [];
            //ids.push(_id)
            $.ajax({
                type: "POST",
                url: CONTEXT_PATH + '/front/content/goodsOff-batch',
                async: true,   //是否为异步请求
                cache: false,  //是否缓存结果
                data: {
                    //id:ids.toString()
                    id: chk_value
                },
                traditional: true,
                dataType: "json",
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                    //'Content-Type': 'application/json;charset=UTF-8'
                },
                success: function (data) {
                    console.log(data);
                    if (data.success) {
                        //storageSessionObj("user",res.data.data);
                        layer.msg("下架成功");
                        //window.location.href = CONTEXT_PATH+res.data.extra.url;
                        //$(_this).parents("li").remove();
                        qry($("#pageNumKey").val(), $("#pageSizeKey").val());
                    } else {
                        //$.alert(data.msg);
                        layer.msg("下架失败");
                    }
                },
                error: function (res) {
                    console.log(res);
                    layer.msg("下架失败");
                }
            });
        });
    }
}

//批量删除
function goodsDel(){
    //jquery获取复选框值
    var chk_value =[];
    $('input[name="conterList"]:checked').each(function(){
        chk_value.push($(this).val());
    });
    //alert(JSON.stringify(chk_value));
    if(chk_value.length==0){
        layer.msg("请选择至少一项");
        return;
    }
    layer.confirm('您确定要删除吗? 删除后不可恢复，请您务必谨慎操作！', {btn: ['确定', '取消'], title: "系统提示"}, function (_this,_id) {
        //var ids = [];
        //ids.push(_id)
        $.ajax({
            type: "POST",
            url: CONTEXT_PATH+'/front/content/physical-delete-batch',
            async: true,   //是否为异步请求
            cache: false,  //是否缓存结果
            data: {
                //id:ids.toString()
                id:chk_value
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
                    layer.msg("删除成功");
                    qry($("#pageNumKey").val(),$("#pageSizeKey").val());
                }else{
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

function delLab(mediaType, mediaId, mediaFileId){
    layer.confirm('您确定要删除吗? 删除后不可恢复，请您务必谨慎操作！', {btn: ['确定', '取消'], title: "提示"}, function () {
        var ids = [];
        ids.push(mediaType);
        ids.push(mediaId);
        ids.push(mediaFileId);
        $.ajax({
            type: "POST",
            url: CONTEXT_PATH+'/front/content/physical-delete',
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
                    //$(obj).parents("li").remove();
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


