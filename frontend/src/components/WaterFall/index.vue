<template>
  <div>
    <el-row :key="'group'+groupIndex" v-for="(group,groupIndex) in rows">
      <el-col :key="'item'+itemIndex" v-for="(item,itemIndex) in group" :span="span">
        <slot v-bind:item="item" v-bind:rowIndex="groupIndex" v-bind:colIndex="itemIndex"></slot>
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
