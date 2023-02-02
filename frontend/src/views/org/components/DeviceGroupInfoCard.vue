<template>
  <el-card class="box-card">
    <div slot="header" class="clearfix">
      <span>设备分组信息</span>
    </div>
    <el-descriptions :column="3">
      <el-descriptions-item span="3" label="设备组名称">{{ deviceGroup==undefined?"":deviceGroup.name }}</el-descriptions-item>
<!--      <el-descriptions-item span="2" label="设备组编号">{{ deviceGroup==undefined?"":deviceGroup.no }}</el-descriptions-item>-->
      <el-descriptions-item label="运营人员姓名">{{ deviceGroupUser==undefined?"":deviceGroupUser.name }}</el-descriptions-item>
      <el-descriptions-item label="运营人员手机号">{{ deviceGroupUser==undefined?"":deviceGroupUser.mobilePhone }}</el-descriptions-item>
      <el-descriptions-item label="运营人员登录账号">{{ deviceGroupUser==undefined?"":deviceGroupUser.userName }}</el-descriptions-item>
    </el-descriptions>
  </el-card>
</template>

<script>
import {getDeviceGroup} from "@/api/org/deviceGroup"
import {getUserProfile, getUser, pageUser} from "@/api/system/user";

export default {
  name: "DeviceGroupInfoCard",
  props: {
    value: {
      default: undefined
    }
  },
  created() {
    this.initDeviceGroup()
  },
  data() {
    return {
      deviceGroupUser: undefined,
      deviceGroup: undefined,
    }
  },
  watch: {
    "value": {
      immediate: true,
      handler: function (val) {
        if (this.org?.orgId != this.value) {
          this.initDeviceGroup();
        }
      }
    }
  },
  methods: {
    async initDeviceGroup() {
      if (this.value != undefined) {
        let {data} = await getDeviceGroup(this.value);
        this.deviceGroup = data;
        if (this.deviceGroup?.userId != undefined) {
          let {data} = await getUser(this.deviceGroup?.userId);
          this.deviceGroupUser = data
        }
      }
    }
  }
}
</script>

<style scoped>

</style>
