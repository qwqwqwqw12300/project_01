package com.newlandnpt.varyar.common.core.domain.entity;

import java.util.Date;
import java.util.List;
import javax.validation.constraints.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import com.newlandnpt.varyar.common.annotation.Excel;
import com.newlandnpt.varyar.common.annotation.Excel.Type;
import com.newlandnpt.varyar.common.annotation.Excels;
import com.newlandnpt.varyar.common.core.domain.BaseEntity;
import com.newlandnpt.varyar.common.xss.Xss;
import org.springframework.data.annotation.Transient;

/**
 * 用户对象 sys_user
 * 
 * @author ruoyi
 */
public class SysUser extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    private Long userId;

    /** 机构ID */
    private Long orgId;

    /** 登录账号 */
    private String userName;

    /** 用户昵称 */
    private String nickName;

    /** 用户姓名 */
    private String name;

    /** 用户性别 */
    private String sex;

    /** 手机号码 */
    private String mobilePhone;

    /** 座机号 */
    private String telephone;

    /** 分机号 */
    private String extensionNumber;

    /** 用户邮箱 */
    private String email;

    /** 用户头像 */
    private String avatar;

    /** 密码 */
    private String password;

    /** 帐号状态（0正常 1停用） */
    @Excel(name = "帐号状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 最后登录IP */
    @Excel(name = "最后登录IP", type = Type.EXPORT)
    private String loginIp;

    /** 最后登录时间 */
    @Excel(name = "最后登录时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Type.EXPORT)
    private Date loginDate;

    /** 机构对象 */
    @Excels({
        @Excel(name = "机构名称", targetAttr = "orgName", type = Type.EXPORT),
        @Excel(name = "机构负责人", targetAttr = "leader", type = Type.EXPORT)
    })
    @Transient
    private TOrg org;

    /** 角色对象 */
    @Transient
    private List<SysRole> roles;

    /** 角色组 */
    private Long[] roleIds;

    /** 岗位组 */
    private Long[] postIds;

    /** 角色ID */
    private Long roleId;

    public SysUser()
    {

    }

    public SysUser(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public boolean isAdmin()
    {
        return isAdmin(this.userId);
    }

    public static boolean isAdmin(Long userId)
    {
        return userId != null && 1L == userId;
    }

    public Long getOrgId()
    {
        return orgId;
    }

    public void setOrgId(Long orgId)
    {
        this.orgId = orgId;
    }

    @Xss(message = "用户昵称不能包含脚本字符")
    @Size(min = 0, max = 30, message = "用户昵称长度不能超过30个字符")
    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    @Xss(message = "用户账号不能包含脚本字符")
    @NotBlank(message = "用户账号不能为空")
    @Size(min = 0, max = 30, message = "用户账号长度不能超过30个字符")
    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符")
    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    @Xss(message = "姓名不能包含脚本字符")
    @Size(min = 0, max = 30, message = "姓名长度不能超过30个字符")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Size(min = 0, max = 11, message = "手机号码长度不能超过11个字符")
    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    @Size(min = 0, max = 14, message = "座机号码长度不能超过14个字符")
    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Size(min = 0, max = 3, message = "分机号码长度不能超过3个字符")
    public String getExtensionNumber() {
        return extensionNumber;
    }

    public void setExtensionNumber(String extensionNumber) {
        this.extensionNumber = extensionNumber;
    }

    public String getSex()
    {
        return sex;
    }

    public void setSex(String sex)
    {
        this.sex = sex;
    }

    public String getAvatar()
    {
        return avatar;
    }

    public void setAvatar(String avatar)
    {
        this.avatar = avatar;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getLoginIp()
    {
        return loginIp;
    }

    public void setLoginIp(String loginIp)
    {
        this.loginIp = loginIp;
    }

    public Date getLoginDate()
    {
        return loginDate;
    }

    public void setLoginDate(Date loginDate)
    {
        this.loginDate = loginDate;
    }

    public TOrg getOrg()
    {
        return org;
    }

    public void setOrg(TOrg org)
    {
        this.org = org;
    }

    public List<SysRole> getRoles()
    {
        return roles;
    }

    public void setRoles(List<SysRole> roles)
    {
        this.roles = roles;
    }

    public Long[] getRoleIds()
    {
        return roleIds;
    }

    public void setRoleIds(Long[] roleIds)
    {
        this.roleIds = roleIds;
    }

    public Long[] getPostIds()
    {
        return postIds;
    }

    public void setPostIds(Long[] postIds)
    {
        this.postIds = postIds;
    }

    public Long getRoleId()
    {
        return roleId;
    }

    public void setRoleId(Long roleId)
    {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("userId", userId)
                .append("orgId", orgId)
                .append("userName", userName)
                .append("nickName", nickName)
                .append("name", name)
                .append("sex", sex)
                .append("mobilePhone", mobilePhone)
                .append("telephone", telephone)
                .append("extensionNumber", extensionNumber)
                .append("email", email)
                .append("avatar", avatar)
                .append("password", password)
                .append("status", status)
                .append("delFlag", delFlag)
                .append("loginIp", loginIp)
                .append("loginDate", loginDate)
                .append("org", org)
                .append("roles", roles)
                .append("roleIds", roleIds)
                .append("postIds", postIds)
                .append("roleId", roleId)
                .toString();
    }
}
