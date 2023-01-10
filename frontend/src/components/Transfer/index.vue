<template>
  <div class="el-transfer">
    <div class="el-transfer-panel">
      <p class="el-transfer-panel__header">{{titles[0]}}</p>
      <div class="el-transfer-panel__body"></div>
    </div>
    <div class="el-transfer__buttons">
      <el-button style="width: 100px"> 左移 </el-button><br/><br/>
      <el-button style="width: 100px">全部左移</el-button><br/><br/>
      <el-button style="width: 100px"> 右移 </el-button><br/><br/>
      <el-button style="width: 100px">全部右移</el-button>
    </div>
    <div class="el-transfer-panel">
      <p class="el-transfer-panel__header">{{titles[1]}}</p>
      <div class="el-transfer-panel__body"></div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Transfer",
  props:{
    titles:{
      type:Array,
      default:["",""]
    },
    height:{
      type:Number,
      default: -1
    }
  }
}
</script>

<style scoped>

</style>
