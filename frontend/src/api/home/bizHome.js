import request from '@/utils/request'

// 运营关心的设备列表
export function notArrangeMemberCount() {
  return request({
    url: '/home/biz/device/care/list',
    method: 'get'
  })
}
// 查询设备的事件数 根据紧急级别分组
export function countUnHandleByDeviceGroupByLevel(deviceId) {
  return request({
    url: '/home/biz/device/'+deviceId+'/event/unhandle/count/groupbylevel',
    method: 'get'
  })
}
