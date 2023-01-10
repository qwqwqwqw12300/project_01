import request from '@/utils/request'

// 查询设备组分页
export function pageDeviceGroup(query) {
  return request({
    url: '/org/devicegroup/page',
    method: 'get',
    params: query
  })
}
// 添加设备组
export function addDeviceGroup(data) {
  return request({
    url: '/org/devicegroup',
    method: 'post',
    data: data
  })
}
// 修改设备组
export function updateDeviceGroup(data) {
  return request({
    url: '/org/devicegroup',
    method: 'put',
    data: data
  })
}
// 分配运营人员
export function arrangeUser(data,userId) {
  return request({
    url: '/org/devicegroup/arrangeUser/'+userId,
    method: 'put',
    data: data
  })
}
// 设备组分配设备
export function deviceArrange(deviceGroupId,data) {
  return request({
    url: '/org/devicegroup/'+deviceGroupId+'/deviceArrange',
    method: 'put',
    data: data
  })
}
