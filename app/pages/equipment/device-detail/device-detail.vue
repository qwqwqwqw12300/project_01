<template>
	<app-body :needService="false">
		<view class="ui-banner">
			<swiper class="swiper" circular :indicator-dots="true" previous-margin="50rpx" next-margin="50rpx"
				:autoplay="false" @change="swiperChange" :current="current">
				<swiper-item v-for="(item, index) in deviceList" :key="item.deviceId">
					<view :class="[current === index ? 'active' : 'noActive']">
						<component :is="getDeviveType(item.type)" :record="item"></component>
					</view>
				</swiper-item>
			</swiper>
		</view>
		<view class="ui-action">
			<view class="tabs">
				<template v-for="(item, index) in tabList">
					<view class="tabs-item" :key="index" v-show="item.show">
						<text @tap="handleTab(item.key)" :class="{ active: tabKey === item.key }">{{ item.name }}</text>
					</view>
				</template>
			</view>
			<view class="action" v-show="tabKey === 'MsgList'">
				<text></text>
				<u-icon name="/static/images/read-all.png" size="50rpx" @click="readMsgAll"></u-icon>
			</view>
		</view>
		<view class="ui-content">
			<component ref="comRef" :is="tabKey" :key="tabKey" :list="sosPositionList" :deviceInfo="swiperData" @refresh="onRefresh">
			</component>
		</view>
	</app-body>
</template>

<script>
	import CardSwiper from './components/card-swiper.vue'
	import RadarSwiper from './components/radar-swiper.vue'
	import WatchSwiper from './components/watch-swiper.vue'
	import CardPosition from './components/card-position.vue'
	import WatchPosition from './components/watch-position.vue'
	import SosSwiper from './components/sos-swiper.vue'
	import SosPosition from './components/sos-position.vue'
	import MsgList from './components/msg-list.vue'
	import RadarMonitor from './components/radar-monitor.vue'
	import WatchSwiperH from './components/watch-swiper-h.vue'
	import WatchPositionH from './components/watch-position-h.vue'
	import {
		getGatewaySubDeviceNum
	} from '@/common/http/api';
	import {
		mapState,
	} from 'vuex';
	export default {
		components: {
			CardSwiper,
			RadarSwiper,
			WatchSwiper,
			CardPosition,
			WatchPosition,
			SosSwiper,
			SosPosition,
			MsgList,
			RadarMonitor,
			WatchSwiperH,
			WatchPositionH
		},
		computed: {
			...mapState({
				/**所有家庭列表**/
				familyList: state => state.familyList,
				deviceInfo: state => state.deviceInfo
			}, ),
			getDeviveType() {
				return function(type) {
					return {
						0: 'RadarSwiper',
						1: 'CardSwiper',
						2: 'WatchSwiper',
						3: 'SosSwiper',
						4: 'WatchSwiperH'
					} [type]
				}
			},
			tabList() {
				return [{
					key: 'MsgList',
					show: true,
					name: '未处理预警'
				}, {
					key: 'RadarMonitor',
					show: this.swiperKey === '0',
					name: 'AI人体感知'
				}, {
					key: 'CardPosition',
					show: this.swiperKey === '1',
					name: '获取定位'
				}, {
					key: 'WatchPosition',
					show: this.swiperKey === '2',
					name: '获取定位'
				}, {
					key: 'SosPosition',
					show: this.swiperKey === '3',
					name: '关联设备'
				}, {
					key: 'WatchPositionH',
					show: this.swiperKey === '4',
					name: '获取定位'
				}]
			}
		},
		async onShow() {
			await this.$store.dispatch('GetContactsList');
			if(this.deviceInfo.type === '3') this.getList()
			const familyId = this.deviceInfo.familyId
			const res = this.familyList.find(n => {
				return familyId === n.familyId
			})
			console.log('resresresres',res)
			this.deviceList = res.devices.map(n => {
				n.shareFlag = res.shareFlag
				return n
			})
			this.current = this.deviceList.indexOf(this.deviceInfo)
			this.swiperData = this.deviceList[this.current]
			this.swiperKey = this.swiperData.type
		},
		data() {
			return {
				current: 0,
				tabKey: 'MsgList',
				swiperKey: '',
				deviceList: [],
				swiperData: {},
				sosPositionList: []
			}
		},
		methods: {
			swiperChange(item) {
				uni.$u.debounce(() => {
					this.current = item.detail.current
					this.swiperData = this.deviceList[item.detail.current]
					this.swiperKey = this.swiperData.type
					this.tabKey = 'MsgList'
					this.$store.commit('setDeviceInfo', this.swiperData);
				}, 500)
			},
			handleTab(key) {
				this.tabKey = key
			},
			readMsgAll() {
				if (this.$refs.comRef?.readMsgAll) {
					uni.showModal({
						title: '提示',
						content: `是否全部标记已读？`,
						success: res => {
							if (res.confirm) {
								this.$refs.comRef.readMsgAll()
							}
						}
					})
				}
			},
			async onRefresh() {
				const familyList = await this.$store.dispatch('getAllFamily');
				const res = familyList.find(n => {
					return this.deviceInfo.familyId === n.familyId
				})
				this.deviceList = res.devices.map(n => {
					n.shareFlag = res.shareFlag
					return n
				})
				this.deviceList = [...this.deviceList]
				this.swiperData = this.deviceList[this.current]
				this.swiperKey = this.swiperData.type
			},
			getList() {
				console.log('请求九宫格')
				getGatewaySubDeviceNum({
					deviceId: this.deviceInfo.deviceId,
				}).then(res => {
				console.log(res,'请求九宫格')
					this.sosPositionList = res.data
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.active {
		transition: all 0.1s;
		transform: scale(1);
	}

	.noActive {
		transition: all 0.1s;
		transform: scale(0.92);
	}

	.ui-banner {
		margin-top: 40rpx;
		// padding: 0 20rpx;

		.swiper {
			height: 320rpx;
		}
	}

	.ui-action {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 0 20rpx;

		.tabs {
			display: flex;
			flex-direction: row;

			.tabs-item {
				height: 90rpx;
				line-height: 90rpx;
				padding: 0 18rpx;

				text {
					font-size: 36rpx;
					color: #888888;
					font-weight: 500;
				}

				.active {
					font-size: 40rpx !important;
					color: #353535 !important;
					font-weight: 550 !important;
				}
			}
		}

		.action {
			display: flex;
			align-items: center;

			text {
				height: 50rpx;
				border-left: 1px solid rgba(53, 53, 53, 1);
				margin-right: 30rpx;
			}
		}
	}

	.ui-content {
		padding: 0 20rpx;
		margin-top: 20rpx;
		overflow: hidden;
		height: calc(100vh - 580rpx - var(--status-bar-height));
	}
</style>