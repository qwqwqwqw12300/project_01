<!--
* @Author: zhanch
* @Date: 2022-12-30 16:47:13
* @FilePath: /pages/myself/room-manage.vue
* @Description: 房间管理
-->

<template>
	<view id="roomMagege" style="font-weight: bold; ">
		<u-navbar :safeAreaInsetTop="true" :fixed="true" :leftIconColor="'#000'" :title="familyInfo.name" height="60px"
			:autoBack="true">
			<view class="action" @click="openFamilyEdit" slot="right">
				<u-icon name="/static/images/room-edit.png" size="28"></u-icon>
				<text>编辑</text>
			</view>
		</u-navbar>
		<app-body :bg="false" :Navbar="false">
			<!-- <view class="ui-logo"> -->
			<!-- <view class="logo-text">
					{{ familyInfo.name }}
				</view> -->

			<!-- </view> -->
			<view class="ui-menu">
				<!-- 房间 -->
				<template v-if="list.length || humanList.length">
					<view class="ui-menu-item" v-for="(item, index) in list" :key="'room' + index">
						<view class="item-box" @click="openRoomEdit(item)">
							<template v-if="item.devices.length">
								<view class="device-status">
									<text class="online" v-if="getDevices(item).onlineFlag == 1">在线</text>
									<text class="offline" v-else-if="getDevices(item).onlineFlag == 2">休眠</text>
									<text class="offline" v-else>离线</text>
								</view>
								<view class="device-info">
									<image :src="getImage(item.devices[0])"></image>
									<view class="detail">
										<text class="name">{{ item.name }}</text>
										<text class="position" v-if="item.devices.length">
											{{ item.devices[0].roomName + ' | ' + item.devices[0].location}}
										</text>
										<text class="position" v-else>
											未绑定设备
										</text>
									</view>
								</view>
							</template>
							<template v-else>
								<view class="device-info">
									<u-text :block="false" :text="item.name || '未命名房间'"
										:prefixIcon="getRoomIcon(item.roomType)" size="40rpx"
										:iconStyle="{height: '70rpx', width: '70rpx'}"></u-text>
								</view>
							</template>

							<view class="device-action">
								<text class="danger" @click.stop="onDelete(item.roomId)">删除</text>
								<text class="warn" v-if="!item.devices.length"
									@click.stop="binding(item, 'room')">绑定</text>
								<text class="orange" v-else @click.stop="unbinding(item.devices)">解绑</text>
							</view>
						</view>
					</view>
					<!-- 人员 -->
					<view class="ui-menu-item" v-for="(item, index) in humanList" :key="'human' + index">
						<view class="item-box" @click="goEdit(item)">
							<template v-if="item.devices && item.devices.length">
								<view class="device-status">
									<text class="online" v-if="getDevices(item).onlineFlag == 1">在线</text>
									<text class="offline" v-else-if="getDevices(item).onlineFlag == 2">休眠</text>
									<text class="offline" v-else>离线</text>
								</view>
								<view class="device-info">
									<image :src="getImage(item.devices[0])"></image>
									<view class="detail">
										<text class="name">{{ item.name }}</text>
										<text class="position" v-if="item.devices.length">
											{{ getDevices(item).name}}
										</text>
										<text class="position" v-else>
											未绑定设备
										</text>
									</view>
								</view>
							</template>
							<template v-else>
								<view class="device-info">
									<u-text :text="item.name || '未命名人员'" prefixIcon="../../static/images/add-person.png"
										size="40rpx" :iconStyle="{height: '70rpx', width: '70rpx'}"></u-text>
								</view>
							</template>
							<view class="device-action">
								<text class="danger" @click.stop="deleteHuman(item.humanId)">删除</text>
								<text class="warn" v-if="!item.devices || !item.devices.length"
									@click.stop="binding(item, 'human')">绑定</text>
								<text class="orange" v-else @click.stop="unbinding(getDevices(item))">解绑</text>
							</view>
						</view>
					</view>
					<!-- /人员 -->
				</template>
				<view class="list-empty" v-else>
					<u-empty mode="list" text="暂无数据"></u-empty>
				</view>
				<!-- /房间 -->

			</view>
			<view class="ui-btn"><button class="default" @click="add">添加</button></view>
			<room-pop ref="addRoom" :mode="roomMode" @update="handleInitList" />
			<family-pop ref="addFamily" mode="edit" @update="familyNext" :id="familyId" />
			<bind-device :payload="bindPayload" @next="handleInitList" ref="bindDev" />
		</app-body>
	</view>

