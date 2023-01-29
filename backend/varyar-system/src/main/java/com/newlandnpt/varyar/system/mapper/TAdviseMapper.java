package com.newlandnpt.varyar.system.mapper;

import java.util.List;
import com.newlandnpt.varyar.system.domain.TAdvise;

/**
 * 建议Mapper接口
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
public interface TAdviseMapper 
{
    /**
     * 查询建议
     * 
     * @param adviseId 建议主键
     * @return 建议
     */
    public TAdvise selectTAdviseByAdviseId(Long adviseId);

    /**
     * 查询建议列表
     * 
     * @param tAdvise 建议
     * @return 建议集合
     */
    public List<TAdvise> selectTAdviseList(TAdvise tAdvise);

    /**
     * 新增建议
     * 
     * @param tAdvise 建议
     * @return 结果
     */
    public int insertTAdvise(TAdvise tAdvise);

    /**
     * 修改建议
     * 
     * @param tAdvise 建议
     * @return 结果
     */
    public int updateTAdvise(TAdvise tAdvise);

    /**
     * 一键已读建议
     *
     * @param adviseIds 需要处理的数据主键集合
     * @return 结果
     */
    public int onReadTAdvise(Long[] adviseIds);


    /**
     * 删除建议
     * 
     * @param adviseId 建议主键
     * @return 结果
     */
    public int deleteTAdviseByAdviseId(Long adviseId);

    /**
     * 批量删除建议
     * 
     * @param adviseIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTAdviseByAdviseIds(Long[] adviseIds);
}
