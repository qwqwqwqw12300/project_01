<template>
  <form-panel title="雷达波的房间设置" :visible.sync="open" width="600px" append-to-body>
    <el-tabs v-if="init" v-model="currentTab">
      <el-tab-pane label="房间设置" name="first">
        <el-row type="flex" justify="center" :gutter="10">
          <el-col :span="10">
            <el-form ref="radarWaveSettingsForm" :model="settings.room" :rules="settingsRoomRules" label-width="140px">
              <el-row type="flex" justify="center">
                <el-col :span="24">
                  <el-form-item label="长度" porp="length">
                    <el-input-number :controls="false" :precision="2"  v-model="settings.room.roomLength" placeholder="请输入长度" clearable >
                      <template slot="append"> 米 </template>
                    </el-input-number> 米
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row type="flex" justify="center">
                <el-col :span="24">
                  <el-form-item label="左侧" porp="left">
                    <el-input-number :controls="false" :precision="2"  v-model="settings.room.roomLeft" placeholder="请输入左侧距离" clearable>
                      <template slot="append"> 米 </template>
                    </el-input-number> 米
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row type="flex" justify="center">
                <el-col :span="24">
                  <el-form-item label="右侧" porp="right">
                    <el-input-number :controls="false" :precision="2"  v-model="settings.room.roomRight" placeholder="请输入右侧距离" clearable>
                      <template slot="append"> 米 </template>
                    </el-input-number> 米
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row type="flex" justify="center">
                <el-col :span="24">
                  <el-form-item label="设置高度" porp="height">
                    <el-input-number :controls="false" :precision="2"  v-model="settings.room.roomHeight" placeholder="请设置高度" clearable>
                      <template slot="append"> 米 </template>
                    </el-input-number> 米
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form>
            <el-row type="flex" justify="center" :gutter="10">
              <el-col :span="6"><el-button type="primary" @click="submitForm">提 交</el-button></el-col>
              <el-col :span="6"><el-button @click="open = false">取 消</el-button></el-col>
            </el-row>
          </el-col>
          <el-col :span="14">
            <el-card>
              <div class="explanation-text-container">
                <span class="explanation-text" style="padding-bottom: 5px;">说明</span>
                <span class="explanation-text">在设备前面的墙上标记一个点，然后测量设备到该点之间的距离。 然后测量该点到房间左侧的距离与到房间右侧的距离</span>
              </div>
              <div style="margin-top: 6.5em;display:flex;justify-content: center; align-items:center;">
                <svg xmlns="http://www.w3.org/2000/svg" width="504" height="362"
                     viewBox="0 0 504 362">
                  <defs >
