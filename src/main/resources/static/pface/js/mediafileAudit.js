
function initStaticData(row,belongLabels, pricesLabels, publishLabels, mediaType) {
    var status= row.mediaStatus.toUpperCase();
    var disabledValue = false;
    if (status == 'AUDITED' || status=='UNAUDITED') {
        disabledValue = true;
    }
    $("#"+mediaType+"Catalog").val(row.catalogueId); $("#"+mediaType+"Catalog").attr("disabled", disabledValue);
    $("#"+mediaType+"Title").val(row.mediaTitle);  $("#"+mediaType+"Title").attr("disabled", disabledValue);
    $("#"+mediaType+"KeyWord").val(row.mediaKeyword);  $("#"+mediaType+"KeyWord").attr("disabled", disabledValue);
    $("#"+mediaType+"Bref").val(row.mediaBrief); $("#"+mediaType+"Bref").attr("disabled", disabledValue);

    var videoPrice = $("#"+mediaType+"Price");videoPrice.empty();
    var videoBelongLabelsDiv = $("#"+mediaType+"BelongLabelsDiv"); videoBelongLabelsDiv.html("");
    var videoPubLabel = $("#"+mediaType+"PubLabel"); videoPubLabel.empty();

    //价格标签
    for (var i=0,len=pricesLabels.length; i<len; i++){
        videoPrice.append('<option value="'+pricesLabels[i].id+'" '+(row.priceLabelid == pricesLabels[i].id ? "selected" : "")+'>'+pricesLabels[i].price + '元('+pricesLabels[i].label +')</option>' );
    }
    videoPrice.attr("disabled", disabledValue);

    //转载标签
    videoPubLabel.append('<option value="">空</option>');
    for (var j=0,lenj = publishLabels.length; j<lenj; j++){
        videoPubLabel.append('<option value="'+publishLabels[j].id+'"'+(row.publishLabelid == publishLabels[j].id ? "selected":"")+'>' + publishLabels[j].label + '</option>');
    }
    videoPubLabel.attr("disabled", disabledValue);

    //归属标签
    var belongLabelsString = "";
    var belongLabelIds = row.belongLabelid; //以，分隔的ids
    var disabledStr = "";
    if (disabledValue){
        disabledStr = "disabled";
    }
    for (var m = 0; m < belongLabels.length; m++) {
        if (belongLabelIds != '') {

            if (belongLabelIds.indexOf(belongLabels[m].id) >= 0) {
                belongLabelsString = belongLabelsString+
                    ' <label class="checkbox-inline">' +
                    '   <input class="ls_alertCheckbox" type="checkbox" id="video_belongLabel'+belongLabels[m].id+'" name="'+mediaType+'_belongLabel" value="'+belongLabels[m].id+'" checked="true" '+disabledStr+'>' +
                    '   <span class="ls_CheckboxLabel">'+belongLabels[m].label+'</span>' +
                    ' </label>';
            } else {
                belongLabelsString = belongLabelsString+
                ' <label class="checkbox-inline">' +
                '   <input class="ls_alertCheckbox" type="checkbox" id="video_belongLabel'+belongLabels[m].id+'" name="'+mediaType+'_belongLabel" value="'+belongLabels[m].id+'" '+disabledStr+'>' +
                '   <span class="ls_CheckboxLabel">'+belongLabels[m].label+'</span>' +
                ' </label>';
            }

        } else {
            belongLabelsString = belongLabelsString+
                ' <label class="checkbox-inline">' +
                '   <input class="ls_alertCheckbox" type="checkbox" id="video_belongLabel'+belongLabels[m].id+'" name="'+mediaType+'_belongLabel" value="'+belongLabels[m].id+'" '+disabledStr+'>' +
                '   <span class="ls_CheckboxLabel">'+belongLabels[m].label+'</span>' +
                ' </label>';
        }
    }

    videoBelongLabelsDiv.html(belongLabelsString);
}

