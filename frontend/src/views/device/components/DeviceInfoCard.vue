<template>
  <el-card class="box-card">
    <div slot="header" class="clearfix">
      <span>设备基本信息</span>
    </div>
    <el-descriptions :column="2">
      <el-descriptions-item label="设备名称：">XX雷达波设备</el-descriptions-item>
      <el-descriptions-item label="设备位置：">卫生间</el-descriptions-item>
      <el-descriptions-item label="雷达波设备：">雷达波设备</el-descriptions-item>
      <el-descriptions-item label="设备供应商：">中国雷达公司</el-descriptions-item>
      <el-descriptions-item label="设备编号：">100000022201111</el-descriptions-item>
      <el-descriptions-item label="设备配网时间：">2022-10-01 00:00:00</el-descriptions-item>
      <el-descriptions-item label="是否在线：">在线</el-descriptions-item>
    </el-descriptions>
  </el-card>
</template>

<script>
import {getDevice} from "@/api/device/device";

export default {
name: "DeviceInfoCard"
,
props: {
  value: {
    type: Number,
    default: undefined
  }
},
created() {
  this.initDevice()
},
data() {
  return {
    org: undefined
  }
},
watch: {
  "value": {
    immediate: true,
    handler: function (val) {
      if (this.device?.deviceId != this.value) {
        this.initDevice();
      }
    }
  }
},
methods: {
  initDevice() {
    if (this.value != undefined)
      getDevice(this.value).then(response => {
        this.device = response.data;
      })
  }
}
}
</script>
