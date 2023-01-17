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

// 查询家庭组信息列表
export function listFamily(query) {
  return request({
    url: '/member/' + query.memberId + '/family/page',
    method: 'get',
    params: query

  })
}

// 运营分配会员
export function memberArrange(userId,data) {
  return request({
    // url: deviceGroupId==null?('/org/devicegroup/devices/arrange'):('/org/devicegroup/'+deviceGroupId+'/devices/arrange'),
    url: userId==null?'/member/arrange/user':'/member/arrange/user/'+userId,
    method: 'put',
    data: data
  })
}
