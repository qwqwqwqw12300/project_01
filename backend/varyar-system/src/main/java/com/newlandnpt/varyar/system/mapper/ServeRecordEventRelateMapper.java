package com.newlandnpt.varyar.system.mapper;

import java.util.List;
import com.newlandnpt.varyar.system.domain.TServeRecordEventRelate;

/**
 * 服务记录和事件关联Mapper接口
 * 
 * @author lin.ju
 * @date 2023-01-04
 */
public interface ServeRecordEventRelateMapper 
{
    /**
     * 查询服务记录和事件关联
     * 
     * @param recordId 服务记录和事件关联主键
     * @return 服务记录和事件关联
     */
    public TServeRecordEventRelate selectServeRecordEventRelateByRecordId(Long recordId);

    /**
     * 查询服务记录和事件关联列表
     * 
     * @param serveRecordEventRelate 服务记录和事件关联
     * @return 服务记录和事件关联集合
     */
    public List<TServeRecordEventRelate> selectServeRecordEventRelateList(TServeRecordEventRelate serveRecordEventRelate);

    /**
     * 新增服务记录和事件关联
     * 
     * @param serveRecordEventRelate 服务记录和事件关联
     * @return 结果
     */
    public int insertServeRecordEventRelate(TServeRecordEventRelate serveRecordEventRelate);

    /**
     * 修改服务记录和事件关联
     * 
     * @param serveRecordEventRelate 服务记录和事件关联
     * @return 结果
     */
    public int updateServeRecordEventRelate(TServeRecordEventRelate serveRecordEventRelate);

    /**
     * 删除服务记录和事件关联
     * 
     * @param recordId 服务记录和事件关联主键
     * @return 结果
     */
    public int deleteServeRecordEventRelateByRecordId(Long recordId);

    /**
     * 批量删除服务记录和事件关联
     * 
     * @param recordIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteServeRecordEventRelateByRecordIds(Long[] recordIds);
}
