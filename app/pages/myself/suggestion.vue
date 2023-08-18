<template>
  <app-body title="意见与建议">
    <!-- <app-logo color="#353535" text="意见与建议"></app-logo> -->
    <view class="ui-form">
      <view class="ui-form-item">
        <u-text text="您的问题与建议" color="#444" size="28rpx">
        </u-text>
        <view class="ui-textarea">
          <u--textarea v-model="content" height="100" placeholder="请输入您的问题或建议"></u--textarea>
        </view>
      </view>
      <figure-code ref="codeRef" :leftText="text"></figure-code>
      <view class="wd-btn-group"><button class="ui-button default" @click="submit">提交</button></view>
    </view>
  </app-body>
</template>

<script>
  import {
    PostAddAdvise,
  } from '@/common/http/api.js';
  export default {
    data() {
      return {
        content: '',
        text: '图形验证码'
      }
    },
    methods: {
      submit() {
        const {
          code: captcha,
          uuid
        } = this.$refs.codeRef.returnCodeData()
        PostAddAdvise({
            content: this.content,
            captcha,
            uuid,
          }).then(res => {
            uni.$u.toast(res.msg)
            setTimeout(() => {
              uni.navigateBack()
            }, 500)
          }),
          () => {
            this.$refs.codeRef.reset();
          }
      }
    }
  }
</script>

<style lang="scss" scoped>
  ::v-deep {
    .u-textarea {
      border: 1px solid #e2e2e2;
    }
  }

  .ui-form {
    margin-top: 100rpx;
    padding: 0 60rpx;
    padding-bottom: 120rpx;

    .ui-input {
      margin: 34rpx 0 60rpx 0;
      border-bottom: 1px solid #e2e2e2;
    }

    .ui-textarea {
      margin: 34rpx 0 60rpx 0;
      // border: 1px solid #e2e2e2;
    }
  }

  .wd-btn-group {
    margin: 120rpx 0;

    .ui-button {
      width: 100%;
      height: 80rpx;
    }
  }
</style>
