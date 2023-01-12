import request from '@/utils/request'

// 查询设备组分页
export function pageDevice(query) {
  return request({
    url: '/device/page',
    method: 'get',
    params: query
  })
}


// 查询设备详细
export function getDevice(deviceId) {
  return request({
    url: '/device/' + deviceId,
    method: 'get'
  })
}