<template>
  
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>会员基本信息</span>
      </div>
      <el-descriptions :column="3">
        <el-descriptions-item label="会员手机号：">{{ member== undefined?"":member.phone}}</el-descriptions-item>
        <el-descriptions-item label="会员编号：">{{ member== undefined?"":member.no }}</el-descriptions-item>
        <el-descriptions-item label="会员注册时间：">{{ member== undefined?"":member.createTime }}</el-descriptions-item>
        <el-descriptions-item label="服务人员：">{{ member== undefined?"":member.userName }}</el-descriptions-item>
        <el-descriptions-item label="服务人员手机号码：">{{ member== undefined?"":member.userPhone }}</el-descriptions-item>
        <el-descriptions-item label="紧急联系电话1：">{{ contacts == undefined || contacts.length <= 0 ?"":member.contacts[0].phone }}</el-descriptions-item>
        <el-descriptions-item label="紧急联系电话2：">{{ contacts == undefined || contacts.length <= 0 ?"":member.contacts[1].phone }}</el-descriptions-item>
        <el-descriptions-item label="紧急联系电话3：">{{ contacts == undefined || contacts.length <= 0 ?"":member.contacts[2].phone }}</el-descriptions-item>
      </el-descriptions>
    </el-card>
</template>


<script>
import {getMember} from "@/api/member/member";

export default {
  name: "memberInfoCard"
  ,
  props: {
    value: {
      type: Number,
      default: undefined
    }
  },
  created() {
    this.initMember()
  },
  data() {
    return {
      member: undefined,
      phone : undefined,
      contacts : [ 
       
      ],
    }
  },
  watch: {
    "value": {
      immediate: true,
      handler: function (val) {
        if (this.member?.memberId != this.value) {
          this.initMember();
        }
      }
    }
  },
  methods: {
    initMember() {
      if (this.value != undefined)
        getMember(this.value).then(response => {
          this.member = response.data;
        })
    }
  }
}
</script>

