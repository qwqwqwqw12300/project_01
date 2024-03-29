package com.newlandnpt.varyar.system.service.impl;

import com.newlandnpt.varyar.common.utils.DateUtils;
import com.newlandnpt.varyar.system.domain.TAdvise;
import com.newlandnpt.varyar.system.mapper.TAdviseMapper;
import com.newlandnpt.varyar.system.service.IAdviseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 建议Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
@Service
public class AdviseServiceImpl implements IAdviseService
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

        //初始标记为未读信息
        tAdvise.setDelFlag("0");
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
     * 一键已读建议
     *
     * @param adviseIds 需要操作的建议主键
     * @return 结果
     */
    @Override
    public int onReadTAdvise(Long[] adviseIds)
    {
        return tAdviseMapper.onReadTAdvise(adviseIds);
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
