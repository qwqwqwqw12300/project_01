package com.newlandnpt.varyar.system.service.impl;

import java.util.List;

import com.newlandnpt.varyar.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.newlandnpt.varyar.system.mapper.TNoticeMapper;
import com.newlandnpt.varyar.system.domain.TNotice;
import com.newlandnpt.varyar.system.service.ITNoticeService;

/**
 * 公告Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-01-12
 */
@Service
public class TNoticeServiceImpl implements ITNoticeService 
{
    @Autowired
    private TNoticeMapper tNoticeMapper;

    /**
     * 查询公告
     * 
     * @param noticeId 公告主键
     * @return 公告
     */
    @Override
    public TNotice selectTNoticeByNoticeId(Long noticeId)
    {
        return tNoticeMapper.selectTNoticeByNoticeId(noticeId);
    }

    public List<TNotice> selectTNoticeByMemberId(Long memberId){
        return tNoticeMapper.selectTNoticeByMemberId(memberId);
    }
    /**
     * 查询公告列表
     * 
     * @param tNotice 公告
     * @return 公告
     */
    @Override
    public List<TNotice> selectTNoticeList(TNotice tNotice)
    {
        return tNoticeMapper.selectTNoticeList(tNotice);
    }

    /**
     * 新增公告
     * 
     * @param tNotice 公告
     * @return 结果
     */
    @Override
    public int insertTNotice(TNotice tNotice)
    {
        tNotice.setCreateTime(DateUtils.getNowDate());
        return tNoticeMapper.insertTNotice(tNotice);
    }

    /**
     * 修改公告
     * 
     * @param tNotice 公告
     * @return 结果
     */
    @Override
    public int updateTNotice(TNotice tNotice)
    {
        tNotice.setUpdateTime(DateUtils.getNowDate());
        return tNoticeMapper.updateTNotice(tNotice);
    }

    /**
     * 批量删除公告
     * 
     * @param noticeIds 需要删除的公告主键
     * @return 结果
     */
    @Override
    public int deleteTNoticeByNoticeIds(Long[] noticeIds)
    {
        return tNoticeMapper.deleteTNoticeByNoticeIds(noticeIds);
    }

    /**
     * 删除公告信息
     * 
     * @param noticeId 公告主键
     * @return 结果
     */
    @Override
    public int deleteTNoticeByNoticeId(Long noticeId)
    {
        return tNoticeMapper.deleteTNoticeByNoticeId(noticeId);
    }
}
