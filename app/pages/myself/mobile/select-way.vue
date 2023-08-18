<template>
  <app-body :bg="false" title="修改手机号">
    <!-- <view class="ui-logo"> -->
    <!-- <app-logo color="#353535" text="修改手机号"></app-logo> -->
    <!-- </view> -->
    <view class="ui-form">
      <view class="ui-form-item">
        <view class="title">
          当前手机号码
        </view>
        <u-input :value="userInfoPhone" disabled disabledColor="#fff" :border="'none'" fontSize="32rpx" clearable />
      </view>
      <view class="ui-btn">
        <button @click="handleNext" class="default">下一步</button>
      </view>
    </view>
    <u-action-sheet :actions="list" :show="show" title="选择验证方式" round="10" cancelText="取消" @close="show = false"
      @select="handleSelect"></u-action-sheet>
  </app-body>
</template>

<script>
  export default {
    computed: {
      userInfoPhone() {
        return this.$store.getters.userInfo.phone
      }
    },
    data() {
      return {
        show: false,
        list: [{
          url: '/pages/myself/mobile/pre-mobile',
          name: '短信验证'
        }, {
          url: '/pages/myself/mobile/pwd-verify',
          name: '密码验证'
        }],
      }
    },
    methods: {
      handleNext() {
        this.show = true
      },
      handleSelect({
        url
      }) {
        console.log(url, '00000')
        uni.navigateTo({
          url
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
      min-height: 130rpx;
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