<!--                    <style > @font-face {-->
<!--                      font-family: "Metropolis-SemiBold";-->
<!--                      src: url(./fonts/Metropolis-SemiBold.woff);-->
<!--                    } </style>-->
                  </defs>
                  <g fill="none" fill-rule="evenodd">
                    <path  fill="#FFF" d="M0.5 0.5H502.5V502.5H0.5z"
                          transform="translate(.522)"></path>
                    <g  fill="#F9AD28">
                      <path
                            d="M271 0c0 11.046-8.954 20-20 20s-20-8.954-20-20M20 0c0 11.046-8.954 20-20 20V0h20zM483 0c0 11.046 8.954 20 20 20V0h-20z"
                            transform="translate(1)"></path>
                    </g>
                    <g >
                      <path  stroke="#CCC" stroke-linecap="round" stroke-linejoin="round"
                            stroke-width="2" d="M33.45 310L33.537 1" transform="translate(218.463 25)"></path>
                      <path  stroke="#CCC" stroke-linecap="round" stroke-linejoin="round"
                            stroke-width="2" d="M0 6L5 0 10 6"
                            transform="translate(218.463 -125) matrix(1 0 0 -1 28.537 460)"></path>
                      <g  transform="translate(218.463 25) translate(3 315)">
                        <rect  width="64" height="22" fill="#3975CE" rx="11"></rect>
                        <text  fill="#FFF" font-family="SFProText-Semibold, SF Pro Text"
                              font-size="12" font-weight="500" letter-spacing="-.36">
                          <tspan  x="10.359" y="15" id="device-text">设备</tspan>
                        </text>
                      </g>
                      <path  stroke="#CCC" stroke-linecap="round" stroke-linejoin="round"
                            stroke-width="2" d="M0 6L5 0 10 6"
                            transform="translate(218.463 25) matrix(-1 0 0 1 38.537 0)"></path>
                      <g  transform="translate(218.463 -30) translate(0 219)">
                        <rect  width="68" height="22" fill="#CCC" rx="11"></rect>
                        <text  fill="#FFF" font-family="Metropolis-SemiBold, Metropolis"
                              font-size="12" font-weight="500" letter-spacing="-.36">
                          <tspan  x="10.359" y="15" id="length-text">长度</tspan>
                        </text>
                      </g>
                    </g>
                    <g >
                      <g  stroke="#CCC" stroke-linecap="round" stroke-linejoin="round"
                         stroke-width="2">
                        <path  d="M5.765 196.417L5.765 2.417"
                              transform="translate(25.428) rotate(90 98.377 101.612)"></path>
                        <path  d="M0 6L5 0 10 6"
                              transform="translate(25.428) rotate(90 98.377 101.612) matrix(1 0 0 -1 .765 198.417)"></path>
                        <path  d="M0 6L5 0 10 6"
                              transform="translate(25.428) rotate(90 98.377 101.612) translate(.765 .417)"></path>
                      </g>
                      <g  transform="translate(25.428) translate(78.572)">
                        <rect  width="48" height="22" fill="#CCC" rx="11"></rect>
                        <text  fill="#FFF" font-family="Metropolis-SemiBold, Metropolis"
                              font-size="12" font-weight="500" letter-spacing="-.36">
                          <tspan  id="left-text" x="10.104" y="15">左侧</tspan>
                        </text>
                      </g>
                    </g>
                    <g >
                      <g  stroke="#CCC" stroke-linecap="round" stroke-linejoin="round"
                         stroke-width="2">
                        <path  d="M5.765 196.417L5.765 2.417"
                              transform="translate(278.428) rotate(90 98.377 101.612)"></path>
                        <path  d="M0 6L5 0 10 6"
                              transform="translate(278.428) rotate(90 98.377 101.612) matrix(1 0 0 -1 .765 198.417)"></path>
                        <path  d="M0 6L5 0 10 6"
                              transform="translate(278.428) rotate(90 98.377 101.612) translate(.765 .417)"></path>
                      </g>
                      <g  transform="translate(278.428) translate(74.072)">
                        <rect  width="57" height="22" fill="#CCC" rx="11"></rect>
                        <text  fill="#FFF" font-family="Metropolis-SemiBold, Metropolis"
                              font-size="12" font-weight="500" letter-spacing="-.36">
                          <tspan  id="right-text" x="10.483" y="15">右侧</tspan>
                        </text>
                      </g>
                    </g>
                  </g>
                </svg>
              </div>
            </el-card>
            </el-col>
        </el-row>
      </el-tab-pane>
      <el-tab-pane label="子区域设置" name="second">
        <el-row type="flex" justify="center" :gutter="10">
          <el-col :span="6">
            <el-row >
              <el-card header="子区域列表" :body-style="{width:'100%'}">
                <water-fall v-model="settings.roomZones" :column-number="1">
                  <template slot-scope="{item,rowIndex}">
                    <el-card :shadow="(index==rowIndex)?'always':'never'" style="margin-bottom: 5px" @click.native="selectZones(rowIndex)">
                      <el-row :gutter="10">
                        <el-col :span="16">
                          <el-input v-model="item.name" placeholder="子区域名称" clearable></el-input>
                        </el-col>
                        <el-col :span="8" >
                          <el-row type="flex" justify="center">
                            <el-button type="primary" @click="removeZones(rowIndex)" @click.stop.native>移除</el-button>
                          </el-row>
                        </el-col>
                      </el-row>
                    </el-card>
                  </template>
                </water-fall>
              </el-card>
            </el-row>
            <el-row type="flex" justify="center" style="margin-top: 10px">
              <el-button type="primary" @click="addRoomZones"> + 加子区域</el-button>
            </el-row>
          </el-col>
          <el-col :span="8">
            <el-card>
              <el-form v-if="roomZone!=undefined" ref="roomZoneSettingsForm" :model="roomZone" :rules="settingsRoomZoneRules" label-width="100px">
                <el-row type="flex" justify="center">
                  <el-col :span="24">
                    <el-form-item label="子区域名称" porp="name" >
                      <el-input v-model="roomZone.name" placeholder="请输入子区域名称" clearable >
                      </el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  测量从设备为起点到这些点的距离
                </el-row>
                <el-row type="flex" justify="center">
                  <el-col :span="24">
                    <el-form-item label="最近的点Y1" porp="left" class="bluedot">
                      <el-input-number :controls="false" :precision="2"  v-model="roomZone.y1" clearable></el-input-number> 米
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row type="flex" justify="center">
                  <el-col :span="24">
                    <el-form-item label="最远的点Y2" porp="left" class="redot">
                      <el-input-number :controls="false" :precision="2"  v-model="roomZone.y2" clearable></el-input-number> 米
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row type="flex" justify="center">
                  <el-col :span="24">
                    <el-form-item label="最左侧的点X1" porp="left" class="bluedot">
                      <el-input-number :controls="false" :precision="2"  v-model="roomZone.x1" clearable></el-input-number> 米
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row type="flex" justify="center">
                  <el-col :span="24">
                    <el-form-item label="最右侧的点X2" porp="left" class="redot">
                      <el-input-number :controls="false" :precision="2"  v-model="roomZone.x2" clearable></el-input-number> 米
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row type="flex" justify="center">
                  <el-col :span="24">
                    <el-form-item label="最顶部的点Z1" porp="left" class="bluedot">
                      <el-input-number :controls="false" :precision="2"  v-model="roomZone.z1" clearable></el-input-number> 米
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row type="flex" justify="center">
                  <el-col :span="24">
                    <el-form-item label="最底部的点Z2" porp="left" class="redot">
                      <el-input-number :controls="false" :precision="2"  v-model="roomZone.z2" clearable></el-input-number> 米
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row type="flex" justify="center">
                  <el-col :span="24">
                    <el-form-item label="进出监控" porp="existFlag" >
                      <el-checkbox v-model="roomZone.existFlag" true-label="1" false-label="0"></el-checkbox>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row type="flex" justify="center">
                  <el-col :span="24">
                    <el-form-item label="跌倒监控" porp="fallFlag" >
                      <el-checkbox v-model="roomZone.fallFlag" true-label="1" false-label="0" clearable></el-checkbox>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row type="flex" justify="center">
                  <el-col :span="24">
                    <el-form-item label="开始监控时间" porp="startTime" >
                      <el-date-picker
                        v-model="roomZone.startTime"
                        type="datetime"
                        clearable
                        value-rmat="timestamp"
                        placeholder="选择开始监控时间">
                      </el-date-picker>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row type="flex" justify="center">
                  <el-col :span="24">
                    <el-form-item label="结束监控时间" porp="endTime" >
                      <el-date-picker
                        v-model="roomZone.endTime"
                        type="datetime"
                        clearable
                        value-rmat="timestamp"
                        placeholder="选择结束监控时间">
                      </el-date-picker>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row type="flex" justify="center">
                  <el-col :span="24">
                    <el-form-item label="进入监控区域超时报警时间" porp="inMonitorFlag" >
                      <el-row>
                        <el-col :span="4">
                          <el-checkbox v-model="roomZone.inMonitorFlag" true-label="0" false-label="1" clearable></el-checkbox>
                        </el-col>
                        <el-col :span="20">
                          <el-select v-model="roomZone.entryTime">
                            <el-option label="5分钟" :value="5*60"></el-option>
                            <el-option label="10分钟" :value="10*60"></el-option>
                            <el-option label="15分钟" :value="15*60"></el-option>
                          </el-select>
                        </el-col>
                      </el-row>
                      </el-form-item>
                    </el-col>
                </el-row>
                <el-row type="flex" justify="center">
                  <el-col :span="24">
                    <el-form-item label="离开监控区域超时报警时间" porp="outMonitorFlag" >
                      <el-row>
                        <el-col :span="4">
                          <el-checkbox v-model="roomZone.outMonitorFlag" true-label="0" false-label="1" clearable></el-checkbox>
                        </el-col>
                        <el-col :span="20">
                          <el-select v-model="roomZone.departureTime">
                            <el-option label="5分钟" :value="5*60"></el-option>
                            <el-option label="10分钟" :value="10*60"></el-option>
                            <el-option label="15分钟" :value="15*60"></el-option>
                          </el-select>
                        </el-col>
                      </el-row>
                      </el-form-item>
                    </el-col>
                </el-row>

              </el-form>
              <el-empty v-if="roomZone == undefined" description=""></el-empty>
            </el-card>
          </el-col>
          <el-col :span="10">
            <el-tabs value="first" type="border-card">
              <el-tab-pane label="左侧区域" name="first">
                <div cdkscrollable="" style="transform: none;"><div class="explanation-text-container ng-star-inserted" style=""><span class="explanation-title" style="padding-bottom: 5px;">测量左侧区域</span><span class="explanation-text">X1和X2都位于设备的左侧，因此为负数. X1离设备更远，因此小于X2. Y1小于Y2，因为它离设备较近.</span></div><object width="450" :data="scanAreaLeft" type="image/svg+xml" style="margin-top: 4.5em;" class="ng-star-inserted"></object><!----></div>
              </el-tab-pane>
              <el-tab-pane label="中间区域" name="second">
                <div cdkscrollable="" style="transform: none;"><div class="explanation-text-container ng-star-inserted" style=""><span class="explanation-title" style="padding-bottom: 5px;">测量中间区域</span><span class="explanation-text">X1在设备的左侧，因此为负数. X2在设备的右侧，因此为正数. Y1小于Y2，因为它离设备更近.</span></div><object width="450" :data="scanAreaCenter" type="image/svg+xml" style="margin-top: 4.5em;" class="ng-star-inserted"></object><!----></div>
              </el-tab-pane>
              <el-tab-pane label="右侧区域" name="third">
                <div cdkscrollable="" style="transform: none;"><div class="explanation-text-container ng-star-inserted" style=""><span class="explanation-title" style="padding-bottom: 5px;">测量右侧区域</span><span class="explanation-text">X1和X2都在设备的右边，因此为正. X2离设备更远，因此小于X1。Y1小于Y2，因为它离设备较近</span></div><object width="450" :data="scanAreaRight" type="image/svg+xml" style="margin-top: 4.5em;" class="ng-star-inserted"></object><!----></div>
              </el-tab-pane>
            </el-tabs>
          </el-col>
        </el-row>
      </el-tab-pane>
    </el-tabs>
    <div v-if="currentTab!='first'" slot="footer" class="dialog-footer">
      <el-button type="primary" @click="submitForm">提 交</el-button>
      <el-button @click="open = false">取 消</el-button>
    </div>
  </form-panel>
