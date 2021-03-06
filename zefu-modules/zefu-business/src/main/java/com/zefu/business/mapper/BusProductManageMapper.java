package com.zefu.business.mapper;

import java.util.List;
import com.zefu.business.domain.BusProductManage;

/**
 * 产品管理Mapper接口
 * 
 * @author linking
 * @date 2021-03-11
 */
public interface BusProductManageMapper 
{
    /**
     * 查询产品管理
     * 
     * @param id 产品管理ID
     * @return 产品管理
     */
    public BusProductManage selectBusProductManageById(Long id);

    /**
     * 查询产品管理列表
     * 
     * @param busProductManage 产品管理
     * @return 产品管理集合
     */
    public List<BusProductManage> selectBusProductManageList(BusProductManage busProductManage);

    /**
     * 新增产品管理
     * 
     * @param busProductManage 产品管理
     * @return 结果
     */
    public int insertBusProductManage(BusProductManage busProductManage);

    /**
     * 修改产品管理
     * 
     * @param busProductManage 产品管理
     * @return 结果
     */
    public int updateBusProductManage(BusProductManage busProductManage);

    /**
     * 删除产品管理
     * 
     * @param id 产品管理ID
     * @return 结果
     */
    public int deleteBusProductManageById(Long id);

    /**
     * 批量删除产品管理
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteBusProductManageByIds(Long[] ids);


}
