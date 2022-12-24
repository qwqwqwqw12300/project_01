package com.newlandnpt.varyar.system.service.impl;

import java.util.List;
import com.newlandnpt.varyar.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.newlandnpt.varyar.system.mapper.TVersionMapper;
import com.newlandnpt.varyar.system.domain.TVersion;
import com.newlandnpt.varyar.system.service.ITVersionService;

/**
 * 版本Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
@Service
public class TVersionServiceImpl implements ITVersionService 
{
    @Autowired
    private TVersionMapper tVersionMapper;

    /**
     * 查询版本
     * 
     * @param versionId 版本主键
     * @return 版本
     */
    @Override
    public TVersion selectTVersionByVersionId(Long versionId)
    {
        return tVersionMapper.selectTVersionByVersionId(versionId);
    }

    /**
     * 查询版本列表
     * 
     * @param tVersion 版本
     * @return 版本
     */
    @Override
    public List<TVersion> selectTVersionList(TVersion tVersion)
    {
        return tVersionMapper.selectTVersionList(tVersion);
    }

    /**
     * 新增版本
     * 
     * @param tVersion 版本
     * @return 结果
     */
    @Override
    public int insertTVersion(TVersion tVersion)
    {
        tVersion.setCreateTime(DateUtils.getNowDate());
        return tVersionMapper.insertTVersion(tVersion);
    }

    /**
     * 修改版本
     * 
     * @param tVersion 版本
     * @return 结果
     */
    @Override
    public int updateTVersion(TVersion tVersion)
    {
        tVersion.setUpdateTime(DateUtils.getNowDate());
        return tVersionMapper.updateTVersion(tVersion);
    }

    /**
     * 批量删除版本
     * 
     * @param versionIds 需要删除的版本主键
     * @return 结果
     */
    @Override
    public int deleteTVersionByVersionIds(Long[] versionIds)
    {
        return tVersionMapper.deleteTVersionByVersionIds(versionIds);
    }

    /**
     * 删除版本信息
     * 
     * @param versionId 版本主键
     * @return 结果
     */
    @Override
    public int deleteTVersionByVersionId(Long versionId)
    {
        return tVersionMapper.deleteTVersionByVersionId(versionId);
    }
}
