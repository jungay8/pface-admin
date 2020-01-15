$(function() {
    if (user == null) {
        //layer.msg("用户登录已超时，请重新登录！");
		$.alert("用户登录已超时，请重新登录！");
        //alert("用户登录已超时，请重新登录！");
        setTimeout(function () {
            window.location.href = "login.html";
        }, 1500);
    }
});