package com.newlandnpt.varyar.system.service.impl;

import com.newlandnpt.varyar.common.annotation.DataScope;
import com.newlandnpt.varyar.common.constant.UserConstants;
import com.newlandnpt.varyar.common.core.domain.TreeSelect;
import com.newlandnpt.varyar.common.core.domain.entity.SysRole;
import com.newlandnpt.varyar.common.core.domain.entity.SysUser;
import com.newlandnpt.varyar.common.core.domain.entity.Org;
import com.newlandnpt.varyar.common.core.text.Convert;
import com.newlandnpt.varyar.common.exception.ServiceException;
import com.newlandnpt.varyar.common.utils.SecurityUtils;
import com.newlandnpt.varyar.common.utils.StringUtils;
import com.newlandnpt.varyar.common.utils.spring.SpringUtils;
import com.newlandnpt.varyar.system.mapper.SysRoleMapper;
import com.newlandnpt.varyar.system.mapper.OrgMapper;
import com.newlandnpt.varyar.system.service.IOrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static com.newlandnpt.varyar.common.constant.OrgConstants.STATUS_CLOSE;
import static com.newlandnpt.varyar.common.constant.OrgConstants.STATUS_OPEN;

/**
 * 机构管理 服务实现
 *
 * @author ruoyi
 */
@Service
public class OrgServiceImpl implements IOrgService {
    @Autowired
    private OrgMapper orgMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    /**
     * 查询机构管理数据
     *
     * @param org 机构信息
     * @return 机构信息集合
     */
    @Override
    @DataScope(orgAlias = "d")
    public List<Org> selectOrgList(Org org) {
        return orgMapper.selectOrgList(org);
    }

    /**
     * 查询机构树结构信息
     *
     * @param org 机构信息
     * @return 机构树信息集合
     */
    @Override
    public List<TreeSelect> selectOrgTreeList(Org org) {
        List<Org> orgs = SpringUtils.getAopProxy(this).selectOrgList(org);
        return buildOrgTreeSelect(orgs);
    }

