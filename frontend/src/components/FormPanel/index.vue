<template>
  <div v-if="visible" class="mask_layer el-dialog is-fullscreen" @click.stop>
    <div class="el-dialog__header">
      <slot name="header">
        <span class="el-dialog__title mask_header_title">{{ title }}</span>
        <el-button class="" plain @click="back" style="float: right">返回</el-button>
      </slot>
    </div>
    <div class="el-dialog__body">
      <slot></slot>
    </div>
    <div class="el-dialog__footer mask_foot_layer">
      <slot name="footer"></slot>
    </div>
  </div>
</template>

<script>
export default {
  name: "FormPanel",
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    title: {
      type: String,
      default: ''
    }
  },
  // watch:{
  //   "$route.name":function(){
  //     if(this.visible){
  //       this.$emit('update:visible', false)
  //     }
  //   }
  // },
  methods: {
    back() {
      this.$emit('update:visible', false)
    }
  }
}
</script>

<style scoped>
.mask_layer {
  position: absolute;
  left: 0;
  top: 0;
  background: rgba(255, 255, 255, 1);
  width: 100%; /*宽度设置为100%，这样才能使隐藏背景层覆盖原页面*/
  height: 100%;
  z-Index: 1000;
}

.mask_foot_layer{
  text-align: center;
}
.mask_header_title{
  border-width: 0px 0px 0px 4px;
  border-style: solid;
  border-color: rgb(0, 121, 254);
  border-top: 0px;
  border-right: 0px;
  border-bottom: 0px;
  padding-left: 4px;
}
</style>
