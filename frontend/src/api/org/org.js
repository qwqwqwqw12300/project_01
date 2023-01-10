import request from '@/utils/request'

// 查询部门列表
export function listOrg(query) {
  return request({
    url: '/org/list',
    method: 'get',
    params: query
  })
}
// 查询部门分页
export function pageOrg(query) {
  return request({
    url: '/org/page',
    method: 'get',
    params: query
  })
}

// 查询部门列表（排除节点）
export function listOrgExcludeChild(orgId) {
  return request({
    url: '/org/list/exclude/' + orgId,
    method: 'get'
  })
}

// 查询部门详细
export function getOrg(orgId) {
  return request({
    url: '/org/' + orgId,
    method: 'get'
  })
}

// 新增部门
export function addOrg(data) {
  console.log(JSON.stringify(data))
  return request({
    url: '/org',
    method: 'post',
    data: data
  })
}

// 修改部门
export function updateOrg(data) {
  return request({
    url: '/org',
    method: 'put',
    data: data
  })
}

// 删除部门
export function delOrg(orgId) {
  return request({
    url: '/org/' + orgId,
    method: 'delete'
  })
}
