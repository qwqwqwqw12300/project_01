<!--
* @Author: zhanghaowei
* @Date: 2023年3月1日14:43:51
* @FilePath: 
* @Description: 位置守护
-->

<template>
	<app-body>
		<app-logo color="#353535" text="位置守护"></app-logo>
		<CardTitle :title="'到达提醒'" :content="'迟到APP有报警'" :backGroundImg="'/static/images/arrive-guard.png'"></CardTitle>
		<view style="margin-top: 50rpx;">
			<template v-if="list.length">
				<view class="ui-cell" v-for="(item,index) in list" :key="index" @click="toJump(item)">
					<view class="ui-cell-content">
						<view class="ui-cell-style">
							<view class="ui-cell-title">{{item.jobName}}</view>
							<view>
								<u-icon name="arrow-right"></u-icon>
							</view>
						</view>
						<view class="ui-cell-style">
							<view class="ui-cell-font">
								<view>{{item.firstDate}} 至 </view>
								<view>{{item.lastDate}}</view>
							</view>
							<u-switch @click.native.stop="handleSwitch(item)" v-model="item.enable" activeValue="1"
								inactiveValue="0" activeColor="#FEAE43" inactiveColor="rgb(138, 138, 138)" size="20"> >
							</u-switch>
						</view>
					</view>
				</view>
			</template>
			<view class="list-empty" v-else>
				<u-empty mode="list" text="暂无数据"></u-empty>
			</view>
		</view>
		<view class="ui-btn"><button class="default" @click="toJumpTime">新增守护位置</button></view>
	</app-body>
</template>

<script>
	import CardTitle from '@/components/card-title/card-title.vue';
	import {
		GetLocationGuardList,
		PostSetLocationGuard
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
				list: [{
						title: '舞蹈课'
					},
					{
						title: '公交站'
					}
				]
			};
		},
		mounted() {

		},
		methods: {
			initData() {
				GetLocationGuardList({
					deviceId: this.deviceInfo.deviceId
				}).then(res => {
					this.list = res.data
				})
			},
			handleSwitch(item) {
				PostSetLocationGuard({
					...item
				}).then(res => {
					console.log(res, 'res')
					uni.$u.toast(res.msg)
					setTimeout(() => {
						this.initData()
					}, 1000);
				}, err => {
					item.enable = item.enable === '1' ? '0' : '1';
				})
			},
			toJump(item) {
				const list = JSON.stringify(item)
				uni.navigateTo({
					url: `/pages/card/info-guard?list=${list}`
				})
			},
			toJumpTime() {
				uni.navigateTo({
					url: '/pages/card/new-guard'
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

	.ui-btn {
		width: 100%;
		position: fixed;
		bottom: 0;
		left: 0;
		z-index: 999;

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