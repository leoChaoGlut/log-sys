package cn.yunyichina.log.service.api.mapper;

import cn.yunyichina.log.common.base.BaseMapper;
import cn.yunyichina.log.component.entity.do_.KvIndex;
import cn.yunyichina.log.component.entity.do_.MidCollectorKv;
import org.springframework.stereotype.Repository;

import java.util.List;

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

}
