var user = getSessionObj("user");
//后台服务的路径
var server={
    "path": "http://127.0.0.1:8880/pface-admin",
};


function storageSessionObj(key,obj) {
    var objJsonStr = JSON.stringify(obj);
    console.log("storageSessionObj",objJsonStr);
    sessionStorage.setItem(key, objJsonStr);
};

function getSessionObj(key) {
   var str= sessionStorage.getItem(key);
    var objAfter = JSON.parse(str);
    console.log(objAfter,typeof objAfter);
    return objAfter;
};

