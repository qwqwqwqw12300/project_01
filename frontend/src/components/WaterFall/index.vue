<template>
  <div>
    <el-row v-for="group in rows">
      <el-col v-for="item in group" :span="span">
        <slot v-bind:item="item"></slot>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: "WaterFall",
  props: {
    value: {
      type: Array,
      default: []
    },
    columnNumber: {
      type: Number,
      default: 4
    }
  },
  computed: {
    span(){
      return 24/this.columnNumber;
    },
    rows() {
      let result = [];
      let group = [];
      result.push(group);
      for (let index = 1; index <= this.value?.length || 0; index++) {
        group.push(this.value[index-1])
        if(index%this.columnNumber == 0){
          group = [];
          result.push(group);
        }
      }
      return result;
    }
  }
}
</script>

<style lang="scss" scoped>

</style>
