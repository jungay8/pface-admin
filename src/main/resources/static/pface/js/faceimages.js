
//弹出窗口表单样式pageNum
layui.use(['form','layer','laypage','upload'], function(){
    var $ = layui.jquery,
            table = layui.table
            form = layui.form,
            layer=layui.layer,
            laypage = layui.laypage,
            upload = layui.upload;

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

    //多文件列表上传
    var demoListView = $('#demoList')
        ,uploadListIns = upload.render({
        elem: '#testList'
        ,url: CONTEXT_PATH + '/front/appimages/uploadimags'
        ,accept: 'images'
        ,acceptMime: 'image/jpeg,image/jpg,image/png,image/bmp,image/tif'
        ,multiple: true
        ,auto: false
        ,bindAction: '#testListAction'
        ,data : {
            sysLibId:function () {
                return $('#querysysLibId').val()
            }
         }
        ,choose: function(obj){
            var files = this.files = obj.pushFile(); //将每次选择的文件追加到文件队列
            // var currFileList = [];
            // $('#demoList tr').each(function () {
            //     var fileName = $(this).children().eq(1).text;
            //     var fileSize = $(this).children().eq(2).text;
            //     currFileList.push({name:fileName, size:fileSize});
            // });
            //读取本地文件
            obj.preview(function(index, file, result){
                var size = (file.size/1024).toFixed(1);
                // for(var i=0; i<currFileList.length; i++){
                //     var csize = (currFileList[i].size/1024).toFixed(1);
                //     if (file.name == currFileList[i].name && size == csize) {
                //         delete  files[index];
                //         layer.msg('文件重复了：'+file.name, {icon:3});
                //         return;
                //     }
                // }
                var tr = $(['<tr id="upload-'+ index +'">'
                    ,'<td><img class="layui-upload-img" src="'+result+'" id="image'+index+'"/></td>'
                    ,'<td>'+ file.name +'</td>'
                    ,'<td>'+ size +'kb</td>'
                    ,'<td>等待上传</td>'
                    ,'<td>'
                    ,'<button class="layui-btn layui-btn-xs demo-reload layui-hide">重传</button>'
                    ,'<button class="layui-btn layui-btn-xs layui-btn-danger demo-delete">删除</button>'
                    ,'</td>'
                    ,'</tr>'].join(''));

                //单个重传
                tr.find('.demo-reload').on('click', function(){
                    obj.upload(index, file);
                });

                //删除
                tr.find('.demo-delete').on('click', function(){
                    delete files[index]; //删除对应的文件
                    tr.remove();
                    uploadListIns.config.elem.next()[0].value = ''; //清空 input file 值，以免删除后出现同名文件不可选
                });

                demoListView.append(tr);
            });
        }
        ,done: function(res, index, upload){
            if(res.code == 200){ //上传成功
                var tr = demoListView.find('tr#upload-'+ index)
                    ,tds = tr.children();
                tds.eq(3).html('<span style="color: #5FB878;">上传成功</span>');
                tds.eq(4).html('-- --'); //清空操作
                return delete this.files[index]; //删除文件队列已经上传成功的文件
            }
            this.error(index, upload);
        }
        ,error: function(index, upload){
            var tr = demoListView.find('tr#upload-'+ index)
                ,tds = tr.children();
            tds.eq(3).html('<span style="color: #FF5722;">上传失败</span>');
            tds.eq(4).find('.demo-reload').removeClass('layui-hide'); //显示重传
        }
    });
    $(document).on('click','#getCheckData2',function(){
        window.location.href = CONTEXT_PATH+"/front/faceimagelibs";
    });

    //批量导入人像图片
    $(document).on('click','#getCheckData',function(){
        active.editTitle = "批量导入人像图片";
        var sysLibId = $(this).attr("data");
        active.sysLibId = sysLibId;
        var w=document.body.clientWidth*0.7;
        var h=document.body.clientHeight*0.9;
        demoListView.html('');
        active.setTop("newsedit",w,h);
    });

    //修改图片属性
    $(document).on('click','.video_edit',function(){
        active.editTitle = "修改图片属性";
        var item=$(this).attr("data");
        var itemArray = item.split("#");
        $("#sysAppImagesId").val(itemArray[0]);
        $("#editdeviceId").val(itemArray[1]);
        $("#editimgId").val(itemArray[2]);
        $("#editlibId").val(itemArray[3]);
        $("#editpersonIdcard").val(itemArray[4]=='' || itemArray[4] == 'null' ? '' : itemArray[4]);
        $("#editpersonName").val(itemArray[5]=='' || itemArray[5] == 'null' ? '' : itemArray[5]);
        $("#editpersonGender").val(itemArray[6]=='' || itemArray[6] == 'null' ? '' : itemArray[6]);
        $("#editpersonAge").val(itemArray[7]=='' || itemArray[7] == 'null' ? '' : itemArray[7]);
        $("#editpersonAddr").val(itemArray[8]=='' || itemArray[8] == 'null' ? '' : itemArray[8]);
        $("#remark").val(itemArray[9]=='' || itemArray[9] == 'null' ? '' : itemArray[9]);
        var w=300;
        var h=document.body.clientHeight*0.7;

        active.setTop("newsedit2",w,h);
    });

    //提交图片属性的修改
    $(document).on("click", "#commitAllFileBtnId", function (){

        var querypersonName =  $("#querypersonName").val();
        var id = $("#sysAppImagesId").val();
        var editdeviceId = $("#editdeviceId").val();
        var editimgId = $("#editimgId").val();
        var editlibId = $("#editlibId").val();
        var editpersonIdcard = $("#editpersonIdcard").val();
        var editpersonName = $("#editpersonName").val();
        var editpersonGender = $("#editpersonGender").val();
        var editpersonAge = $("#editpersonAge").val();
        var editpersonAddr = $("#editpersonAddr").val();
        var remark = $("#remark").val();
        var fd = new FormData();
        fd.append("id", id);
        fd.append("editdeviceId", editdeviceId);
        fd.append("editimgId", editimgId);
        fd.append("editlibId", editlibId);
        fd.append("editpersonIdcard", editpersonIdcard);
        fd.append("editpersonName", editpersonName);
        fd.append("editpersonGender", editpersonGender);
        fd.append("editpersonAge", editpersonAge);
        fd.append("editpersonAddr", editpersonAddr);
        fd.append("querypersonName", querypersonName);
        fd.append("remark", remark);

        $.ajax({
            processData: false,
            contentType: false,
            type: "POST",
            url: CONTEXT_PATH + '/front/appimages/myupdate',
            data: fd,
            success: function (response) {
                if (response.success) {
                    layer.msg(response.msg, {
                        time: 2000,
                        icon: 5,
                        anim: 6
                    },function () {
                        // window.location.href = response.extra.url;
                        $("#pagesysLibId").val(response.extra.sysLibId);
                        $("#pagedeviceId").val(response.extra.deviceId);
                        $("#pagelibId").val(response.extra.libId);
                        $("#pagepersonName").val(response.extra.personName);
                        qry2();
                    });
                } else {
                    active.myalert(response.msg);
                }
            },
            error: function (error) {
                console.log(error);
            }
        });
    });

    //关闭弹窗
    $(document).on('click','.return_exq',function () {

        layer.closeAll();
    });


    //弹出层
    var active = {
        sysLibId:'',
        editTitle : '导入人脸图片',
        setTop: function(domId,w,h){
            var that = this;
            //多窗口模式，层叠置顶
            layer.open({
                skin: 'Ldemo-class',
                maxmin: true,
                type: 1
                ,title: active.editTitle
                ,btnAlign: 'l' //按钮居左
                ,shade: 0.65
                ,area: [w+'px', h+"px"]
                ,content:$('#'+domId)
                ,cancel:function () {
                    console.log("cancel() for domId = " +domId);
                    if (domId == 'newsedit') {
                        var trs = demoListView.children();
                        if(trs.length > 0){

                            qry2();
                        }
                    }
                }
                ,zIndex: layer.zIndex //重点1

            });
        },
        myalert:function(msg){
            layer.msg(msg, {
                time: 3000, //3s后自动关闭
                icon: 5,
                anim: 6
            });
        }
    }


});

