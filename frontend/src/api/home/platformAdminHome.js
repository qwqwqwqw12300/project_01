import request from '@/utils/request'

// 设备数量
export function deviceCount() {
  return request({
    url: '/home/platform/admin/device/count',
    method: 'get'
  })
}

// 未分配（配对）设备数量
export function notAssociateDeviceCount() {
  return request({
    url: '/home/platform/admin/device/notassociate/count',
    method: 'get'
  })
}

// 会员总数
export function memberCount() {
  return request({
    url: '/home/platform/admin/member/count',
    method: 'get'
  })
}

// 未分配会员总数
export function notArrangeMemberCount() {
  return request({
    url: '/home/platform/admin/member/notarrange/count',
    method: 'get'
  })
}

// 机构总数
export function orgCount() {
  return request({
    url: '/home/platform/admin/org/count',
    method: 'get'
  })
}

// 未处理消息（事件）数
export function unHandleEventCount() {
  return request({
    url: '/home/platform/admin/event/unhandle/count',
    method: 'get'
  })
}

// 未处理会员消息（事件）数
export function unHandleMemberEventCount() {
  return request({
    url: '/home/platform/admin/event/member/unhandle/count',
    method: 'get'
  })
}
