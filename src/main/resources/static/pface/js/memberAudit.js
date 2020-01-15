function submitAudit() {

    var uid=$("#uid").val();
    var y= $("input[name='yes']:checked").val();
    if(y=='y'){
        //审核通过
        var reason=$("#reason").val();
        if(reason=='' || reason==null || reason=='undefined'){
            alert("请填写审核说明！")
            return;
        }
        $.ajax({
            type: "POST",
            url: CONTEXT_PATH+'/admin/cert/auditPass',
            async: true,   //是否为异步请求
            cache: false,  //是否缓存结果
            data: {
                belongUid:uid,
                auditMsg:reason
            },
            dataType:"json",
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
                //'Content-Type': 'application/json;charset=UTF-8'
            },
            success:function(data){
                console.log(data);
                if(data.success){
                    //storageSessionObj("user",res.data.data);
                    alert("审核成功");
                    //window.location.href = CONTEXT_PATH+data.extra.url;
                    //layer.close(layer.index);
                    $('#certModal').modal('hide')
                }else{
                    //$.alert(data.msg);
                    alert("审核失败");
                }
            },
            error:function (res) {
                console.log(res);
                alert("审核失败");
            }
        });
    }else{
        //审核不通过
        var reason=$("#reason").val();
        if(reason=='' || reason==null || reason=='undefined'){
          alert("请填写审核说明！")
            return;
        }
        $.ajax({
            type: "POST",
            url: CONTEXT_PATH+'/admin/cert/auditNoPass',
            async: true,   //是否为异步请求
            cache: false,  //是否缓存结果
            data: {
                belongUid:uid,
                auditMsg:reason
            },
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
                    alert("审核成功");
                    //window.location.href = CONTEXT_PATH+data.extra.url;
                    //layer.close(layer.index);
                    $('#certModal').modal('hide')
                }else{
                    //$.alert(data.msg);
                    alert("审核失败");
                }
            },
            error:function (res) {
                console.log(res);
                //layer.msg("审核失败");
                alert("审核失败");
            }
        });
    }

}
