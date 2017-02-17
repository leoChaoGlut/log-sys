package cn.yunyichina.log.service.api.mapper;


import cn.yunyichina.log.common.entity.base.BaseMapper;
import cn.yunyichina.log.common.entity.entity.do_.Collector;
import org.springframework.stereotype.Repository;

/**
 * Created by Jonven on 2017/1/4.
 */
@Repository
public interface CollectorMapper extends BaseMapper<Collector> {

    public Collector findByCollectorName(Collector collector);

    public Collector findByCollectorId(Integer collectorId);

}
