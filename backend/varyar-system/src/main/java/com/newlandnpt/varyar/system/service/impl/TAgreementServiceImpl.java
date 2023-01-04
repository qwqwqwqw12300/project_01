package com.newlandnpt.varyar.system.service.impl;

import java.util.List;
import com.newlandnpt.varyar.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.newlandnpt.varyar.system.mapper.TAgreementMapper;
import com.newlandnpt.varyar.system.domain.TAgreement;
import com.newlandnpt.varyar.system.service.ITAgreementService;

/**
 * 协议Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
@Service
public class TAgreementServiceImpl implements ITAgreementService 
{
    @Autowired
    private TAgreementMapper tAgreementMapper;

    /**
     * 查询协议
     * 
     * @param agreementId 协议主键
     * @return 协议
     */
    @Override
    public TAgreement selectTAgreementByAgreementId(Long agreementId)
    {
        return tAgreementMapper.selectTAgreementByAgreementId(agreementId);
    }

    /**
     * 查询协议列表
     * 
     * @param tAgreement 协议
     * @return 协议
     */
    @Override
    public List<TAgreement> selectTAgreementList(TAgreement tAgreement)
    {
        return tAgreementMapper.selectTAgreementList(tAgreement);
    }

    /**
     * 新增协议
     * 
     * @param tAgreement 协议
     * @return 结果
     */
    @Override
    public int insertTAgreement(TAgreement tAgreement)
    {
        tAgreement.setCreateTime(DateUtils.getNowDate());
        return tAgreementMapper.insertTAgreement(tAgreement);
    }

    /**
     * 修改协议
     * 
     * @param tAgreement 协议
     * @return 结果
     */
    @Override
    public int updateTAgreement(TAgreement tAgreement)
    {
        tAgreement.setUpdateTime(DateUtils.getNowDate());
        return tAgreementMapper.updateTAgreement(tAgreement);
    }

    /**
     * 批量删除协议
     * 
     * @param agreementIds 需要删除的协议主键
     * @return 结果
     */
    @Override
    public int deleteTAgreementByAgreementIds(Long[] agreementIds)
    {
        return tAgreementMapper.deleteTAgreementByAgreementIds(agreementIds);
    }

    /**
     * 删除协议信息
     * 
     * @param agreementId 协议主键
     * @return 结果
     */
    @Override
    public int deleteTAgreementByAgreementId(Long agreementId)
    {
        return tAgreementMapper.deleteTAgreementByAgreementId(agreementId);
    }

    /**
     * 查询协议
     *
     * @param agreementType 协议类型
     * @return 协议
     */
    @Override
    public TAgreement selectTAgreementByAgreementType(String agreementType)
    {
        return tAgreementMapper.selectTAgreementByAgreementType(agreementType);
    }
}