</template>

<script>
	import {
		PostDelRoom,
		PostRoomList,
		PostEditRoom,
		relDevice,
		PostSelectTHumanListByFamilyId,
		PostDeleteTHumanByHumanIds,
		PostCareCardUnBind,
		PostWatchUnBind
	} from '@/common/http/api.js';
	import {
		mapState,
	} from 'vuex';
	import {
		assignDeep
	} from '../../common/utils/util';
	export default {
		data() {
			return {
				roomMode: 'add',
				/**房间列表**/
				list: [],
				/**人员列表**/
				humanList: [],
				/**家庭信息**/
				// familyInfo: {},
				bindPayload: {},
				familyId: ''
			};
		},
		onLoad(params) {
			this.familyId = params.id;
		},
		onShow() {
			this.handleInitList()
		},
		computed: {
			getDevices: () => {
				return (item) => {
					if (item.devices[0]) {
						return item.devices[0]
					}
					return {}

				}
			},
			...mapState({
				/**所有家庭列表**/
				familyInfo: function(state) {
					const familys = state.familyList.filter(item =>
						item.familyId == this.familyId
					);
					return familys[0] || {}
				}
			}),
			/**获取房间图标**/
			getRoomIcon: () => {
				return (type) => {
					let pic;
					switch (type) { //  0:其他、1:书房、2:客厅、3:卧室、4:浴室、5:厨房、6:餐厅
						case '1':
						case '2':
							pic = `../../static/images/index/room${type}.png`;
							break;
						default:
							pic = `../../static/images/index/room0.png`;
							break;
					}
					return pic;
				}
			},
			getImage() {
				return function(val) {
					return {
						0: '/static/images/leida-nm.png',
						1: '/static/images/dzqgk.png',
						2: '/static/images/watch-device.png',
						3: '/static/images/sos/sos-equipment_2.png',
						4: '/static/images/new-watch-device.png'
					} [val.type]
				}
			}
		},
		methods: {

			/**
			 * 修改房间
			 */
			openRoomEdit(item) {
				this.roomMode = 'edit';
				this.$nextTick(() => {
					this.$refs.addRoom.open({
						...item
					});
				});

			},

			/**
			 * 删除
			 */
			onDelete(roomId) {
				uni.showModal({
					title: '提示',
					content: '是否确认删除房间',
					success: res => {
						if (res.confirm) {
							PostDelRoom({
								roomId,
							}).then(res => {
								uni.$u.toast(res.msg)
								setTimeout(() => {
									this.handleInitList()
								}, 1000)
							})
						} else if (res.cancel) {
							console.log('用户点击取消');
						}
					}
				});
			},

			/**
			 * 删除人员
			 */
			deleteHuman(id) {
				uni.showModal({
					title: '提示',
					content: '是否确认删除人员',
					success: res => {
						if (res.confirm) {
							PostDeleteTHumanByHumanIds({
								humanIds: [id]
							}).then(res => {
								uni.$u.toast(res.msg)
								setTimeout(() => {
									this.handleInitList()
								}, 1000)
							})
						}
					}
				});
			},

			/**
			 * 添加房间
			 */
			add() {
				this.roomMode = 'add';
				this.$nextTick(() => {
					this.$refs.addRoom.open({
						familyId: this.familyInfo.familyId,
					});
				});

			},


			/**
			 * 初始化家庭列表
			 */
			handleInitList() {
				this.$store.dispatch('getAllDevices');
				Promise.all([
					PostRoomList({ // 房间列表
						familyId: this.familyId,
					}),
					PostSelectTHumanListByFamilyId({ // 人员列表
						familyId: this.familyId,
					})
				]).then(res => {
					this.list = res[0].rows;
					this.humanList = res[1].data;
				})
			},

			/**
			 * 家庭信息修改完成
			 */
			familyNext() {
				this.$store.dispatch('getAllFamily');
			},
			/**
			 * 编辑家庭信息
			 */
			openFamilyEdit() {
				console.log(this.familyInfo,'this.familyInfothis.familyInfothis.familyInfo')
				this.$refs.addFamily.open(
					assignDeep({}, {
						...this.familyInfo,
						familyName: this.familyInfo.name
					}));
			},

			/**
			 * 跳转人员管理
			 * @param {Object} item
			 */
			goEdit(item) {
				uni.navigateTo({
					url: `/pages/watch/watch-set/information?id=${item.humanId}`
				})
			},

			/**
			 * 绑定设备
			 */
			binding(item, type) {
				this.bindPayload = {
					familyId: item.familyId,
					id: type === 'room' ? item.roomId : item.humanId,
					type
				};
				this.$refs.bindDev.open();
			},

			/**
			 * 解绑
			 */
			unbinding(devices) {
				if (devices.humanId) {
					uni.showModal({
						title: '提示',
						content: '是否和人员解除绑定',
						success: res => {
							if (res.confirm) {
								const post = devices.type === '1' ? PostCareCardUnBind : PostWatchUnBind;
								post({
									deviceId: devices.deviceId,
									humanId: devices.humanId
								}).then(res => {
									uni.$u.toast(res.msg);
									setTimeout(() => {
										this.handleInitList();
									}, 500);
								})
							}
						}
					});
				} else {
					uni.showModal({
						title: '提示',
						content: '是否和房间解除绑定',
						success: res => {
							if (res.confirm) {
								relDevice({
									deviceId: devices[0].deviceId,
									flag: '3'
								}).then(res => {
									uni.$u.toast(res.msg);
									setTimeout(() => {
										this.handleInitList();
									}, 500);
								})
							}
						}
					});
				}

			},
		}

	};
