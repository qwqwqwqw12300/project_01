<template>
	<app-body :bg="false" :bodyStyle="{backgroundColor:'#FFF'}">
		<app-logo color="#353535" text="历史位置"></app-logo>
		<view class="ui-box">
			<!-- 	<u-cell @tap="handleSelect" title="日期" arrow-direction="right" isLink>
				<text slot="value" class="u-slot-value">
					{{ startDate }} 至 {{endDate}}
				</text>
			</u-cell> -->
			<view class="time-picker">
				<text class="label">
					日期
				</text>
				<text class="value" @tap="handleSelect">
					{{ defaultValue.length ? `${defaultValue[0]}  至 ${defaultValue[1]}` : '请选择'}}
				</text>
			</view>
		</view>
		<time-picker :show="showPicker" format="yyyy-mm-dd hh:ii" type="rangetime" :value="defaultValue"
			:show-tips="true" :begin-text="'开始'" :end-text="'结束'" :show-seconds="false" @confirm="onSelected"
			@cancel="showPicker=false">
		</time-picker>
	</app-body>
</template>

<script>
	import timePicker from '@/components/term-picker/term-picker.vue';
	export default {
		components: {
			timePicker
		},
		data() {
			return {
				defaultValue: [],
				startTime: "14:00",
				endTime: "15:00",
				timeShow: false,
				showPicker: false,
				defaultTime: [0, 0, 0, 23, 59],
				list: [{
						name: '新大陆科技园',
						dateTime: '2023/02/28 13:00'
					},
					{
						name: '新大陆壹号',
						dateTime: '2023/02/28 13:30'
					},
					{
						name: '中国移动',
						dateTime: '2023/02/28 14:00'
					},
					{
						name: '中国海关',
						dateTime: '2023/02/28 14:20'
					},
					{
						name: '名城国际',
						dateTime: '2023/02/28 15:38'
					},
				]
			}
		},
		mounted() {
			const newData = new Date()
			const endTime = uni.$u.timeFormat(newData, 'yyyy-mm-dd hh:MM')
			const startTime = uni.$u.timeFormat(new Date(newData.getTime() - 24 * 60 * 60 * 1000),
				'yyyy-mm-dd hh:MM') //前一天
			this.defaultValue = [startTime, endTime]
		},
		methods: {
			handleSelect() {
				this.showPicker = true
			},
			onSelected(e) {
				this.defaultValue = [...e.value]
				this.showPicker = false
			},
		}
	}
</script>

<style lang="scss" scoped>
	.ui-box {
		padding: 32rpx;

		.time-picker {
			height: 128rpx;
			display: flex;
			align-items: center;
			justify-content: space-between;
			border-bottom: solid 2px #f7f7f7;

			.label {
				width: 80rpx;
				font-size: 34rpx;
				color: #353535;
			}

			.value {
				flex: 1;
				font-size: 30rpx;
				color: #888888;
				text-align: right;
			}
		}

		.map-address {
			margin-top: 30rpx;
			padding: 0 32rpx;
			background-color: #fff;
			border-radius: 16rpx;

			.address-box {
				display: flex;
				flex-direction: column;


				.list-item {
					// padding: 48rpx 32rpx;
					height: 128rpx;
					display: flex;
					align-items: center;
					justify-content: space-between;
					border-bottom: solid 2px #f7f7f7;
				}
			}
		}

		.ui-btn {
			margin-top: 50rpx;
		}
	}
</style>
