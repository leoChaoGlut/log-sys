var Index = (function () {

    $(function () {
        var tabHeight = $(".el-tabs__header").height();
        var logContentHeight = $(window).height() - $(window).width() * 0.02 - 15 - tabHeight;

        $(".panel-tab").css("height", tabHeight + "px");
        $(".panel-tab").css("line-height", tabHeight + "px");
        $(".log-content").css("height", logContentHeight + "px");
        $("#foundLogFile").css("height", logContentHeight + "px");

    })
    $(window).resize(function () {
        var tabHeight = $(".el-tabs__header").height();
        var logContentHeight = $(window).height() - $(window).width() * 0.02 - 15 - tabHeight;

        $(".panel-tab").css("height", tabHeight + "px");
        $(".panel-tab").css("line-height", tabHeight + "px");
        $(".log-content").css("height", logContentHeight + "px");
        $("#foundLogFile").css("height", logContentHeight + "px");
    });

    var loading;

    var DOUBLE_CLICK_DURATION = 400;
    var MAX_LOGIN_COUNT = 3;//登录次数,超过该次数,将不会出现使用提示.
    var CACHE = {
        LOGIN_COUNT: "loginCount",
    }
    var USER_NAME_LIST = ["老板", "大兄弟", "大兄弟", "客官", "大佬", "亲", "亲", "亲"]
    var SEARCH_ENGINE_TYPE = {
        KEYWORD: 0,
        KV: 1,
        NO_INDEX: 2
    }
    var PANEL = {
        SEARCH: "searchPanel",
        DOWNLOAD: "downloadPanel"
    }

    function historySearch(data) {
        Common.post(Common.SEARCHER_GATEWAY, data, function (resp) {
            if (loading) {
                loading.close();
            }
            if (200 == resp.status) {
                var respBody = JSON.parse(resp.responseText);
                if (0 == respBody.code) {
                    var i, j;
                    var logFileList = [];
                    var firstLogResult = respBody.result[0];
                    if (firstLogResult) {
                        vm.logContent = firstLogResult.contextStr;
                    }
                    for (i = 0; i < respBody.result.length; i++) {//result 不会为空
                        var logResult = respBody.result[i];
                        var logRegionSet = logResult.logRegionSet;
                        var logTreeNode = {
                            beginLog: logRegionSet[0],
                            contextStr: logResult.contextStr,
                            endLogList: [],
                        }
                        for (j = 1; j < logRegionSet.length; j++) {
                            logTreeNode.endLogList.push({
                                beginLog: logRegionSet[j],
                                contextStr: logResult.contextStr,
                            });
                        }
                        logFileList.push(logTreeNode);
                    }
                    vm.logFileList = logFileList;
                } else {
                    vm.submitting = false;
                    vm.$message({
                        showClose: true,
                        title: "服务器返回异常",
                        message: respBody.msg,
                        type: 'error'
                    });
                }
            } else {
                vm.$message({
                    showClose: true,
                    title: "Http请求失败",
                    message: resp.status + ":" + resp.statusText,
                    type: 'error'
                });
            }
        });
    }

    function realtimeSearch(data) {

    }

    function searchRangeLog(data) {

    }

    function requestSearchOption() {
        Common.get(Common.GATEWAY + "/api/front/options", function (resp) {
            if (200 == resp.status) {
                var respBody = JSON.parse(resp.responseText);
                if (0 == respBody.code) {
                    vm.groupCollectorMap = respBody.result.groupCollectorMap;
                    if (vm.groupCollectorMap) {
                        for (var group in vm.groupCollectorMap) {
                            if (vm.groupCollectorMap[group].length > 0) {
                                vm.searchCondition.collector = vm.groupCollectorMap[group][0];
                                break;
                            }
                        }
                    }
                    vm.collectorKeywordMap = respBody.result.collectorKeywordMap;
                    vm.collectorKeyMap = respBody.result.collectorKeyMap;
                } else {
                    vm.$notify.error({
                        title: '获取搜索选项错误',
                        message: respBody.status + ":" + respBody.statusText,
                    });
                }
            } else {
                vm.$message({
                    showClose: true,
                    title: "Http请求失败",
                    message: resp.status + ":" + resp.statusText,
                    type: 'error'
                });
            }
        })
    }

    function validation() {
        var user = USER_NAME_LIST[parseInt(Math.random() * USER_NAME_LIST.length)];
        var validate = {
            pass: false,
            msg: "",
        }
        if (PANEL.SEARCH == vm.activedPanel) {
            if ("" == vm.searchCondition.collector.name) {
                validate.msg = user + ",请选择日志节点";
            } else if ("" == vm.searchCondition.beginDatetime) {
                validate.msg = user + ",请选择开始时间";
            } else if ("" == vm.searchCondition.endDatetime) {
                validate.msg = user + ",请选择结束时间";
            } else if (SEARCH_ENGINE_TYPE.KEYWORD == vm.searchCondition.searchEngineType) {
                if ("" == vm.searchCondition.keyword) {
                    validate.msg = user + ",请选择关键词";
                } else {
                    validate.pass = true;
                }
            } else if (SEARCH_ENGINE_TYPE.KV == vm.searchCondition.searchEngineType) {
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
        } else if (PANEL.DOWNLOAD == vm.activedPanel) {
            if ("" == vm.searchCondition.beginDatetime) {
                validate.msg = user + ",请选择开始时间";
            } else if ("" == vm.searchCondition.endDatetime) {
                validate.msg = user + ",请选择结束时间";
            } else {
                validate.pass = true;
            }
        }

        return validate;
    }

    var vm = new Vue({
        el: "#vmContainer",
        mounted: function () {
            requestSearchOption();
            $("#vmContainer").fadeIn(500);
            var loginCount = localStorage.getItem(CACHE.LOGIN_COUNT);
            if (!loginCount) {
                loginCount = 0;
            }
            if (loginCount < MAX_LOGIN_COUNT) {
                setTimeout(function () {
                    vm.$notify.info({
                        title: "提示1",
                        message: "部分文字标题有使用提示(鼠标悬浮会出现问号),长按标题可出现文字提示.",
                        duration: 4000,
                    });
                    setTimeout(function () {
                        vm.$notify.info({
                            title: "提示2",
                            message: "'上下文详情',仅显示关键词所处的上下文.",
                            duration: 8000,
                        });
                    }, 500);
                }, 500);
                localStorage.setItem(CACHE.LOGIN_COUNT, parseInt(loginCount) + 1);
            }
        },
        data: {
            form: {
                name: '',
                region: '',
                date1: '',
                date2: '',
                delivery: false,
                type: [],
                resource: '',
                desc: ''
            },
            showRealtime: false,
            realtime: false,
            //
            searchCondition: {
                searchEngineType: 1,
                fuzzy: false,
                beginDatetime: "",
                endDatetime: "",
                noIndexKeyword: "",
                keyword: "",
                key: "",
                value: "",
                collector: {
                    name: "",
                    application_name: "",
                    group: "",
                    create_time: 0,
                }
            },
            keywordList: [],
            keyValueList: [],
            submitting: false,
            logFileList: [],
            defaultProps: {
                label: 'beginLog',
                children: 'endLogList',
            },
            activedPanel: PANEL.SEARCH,
            logContent: "",
            //
            lastClickNode: {},
            lastClickNodeTime: new Date(),
            //
            groupCollectorMap: {},
            collectorKeywordMap: {},
            collectorKeyMap: {},
            //
        },
        methods: {
            submit: function () {
                var validate = validation();
                if (validate.pass) {
                    if (PANEL.SEARCH == vm.activedPanel) {
                        vm.logContent = "";
                        vm.submitting = true;
                        loading = vm.$loading({
                            text: "拼命加载中~",
                            lock: true,
                            target: ".search-panel"
                        });
                        if (vm.realtime) {
                            var data = {
                                searchEngineType: vm.searchCondition.searchEngineType,
                                fuzzy: vm.searchCondition.fuzzy,
                                noIndexKeyword: vm.searchCondition.noIndexKeyword,
                                keyword: vm.searchCondition.keyword,
                                key: vm.searchCondition.key,
                                value: vm.searchCondition.value,
                            }
                            realtimeSearch(data);
                        } else {
                            var data = {
                                json: encodeURIComponent(JSON.stringify({
                                    searchEngineType: vm.searchCondition.searchEngineType,
                                    fuzzy: vm.searchCondition.fuzzy,
                                    beginDatetime: vm.searchCondition.beginDatetime.getTime(),
                                    endDatetime: vm.searchCondition.endDatetime.getTime(),
                                    noIndexKeyword: vm.searchCondition.noIndexKeyword,
                                    keyword: vm.searchCondition.keyword,
                                    key: vm.searchCondition.key,
                                    value: vm.searchCondition.value,
                                    collector: {
                                        name: vm.searchCondition.collector.name,
                                    }
                                }))
                            }
                            console.log(data);
                            historySearch(data);
                        }
                    } else if (PANEL.DOWNLOAD == vm.activedPanel) {
                        vm.$message({
                            showClose: true,
                            message: '待开发........',
                            type: 'error'
                        });
                        return;
                        vm.submitting = true;
                        loading = vm.$loading({
                            text: "拼命加载中~",
                            lock: true,
                            target: ".search-panel"
                        });
                        var data = {
                            collector: {
                                name: vm.searchCondition.collector.name,
                            },
                            beginDatetime: vm.searchCondition.beginDatetime.getTime(),
                            endDatetime: vm.searchCondition.endDatetime.getTime(),
                        }
                        searchRangeLog(data);
                    }
                } else {
                    vm.$notify({
                        showClose: true,
                        title: "提示",
                        message: validate.msg,
                        type: 'warning'
                    });
                }

            },
            handleNodeClick: function (obj, node, component) {
                vm.logContent = obj.contextStr;
                if (vm.lastClickNode == obj.beginLog) {
                    var now = new Date().getTime();
                    if (now - vm.lastClickNodeTime > DOUBLE_CLICK_DURATION) {
                        vm.lastClickNodeTime = now;
                    } else {
                        // 完成下载功能后再解开注释
                        // vm.$confirm("确定要下载 " + obj.beginLog + " 的日志吗?", "提示", {
                        //     confirmButtonText: '确定',
                        //     cancelButtonText: '取消',
                        //     type: 'info',
                        //     closeOnPressEscape: true,
                        //     callback: function (action) {
                        //         switch (action) {
                        //             case "confirm":
                        //                 vm.$notify({
                        //                     showClose: true,
                        //                     title: "提示",
                        //                     message: "功能开发中~",
                        //                     type: 'warning'
                        //                 });
                        //                 break;
                        //             case "cancel":
                        //                 vm.$notify({
                        //                     showClose: true,
                        //                     title: "提示",
                        //                     message: "功能开发中~",
                        //                     type: 'warning'
                        //                 });
                        //                 break;
                        //             default:
                        //                 break;
                        //         }
                        //     }
                        // });
                    }
                } else {
                    vm.lastClickNode = obj.beginLog;
                    vm.lastClickNodeTime = new Date().getTime();
                }
            },
            handleTabClick: function (tab) {
                vm.activedPanel = tab.name;
            }
        },
        computed: {},
        watch: {
            "searchCondition.beginDatetime": function (val) {
                var endDatetime = vm.searchCondition.endDatetime;
                if (endDatetime) {
                    if (val >= endDatetime) {
                        var user = USER_NAME_LIST[parseInt(Math.random() * USER_NAME_LIST.length)];
                        vm.$notify({
                            showClose: true,
                            title: "提示",
                            message: user + ",开始时间大于结束时间了~",
                            type: 'warning'
                        });
                        vm.searchCondition.beginDatetime = "";
                    } else {
                    }
                } else {

                }
            },
            "searchCondition.endDatetime": function (val) {
                var beginDatetime = vm.searchCondition.beginDatetime;
                if (beginDatetime) {
                    if (val <= beginDatetime) {
                        var user = USER_NAME_LIST[parseInt(Math.random() * USER_NAME_LIST.length)];
                        vm.$notify({
                            showClose: true,
                            title: "提示",
                            message: user + ",开始时间小于结束时间了~",
                            type: 'warning'
                        });
                        vm.searchCondition.endDatetime = "";
                    } else {
                    }
                } else {

                }
            },
            "searchCondition.collector": function (collector) {
                console.log(collector);
                vm.searchCondition.keyword = "";
                vm.searchCondition.key = "";
            }
        }
    })

    return {}
})()