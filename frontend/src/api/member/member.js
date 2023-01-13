import request from '@/utils/request'

// 查询会员列表
export function listMember(query) {
  return request({
    url: '/member/page',
    method: 'get',
    params: query
  })
}

// 查询会员详细
export function getMember(memberId) {
  return request({
    url: '/member/' + memberId,
    method: 'get'
  })
}
