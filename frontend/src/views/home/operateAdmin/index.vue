<template>
  <div class="app-container home">
    <el-row>
      <el-card>
        <div slot="header" class="clearfix">
          <span class="el-dialog__title">紧急处理设备</span>
        </div>
        <water-fall :value="urgentDevices">
          <template slot-scope="{item}">
            <el-card :style="{background: item.onlineFlag=='0'?'#ff9999':'#e1f3fb'}" shadow="hover" class="card-item card-item-click" @click.native="goDeviceEvent(item.deviceId,item.orgId,item.memberId)">
              <span :style="{color:item.urgent?'#ff0000':'none'}">{{ item.name }}</span> | {{getEventSum(item)}}条
              <br>
              {{ item.memberId==undefined?item.deviceGroupName:item.familyName }} | {{ item.memberId==undefined?item.location:item.roomName }} |
              <span :style="{color:item.onlineFlag=='0'?'#ff0000':'none'}">{{item.onlineFlag=='1'?"在线":"不在线"}}</span>
              <br>
              {{ item.memberId==undefined?"机构业务":("会员账号："+item.memberPhone) }}
            </el-card>
          </template>
        </water-fall>
      </el-card>
    </el-row>
    <el-row>
      <el-card>
        <div slot="header" class="clearfix">
          <span class="el-dialog__title">运营设备</span>
        </div>
        <water-fall :value="deviceList">
          <template slot-scope="{item}">
            <el-card :style="{background: item.onlineFlag=='0'?'#ff9999':'#e1f3fb'}" shadow="hover" class="card-item card-item-click" @click.native="goDeviceEvent(item.deviceId,item.orgId,item.memberId)">
              <span :style="{color:item.urgent?'#ff0000':'none'}">{{ item.name }}</span> | {{getEventSum(item)}}条
              <br>
              {{ item.memberId==undefined?item.deviceGroupName:item.familyName }} | {{ item.memberId==undefined?item.location:item.roomName }} |
              <span :style="{color:item.onlineFlag=='0'?'#ff0000':'none'}">{{item.onlineFlag=='1'?"在线":"不在线"}}</span>
              <br>
              {{ item.memberId==undefined?"机构业务":("会员账号："+item.memberPhone) }}
            </el-card>
          </template>
        </water-fall>
      </el-card>
    </el-row>
  </div>
</template>

<script>
import {careDeviceList,countUnHandleByDeviceGroupByLevel} from '@/api/home/bizHome'

export default {
  name: "OperateAdmin",
  data() {
    return {
      deviceList:[],
      urgentLevel:"urgent",
      countUnHandleByDeviceGroupByLevel:{}
    };
  },
  created(){
    this.getList();
    setInterval(()=>{
      if (this.$route.name == "OperateAdminHome") {
        this.getList()
      }
    }, 5000)
  },
  computed:{
    urgentDevices(){
      return this.deviceList.filter(x=>x.urgent||x.onlineFlag == '0')
    },
    notUrgentDevices(){
      return this.deviceList.filter(x=>!(x.urgent||x.onlineFlag == '0'))
    },
  },
  methods: {
    goTarget(href) {
      window.open(href, "_blank");
    },
    getList(){
      careDeviceList().then(response=>{
        this.deviceList = response.data.map(x=>{
          x.countUnHandleByDeviceGroupByLevel = this.countUnHandleByDeviceGroupByLevel[x.deviceId]
          x.urgent = x.countUnHandleByDeviceGroupByLevel?.find(p=>(p.level==this.urgentLevel&&p.count>0))!=undefined;
          countUnHandleByDeviceGroupByLevel(x.deviceId)
            .then(countResponse=>{
              this.countUnHandleByDeviceGroupByLevel[x.deviceId] = countResponse.data;
              x.countUnHandleByDeviceGroupByLevel=this.countUnHandleByDeviceGroupByLevel[x.deviceId];
              x.urgent = x.countUnHandleByDeviceGroupByLevel?.find(p=>(p.level==this.urgentLevel&&p.count>0))!=undefined;
            })
          return x;
        });

      })
    },
    getEventSum(device){
      return device.countUnHandleByDeviceGroupByLevel?.map(x=>x.count).reduce((val, oldVal) => val + oldVal,0)
    },
    //机构处理单个设备消息/会员单个设备消息
    goDeviceEvent(deviceId,orgId,memberId){
      //this.$router.push({path:'/org/orgSingleDeviceEvent',query: {deviceId:deviceId, orgId:orgId }})
      memberId==undefined?this.$router.push({path:'/org/orgSingleDeviceEvent',query: {deviceId:deviceId, orgId:orgId,operateFlag:0 }})
      :this.$router.push({path:'/members/singleDeviceRB',query: {deviceId: deviceId, memberId: memberId,operateFlag:0}})

     }
  },
};
</script>

<style scoped lang="scss">
.home {
  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }
  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }
  .col-item {
    margin-bottom: 20px;
  }

  ul {
    padding: 0;
    margin: 0;
  }

  font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    list-style-type: none;
  }

  h4 {
    margin-top: 0px;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }

  .update-log {
    ol {
      display: block;
      list-style-type: decimal;
      margin-block-start: 1em;
      margin-block-end: 1em;
      margin-inline-start: 0;
      margin-inline-end: 0;
      padding-inline-start: 40px;
    }
  }
}
</style>

<style scoped lang="scss">

.el-row {
  margin-bottom: 20px;

  &
  :last-child {
    margin-bottom: 0;
  }
}
</style>
