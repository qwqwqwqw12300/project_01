package com.newlandnpt.varyar.system.service;

import java.util.List;
import com.newlandnpt.varyar.system.domain.TServeRecord;

/**
 * 服务记录Service接口
 * 
 * @author lin.ju
 * @date 2023-01-04
 */
public interface IServeRecordService 
{
    /**
     * 查询服务记录
     * 
     * @param recordId 服务记录主键
     * @return 服务记录
     */
    public TServeRecord selectServeRecordByRecordId(Long recordId);

    /**
     * 查询服务记录列表
     * 
     * @param serveRecord 服务记录
     * @return 服务记录集合
     */
    public List<TServeRecord> selectServeRecordList(TServeRecord serveRecord);

    /**
     * 新增服务记录
     * 
     * @param serveRecord 服务记录
     * @return 结果
     */
    public int insertServeRecord(TServeRecord serveRecord);

    /**
     * 修改服务记录
     * 
     * @param serveRecord 服务记录
     * @return 结果
     */
    public int updateServeRecord(TServeRecord serveRecord);

    /**
     * 批量删除服务记录
     * 
     * @param recordIds 需要删除的服务记录主键集合
     * @return 结果
     */
    public int deleteServeRecordByRecordIds(Long[] recordIds);

    /**
     * 删除服务记录信息
     * 
     * @param recordId 服务记录主键
     * @return 结果
     */
    public int deleteServeRecordByRecordId(Long recordId);
}
