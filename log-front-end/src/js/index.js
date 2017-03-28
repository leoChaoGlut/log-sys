/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/3/2 11:47
 * @Description:
 */

var Home = (function () {

    var ANIMATION_DURATION = 1500;
    var USER_NAME_LIST = ["老板", "大兄弟", "大兄弟", "客官", "大佬", "亲", "亲", "亲"];

    /**
     * @See cn.yunyichina.log.common.constant.SearchEngineType
     */
    var SEARCH_ENGINE_TYPE = {
        KEYWORD: 200,
        KEY_VALUE: 201,
        NO_INDEX: 202,
    }
    var DOUBLE_CLICK_DURATION = 400;
    var MAX_CHAR_SIZE = 50000;

    var loading,
        realtimeSocket,
        historySocket
        ;
    //================= tips begin =====================

    var Tips = (function () {

        var Type = {
            success: 'success',
            info: 'info',
            warning: 'warning',
            error: 'error',
        }

        var success = function (msg) {
            vm.$message({
                message: msg,
                type: Type.success,
                duration: ANIMATION_DURATION,
            });
        }

        var info = function (msg) {
            vm.$message({
                message: msg,
                type: Type.info,
                duration: ANIMATION_DURATION,
            });
        }

        var warning = function (msg) {
            vm.$message({
                message: msg,
                type: Type.warning,
                duration: ANIMATION_DURATION,
            });
        }

        var error = function (msg) {
            vm.$message({
                message: msg,
                type: Type.error,
                duration: ANIMATION_DURATION,
            });
        }

        return {
            success: success,
            info: info,
            warning: warning,
            error: error,
            Type: Type,
        }

    })()
    //================= tips end =====================

    //================= http util begin =====================
    var Http = (function () {
        var get = function (url, successCallback) {
            $.ajax({
                timeout: Common.TIME_OUT,
                url: url,
                complete: function (resp) {
                    if (200 == resp.status) {
                        var respBody = JSON.parse(resp.responseText);
                        if (Common.Status.OK == respBody.code) {
                            if (successCallback) {
                                successCallback(respBody.result);
                            }
                        } else {
                            Tips.error('code:' + respBody.code + ", msg:" + respBody.msg);
                        }
                    } else {
                        if ("timeout" == resp.statusText) {
                            Tips.error("请求超时")
                        } else {
                            Tips.error("连接服务器异常,code:" + resp.status + ",msg:" + resp.statusText);
                        }
                    }
                },
            })
        }
        var post = function (url, data, successCallback) {
            $.ajax({
                url: url,
                data: JSON.stringify(data),
                timeout: Common.TIME_OUT,
                type: "POST",
                dataType: "json",
                contentType: "application/json",
                complete: function (resp) {
                    if (loading) {
                        loading.close();
                    }
                    if (200 == resp.status) {
                        var respBody = JSON.parse(resp.responseText);
                        if (Common.Status.OK == respBody.code) {
                            if (successCallback) {
                                successCallback(respBody.result);
                            }
                        } else {
                            Tips.error('code:' + respBody.code + ", msg:" + respBody.msg);
                        }
                    } else {
                        if ("timeout" == resp.statusText) {
                            Tips.error("请求超时")
                        } else {
                            Tips.error("连接服务器异常,code:" + resp.status + ",msg:" + resp.statusText);
                        }
                    }
                },
            })
        }
        return {
            get: get,
            post: post,
        }
    })()

    //================= http util end =====================
    function menu(title, icon, subMenuList) {
        return {
            title: title,
            icon: icon,
            subMenuList: subMenuList,
        }
    }

    function validation() {
        var user = USER_NAME_LIST[parseInt(Math.random() * USER_NAME_LIST.length)];
        var validate = {
            pass: false,
            msg: "",
        }
        if (!vm.searchCondition.beginDateTime || "" == vm.searchCondition.beginDateTime) {
            validate.msg = user + ",请选择开始时间";
        } else if (!vm.searchCondition.endDateTime || "" == vm.searchCondition.endDateTime) {
            validate.msg = user + ",请选择结束时间";
        } else if (SEARCH_ENGINE_TYPE.KEYWORD == vm.searchCondition.searchEngineType) {
            if ("" == vm.searchCondition.keyword) {
                validate.msg = user + ",请选择关键词";
            } else {
                validate.pass = true;
            }
        } else if (SEARCH_ENGINE_TYPE.KEY_VALUE == vm.searchCondition.searchEngineType) {
            if ("" == vm.searchCondition.key) {
                validate.msg = user + ",请选择key";
            } else if ("" == vm.searchCondition.value) {
                validate.msg = user + ",请输入value";
            } else {
                validate.pass = true;
            }
        } else if (SEARCH_ENGINE_TYPE.NO_INDEX == vm.searchCondition.searchEngineType) {
            if ("" == vm.searchCondition.noIndexKeyword) {
                validate.msg = user + ",请输入关键词";
            } else {
                validate.pass = true;
            }
        } else {
            validate.pass = true;
        }

        return validate;
    }


    (function () {
        var url = Common.GATEWAY + "/collector-service/collector/all";
        // var url = Common.GATEWAY + "/log-service-collector-service/collector/all";
        Http.get(url, function (result) {
            var interval = setInterval(function () {
                if (vm) {
                    vm.collectorList = result;
                    for (var i = 0; i < vm.collectorList.length; i++) {
                        if (vm.collectorList[i].collectedItemList.length > 0) {
                            vm.collectedItem = vm.collectorList[i].collectedItemList[0];
                        }
                    }
                    clearInterval(interval);
                }
            }, 100);
        })
    })()

    function getCollectorBy(collectorList, collectorId) {
        for (var i = 0; i < collectorList.length; i++) {
            var collector = collectorList[i]
            if (collector.id == collectorId) {
                return collector;
            }
        }
        return null;
    }

    function buildUrlBy(collector, suffix) {
        if (collector.reachable == "1") {
            return "http://" + collector.ip + ":" + collector.port + "/" + suffix;
        } else {
            return Common.GATEWAY + "/" + collector.applicationName + "/" + suffix;
        }
    }

    var vm = new Vue({
        el: "#app",
        mounted: function () {
            var panelHeight = ($(window).height() - $(".el-tabs__header").height() - 15 - 30 - 30) + "px";
            this.style.logResultList = {
                "height": panelHeight,
            }
            this.style.logContent = {
                "height": panelHeight,
                "overflow-y": "auto",
                "word-wrap": "break-word",
            }
            $("#app").fadeIn(ANIMATION_DURATION);
        },
        data: {
            logResultList: [],
            lastClickNode: {},
            lastClickNodeTime: new Date(),
            SEARCH_ENGINE_TYPE: SEARCH_ENGINE_TYPE,
            collectorList: [],
            keywordTagList: [],
            kvTagList: [],
            activedMenu: "0-0",
            menuList: [
                menu("搜索", "el-icon-information", [
                    menu("历史日志", "el-icon-message"),
                ]),
                menu("查看", "el-icon-information", [
                    menu("实时日志", "el-icon-setting"),
                    menu("历史日志", "el-icon-message"),
                ]),
                menu("管理", "el-icon-menu", [
                    menu("采集节点", "el-icon-date"),
                ]),
            ],
            user: {
                username: "",
            },
            searchCondition: {
                searchEngineType: 201,
                fuzzy: false,
                beginDateTime: "",
                endDateTime: "",
                noIndexKeyword: "",
                keyword: "",
                key: "",
                value: "",
                collectedItemId: -1,
            },
            activedPanel: "panel",
            collectedItem: "",
            logContent: "",
            defaultProps: {
                label: 'beginLog',
                children: 'endLogList',
            },
            style: {
                logResultList: {},
                logContent: {}
            },
            linking: false,
            reading: false,
            logContentTitle: "",
            showLogContentDialog: false,
            lockScroll: false,
            readingRealtimeLog: false,
            noMoreContentToBeLoad: false,
        },
        methods: {
            menuSelect: function (index, indexPath) {
                vm.activedMenu = index;

            },
            search: function () {
                var validate = validation();
                if (validate.pass) {
                    vm.logContent = "";
                    loading = vm.$loading();
                    var collectorId = vm.collectedItem.collectorId;
                    var collector = getCollectorBy(vm.collectorList, collectorId);
                    var url = buildUrlBy(collector, "search/history");
                    Http.post(url, vm.searchCondition, function (result) {
                        if (result.length > 0) {
                            var logResultList = [];
                            var firstLogResult = result[0];
                            if (firstLogResult) {
                                vm.logContent = firstLogResult.contextContent;
                            } else {
                                vm.logContent = "";
                            }
                            for (var i = 0; i < result.length; i++) {//result 不会为空
                                var logResult = result[i];
                                var logRegionSet = logResult.logRegionSet;
                                var logTreeNode = {
                                    beginLog: logRegionSet[0],
                                    contextContent: logResult.contextContent,
                                    endLogList: [],
                                }
                                for (var j = 1; j < logRegionSet.length; j++) {
                                    logTreeNode.endLogList.push({
                                        beginLog: logRegionSet[j],
                                        contextContent: logResult.contextContent,
                                    });
                                }
                                logResultList.push(logTreeNode);
                            }
                            vm.logResultList = logResultList;
                        } else {
                            Tips.warning("无匹配日志");
                        }
                    })
                } else {
                    Tips.warning(validate.msg);
                }
            },
            loadLogContent: function (obj, node, component) {
                vm.logContent = obj.contextContent;
                if (vm.lastClickNode == obj.beginLog) {
                    var now = new Date().getTime();
                    if (now - vm.lastClickNodeTime > DOUBLE_CLICK_DURATION) {
                        vm.lastClickNodeTime = now;
                    } else {
                        vm.showLogContentDialog = true;
                        vm.logContentTitle = obj.beginLog;

                        var collectorId = vm.collectedItem.collectorId;
                        var collector = getCollectorBy(vm.collectorList, collectorId);
                        var url = buildUrlBy(collector, "history");

                        historySocket = new SockJS(url);
                        historySocket.onopen = function (e) {
                            loading.close();
                            Tips.success("连接成功");
                            var data = {
                                collectedItemId: vm.searchCondition.collectedItemId,
                                beginDateTime: vm.logContentTitle,
                                endDateTime: vm.logContentTitle,
                            }
                            vm.logContent = "";
                            historySocket.send(JSON.stringify(data));
                        }
                        historySocket.onmessage = function (e) {
                            vm.logContent += e.data;
                        }
                        historySocket.onerror = function (error) {
                            loading.close();
                            Tips.error(JSON.stringify(error));
                        }
                        historySocket.onclose = function (e) {
                            vm.reading = false;
                        }
                    }
                } else {
                    vm.lastClickNode = obj.beginLog;
                    vm.lastClickNodeTime = new Date().getTime();
                }
            },
            readRealtimeLog: function () {
                vm.noMoreContentToBeLoad = false;
                vm.readingRealtimeLog = true;
                if (vm.linking) {
                    vm.linking = false;
                    if (realtimeSocket) {
                        realtimeSocket.close();
                        Tips.warning("已 断开");
                    }
                } else {
                    vm.linking = true;
                    loading = vm.$loading();

                    var collectorId = vm.collectedItem.collectorId;
                    var collector = getCollectorBy(vm.collectorList, collectorId);
                    var url = buildUrlBy(collector, "realtime");

                    realtimeSocket = new SockJS(url);
                    realtimeSocket.onopen = function (e) {
                        loading.close();
                        Tips.success("已 连接");
                        realtimeSocket.send(vm.collectedItem.id);
                        vm.logContent = "";
                    }
                    realtimeSocket.onmessage = function (e) {
                        vm.logContent += "<br/>" + e.data;
                    }
                    realtimeSocket.onerror = function (error) {
                        loading.close();
                        Tips.error(JSON.stringify(error));
                    }
                    realtimeSocket.onclose = function (e) {
                        Tips.warning("连接已断开");
                    }
                }
            },
            readHistoryLog: function () {
                vm.noMoreContentToBeLoad = false;
                vm.readingRealtimeLog = false;
                if (vm.reading) {
                    vm.reading = false;
                    if (historySocket) {
                        historySocket.close();
                        Tips.warning("已 中断");
                    }
                } else {
                    var user = USER_NAME_LIST[parseInt(Math.random() * USER_NAME_LIST.length)];
                    var validate = {
                        pass: true,
                        msg: "",
                    }
                    if (!vm.searchCondition.beginDateTime || "" == vm.searchCondition.beginDateTime) {
                        validate.msg = user + ",请选择开始时间";
                        validate.pass = false;
                    } else if (!vm.searchCondition.endDateTime || "" == vm.searchCondition.endDateTime) {
                        validate.msg = user + ",请选择结束时间";
                        validate.pass = false;
                    }
                    if (validate.pass) {
                        vm.reading = true;
                        loading = vm.$loading();

                        var collectorId = vm.collectedItem.collectorId;
                        var collector = getCollectorBy(vm.collectorList, collectorId);
                        var url = buildUrlBy(collector, "history");

                        historySocket = new SockJS(url);
                        historySocket.onopen = function (e) {
                            vm.logContent = "";
                            loading.close();
                            vm.$message({
                                message: "正在获取,获取完毕后,在日志最下方会有 '===结束===' 字样",
                                type: 'success',
                                duration: ANIMATION_DURATION * 2,
                            });
                            historySocket.send(JSON.stringify(vm.searchCondition));
                        }
                        historySocket.onmessage = function (e) {
                            vm.logContent += e.data;
                        }
                        historySocket.onerror = function (error) {
                            loading.close();
                            Tips.error(JSON.stringify(error));
                        }
                        historySocket.onclose = function (e) {
                            vm.noMoreContentToBeLoad = true;
                            vm.reading = false;
                            Tips.warning("连接已断开");
                        }
                        historySocket.onerror = function (e) {
                            Tips.warning(JSON.stringify(e));
                        }
                    } else {
                        Tips.warning(validate.msg);
                    }
                }
            },
            readMoreHistoryLog: function () {
                historySocket.send();
            }
        },
        watch: {
            "collectedItem": function (collectedItem) {
                vm.searchCondition.key = "";
                vm.searchCondition.keyword = "";
                vm.keywordTagList = collectedItem.keywordTagList
                vm.kvTagList = collectedItem.kvTagList;
                vm.searchCondition.collectedItemId = collectedItem.id;
                if ("1-0" == vm.activedMenu) {
                    vm.linking = false;
                    if (realtimeSocket) {
                        realtimeSocket.close();
                    }
                } else if ("1-1" == vm.activedMenu) {
                    vm.reading = false;
                    if (historySocket) {
                        historySocket.close();
                    }
                }
            },
            "searchCondition.beginDateTime": function (val) {
                var endDateTime = vm.searchCondition.endDateTime;
                if (endDateTime) {
                    if (val >= endDateTime) {
                        var user = USER_NAME_LIST[parseInt(Math.random() * USER_NAME_LIST.length)];
                        Tips.warning(user + ",开始时间大于结束时间了~");
                        vm.searchCondition.beginDateTime = "";
                    } else {

                    }
                } else {

                }
            },
            "searchCondition.endDateTime": function (val) {
                var beginDateTime = vm.searchCondition.beginDateTime;
                if (beginDateTime) {
                    if (val <= beginDateTime) {
                        var user = USER_NAME_LIST[parseInt(Math.random() * USER_NAME_LIST.length)];
                        Tips.warning(user + ",开始时间大于结束时间了~");
                        vm.searchCondition.endDateTime = "";
                    } else {

                    }
                } else {

                }
            },
            "activedMenu": function (val) {
                if ("1-0" != val) {
                    vm.linking = false;
                    if (realtimeSocket) {
                        realtimeSocket.close();
                    }
                } else if ("1-1" != val) {
                    vm.reading = false;
                    if (historySocket) {
                        historySocket.close();
                    }
                }
            },
            "logContent": function (val) {
                if (vm.lockScroll) {

                } else {
                    var realtimeLogContentPanel = document.getElementById('realtimeLogContentPanel');
                    realtimeLogContentPanel.scrollTop = realtimeLogContentPanel.scrollHeight
                }

                if (val.length > MAX_CHAR_SIZE) {
                    if (vm.readingRealtimeLog) {
                        vm.logContent = "";
                    } else {
                        Tips.warning("日志长度超过 2万 字符,继续加载能会造成卡顿");
                    }
                }
            }
        },
    })

    return {}
})()