function fillVideoForm(row,belongLabels, pricesLabels, publishLabels) {
    $("#videoSize").text(renderSize(row.fileSize));
    $("#videoUpdateTime").text('更新时间:'+row.uploadDate);

    //$("#videoCover").attr("src",CONTEXT_PATH+'/image?url='+encodeURIComponent(row.coverUrl));
    //$("#myMovie").attr("poster",CONTEXT_PATH+getUrlPath(row.coverUrl));
    //$("#myMovie").attr("poster",CONTEXT_PATH+'/image?url='+encodeURIComponent(row.coverUrl));
    //$("#myMovie").attr("src",CONTEXT_PATH+'/image?url='+encodeURIComponent(row.fileViewUrl));

    $("#myMovie").attr("src",CONTEXT_PATH+getUrlPath(row.fileViewUrl));
    var coverOrigin = row.coverOrigin;
    if (coverOrigin == "0"){ //手动
        var cover = row.uploadCoverUrl;
        if (cover==null || cover == '' || cover==undefined || cover=='undefined'){
            $("#myMovie").attr("poster",CONTEXT_PATH+'/static/img/covervideouploaddefault.jpg');
        } else {
            $("#myMovie").attr("poster",CONTEXT_PATH+getUrlPath(cover));
        }
    }else{
        var cover = row.coverUrl;
        if (cover==null || cover == '' || cover==undefined || cover=='undefined'){
            $("#myMovie").attr("poster",CONTEXT_PATH+'/static/img/covernovideodefault.jpg');
        } else {
            $("#myMovie").attr("poster",CONTEXT_PATH+getUrlPath(cover));
        }
    }

    $("#videoMediaId").val(row.id);
    var status= row.mediaStatus.toUpperCase();
    console.log("status = "+status + ",id = "+row.id);

    // $("#videoTitle").text(row.mediaTitle);
    // $("#videoKeyWord").text(row.mediaKeyword);
    // $("#videoCatalog").text(row.catalogueName);
    // $("#videoPrice").text('￥'+row.price);
    // $("#videoLabel").text(row.belongLabelNames);
    // $("#videoPubLabel").text(row.publishLabelName);
    // $("#videoBref").text(row.mediaBrief);

    initStaticData(row, belongLabels, pricesLabels, publishLabels,  "video");

    if (status == 'AUDITED' || status=='UNAUDITED') {

        // if (status.toUpperCase() == 'AUDITED' ) {
        //     $("input:radio[name='videoYes'][value='y']").prop('checked','true');
        //     $("input:radio[name='videoYes'][value='n']").removeAttr('checked');
        // }else {
        //     $("input:radio[name='videoYes'][value='n']").prop('checked','true');
        //     $("input:radio[name='videoYes'][value='y']").removeAttr('checked');
        // }

        $("input:radio[name='videoYes']").each(function(index,domEle){
            $(this).prop("disabled", true);

            if ($(this).val() == 'y' && status == 'AUDITED') {
                $(this).prop("checked", true);
            }

            if ($(this).val() == 'n' && status == 'UNAUDITED') {
                $(this).prop("checked", true);
            }

        });

        $("#videoReason").text(row.lastedAuditMsg);
        $("#videoReason").prop("disabled", true);
    }else {
        $("input:radio[name='videoYes']").each(function(index,domEle){
            $(this).prop("disabled", false);
            // $(this).prop("checked", false);
            $(this).removeAttr("checked");
        });
        $("#videoReason").text("");
        $("#videoReason").prop("disabled", false);
    }

    $("#auditSubmitVideo").hide();
    if(status=='AUDITING' || status=='auditing'){
        $("#auditSubmitVideo").show();
    }
}

