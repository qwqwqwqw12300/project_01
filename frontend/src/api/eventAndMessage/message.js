import request from '@/utils/request'

// 查询消息列表
export function listMsg(query) {
  return request({
    url: '/msg/list',
    method: 'get',
    params: query
  })
}

// 查询消息详细
export function getMsg(msgId) {
  return request({
    url: '/msg/' + msgId,
    method: 'get'
  })
}

// 新增消息
export function addMsg(data) {
  return request({
    url: '/msg',
    method: 'post',
    data: data
  })
}

// 修改消息
export function updateMsg(data) {
  return request({
    url: '/msg',
    method: 'put',
    data: data
  })
}

// 删除消息
export function delMsg(msgId) {
  return request({
    url: '/msg/' + msgId,
    method: 'delete'
  })
}

// 查询机构下拉树结构
export function orgTreeSelect() {
  return request({
    url: '/system/user/orgTree',
    method: 'get'
  })
}