<template>
	<app-body leftIconColor="#000" :bodyStyle="{backgroundImage: 'linear-gradient(180deg, #FFEDC9 0%, #FFFFFF 100%)' }">
		<view class="ui-banner">
			<swiper class="ui-swiper" circular :indicator-dots="true" :autoplay="false" @change="swiperChange">
				<swiper-item v-for="item in deviceList" :key="item.deviceId">
					<device-swiper :record="item"></device-swiper>
				</swiper-item>
			</swiper>
		</view>
		<view class="ui-tabs">
			<view class="ui-tabs-item" v-for="item in tabList" :key="item.key">
				<text @tap="handleTab(item.key)" :class="{'active': tabKey === item.key }">{{ item.name }}</text>
			</view>
		</view>
		<view class="ui-content">
			<component :is="tabKey" :key="tabKey" :deviceInfo="swiperData"></component>
		</view>
	</app-body>
</template>

<script>
	import DeviceSwiper from './components/device-swiper.vue';
	import MsgList from './components/msg-list.vue';
	import RadarMonitor from './components/radar-monitor.vue';
	import {
		mapState,
	} from 'vuex';
	export default {
		components: {
			DeviceSwiper,
			MsgList,
			RadarMonitor,
		},
		data() {
			return {
				deviceList: [],
				swiperData: {},
				tabList: [{
					key: 'MsgList',
					name: '未处理预警'
				}, {
					key: 'RadarMonitor',
					name: 'AI人体感知'
				}],
				tabKey: 'MsgList',
			}
		},
		computed: {
			...mapState({
				/**所有家庭列表**/
				familyList: state => state.familyList,
			}, ),

		},
		mounted() {
			const familyId = this.$getCache('familyId')
			this.deviceList = this.familyList.find(n => {
				return familyId === n.familyId
			}).devices
			this.swiperData = this.deviceList[0]
			console.log(this.deviceList, '000')

		},
		methods: {
			handleTab(index) {
				this.tabKey = index
				console.log(this.swiperData, '9999')
			},
			swiperChange(val) {
				this.swiperData = this.deviceList[val.detail.current]
				this.tabKey = 'MsgList'
			},
		}
	}
</script>

<style lang="scss" scoped>
	.ui-banner {
		margin-top: 60rpx;
		padding: 0 20rpx;
	}

	.ui-swiper {
		height: 320rpx;
	}

	.active {
		font-size: 40rpx !important;
		color: #353535 !important;
		font-weight: 550 !important;
	}

	.ui-tabs {
		display: flex;
		flex-direction: row;
		padding: 0 20rpx;

		.ui-tabs-item {
			height: 90rpx;
			line-height: 90rpx;
			padding: 0 18rpx;

			text {
				font-size: 36rpx;
				color: #888888;
				font-weight: 500;
			}
		}
	}

	.ui-content {
		padding: 0 20rpx;
		margin-top: 20rpx;
		height: calc(100vh - 600rpx - var(--status-bar-height));
	}
</style>
