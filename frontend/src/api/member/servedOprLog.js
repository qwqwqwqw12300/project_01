import request from '@/utils/request'

// 查询服务记录列表
export function listRecord(query) {
  return request({
    //url: '/system/record/list',
    url: '/device/serve/record/page',

    method: 'get',
    params: query
  })
}

// // 查询服务记录详细
// export function getRecord(recordId) {
//   return request({
//     url: '/system/record/' + recordId,
//     method: 'get'
//   })
// }

// 新增服务记录
export function addRecord(data) {
  return request({
    url: '/device/serve/record',
    method: 'post',
    data: data
  })
}

// // 修改服务记录
// export function updateRecord(data) {
//   return request({
//     url: '/system/record',
//     method: 'put',
//     data: data
//   })
// }

// // 删除服务记录
// export function delRecord(recordId) {
//   return request({
//     url: '/system/record/' + recordId,
//     method: 'delete'
//   })
// }
