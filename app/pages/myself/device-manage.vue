<!--
* @Author: zhanch
* @Date: 2022-12-30 17:14:05
* @FilePath: /pages/myself/device-manage.vue
* @Description: 设备管理
-->

<template>
	<view id="deviceManage">
		<app-body :back="back" :bg="false">
			<view class="ui-logo">
				<app-logo text="设备管理"></app-logo>
			</view>
			<view class="ui-menu">
				<view v-for="(device, index) of devices" :key="'f' + index">
					<!-- 	<view class="ui-title" v-if="device.list.length">{{device.name}}</view> -->
					<view class="ui-menu-title" v-if="device.list.length">
						<u-icon name="/static/images/home.png" size="28"></u-icon>
						<text>{{device.name || '未命名家庭'}}</text>
					</view>
					<view class="ui-menu-content">
						<view class="ui-menu-item" v-for="(item, index) in device.list" :key="index">
							<view class="item-box" @click="edit(item)">
								<view class="device-status">
									<text class="online" v-if="item.onlineFlag == 1">在线</text>
									<text class="offline" v-else>离线</text>
								</view>
								<view class="device-info">
									<image src="/static/images/leida-nm.png"></image>
									<view class="detail">
										<text class="name">{{ item.name || '未命名设备' }}</text>
										<text class="position">
											{{ (item.roomName || '--' )+ ' | ' + (item.location || '--')}}
										</text>
									</view>
								</view>
								<view class="device-action">
									<text class="danger" @tap.stop="onDelete(item.deviceId)">删除</text>
									<text class="warn" v-if="!item.roomId" @click.stop="binding(item)">绑定</text>
									<text class="orange" v-else @click.stop="unbinding(item)">解绑</text>
								</view>
							</view>
						</view>
					</view>
				</view>

			</view>
			<view class="ui-btn"><button class="default" @click="show = true">添加设备</button></view>
			<AppHandle :isShow="show" :safeShow="safeAreaShow = true" @cancle="show = false"></AppHandle>
			<select-room ref="selectRef" @comfirm="bindSubmit"></select-room>
		</app-body>
	</view>
</template>

<script>
	import {
		getRoomList,
		PostDeviceList,
		PosteditDevice,
		PostDeviceDel,
		setDevice,
		relDevice,
		getDeviceListState
	} from '@/common/http/api.js';
	import {	
		mapState,
		mapActions
	} from 'vuex';
	import {
		INIT_DEIVCE_SET
	} from '../../config/db';
	import 
		AppHandle
	 from '@/components/add-handle/add-handle.vue';
	export default {
		components: {
			AppHandle
		},
		data() {
			return {
				show:false,
				safeAreaShow:false,
				bindForm: {
					...INIT_DEIVCE_SET
				},
				editFrom: {
					deviceId: '',
					deviceName: '',
					deviceType: '',
					deviceNo: '',
					roomLeft: 6,
					roomHeight: 6,
					roomRight: 6,
					roomLength: 6
				},
				list: [],
				back: () => {
					uni.switchTab({
						url: '/pages/myself/myself'
					})
				}
			};
		},
		computed: {
			...mapState({
				/**所有设备列表**/
				// list: state => state.devicesList,
				devices: function(state) {
					const list = this.list,
						devices = [{
							name: '未绑定设备',
							list: list.filter(item => item && !item.familyId) || []
						}];
					state.familyList.forEach(ele => {
						if (ele.shareFlag === '2') {
							const items = list.filter(item => item.familyId === ele.familyId);
							if (items.length)
								devices.push({
									name: ele.name,
									list: items
								});
						}
					});
					return devices;
				}

			}),
		},
		onShow() {
			this.init();
		},
		methods: {
			...mapActions(['getAllFamily']),

			getList() {
				getDeviceListState({}).then(res => {
					this.list = res.rows || [];
				});
			},

			/**
			 * 编辑浮层打开
			 */
			edit(item) {
				this.$setCache('setDevice', item);
				uni.navigateTo({
					url: '/pages/equipment/setting/radar-setting'
				})
			},


			/**
			 * 绑定
			 */
			binding(item) {
				const {
					name,
					deviceId,
					type,
					no
				} = item;
				Object.assign(this.bindForm, {
					deviceName: name,
					deviceId,
					deviceType: type,
					deviceNo: no
				});
				this.$refs.selectRef.open();
				// this.bindRoomShow = true;
			},

			/**
			 * 解绑
			 */
			unbinding({
				deviceId
			}) {
				uni.showModal({
					title: '提示',
					content: '是否和房间解除绑定',
					success: res => {
						if (res.confirm) {
							relDevice({
								deviceId,
								flag: '3'
							}).then(res => {
								uni.$u.toast(res.msg);
								setTimeout(() => {
									this.init();
								}, 1000);
							})
						}
					}
				});
			},

			/**
			 * 绑定提交
			 */
			bindSubmit(form) {
				Object.assign(this.bindForm, form);
				if (this.bindForm.familyId && this.bindForm.roomId) {
					setDevice({
						...this.bindForm,
						flag: '1'
					}).then(res => {
						uni.$u.toast(res.msg);
						setTimeout(() => {
							this.init();
						}, 1000);
					})
				} else {
					uni.$u.toast('请选择要绑定的房间');
				}
			},

			/**
			 * 初始化设备
			 */
			init() {
				Promise.all([
					this.getList(),
					this.getAllFamily()
				])
			},

			/**
			 * 删除设备
			 */
			onDelete(deviceId) {
				uni.showModal({
					title: '提示',
					content: '是否确认删除该设备',
					success: res => {
						if (res.confirm) {
							PostDeviceDel({
								deviceId
							}).then(res => {
								uni.$u.toast('删除成功');
								setTimeout(() => {
									this.init();
								}, 1000);
							})
						}
					}
				});
			}
		},
		onBackPress(event) {
			console.log('物理返回', event);
			if (event.from === 'backbutton') {
				uni.switchTab({
					url: '/pages/myself/myself'
				});
				return true;
			}

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
		padding-bottom: 50rpx;
	}

	.ui-menu {
		// margin: 70rpx 0;
		padding: 0 20rpx 120rpx 20rpx;
		// padding-top: 30rpx;
		min-height: 600rpx;

		.ui-menu-title {
			display: flex;
			align-items: center;
			font-size: 34rpx;
			color: #353535;
			height: 40px;
			font-weight: 550;
			margin-bottom: 10rpx;

			text {
				margin-left: 10rpx;
			}
		}

		.ui-menu-content {
			padding-bottom: 30rpx;
			display: flex;
			flex-wrap: wrap;
			box-sizing: border-box;

			.ui-menu-item {
				background: #FFFFFF;
				border-radius: 16px;
				width: 48%;
				margin: 0 1%;
				margin-bottom: 20rpx;
				position: relative;

				.item-box {
					padding: 30rpx 20rpx;
					display: flex;
					flex-direction: column;
					justify-content: center;
					align-items: center;

					.device-status {
						position: absolute;
						top: 12rpx;
						right: 12rpx;

						text {
							color: #FFFFFF;
							font-size: 20rpx;
							padding: 4rpx 8rpx;
							border-radius: 4px;
						}
					}

					.device-info {
						// width: 100%;
						margin-top: 10rpx;
						display: flex;
						align-items: center;
						justify-content: space-between;

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
								font-size: 32rpx;
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
						align-items: center;

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
	}

	.ui-btn {
		width: 100%;
		position: fixed;
		bottom: 0;
		left: 0;

		button {
			border-radius: 0rpx !important;
		}
	}

	

	
</style>
