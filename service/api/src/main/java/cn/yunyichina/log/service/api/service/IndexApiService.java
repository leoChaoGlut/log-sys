package cn.yunyichina.log.service.api.service;

import cn.yunyichina.log.component.entity.do_.*;
import cn.yunyichina.log.service.api.mapper.CollectorMapper;
import cn.yunyichina.log.service.api.mapper.GroupMapper;
import cn.yunyichina.log.service.api.mapper.KeywordIndexMapper;
import cn.yunyichina.log.service.api.mapper.KvIndexMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jonven on 2017/1/4.
 */
@Service
@Transactional(rollbackFor = {Exception.class, RuntimeException.class})
public class IndexApiService {

    @Autowired
    CollectorMapper collectorMapper;

    @Autowired
    GroupMapper groupMapper;

    @Autowired
    KeywordIndexMapper keywordIndexMapper;

    @Autowired
    KvIndexMapper kvIndexMapper;

    /**
     * 新增一个分组
     *
     * @param groupName
     * @return
     * @throws Exception
     */
    public void addGroup(String groupName) throws Exception {

        if (StringUtils.isBlank(groupName)) {
            throw new Exception("分组名称不能为空");
        }
        Map<String, Object> resultMap = new HashMap<>();
        Group group = groupMapper.findByGroupName(groupName);
        if (group == null) {
            int result = groupMapper.insertOne(new Group().setName(groupName));
            if (result > 0) {

            } else {
                throw new Exception("新增分组失败");
            }
        } else {
            throw new Exception("该分组名称已存在");
        }
    }

    /**
     * 新增节点
     *
     * @param collectorName
     * @param groupId
     * @param serviceName
     * @throws Exception
     */
    public void addCollector(String collectorName, Integer groupId, String serviceName) throws Exception {
        if (StringUtils.isBlank(collectorName)) {
            throw new Exception("节点名称不能为空");
        }
        if (groupId == null) {
            throw new Exception("所属分组不能为空");
        }
        Group group = groupMapper.findByGroupId(groupId);
        if (group == null) {
            throw new Exception("找不到所属分组");
        } else {
            Collector collector = collectorMapper.findByCollectorName(new Collector().setName(collectorName).setGroup_id(groupId));
            if (collector == null) {
                int result = collectorMapper.insertOne(new Collector().setName(collectorName).setGroup_id(groupId).setService_name(serviceName));
                if (result > 0) {

                } else {
                    throw new Exception("新增节点失败");
                }
            } else {
                throw new Exception("该节点名称已存在");
            }
        }
    }

    /**
     * 新增keyword索引
     *
     * @param keywordIndexName
     * @param collectorId
     * @throws Exception
     */
    public void addKeywordIndex(String keywordIndexName, Integer collectorId) throws Exception {
        if (StringUtils.isBlank(keywordIndexName)) {
            throw new Exception("keyword索引名称不能为空");
        }
        if (collectorId == null) {
            throw new Exception("所属节点不能为空");
        }
        Collector collector = collectorMapper.findByCollectorId(collectorId);
        if (collector == null) {
            throw new Exception("找不到所属节点");
        } else {
            KeywordIndex keywordIndex = keywordIndexMapper.findByName(keywordIndexName);
            if (keywordIndex == null) {
                KeywordIndex insertKeywordIndex = new KeywordIndex();
                insertKeywordIndex.setKeyword(keywordIndexName);
                int keywordIndexId = keywordIndexMapper.insertOne(insertKeywordIndex);
                if (keywordIndexId > 0) {
                    int result = keywordIndexMapper.insertMid(collectorId, insertKeywordIndex.getId());
                    if (result > 0) {

                    } else {
                        keywordIndexMapper.removeById(keywordIndexId);
                        throw new Exception("新增keyword索引失败");
                    }
                } else {
                    throw new Exception("新增keyword索引失败");
                }
            } else {
                MidCollectorKeyword midCollectorKeyword = keywordIndexMapper.findMidCollectorKeyword(collectorId, keywordIndex.getId());
                if (midCollectorKeyword == null) {
                    int result = keywordIndexMapper.insertMid(collectorId, keywordIndex.getId());
                    if (result > 0) {

                    } else {
                        throw new Exception("新增keyword索引失败");
                    }
                } else {
                    throw new Exception("该keyword索引已经在");
                }
            }
        }
    }


