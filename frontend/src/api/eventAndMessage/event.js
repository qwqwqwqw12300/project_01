import request from '@/utils/request'

// 查询事件列表
export function listEvent(query) {
  return request({
    url: '/event/list',
    method: 'get',
    params: query
  })
}

// 查询事件详细
export function getEvent(eventId) {
  return request({
    url: '/event/' + eventId,
    method: 'get'
  })
}

// 新增事件
export function addEvent(data) {
  return request({
    url: '/event',
    method: 'post',
    data: data
  })
}

// 修改事件
export function updateEvent(data) {
  return request({
    url: '/event',
    method: 'put',
    data: data
  })
}

// 删除事件
export function delEvent(eventId) {
  return request({
    url: '/event/' + eventId,
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