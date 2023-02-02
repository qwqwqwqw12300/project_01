<!--
* @Author: zhanch
* @Date: 2022-12-27 15:53:29
* @FilePath: /pages/index/index.vue
* @Description: 应用首页
-->

<template>
	<view @click="closeDevice">
		<app-body :hideTitle="true">
			<view class="ui-scan">
				<view class="ui-sys-message">
					<u-badge :isDot="!!readInfo.length" absolute :offset="[0, 0]"></u-badge>
					<u-icon name="chat" @click="goPage('/pages/service/system-messages')" class="active" color="#fff"
						size="50rpx"></u-icon>
				</view>
				<u-icon name="plus-circle" @click="addDevice" :stop="true" class="active" color="#fff" size="45rpx">
				</u-icon>
				<!-- 下拉框 -->
				<view class="ui-select" v-if="isAddShow">
					<view class="ui-select-item active" @click="goPage('/pages/equipment/radar')">
						<u-icon name="../../static/images/leida.png" class="active" color="#fff" size="40rpx"></u-icon>
						<text>雷达波设备</text>
					</view>
					<view class="ui-select-item active" @click="goPage('/pages/equipment/monitor')">
						<u-icon name="../../static/images/shoubiao.png" class="active" color="#fff" size="40rpx">
						</u-icon>
						<text>监护手表</text>
					</view>
				</view>
				<!-- /下拉框 -->
			</view>
			<app-logo></app-logo>
			<!-- 家庭列表 -->
			<template v-if="familyList.length">
				<view class="ui-group" v-for="(familyItem, index) of familyList" :key="'family' + index">
					<view class="ui-title">
						<view>
							<text>{{familyItem.name}}</text>
							<!-- <text>共{{(familyItem.devices && familyItem.devices.length) || 0}}个设备</text>
							<text>在线{{ getOnlineData(familyItem) }}个设备</text> -->
						</view>
						<u-text @click="goPage('/pages/share/share?familyId='+ familyItem.familyId)"
							prefixIcon="share-square" size="28rpx" :align="'right'" :block="false" color="#fff"
							:iconStyle="{
							fontSize: '44rpx',
							color: '#fff'
						}" text="分享" v-if="familyItem.shareFlag === '2'"></u-text>
					</view>
					<view class="ui-device">
						<view class="ui-list" v-for="device of familyItem.devices" :key="device.deviceId">
							<view class="ui-list-box active" @click="goDeciveDetails(device)">
								<image src="../../static/images/device.png"></image>
								<text>{{device.name || '未命名设备'}}</text>
								<text>{{device.roomName || '--'}}</text>
								<view class="ui-list-static">
									<u-icon :name="device.onlineFlag === '1' ? 'wifi' : 'wifi-off'"
										:color="device.onlineFlag === '1' ? '#0dab1c' : '#ff4800'" size="28"></u-icon>
								</view>
								<u-badge v-if="device.msgNum" :offset="[-9, 0]" :value="device.msgNum" absolute>
								</u-badge>
							</view>
						</view>
						<!-- 空房间 -->
						<view class="ui-list">
							<view class="ui-list-box active">
								<u-icon name="info-circle" size="90rpx"></u-icon>
								<text class="ui-link">点击绑定设备</text>
								<text>空房间</text>
							</view>
						</view>
						<!-- /空房间 -->
						<!-- 新增房间 -->
						<view class="ui-list">
							<view class="ui-list-box active">
								<u-icon name="plus" size="70rpx"></u-icon>
							</view>
						</view>
						<!-- /新增房间 -->
					</view>
				</view>
			</template>
			<!-- /家庭列表 -->
			<!-- 空户 -->
			<template v-else>
				<view class="ui-default">
					<image src="../../static/images/tb.png" mode=""></image>
					<button @click="addStep">创建家庭</button>
				</view>
			</template>
			<!-- /空户 -->
			<add-step ref="addStepRef"></add-step>
		</app-body>
	</view>