function qry2(){
    qry($("#pageNumKey").val(),$("#pageSizeKey").val());
}
function qry(_curr,_limit){
    var $eleForm = $("#listpage");
    $eleForm.attr("action", CONTEXT_PATH + '/front/faceappimages');
    $("#pageNumKey").val(_curr);
    $("#pageSizeKey").val(_limit);
    $("#pagesysLibId").val($('#querysysLibId').val());
    $("#pagedeviceId").val($('#querydeviceId').val());
    $("#pagelibId").val($('#querylibId').val());
    $("#pagepersonName").val($('#querypersonName').val());
    $eleForm.submit();
}
//
// function checkAllBox(){
//     $("input[name='conterList']").attr("checked",true);
// }
//
// function goodsOn(){
//     //jquery获取复选框值
//     var chk_value =[];//定义一个数组
//     $('input[name="conterList"]:checked').each(function(){//遍历每一个名字为interest的复选框，其中选中的执行函数
//         chk_value.push($(this).val());//将选中的值添加到数组chk_value中
//     });
//     //alert(JSON.stringify(chk_value));
//     if(chk_value.length<=0){
//         layer.msg("请选择至少一项");
//         return;
//     }
//
//     var isOnOk = true;
//
//     for(var i=0; i<chk_value.length; i++){
//         var itemArray = chk_value[i].split("#");
//         var mediaStatus = itemArray[3];
//         var goodsStatus = itemArray[4];
//         if (mediaStatus == 'AUDITED' || goodsStatus=='OFF') {
//           continue;
//         }else {
//             isOnOk = false;
//             layer.msg("选择的内容，必须全部通过审核或者是下架的内容，才能上架");
//             break;
//         }
//
//     }
//     console.log('isOnok= ' + isOnOk)
//     if (isOnOk) {
//         layer.confirm('您确定要上架吗?', {btn: ['确定', '取消'], title: "提示"}, function (_this, _id) {
//             //var ids = [];
//             //ids.push(_id)
//             $.ajax({
//                 type: "POST",
//                 url: CONTEXT_PATH + '/front/content/goodsOn-batch',
//                 async: true,   //是否为异步请求
//                 cache: false,  //是否缓存结果
//                 data: {
//                     //id:ids.toString()
//                     id: chk_value
//                 },
//                 traditional: true,
//                 dataType: "json",
//                 headers: {
//                     'Content-Type': 'application/x-www-form-urlencoded'
//                     //'Content-Type': 'application/json;charset=UTF-8'
//                 },
//                 success: function (data) {
//                     console.log(data);
//                     if (data.success) {
//                         //storageSessionObj("user",res.data.data);
//                         layer.msg("上架成功");
//                         //window.location.href = CONTEXT_PATH+res.data.extra.url;
//                         //$(_this).parents("li").remove();
//                         qry($("#pageNumKey").val(), $("#pageSizeKey").val());
//                     } else {
//                         //$.alert(data.msg);
//                         layer.msg("上架失败");
//                     }
//                 },
//                 error: function (res) {
//                     console.log(res);
//                     layer.msg("上架失败");
//                 }
//             });
//
//         });
//     }
// }
//
// function goodsOff(){
//     //jquery获取复选框值
//     var chk_value =[];//定义一个数组
//     $('input[name="conterList"]:checked').each(function(){//遍历每一个名字为interest的复选框，其中选中的执行函数
//         chk_value.push($(this).val());//将选中的值添加到数组chk_value中
//     });
//     //alert(JSON.stringify(chk_value));
//     if(chk_value.length==0){
//         layer.msg("请选择至少一项");
//         return;
//     }
//
//     var isOffOk = true;
//     for(var i=0; i<chk_value.length; i++){
//         var itemArray = chk_value[i].split("#");
//         var mediaStatus = itemArray[3];
//         var goodsStatus = itemArray[4];
//         if (goodsStatus != 'ON') {
//             isOffOk = false;
//             layer.msg("选择的内容，必须全部是上架的，才能下架");
//             break;
//         }else {
//             console;
//         }
//     }
//     console.log("isOffOk = " + isOffOk);
//     if (isOffOk) {
//         layer.confirm('您确定要下架吗?', {btn: ['确定', '取消'], title: "提示"}, function (_this, _id) {
//             //var ids = [];
//             //ids.push(_id)
//             $.ajax({
//                 type: "POST",
//                 url: CONTEXT_PATH + '/front/content/goodsOff-batch',
//                 async: true,   //是否为异步请求
//                 cache: false,  //是否缓存结果
//                 data: {
//                     //id:ids.toString()
//                     id: chk_value
//                 },
//                 traditional: true,
//                 dataType: "json",
//                 headers: {
//                     'Content-Type': 'application/x-www-form-urlencoded'
//                     //'Content-Type': 'application/json;charset=UTF-8'
//                 },
//                 success: function (data) {
//                     console.log(data);
//                     if (data.success) {
//                         //storageSessionObj("user",res.data.data);
//                         layer.msg("下架成功");
//                         //window.location.href = CONTEXT_PATH+res.data.extra.url;
//                         //$(_this).parents("li").remove();
//                         qry($("#pageNumKey").val(), $("#pageSizeKey").val());
//                     } else {
//                         //$.alert(data.msg);
//                         layer.msg("下架失败");
//                     }
//                 },
//                 error: function (res) {
//                     console.log(res);
//                     layer.msg("下架失败");
//                 }
//             });
//         });
//     }
// }
//
// //批量删除
// function goodsDel(){
//     //jquery获取复选框值
//     var chk_value =[];
//     $('input[name="conterList"]:checked').each(function(){
//         chk_value.push($(this).val());
//     });
//     //alert(JSON.stringify(chk_value));
//     if(chk_value.length==0){
//         layer.msg("请选择至少一项");
//         return;
//     }
//     layer.confirm('您确定要删除吗? 删除后不可恢复，请您务必谨慎操作！', {btn: ['确定', '取消'], title: "系统提示"}, function (_this,_id) {
//         //var ids = [];
//         //ids.push(_id)
//         $.ajax({
//             type: "POST",
//             url: CONTEXT_PATH+'/front/content/physical-delete-batch',
//             async: true,   //是否为异步请求
//             cache: false,  //是否缓存结果
//             data: {
//                 //id:ids.toString()
//                 id:chk_value
//             },
//             traditional: true,
//             dataType:"json",
//             headers: {
//                 'Content-Type': 'application/x-www-form-urlencoded'
//                 //'Content-Type': 'application/json;charset=UTF-8'
//             },
//             success:function(data){
//                 console.log(data);
//                 if(data.success){
//                     layer.msg("删除成功");
//                     qry($("#pageNumKey").val(),$("#pageSizeKey").val());
//                 }else{
//                     layer.msg("删除失败");
//                 }
//             },
//             error:function (res) {
//                 console.log(res);
//                 layer.msg("删除失败");
//             }
//         });
//     });
// }


function delfaceappimage(_id, deviceId, imgId, libId){
    layer.confirm('将删除远程和本地的图片，请务必谨慎，不可恢复，您确定要删除吗?', {btn: ['确定', '取消'], title: "提示"}, function () {
        var ids = [];
        ids.push(_id);
        $.ajax({
            type: "POST",
            // url: CONTEXT_PATH+'/front/content/logic-delete',
            url: CONTEXT_PATH+'/front/appimages/logic-delete',
            async: true,   //是否为异步请求
            cache: false,  //是否缓存结果
            data: {
                //id:ids.toString()
                id:ids,
                deviceId:deviceId,
                imgId:imgId,
                libId:libId
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
                    layer.msg(data.msg);
                    //window.location.href = CONTEXT_PATH+res.data.extra.url;
                    qry2();
                }else{
                    //$.alert(data.msg);
                    layer.msg(data.msg);
                }
            },
            error:function (res) {
                console.log(res);
                layer.msg("删除失败");
            }
        });
    });


}


