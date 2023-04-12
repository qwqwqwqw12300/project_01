<template>
	<app-body :bg="false" :bodyStyle="{background: '#F7F7F7' }">
		<app-logo text="血压" :isShare="true"></app-logo>
		<view class="box">
			<date-picker @onSelect="onSelect" @month="monthChange" :lightDot="monthData"></date-picker>
		</view>
		<component ref="comRef" :is="tabKey" :time="time"></component>
	</app-body>
</template>

<script>
	import {
		GetMonthDataFlag,
	} from '@/common/http/api.js';
	import date from './components/blood-date.vue'
	import week from './components/blood-week.vue'
	export default {
		components: {
			week,
			date,
		},
		data() {
			return {
				tabKey: 'date',
				time: '',
				monthData: [],
			}
		},
		created() {
			const month = uni.$u.timeFormat(new Date(), 'yyyy-mm')
			this.getMonthData(month)
		},
		methods: {
			monthChange(date) {
				const month = uni.$u.timeFormat(date, 'yyyy-mm')
				this.getMonthData(month)
			},
			
			getMonthData(month) {
				GetMonthDataFlag({
					yearMonth: month,
					type: '7', //（ 1卡路里 2活动时长 3步数 4睡眠 5站立 6心率 7血压 8血氧 9心电）
				}).then(res => {
					this.monthData = res.data.filter(n => {
						return n.flag === '1'
					}).map(item => {
						return item.date
					})
				})
			},
			onSelect(val) {
				this.tabKey = val.type
				this.time = val.value
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
</style>