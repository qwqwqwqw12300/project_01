<!--
* @Author: zhanch
* @Date: 2022-12-30 17:14:05
* @FilePath: /pages/myself/device-manage.vue
* @Description: 设备管理
-->

<template>
  <view id="deviceManage">
    <app-body :back="back" :bg="false" title="设备管理">
      <!-- <view class="ui-logo">
				<app-logo text="设备管理"></app-logo>
			</view> -->
      <view class="ui-menu">
        <template v-if="devices.length">
          <view v-for="(device, index) of devices" :key="'f' + index">
            <!-- 	<view class="ui-title" v-if="device.list.length">{{device.name}}</view> -->
            <view class="ui-menu-title" v-if="device.list.length">
              <u-icon name="/static/images/home.png" size="28"></u-icon>
              <text>{{device.name || '未命名家庭'}}</text>
            </view>
            <view class="ui-menu-content">
              <template v-for="(item, index) in device.list">
                <!-- 雷达波设备 -->
                <device-card v-if="item.type === '0'" :device="item" @change="init" :key="'leidabo' + index"
                  @bind="binding" />
                <!-- /雷达波设备 -->
                <!-- 电子牵挂卡设备 -->
                <dzqgk-card v-if="item.type === '1'" :device="item" @change="init" :key="'dzqgk' + index"
                  @bind="bindingHuman($event, '1')" />
                <!-- 电子牵挂卡设备 -->
                <!-- 4g手表设备 -->
                <watch-card v-if="item.type === '2'" :device="item" @change="init" :key="'watch' + index"
                  @bind="bindingHuman($event, '2')" />
                <!-- /4g手表设备 -->
                <!-- SOS设备 -->
                <sos-card v-if="item.type === '3'" :device="item" @change="init" :key="'watch' + index"
                  @bind="binding" />
                <!-- /SOS设备 -->
                <!-- H102手表设备 -->
                <watch-card-h v-if="item.type === '4'" :device="item" @change="init" :key="'watch' + index"
                  @bind="bindingHuman($event, '2')" />
                <!-- /H102手表设备 -->
              </template>
            </view>
          </view>
        </template>
        <view class="list-empty" v-else>
          <u-empty mode="list" text="暂无数据"></u-empty>
        </view>
      </view>
      <view class="ui-btn"><button class="default" @click="show = true">添加设备</button></view>
      <app-handle :isShow="show" :safeShow="safeAreaShow = true" @cancle="show = false"></app-handle>
      <select-bind ref="selectRef" @comfirm="bindSubmit"></select-bind>
      <select-human ref="selectHumanRef" @comfirm="bindHumanSubmit"></select-human>
    </app-body>
  </view>
</template>

<script>
  import {
    getRoomList,
    PostDeviceList,
    PosteditDevice,
    PostDeviceDel,
    setDevice,
    relDevice,
    getDeviceListState,
    PostUpdateCareCardBind,
    PostUpdateWatchBind,
    updateGatewaydBind
  } from '@/common/http/api.js';
  import {
    mapState,
    mapActions
  } from 'vuex';
  import
  DeviceCard
  from './components/device-card.vue';
  import
  SelectBind
  from './components/select-bind.vue';
  import
  DzqgkCard
  from './components/dzqgk-card.vue';
  import
  AppHandle
  from '@/components/add-handle/add-handle.vue';
  import
  SelectHuman
  from './components/select-human.vue';
  import
  WatchCard
  from './components/watch-card.vue';
  import
  SosCard
  from './components/sos-card.vue';
  import
  WatchCardH
  from './components/watch-card-h.vue';
  export default {
    components: {
      AppHandle,
      DeviceCard,
      SelectBind,
      DzqgkCard,
      SelectHuman,
      WatchCard,
      SosCard,
      WatchCardH
    },
    data() {
      return {
        show: false,
        safeAreaShow: false,
        list: [],
        dzqgkList: [],
        // 绑定的设备 1 -电子牵挂卡 2-手表
        bindType: '1',
        back: () => {
          uni.switchTab({
            url: '/pages/myself/myself'
          })
        }
      };
    },
    computed: {
      ...mapState({
        devices: function(state) {
          const list = this.list,
            devices = [{
              name: '未绑定设备',
              list: list.filter(item => item && !item.familyId) || []
            }];
          state.familyList.forEach(ele => {
            if (ele.shareFlag === '2') {
              const items = list.filter(item => item.familyId === ele.familyId);
              if (items.length)
                console.log(items, 'items')
              devices.push({
                name: ele.name,
                list: items
              });
            }
          });
          return devices;
        }

      }),
    },
    onShow() {
      this.init();
    },
    methods: {
      ...mapActions(['getAllFamily']),

      getList() {
        getDeviceListState({
          pageSize: 10000
        }).then(res => {
          this.list = res.rows || [];
        });
      },

      /**
       * 绑定
       */
      binding(deviceId) {
        this.$refs.selectRef.open(deviceId);
      },

      /**
       * 绑定人员
       */
      bindingHuman(deviceId, type) {
        this.bindType = type;
        this.$refs.selectHumanRef.open(deviceId);
      },

      /**
       * 绑定提交
       */
      bindSubmit(form) {
        if (form.familyId && form.roomId) {
          setDevice({
            ...form,
            flag: '1'
          }).then(res => {
            uni.$u.toast(res.msg);
            setTimeout(() => {
              this.init();
            }, 1000);
          })
        } else {
          uni.$u.toast('请选择要绑定的房间');
        }
      },

      /**
       * 绑定人员
       */
      bindHumanSubmit(form) {
        if (form.familyId && form.humanId) {
          const post = this.bindType === '1' ? PostUpdateCareCardBind : this.bindType === '2' ? PostUpdateWatchBind :
            updateGatewaydBind;
          post({
            ...form,
          }).then(res => {
            uni.$u.toast(res.msg);
            setTimeout(() => {
              this.init();
            }, 1000);
          })

        } else {
          uni.$u.toast('请选择要绑定的人员');
        }
      },

      /**
       * 初始化设备
       */
      init() {
        Promise.all([
          this.getList(),
          this.getAllFamily()
        ])
      },
    },
    onBackPress(event) {
      console.log('物理返回', event);
      if (event.from === 'backbutton') {
        uni.switchTab({
          url: '/pages/myself/myself'
        });
        return true;
      }

    }
  };
</script>

<style lang="scss">
  .ui-logo {
    background: #ffffff;
    padding-bottom: 50rpx;
  }

  .ui-menu {
    // margin: 70rpx 0;
    padding: 0 20rpx 120rpx 20rpx;
    // padding-top: 30rpx;
    min-height: 600rpx;

    .ui-menu-title {
      display: flex;
      align-items: center;
      font-size: 34rpx;
      color: #353535;
      height: 40px;
      font-weight: 550;
      margin-bottom: 10rpx;

      text {
        margin-left: 10rpx;
      }
    }

    .ui-menu-content {
      padding-bottom: 30rpx;
      display: flex;
      flex-wrap: wrap;
      box-sizing: border-box;
    }

    .list-empty {
      width: 100%;
      height: 500rpx;
      display: flex;
      justify-content: center;
      align-items: center;
    }
  }

  .ui-btn {
    width: 100%;
    position: fixed;
    bottom: 0;
    left: 0;

    button {
      border-radius: 0rpx !important;
    }
  }
</style>
