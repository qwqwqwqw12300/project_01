<template>
	<view>
		<polygon-map @sendMsg="savepPoints" :record="mapInfo"></polygon-map>
		<touch-popup :minHeight="0.1" :maxHeight="0.25" :touchHeight="64" radius="30rpx">
			<view class="title">
				电子围栏设置
			</view>
			<view class="ui-btn">
				<button class="default" @click="handleSubmit">提交</button>
			</view>
		</touch-popup>
	</view>
</template>

<script>
	import {
		mapState,
	} from 'vuex';
	import {
		isIos,
	} from '@/common/utils/util';
	import {
		GetFenceInfo,
		PostAddFence,
	} from '@/common/http/api.js';
	export default {
		data() {
			return {
				points: [],
				mapInfo: {
					longitude: '',
					latitude: '',
					points: [],
				}
			}
		},
		computed: {
			...mapState({
				deviceInfo: state => state.deviceInfo
			}, ),
		},
		mounted() {
			this.handleInit()
		},
		methods: {
			savepPoints(data) {
				this.points = data
			},
			handleInit() {
				GetFenceInfo({
					deviceId: this.deviceInfo.deviceId,
				}).then(res => {
					console.log(res, '00-----------')
					if (!res.data.length) return this.handleGetLocation()
					const data = res.data[0]
					if (data.fenceType === 'polygon') {
						const res = data.points.split(';').map(n => {
							return n.split(',')
						})
						this.mapInfo = {
							longitude: res[0][0],
							latitude: res[0][1],
							points: res,
						}
					} else {
						this.handleGetLocation()
					}
				})
			},
			/**
			 * 获取当前定位
			 */
			handleGetLocation() {
				uni.showLoading({
					title: '加载中'
				})
				try {
					uni.getLocation({
						geocode: true,
						type: isIos() ? 'wgs84' : 'gcj02',
						success: (res) => {
							const {
								latitude,
								longitude,
							} = res
							this.mapInfo = {
								latitude,
								longitude,
								points: []
							}
							uni.hideLoading()
						},
						false: (res) => {
							console.log(res, 'error')
							uni.hideLoading()
						}
					})
				} catch (e) {
					console.log(e, '代码报错--------');
					//TODO handle the exception
				}

			},
			handleSubmit() {
				console.log(this.points, '--------')
				const res = this.points.map(n => {
					const {
						longitude,
						latitude
					} = n
					return `${longitude},${latitude}`
				}).join(';')
				PostAddFence({
					fenceType: 'polygon',
					deviceNo: this.deviceInfo.no,
					deviceId: this.deviceInfo.deviceId,
					points: res,
				}).then(res => {
					uni.$u.toast(res.msg)
					setTimeout(() => {
						uni.navigateBack()
					}, 500);
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.title {
		height: 120rpx;
		display: flex;
		align-items: center;
		border-bottom: solid 2px #f7f7f7;
		font-size: 34rpx;
		color: #353535;
		font-weight: 550;
	}

	.ui-btn {
		margin-top: 50rpx;
	}
</style>
