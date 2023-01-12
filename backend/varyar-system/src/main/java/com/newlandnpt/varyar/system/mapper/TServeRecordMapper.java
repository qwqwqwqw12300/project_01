package com.newlandnpt.varyar.system.mapper;

import java.util.List;
import com.newlandnpt.varyar.system.domain.TServeRecord;

/**
 * 服务记录Mapper接口
 * 
 * @author lin.ju
 * @date 2023-01-04
 */
public interface TServeRecordMapper
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
     * 删除服务记录
     * 
     * @param recordId 服务记录主键
     * @return 结果
     */
    public int deleteServeRecordByRecordId(Long recordId);

    /**
     * 批量删除服务记录
     * 
     * @param recordIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteServeRecordByRecordIds(Long[] recordIds);
}
