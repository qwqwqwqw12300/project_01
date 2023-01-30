<!--
* @Author: zhanch
* @Date: 2023-01-10 09:47:15
* @FilePath: /pages/equipment/map.vue
* @Description: 地图与电子围栏
-->

<template>
	<app-body leftText="设备管理" :needService="false">
		<app-logo text="获取当前定位"></app-logo>
		<view class="ui-map">
			<view class="ui-map-box"><map style="width: 100%; height: 600rpx;" :latitude="latitude"
					:longitude="longitude" :markers="covers" :circles="circles"></map></view>
			<u-text prefixIcon="map-fill" iconStyle="font-size: 40rpx" text="当前地址"></u-text>
			<text>{{ address }}</text>
		</view>
		<view class="ui-btn-group">
			<button class="wd-sms" @click="getListState">立即获取定位</button>
			<button @click="goEnclosure">设置电子围栏</button>
		</view>
	</app-body>
</template>

<script>
	import {
		GetDeviceSize
	} from '../../common/http/api';
	import {
		mapState
	} from 'vuex';
	export default {
		data() {
			return {
				id: 0, // 使用 marker点击事件 需要填写id
				title: 'map',
				latitude: 39.909,
				longitude: 116.39742,
				address: '',
				covers: [],
				circles: [],
			};
		},
		computed: {
			...mapState({
				deviceInfo: state => state.deviceInfo
			})
		},
		onShow() {
			this.getListState();
		},
		methods: {
			goEnclosure() {
				uni.navigateTo({
					url: '/pages/equipment/enclosure'
				});
			},

			/**
			 * 获取定位
			 */
			getListState() {
				// const {
				// 	deviceId,
				// 	familyId,
				// 	roomId
				// } = this.deviceInfo;
				GetDeviceSize({
					deviceId: this.deviceInfo.deviceId,
				}).then(res => {
					console.log(res.data.nowLoacation, 'fdd')
					const {
						longitude,
						latitude,
						radius,
						address
					} = JSON.parse(res.data.nowLoacation)
					this.address = address
					this.longitude = longitude
					this.latitude = latitude,
						this.covers[0] = {
							latitude,
							longitude,
							iconPath: '../../../static/location.png'
						}
					this.circles[0] = {
						latitude,
						longitude,
						color: '#d81c20',
						strokeWidth: 1,
						radius: radius || 200,
						fillColor: '#df7d7f21'
					}

					this.$forceUpdate()
				});
			}
		}
	};
</script>

<style lang="scss">
	.ui-map {
		box-sizing: border-box;
		width: 100%;
		padding: 43rpx 50rpx;

		.ui-map-box {
			border-radius: 20rpx;
			filter: drop-shadow(1.805px 2.396px 19px rgba(7, 5, 5, 0.18));
			margin-bottom: 30rpx;
		}

		text {
			display: inline-block;
			margin-top: 15rpx;
			padding-left: 10rpx;
			font-size: 30rpx;
			color: #565353;
		}
	}

	.ui-btn-group {
		margin-top: 60rpx;

		button {
			margin-top: 40rpx;
			width: 342rpx;
			height: 101rpx;
		}
	}
</style>