function fillAudioForm(row,belongLabels, pricesLabels, publishLabels) {
    $("#audioMediaId").val(row.id);

    $("#music").attr("src",CONTEXT_PATH+getUrlPath(row.fileUrl));
    // $("#music").attr("src",CONTEXT_PATH+'/image?url='+encodeURIComponent(row.fileUrl));

    var cover = row.uploadCoverUrl;
    if (cover==null || cover == '' || cover==undefined || cover=='undefined'){
        $("#audioCover").attr("src",CONTEXT_PATH+'/static/img/covernoaudiodefault.jpg');
    }else {
        $("#audioCover").attr("src",CONTEXT_PATH+'/image?url='+encodeURIComponent(cover));
    }

    $("#audioSize").text(renderSize(row.fileSize));
    $("#audioUpdateTime").text(row.uploadDate);

    // $("#audioTitle").text(row.mediaTitle);
    // $("#audioCatalog").text(row.catalogueName);
    // $("#audioPrice").text('￥'+row.price);
    // $("#auidoKeyWord").text(row.mediaKeyword);
    // $("#auidoLabel").text(row.belongLabelNames);
    // $("#auidoPubLabel").text(row.publishLabelName);
    // $("#auidoBref").text(row.mediaBrief);

    initStaticData(row,belongLabels, pricesLabels, publishLabels,"audio");

    var status= row.mediaStatus.toUpperCase();
    if (status == 'AUDITED' || status=='UNAUDITED') {
        // if (status == 'AUDITED' ) {
        //     $("input:radio[name='audioYes'][value='y']").attr('checked','true');
        //     $("input:radio[name='audioYes'][value='n']").attr('checked','false');
        // }else {
        //     $("input:radio[name='audioYes'][value='n']").attr('checked','true');
        //     $("input:radio[name='audioYes'][value='y']").attr('checked','false');
        // }

        $("input:radio[name='audioYes']").each(function(index,domEle){
            $(this).attr("disabled", true);

            if ($(this).val() == 'y' && status == 'AUDITED') {
                $(this).prop("checked", true);
            }

            if ($(this).val() == 'n' && status == 'UNAUDITED') {
                $(this).prop("checked", true);
            }
        });

        $("#audioReason").text(row.lastedAuditMsg);
        $("#audioReason").attr("disabled", true);
    }else {
        $("input:radio[name='audioYes']").each(function(index,domEle){
            $(this).attr("disabled", false);
            $(this).removeAttr("checked");
        });
        $("#audioReason").text("");
        $("#audioReason").attr("disabled", false);
    }

    $("#auditSubmitAudio").hide();
    if(status=='AUDITING' || status=='auditing'){
        $("#auditSubmitAudio").show();
    }
}

