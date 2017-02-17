package cn.yunyichina.log.service.api.mapper;

import cn.yunyichina.log.common.entity.base.BaseMapper;
import cn.yunyichina.log.common.entity.entity.do_.KeywordIndex;
import cn.yunyichina.log.common.entity.entity.do_.MidCollectorKeyword;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Created by Jonven on 2017/1/4.
 */
@Repository
public interface KeywordIndexMapper extends BaseMapper<KeywordIndex> {

    public KeywordIndex findByName(String keywordIndexName);

    public int insertMid(Integer collertorId, Integer keywordIndexId);

    public void removeById(Integer id);

    public MidCollectorKeyword findMidCollectorKeyword(Integer collertorId, Integer keywordIndexId);

    public Set<MidCollectorKeyword> selectMidByCollectorId(Integer collertorId);

    public List<MidCollectorKeyword> queryMidByKeywordId(Integer keywordId);

    public int updateMid(Integer collectorId, Integer oldKeywordId, Integer newKeywordId);

    public List<KeywordIndex> selectKeywordInId(List<Integer> idList);

    public List<MidCollectorKeyword> selectAllMid();
}