    /**
     * 新增keyValue索引
     *
     * @param kvIndexName
     * @param collectorId
     * @throws Exception
     */
    public void addKvIndex(String kvIndexName, Integer collectorId) throws Exception {
        if (StringUtils.isBlank(kvIndexName)) {
            throw new Exception("keyValue索引名称不能为空");
        }
        if (collectorId == null) {
            throw new Exception("所属节点不能为空");
        }
        Collector collector = collectorMapper.findByCollectorId(collectorId);
        if (collector == null) {
            throw new Exception("找不到所属节点");
        } else {
            KvIndex kvIndex = kvIndexMapper.findByKey(kvIndexName);
            if (kvIndex == null) {
                KvIndex insertKvIndex = new KvIndex();
                insertKvIndex.setKey(kvIndexName);
                int kvIndexId = kvIndexMapper.insertOne(insertKvIndex);
                if (kvIndexId > 0) {
                    int result = kvIndexMapper.insertMid(collectorId, insertKvIndex.getId());
                    if (result > 0) {

                    } else {
                        kvIndexMapper.removeById(kvIndexId);
                        throw new Exception("新增keyValue索引失败");
                    }
                } else {
                    throw new Exception("新增keyValue索引失败");
                }
            } else {
                MidCollectorKv midCollectorKv = kvIndexMapper.findMidCollectorKv(collectorId, kvIndex.getId());
                if (midCollectorKv == null) {
                    int result = kvIndexMapper.insertMid(collectorId, kvIndex.getId());
                    if (result > 0) {

                    } else {
                        throw new Exception("新增keyValue索引失败");
                    }
                } else {
                    throw new Exception("该keyValue索引已经在");
                }
            }
        }
    }

    /**
     * 更改分组名称
     *
     * @param groupId
     * @param groupName
     * @throws Exception
     */
    public void updateGroup(Integer groupId, String groupName) throws Exception {

        if (groupId == null) {
            throw new Exception("分组id不能为空");
        }
        if (StringUtils.isBlank(groupName)) {
            throw new Exception("分组名称不能为空");
        }

        Group group = new Group();
        group.setId(groupId);
        group.setName(groupName);

        Group oldGroup = groupMapper.selectOne(group);
        if (oldGroup == null) {
            throw new Exception("分组不存在");
        } else {
            if (oldGroup.getName().equals(groupName)) {
                throw new Exception("分组名称没有改变");
            } else {
                Group resultObj = groupMapper.findByGroupName(groupName);
                if (resultObj == null) {
                    int result = groupMapper.updateOne(group);
                    if (result > 0) {

                    } else {
                        throw new Exception("修改分组失败");
                    }
                } else {
                    throw new Exception("该分组已存在");
                }
            }
        }
    }

    /**
     * 更改节点名称
     *
     * @param collectorId
     * @param collectorName
     * @throws Exception
     */
    public void updateCollector(Integer collectorId, String collectorName) throws Exception {

        if (collectorId == null) {
            throw new Exception("节点id不能为空");
        }
        if (StringUtils.isBlank(collectorName)) {
            throw new Exception("节点名称不能为空");
        }

        Collector collector = new Collector();
        collector.setId(collectorId);
        collector.setName(collectorName);

        Collector oldGCollector = collectorMapper.selectOne(collector);
        if (oldGCollector == null) {
            throw new Exception("节点不存在");
        } else {
            if (oldGCollector.getName().equals(collectorName)) {
                throw new Exception("节点名称没有改变");
            } else {
                Collector resultObj = collectorMapper.findByCollectorName(new Collector().setName(collectorName).setGroup_id(oldGCollector.getGroup_id()));
                if (resultObj == null) {
                    int result = collectorMapper.updateOne(collector);
                    if (result > 0) {

                    } else {
                        throw new Exception("修改节点失败");
                    }
                } else {
                    throw new Exception("节点已存在");
                }


            }
        }
    }