</template>
<script>
	import {
		getDeviceList,
		getFamilyList,
		GetReadInfo
	} from '../../common/http/api';
	import {
		mapState,
		mapActions,
		mapGetters
	} from 'vuex';
	export default {
		data() {
			return {
				/**是否展示添加弹窗**/
				isAddShow: false,
				/**公告状态**/
				readInfo: []
			}
		},
		computed: {
			...mapState({
				/**所有家庭列表**/
				familyList: state => state.familyList
			}),
			...mapGetters(['filterDevice']),
			getOnlineData() {
				return function(item) {
					return item.devices.reduce((pre, cur) => {
						return pre + (cur.onlineFlag === '1' ? 1 : 0)
					}, 0)
				}

			}
		},
		onShow() {
			Promise.all([
				this.getAllFamily(),
				this.getAllDevices(),
				this.getReadInfo(),
				this.getPushMsgState()
			]);
		},
		methods: {
			...mapActions(['getAllFamily', 'getAllDevices', 'getPushMsgState', 'getAllRoom']),
			/**
			 * 打开添加按钮
			 */
			addDevice() {
				setTimeout(() => {
					this.isAddShow = true;
				}, 10);
			},
			/**
			 * 关闭添加按钮
			 */
			closeDevice() {
				this.isAddShow = false;
			},
			/**
			 * 页面跳转
			 */
			goPage(url) {
				uni.navigateTo({
					url
				});
			},
			/**
			 * 跳转设备详情
			 */
			goDeciveDetails(info) {
				this.$store.commit('setDeviceInfo', info);
				let url;
				if (info.type === '0') { // 雷达波
					url = '/pages/equipment/radar-detail';
				} else { // 监控设备
					url = '/pages/equipment/watch-detail';
				}
				this.goPage(url);

			},

			/**
			 * 添加家庭
			 */
			addStep() {
				this.$refs.addStepRef.open();
			},

			/**
			 * 获取公告状态
			 */
			getReadInfo() {
				return new Promise(resolve => {
					GetReadInfo().then(res => {
						this.readInfo = res.data;
						resolve();
					});
				})

			}
		}
	}
</script>

<style lang="scss">
	.ui-scan {
		display: flex;
		position: absolute;
		right: 24rpx;
		top: calc(90rpx);
		z-index: 2;

		.ui-sys-message {
			position: relative;

		}

		&>* {
			margin-left: 20rpx;
		}

		.ui-select {
			position: absolute;
			top: 70rpx;
			right: -12rpx;
			width: 228rpx;
			height: 258rpx;
			border-radius: 20rpx;
			filter: drop-shadow(0 0 5px rgba(7, 5, 5, 0.34));
			background-image: linear-gradient(-36deg, #f6ba33 0%, #f2bd6b 52%, #f6d7b0 100%);
			border: 1px solid #ffcb3d;
			padding: 16rpx;

			.ui-select-item {
				// margin-top: 16rpx;
				padding: 15rpx 0;
				display: flex;
				font-size: 26rpx;
				color: #fff;
				border-bottom: 1px solid #fff;

				text {
					display: inline-block;
					margin-left: 20rpx;
				}
			}
		}
	}

	.ui-group {
		margin-top: 56rpx;
		width: 100%;
		display: inline-flex;
		flex-direction: column;
		align-items: center;

		.ui-title {
			box-sizing: border-box;
			padding: 0 32rpx;
			display: flex;
			align-items: center;
			justify-content: space-between;
			height: 80rpx;
			width: calc(100% - 74rpx);
			border-radius: 40px;
			overflow: hidden;
			color: #fff;
			background-color: rgba(0, 0, 0, 0.4);
			font-size: 30rpx;

			text {
				margin-right: 10rpx;
			}
		}

		.ui-device {
			display: inline-block;
			width: calc(100% - 128rpx);
			margin-top: 74rpx;

			& .ui-list {
				margin-bottom: 50rpx;
				float: left;
				height: 258rpx;
				width: 50%;
				text-align: center;

				.ui-list-box {
					position: relative;
					height: 100%;
					width: 258rpx;
					display: inline-flex;
					align-items: center;
					justify-content: center;
					flex-direction: column;
					border-radius: 10px;
					filter: drop-shadow(7.824px 10.382px 8px rgba(7, 5, 5, 0.08));
					background-image: linear-gradient(96deg, #f5f5f5 0%, #e5e5e5 100%);

					image {
						height: 90rpx;
						width: 90rpx;
					}

					text {
						display: inline-block;
						margin-top: 15rpx;
						font-size: 22rpx;

						&:nth-child(2) {
							font-size: 30rpx;
						}
					}

					.ui-list-static {
						position: absolute;
						bottom: 10rpx;
						right: 12rpx;
					}
				}

			}
		}
	}

	.ui-default {
		width: 100%;
		box-sizing: border-box;
		display: inline-block;
		margin-top: 300rpx;
		padding: 0 73rpx;
		text-align: center;

		image {
			max-width: 305rpx;
			max-height: 295rpx;
			margin-bottom: 73rpx;
		}

		bottom: {
			margin-top: 73rpx;
		}
	}

	.ui-link {
		color: #0094ff;
	}
</style>
