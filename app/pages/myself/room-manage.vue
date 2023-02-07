<!--
* @Author: zhanch
* @Date: 2022-12-30 16:47:13
* @FilePath: /pages/myself/room-manage.vue
* @Description: 房间管理
-->

<template>
	<view id="roomMagege">
		<app-body>
			<app-logo :text="familyInfo.name">
				<u-icon class="ui-logo-icon" name="edit-pen" size="60rpx" color="#fff" @click="openFamilyEdit"></u-icon>
			</app-logo>
			<view class="ui-menu">
				<u-grid col="2">
					<u-grid-item v-for="(item, index) in list" :key="index">
						<view class="ui-menu-item active" :border="false" @click="openRoomEdit(item)">
							<u-icon :customStyle="{ paddingTop: 20 + 'rpx' }" name="/static/images/myself/room.png"
								size="60rpx"></u-icon>
							<text class="grid-text">{{ item.name }}</text>
							<view class="device-info" v-if="item.devices.length">
								<text class="grid-text-device">{{ (getDevices(item).name || '未命名设备')  + ' | '}}</text>
								<text class="grid-text-device"
									:style="{color: getDevices(item).onlineFlag == 1 ? 'rgb(13, 171, 28)' : 'rgb(255, 72, 0)'}">
									{{(getDevices(item).onlineFlag == 1 ? '在线' : '离线')}}</text>
							</view>
							<text class="grid-text-device" v-else>未绑定设备</text>
							<u-icon @click.native.stop="onDelete(item.roomId)" class="ui-close active"
								name="close-circle-fill" size="40rpx">
							</u-icon>
						</view>
						<view class="ui-menu-btn">
							<button v-if="!item.devices.length" @click="binding(item)">绑定</button>
							<button v-else class="wd-sms" @click="unbinding(item.devices)">解绑</button>
						</view>
					</u-grid-item>
				</u-grid>
			</view>
			<view class="ui-btn"><button @click="add">创建房间</button></view>
			<room-pop ref="addRoom" :mode="roomMode" @update="handleInitList" />
			<family-pop ref="addFamily" mode="edit" @update="familyNext" />
			<bind-device :payload="bindPayload" @next="handleInitList" ref="bindDev" />
		</app-body>
	</view>

</template>

<script>
	import {
		PostDelRoom,
		PostRoomList,
		PostEditRoom,
		relDevice
	} from '@/common/http/api.js';
	import {
		assignDeep
	} from '../../common/utils/util';
	export default {
		data() {
			return {
				roomMode: 'add',
				list: [],
				/**家庭信息**/
				familyInfo: {},
				bindPayload: {}
			};
		},
		onLoad() {
			this.familyInfo = this.$getCache('familyInfo');
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
			}
		},
		methods: {

			/**
			 * 修改房间
			 */
			openRoomEdit(item) {
				this.roomMode = 'edit';
				this.$refs.addRoom.open({
					...item
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
			 * 添加房间
			 */
			add() {
				this.roomMode = 'add';
				this.$refs.addRoom.open({
					familyId: this.familyInfo.familyId,
				});
			},


			/**
			 * 初始化家庭列表
			 */
			handleInitList() {
				this.$store.dispatch('getAllDevices');
				PostRoomList({
					familyId: this.familyInfo.familyId,
				}).then(res => {
					this.list = res.rows
				})
			},

			/**
			 * 家庭信息修改完成
			 */
			familyNext({
				name
			}) {
				this.familyInfo.name = name;
			},
			/**
			 * 编辑家庭信息
			 */
			openFamilyEdit() {
				this.$refs.addFamily.open(
					assignDeep({}, {
						...this.familyInfo,
						familyName: this.familyInfo.name
					}));
			},

			/**
			 * 绑定设备
			 */
			binding({
				familyId,
				roomId
			}) {
				this.bindPayload = {
					familyId,
					roomId
				};
				this.$refs.bindDev.open();
			},

			/**
			 * 解绑
			 */
			unbinding(devices) {
				uni.showModal({
					title: '提示',
					content: '是否和房间解除绑定',
					success: res => {
						if (res.confirm) {
							relDevice({
								deviceId: devices[0].deviceId,
							}).then(res => {
								uni.$u.toast(res.msg);
								setTimeout(() => {
									this.handleInitList();
								}, 500);
							})
						}
					}
				});
			},
		}

	};
</script>

<style lang="scss">
	.ui-logo-icon {
		margin-top: 30rpx;
	}

	.ui-menu {
		margin-top: 70rpx;
		padding: 0 78rpx;
		min-height: 600rpx;

		.ui-menu-item {
			position: relative;
			margin-top: 58rpx;
			display: flex;
			align-items: center;
			justify-content: center;
			flex-direction: column;
			font-size: 27rpx;
			color: #414141;
			height: 258rpx;
			width: 258rpx;
			border-radius: 10rpx;
			filter: drop-shadow(7.824rpx 10.382rpx 8rpx rgba(7, 5, 5, 0.08));
			background-image: linear-gradient(96deg, #f5f5f5 0%, #e5e5e5 100%);
			text-align: center;

			.grid-text {
				display: inline-flex;
				align-items: center;
				justify-content: center;
				margin: 20rpx 0 10rpx 0;
				width: 70%;
				height: 60rpx;
				font-size: 32rpx;
				color: #414141;
			}

			.grid-text-device {
				color: #999;
			}

			.ui-edit {
				position: absolute;
				bottom: 10rpx;
				right: 10rpx;
				z-index: 10;
			}

			.ui-close {
				position: absolute;
				right: -10rpx;
				top: -10rpx;
			}
		}
	}

	.ui-btn {
		text-align: center;
		margin: 50rpx 0;

		button {
			width: 276rpx;
			height: 74rpx;
			font-size: 30rpx;
			line-height: 74rpx;
			border-radius: 60rpx;
		}
	}

	.ui-menu-btn {
		margin-top: 10rpx;
		text-align: center;
		width: 100%;

		button {
			height: 60rpx;
			width: 258rpx;
			line-height: 60rpx;
			border-radius: 10px;
			font-size: 30rpx;
		}
	}

	.ui-logo {
		height: 69rpx;
		// width: 366rpx;
		font-size: 69rpx;
		color: #fff;
		margin: 20rpx 72rpx;
		display: inline-block;
		font-weight: bold;
	}

	.ui-slider {
		width: 100%;
		display: flex;
		flex-direction: row;
		align-items: center;
		justify-content: space-between;

		text {
			font-size: 26rpx;
			color: #999;
		}

		&>* {
			:nth-child(1) {
				width: 320rpx;
			}
		}
	}
</style>
