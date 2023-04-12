<template>
	<app-body :bg="false" :needService="false">
		<app-logo text="心电" :isShare="true"></app-logo>
		<view class="box">
			<dates @change="change" @month="monthChange" :lightDot="monthData"></dates>
		</view>
		<date :time="time"></date>
	</app-body>
</template>

<script>
	import {
		GetMonthDataFlag,
	} from '@/common/http/api';
	import date from './components/electrocardiograph-date.vue'
	import dates from '@/components/date-picker/components/date.vue'
	export default {
		components: {
			date,
			dates
		},
		data() {
			return {
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
					type: '9', //（ 1卡路里 2活动时长 3步数 4睡眠 5站立 6心率 7血压 8血氧 9心电）
				}).then(res => {
					this.monthData = res.data.filter(n => {
						return n.flag === '1'
					}).map(item => {
						return item.date
					})
				})
			},
			change(val) {
				console.log(val, 'val')
				this.time = val.value
				// this.time = '2023-1-10'
			}
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