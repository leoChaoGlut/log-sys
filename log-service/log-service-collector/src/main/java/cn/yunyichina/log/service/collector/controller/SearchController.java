package cn.yunyichina.log.service.collector.controller;

import cn.yunyichina.log.common.entity.dto.LogResultDTO;
import cn.yunyichina.log.common.entity.dto.ResponseDTO;
import cn.yunyichina.log.common.entity.dto.SearchConditionDTO;
import cn.yunyichina.log.service.collector.service.SearchService;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2017/2/28 18:33
 * @Description:
 */
@RestController
@RequestMapping("search")
public class SearchController {

    final Logger logger = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    SearchService searchService;

    @PostMapping("history")
    ResponseDTO searchHistory(
            @RequestBody SearchConditionDTO condition
    ) {
        try {
            logger.info(JSON.toJSONString(condition, true));
            List<LogResultDTO> logResultList = searchService.searchHistory(condition);
            return ResponseDTO.ok(logResultList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDTO.error(e.getMessage());
        }
    }
}
