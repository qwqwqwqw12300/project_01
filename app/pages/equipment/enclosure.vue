<!--
* @Author: zhanch
* @Date: 2023-01-10 10:37:04
* @FilePath: /pages/equipment/enclosure.vue
* @Description: 电子围栏
-->

<template>
	<app-body :needService="false" leftText="当前定位">
		<app-logo text="设置电子围栏"></app-logo>
		<view class="ui-map">
			<view class="ui-map-box">
				<map id="map" ref="map" style=" width: 100%; height: 600rpx;" :latitude="latitude"
					:longitude="longitude" :markers="covers" :circles="circles">
				</map>
			</view>
			<view class="ui-site">
				<u-text prefixIcon="map-fill" iconStyle="font-size: 36rpx" size="30rpx" color="#444" text="请输入地址">
				</u-text>
				<view class="ui-input">
					<text :class="{ place: !siteInfo }" @click="goSearch">{{siteInfo || '请输入地址'}}</text>
				</view>

			</view>
			<view class="ui-setting">
				<view class="ui-setting-title">
					<u-icon name="setting" size="36rpx"></u-icon>
					<text>设置半径长度</text>
					<text>{{sliderValue}}米</text>
				</view>
				<u-slider @change="onSlider" max="500" v-model="sliderValue" activeColor="#eeaa3d" blockColor="#eeaa3d"
					inactiveColor="#c0c4cc" />
			</view>
			<view class="ui-btn">
				<button @click="handleSubmit">提交</button>
			</view>
		</view>
	</app-body>
</template>

<script>
	import {
		PostAddFence,
	} from '@/common/http/api.js';
	import {
		mapState
	} from 'vuex';
	import {
		push
	} from '@/common/sdk/push.js';
	export default {
		data() {
			return {
				/*地址查询是否展示**/
				popShow: false,
				/**地址查询内容**/
				search: '',
				/**地址信息**/
				siteInfo: '',
				id: 0, // 使用 marker点击事件 需要填写id
				title: 'map',
				latitude: 39.909,
				longitude: 116.39742,
				covers: [],
				circles: [],
				// covers: [{
				// latitude: 39.909,
				// longitude: 116.39742,
				// 	iconPath: '../../../static/location.png'
				// }, {
				// latitude: 39.90,
				// longitude: 116.39,
				// iconPath: '../../../static/location.png'
				// }],
				sliderValue: 200,
				mapSearch: null,
				registrationId: '',
			}
		},
		created() {
			this.mapSearch = uni.createMapContext("map", this);
			for (let item in this.mapSearch) {
				console.log(item, 'item');
			}
			console.log(this.mapSearch, 'this.mapSearch');
			this.handleGetLocation()
			this.getRegistrationID()
		},
		computed: {
			...mapState({
				deviceId: state => state.deviceInfo.deviceId
			})
		},
		methods: {
			close() {
				this.popShow = false;
			},
			/**
			 * 保存
			 */
			handleSubmit() {
				const {
					deviceId,
					siteInfo: address,
					longitude,
					latitude,
					sliderValue,
					registrationId,
				} = this
				if (sliderValue === 0) {
					return uni.$u.toast('半径长度大于0')
				}
				PostAddFence({
					deviceId,
					address,
					longitude,
					latitude,
					radius: sliderValue + '',
					deviceNo: registrationId,
				}).then(res => {
					uni.navigateBack()
					uni.$u.toast(res.msg)
				})
			},
			/**
			 * 获取当前定位
			 */
			handleGetLocation() {
				uni.showLoading({
					title: '加载中'
				})
				uni.getLocation({
					geocode: true,
					type: 'gcj02',
					success: (res) => {
						const {
							latitude,
							longitude,
							address: {
								province,
								city,
								district,
								street,
								streetNum,
								poiName
							},
						} = res
						this.latitude = latitude
						this.longitude = longitude
						this.siteInfo = province + city + district + street + streetNum + poiName
						this.covers[0] = {
							latitude,
							longitude,
							iconPath: '../../../static/location.png'
						}
						this.circles[0] = {
							latitude,
							longitude,
							color: '#E51860',
							strokeWidth: 1,
							radius: this.sliderValue,
							fillColor: '#E5186020'
						}
						this.$forceUpdate()
						uni.hideLoading()
					},
					false: (res) => {
						console.log(res, 'error')
						uni.hideLoading()
					}
				})
			},

			/**
			 * 跳转搜索
			 */
			goSearch() {
				uni.navigateTo({
					url: '/pages/equipment/search'
				})
			},
			/**
			 * 半径滑动
			 */
			onSlider(e) {
				if (this.circles.length) {
					this.circles[0].radius = e
					this.circles = [...this.circles]
				}
			},
			/**
			 * 获取登录设备注册号
			 */
			getRegistrationID() {
				push.getRegistrationID().then(res => {
					this.registrationId = res
				});
			},
		},
		onShow() {
			uni.$on('searchData', data => {
				const {
					province,
					city,
					district,
					address,
					location: {
						latitude,
						longitude
					}
				} = data
				this.siteInfo = province + city + district + address
				this.latitude = latitude
				this.longitude = longitude
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
					radius: this.sliderValue,
					fillColor: '#df7d7f21'
				}
				this.$forceUpdate()
			})
		}
	}
</script>

<style lang="scss">
	.ui-map {
		box-sizing: border-box;
		width: 100vw;
		padding: 43rpx 50rpx;

		.ui-map-box {
			border-radius: 20rpx;
			filter: drop-shadow(1.805rpx 2.396rpx 19rpx rgba(7, 5, 5, 0.18));
			margin-bottom: 30rpx;
		}

		.ui-site {
			margin-top: 50rpx;
		}

		.ui-input {
			width: calc(100% - 100rpx);
			margin: 0 30rpx;
			padding: 30rpx 16rpx 20rpx 16rpx;
			border-bottom: 1rpx solid #e6e6e6;

			>text {
				display: inline-block;
				font-size: 28rpx;
				color: #444;

				&.place {
					color: #999999;
				}
			}
		}

		.ui-setting {
			margin-top: 50rpx;

			.ui-setting-title {
				display: flex;
				align-items: center;

				flex-direction: row;

				text {
					display: inline-block;
					margin-left: 10rpx;
					font-size: 30rpx;
					color: #444444;

					&:nth-child(3) {
						color: #999999;
						margin-left: 20rpx;
					}
				}
			}
		}

		.ui-btn {
			margin-top: 30rpx;
		}
	}
</style>
