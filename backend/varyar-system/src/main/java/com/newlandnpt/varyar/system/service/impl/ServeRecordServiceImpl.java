package com.newlandnpt.varyar.system.service.impl;

import java.util.List;

import com.newlandnpt.varyar.common.annotation.DataScope;
import com.newlandnpt.varyar.common.core.domain.entity.SysUser;
import com.newlandnpt.varyar.common.exception.ServiceException;
import com.newlandnpt.varyar.system.domain.TServeRecordEventRelate;
import com.newlandnpt.varyar.system.mapper.TEventMapper;
import com.newlandnpt.varyar.system.mapper.TServeRecordEventRelateMapper;
import com.newlandnpt.varyar.system.mapper.SysUserMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.newlandnpt.varyar.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.newlandnpt.varyar.system.mapper.TServeRecordMapper;
import com.newlandnpt.varyar.system.domain.TServeRecord;
import com.newlandnpt.varyar.system.service.IServeRecordService;

import static com.newlandnpt.varyar.common.constant.EventConstants.OPERATOR_TYPE_OPER;

/**
 * 服务记录Service业务层处理
 * 
 * @author lin.ju
 * @date 2023-01-04
 */
@Service
public class ServeRecordServiceImpl implements IServeRecordService 
{
    @Autowired
    private TServeRecordMapper serveRecordMapper;
    @Autowired
    private TServeRecordEventRelateMapper serveRecordEventRelateMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private TEventMapper eventMapper;


    /**
     * 查询服务记录
     * 
     * @param recordId 服务记录主键
     * @return 服务记录
     */
    @Override
    public TServeRecord selectServeRecordByRecordId(Long recordId)
    {
        return serveRecordMapper.selectServeRecordByRecordId(recordId);
    }

    /**
     * 查询服务记录列表
     * 
     * @param serveRecord 服务记录
     * @return 服务记录
     */
    @Override
    @DataScope(orgAlias="u",userAlias="u")
    public List<TServeRecord> selectServeRecordList(TServeRecord serveRecord)
    {
        return serveRecordMapper.selectServeRecordList(serveRecord);
    }

    /**
     * 新增服务记录
     * 
     * @param serveRecord 服务记录
     * @return 结果
     */
    @Override
    @Transactional(readOnly = false,propagation = Propagation.REQUIRED)
    public int insertServeRecord(TServeRecord serveRecord)
    {
        if(CollectionUtils.isEmpty(serveRecord.getServeEvents())){
            throw new ServiceException(String.format("处理消息不能为空"));
        }
        SysUser sysUser = sysUserMapper.selectUserById(serveRecord.getServedUserId());
        if(sysUser == null){
            throw new ServiceException(String.format("运营人员不存在"));
        }
        //清除非库表映射属性
        sysUser.clearTransient();
        serveRecord.setServedUserSnapshot(sysUser);
        serveRecord.autoSetCreateByLoginUser();
        serveRecord.setCreateTime(DateUtils.getNowDate());
        int effect = serveRecordMapper.insertServeRecord(serveRecord);
        serveRecord.getServeEvents().forEach(serveEventSimple -> {
            TServeRecordEventRelate relate = new TServeRecordEventRelate();
            relate.setRecordId(serveRecord.getRecordId());
            relate.setEventId(serveEventSimple.getEventId());
            serveRecordEventRelateMapper.insertServeRecordEventRelate(relate);
        });
        //将非会员事件处理状态进行修改
        eventMapper.dealNotMemberEvents(serveRecord.getServeEvents().stream()
                .map(p->p.getEventId()).toArray(Long[]::new),serveRecord.getServedUserId(),OPERATOR_TYPE_OPER);
        return effect;
    }

    /**
     * 修改服务记录
     * 
     * @param serveRecord 服务记录
     * @return 结果
     */
    @Override
    public int updateServeRecord(TServeRecord serveRecord)
    {
        //服务记录对应消息暂不修改
        return serveRecordMapper.updateServeRecord(serveRecord);
    }

    /**
     * 批量删除服务记录
     * 
     * @param recordIds 需要删除的服务记录主键
     * @return 结果
     */
    @Override
    public int deleteServeRecordByRecordIds(Long[] recordIds)
    {
        return serveRecordMapper.deleteServeRecordByRecordIds(recordIds);
    }

    /**
     * 删除服务记录信息
     * 
     * @param recordId 服务记录主键
     * @return 结果
     */
    @Override
    public int deleteServeRecordByRecordId(Long recordId)
    {
        return serveRecordMapper.deleteServeRecordByRecordId(recordId);
    }
}
