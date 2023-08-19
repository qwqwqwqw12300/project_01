<template>
  <app-body :bg="false" :needService="false" :bodyStyle="{backgroundColor:'#FFF'}" :title="title">
    <view class="app-head">
      <!-- <app-logo color="#353535"> -->
      <!-- <u--text :text="title" :lines="2" size="25"></u--text> -->
      <!-- <view>{{ title }}</view> -->
      <!-- <u--text :lines="1" :text="title" size="25"></u--text> -->
      <!-- </app-logo> -->
      <view class="tab">
        <view class="tab-box">
          <text @tap="handleTab(item.key)" class="tab-item" :class="{'active': tabKey === item.key }"
            v-for="item in tabList" :key="item.key">
            {{ item.name }}
          </text>
        </view>
      </view>
    </view>
    <component ref="comRef" :is="tabKey"></component>
  </app-body>
</template>

<script>
  import {
    mapState,
  } from 'vuex';
  import circleMap from './components/circle.vue'
  import polygonMap from './components/polygon.vue'
  export default {
    components: {
      circleMap,
      polygonMap,
    },
    data() {
      return {
        tabList: [{
          key: 'circleMap',
          name: '圆形'
        }, {
          key: 'polygonMap',
          name: '多边形'
        }],
        tabKey: 'circleMap',
        title: ''
      }
    },
    computed: {
      ...mapState({
        deviceInfo: state => state.deviceInfo,
        urlLocation: state => state.urlLocation
      }, )
    },
    created() {
      this.tabKey = this.urlLocation.fenceType === 'circle' ? 'circleMap' : this.urlLocation.fenceType ===
        'polygon' ? 'polygonMap' : 'circleMap'
      this.title = this.urlLocation.name ? `守护区-${this.urlLocation.name}` : '新增守护区'
    },
    methods: {
      handleTab(key) {
        this.tabKey = key
      },
    }
  }
</script>

<style lang="scss" scoped>
  .app-head {
    // margin-top: 60rpx;
    margin-left: 32rpx;
    margin-bottom: 20rpx !important;
    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  .wd-logo {
    margin: 0 !important;
  }

  .tab {
    // position: absolute;
    // left: 32rpx;
    // top: 800rpx;
    // z-index: 9;
    width: 100%;
    display: flex;
    justify-content: flex-end;

    .tab-box {
      width: 260rpx;

      height: 70rpx;
      padding: 0 10rpx;
      // box-sizing: border-box;
      background-color: #fff;
      border-radius: 28rpx;
      display: flex;
      align-items: center;
      justify-content: space-around;

      .tab-item {
        flex: 1;
        height: 50rpx;
        line-height: 50rpx;
        text-align: center;
        font-size: 30rpx;
        color: #353535;
        border-radius: 28rpx;
      }

      .active {
        color: #FFFFFF !important;
        background-image: linear-gradient(90deg, #FFB24D 0%, #FD913B 100%);
      }

      .t1 {
        flex: 1;
        height: 50rpx;
        line-height: 50rpx;
        text-align: center;
        font-size: 30rpx;
        color: #FFFFFF;
        background-image: linear-gradient(90deg, #FFB24D 0%, #FD913B 100%);
        border-radius: 28rpx;
      }

      .t2 {
        flex: 1;
      }

      // background-color: #FFB24D;
    }
  }
</style>
