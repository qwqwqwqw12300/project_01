package com.newlandnpt.varyar.system.mapper;

import java.util.List;
import com.newlandnpt.varyar.system.domain.SysRoleOrg;

/**
 * 角色与机构关联表 数据层
 * 
 * @author ruoyi
 */
public interface SysRoleOrgMapper
{
    /**
     * 通过角色ID删除角色和机构关联
     * 
     * @param roleId 角色ID
     * @return 结果
     */
    public int deleteRoleOrgByRoleId(Long roleId);

    /**
     * 批量删除角色机构关联信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteRoleOrg(Long[] ids);

    /**
     * 查询机构使用数量
     * 
     * @param orgId 机构ID
     * @return 结果
     */
    public int selectCountRoleOrgByOrgId(Long orgId);

    /**
     * 批量新增角色机构信息
     * 
     * @param roleOrgList 角色机构列表
     * @return 结果
     */
    public int batchRoleOrg(List<SysRoleOrg> roleOrgList);
}
