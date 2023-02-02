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
            <el-card shadow="hover" class="card-item card-item-click" @click.native="goNotActiveDevice()">
              未激活设备
              <br>
              {{ notActiveDeviceCount }}台
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card shadow="hover" class="card-item card-item-click" @click.native="goNotArrangeMember()">
              待分配运营人员的会员
              <br>
              {{ notArrangeMemberCount }}名
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card shadow="hover" class="card-item card-item-click" @click.native="goNotHandleMemberEvent()">
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
  notActiveDeviceCount,
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
      notActiveDeviceCount: undefined,
      memberCount: undefined,
      notArrangeMemberCount: undefined,
      orgCount: undefined,
      unHandleEventCount: undefined,
    };
  },
  created() {
    this.init();
    setInterval(()=>{
      if (this.$route.name == "PlatformAdminHome") {
        this.init()
      }
    }, 5000)
  },
  activated() {
    this.init();
  },
  methods: {
    init(){
      // this.deviceCount= undefined;
      // this.notAssociateDeviceCount= undefined;
      // this.memberCount= undefined;
      // this.notArrangeMemberCount= undefined;
      // this.orgCount= undefined;
      // this.unHandleEventCount= undefined;
      deviceCount().then(response => this.deviceCount = response.data);
      notActiveDeviceCount().then(response => this.notActiveDeviceCount = response.data);
      memberCount().then(response => this.memberCount = response.data);
      notArrangeMemberCount().then(response => this.notArrangeMemberCount = response.data);
      orgCount().then(response => this.orgCount = response.data);
      unHandleEventCount().then(response => this.unHandleEventCount = response.data);
    },
    goDevice(){
      this.$router.push({path:'/devices/device'})
    },
    goNotActiveDevice(){
      this.$router.push({path:'/devices/device',query: {status: '0'}})
    },
    goNotAssosiateDevice(){
      this.$router.push({path:'/devices/device',query: {distributeFlag: 0}})
    },
    goMember(){
      this.$router.push({path:'/member'})
    },
    goOrg(){
      this.$router.push({path:'/org/org'})
    },
    //未处理消息
    goUnHandleEvent(){
      this.$router.push({path:'/members/deviceEvents',query: {operateFlag: 0}})
    },
    //待分配运营的会员
    goNotArrangeMember(){
      this.$router.push({path:'/members/memberArrange'})
    },
    //未处理的会员事件
    goNotHandleMemberEvent(){
      this.$router.push({path:'/members/unHandleEvents',query: {operateFlag: 0}})
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

