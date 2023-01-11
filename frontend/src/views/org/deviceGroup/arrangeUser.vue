<template>
  <div class="app-container">
    <el-row>
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>机构基本信息</span>
        </div>
        <el-descriptions :column="2" v-if="org">
          <el-descriptions-item label="机构名称">{{ org.orgName }}</el-descriptions-item>
          <el-descriptions-item label="上级机构">{{ org.parentName }}</el-descriptions-item>
          <el-descriptions-item label="机构地址">{{ org.address }}</el-descriptions-item>
          <el-descriptions-item label="机构负责人">{{ org.leader }}</el-descriptions-item>
          <el-descriptions-item label="机构负责人电话">{{ org.leaderPhone }}</el-descriptions-item>
          <el-descriptions-item label="第一服务电话">{{ org.phone1 }}({{ org.attendantName1 }})</el-descriptions-item>
          <el-descriptions-item label="第二服务电话">{{ org.phone2 }}({{ org.attendantName2 }})</el-descriptions-item>
          <el-descriptions-item label="第三服务电话">{{ org.phone3 }}({{ org.attendantName3 }})</el-descriptions-item>
        </el-descriptions>
        <el-empty description="描述文字" v-else></el-empty>
      </el-card>
    </el-row>
  </div>
</template>

<script>
import {pageDeviceGroup, addDeviceGroup, updateDeviceGroup, arrangeUser, deviceArrange} from "@/api/org/deviceGroup"
import {getOrg} from "@/api/org/org";

export default {
  name: "DeviceGroupArrangeUser",
  data() {
    return {
      orgId: undefined,
      org: undefined
    }
  },
  async created() {
    this.orgId = this.$route.query.orgId;
    this.initOrg();
  },
  methods: {
    initOrg() {
      if(this.orgId != undefined)
        getOrg(this.orgId).then(response => {
          this.org = response.data;
        })
    },
  }
}
</script>

<style scoped>

</style>
