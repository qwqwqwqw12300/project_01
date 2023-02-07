<template>
  <form-panel title="监护设备设置" :visible.sync="open" width="600px" append-to-body>
    <el-tabs v-model="currentTab">
      <el-tab-pane label="设备定位" name="first" >
        <el-row type="flex" justify="center">
            <span v-show="reLocating">获取机器定位中...</span>
            <span v-show="!reLocating">定位完毕</span>
        </el-row>
        <el-row type="flex" justify="center">
            <gao-de-map ref="gaoDeMap" style="height: 400px;width: 400px" v-model="location" :address.sync="address" :reLocating.sync="reLocating"></gao-de-map>
          </el-row>
        <el-row type="flex" justify="center">
          <el-card :body-style="{width:'400px'}">当前地址：{{address}}</el-card>
        </el-row>
        <el-row type="flex" justify="center">
          <el-button type="primary" @click="()=>this.$refs['gaoDeMap'].reLocateCurrentPosition()">立即获取定位</el-button>
        </el-row>
      </el-tab-pane>
      <el-tab-pane label="设置电子围栏" name="second">
        <el-row type="flex" justify="center">
          <gao-de-map v-if="currentTab == 'second'" v-model="location" show-circle="true" :circle-radius="radius" style="height: 400px;width: 400px" ></gao-de-map>
        </el-row>
        <el-row type="flex" justify="center">
          <el-card :body-style="{width:'400px'}">
            <el-row type="flex" justify="center">
              <el-input v-model="address" placeholder="请输入地址"></el-input>
            </el-row>
            <el-row type="flex" justify="center">
              <el-col :span="20"><span>设置半径长度</span></el-col>
              <el-col :span="4"><span>200米</span></el-col>
            </el-row>
            <el-row type="flex" justify="center">
              <el-col>
                <el-slider v-model="radius" style="width:90%" :min="1" :max="200"></el-slider>
              </el-col>
            </el-row>
          </el-card>
        </el-row>
      </el-tab-pane>
      <el-tab-pane label="设置联系电话" name="third">
        设置联系电话
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
    settings: {
      type: Object,
      default(){
        return {
          type:"1"
        }
      }
    },
    visible: {
      type: Boolean,
      default: false
    }
  },
  data(){
    return {
      currentTab:'first',
      reLocating:false,
      address:"",
      location:undefined,
      radius:100
    }
  },
  computed: {
    open: {
      get() {
        return this.visible;
      },
      set(val) {
        this.$emit('update:visible', val)
      }
    }
  },
  methods: {
    submitForm() {

    }
  }
}
</script>

<style scoped>

</style>
