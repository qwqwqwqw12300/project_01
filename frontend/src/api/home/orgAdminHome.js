import request from '@/utils/request'

// 获取分支机构列表(包含本身)
export function subOrgList() {
  return request({
    url: '/home/org/admin/suborg/list',
    method: 'get'
  })
}

// 统计分支机构设备数
export function subOrgDeviceCount() {
  return request({
    url: '/home/org/admin/suborg/device/count/groupbyorg',
    method: 'get'
  })
}

// 统计机构未处理消息（事件）数
export function orgUnHandleEventCount(orgId) {
  return request({
    url: '/home/org/admin/org/'+orgId+'/event/unhandle/count',
    method: 'get'
  })
}

// 未分配（配对）设备数量
export function notAssociateDeviceCount() {
  return request({
    url: '/home/org/admin/device/notassociate/count',
    method: 'get'
  })
}

// 未分组设备数量
export function notArrangeDeviceCount() {
  return request({
    url: '/home/org/admin/device/notarrange/count',
    method: 'get'
  })
}

// 未分配设备组数量
export function notArrangeDeviceGroupCount() {
  return request({
    url: '/home/org/admin/devicegroup/notarrange/count',
    method: 'get'
  })
}
