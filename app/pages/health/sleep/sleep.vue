<template>
  <app-body :bg="false" title="睡眠">
    <!-- <app-logo text="睡眠" :isShare="true"></app-logo> -->
    <view class="box">
      <date-picker @onSelect="onSelect" @month="monthChange" :lightDot="monthData"></date-picker>
    </view>
    <view class="box-content">
      <component ref="comRef" :dayTime="searchDate" :is="tabKey"></component>
    </view>
  </app-body>
</template>

<script>
  import {
    GetMonthDataFlag,
  } from '@/common/http/api.js';
  import date from './components/sleep-date.vue'
  import week from './components/sleep-week.vue'
  import {
  	mapState,
  } from 'vuex';
  export default {
    components: {
      week,
      date,
    },
    data() {
      return {
        tabKey: 'date',
        monthData: [],
        searchDate: '',
      }
    },
    created() {
      const month = uni.$u.timeFormat(new Date(), 'yyyy-mm')
      this.getMonthData(month)
    },
	computed: {
		...mapState({
			deviceInfo: state => state.deviceInfo
		}),
	},
    methods: {
      monthChange(date) {
        const month = uni.$u.timeFormat(date, 'yyyy-mm')
        this.getMonthData(month)
      },

      getMonthData(month) {
		  const {
		  	deviceId,
		  	humanId,
		  } = this.deviceInfo
        GetMonthDataFlag({
			deviceId,
          yearMonth: month,
          type: '4', //（ 1卡路里 2活动时长 3步数 4睡眠 5站立 6心率 7血压 8血氧 9心电）
        }).then(res => {
          this.monthData = res.data.filter(n => {
            return n.flag === '1'
          }).map(item => {
            return item.date
          })
        })
      },
      onSelect(val) {
        console.log(val, '4333333333')
        this.searchDate = val.value
        this.tabKey = val.type
      },

    }
  }
</script>

<style lang="scss" scoped>
  .box {
    margin-top: 68rpx;
  }

  .ui-icon {
    margin-left: 546rpx;
  }

  .box-content {
    padding-bottom: 30px;
  }
</style>
