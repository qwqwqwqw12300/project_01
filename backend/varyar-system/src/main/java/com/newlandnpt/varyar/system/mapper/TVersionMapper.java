package com.newlandnpt.varyar.system.mapper;

import java.util.List;
import com.newlandnpt.varyar.system.domain.TVersion;

/**
 * 版本Mapper接口
 * 
 * @author chenxw
 * @date 2022-12-24
 */
public interface TVersionMapper 
{
    /**
     * 查询版本
     * 
     * @param versionId 版本主键
     * @return 版本
     */
    public TVersion selectTVersionByVersionId(Long versionId);

    /**
     * 查询版本列表
     * 
     * @param tVersion 版本
     * @return 版本集合
     */
    public List<TVersion> selectTVersionList(TVersion tVersion);

    /**
     * 新增版本
     * 
     * @param tVersion 版本
     * @return 结果
     */
    public int insertTVersion(TVersion tVersion);

    /**
     * 修改版本
     * 
     * @param tVersion 版本
     * @return 结果
     */
    public int updateTVersion(TVersion tVersion);

    /**
     * 删除版本
     * 
     * @param versionId 版本主键
     * @return 结果
     */
    public int deleteTVersionByVersionId(Long versionId);

    /**
     * 批量删除版本
     * 
     * @param versionIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTVersionByVersionIds(Long[] versionIds);
}
