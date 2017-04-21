package cn.yunyichina.log.component.filter.imp;

import cn.yunyichina.log.common.constant.Tag;
import cn.yunyichina.log.component.filter.AbstractFilter;
import cn.yunyichina.log.component.filter.Filter;
import cn.yunyichina.log.component.filter.entity.FilterConditionDTO;

/**
 * Created by Csy on 2017年4月5日
 */
public class ContextFilter extends AbstractFilter implements Filter<String> {

    public ContextFilter() {
    }

    public ContextFilter(String inputStr, FilterConditionDTO filterCondition) {
        this.inputStr = inputStr;
        this.filterCondition = filterCondition;
    }

    @Override
    public String filter() throws Exception {
        int cursor = 0;
        int contextIdBeginTagIndex;
        int contextIdEndTagIndex;
        String contextId = "";
        StringBuilder sb = new StringBuilder(inputStr.length());
        //循环找到CtxBegin上下文
        while (0 <= (cursor = inputStr.indexOf(Tag.CONTEXT_BEGIN, cursor))) {
            contextIdBeginTagIndex = cursor + Tag.CONTEXT_BEGIN.length();
            contextIdEndTagIndex = inputStr.indexOf(Tag.CONTEXT_ID_END, contextIdBeginTagIndex);
            contextId = inputStr.substring(contextIdBeginTagIndex, contextIdEndTagIndex);
            if (contextId.equals(filterCondition.getContextId())) {
                String contextBeforeBeginTag = inputStr.substring(0, cursor);
                String contextBeginStr = "";
                if (contextBeforeBeginTag.lastIndexOf(Tag.CONTEXT_ID_END) > 0) {
                    contextBeginStr = inputStr.substring(contextBeforeBeginTag.lastIndexOf(Tag.CONTEXT_ID_END) + Tag.CONTEXT_ID_END.length(), cursor);
                } else {
                    contextBeginStr = contextBeforeBeginTag;
                }
                sb.append(contextBeginStr.trim());
                break;
            } else {
                cursor = contextIdBeginTagIndex;
            }
        }
        String ctxRowBegin = Tag.ROW_END + contextId + Tag.CONTEXT_ID_END;
        cursor = inputStr.indexOf(ctxRowBegin) + ctxRowBegin.length();
        //循环添加RowEnd
        while (0 <= (cursor = inputStr.indexOf(Tag.ROW_END + contextId, cursor))) {
            String contextBeforeTagRowEnd = inputStr.substring(0, cursor);
            String contextRowStr = inputStr.substring(contextBeforeTagRowEnd.lastIndexOf(Tag.CONTEXT_ID_END) + Tag.CONTEXT_ID_END.length(), cursor);
            sb.append("\r\n" + contextRowStr.trim());
            cursor += ctxRowBegin.length();
        }
        return sb.toString();
    }

}
