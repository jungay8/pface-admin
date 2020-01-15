
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
        ,limits:[10, 20, 30, 50, 70,100]
        // ,limits:[4,7,10, 20, 30, 40, 50]
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
    $eleForm.attr("action", CONTEXT_PATH + '/front/assembleAppSnapList');
    $("#pageNumKey").val(_curr);
    $("#pageSizeKey").val(_limit);
    $("#pagesyssceneid").val($('#querysyssceneid').val());
    $eleForm.submit();
}


function checkAllBox(){
    $("input[name='conterList']").attr("checked",true);
}


