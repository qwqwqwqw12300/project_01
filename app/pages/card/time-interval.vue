<!--
* @Author: zhanghaowei
* @Date: 2023年2月28日14:43:51
* @FilePath: 
* @Description: 时段禁用
-->

<template>
	<app-body title="时段禁止">
		<!-- <app-logo color="#353535" text="时段禁止"></app-logo> -->
		<CardTitle :title="'禁用运行中'" :content="'禁用时间拒绝所有电话和短信，但可查看时间和定位'"
			:backGroundImg="'/static/images/prohibit-moving.png'"></CardTitle>
		<view style="margin-top: 50rpx;padding-bottom: 50rpx;">
			<template v-if="list.length">
				<view class="ui-cell" @click="toJump(item)" v-for="(item,index) in list" :key="index" style="height: 75px;">
					<view class="ui-cell-content">
						<view class="ui-cell-style">
							<view class="ui-cell-title">{{item.periodDisableTag}}</view>
							<view>
								<u-icon name="arrow-right"></u-icon>
							</view>
						</view>
						<view class="ui-cell-style">
							<view class="ui-cell-font">
								<view>{{item.beginTime}} 至 {{item.endTime}}</view>
								<view>{{getperiodtext(item.period)}}</view>
							</view>
							<u-switch @click.native.stop="handleSwitch(item)" v-model="item.enable" activeValue="1"
								inactiveValue="0" activeColor="#FEAE43" inactiveColor="rgb(138, 138, 138)" size="20">
							</u-switch>
						</view>
					</view>
				</view>
			</template>
			<view class="list-empty" v-else>
				<u-empty mode="list" text="暂无数据"></u-empty>
			</view>
		</view>
		<view class="ui-div"></view>
		<view class="ui-btn">
			<view class="box">
				<button class="default" @click="toJumpTime">新增时间段</button>
			</view>
		</view>
	</app-body>
</template>

<script>
	import CardTitle from '@/components/card-title/card-title.vue';
	import {
		GetPeriodDisableList,
		PostSetPeriodDisable
	} from '@/common/http/api';
	import {
		mapState,
	} from 'vuex';
	export default {
		components: {
			CardTitle
		},
		computed: {
			...mapState({
				/**所有家庭列表**/
				deviceInfo: state => state.deviceInfo
			}),

		},
		data() {
			return {
				flag: '0', //0关闭，1开启
				list: []
			};
		},
		mounted() {
			console.log(this.deviceInfo, 'this.deviceInfo')
		},
		methods: {
			getperiodtext(jsm){
				if(!jsm) return
				let periods = jsm.split(',')
				let text = ''
				if(periods.length == 7){
					return '每天'
				}
				if(periods.length == 2&&periods[0] == 0&&periods[1] == 6){
					return '周末'
				}
				if(periods.length == 5&&periods[0] == 1&&periods[1] == 2&&periods[2] == 3&&periods[3] == 4&&periods[4] == 5){
					return '工作日'
				}
				for (let i = 0; i < periods.length; ++i) {
					if(periods[i] == 0){
						text+='周日，'
					}
					if(periods[i] == 1){
						text+='周一，'
					}
					if(periods[i] == 2){
						text+='周二，'
					}
					if(periods[i] == 3){
						text+='周三，'
					}
					if(periods[i] == 4){
						text+='周四，'
					}
					if(periods[i] == 5){
						text+='周五，'
					}
					if(periods[i] == 6){
						text+='周六，'
					}
				}
				text = text.substring(0,text.length-1)
				return text
			},
			initData() {
				GetPeriodDisableList({
					deviceId: this.deviceInfo.deviceId
				}).then(res => {
					console.log(res, 'res')
					this.list = res.data
					for (var i = 0; i < this.list.length; i++) {
						
					}
				})
			},
			handleSwitch(item) {
				PostSetPeriodDisable({
					...item
				}).then(res => {
					uni.$u.toast(res.msg)
					setTimeout(() => {
						this.initData()
					}, 1000);
				}, err => {
					item.enable = item.enable === '0' ? '1' : '0'; // 异常需要把状态重置
				})
			},
			toJump(item) {
				const list = JSON.stringify(item)
				uni.navigateTo({
					url: `/pages/equipment/info-interval?list=${list}`
				})
			},
			toJumpTime() {
				uni.navigateTo({
					url: '/pages/equipment/new-interval'
				})
			}
		},
		onShow() {
			this.initData()
		}
	};
</script>

<style lang="scss">
	.ui-cell {
		position: relative;
		width: 100%;
		height: 240rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		background-color: #fff;

		&::before {
			bottom: 0;
			left: 32rpx;
			content: '';
			height: 1rpx;
			width: calc(100% - 32rpx);
			position: absolute;
			background: #f2f2f2;
		}

		.ui-cell-content {
			width: 91.4%;
			height: 72%;
			display: flex;
			flex-direction: column;
			justify-content: space-between;

			.ui-cell-style {
				display: flex;
				align-items: center;
				justify-content: space-between;

				.ui-cell-font {
					display: flex;
					align-items: flex-start;
					flex-direction: column;
					font-size: 26rpx;
					color: #888888;
					letter-spacing: 0;
					line-height: 40rpx;
					font-weight: 400;
				}

				.ui-cell-title {
					font-size: 34rpx;
					color: #353535;
					line-height: 34rpx;
					font-weight: 400;
				}
			}
		}
	}

	.ui-div {
		height: 90rpx;
	}

	.ui-btn {

		.box {
			width: 100%;
			position: fixed;
			z-index: 999;
			bottom: 0;
			left: 0;

		}

		button {
			border-radius: 0rpx !important;
		}
	}

	.list-empty {
		width: 100%;
		height: 500rpx;
		display: flex;
		justify-content: center;
		align-items: center;
	}
</style>