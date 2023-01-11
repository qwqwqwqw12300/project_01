package com.newlandnpt.varyar.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.newlandnpt.varyar.common.core.domain.entity.TOrg;
import org.apache.ibatis.annotations.Select;

/**
 * 机构管理 数据层
 * 
 * @author ruoyi
 */
public interface TOrgMapper
{
    /**
     * 查询机构管理数据
     * 
     * @param org 机构信息
     * @return 机构信息集合
     */
    public List<TOrg> selectOrgList(TOrg org);

    /**
     * 根据角色ID查询机构树信息
     * 
     * @param roleId 角色ID
     * @param orgCheckStrictly 机构树选择项是否关联显示
     * @return 选中机构列表
     */
    public List<Long> selectOrgListByRoleId(@Param("roleId") Long roleId, @Param("orgCheckStrictly") boolean orgCheckStrictly);

    /**
     * 根据机构ID查询信息
     * 
     * @param orgId 机构ID
     * @return 机构信息
     */
    public TOrg selectOrgById(Long orgId);

    /**
     * 根据ID查询所有子机构
     * 
     * @param orgId 机构ID
     * @return 机构列表
     */
    public List<TOrg> selectChildrenOrgById(Long orgId);

    /**
     * 根据ID查询所有子机构（正常状态）
     * 
     * @param orgId 机构ID
     * @return 子机构数
     */
    public int selectNormalChildrenOrgById(Long orgId);

    /**
     * 是否存在子节点
     * 
     * @param orgId 机构ID
     * @return 结果
     */
    public int hasChildByOrgId(Long orgId);

    /**
     * 查询机构是否存在用户
     * 
     * @param orgId 机构ID
     * @return 结果
     */
    public int checkOrgExistUser(Long orgId);

    /**
     * 校验机构名称是否唯一
     * 
     * @param orgName 机构名称
     * @param parentId 父机构ID
     * @return 结果
     */
    public TOrg checkOrgNameUnique(@Param("orgName") String orgName, @Param("parentId") Long parentId);

    /**
     * 新增机构信息
     * 
     * @param org 机构信息
     * @return 结果
     */
    public int insertOrg(TOrg org);

    /**
     * 修改机构信息
     * 
     * @param org 机构信息
     * @return 结果
     */
    public int updateOrg(TOrg org);

    /**
     * 修改所在机构正常状态
     * 
     * @param orgIds 机构ID组
     */
    public void updateOrgStatusNormal(Long[] orgIds);

    /**
     * 修改子机构开通状态
     *
     * @param orgId 机构Id
     */
    public void updateChildrenStatusOpen(Long orgId);

    /**
     * 修改子机构关闭状态
     *
     * @param orgId 机构Id
     */
    public void updateChildrenStatusClose(Long orgId);

    /**
     * 修改子元素关系
     * 
     * @param orgs 子元素
     * @return 结果
     */
    public int updateOrgChildren(@Param("orgs") List<TOrg> orgs);

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
    @Select("select count(*) from t_org where del_flag = '0'")
    public long total();
}
