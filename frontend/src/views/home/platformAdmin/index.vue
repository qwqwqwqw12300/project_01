<template>
  <div class="app-container home">
    <el-row>
      <el-card>
        <div slot="header" class="clearfix">
          <span class="el-dialog__title">数据面板</span>
        </div>

        <el-row>
          <el-col :span="6">
            <el-card shadow="hover" class="card-item card-item-click"
                     @click.native="goDevice()">
              设备总数
              <br>
              {{ deviceCount }}台
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card shadow="hover" class="card-item card-item-click" @click.native="goMember()">
              会员数
              <br>
              {{ memberCount }}名
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card shadow="hover" class="card-item card-item-click" @click.native="goOrg()">
              接入机构数
              <br>
              {{ orgCount }}个
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card shadow="hover" class="card-item card-item-click" @click.native="goUnHandleEvent()">
              未处理消息数
              <br>
              {{ unHandleEventCount }}条
            </el-card>
          </el-col>

        </el-row>
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
              待分配运营人员的会员
              <br>
              {{ notArrangeMemberCount }}名
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card shadow="hover" class="card-item card-item-click">
              未处理的会员事件
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card shadow="hover" class="card-item card-item-click" @click.native="goOrg()">
              机构管理
            </el-card>
          </el-col>
        </el-row>
      </el-card>
    </el-row>
  </div>
</template>

<script>
import {
  deviceCount,
  notAssociateDeviceCount,
  memberCount,
  notArrangeMemberCount,
  orgCount,
  unHandleEventCount,
  unHandleMemberEventCount
} from "@/api/home/platformAdminHome"

export default {
  name: "Index",
  data() {
    return {
      deviceCount: undefined,
      notAssociateDeviceCount: undefined,
      memberCount: undefined,
      notArrangeMemberCount: undefined,
      orgCount: undefined,
      unHandleEventCount: undefined,
    };
  },
  created() {
    this.init();
  },
  activated() {
    this.init();
  },
  methods: {
    init(){
      this.deviceCount= undefined;
      this.notAssociateDeviceCount= undefined;
      this.memberCount= undefined;
      this.notArrangeMemberCount= undefined;
      this.orgCount= undefined;
      this.unHandleEventCount= undefined;
      deviceCount().then(response => this.deviceCount = response.data);
      notAssociateDeviceCount().then(response => this.notAssociateDeviceCount = response.data);
      memberCount().then(response => this.memberCount = response.data);
      notArrangeMemberCount().then(response => this.notArrangeMemberCount = response.data);
      orgCount().then(response => this.orgCount = response.data);
      unHandleEventCount().then(response => this.unHandleEventCount = response.data);
    },
    goDevice(){
      this.$router.push({path:'/devices/device'})
    },
    goNotAssosiateDevice(){
      this.$router.push({path:'/devices/device',query: {distributeFlag: 0}})
    },
    goMember(){
      this.$router.push({path:'/member'})
    },
    goOrg(){
      this.$router.push({path:'/org/org'})
    }
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

