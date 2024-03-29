package com.newlandnpt.varyar.system.service;

import java.util.List;
import com.newlandnpt.varyar.common.core.domain.TreeSelect;
import com.newlandnpt.varyar.common.core.domain.entity.TOrg;

/**
 * 机构管理 服务层
 * 
 * @author ruoyi
 */
public interface IOrgService
{
    /**
     * 查询机构管理数据
     * 
     * @param org 机构信息
     * @return 机构信息集合
     */
    public List<TOrg> selectOrgList(TOrg org);

    /**
     * 查询机构树结构信息
     * 
     * @param org 机构信息
     * @return 机构树信息集合
     */
    public List<TreeSelect> selectOrgTreeList(TOrg org);

    /**
     * 构建前端所需要树结构
     * 
     * @param orgs 机构列表
     * @return 树结构列表
     */
    public List<TOrg> buildOrgTree(List<TOrg> orgs);

    /**
     * 构建前端所需要下拉树结构
     * 
     * @param orgs 机构列表
     * @return 下拉树结构列表
     */
    public List<TreeSelect> buildOrgTreeSelect(List<TOrg> orgs);

    /**
     * 根据角色ID查询机构树信息
     * 
     * @param roleId 角色ID
     * @return 选中机构列表
     */
    public List<Long> selectOrgListByRoleId(Long roleId);

    /**
     * 根据机构ID查询信息
     * 
     * @param orgId 机构ID
     * @return 机构信息
     */
    public TOrg selectOrgById(Long orgId);

    /**
     * 根据ID查询所有子机构（正常状态）
     * 
     * @param orgId 机构ID
     * @return 子机构数
     */
    public int selectNormalChildrenOrgById(Long orgId);

    /**
     * 是否存在机构子节点
     * 
     * @param orgId 机构ID
     * @return 结果
     */
    public boolean hasChildByOrgId(Long orgId);

    /**
     * 查询机构是否存在用户
     * 
     * @param orgId 机构ID
     * @return 结果 true 存在 false 不存在
     */
    public boolean checkOrgExistUser(Long orgId);

    /**
     * 校验机构名称是否唯一
     * 
     * @param org 机构信息
     * @return 结果
     */
    public String checkOrgNameUnique(TOrg org);

    /**
     * 校验机构是否有数据权限
     * 
     * @param orgId 机构id
     */
    public void checkOrgDataScope(Long orgId);

    /**
     * 新增保存机构信息
     * 
     * @param org 机构信息
     * @return 结果
     */
    public int insertOrg(TOrg org);

    /**
     * 修改保存机构信息
     * 
     * @param org 机构信息
     * @return 结果
     */
    public int updateOrg(TOrg org);

    /**
     * 删除机构管理信息
     * 
     * @param orgId 机构ID
     * @return 结果
     */
    public int deleteOrgById(Long orgId);

    /**
     * 机构总数
     * @return
     */
    public long total();
}
