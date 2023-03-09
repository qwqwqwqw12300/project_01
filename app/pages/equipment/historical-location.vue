<template>
	<app-body :bg="false" :bodyStyle="{backgroundColor:'#FFF'}">
		<app-logo color="#353535" text="历史位置"></app-logo>
		<view class="ui-box">
			<view class="time-picker" @tap="handleSelect">
				<text class="label">
					日期
				</text>
				<text class="value">
					{{ dateData }}
				</text>
			</view>
		</view>
		<!-- 	<u-cell-group> -->
		<u-cell icon="setting-fill" title="地址列表"></u-cell>
		<u-cell :title="item.address" :label="item.subAddress" v-for="(item,index) in dataList" :key="index">
			<text slot="value" class="u-slot-value">
				{{item.locateTime}}
			</text>
		</u-cell>
		<u-calendar ref="calendar" :defaultDate="dateData" :show="show" mode="'single" @confirm="onSelected"
			@close="show = false" closeOnClickOverlay>
		</u-calendar>
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
				dataList: [],
			}
		},
		computed: {
			...mapState({
				/**所有家庭列表**/
				deviceInfo: state => state.deviceInfo
			}),
		},
		mounted() {
			this.dateData = uni.$u.timeFormat(new Date(), 'yyyy-mm-dd')
			console.log(this.dateData, '44')
			this.queryData()
		},
		methods: {
			handleSelect() {
				this.show = true
			},
			onSelected(e) {
				this.dateData = e[0]
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
					deviceNo: this.deviceInfo.no
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
