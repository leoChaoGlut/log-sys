package cn.yunyichina.log.service.api.mapper;


import cn.yunyichina.log.common.entity.base.BaseMapper;
import cn.yunyichina.log.common.entity.entity.do_.KvIndex;
import cn.yunyichina.log.common.entity.entity.do_.MidCollectorKv;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Created by Jonven on 2017/1/4.
 */
@Repository
public interface KvIndexMapper extends BaseMapper<KvIndex> {

    public KvIndex findByKey(String KvIndexName);

    public int insertMid(Integer collertorId, Integer KvIndexId);

    public void removeById(Integer id);

    public MidCollectorKv findMidCollectorKv(Integer collertorId, Integer KvIndexId);

    public List<MidCollectorKv> queryMidByKvId(Integer kvId);

    public int updateMid(Integer collectorId, Integer oldKvId, Integer newKvId);

    public Set<MidCollectorKv> selectMidByCollectorId(Integer collertorId);

    public List<KvIndex> selectKvInId(List<Integer> idList);

    public List<MidCollectorKv> selectAllMid();

}
