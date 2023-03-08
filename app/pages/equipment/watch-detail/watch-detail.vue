<!-- 手表设备详情 -->
<template>
	<app-body :needService="false">
		<view class="ui-banner">
			<swiper class="ui-swiper" circular :indicator-dots="true" :autoplay="false" @change="swiperChange"
				:current="current">
				<swiper-item>
					<device-swiper></device-swiper>
				</swiper-item>
			</swiper>
		</view>
		<view class="ui-tab">
			<view class="ui-tabs">
				<view class="ui-tabs-item" v-for="item in tabList" :key="item.key">
					<text @tap="handleTab(item.key)" :class="{'active': tabKey === item.key }">{{ item.name }}</text>
				</view>
			</view>
		</view>
		<view class="ui-content">
			<component ref="comRef" :is="tabKey" :key="tabKey"></component>
		</view>
	</app-body>
</template>

<script>
	import DeviceSwiper from './components/device-swiper.vue'
	import GetPosition from './components/get-position.vue'
	import MsgList from './components/msg-list.vue'
	// import {
	// 	getMessage,
	// 	PostMessageDeatil
	// } from '../../common/http/api';
	export default {
		components: {
			DeviceSwiper,
			GetPosition,
			MsgList,
		},
		data() {
			return {
				current: 0,
				tabKey: 'MsgList',
				tabList: [{
					key: 'MsgList',
					name: '未处理预警'
				}, {
					key: 'GetPosition',
					name: '获取定位'
				}],
			}
		},
		computed: {},
		mounted() {

		},
		methods: {
			swiperChange() {

			},
			handleTab(index) {
				this.tabKey = index
			}
		}
	};
</script>

<style lang="scss" scoped>
	.active {
		font-size: 40rpx !important;
		color: #353535 !important;
		font-weight: 550 !important;
	}

	.ui-banner {
		padding: 0 20rpx;
		.ui-swiper {
			height: 320rpx;
		}
	}

	.ui-tab {
		display: flex;
		align-items: center;
		padding: 0 20rpx;

		.ui-tabs {
			display: flex;
			flex-direction: row;

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

		.ui-action {
			display: flex;
			align-items: center;

			text {
				height: 50rpx;
				border-left: 1px solid rgba(53, 53, 53, 1);
				margin-right: 30rpx;
			}
		}
	}
</style>
