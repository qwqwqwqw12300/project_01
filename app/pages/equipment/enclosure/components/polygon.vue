<template>
	<view>
		<polygon-map @sendMsg="savepPoints"></polygon-map>
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
		PostAddFence2,
	} from '@/common/http/api.js';
	export default {
		data() {
			return {
				points: [],
			}
		},
		computed: {
			...mapState({
				deviceInfo: state => state.deviceInfo
			}, ),
		},
		methods: {
			savepPoints(data) {
				this.points = data
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
				console.log(res, 'resres')
				PostAddFence2({
					fenceType: 'polygon',
					deviceNo: this.deviceInfo.no,
					deviceId: this.deviceInfo.deviceId,
					points: res,
				}).then(res => {
					console.log(res, 'rrr')
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
