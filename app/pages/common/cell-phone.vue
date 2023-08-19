<!-- 修改设备名称 -->
<template>
  <app-body :bg="false" title="设置手机号码">
    <!-- <view class="ui-logo">
			<app-logo color="#353535" text="设置手机号码"></app-logo>
		</view> -->
    <view class="ui-form">
      <u-form>
        <u-form-item label="手机号码" borderBottom labelWidth="100">
          <u-input v-model="devicePhone" maxlength="11" border="none" placeholder="请输入" clearable />
          <image style="width: 50rpx;height: 50rpx;margin-left: 10rpx;" src="../../static/images/dial.png" mode=""
            @tap="phoneDial"></image>
        </u-form-item>
        <u-form-item>
          <u-button class="ui-button default" type="primary" text="提交" @click="handleSumbit"></u-button>
        </u-form-item>
      </u-form>
    </view>
  </app-body>
</template>

<script>
  import {
    mapState,
  } from 'vuex';
  import {
    setDevicePhone
  } from '@/common/http/api.js';
  export default {
    data() {
      return {
        devicePhone: '',
      }
    },
    computed: {
      ...mapState({
        deviceInfo: state => state.deviceInfo
      }, ),
    },
    mounted() {
      console.log(this.deviceInfo, 'info')
      this.devicePhone = this.deviceInfo.devicePhone
    },
    methods: {
      phoneDial() {
        if (this.devicePhone.length !== 11) return
        let phone = this.devicePhone
        // 获取设备平台
        let platform = uni.getSystemInfoSync().platform
        switch (platform) {
          case 'android':
            uni.showActionSheet({
              itemList: [phone, '呼叫'],
              success: function(res) {
                console.log(res);
                if (res.tapIndex === 1) {
                  plus.device.dial(phone, false)
                }
              },
              complete: function(res) {
                console.log("安卓失败", res)
              }
            })
            break;
          case 'ios':
            // 使用uni-app提供的借口
            uni.makePhoneCall({
              phoneNumber: phone

            })
            break;
          default:
            // 调试器工具
        }
      },
      handleSumbit() {
        if (!this.devicePhone) return uni.$u.toast('请输入您要设置的手机号码');
        setDevicePhone({
          devicePhone: this.devicePhone,
          deviceId: this.deviceInfo.deviceId
        }).then(res => {
          uni.$u.toast(res.msg)
          this.$store.dispatch('updateDevacesInfo');
          setTimeout(() => {
            uni.navigateBack()
          }, 1000)
        })
      }
    }
  }
</script>

<style lang="scss" scoped>
  .ui-logo {
    margin-top: 20rpx;
    background: #ffffff;
    padding-bottom: 120rpx;
  }

  .ui-form {
    padding: 30rpx;
    background: #ffffff;

    .ui-button {
      margin-top: 40rpx;
    }
  }
</style>
