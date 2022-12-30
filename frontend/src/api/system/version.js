import request from '@/utils/request'

// 查询版本列表
export function listVersion(query) {
  return request({
    url: '/system/version/list',
    method: 'get',
    params: query
  })
}

// 查询版本详细
export function getVersion(versionId) {
  return request({
    url: '/system/version/' + versionId,
    method: 'get'
  })
}

// 新增版本
export function addVersion(data) {
  return request({
    url: '/system/version',
    method: 'post',
    data: data
  })
}

// 修改版本
export function updateVersion(data) {
  return request({
    url: '/system/version',
    method: 'put',
    data: data
  })
}

// 删除版本
export function delVersion(versionId) {
  return request({
    url: '/system/version/' + versionId,
    method: 'delete'
  })
}
