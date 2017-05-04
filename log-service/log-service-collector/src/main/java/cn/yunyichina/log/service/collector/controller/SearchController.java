package cn.yunyichina.log.service.collector.controller;

import cn.yunyichina.log.common.base.AbstractController;
import cn.yunyichina.log.common.entity.dto.ResponseBodyDTO;
import cn.yunyichina.log.common.entity.dto.SearchConditionDTO;
import cn.yunyichina.log.service.collector.service.SearchService;
import cn.yunyichina.log.service.common.entity.dto.ContextInfoDTO;
import cn.yunyichina.log.service.common.entity.dto.LogResultDTO;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/2/28 18:33
 * @Description:
 */
@RestController
@RequestMapping("search")
public class SearchController extends AbstractController {

    final Logger logger = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    SearchService searchService;

    @PostMapping("history")
    ResponseBodyDTO<List<LogResultDTO>> searchHistory(
            SearchConditionDTO condition
    ) throws Exception {
        logger.info("搜索开始:" + JSON.toJSONString(condition, true));
        long begin = System.nanoTime();
        List<LogResultDTO> logResultList = searchService.searchHistory(condition);
        logger.info("搜索结束,总耗时:" + BigDecimal.valueOf(System.nanoTime() - begin, 9) + " 秒," + logResultList.size());
        return ResponseBodyDTO.ok(logResultList);
    }

    @PostMapping("by/contextId")
    ResponseBodyDTO<String> searchByContextId(
            Integer collectedItemId,
            String contextId
    ) throws Exception {
        logger.info(collectedItemId + " - " + contextId);
        String logContent = searchService.searchByContextId(collectedItemId, contextId);
        return ResponseBodyDTO.ok(logContent);
    }

    @PostMapping("by/contextInfoDTO")
    ResponseBodyDTO<String> searchByContextInfoDTO(
            @RequestBody ContextInfoDTO contextInfoDTO
    ) throws Exception {
        logger.info(JSON.toJSONString(contextInfoDTO, true));
        String logContent = searchService.readLogContentBy(contextInfoDTO);
        return ResponseBodyDTO.ok(logContent);
    }
}
