<template>
	<app-body :needService="false" :bg="false" :bodyStyle="{backgroundColor:'#FFF'}">
		<map v-show="false"></map>
		<locus-map :record="mapData"></locus-map>
		<view class="ui-select">
			<view class="label">
				<text>选择时间</text>
			</view>
			<view class="value">
				<view class="value-left" @tap="queryData('last')">前一天</view>
				<view class="value-date" @tap="handleSelect">
					<!-- <u-input disabled type="text" :value="`${dateData[0]}`" /> -->
					<view class="value-date-text">{{ dateTimeFormatting() }}</view>
					<u-icon name="arrow-down-fill" color="#e88e6e" size="18"></u-icon>
				</view>
				<view class="value-right" @tap="queryData('next')" :style="{'color': ColorStyle}">后一天</view>
				<!-- <text style="margin: 0 20rpx;">至</text> -->
				<!-- <u-input disabled type="text" :value="`${dateData[1]} 23:59`" /> -->
			</view>
		</view>
		<touch-popup :minHeight="0.13" :maxHeight="0.5" :touchHeight="64" radius="30rpx">
			<!-- 	<view class="ui-search">
				<u-search placeholder="请输入您想搜索的内容" :showAction="false"></u-search>
			</view> -->
			<!-- <view class="ui-select">
				<view class="label">
					<text>选择时间</text>
				</view>
				<view class="value">
					<view>前一天</view>
					<view @click="handleSelect">
						<u-input disabled type="text" :value="`${dateData[0]}`" />
					</view>
					<view>后一天</view>
					<text style="margin: 0 20rpx;">至</text>
					<u-input disabled type="text" :value="`${dateData[1]} 23:59`" />
				</view>
			</view> -->
			<view class="address-list" @touchstart.stop @touchmove.stop @touchend.stop>
				<template v-if="dataList.length">
					<scroll-view :scroll-y="true" class="scroll">
						<view class="address-cell" v-for="(item,index) in dataList" :key="index">
							<view class="label">
								<text>{{ item.address }}</text>
								<text class="sub">{{ item.subAddress }}</text>
							</view>
							<text class="value">
								{{item.locateTime}}
							</text>
						</view>
					</scroll-view>
				</template>
				<view class="list-empty" v-else>
					<u-empty mode="list" text="暂无数据"></u-empty>
				</view>
			</view>
		</touch-popup>
		<u-calendar :show="show" mode="single" monthNum="4" :minDate="minDate" :maxDate="maxDate" @confirm="onSelected"
			@close="show=false"></u-calendar>
	</app-body>
</template>

