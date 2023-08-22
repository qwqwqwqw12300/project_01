<!--
* @Author: zhanch
* @Date: 2023-02-11 11:31:28
* @FilePath: /pages/equipment/radar.vue
* @Description: 添加跌倒检测器
-->

<template>
  <app-body title="增加跌倒检测器">
    <!-- <app-logo text="增加跌倒检测器"></app-logo> -->
    <view class="ui-step">
      <text>1、请确保手机蓝牙、WIFI处于开启状态</text>
    </view>
    <view class="ui-satellite">
      <image src="@/static/images/wifi@3x.png" mode="heightFix"></image>
      <image src="@/static/images/blueth@3x.png" mode="heightFix"></image>
    </view>
    <view class="ui-step">
      <text>2、长按智能设备开关5秒,等待蓝灯闪烁后点击下一步</text>
    </view>
    <view class="ui-bluetooth">
      <image src="@/static/images/scan.png" mode="heightFix"></image>
    </view>
    <view class="ui-btn" v-if="connectStatic === 'init'">
      <button class="default" @click="addNext">下一步</button>
    </view>
    <view class="ui-btn" v-if="connectStatic === 'connect'">
      <u-loading-icon :text="eventMsg"></u-loading-icon>
    </view>
  </app-body>
</template>

<script>
  import {
    PostcreDevice,
    PosteditDevice
  } from '../../../common/http/api';
  import {
    getCurPage,
    isApp
  } from '../../../common/utils/util';
  import {
    env
  } from '../../../config/env';

  export default {
    data() {
      return {
        connectStatic: 'init'
      }
    },
    methods: {
      /**
       * 权限检查
       */
      permissionCheck() {
        return new Promise(resolve => {
          resolve(true);
          return;
          console.log('开始验证蓝牙');
          // 验证蓝牙权限
          uni.openBluetoothAdapter();
          console.log('蓝牙模块启动成功');
          uni.getBluetoothAdapterState({
            success(res) {
              //如果res.avaliable==false 说明没打开蓝牙 反之则打开
              console.log(res.available, '蓝牙是否打开');
              if (res.available == false) {
                uni.$u.toast("请先打开手机蓝牙");
                resolve(false)
              } else {
                /*#ifdef APP-PLUS*/
                uni.startWifi({ // 验证wifi权限
                  success: res => resolve(true),
                  fail: err => {
                    uni.$u.toast("请先打开并连接手机wifi");
                    resolve(false);
                  }
                })
                /*#endif*/
              }
            },
            fail: error => {
              uni.$u.toast("请先打开手机蓝牙");
              resolve(false);
            }
          })
        });
      },

      /**
       * 添加下一步
       */
      async addNext() {
        // uni.navigateTo({
        // 	url: '/pages/equipment/add/add-steps'
        // });
        if (await this.permissionCheck()) {
          uni.navigateTo({
            url: '/pages/equipment/add/add-steps'
          });
        }
      }

    }
  }
</script>

<style lang="scss" scoped>
  .ui-step {
    height: 60rpx;
    line-height: 60rpx;
    margin-top: 50rpx;
    padding: 0 30rpx;

    text {
      font-size: 34rpx;
      color: #353535;
      font-weight: 550;
    }
  }

  .ui-satellite {
    margin-top: 40rpx;
    padding: 0 70rpx;
    // width: 100%;
    height: 300rpx;
    display: flex;
    align-items: center;
    justify-content: space-around;

    image {
      // width: 400rpx;
      height: 300rpx;
    }
  }

  .ui-bluetooth {
    margin-top: 40rpx;
    width: 100%;
    height: 300rpx;
    display: flex;
    align-items: center;
    justify-content: center;

    image {
      // width: 380rpx;
      height: 300rpx;
    }
  }



  .ui-btn {
    padding: 0 30rpx !important;
    margin-top: 50rpx;

    text {
      font-size: 30rpx;
      color: #0094ff;
    }

    &>* {
      &:active {
        opacity: 0.8;
      }
    }

    &>*:nth-child(1) {
      margin-bottom: 50rpx;
    }
  }


  .wd-add {
    width: 582rpx;
    min-height: 500rpx;
    border-radius: 20rpx;
    filter: drop-shadow(0 0 5rpx rgba(7, 5, 5, 0.34));
    background-image: linear-gradient(-36deg, #e4e4e4 0%, #f8f8f8 100%);
    padding: 53rpx 31rpx;

    .ui-radio {
      padding-top: 15rpx;
    }

    &>view {
      margin-top: 52rpx;

      &.ui-input {
        margin-top: 40rpx;
        position: relative;
        padding: 10rpx 20rpx;

        &:nth-child(3) {
          margin-top: 20rpx;
        }

        &::after {
          bottom: 10rpx;
          left: 40rpx;
          content: '';
          width: 500rpx;
          height: 1rpx;
          background: #e4e4e4;
          position: absolute;
        }
      }
    }

    .wd-btn-group {
      text-align: center;
      margin-top: 70rpx;

      button {
        width: 237rpx;
        height: 71rpx;
        line-height: 71rpx;
        font-size: 28rpx;
      }
    }
  }
</style>
