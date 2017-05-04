package cn.yunyichina.log.component.filter.imp;

import cn.yunyichina.log.common.constant.Tag;
import cn.yunyichina.log.component.filter.AbstractFilter;
import cn.yunyichina.log.component.filter.Filter;
import cn.yunyichina.log.component.filter.entity.FilterConditionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * Created by Csy on 2017年4月5日
 */
public class ContextFilter extends AbstractFilter implements Filter<String> {

    Logger logger = LoggerFactory.getLogger(ContextFilter.class);

    public ContextFilter() {
    }

    public ContextFilter(String inputStr, FilterConditionDTO filterCondition) {
        this.inputStr = inputStr;
        this.filterCondition = filterCondition;
    }

    @Override
    public String filter() {
        long begin = System.nanoTime();
        int cursor = 0;
        int contextIdBeginTagIndex;
        int contextIdEndTagIndex;
        String contextId = "";
        StringBuilder filteredLog = new StringBuilder(inputStr.length());
        if (filterCondition != null) {
            //循环找到CtxBegin上下文
            while (0 <= (cursor = inputStr.indexOf(Tag.CONTEXT_BEGIN, cursor))) {
                contextIdBeginTagIndex = cursor + Tag.CONTEXT_BEGIN.length();
                contextIdEndTagIndex = inputStr.indexOf(Tag.CONTEXT_ID_END, contextIdBeginTagIndex);
                if (0 <= contextIdEndTagIndex && contextIdBeginTagIndex < contextIdEndTagIndex) {//防止标记异常
                    contextId = inputStr.substring(contextIdBeginTagIndex, contextIdEndTagIndex);

                    if (contextId.equals(filterCondition.getContextId())) {
                        String contextBeforeBeginTag = inputStr.substring(0, cursor);
                        String contextBeginLog = "";
                        int contextBeforeBeginTagIndex = contextBeforeBeginTag.lastIndexOf(Tag.CONTEXT_ID_END);
                        if (contextBeforeBeginTagIndex > 0) {
                            int contextIdEndIndex = contextBeforeBeginTagIndex + Tag.CONTEXT_ID_END.length();
                            if (contextIdEndIndex < cursor) {
                                contextBeginLog = inputStr.substring(contextIdEndIndex, cursor);
                            }
                        } else {
                            contextBeginLog = contextBeforeBeginTag;
                        }
                        filteredLog.append(contextBeginLog);
                        break;
                    } else {
                        cursor = contextIdBeginTagIndex;
                    }
                }
            }
        }

        String ctxRowBegin = Tag.ROW_END + contextId + Tag.CONTEXT_ID_END;
        cursor = inputStr.indexOf(ctxRowBegin) + ctxRowBegin.length();
        //循环添加RowEnd
        while (0 <= (cursor = inputStr.indexOf(Tag.ROW_END + contextId, cursor))) {
            String contextBeforeTagRowEnd = inputStr.substring(0, cursor);
            int contextBeforeTagRowEndIndex = contextBeforeTagRowEnd.lastIndexOf(Tag.CONTEXT_ID_END);
            if (contextBeforeTagRowEndIndex > 0) {
                int contextIdEndIndex = contextBeforeTagRowEndIndex + Tag.CONTEXT_ID_END.length();
                if (contextIdEndIndex < cursor) {
                    String contextRowLog = inputStr.substring(contextIdEndIndex, cursor);
                    filteredLog.append("\r\n" + contextRowLog.trim());
                }
            }
            cursor += ctxRowBegin.length();
        }
        logger.info("上下文过滤耗时:" + BigDecimal.valueOf(System.nanoTime() - begin, 9));
        return filteredLog.toString();
    }

}
