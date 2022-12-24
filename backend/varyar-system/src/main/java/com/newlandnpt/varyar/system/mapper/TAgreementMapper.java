package com.newlandnpt.varyar.system.mapper;

import java.util.List;
import com.newlandnpt.varyar.system.domain.TAgreement;

/**
 * 协议Mapper接口
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
public interface TAgreementMapper 
{
    /**
     * 查询协议
     * 
     * @param agreementId 协议主键
     * @return 协议
     */
    public TAgreement selectTAgreementByAgreementId(Long agreementId);

    /**
     * 查询协议列表
     * 
     * @param tAgreement 协议
     * @return 协议集合
     */
    public List<TAgreement> selectTAgreementList(TAgreement tAgreement);

    /**
     * 新增协议
     * 
     * @param tAgreement 协议
     * @return 结果
     */
    public int insertTAgreement(TAgreement tAgreement);

    /**
     * 修改协议
     * 
     * @param tAgreement 协议
     * @return 结果
     */
    public int updateTAgreement(TAgreement tAgreement);

    /**
     * 删除协议
     * 
     * @param agreementId 协议主键
     * @return 结果
     */
    public int deleteTAgreementByAgreementId(Long agreementId);

    /**
     * 批量删除协议
     * 
     * @param agreementIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTAgreementByAgreementIds(Long[] agreementIds);
}