<script>
	import {
		GetOneDayPoints,
	} from '@/common/http/api';
	import timePicker from '@/components/term-picker/term-picker.vue';
	import {
		mapState,
	} from 'vuex';
	import {
		isIos,
		isApp
	} from '@/common/utils/util';
	let mapSearch;
	if (isApp()) mapSearch = weex.requireModule('mapSearch');
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
				return this.dataList.filter(ele => {
					const {
						longitude,
						latitude
					} = ele.location;
					return longitude && latitude;
				}).map(n => {
					const {
						longitude,
						latitude
					} = n.location
					// return [longitude, latitude]
					return {location: [longitude, latitude], icon: n.icon}
				})
			},
			dateTimeFormatting() {
				return function() {
					return this.dateData === uni.$u.timeFormat((new Date().setDate(new Date().getDate())), 'yyyy-mm-dd') ? '今天' : this.dateData
				}
			},
			ColorStyle() {
				return this.dateData === uni.$u.timeFormat((new Date().setDate(new Date().getDate())), 'yyyy-mm-dd') ? '#c2b4b4' : 'black'
			}
		},
		mounted() {
			const today = new Date()
			this.dateData = uni.$u.timeFormat((new Date().setDate(today.getDate())), 'yyyy-mm-dd')
			this.maxDate = uni.$u.timeFormat(today, 'yyyy-mm-dd')
			this.minDate = uni.$u.timeFormat((today.setMonth(today.getMonth() - 2)), 'yyyy-mm-dd')
			this.queryData('init')
		},
		methods: {
			handleSelect() {
				this.show = true
			},
			onSelected(e) {
				// this.dateData = [e[0], e[e.length - 1]]
				this.dateData = e[0]
				this.show = false
				this.queryData('init')
			},
			getLocation(n) {
				const {
					latitude,
					longitude
				} = n.location
				return new Promise((resolve, reject) => {
					if (!latitude || !longitude) {
						return reject()
					}
					mapSearch && mapSearch.reverseGeocode({
							point: {
								latitude,
								longitude
							}
						},
						e => {
							if (e.type === 'fail') return reject()
							resolve({
								location: {
									latitude,
									longitude,
								},
								locateTime: n.locateTime,
								address: e.address
							})
						})
				})
			},
			queryData(type) {
				uni.showLoading({
					title: '加载中'
				})
				let timeStamp, timeDate
				if(type === 'init') {
					timeDate = this.dateData
				} else if (type === 'last') {
					timeStamp = new Date(this.dateData) - 24 * 60 * 60 * 1000
					this.dateData = uni.$u.timeFormat(new Date(timeStamp), 'yyyy-mm-dd')
					timeDate = this.dateData
				} else {
					if(new Date().getTime() === Date.parse(this.dateData)) return
					timeStamp = Date.parse(this.dateData) + 86400000
					this.dateData = uni.$u.timeFormat(new Date(timeStamp), 'yyyy-mm-dd')
					timeDate = this.dateData
				}
				GetOneDayPoints({
					// startTime: this.dateData[0] + " " + '00:00:00',
					// endTime: this.dateData[0] + " " + '23:59:59',
					queryTime: timeDate,
					deviceId: this.deviceInfo.deviceId
				}).then(async res => {
					const list = res.data.map(n => {
						n.locateTime = uni.$u.timeFormat(n.locateTime, 'yyyy-mm-dd hh:MM')
						return n
					})
					const arr = []
					for (let i = 0; i < list.length; i++) {
						const res = await this.getLocation(list[i])
						res.index = i
						res.icon = "./static/images/starting_point.png"
						arr.push(res)
					}
					this.dataList = arr
					uni.hideLoading()
					// const promises = list.map(n => {
					// 	return this.getAddress(n)
					// })
					// Promise.allSettled(promises).then(res => {
					// 	this.dataList = res.filter(n => {
					// 		return n.status = 'fulfilled'
					// 	}).map(item => {
					// 		return item.value
					// 	})
					// 	console.log(this.historyList, 'pppppppp------------')
					// }).catch(res => {}).finally(() => {})
				})
			},
			dateOperation(type) {
				this.queryData(type)
			}
		}
	}
</script>

<style lang="scss" scoped>
	.list-empty {
		height: 600rpx;
		display: flex;
		justify-content: center;
		align-items: center;
	}

	.ui-search {
		margin: 20rpx 0;
	}

	.ui-select {
		position: absolute;
		top: 30rpx;
		left: 0;
		width: 90%;
		margin-left: 5%;
		box-sizing: border-box;
		.label {
			height: 80rpx;
			display: flex;
			align-items: center;
			font-size: 26rpx;
			color: #353535;
			border-bottom: solid 2px #f7f7f7;
		}

		.value {
			height: 80rpx;
			display: flex;
			align-items: center;
			justify-content: space-between;
			background-color: #fff;
			border: 1px solid #bbbbb9;
			border-radius: 60rpx;
			padding: 0 50rpx;
			box-sizing: border-box;
			&-left {
				
			}
			&-date {
				height: 100%;
				display: flex;
				align-items: center;
				&-text {
					color: #e88e6e;
					padding-right: 30rpx;
				}
			}
			/deep/ .u-input {
				flex: .6;
			}
			/deep/ .uni-input-input {
				text-align: center;
			}
		}
	}

	.address-list {
		margin-top: 30rpx;
		background-color: #fff;

		.scroll {
			height: 660rpx;
		}

		.address-cell {
			margin-top: 30rpx;
			height: 128rpx;
			border-bottom: solid 2px #f7f7f7;
			display: flex;
			align-items: center;
			justify-content: space-between;

			.label {
				width: 400rpx;
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
	}
</style>