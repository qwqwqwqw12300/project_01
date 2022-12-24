package com.newlandnpt.varyar.system.service;

import java.util.List;
import com.newlandnpt.varyar.system.domain.TAdvise;

/**
 * 建议Service接口
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
public interface ITAdviseService 
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
     * 批量删除建议
     * 
     * @param adviseIds 需要删除的建议主键集合
     * @return 结果
     */
    public int deleteTAdviseByAdviseIds(Long[] adviseIds);

    /**
     * 删除建议信息
     * 
     * @param adviseId 建议主键
     * @return 结果
     */
    public int deleteTAdviseByAdviseId(Long adviseId);
}
