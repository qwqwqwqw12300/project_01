<template>
	<u-action-sheet :closeOnClickOverlay="true" :closeOnClickAction="true" :safeAreaInsetBottom="safeShow"
		@close="closeHandle" :show="isShow" cancelText="取消">
		<view class="list_box">
			<scroll-view scroll-y="true" style="height: 610rpx;">
				<view @click="sheetSelect(item)" class="items" v-for="(item, index) of addHandle.list"
					:key="'sheet' + index">
					<image :src="item.icon" class="active">
					</image>
					<view class="text">{{item.name}}</view>
				</view>
			</scroll-view>
		</view>
	</u-action-sheet>
</template>

<script>
	import {
		isProd
	} from '@/common/utils/util.js'
	export default {
		props: {
			isShow: {
				default: false
			},
			safeShow: {
				default: false
			}
		},
		data() {
			return {
				addHandle: {
					show: false,
					list: [{
							name: '跌倒检测器',
							icon: '/static/images/leida-nm.png',
							url: '/pages/equipment/add/radar'
						},
						{
							name: '电子牵挂卡',
							icon: '/static/images/dzqgk.png',
							url: '/pages/card/add-card?type=card',
							// url: '/pages/equipment/watch-detail/watch-detail',
						},
						{
							name: '健康手表',
							icon: '/static/images/new-watch-device.png',
							url: '/pages/watch/add-watch'
							// url: '/pages/watch/add-watch?type=2'
						},
						{
							name: 'SOS设备',
							icon: '/static/images/sos/sos-equipment_2.png',
							url: '/pages/card/add-card?type=sos'
						},
						// {
						// 	name: 'H102智能手表',
						// 	icon: '/static/images/new-watch-device.png',
						// 	url: '/pages/watch/add-watch?type=4'
						// }
					]
				},
			}
		},
		methods: {
			/**
			 * 创建设备
			 */
			sheetSelect({
				url,
				name
			}) {
				this.$emit('cancle')
				uni.navigateTo({
					url
				});
			},
			closeHandle() {
				this.$emit('cancle')
			}
		}
	}
</script>

<style lang="scss">
	.list_box{
		background-color: rgb(234, 234, 236);
		padding: 30rpx;
		border-radius: 10px 10px 0 0;
	}
	.items {
		background-color: #FFFFFF;
		width: calc(50% - 11rpx);
		display: inline-block;
		text-align: center;
		float: left;
		border-radius: 20rpx;
		padding: 25rpx 0;
		margin-bottom: 20rpx;
		.active{
			margin: 0 auto;
			width: 100rpx;
			height: 100rpx;
		}
		.text {
			margin-left: 20rpx;
			font-size: 32rpx;
			color: #000000;
			text-align: center;
			line-height: 34rpx;
			font-weight: 400;
		}
	}
	.items:nth-child(2n+1){
		margin-right: 20rpx;
	}
	::v-deep .u-action-sheet__cancel-text {
		font-size: 36rpx;
		color: #599FFF;
		line-height: 36rpx;
		font-weight: 500;
	}
	::v-deep .u-popup__content{
		border-radius: 10px 10px 0 0;
	}
</style>
