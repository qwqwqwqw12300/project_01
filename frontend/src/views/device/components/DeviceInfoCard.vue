<template>
  <el-card class="box-card">
    <div slot="header" class="clearfix">
      <span>设备基本信息</span>
    </div>
    <el-descriptions :column="2">
      <el-descriptions-item label="设备名称">{{ device== undefined?"":device.name}}</el-descriptions-item>
      <el-descriptions-item label="设备位置">{{ device== undefined?"":device.location}}</el-descriptions-item>
      <el-descriptions-item label="设备类型">{{ device== undefined?"":deviceTypeFormat(device.type)}}</el-descriptions-item>
      <el-descriptions-item label="设备供应商">{{ device== undefined?"":device.org_name}}</el-descriptions-item>
      <el-descriptions-item label="设备编号">{{ device== undefined?"":device.no}}</el-descriptions-item>
      <el-descriptions-item label="设备配网时间">{{ device== undefined?"":device.register_time}}</el-descriptions-item>
      <el-descriptions-item label="是否在线">{{ device== undefined?"":device.status}}</el-descriptions-item>
    </el-descriptions>
  </el-card>
</template>

<script>
import {getDevice} from "@/api/device/device";

export default {
name: "DeviceInfoCard",
dicts: ['device_type'],

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
    device: undefined
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
  },
    // 设备类型字典翻译
    deviceTypeFormat(column) {
      return this.selectDictLabel(this.dict.type.device_type, column)
    },
}
}
</script>
