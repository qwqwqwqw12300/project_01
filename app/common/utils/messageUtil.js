/*
 * @Date: 2023-05-15 16:10:13
 * @LastEditTime: 2023-05-15 16:10:13
 * @FilePath: /common/utils/messageUtil.js
 * @Description: 雷达波数据处理
 */

// 设备接口工具
import message from '@/static/js/weichatPb/message.js';
// buf数据处理工具
import protobuf from '@/static/js/weichatPb/protobuf.js';

const MessageRoot = protobuf.Root.fromJSON(message);

export const WifiCred = MessageRoot.lookupType("WifiCred");
export const ToDeviceMessage = MessageRoot.lookupType("ToDeviceMessage");
export const CloudDetails = MessageRoot.lookupType("CloudDetails");
export const Pair = MessageRoot.lookupType("Pair");
export const PairingComplete = MessageRoot.lookupType("PairingComplete");
export const ToAppMessage = MessageRoot.lookupType("ToAppMessage");
export const ToDeviceMessageType = MessageRoot.ToDeviceMessageType;
export const PairResult = MessageRoot.lookupType("PairResult");