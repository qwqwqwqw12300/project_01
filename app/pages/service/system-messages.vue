<!--
* @Author: zhanch
* @Date: 2023-01-05 10:58:39
* @FilePath: /pages/service/system-messages.vue
* @Description: 系统服务
-->

<template>
  <app-body :bg="false" :isScroll="false">
    <view id="system">
      <scroll-view class="ui-list" @refresherrefresh="onRefresh" refresher-enabled scroll-y="true"
        :refresher-triggered="triggered" refresher-background="transparent">
        <view class="ui-item" v-for="item in messageList" :key="item.noticeId">
          <view class="ui-date">
            {{ item.createTime }}
            <!-- 2022年10月11日 11：22 -->
          </view>
          <view class="ui-box active" @click="details(item)">
            <u-badge :isDot="item.readFlag !== '1'" absolute :offset="[-3, 3]"></u-badge>
            <text>{{item.noticeTitle}}</text>
            <u-text size="25rpx" color="#666" :lines="3" :text="item.noticeContent"></u-text>
            <view class="ui-detail">
              <text>立即查看</text>
              <u-icon name="arrow-right" size="40rpx"></u-icon>
            </view>
          </view>
        </view>
      </scroll-view>
    </view>
    <u-popup :closeable="true" zIndex="99999" :round="10" :show="showVisible" mode="center"
      @close="showVisible = false">
      <view class="wd-content">
        <scroll-view scroll-y class="uni-scroll">
          <view v-html="readInfo"></view>
        </scroll-view>
        <button @click="showVisible = false" class="default">我知道了</button>

      </view>
    </u-popup>
    <!-- <u-modal @confirm="onConfirm" :show="showVisible" title="消息详情" :content='readInfo'></u-modal> -->
  </app-body>
</template>

<script>
  import {
    GetSysNotice,
    postSetNoticeFlag,
  } from '@/common/http/api.js';
  import {
    mapState
  } from 'vuex';
  export default {
    data() {
      return {
        messageList: [],
        showVisible: false,
        triggered: false,
      }
    },
    computed: {
      ...mapState({
        readInfo: state => state.service.readInfo
      })
    },
    methods: {
      details({
        noticeId,
        noticeContent,
        readFlag
      }) {
        this.$store.commit('setReadInfo', noticeContent);
        this.showVisible = true
        if (readFlag !== '1') {
          postSetNoticeFlag({
            noticeId
          }).then(res => {
            this.messageList.find(n => {
              return n.noticeId === noticeId
            }).readFlag = '1'
          });
        }

      },
      initData() {
        GetSysNotice().then(res => {
          this.messageList = res.rows.reverse()
        }).finally(() => {
          this.triggered = false
        })
      },
      onConfirm() {
        // this.initData()
        this.showVisible = false
      },
      onRefresh() {
        console.log('cccoooo')
        this.triggered = true
        this.initData()
      }
    },
    mounted() {
      this.initData()
    }
  }
</script>

<style lang="scss">
  ::v-deep {
    .u-badge--dot {
      width: 20rpx !important;
      height: 20rpx !important;
    }
  }

  .ui-list {
    margin-top: 0rpx;
    width: 100%;
    height: 100vh;
    box-sizing: border-box;
    padding: 0 22rpx 0rpx 22rpx;

    .ui-item {
      margin-top: 40rpx;
      box-sizing: border-box;
      text-align: center;


      &:nth-last-child(1) {
        // margin-bottom: 300rpx;
        padding-bottom: 150rpx;
      }

      .ui-date {
        display: inline-flex;
        align-items: center;
        justify-content: center;
        width: 300rpx;
        border-radius: 10rpx;
        padding: 10rpx;
        margin: 16rpx 0;
        font-size: 24rpx;
        color: #fff;
        background-color: rgba(0, 0, 0, 0.3);
      }

      .ui-box {
        margin-top: 20rpx;
        padding: 30rpx 20rpx 0 20rpx;
        border-radius: 10px;
        background: #fff;
        text-align: left;
        position: relative;

        >text {
          width: 100%;
          margin-bottom: 10rpx;

          &:nth-child(2) {
            margin-bottom: 20rpx;
            display: inline-block;
            font-size: 30rpx;

          }
        }

        .ui-detail {
          margin-top: 20rpx;
          display: inline-flex;
          align-items: center;
          justify-content: space-between;
          width: 100%;
          height: 80rpx;
          border-top: 1px solid #e2e2e2;

          text {
            font-size: 25rpx;
          }
        }
      }


    }
  }

  .wd-content {
    width: 600rpx;
    height: 900rpx;
    border-radius: 20rpx;
    filter: drop-shadow(0 0 5rpx rgba(7, 5, 5, 0.34));
    background-image: #fff;
    padding: 100rpx 31rpx 53rpx 31rpx;

    .uni-scroll {
      height: 800rpx;
      word-wrap: break-word;
      word-break: normal;
      text-indent: 1em;
    }

    button {
      margin-top: 32rpx;
      width: 300rpx;
    }
  }
</style>
