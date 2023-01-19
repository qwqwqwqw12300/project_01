<template>
  <form-panel title="监护设备设置" :visible.sync="open" width="600px" append-to-body>
    <el-tabs value="first">
      <el-tab-pane label="设备定位" name="first">
        设备定位
      </el-tab-pane>
      <el-tab-pane label="设置电子围栏" name="second">
        设置电子围栏
      </el-tab-pane>
      <el-tab-pane label="设置联系电话" name="second">
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
        return {}
      }
    },
    visible: {
      type: Boolean,
      default: false
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
