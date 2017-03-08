/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/2/9 17:30
 * @Description:
 */
var Login = (function () {

    (function () {
        var token = localStorage.getItem("token");
        if (token) {
            var username = localStorage.getItem("username");
            Common.get(Common.URL_PREFIX + "user/validate/" + username + "/" + token, function (resp) {
                if (200 == resp.status) {
                    var respBody = JSON.parse(resp.responseText);
                    if (Common.Status.OK == respBody.code) {
                        location.href = "index.html";
                    } else {
                        // Materialize.toast('错误:code:' + respBody.code + ", msg:" + respBody.msg, 2000);
                    }
                } else {
                    if ("timeout" == resp.statusText) {
                        Materialize.toast("请求超时", 2000);
                    } else {
                        Materialize.toast("连接服务器异常,msg:" + resp.statusText, 2000);
                    }
                }
            })
        } else {

        }
    })()

    var vm = new Vue({
        el: "#app",
        mounted: function () {
            $("#app").fadeIn(1500);
        },
        data: {
            submitting: false,
            username: "",
            password: "",
        },
        methods: {
            login: function () {
                if (vm.username.trim() != "" && vm.password.trim() != "") {
                    vm.submitting = true;
                    var data = {
                        username: vm.username,
                        password: vm.password,
                    }
                    Common.post(Common.URL_PREFIX + "user/login", data, function (resp) {
                        vm.submitting = false;
                        if (200 == resp.status) {
                            var respBody = JSON.parse(resp.responseText);
                            if (Common.Status.OK == respBody.code) {
                                localStorage.setItem("username", vm.username);
                                localStorage.setItem("token", respBody.result.token);
                                localStorage.setItem("editable", respBody.result.editable);
                                localStorage.setItem("name", respBody.result.name);
                                location.href = "index.html";
                            } else {
                                Materialize.toast('错误:code:' + respBody.code + ", msg:" + respBody.msg, 2000);
                            }
                        } else {
                            if ("timeout" == resp.statusText) {
                                Materialize.toast("请求超时", 2000);
                            } else {
                                Materialize.toast("连接服务器异常,msg:" + resp.statusText, 2000);
                            }
                        }
                    })
                } else {
                    Materialize.toast("用户名和密码都不能为空", 1500);
                }
            }
        }
    })
})()