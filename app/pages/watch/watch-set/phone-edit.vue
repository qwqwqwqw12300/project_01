<!-- 修改手机号码 -->
<template>
  <app-body :bg="false" title="修改手机号">
    <!-- <view class="ui-logo">
			<app-logo color="#353535" text="修改手机号"></app-logo>
		</view> -->
    <view class="ui-form">
      <view class="ui-form-item">
        <view class="title">
          手机号码
        </view>
        <u-input v-model="simNumber" type="number" maxlength="11" disabledColor="#fff" :border="'none'" fontSize="32rpx"
          clearable />
      </view>
      <view class="ui-btn">
        <button @click="handleSumbit" class="default">确定</button>
      </view>
    </view>
  </app-body>
</template>

<script>
  import {
    mapState,
  } from 'vuex';
  import {
    PostSetSimNumber,
    GetSimNumber
  } from '@/common/http/api';
  import {
    phoneValidator
  } from '@/common/utils/util';
  export default {
    data() {
      return {
        simNumber: '',
      }
    },
    computed: {
      ...mapState({
        deviceInfo: state => state.deviceInfo
      }, )
    },
    mounted() {
      this.handleInit()
    },
    methods: {
      handleInit() {
        GetSimNumber({
          deviceId: this.deviceInfo.deviceId
        }).then(res => {
          this.simNumber = res.data.simNumber
        })
      },
      handleSumbit() {
        if (!phoneValidator(this.simNumber)) {
          return uni.$u.toast('请填写正确的手机号码')
        }
        PostSetSimNumber({
          deviceId: this.deviceInfo.deviceId,
          simNumber: this.simNumber
        }).then(res => {
          uni.$u.toast('修改成功')
          setTimeout(() => {
            uni.navigateBack()
          }, 500)
        })
      },
    }
  }
</script>

<style lang="scss" scoped>
  .ui-logo {
    margin-top: 20rpx;
    background: #ffffff;
    padding-bottom: 120rpx;
    // margin-top: 60rpx;
  }

  .ui-form {
    padding: 0 44rpx;
    padding-bottom: 60rpx;
    background-color: #ffffff;

    .ui-form-item {
      display: flex;
      align-items: center;
      justify-content: space-between;
      min-height: 100rpx;
      border-bottom: solid 2px #f9f9f9;

      .title {
        font-size: 36rpx;
        color: #353535;
        font-weight: 400;
        width: 250rpx;
      }
    }

    .ui-btn {
      margin-top: 80rpx;
    }
  }
</style>
