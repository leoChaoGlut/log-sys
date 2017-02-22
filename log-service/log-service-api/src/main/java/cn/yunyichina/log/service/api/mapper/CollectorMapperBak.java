package cn.yunyichina.log.service.api.mapper;


import cn.yunyichina.log.common.entity.base.BaseMapper;
import cn.yunyichina.log.common.entity.entity.do_.Collector;

/**
 * Created by Jonven on 2017/1/4.
 */
public interface CollectorMapperBak extends BaseMapper<Collector> {

    Collector findByCollectorName(Collector collector);

    Collector findByCollectorId(Integer collectorId);

}