    /**
     * 构建前端所需要树结构
     *
     * @param orgs 机构列表
     * @return 树结构列表
     */
    @Override
    public List<Org> buildOrgTree(List<Org> orgs) {
        List<Org> returnList = new ArrayList<Org>();
        List<Long> tempList = orgs.stream().map(Org::getOrgId).collect(Collectors.toList());
        for (Org org : orgs) {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(org.getParentId())) {
                recursionFn(orgs, org);
                returnList.add(org);
            }
        }
        if (returnList.isEmpty()) {
            returnList = orgs;
        }
        return returnList;
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param orgs 机构列表
     * @return 下拉树结构列表
     */
    @Override
    public List<TreeSelect> buildOrgTreeSelect(List<Org> orgs) {
        List<Org> orgTrees = buildOrgTree(orgs);
        return orgTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 根据角色ID查询机构树信息
     *
     * @param roleId 角色ID
     * @return 选中机构列表
     */
    @Override
    public List<Long> selectOrgListByRoleId(Long roleId) {
        SysRole role = roleMapper.selectRoleById(roleId);
        return orgMapper.selectOrgListByRoleId(roleId, role.isOrgCheckStrictly());
    }

    /**
     * 根据机构ID查询信息
     *
     * @param orgId 机构ID
     * @return 机构信息
     */
    @Override
    public Org selectOrgById(Long orgId) {
        return orgMapper.selectOrgById(orgId);
    }

    /**
     * 根据ID查询所有子机构（正常状态）
     *
     * @param orgId 机构ID
     * @return 子机构数
     */
    @Override
    public int selectNormalChildrenOrgById(Long orgId) {
        return orgMapper.selectNormalChildrenOrgById(orgId);
    }

    /**
     * 是否存在子节点
     *
     * @param orgId 机构ID
     * @return 结果
     */
    @Override
    public boolean hasChildByOrgId(Long orgId) {
        int result = orgMapper.hasChildByOrgId(orgId);
        return result > 0;
    }

    /**
     * 查询机构是否存在用户
     *
     * @param orgId 机构ID
     * @return 结果 true 存在 false 不存在
     */
    @Override
    public boolean checkOrgExistUser(Long orgId) {
        int result = orgMapper.checkOrgExistUser(orgId);
        return result > 0;
    }

    /**
     * 校验机构名称是否唯一
     *
     * @param org 机构信息
     * @return 结果
     */
    @Override
    public String checkOrgNameUnique(Org org) {
        Long orgId = StringUtils.isNull(org.getOrgId()) ? -1L : org.getOrgId();
        Org info = orgMapper.checkOrgNameUnique(org.getOrgName(), org.getParentId());
        if (StringUtils.isNotNull(info) && info.getOrgId().longValue() != orgId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验机构是否有数据权限
     *
     * @param orgId 机构id
     */
    @Override
    public void checkOrgDataScope(Long orgId) {
        if (!SysUser.isAdmin(SecurityUtils.getUserId())) {
            Org org = new Org();
            org.setOrgId(orgId);
            List<Org> orgs = SpringUtils.getAopProxy(this).selectOrgList(org);
            if (StringUtils.isEmpty(orgs)) {
                throw new ServiceException("没有权限访问机构数据！");
            }
        }
    }

    /**
     * 新增保存机构信息
     *
     * @param org 机构信息
     * @return 结果
     */
    @Override
    public int insertOrg(Org org) {
        Org info = orgMapper.selectOrgById(org.getParentId());
        // 如果父节点不为正常状态,则不允许新增子节点
        if (!STATUS_OPEN.equals(info.getStatus())) {
            throw new ServiceException("父级机构关闭，不允许新增");
        }
        org.setAncestors(info.getAncestors() + "," + org.getParentId());
        return orgMapper.insertOrg(org);
    }

    /**
     * 修改保存机构信息
     *
     * @param org 机构信息
     * @return 结果
     */
    @Override
    public int updateOrg(Org org) {
        Org newParentOrg = orgMapper.selectOrgById(org.getParentId());
        Org oldOrg = orgMapper.selectOrgById(org.getOrgId());
        if (StringUtils.isNotNull(newParentOrg) && StringUtils.isNotNull(oldOrg)) {
            String newAncestors = newParentOrg.getAncestors() + "," + newParentOrg.getOrgId();
            String oldAncestors = oldOrg.getAncestors();
            org.setAncestors(newAncestors);
            updateOrgChildren(org.getOrgId(), newAncestors, oldAncestors);
        }
        int result = orgMapper.updateOrg(org);
        // 如果机构状态发生改变下级机构状态同步调整
        if (StringUtils.isNotBlank(org.getStatus())&&StringUtils.equals(oldOrg.getStatus(), org.getStatus())){
            this.updateChildrenOrgStatus(org);
        }
        return result;
    }

    /**
     * 修改该机构的父级机构状态
     *
     * @param org 当前机构
     */
    private void updateParentOrgStatusNormal(Org org) {
        String ancestors = org.getAncestors();
        Long[] orgIds = Convert.toLongArray(ancestors);
        orgMapper.updateOrgStatusNormal(orgIds);
    }

    /**
     * 修改该机构的子级机构状态
     *
     * @param org 当前机构
     */
    private void updateChildrenOrgStatus(Org org) {
        Long orgId = org.getOrgId();
        if (STATUS_OPEN.equals(org.getStatus())) {
            orgMapper.updateChildrenStatusOpen(orgId);
        } else if (STATUS_CLOSE.equals(org.getStatus())) {
            orgMapper.updateChildrenStatusClose(orgId);
        }
    }

    /**
     * 修改子元素关系
     *
     * @param orgId        被修改的机构ID
     * @param newAncestors 新的父ID集合
     * @param oldAncestors 旧的父ID集合
     */
    public void updateOrgChildren(Long orgId, String newAncestors, String oldAncestors) {
        List<Org> children = orgMapper.selectChildrenOrgById(orgId);
        for (Org child : children) {
            child.setAncestors(child.getAncestors().replaceFirst(oldAncestors, newAncestors));
        }
        if (children.size() > 0) {
            orgMapper.updateOrgChildren(children);
        }
    }

    /**
     * 删除机构管理信息
     *
     * @param orgId 机构ID
     * @return 结果
     */
    @Override
    public int deleteOrgById(Long orgId) {
        return orgMapper.deleteOrgById(orgId);
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<Org> list, Org t) {
        // 得到子节点列表
        List<Org> childList = getChildList(list, t);
        t.setChildren(childList);
        for (Org tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<Org> getChildList(List<Org> list, Org t) {
        List<Org> tlist = new ArrayList<Org>();
        Iterator<Org> it = list.iterator();
        while (it.hasNext()) {
            Org n = (Org) it.next();
            if (StringUtils.isNotNull(n.getParentId()) && n.getParentId().longValue() == t.getOrgId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<Org> list, Org t) {
        return getChildList(list, t).size() > 0;
    }
}
