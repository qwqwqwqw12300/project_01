<template>
  <div class="app-container home">
    <el-row>
      <el-card>
        <div slot="header" class="clearfix">
          <span class="el-dialog__title">分支机构</span>
        </div>
        <water-fall :value="orgList">
          <template slot-scope="{item}">
            <el-card shadow="hover" class="card-item card-item-click" @click.native="goNotAssosiateDevice()">
              {{item.orgName}}
              <br>
              共{{orgUnHandleEventCountMap.get(item.orgId)}}台设备
            </el-card>
          </template>
        </water-fall>
      </el-card>
    </el-row>
    <el-row>
      <el-card>
        <div slot="header" class="clearfix">
          <span class="el-dialog__title">工作面板</span>
        </div>
        <el-row>
          <el-col :span="6">
            <el-card shadow="hover" class="card-item card-item-click" @click.native="goNotAssosiateDevice()">
              待分配设备
              <br>
              {{ notAssociateDeviceCount }}台
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card shadow="hover" class="card-item card-item-click">
              待分组设备
              <br>
              {{ notArrangeDeviceCount }}台
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card shadow="hover" class="card-item card-item-click">
              待分配运营人员设备组
              <br>
              {{notArrangeDeviceGroupCount}}组
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card shadow="hover" class="card-item card-item-click" @click.native="goOrg()">
              机构设备消息（未处理）
              <br>
              {{orgUnHandleEventCount}}条
            </el-card>
          </el-col>
        </el-row>
      </el-card>
    </el-row>
  </div>
</template>

<script>
import {subOrgList,subOrgDeviceCount,orgUnHandleEventCount,notAssociateDeviceCount,notArrangeDeviceCount,notArrangeDeviceGroupCount} from "@/api/home/orgAdminHome"
import {getUserProfile, getUser, pageUser} from "@/api/system/user";

export default {

  name: "Index",
  data() {
    return {
      orgList:[],
      orgDeviceCountList:[],
      orgUnHandleEventCount:undefined,
      notAssociateDeviceCount:undefined,
      notArrangeDeviceCount:undefined,
      notArrangeDeviceGroupCount:undefined,
      orgUnHandleEventCountMap:new Map()
    };
  },
  created(){
    this.init()
  },
  activated(){
    this.init()
  },
  methods: {
    init() {
      this.orgList=[];
      this.orgDeviceCountList=[];
      this.orgUnHandleEventCount=undefined;
      this.notAssociateDeviceCount=undefined;
      this.notArrangeDeviceCount=undefined;
      this.notArrangeDeviceGroupCount=undefined;
      subOrgList().then(response => {
        this.orgList = response.data;
        this.orgList.forEach(org=>{
          orgUnHandleEventCount(org.orgId).then(response => {
            this.orgUnHandleEventCountMap.set(org.orgId,response.data)
            console.log(JSON.stringify(response)+">>>>>"+org.orgId+">>>>>>>"+JSON.stringify(this.orgUnHandleEventCountMap))
          })
        });
      });
      subOrgDeviceCount().then(response => this.orgDeviceCountList = response.data);
      getUserProfile().then(user =>{
        if(user.data.orgId == undefined){
          this.orgUnHandleEventCount=0
        }else{
          orgUnHandleEventCount(user.data.orgId).then(response => this.orgUnHandleEventCount = response.data)
        }
      });
      notAssociateDeviceCount().then(response => this.notAssociateDeviceCount = response.data);
      notArrangeDeviceCount().then(response => this.notArrangeDeviceCount = response.data);
      notArrangeDeviceGroupCount().then(response => this.notArrangeDeviceGroupCount = response.data);
    },
  },
};
</script>

<style scoped lang="scss">

.el-row {
  margin-bottom: 20px;

  &
  :last-child {
    margin-bottom: 0;
  }
}
</style>

