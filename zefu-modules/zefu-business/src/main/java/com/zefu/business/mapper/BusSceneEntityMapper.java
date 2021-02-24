package com.zefu.business.mapper;

import java.util.List;
import com.zefu.business.domain.BusSceneEntity;

/**
 * 场景实体关联Mapper接口
 * 
 * @author linking
 * @date 2021-02-08
 */
public interface BusSceneEntityMapper 
{
    /**
     * 查询场景实体关联
     * 
     * @param id 场景实体关联ID
     * @return 场景实体关联
     */
    public BusSceneEntity selectBusSceneEntityById(Long id);

    /**
     * 查询场景实体关联列表
     * 
     * @param busSceneEntity 场景实体关联
     * @return 场景实体关联集合
     */
    public List<BusSceneEntity> selectBusSceneEntityList(BusSceneEntity busSceneEntity);

    /**
     * 新增场景实体关联
     * 
     * @param busSceneEntity 场景实体关联
     * @return 结果
     */
    public int insertBusSceneEntity(BusSceneEntity busSceneEntity);

    /**
     * 修改场景实体关联
     * 
     * @param busSceneEntity 场景实体关联
     * @return 结果
     */
    public int updateBusSceneEntity(BusSceneEntity busSceneEntity);

    /**
     * 删除场景实体关联
     * 
     * @param id 场景实体关联ID
     * @return 结果
     */
    public int deleteBusSceneEntityById(Long id);

    /**
     * 批量删除场景实体关联
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteBusSceneEntityByIds(Long[] ids);
}