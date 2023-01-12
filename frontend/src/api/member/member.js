import request from '@/utils/request'

// 查询部门详细
export function getMember(memberId) {
  return request({
    url: '/member/' + memberId,
    method: 'get'
  })
}
