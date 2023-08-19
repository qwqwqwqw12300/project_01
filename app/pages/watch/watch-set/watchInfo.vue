<!-- 设备信息 -->
<template>
  <app-body :bg="false" :bodyStyle="{backgroundColor:'#FFF'}" title="设备信息">
    <!-- <app-logo text="设备信息"></app-logo> -->
    <view class="ui-scan">
      <view class="scan-box">
        <view class="box-bg">
          <view class="ui-code"><canvas id="qrcode" canvas-id="qrcode"
              :style="{ width: `${size}px`, height: `${size}px` }"></canvas></view>
        </view>
      </view>
    </view>
    <view class="ui-cell">
      <view class="cell-box">
        <u-cell-group>
          <!-- <u-cell @tap="phoneEdit" title="设置手机号码" arrow-direction="right" isLink
						titleStyle="font-size: 15px;color: #303133;">
						<view slot="value" class="u-slot-value">
							{{ phoneDeal(deviceData.simPhone || '') }}
						</view>
					</u-cell> -->
          <u-cell title="IMEI号" titleStyle="font-size: 15px;color: #303133;">
            <view slot="value" class="u-slot-value">
              {{ deviceData.deviceNo }}
            </view>
          </u-cell>
          <u-cell title="固件版本" titleStyle="font-size: 15px;color: #303133;">
            <view slot="value" class="u-slot-value">
              {{ deviceData.firmWareVs }}
            </view>
          </u-cell>
        </u-cell-group>
      </view>
    </view>
    <view class="ui-tips">
      <text>升级路径：</text>
      <text style="margin-top: 15rpx;">
        在手表-主菜单-设置-设备信息页点击【软件更新】按钮，如检测到有新的固件，手表会自动升级，升级前请确保手机电量不低于30%
      </text>
    </view>
  </app-body>
</template>

<script>
  import {
    phoneHide
  } from '@/common/utils/util';
  import UQRCode from '@/uni_modules/Sansnn-uQRCode/js_sdk/uqrcode/uqrcode.js';
  import {
    mapState,
  } from 'vuex';
  import {
    GetWatchInfo
  } from '@/common/http/api';
  export default {
    data() {
      return {
        deviceData: {},
        size: 200,
      }
    },
    computed: {
      ...mapState({
        deviceInfo: state => state.deviceInfo
      }, ),
      phoneDeal() {
        return function(phone) {
          return phoneHide(phone)
        }
      }
    },
    onLoad() {
      this.handleInit()
    },
    onShow() {
      this.handleInit()
    },
    methods: {
      phoneEdit() {
        uni.navigateTo({
          url: `/pages/watch/watch-set/phone-edit`
        })
      },
      handleInit() {
        GetWatchInfo({
          deviceId: this.deviceInfo.deviceId
        }).then(res => {
          this.deviceData = res.data
          this.$nextTick(() => {
            const qr = new UQRCode();
            /* 设置二维码内容 */
            qr.data = this.deviceData.deviceNo;
            /* 设置二维码大小，必须与canvas设置的宽高一致 */
            qr.size = this.size;
            /* 设置二维码前景图 */
            // qr.foregroundImageSrc = '/static/images/tb.png';
            qr.foregroundImagePadding = 4;
            qr.foregroundImageBorderRadius = 4;
            qr.foregroundImageShadowOffsetX = 0;
            qr.foregroundImageShadowOffsetY = 0;
            /* 调用制作二维码方法 */
            qr.make();
            const canvasContext = uni.createCanvasContext('qrcode');
            /* 设置uQRCode实例的canvas上下文 */
            qr.canvasContext = canvasContext;
            /* 调用绘制方法将二维码图案绘制到canvas上 */
            qr.drawCanvas();
          })
        })
      },

    }
  }
</script>

<style lang="scss" scoped>
  ::v-deep {
    .u-cell .u-line {
      margin: 0px !important;
    }
  }

  .ui-scan {
    margin-top: 80rpx;
    padding: 0 32rpx;
    box-sizing: border-box;

    .scan-box {
      width: 360rpx;
      height: 360rpx;
      margin: 0 auto;
      background: #F2F2F2;
      border-radius: 16px;
      box-sizing: border-box;
      padding: 20rpx;

      .box-bg {
        width: 100%;
        height: 100%;
        // background-image: url('@/static/images/QRcode.svg');
        // background-size: 100% 100%;
        display: flex;
        align-items: center;
        justify-content: center;
      }
    }
  }

  .ui-cell {
    margin-top: 64rpx;
    padding: 0 32rpx;

    .cell-box {
      background-color: #fff;
      border-radius: 16rpx;
    }
  }

  .ui-tips {
    width: 686rpx;
    padding: 32rpx 32rpx;
    // margin-top: 32rpx;
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    justify-content: flex-start;
    font-size: 26rpx;
    color: #888888;
    letter-spacing: 0;
    text-align: left;
    line-height: 40rpx;
    font-weight: 400;
  }
</style>
