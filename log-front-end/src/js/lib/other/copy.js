/**
 * Created by zhoufeng on 2017/5/5 0005.
 */

$(document).unbind("mouseup").bind("mouseup", function (e) {
    var selectedText;
    var xx = e.originalEvent.x || e.originalEvent.layerX || 0;
    var yy = e.originalEvent.y || e.originalEvent.layerY || 0;

    if (window.getSelection) {//其它浏览器
        selectedText = window.getSelection();
    } else if (document.selection) {//ie浏览器
        selectedText = document.selection.createRange().text;
    }
    $("#sel_content").val(selectedText);
    if (selectedText != "") {
        $(".tooltip").css("display", "block");
        $(".tooltip").css("top", yy - 35);
        $(".tooltip").css("left", xx);
    } else {
        $(".tooltip").css("display", "none");
    }
    //阻止事件冒泡
    $('.tipfloat').unbind("mouseup").bind("mouseup", function (event) {  //  为input绑定click事件
        event.stopPropagation();    //  阻止事件冒泡
    });

    $("#search").unbind("click").bind("click", function () {
        window.location.href = "http://www.baidu.com/s?wd=" + selectedText;//这里是搜索事件，只是跳转到百度页面，未完成功能！
    })

    init();


})


function init() {
    var re = /[\\n]/g;
    var selectedText = $("#sel_content").val();
    var clip = new ZeroClipboard.Client(); // 新建一个对象
    clip.setHandCursor(true);
//        clip.setText(selectedText.replace(new RegExp("\\n",'gm'),'')); // 设置要复制的文本。
    var text = selectedText.replace(re, '');
    if (formatJson(text) == false) {
        clip.setText((formatXml(text))); // 设置要复制的xml文本。
    } else {
        clip.setText((formatJson(text))); // 设置要复制的json文本。
    }

    clip.addEventListener("mouseUp", function (client) {
        $(".tooltip").css("display", "none");
    });
    // 注册一个 button，参数为 id。点击这个 button 就会复制。
    clip.glue("copycardid");
}


//格式化代码函数,已经用原生方式写好了不需要改动,直接引用就好
String.prototype.removeLineEnd = function () {
    return this.replace(/(<.+?\s+?)(?:\n\s*?(.+?=".*?"))/g, '$1 $2')
}
function formatXml(text) {
    //去掉多余的空格
    text = '\n' + text.replace(/(<\w+)(\s.*?>)/g, function ($0, name, props) {
            return name + ' ' + props.replace(/\s+(\w+=)/g, " $1");
        }).replace(/>\s*?</g, ">\n<");

    //把注释编码
    text = text.replace(/\n/g, '\r').replace(/<!--(.+?)-->/g, function ($0, text) {
        var ret = '<!--' + escape(text) + '-->';
        //alert(ret);
        return ret;
    }).replace(/\r/g, '\n');

    //调整格式
    var rgx = /\n(<(([^\?]).+?)(?:\s|\s*?>|\s*?(\/)>)(?:.*?(?:(?:(\/)>)|(?:<(\/)\2>)))?)/mg;
    var nodeStack = [];
    var output = text.replace(rgx, function ($0, all, name, isBegin, isCloseFull1, isCloseFull2, isFull1, isFull2) {
        var isClosed = (isCloseFull1 == '/') || (isCloseFull2 == '/' ) || (isFull1 == '/') || (isFull2 == '/');
        //alert([all,isClosed].join('='));
        var prefix = '';
        if (isBegin == '!') {
            prefix = getPrefix(nodeStack.length);
        }
        else {
            if (isBegin != '/') {
                prefix = getPrefix(nodeStack.length);
                if (!isClosed) {
                    nodeStack.push(name);
                }
            }
            else {
                nodeStack.pop();
                prefix = getPrefix(nodeStack.length);
            }

        }
        var ret = '\n' + prefix + all;
        return ret;
    });

    var prefixSpace = -1;
    var outputText = output.substring(1);
    //alert(outputText);

    //把注释还原并解码，调格式
    outputText = outputText.replace(/\n/g, '\r').replace(/(\s*)<!--(.+?)-->/g, function ($0, prefix, text) {
        //alert(['[',prefix,']=',prefix.length].join(''));
        if (prefix.charAt(0) == '\r')
            prefix = prefix.substring(1);
        text = unescape(text).replace(/\r/g, '\n');
        var ret = '\n' + prefix + '<!--' + text.replace(/^\s*/mg, prefix) + '-->';
        //alert(ret);
        return ret;
    });

    return outputText.replace(/\s+$/g, '').replace(/\r/g, '\r\n');
}
function getPrefix(prefixIndex) {
    var span = '    ';
    var output = [];
    for (var i = 0; i < prefixIndex; ++i) {
        output.push(span);
    }

    return output.join('');
}


var formatJson = function (json, options) {
    try {
        var reg = null,
            formatted = '',
            pad = 0,
            PADDING = '    ';
        options = options || {};
        options.newlineAfterColonIfBeforeBraceOrBracket = (options.newlineAfterColonIfBeforeBraceOrBracket === true) ? true : false;
        options.spaceAfterColon = (options.spaceAfterColon === false) ? false : true;
        if (typeof json !== 'string') {
            json = JSON.stringify(json);
        } else {
            json = JSON.parse(json);
            json = JSON.stringify(json);
        }
        reg = /([\{\}])/g;
        json = json.replace(reg, '\r\n$1\r\n');
        reg = /([\[\]])/g;
        json = json.replace(reg, '\r\n$1\r\n');
        reg = /(\,)/g;
        json = json.replace(reg, '$1\r\n');
        reg = /(\r\n\r\n)/g;
        json = json.replace(reg, '\r\n');
        reg = /\r\n\,/g;
        json = json.replace(reg, ',');
        if (!options.newlineAfterColonIfBeforeBraceOrBracket) {
            reg = /\:\r\n\{/g;
            json = json.replace(reg, ':{');
            reg = /\:\r\n\[/g;
            json = json.replace(reg, ':[');
        }
        if (options.spaceAfterColon) {
            reg = /\:/g;
            json = json.replace(reg, ':');
        }
        (json.split('\r\n')).forEach(function (node, index) {
                var i = 0,
                    indent = 0,
                    padding = '';

                if (node.match(/\{$/) || node.match(/\[$/)) {
                    indent = 1;
                } else if (node.match(/\}/) || node.match(/\]/)) {
                    if (pad !== 0) {
                        pad -= 1;
                    }
                } else {
                    indent = 0;
                }

                for (i = 0; i < pad; i++) {
                    padding += PADDING;
                }

                formatted += padding + node + '\r\n';
                pad += indent;
            }
        );
        return formatted;
    } catch (e) {
        return false;
    }
    ;

};