    /**
     * 更改keyword索引名称
     *
     * @param collectorId
     * @param keywordIndexId
     * @param keyword
     * @throws Exception
     */
    public void updateKeywordIndex(Integer collectorId, Integer keywordIndexId, String keyword) throws Exception {

        if (collectorId == null) {
            throw new Exception("节点id不能为空");
        }

        if (keywordIndexId == null) {
            throw new Exception("keyword索引id不能为空");
        }
        if (StringUtils.isBlank(keyword)) {
            throw new Exception("keyword索引名称不能为空");
        }

        KeywordIndex keywordIndex = new KeywordIndex();
        keywordIndex.setId(keywordIndexId);
        keywordIndex.setKeyword(keyword);

        KeywordIndex oldKeywordIndex = keywordIndexMapper.selectOne(keywordIndex);
        if (oldKeywordIndex == null) {
            throw new Exception("keyword索引不存在");
        } else {
            if (oldKeywordIndex.getKeyword().equals(keyword)) {
                throw new Exception("keyword索引没有变化");
            } else {
                List<MidCollectorKeyword> midCollectorKeywords = keywordIndexMapper.queryMidByKeywordId(keywordIndexId);
                if (midCollectorKeywords.size() == 1) {
                    KeywordIndex byName = keywordIndexMapper.findByName(keyword);
                    if (byName == null) {
                        int result = keywordIndexMapper.updateOne(byName);
                        if (result > 0) {

                        } else {
                            throw new Exception("修改keyword索引失败");
                        }
                    } else {
                        MidCollectorKeyword midCollectorKeyword = keywordIndexMapper.findMidCollectorKeyword(collectorId, byName.getId());
                        if (midCollectorKeyword == null) {
                            int updateMid = keywordIndexMapper.updateMid(collectorId, keywordIndexId, byName.getId());
                            if (updateMid > 0) {
                                keywordIndexMapper.removeById(keywordIndexId);
                            } else {
                                throw new Exception("修改keyword索引失败");
                            }
                        } else {
                            throw new Exception("keyword索引已存在");
                        }
                    }
                } else {
                    KeywordIndex newKeywordIndex = new KeywordIndex();
                    newKeywordIndex.setKeyword(keyword);
                    int result = keywordIndexMapper.insertOne(newKeywordIndex);
                    if (result > 0) {
                        int updateMid = keywordIndexMapper.updateMid(collectorId, keywordIndexId, newKeywordIndex.getId());
                        if (updateMid > 0) {

                        } else {
                            keywordIndexMapper.removeById(newKeywordIndex.getId());
                            throw new Exception("修改keyword索引失败");
                        }
                    } else {
                        throw new Exception("修改keyword索引失败");
                    }
                }

            }
        }
    }

    /**
     * 更改kv索引名称
     *
     * @param collectorId
     * @param kvIndexId
     * @param key
     * @throws Exception
     */
    public void updateKvIndex(Integer collectorId, Integer kvIndexId, String key) throws Exception {

        if (kvIndexId == null) {
            throw new Exception("kv索引id不能为空");
        }
        if (StringUtils.isBlank(key)) {
            throw new Exception("kv索引名称不能为空");
        }

        KvIndex kvIndex = new KvIndex();
        kvIndex.setId(kvIndexId);
        kvIndex.setKey(key);

        KvIndex oleKvIndex = kvIndexMapper.selectOne(kvIndex);
        if (oleKvIndex == null) {
            throw new Exception("kv索引不存在");
        } else {
            if (oleKvIndex.getKey().equals(key)) {
                throw new Exception("kv索引名称没有改变");
            } else {
                List<MidCollectorKv> midCollectorKvList = kvIndexMapper.queryMidByKvId(kvIndexId);
                if (midCollectorKvList.size() == 1) {
                    KvIndex byKey = kvIndexMapper.findByKey(key);
                    if (byKey == null) {
                        int result = kvIndexMapper.updateOne(kvIndex);
                        if (result > 0) {

                        } else {
                            throw new Exception("修改kv索引失败");
                        }
                    } else {
                        MidCollectorKv midCollectorKv = kvIndexMapper.findMidCollectorKv(collectorId, byKey.getId());
                        if (midCollectorKv == null) {
                            int updateMid = kvIndexMapper.updateMid(collectorId, kvIndexId, byKey.getId());
                            if (updateMid > 0) {
                                kvIndexMapper.removeById(kvIndexId);
                            } else {
                                throw new Exception("修改kv索引失败");
                            }
                        } else {
                            throw new Exception("kv索引已存在");
                        }
                    }

                } else {
                    KvIndex newKvIndex = new KvIndex();
                    newKvIndex.setKey(key);
                    int result = kvIndexMapper.insertOne(newKvIndex);
                    if (result > 0) {
                        int updateMid = kvIndexMapper.updateMid(collectorId, kvIndexId, newKvIndex.getId());
                        if (updateMid > 0) {

                        } else {
                            kvIndexMapper.removeById(newKvIndex.getId());
                            throw new Exception("修改kv索引失败");
                        }
                    } else {
                        throw new Exception("修改kv索引失败");
                    }
                }
            }
        }
    }

}
