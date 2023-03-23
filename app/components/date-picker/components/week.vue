<template>
	<view>
		<view class="ui-box">
			<view class="content">
				<u-icon name="/static/images/back.png" size="25" @tap="handleBack"></u-icon>
				<text>{{ dateFormat }}</text>
				<u-icon name="/static/images/forward.png" size="25" @tap="handleForward"></u-icon>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				dateStartTime: '',
				dateEndTime: '',
				weekDate: [],
			}
		},
		computed: {
			dateFormat() {
				return `${uni.$u.timeFormat(this.weekDate[0], 'mm/dd') || null}-${uni.$u.timeFormat(this.weekDate[6],'mm/dd') || null} `
			}
		},
		watch: {
			weekDate: {
				handler(val) {
					this.$emit('change', {
						value: val
					})
				}
			}
		},
		mounted() {
			this.dateStartTime = new Date()
			this.weekDate = this.setDate(this.addDate(this.dateStartTime, 0))
			console.log(this.weekDate, 'ww')
		},
		methods: {
			formatDate(date) {
				return uni.$u.timeFormat(date, 'yyyy-mm-dd')
			},
			addDate(date, n) {
				date.setDate(date.getDate() + n)
				return date
			},
			setDate(date) {
				const week = date.getDay() - 1 //getDay() 方法可返回一周（0~6）的某一天的数字 星期一为 1,
				const firstDate = this.addDate(date, week * -1) //获取当周周一日期
				this.dateStartTime = new Date(firstDate)
				const obj = []
				for (let i = 0; i < 7; i++) {
					obj.push(this.formatDate(i === 0 ? firstDate : this.addDate(firstDate, 1)))
				}
				return obj
			},
			handleBack() {
				this.weekDate = this.setDate(this.addDate(this.dateStartTime, -7))
			},
			handleForward() {
				this.weekDate = this.setDate(this.addDate(this.dateStartTime, 7))
			}
		}
	}
</script>

<style scoped lang="scss">
	.ui-box {
		margin-top: 60rpx;
		height: 50rpx;
		padding: 0 40rpx;
		display: flex;
		align-items: center;
		justify-content: center;

		.content {
			display: flex;
			align-items: center;

			text {
				margin: 0 40rpx;
				font-size: 32rpx;
				color: #353535;
			}
		}

	}
</style>
