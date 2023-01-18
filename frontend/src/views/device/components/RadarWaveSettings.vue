<template>
  <form-panel title="雷达波的房间设置" :visible.sync="open" width="600px" append-to-body>
    <el-tabs value="first">
      <el-tab-pane label="房间设置" name="first">
        房间设置22
      </el-tab-pane>
      <el-tab-pane label="子区域设置" name="second">
        子区域设置11
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
        return {}
      }
    },
    visible: {
      type: Boolean,
      default: false
    }
  },
  data(){
    return {
      position:"first"
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
