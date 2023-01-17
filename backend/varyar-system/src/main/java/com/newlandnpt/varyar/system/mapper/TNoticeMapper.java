package com.newlandnpt.varyar.system.mapper;

import java.util.List;
import com.newlandnpt.varyar.system.domain.TNotice;

/**
 * 公告Mapper接口
 * 
 * @author ruoyi
 * @date 2023-01-12
 */
public interface TNoticeMapper 
{
    /**
     * 查询公告
     * 
     * @param noticeId 公告主键
     * @return 公告
     */
    public TNotice selectTNoticeByNoticeId(Long noticeId);
    public List<TNotice> selectTNoticeByMemberId(Long memberId);
    /**
     * 查询公告列表
     * 
     * @param tNotice 公告
     * @return 公告集合
     */
    public List<TNotice> selectTNoticeList(TNotice tNotice);

    /**
     * 新增公告
     * 
     * @param tNotice 公告
     * @return 结果
     */
    public int insertTNotice(TNotice tNotice);

    /**
     * 修改公告
     * 
     * @param tNotice 公告
     * @return 结果
     */
    public int updateTNotice(TNotice tNotice);

    /**
     * 删除公告
     * 
     * @param noticeId 公告主键
     * @return 结果
     */
    public int deleteTNoticeByNoticeId(Long noticeId);

    /**
     * 批量删除公告
     * 
     * @param noticeIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTNoticeByNoticeIds(Long[] noticeIds);
}
