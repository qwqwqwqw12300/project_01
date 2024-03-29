import request from '@/utils/request'

// 查询设备组分页
export function pageDevice(query) {
  return request({
    url: '/device/page',
    method: 'get',
    params: query
  })
}

// 设备配对
export function associateDevice(object) {
  return request({
    url: '/device/associate',
    method: 'put',
    data: object
  })
}
//分配设备组
export function groupArrange(deviceId,deviceGroupId) {
  return request({
    url: deviceGroupId==null?'/device/'+deviceId+'/arrange/group':'/device/'+deviceId+'/arrange/group/'+deviceGroupId,
    method: 'put'
  })
}



// 查询设备详细
export function getDevice(deviceId) {
  return request({
    url: '/device/' + deviceId,
    method: 'get'
  })
}

// 设备上线
export function active(deviceId) {
  return request({
    url: '/device/' + deviceId+ '/status/active',
    method: 'put'
  })
}

// 设备下线
export function offline(deviceId) {
  return request({
    url: '/device/' + deviceId + '/status/offline',
    method: 'put'
  })
}

// 加载设备参数
export function loadSettings(deviceId) {
  return request({
    url: '/device/' + deviceId + '/settings',
    method: 'get'
  })
}

// 设置设备参数
export function setSettings(deviceId,data) {
  return request({
    url: '/device/' + deviceId + '/settings',
    method: 'put',
    data:data
  })
}