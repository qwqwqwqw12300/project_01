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
			<!-- <view class="map-address">
				<view class="title-cell">
					<text></text>地址列表
				</view>
				<view class="address-cell" v-for="(n,i) in list" :key="i">
					<text class="label">{{ n.name }}</text>
					<text class="value">{{ n.dateTime }}</text>
				</view>
			</view> -->
		</view>
		<!-- 	<u-cell-group> -->
		<u-cell icon="setting-fill" title="地址列表"></u-cell>
		<u-cell :title="item.address" :label="item.subAddress" v-for="(item,index) in dataList" :key="index">
			<text slot="value" class="u-slot-value">
				{{item.locateTime}}
			</text>
		</u-cell>
		<!-- 	</u-cell-group> -->
		<time-picker :show="showPicker" format="yyyy-mm-dd hh:ii" type="rangetime" :value="defaultValue"
			:show-tips="true" :begin-text="'开始'" :end-text="'结束'" :show-seconds="false" @confirm="onSelected"
			@cancel="showPicker=false">
		</time-picker>
	</app-body>
</template>

<script>
	import {
		GetsetAddressBook,
	} from '@/common/http/api';
	import timePicker from '@/components/term-picker/term-picker.vue';
	export default {
		components: {
			timePicker
		},
		data() {
			return {
				defaultValue: [],
				showPicker: false,
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
				],
				dataList: [],
			}
		},
		mounted() {
			const newData = new Date()
			const endTime = uni.$u.timeFormat(newData, 'yyyy-mm-dd hh:MM')
			const startTime = uni.$u.timeFormat(new Date(newData.getTime() - 24 * 60 * 60 * 1000),
				'yyyy-mm-dd hh:MM') //前一天
			this.defaultValue = [startTime, endTime]
			this.queryData(startTime, endTime)
		},
		methods: {
			handleSelect() {
				this.showPicker = true
			},
			onSelected(e) {
				this.defaultValue = [...e.value]
				this.showPicker = false
				this.queryData()
			},
			getAddress(n) {
				const {
					latitude,
					longitude
				} = n.location
				return new Promise((resolve, reject) => {
					// console.log(latitude, longitude, 'lllllll')
					uni.getLocation({
						latitude,
						longitude,
						geocode: true,
						type: 'gcj02',
						success: res => {
							const {
								address: {
									province,
									city,
									district,
									street,
									streetNum,
									poiName
								},
							} = res
							n.address = province + city + district
							n.subAddress = street + streetNum + poiName
							resolve(n)
						},
						false: (res) => {
							reject(res)
						}
					})
				})
			},
			queryData() {
				// uni.showLoading()
				const startTime = this.defaultValue[0]
				const endTime = this.defaultValue[1]
				GetsetAddressBook({
					startTime,
					endTime,
					deviceNo: "867977060000248"
				}).then(res => {
					const list = res.data.map(n => {
						n.locateTime = uni.$u.timeFormat(n.locateTime, 'yyyy-mm-dd hh:MM')
						return n
					}).slice(0, 4)
					const promises = list.map(n => {
						return this.getAddress(n)
					})
					Promise.all(promises).then(res => {
						console.log(res, 'sssssssssss')
						this.dataList = res
					}).catch(res => {}).finally(() => {
						uni.hideLoading()
					})
				})
			}
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
			// border-bottom: solid 2px #f7f7f7;

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
			background-color: #fff;

			.title-cell {
				height: 130rpx;
				display: flex;
				align-items: center;
				font-size: 36rpx;
				color: #333;
				border-bottom: solid 2px #f7f7f7;

				text {
					height: 50rpx;
					border-left: solid 14rpx #FEAE43;
					border-radius: 4rpx;
					margin-right: 20rpx;
				}
			}

			.address-cell {
				height: 128rpx;
				border-bottom: solid 2px #f7f7f7;
				display: flex;
				align-items: center;
				justify-content: space-between;

				.label {
					font-size: 32rpx;
					color: #353535;
				}

				.value {
					font-size: 30rpx;
					color: #888888;
				}
			}

			// .address-box {
			// 	display: flex;
			// 	flex-direction: column;


			// 	.list-item {
			// 		// padding: 48rpx 32rpx;
			// 		height: 128rpx;
			// 		display: flex;
			// 		align-items: center;
			// 		justify-content: space-between;
			// 		border-bottom: solid 2px #f7f7f7;
			// 	}
			// }
		}

		.ui-btn {
			margin-top: 50rpx;
		}
	}
</style>
