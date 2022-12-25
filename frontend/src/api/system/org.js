import request from '@/utils/request'

// 查询部门列表
export function listOrg(query) {
  return request({
    url: '/system/org/list',
    method: 'get',
    params: query
  })
}

// 查询部门列表（排除节点）
export function listOrgExcludeChild(orgId) {
  return request({
    url: '/system/org/list/exclude/' + orgId,
    method: 'get'
  })
}

// 查询部门详细
export function getOrg(orgId) {
  return request({
    url: '/system/org/' + orgId,
    method: 'get'
  })
}

// 新增部门
export function addOrg(data) {
  console.log(JSON.stringify(data))
  return request({
    url: '/system/org',
    method: 'post',
    data: data
  })
}

// 修改部门
export function updateOrg(data) {
  return request({
    url: '/system/org',
    method: 'put',
    data: data
  })
}

// 删除部门
export function delOrg(orgId) {
  return request({
    url: '/system/org/' + orgId,
    method: 'delete'
  })
}
