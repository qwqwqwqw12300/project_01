<template>
  <form-panel title="雷达波的房间设置" :visible.sync="open" width="600px" append-to-body>
    <el-tabs v-if="init" value="first">
      <el-tab-pane label="房间设置" name="first">
        <el-row type="flex" justify="center">
          <el-col :span="10">
            <el-form ref="radarWaveSettingsForm" :model="settings.room" :rules="settingsRoomRules" label-width="80px">
              <el-row type="flex" justify="center">
                <el-col :span="24">
                  <el-form-item label="长度" porp="length">
                    <el-input v-model="settings.room.roomLength" placeholder="请输入长度" clearable >
                      <template slot="append"> 米 </template>
                    </el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row type="flex" justify="center">
                <el-col :span="24">
                  <el-form-item label="左侧" porp="left">
                    <el-input v-model="settings.room.roomLeft" placeholder="请输入左侧距离" clearable>
                      <template slot="append"> 米 </template>
                    </el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row type="flex" justify="center">
                <el-col :span="24">
                  <el-form-item label="右侧" porp="right">
                    <el-input v-model.number="settings.room.roomRight" placeholder="请输入右侧距离" clearable>
                      <template slot="append"> 米 </template>
                    </el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row type="flex" justify="center">
                <el-col :span="24">
                  <el-form-item label="设置高度" porp="height">
                    <el-input v-model="settings.room.roomHeight" placeholder="请设置高度" clearable>
                      <template slot="append"> 米 </template>
                    </el-input>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form>
          </el-col>
          <el-col :span="14"></el-col>
        </el-row>
      </el-tab-pane>
      <el-tab-pane label="子区域设置" name="second">
        <el-row type="flex" justify="center" :gutter="10">
          <el-col :span="6">
            <el-row >
              <el-card header="子区域列表" :body-style="{width:'100%'}">
                <water-fall v-model="settings.roomZones" :column-number="1">
                  <template slot-scope="{item,rowIndex}">
                    <el-card style="margin-bottom: 5px" @click.native="selectZones(rowIndex)">
                      <el-row>
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
                      <el-input v-model="roomZone.y1" clearable></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row type="flex" justify="center">
                  <el-col :span="24">
                    <el-form-item label="最远的点Y2" porp="left" class="redot">
                      <el-input v-model="roomZone.y2" clearable></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row type="flex" justify="center">
                  <el-col :span="24">
                    <el-form-item label="最左侧的点X1" porp="left" class="bluedot">
                      <el-input v-model="roomZone.x1" clearable></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row type="flex" justify="center">
                  <el-col :span="24">
                    <el-form-item label="最右侧的点X2" porp="left" class="redot">
                      <el-input v-model="roomZone.x2" clearable></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row type="flex" justify="center">
                  <el-col :span="24">
                    <el-form-item label="最顶部的点Z1" porp="left" class="bluedot">
                      <el-input v-model="roomZone.z1" clearable></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row type="flex" justify="center">
                  <el-col :span="24">
                    <el-form-item label="最底部的点Z2" porp="left" class="redot">
                      <el-input v-model="roomZone.z2" clearable></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>

              </el-form>
              <el-empty v-if="roomZone == undefined" description=""></el-empty>
            </el-card>
          </el-col>
          <el-col :span="10">
            <el-tabs value="first" type="border-card">
              <el-tab-pane label="左侧区域" name="first">左侧区域</el-tab-pane>
              <el-tab-pane label="中间区域" name="second">中间区域</el-tab-pane>
              <el-tab-pane label="右侧区域" name="third">右侧区域</el-tab-pane>
            </el-tabs>
          </el-col>
        </el-row>
      </el-tab-pane>
    </el-tabs>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="submitForm">提 交</el-button>
      <el-button @click="open = false">取 消</el-button>
    </div>
  </form-panel>
</template>

<script>
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
        z2:undefined
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
</style>
