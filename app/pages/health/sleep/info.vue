<template>
  <app-body :bg="false" :title="text">
    <!-- <app-logo :text="text" :isShare="false"></app-logo> -->
    <view v-for="(item,index) in list" :key="index" class="ui-window">
      <view class="ui-window-title">{{item.title}}</view>
      <view>{{item.content}}</view>
    </view>
  </app-body>
</template>

<script>
  import {
    clearHeaded, //清醒
    lightSleep, //浅睡
    deepSleep, //深睡
    nocturnalSleep, //夜睡
    heartRate, //心率
    bloodPressure, //血压
    bloodOxygen, //血氧
    bodyTemperature, //体温
  } from '@/config/db';
  export default {
    data() {
      const textObj = {
        '清醒': clearHeaded,
        '浅睡': lightSleep,
        '深睡': deepSleep,
        '夜间睡眠': nocturnalSleep,
        '心率': heartRate,
        '血压': bloodPressure,
        '血氧': bloodOxygen,
        '体温': bodyTemperature
      }
      return {
        text: '',
        list: [],
        textObj,
      }
    },
    created() {

    },
    methods: {


    },
    onLoad(option) {
      this.text = option.name
      this.list = this.textObj[option.name]
    }
  }
</script>

<style lang="scss" scoped>
  .ui-window {
    width: 90%;
    margin: 0 auto;
    margin-top: 30rpx !important;

    .ui-window-title {
      margin-bottom: 20rpx;
      font-weight: bold;
      font-size: 32rpx;
    }
  }
</style>
