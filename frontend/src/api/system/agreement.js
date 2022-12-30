import request from '@/utils/request'

// 查询用户协议管理列表
export function listAgreement(query) {
  return request({
    url: '/system/agreement/list',
    method: 'get',
    params: query
  })
}

// 查询用户协议管理详细
export function getAgreement(agreementId) {
  return request({
    url: '/system/agreement/' + agreementId,
    method: 'get'
  })
}

// 新增用户协议管理
export function addAgreement(data) {
  return request({
    url: '/system/agreement',
    method: 'post',
    data: data
  })
}

// 修改用户协议管理
export function updateAgreement(data) {
  return request({
    url: '/system/agreement',
    method: 'put',
    data: data
  })
}

// 删除用户协议管理
export function delAgreement(agreementId) {
  return request({
    url: '/system/agreement/' + agreementId,
    method: 'delete'
  })
}