function fillImageForm(row, belongLabels, pricesLabels, publishLabels) {

    $("#fileMediaId").val(row.id);
    var cover = row.uploadCoverUrl;
    if (cover==null || cover == '' || cover==undefined || cover=='undefined'){
        $("#imageCover").attr("src",CONTEXT_PATH+'/static/img/covernodocdefault.jpg');
    }else {
        $("#imageCover").attr("src",CONTEXT_PATH+'/image?url='+encodeURIComponent(cover));
    }

    $("#imageSize").text(renderSize(row.fileSize));
    $("#imageUpdateTime").text(row.uploadDate);
    $("#viewFile").attr("href",CONTEXT_PATH+"/image?action=down&url="+encodeURIComponent(row.fileUrl));
    // $("#downFile").attr("href",CONTEXT_PATH+"/image?action=down&url="+encodeURIComponent(row.fileUrl));


    // var imagePath=row.imagePath;
    var imagePath  = row.imgfilePaths;
    if (imagePath==null || imagePath == '' || imagePath==undefined || imagePath=='undefined'){
    }else {
        var images=imagePath.split(",");
        var html="";
        for( i=0,s=images.length;i<s;i++){
            html=html+"<li><img  src="+CONTEXT_PATH+'/image?url='+encodeURIComponent(images[i])+"/></li>";
        }
        $("#imgUl").html(html);
    }

    // $("#imageTitle").html('<span class="glyphicon glyphicon-paperclip"></span>'+ row.mediaTitle);
    // $("#imageCatalog").text(row.catalogueName);
    // $("#imagePrice").text('￥'+row.price);
    // $("#imageKeyWord").text(row.mediaKeyword);
    // $("#imageLabel").text(row.belongLabelNames);
    // $("#imagePubLabel").text(row.publishLabelName);
    // $("#imageBref").text(row.mediaBrief);

    initStaticData(row,belongLabels, pricesLabels, publishLabels,"image");

    var status= row.mediaStatus.toUpperCase();
    if (status == 'AUDITED' || status=='UNAUDITED') {
        // if (status == 'AUDITED' ) {
        //     $("input:radio[name='fileYes'][value='y']").attr('checked','true');
        //     $("input:radio[name='fileYes'][value='n']").attr('checked','false');
        // }else {
        //     $("input:radio[name='fileYes'][value='n']").attr('checked','true');
        //     $("input:radio[name='fileYes'][value='y']").attr('checked','false');
        // }

        $("input:radio[name='fileYes']").each(function(index,domEle){
            $(this).attr("disabled", true);

            if ($(this).val() == 'y' && status == 'AUDITED') {
                $(this).prop("checked", true);
            }

            if ($(this).val() == 'n' && status == 'UNAUDITED') {
                $(this).prop("checked", true);
            }
        });

        $("#fileReason").text(row.lastedAuditMsg);
        $("#fileReason").attr("disabled", true);
    }else {
        $("input:radio[name='fileYes']").each(function(index,domEle){
            $(this).attr("disabled", false);
            $(this).removeAttr("checked");
        });
        $("#fileReason").text("");
        $("#fileReason").attr("disabled", false);
    }

    $("#auditSubmitFile").hide();
    if(status=='AUDITING' || status=='auditing'){
       // $("#auditSubmitFile").attr("disabled",false);
        $("#auditSubmitFile").show();
    }

}

