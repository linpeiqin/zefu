package com.zefu.business.service;

import java.util.List;
import com.zefu.business.domain.BusEquipmentDefinition;

/**
 * 设备定义Service接口
 * 
 * @author linking
 * @date 2021-02-08
 */
public interface IBusEquipmentDefinitionService 
{
    /**
     * 查询设备定义
     * 
     * @param id 设备定义ID
     * @return 设备定义
     */
    public BusEquipmentDefinition selectBusEquipmentDefinitionById(Long id);

    /**
     * 查询设备定义列表
     * 
     * @param busEquipmentDefinition 设备定义
     * @return 设备定义集合
     */
    public List<BusEquipmentDefinition> selectBusEquipmentDefinitionList(BusEquipmentDefinition busEquipmentDefinition);

    /**
     * 新增设备定义
     * 
     * @param busEquipmentDefinition 设备定义
     * @return 结果
     */
    public int insertBusEquipmentDefinition(BusEquipmentDefinition busEquipmentDefinition);

    /**
     * 修改设备定义
     * 
     * @param busEquipmentDefinition 设备定义
     * @return 结果
     */
    public int updateBusEquipmentDefinition(BusEquipmentDefinition busEquipmentDefinition);

    /**
     * 批量删除设备定义
     * 
     * @param ids 需要删除的设备定义ID
     * @return 结果
     */
    public int deleteBusEquipmentDefinitionByIds(Long[] ids);

    /**
     * 删除设备定义信息
     * 
     * @param id 设备定义ID
     * @return 结果
     */
    public int deleteBusEquipmentDefinitionById(Long id);
}
