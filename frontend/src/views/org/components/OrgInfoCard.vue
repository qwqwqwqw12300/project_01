<template>
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>机构基本信息</span>
      </div>
      <el-descriptions :column="2">
        <el-descriptions-item label="机构名称">{{ org==undefined?"":org.orgName }}</el-descriptions-item>
        <el-descriptions-item label="上级机构">{{ org==undefined?"":org.parentName }}</el-descriptions-item>
        <el-descriptions-item label="机构地址">{{ org==undefined?"":org.address }}</el-descriptions-item>
        <el-descriptions-item label="机构负责人">{{ org==undefined?"":org.leader }}</el-descriptions-item>
        <el-descriptions-item label="机构负责人电话">{{ org==undefined?"":org.leaderPhone }}</el-descriptions-item>
        <el-descriptions-item label="第一服务电话">{{ org==undefined?"":org.phone1 }}<span v-if="org&&org.attendantName1">({{ org.attendantName1 }})</span></el-descriptions-item>
        <el-descriptions-item label="第二服务电话">{{ org==undefined?"":org.phone2 }}<span v-if="org&&org.attendantName3">({{ org.attendantName3 }})</span></el-descriptions-item>
        <el-descriptions-item label="第三服务电话">{{ org==undefined?"":org.phone3 }}<span v-if="org&&org.attendantName3">({{ org.attendantName3 }})</span></el-descriptions-item>
      </el-descriptions>
    </el-card>
</template>

<script>
import {getOrg} from "@/api/org/org";

export default {
  name: "OrgInfoCard"
  ,
  props: {
    value: {
      default: undefined
    }
  },
  created() {
    this.initOrg()
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
        if (this.org?.orgId != this.value) {
          this.initOrg();
        }
      }
    }
  },
  methods: {
    initOrg() {
      if (this.value != undefined)
        getOrg(this.value).then(response => {
          this.org = response.data;
        })
    }
  }
}
</script>
