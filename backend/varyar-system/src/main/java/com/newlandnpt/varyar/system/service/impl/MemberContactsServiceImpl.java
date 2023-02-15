package com.newlandnpt.varyar.system.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.newlandnpt.varyar.common.core.domain.model.MemberContactsRequest;
import com.newlandnpt.varyar.common.exception.ServiceException;
import com.newlandnpt.varyar.common.utils.DateUtils;
import com.newlandnpt.varyar.common.utils.SecurityUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.newlandnpt.varyar.system.mapper.TMemberContactsMapper;
import com.newlandnpt.varyar.system.domain.TMemberContacts;
import com.newlandnpt.varyar.system.service.IMemberContactsService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 会员联络人Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-12-24
 */
@Service
public class MemberContactsServiceImpl implements IMemberContactsService
{
    @Autowired
    private TMemberContactsMapper memberContactsMapper;

    /**
     * 查询会员联络人
     * 
     * @param memberContactsId 会员联络人主键
     * @return 会员联络人
     */
    @Override
    public TMemberContacts selectMemberContactsByMemberContactsId(Long memberContactsId)
    {
        return memberContactsMapper.selectMemberContactsByMemberContactsId(memberContactsId);
    }

    /**
     * 查询会员联络人列表
     * 
     * @param memberContacts 会员联络人
     * @return 会员联络人
     */
    @Override
    public List<TMemberContacts> selectMemberContactsList(TMemberContacts memberContacts)
    {
        return memberContactsMapper.selectMemberContactsList(memberContacts);
    }

    /**
     * 新增会员联络人
     * 
     * @param memberContacts 会员联络人
     * @return 结果
     */
    @Override
    public int insertMemberContacts(TMemberContacts memberContacts)
    {
        memberContacts.setCreateTime(DateUtils.getNowDate());
        return memberContactsMapper.insertMemberContacts(memberContacts);
    }

    /**
     * 修改会员联络人
     * 
     * @param memberContacts 会员联络人
     * @return 结果
     */
    @Override
    public int updateMemberContacts(TMemberContacts memberContacts)
    {
        memberContacts.setUpdateTime(DateUtils.getNowDate());
        return memberContactsMapper.updateMemberContacts(memberContacts);
    }

    /**
     * 批量删除会员联络人
     * 
     * @param memberContactsIds 需要删除的会员联络人主键
     * @return 结果
     */
    @Override
    public int deleteMemberContactsByMemberContactsIds(Long[] memberContactsIds)
    {
        return memberContactsMapper.deleteMemberContactsByMemberContactsIds(memberContactsIds);
    }

    /**
     * 删除会员联络人信息
     * 
     * @param memberContactsId 会员联络人主键
     * @return 结果
     */
    @Override
    public int deleteMemberContactsByMemberContactsId(Long memberContactsId)
    {
        return memberContactsMapper.deleteMemberContactsByMemberContactsId(memberContactsId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,readOnly = false)
    public int setMemberContacts(Long memberId, List<MemberContactsRequest> memberContactsRequestList) {

        TMemberContacts cond = new TMemberContacts();
        cond.setMemberId(memberId);

        int result = 0;

        Map<String,List<MemberContactsRequest>> memberContactsRequests = memberContactsRequestList.stream().collect(Collectors.groupingBy(p->p.getOrderNum()));

        for(String orderNum:memberContactsRequests.keySet()){
            List<MemberContactsRequest> requests = memberContactsRequests.get(orderNum);
            if(requests.size()>1){
                throw new ServiceException("第"+orderNum+"紧急联系人重复！");
            }
        }

        List<TMemberContacts> memberContactsList = selectMemberContactsList(cond);

        for(MemberContactsRequest memberContactsRequest:memberContactsRequestList){
            TMemberContacts memberContacts = memberContactsList==null?null:
                    memberContactsList.stream()
                            .filter(p->StringUtils.equals(memberContactsRequest.getMemberContactsId(),""+p.getMemberContactsId()))
                            .findAny()
                            .orElse(null);

            if(memberContacts == null){
                memberContacts = new TMemberContacts();
                memberContacts.setMemberId(memberId);
                memberContacts.setCreateBy(SecurityUtils.getUsername());
                memberContacts.setName(memberContactsRequest.getName());
                memberContacts.setPhone(memberContactsRequest.getPhone());
                memberContacts.setOrderNum(Long.valueOf(memberContactsRequest.getOrderNum()));
                memberContacts.setDelFlag("0");
                insertMemberContacts(memberContacts);
            }else{
                memberContacts.setName(memberContactsRequest.getName());
                memberContacts.setPhone(memberContactsRequest.getPhone());
                memberContacts.setOrderNum(Long.valueOf(memberContactsRequest.getOrderNum()));
                updateMemberContacts(memberContacts);
            }
            result++;
        }

        List<Long> removeIds = memberContactsList==null?new ArrayList<>(0):
                memberContactsList.stream()
                        .filter(p-> CollectionUtils.isEmpty(memberContactsRequestList)||
                                memberContactsRequestList.stream().noneMatch(q-> StringUtils.equals(q.getMemberContactsId(),""+p.getMemberContactsId())))
                        .map(p->p.getMemberContactsId())
                        .collect(Collectors.toList());
        if(removeIds.size()>0){
            deleteMemberContactsByMemberContactsIds(removeIds.stream().toArray(Long[]::new));
        }

        return result;
    }
}
