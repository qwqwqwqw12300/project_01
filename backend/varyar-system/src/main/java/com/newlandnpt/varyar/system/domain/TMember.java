package com.newlandnpt.varyar.system.domain;

import com.newlandnpt.varyar.common.core.domain.entity.MemberParameter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.newlandnpt.varyar.common.core.domain.BaseEntity;
import org.springframework.data.annotation.Transient;

import java.util.List;

/**
 * 会员对象 t_member
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
public class TMember extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 会员id */
    private Long memberId;

    /** 会员编号 */
    private String no;

    /** 会员手机号 */
    private String phone;

    /** 密码 */
    private String password;

    /** 会员昵称 */
    private String nickname;

    /** 头像地址 */
    private String avatar;


    /** 分配标志（0未分配 1已分配） */
    private String distributeFlag;

    /** 运营者id */
    private Long userId;

    /** 运营者姓名 */
    private String userName;

    /** 运营者手机号 */
    @Transient
    private String userPhone;

    /** 联络人 */
    @Transient
    private List<TMemberContacts> contacts;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 会员参数 */
    private MemberParameter parameter;


    /** 会员登录设备注册号 */
    private String registrationId;

    /** 登录设备注册类型（0:android 1:ios） */
    private String registrationType;

    /** 机构id */
    private Long orgId;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getRegistrationType() {
        return registrationType;
    }

    public void setRegistrationType(String registrationType) {
        this.registrationType = registrationType;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public MemberParameter getParameter() {
        return parameter;
    }

    public void setParameter(MemberParameter parameter) {
        this.parameter = parameter;
    }

    public void setMemberId(Long memberId)
    {
        this.memberId = memberId;
    }

    public Long getMemberId() 
    {
        return memberId;
    }
    public void setNo(String no) 
    {
        this.no = no;
    }

    public String getNo() 
    {
        return no;
    }
    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }
    public void setPassword(String password) 
    {
        this.password = password;
    }

    public String getPassword() 
    {
        return password;
    }
    public void setDistributeFlag(String distributeFlag) 
    {
        this.distributeFlag = distributeFlag;
    }

    public String getDistributeFlag() 
    {
        return distributeFlag;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setUserName(String userName) 
    {
        this.userName = userName;
    }

    public String getUserName() 
    {
        return userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public List<TMemberContacts> getContacts() {
        return contacts;
    }

    public void setContacts(List<TMemberContacts> contacts) {
        this.contacts = contacts;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("memberId", memberId)
                .append("no", no)
                .append("phone", phone)
                .append("nickname", nickname)
                .append("avatar", avatar)
                .append("password", password)
                .append("distributeFlag", distributeFlag)
                .append("userId", userId)
                .append("userName", userName)
                .append("userPhone", userPhone)
                .append("delFlag", delFlag)
                .append("registrationId", registrationId)
                .append("registrationType", registrationType)
                .append("orgId", orgId)
                .toString();
    }
}
