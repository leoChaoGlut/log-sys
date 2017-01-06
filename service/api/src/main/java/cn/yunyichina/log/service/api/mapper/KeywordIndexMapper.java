package cn.yunyichina.log.service.api.mapper;

import cn.yunyichina.log.common.base.BaseMapper;
import cn.yunyichina.log.component.entity.do_.KeywordIndex;
import cn.yunyichina.log.component.entity.do_.MidCollectorKeyword;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Jonven on 2017/1/4.
 */
@Repository
public interface KeywordIndexMapper extends BaseMapper<KeywordIndex> {

    public KeywordIndex findByName(String keywordIndexName);

    public int insertMid(Integer collertorId, Integer keywordIndexId);

    public void removeById(Integer id);

    public MidCollectorKeyword findMidCollectorKeyword(Integer collertorId, Integer keywordIndexId);

    public List<MidCollectorKeyword> queryMidByKeywordId(Integer keywordId);

    public int updateMid(Integer collectorId, Integer oldKeywordId, Integer newKeywordId);

}
