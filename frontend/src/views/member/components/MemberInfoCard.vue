<template>
  
  <el-card class="box-card">
    <div slot="header" class="clearfix">
      <span>会员基本信息</span>
    </div>
    <el-descriptions :column="3">
      <el-descriptions-item label="会员手机号">{{ member== undefined?"":member.phone}}</el-descriptions-item>
      <!-- <el-descriptions-item label="会员编号">{{ member== undefined?"":member.no }}</el-descriptions-item> -->
      <el-descriptions-item label="会员注册时间">{{ member== undefined?"":member.createTime }}</el-descriptions-item>
      <el-descriptions-item label=""></el-descriptions-item>
      <el-descriptions-item label="服务人员">{{ member== undefined?"":member.userName }}</el-descriptions-item>
      <el-descriptions-item label="服务人员手机号码">{{ member== undefined?"":member.userPhone }}</el-descriptions-item>
      <!-- <el-descriptions-item label="紧急联系电话1">{{ member.contacts == undefined || member.contacts.length == 0 ?"":member.contacts[0].phone }}</el-descriptions-item>
      <el-descriptions-item label="紧急联系电话2">{{ member.contacts == undefined || member.contacts.length == 0 ?"":member.contacts[1].phone }}</el-descriptions-item>
      <el-descriptions-item label="紧急联系电话3">{{ member.contacts == undefined || member.contacts.length == 0 ?"":member.contacts[2].phone }}</el-descriptions-item> -->
    </el-descriptions>
    <!-- 瀑布流布局 -->
    <el-row>
          <water-fall :value="contacts" :columnNumber="3">
            <template slot-scope="{item}">
              <el-card shadow="hover" class="card-item card-item-click" >
                <br>
                紧急联系电话{{item.orderNum}}：{{ item.phone }}<span v-if="item.name">  ({{ item.name }})</span>
              </el-card>
            </template>
          </water-fall>
    </el-row>
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
        this.contacts = response.data.contacts;
        //console.log(this.contacts + ">>>>>" + ">>>>>>>" )

      })
  },
}
}
</script>