function submitAuditFile(mediaType) {
    //alert(mediaType);
    var fileid,radioV,reasonV;
    var catalogueId,mediaTitle,mediaKeyword,mediaBrief,priceid,pubLabel,belongLabels;
    if('video'==mediaType || mediaType=='VIDEO'){
        fileid=$("#videoMediaId").val();
        radioV= $("input[name='videoYes']:checked").val();
        reasonV=$("#videoReason").val();

        catalogueId = $("#videoCatalog").find("option:selected").val();
        mediaTitle = $.trim($("#videoTitle").val());
        mediaKeyword = $.trim($("#videoKeyWord").val());
        mediaBrief = $.trim($("#videoBref").val());
        priceid = $("#videoPrice").find("option:selected").val();
        pubLabel = $("#videoPubLabel").find("option:selected").val();
        belongLabels = getCheckboxCheckedValues("video_belongLabel");

    }else if('audio'==mediaType || mediaType=='AUDIO'){
        fileid=$("#audioMediaId").val();
        radioV= $("input[name='audioYes']:checked").val();
        reasonV=$("#audioReason").val();

        catalogueId = $("#audioCatalog").find("option:selected").val();
        mediaTitle = $.trim($("#audioTitle").val());
        mediaKeyword = $.trim($("#audioKeyWord").val());
        mediaBrief = $.trim($("#audioBref").val());
        priceid = $("#audioPrice").find("option:selected").val();
        pubLabel = $("#audioPubLabel").find("option:selected").val();
        belongLabels = getCheckboxCheckedValues("audio_belongLabel");

    }else if('file'==mediaType || mediaType=='FILE'){
        fileid=$("#fileMediaId").val();
        radioV= $("input[name='fileYes']:checked").val();
        reasonV=$("#fileReason").val();

        catalogueId = $("#imageCatalog").find("option:selected").val();
        mediaTitle = $.trim($("#imageTitle").val());
        mediaKeyword = $.trim($("#imageKeyWord").val());
        mediaBrief = $.trim($("#imageBref").val());
        priceid = $("#imagePrice").find("option:selected").val();
        pubLabel = $("#imagePubLabel").find("option:selected").val();
        belongLabels = getCheckboxCheckedValues("image_belongLabel");
    }

    //alert(radioV)
    //alert(fileid)
    if (mediaTitle ==''){
        alert('"标题"不能为空。');
        return false;
    }
    if (mediaKeyword == ''){
        alert('"关键字"不能为空。');
        return false;
    }
    if (belongLabels == ''){
        alert("请选择：归属标签。");
        return false;
    }
    // if (mediaBrief == ''){
    //     alert('"简介"不能为空。');
    //     return false;
    // }
    if (radioV == ''){
        alert("请先选择；是否审核通过。");
        return false;
    }

    console.log("radioV====="+radioV);
    if(radioV=='y'){
        //审核通过
        if(reasonV=='' || reasonV==null || reasonV=='undefined'){
            alert("请填写审核说明！")
            return;
        }
        $.ajax({
            type: "POST",
            url: CONTEXT_PATH+'/admin/auditfile/auditPass',
            async: true,   //是否为异步请求
            cache: false,  //是否缓存结果
            data: {
                msgOriginId:fileid,
                auditMsg:reasonV,
                catalogueId : catalogueId,
                mediaTitle : mediaTitle,
                mediaKeyword:mediaKeyword,
                mediaBrief:mediaBrief,
                priceid:priceid,
                pubLabel:pubLabel,
                belongLabels:belongLabels
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
                    //layer.close(layer.index);
                    if('video'==mediaType){
                        $('#videoModal').modal('hide')
                    }else if('audio'==mediaType){
                        $('#audioModal').modal('hide')
                    }if('file'==mediaType){
                        $('#fileModal').modal('hide')
                    }
                    Search();
                    // window.location.href = CONTEXT_PATH+data.extra.url;
                }else{
                    //$.alert(data.msg);
                    alert("审核失败,请稍后再试");
                }
            },
            error:function (res) {
                console.log(res);
                alert("系统错误，请稍后再试");
            }
        });
    }else{
        //审核不通过

        if(reasonV=='' || reasonV==null || reasonV=='undefined'){
            alert("请填写审核说明！")
            return;
        }
        $.ajax({
            type: "POST",
            url: CONTEXT_PATH+'/admin/auditfile/auditNoPass',
            async: true,   //是否为异步请求
            cache: false,  //是否缓存结果
            data: {
                msgOriginId:fileid,
                auditMsg:reasonV,
                catalogueId : catalogueId,
                mediaTitle : mediaTitle,
                mediaKeyword:mediaKeyword,
                mediaBrief:mediaBrief,
                priceid:priceid,
                pubLabel:pubLabel,
                belongLabels:belongLabels
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

                    //layer.close(layer.index);
                    if('video'==mediaType){
                        $('#videoModal').modal('hide')
                    }else if('audio'==mediaType){
                        $('#audioModal').modal('hide')
                    }if('file'==mediaType){
                        $('#fileModal').modal('hide')
                    }
                    // window.location.href = CONTEXT_PATH+data.extra.url;
                    Search();
                }else{
                    //$.alert(data.msg);
                    alert("审核失败，请稍后再试。");
                }
            },
            error:function (res) {
                console.log(res);
                alert("系统错误，请稍后再试");
            }
        });
    }

}


function viewFile(url){
   //window.open(url);
    var $eleForm = $("<form method='get'></form>");
    $eleForm.attr("action",url);
    $(document.body).append($eleForm);
    //提交表单，实现下载
    $eleForm.submit();
}

function downFile(url){
    //window.open(url);
    var $eleForm = $("<form method='get'></form>");
    $eleForm.attr("action",url);
    $(document.body).append($eleForm);
    //提交表单，实现下载
    $eleForm.submit();
}

function getCheckboxCheckedValues(ckname) {
    var type = '';
    $('input[type=checkbox][name="' + ckname + '"]').each(function () {
        if ($(this).prop("checked")) {
            type += $(this).val() + ',';
        }
    });
    if (type != '') {
        type = type.substring(0, type.length - 1);
    }
    return type;
}