<template>
	<app-body :bg="false" title="抬腕亮屏设置">
		<view class="ui-form">
			<view class="u-slot-value">
				<text>抬腕亮屏</text>
				<u-switch space="2" v-model="checks" activeValue="1" inactiveValue="0" @change="change" size="20"
					activeColor="#FEAE43" inactiveColor="rgb(230, 230, 230)"></u-switch>
			</view>
			<view class="u-slot-value" style="margin: 0;" @click="showtime">
				<text>时间段设置</text>
				<view class="arrow">
					<text>{{aboveTime}}:00-{{underTime}}:00</text>
					<u-icon name="arrow-right"></u-icon>
				</view>
			</view>
		</view>
		<smh-time-ranges v-if="show" :time="['09','0','0','21','0']" @confrim="confrim"
			@cancel="cancel"></smh-time-ranges>
		<view class="ui-button">
			<button @click="send" class="default">
				保存
			</button>
		</view>
	</app-body>
</template>

<script>
	import {
		mapState,
	} from 'vuex';
	import {
		PostSetLcdStatus,
	} from '@/common/http/api.js';
	export default {
		data() {
			return {
				checks: '1',
				aboveTime: '09',
				underTime: '21',
				show: false,
				deviceId:''
			}
		},
		onLoad(options) {
			this.deviceId = options.deviceId
			
		},
		methods: {
			confrim(e) {
				this.aboveTime = e.aboveTime;
				this.underTime = e.underTime
				this.showtime()
			},
			cancel(e) {
				console.log(e) //取消事件 =>12:30-17:30
			},
			showtime() {
				this.show = !this.show
			},
			send() {
				PostSetLcdStatus({
					deviceId: this.deviceId,
					open: Boolean(this.checks),
					startHour:this.aboveTime,
					endHour:this.underTime
				}).then(res => {
					uni.$u.toast(res.msg)
				}, err => {
					uni.$u.toast(err.msg)
				})
			},
		}
	}
</script>

<style lang="scss" scoped>
	.ui-logo {
		margin-top: 20rpx;
		background: #ffffff;
		padding-bottom: 120rpx;
	}

	.ui-form {
		padding: 30rpx;
		background: #ffffff;

		.u-slot-value {
			display: flex;
			align-items: center;
			justify-content: space-between;
			margin-bottom: 20px;
		}

		.arrow {
			display: flex;
			align-items: center;
		}
	}

	.ui-button {
		width: calc(100% - 64rpx);
		padding: 50rpx 32rpx;
		position: fixed;
		left: 0;
		bottom: 10px;
		
	}
</style>