</template>

<script>
import scanAreaLeft from '@/assets/images/scan-area-left.svg'
import scanAreaCenter from '@/assets/images/scan-area-center.svg'
import scanAreaRight from '@/assets/images/scan-area-right.svg'
export default {
  name: "RadarWaveSettings",
  props: {
    value: {
      type: Object,
      default(){
        return {
          type:"0",
          room:{
            roomLength:undefined,
            roomLeft:undefined,
            roomRight:undefined,
            roomHeight:undefined
          },
          roomZones:[]
        }
      }
    },
    visible: {
      type: Boolean,
      default: false
    }
  },
  watch:{
    "visible":{
      immediate: true,
      handler: function (val) {
        if(val == true){
          if(this.settings.type == undefined){
            this.settings.type = "0"
          }
          if(this.settings.room == undefined){
            this.settings.room = {
              roomLength:undefined,
              roomLeft:undefined,
              roomRight:undefined,
              roomHeight:undefined
            }
          }
          if(this.settings.roomZones == undefined){
            this.settings.roomZones = []
          }
          this.$emit('input', this.settings)
          this.init = true
        }
      }
    }
  },
  data(){
    return {
      currentTab:"first",
      scanAreaLeft,
      scanAreaCenter,
      scanAreaRight,
      init:false,
      position:"first",
      settingsRoomRules:{},
      settingsRoomZoneRules:{},
      index:undefined
    }
  },
  computed: {
    open: {
      get() {
        return this.visible;
      },
      set(val) {
        console.log(">>>>>")
        this.$emit('update:visible', val)
      }
    },
    settings:{
      get(){
        return this.value
      },
      set(val){
        console.log(">>>>>")
        this.$emit('input', val)
      }
    },
    roomZone:{
      get(){
        return this.index == undefined?undefined:this.settings.roomZones[this.index]
      },
      set(val){
        if(this.index != undefined&&this.settings.roomZones[this.index]!=undefined){
          this.settings.roomZones.splice(this.index,1,val);
        }
      }
    }
  },
  methods: {
    submitForm() {
      this.$emit('submit', this.settings)
    },
    addRoomZones(){
      this.settings.roomZones.push({
        name:undefined,
        x1:undefined,
        x2:undefined,
        y1:undefined,
        y2:undefined,
        z1:undefined,
        z2:undefined,
        existFlag:"0",
        fallFlag:"0",
        entryTime:undefined,
        departureTime:undefined,
        inMonitorFlag:"0",
        outMonitorFlag:"0",
        startTime:undefined,
        endTime:undefined
      });
      this.index = this.settings.roomZones.length-1
    },
    removeZones(index){
      this.settings.roomZones.splice(index,1);
    },
    selectZones(index){
      this.index = index;
    }
  }
}
</script>
<style scoped>
.redot::before{
  display:block;
  background:#f00;
  border-radius:50%;
  width:7px;
  height:7px;
  top:15px;
  left:-9px;
  position:absolute;
}
.bluedot::before{
  display:block;
  background:#00f;
  border-radius:50%;
  width:7px;
  height:7px;
  top:15px;
  left:-9px;
  position:absolute;
}
.explanation-text-container {
  position: absolute;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.explanation-text, .explanation-title {
  font-family: Metropolis;
  font-size: 11px;
  font-weight: 500;
  font-style: normal;
  color: #4a5466;
  line-height: 20px;
  text-align: center;
  padding: 0 2em;
}
</style>
