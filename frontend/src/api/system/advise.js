import request from '@/utils/request'

// 查询问题建议管理列表
export function listAdvise(query) {
  return request({
    url: '/system/advise/list',
    method: 'get',
    params: query
  })
}

// 查询问题建议管理详细
export function getAdvise(adviseId) {
  return request({
    url: '/system/advise/' + adviseId,
    method: 'get'
  })
}

// 新增问题建议管理
export function addAdvise(data) {
  return request({
    url: '/system/advise',
    method: 'post',
    data: data
  })
}

// 修改问题建议管理
export function updateAdvise(data) {
  return request({
    url: '/system/advise',
    method: 'put',
    data: data
  })
}

// 一键已读
export function onReadAdvise(adviseId) {
  return request({
    url: '/system/advise/' + adviseId,
    method: 'put'
  })
}

// 删除问题建议管理
export function delAdvise(adviseId) {
  return request({
    url: '/system/advise/' + adviseId,
    method: 'delete'
  })
}
