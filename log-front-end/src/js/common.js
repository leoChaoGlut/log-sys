/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/12/26 14:29
 * @Description:
 */

var Common = (function () {

    var TIME_OUT = 15000;

    var DOMAIN = "http://log.yunyichina.cn";
    var GATEWAY = DOMAIN + ":10300";
    var SEARCHER_GATEWAY = DOMAIN + ":10500";

    var get = function (url, completeCallback) {
        $.ajax({
            timeout: TIME_OUT,
            url: url,
            complete: completeCallback,
        })
    }

    var post = function (url, data, completeCallback) {
        $.ajax({
            type: "POST",
            timeout: TIME_OUT,
            url: url,
            // data: {
            //     json: encodeURIComponent(JSON.stringify(data)),
            // },
            data: data,
            complete: completeCallback,
        })
    }

    return {
        get: get,
        post: post,
        DOMAIN: DOMAIN,
        GATEWAY: GATEWAY,
        SEARCHER_GATEWAY: SEARCHER_GATEWAY,
    }
})()