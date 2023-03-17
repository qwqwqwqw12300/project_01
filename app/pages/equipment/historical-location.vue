<template>
	<app-body :bg="false" :bodyStyle="{backgroundColor:'#FFF'}">
		<locus-map :record="mapData"></locus-map>
		<touch-popup :minHeight="0.1" :maxHeight="0.6" :touchHeight="64" radius="30rpx">
			<!-- 	<view class="ui-search">
				<u-search placeholder="请输入您想搜索的内容" :showAction="false"></u-search>
			</view> -->
			<view class="ui-select">
				<view class="label">
					<text>选择时间</text>
				</view>
				<view class="value" @click="handleSelect">
					<u-input disabled type="text" :value="`${dateData} 00:00`" /><text style="margin: 0 20rpx;">至</text>
					<u-input disabled type="text" :value="`${dateData} 23:59`" />
				</view>
			</view>
			<view class="address-list">
				<view class="address-cell" v-for="(item,index) in dataList" :key="index">
					<view class="label">
						<text>{{ item.address }}</text>
						<text class="sub">{{ item.subAddress }}</text>
					</view>
					<text class="value">
						{{item.locateTime}}
					</text>
				</view>
			</view>
		</touch-popup>
		<u-calendar :show="show" mode="single" monthNum="4" :minDate="minDate" :maxDate="maxDate" @confirm="onSelected"
			@close="show=false"></u-calendar>
	</app-body>
</template>

<script>
	import {
		GetsetAddressBook,
	} from '@/common/http/api';
	import timePicker from '@/components/term-picker/term-picker.vue';
	import {
		mapState,
	} from 'vuex';
	export default {
		components: {
			timePicker
		},
		data() {
			return {
				show: false,
				dateData: '',
				maxDate: '',
				minDate: '',
				dataList: [],
				currentSelect: '',
			}
		},
		computed: {
			...mapState({
				/**所有家庭列表**/
				deviceInfo: state => state.deviceInfo
			}),
			mapData() {
				return this.dataList.map(n => {
					const {
						longitude,
						latitude
					} = n.location
					return [longitude, latitude]
				})
			}
		},
		mounted() {
			const today = new Date()
			this.dateData = uni.$u.timeFormat(today, 'yyyy-mm-dd')
			this.maxDate = uni.$u.timeFormat(today, 'yyyy-mm-dd')
			this.minDate = uni.$u.timeFormat((today.setMonth(today.getMonth() - 3)), 'yyyy-mm-dd')
			this.queryData()
		},
		methods: {
			handleSelect() {
				this.show = true
			},
			onSelected(e) {
				this.dateData = e[0]
				// this.dateData = e.value
				this.show = false
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
				GetsetAddressBook({
					startTime: this.dateData + " " + '00:00:00',
					endTime: this.dateData + " " + '23:59:59',
					deviceId: this.deviceInfo.deviceId
				}).then(res => {
					const list = res.data.map(n => {
						n.locateTime = uni.$u.timeFormat(n.locateTime, 'yyyy-mm-dd hh:MM')
						return n
					}).slice(0, 4)
					const promises = list.map(n => {
						return this.getAddress(n)
					})
					Promise.all(promises).then(res => {
						this.dataList = res
					}).catch(res => {}).finally(() => {
						// uni.hideLoading()
					})
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.ui-search {
		margin: 20rpx 0;
	}

	.ui-select {
		.label {
			height: 80rpx;
			display: flex;
			align-items: center;
			font-size: 26rpx;
			color: #353535;
			border-bottom: solid 2px #f7f7f7;
		}

		.value {
			height: 120rpx;
			display: flex;
			align-items: center;
			justify-content: space-between;
		}
	}

	.address-list {
		margin-top: 30rpx;
		background-color: #fff;

		.address-cell {
			height: 128rpx;
			border-bottom: solid 2px #f7f7f7;
			display: flex;
			align-items: center;
			justify-content: space-between;

			.label {
				display: flex;
				flex-direction: column;
				font-size: 32rpx;
				color: #353535;

				.sub {
					margin-top: 5px;
					font-size: 12px;
					color: #909193;
				}
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
