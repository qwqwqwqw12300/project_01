<!-- 增加监护设备 -->
<template>
  <app-body :bg="false" :bodyStyle="{backgroundColor:'#FFF'}" :title="title">
    <!-- <app-logo color="#353535" :text="title"></app-logo> -->
    <view class="ui-scan">
      <view class="scan-box">
        <view class="box-bg" @click="handleScan">
          <image class="img" src="@/static/images/camera.png"></image>
        </view>
      </view>
      <view class="scan-tip">
        <text>请扫描设备上的条形码或在下方手动输入设备IMEI码进行绑定</text>
      </view>
    </view>
    <view class="ui-code">
      <view class="code-box">
        <text class="label">设备IMEI码</text>
        <view class="input">
          <u--input v-model="cardInfo.deviceNo" type="number" placeholder="请输入设备IMEI码" border="none" clearable>
          </u--input>
        </view>
      </view>
    </view>
    <view class="ui-code">
      <view class="code-box">
        <text class="label">设备名称</text>
        <view class="input">
          <u--input v-model="cardInfo.deviceName" maxlength="6" placeholder="请输入设备名称" border="none" clearable>
          </u--input>
        </view>
      </view>
    </view>
    <view class="ui-btn">
      <button @click="handleSubmit" class="default">立即绑定</button>
    </view>
  </app-body>
</template>

<script>
  import {
    PostcreDevice
  } from '@/common/http/api';
  export default {
    data() {
      return {
        cardInfo: {
          deviceNo: '',
          deviceName: '',
          deviceType: ""
        },
        title: '',
        cardType: ''
      }
    },
    onLoad(options) {
      console.log(options, 'options')
      this.cardType = options.type
      this.title = options.type === 'card' ? '绑定电子牵挂卡' : 'SOS设备'
    },
    methods: {
      handleScan() {
        uni.scanCode({
          // scanType: ['barCode', 'qrCode'],
          success: res => {
            const {
              scanType,
              result
            } = res
            this.cardInfo.deviceNo = result
          },
          false: res => {
            console.log(res, '00000000000000000000000')
          }
        });
      },
      handleSubmit() {
        const {
          deviceNo,
          deviceName
        } = this.cardInfo
        this.cardInfo.deviceType = this.cardType === 'card' ? '1' : '3'
        if (!deviceNo) return uni.$u.toast('设备码不能为空')
        if (!deviceName) return uni.$u.toast('设备名称不能为空')
        PostcreDevice({
          ...this.cardInfo
        }).then(res => {
          uni.$u.toast(res.msg)
          setTimeout(() => {
            uni.navigateBack()
          }, 1000)
        })

      }
    }
  }
</script>

<style lang="scss" scoped>
  .ui-scan {
    margin-top: 80rpx;
    padding: 0 32rpx;
    box-sizing: border-box;

    .scan-box {
      height: 200rpx;
      background: #F2F2F2;
      border-radius: 16px;
      box-sizing: border-box;
      padding: 28rpx 63rpx;

      .box-bg {
        width: 100%;
        height: 100%;
        background-image: url('@/static/images/barCode.png');
        background-size: 100% 100%;
        display: flex;
        align-items: center;
        justify-content: center;

        .img {
          width: 100rpx;
          height: 100rpx;
        }
      }
    }

    .scan-tip {
      margin-top: 26rpx;
      text-align: center;

      text {
        font-size: 24rpx;
        color: #888888;
      }
    }
  }

  .ui-code {
    margin-top: 30rpx;
    padding: 0 32rpx;

    .code-box {
      height: 128rpx;
      display: flex;
      align-items: center;
      justify-content: space-between;
      border-bottom: solid 2px #f7f7f7;

      .label {
        font-size: 34rpx;
        color: #353535;
      }

      .input {
        width: 300rpx;
      }
    }
  }

  .ui-btn {
    padding: 0 32rpx;
    margin-top: 100rpx;
  }
</style>
