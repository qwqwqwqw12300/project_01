package com.newlandnpt.varyar.system.service.impl;

import java.util.List;
import com.newlandnpt.varyar.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.newlandnpt.varyar.system.mapper.TAdviseMapper;
import com.newlandnpt.varyar.system.domain.TAdvise;
import com.newlandnpt.varyar.system.service.ITAdviseService;

/**
 * 建议Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
@Service
public class TAdviseServiceImpl implements ITAdviseService 
{
    @Autowired
    private TAdviseMapper tAdviseMapper;

    /**
     * 查询建议
     * 
     * @param adviseId 建议主键
     * @return 建议
     */
    @Override
    public TAdvise selectTAdviseByAdviseId(Long adviseId)
    {
        return tAdviseMapper.selectTAdviseByAdviseId(adviseId);
    }

    /**
     * 查询建议列表
     * 
     * @param tAdvise 建议
     * @return 建议
     */
    @Override
    public List<TAdvise> selectTAdviseList(TAdvise tAdvise)
    {
        return tAdviseMapper.selectTAdviseList(tAdvise);
    }

    /**
     * 新增建议
     * 
     * @param tAdvise 建议
     * @return 结果
     */
    @Override
    public int insertTAdvise(TAdvise tAdvise)
    {
        tAdvise.setCreateTime(DateUtils.getNowDate());
        return tAdviseMapper.insertTAdvise(tAdvise);
    }

    /**
     * 修改建议
     * 
     * @param tAdvise 建议
     * @return 结果
     */
    @Override
    public int updateTAdvise(TAdvise tAdvise)
    {
        tAdvise.setUpdateTime(DateUtils.getNowDate());
        return tAdviseMapper.updateTAdvise(tAdvise);
    }

    /**
     * 批量删除建议
     * 
     * @param adviseIds 需要删除的建议主键
     * @return 结果
     */
    @Override
    public int deleteTAdviseByAdviseIds(Long[] adviseIds)
    {
        return tAdviseMapper.deleteTAdviseByAdviseIds(adviseIds);
    }

    /**
     * 删除建议信息
     * 
     * @param adviseId 建议主键
     * @return 结果
     */
    @Override
    public int deleteTAdviseByAdviseId(Long adviseId)
    {
        return tAdviseMapper.deleteTAdviseByAdviseId(adviseId);
    }
}