</script>

<style lang="scss">
	.online {
		background-image: linear-gradient(90deg, #1EC862 0%, #13B98F 100%);
	}

	.offline {
		background: #D4D4D4;
	}

	.ui-logo {
		background: #ffffff;
		padding: 50rpx 40rpx;
		padding-top: 50rpx;
		display: flex;
		align-items: center;
		justify-content: flex-end;

		.logo-text {
			font-size: 52rpx;
			font-weight: bold;
		}
	}

	.ui-menu {
		padding: 30rpx 20rpx 120rpx 20rpx;
		display: flex;
		flex-wrap: wrap;
		box-sizing: border-box;

		.list-empty {
			width: 100%;
			height: 500rpx;
			display: flex;
			justify-content: center;
			align-items: center;
		}

		.ui-menu-item {
			background: #FFFFFF;
			border-radius: 16px;
			width: 48%;
			margin: 0 1%;
			margin-bottom: 20rpx;
			position: relative;

			.item-box {
				box-sizing: border-box;
				padding: 40rpx 20rpx 28rpx 20rpx;
				display: flex;
				flex-direction: column;
				justify-content: center;
				align-items: center;

				.device-status {
					position: absolute;
					top: 18rpx;
					right: 18rpx;

					text {
						color: #FFFFFF;
						font-size: 20rpx;
						padding: 4rpx 8rpx;
						border-radius: 4px;
					}
				}

				.device-info {
					display: flex;
					align-items: center;
					justify-content: space-between;
					min-height: 120rpx;

					image {
						height: 120rpx;
						width: 120rpx;
					}

					.detail {
						flex: 1;
						margin-left: 10rpx;
						display: inline-flex;
						flex-direction: column;

						text {
							display: inline-block;
						}

						.name {
							font-size: 36rpx;
							color: #353535;
							font-weight: 500;
						}

						.position {
							margin-top: 10rpx;
							font-size: 26rpx;
							color: #888888;
						}
					}
				}

				.device-action {
					width: 100%;
					margin-top: 20rpx;
					display: flex;
					justify-content: space-around;

					.danger {
						color: #E95656;
					}

					.warn {
						border: 1px solid rgba(254, 174, 67, 1);
						border-radius: 24px;
						color: #FEAE43;
						padding: 6rpx 20rpx;
						font-size: 26rpx;
					}

					.orange {
						padding: 6rpx 20rpx;
						font-size: 26rpx;
						color: #fff;
						background-image: linear-gradient(90deg, #FFB24D 0%, #FD913B 100%);
						border-radius: 24px;
					}

					text {
						font-size: 26rpx
					}
				}
			}
		}
	}

	// .ui-logo-icon {
	// 	margin-top: 30rpx;
	// }

	// .ui-menu {
	// 	margin-top: 70rpx;
	// 	padding: 0 78rpx;
	// 	min-height: 600rpx;

	// 	.ui-menu-item {
	// 		position: relative;
	// 		margin-top: 58rpx;
	// 		display: flex;
	// 		align-items: center;
	// 		justify-content: center;
	// 		flex-direction: column;
	// 		font-size: 27rpx;
	// 		color: #414141;
	// 		height: 258rpx;
	// 		width: 258rpx;
	// 		border-radius: 10rpx;
	// 		filter: drop-shadow(7.824rpx 10.382rpx 8rpx rgba(7, 5, 5, 0.08));
	// 		background-image: linear-gradient(96deg, #f5f5f5 0%, #e5e5e5 100%);
	// 		text-align: center;

	// 		.grid-text {
	// 			display: inline-flex;
	// 			align-items: center;
	// 			justify-content: center;
	// 			margin: 20rpx 0 10rpx 0;
	// 			width: 70%;
	// 			height: 60rpx;
	// 			font-size: 32rpx;
	// 			color: #414141;
	// 		}

	// 		.grid-text-device {
	// 			color: #999;
	// 		}

	// 		.ui-edit {
	// 			position: absolute;
	// 			bottom: 10rpx;
	// 			right: 10rpx;
	// 			z-index: 10;
	// 		}

	// 		.ui-close {
	// 			position: absolute;
	// 			right: -10rpx;
	// 			top: -10rpx;
	// 		}
	// 	}
	// }

	// .ui-btn {
	// 	text-align: center;
	// 	margin: 50rpx 0;

	// 	button {
	// 		width: 276rpx;
	// 		height: 74rpx;
	// 		font-size: 30rpx;
	// 		line-height: 74rpx;
	// 		border-radius: 60rpx;
	// 	}
	// }

	// .ui-menu-btn {
	// 	margin-top: 10rpx;
	// 	text-align: center;
	// 	width: 100%;

	// 	button {
	// 		height: 60rpx;
	// 		width: 258rpx;
	// 		line-height: 60rpx;
	// 		border-radius: 10px;
	// 		font-size: 30rpx;
	// 	}
	// }

	// .ui-logo {
	// 	height: 69rpx;
	// 	// width: 366rpx;
	// 	font-size: 69rpx;
	// 	color: #fff;
	// 	margin: 20rpx 72rpx;
	// 	display: inline-block;
	// 	font-weight: bold;
	// }

	// .ui-slider {
	// 	width: 100%;
	// 	display: flex;
	// 	flex-direction: row;
	// 	align-items: center;
	// 	justify-content: space-between;

	// 	text {
	// 		font-size: 26rpx;
	// 		color: #999;
	// 	}

	// 	&>* {
	// 		:nth-child(1) {
	// 			width: 320rpx;
	// 		}
	// 	}
	// }
	.ui-btn {
		width: 100%;
		position: fixed;
		bottom: 0;
		left: 0;

		button {
			border-radius: 0rpx !important;
		}
	}

	//头部样式
	/deep/ .u-navbar__content__title {
		font-size: 20px;
	}

	.action {
		display: flex;
		align-items: center;
		justify-content: space-between;

		text {
			font-size: 26rpx;
			color: #353535;
		}
	}
</style>