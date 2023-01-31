<!--
* @Author: zhanch
* @Date: 2022-12-30 17:14:05
* @FilePath: /pages/myself/device-manage.vue
* @Description: 设备管理
-->

<template>
	<view id="deviceManage">
		<app-body>
			<!-- <view class="ui-logo">设备管理</view> -->
			<app-logo text="设备管理"></app-logo>
			<view class="ui-menu">
				<u-grid col="2">
					<u-grid-item v-for="(item, index) in list" :key="index">
						<view class="ui-menu-item" :border="false">
							<u-icon :customStyle="{ paddingTop: 20 + 'rpx' }" name="/static/images/myself/device.png"
								size="80rpx"></u-icon>
							<!-- <text class="grid-text">{{ baseListItem.title }}</text> -->
							<u-text class="grid-text" @click="edit(item)" suffixIcon="edit-pen-fill"
								iconStyle="font-size: 36rpx" align="center" :text="item.name"></u-text>
							<text class="grid-text">{{ item.location }}</text>
							<view class="ui-wifi active">
								<u-icon :customStyle="{ paddingTop: 20 + 'rpx' }"
									:name="item.onlineFlag === '1' ? 'wifi' : 'wifi-off'" size="40rpx"
									:color="item.onlineFlag === '1' ? '#0dab1c' : '#ff4800'"></u-icon>
							</view>
							<!-- 							<u-icon @click="onDelete" class="ui-close active" name="close-circle-fill" size="40rpx">
							</u-icon> -->
						</view>
						<view class="ui-btn">
							<button v-if="!item.roomId" @click="binding(item)">绑定</button>
							<button v-else class="wd-sms" @click="unbinding(item)">解绑</button>
						</view>
					</u-grid-item>
				</u-grid>
			</view>
			<view class="ui-add-btn"><button @click="addHandle.show = true">创建设备</button></view>
			<!-- 绑定房间 -->
			<u-popup :closeable="true" :round="10" :show="bindRoomShow" mode="center" @close="close">
				<view class="wd-add">
					<u-text prefixIcon="plus-circle" :iconStyle="{ fontSize: '38rpx', color: '#ea942f' }"
						color="#ea942f" size="30rpx" text="绑定设备"></u-text>
					<view class="ui-add-box">
						<u-text size="28rpx" prefixIcon="home" iconStyle="font-size: 36rpx" text="选择家庭"></u-text>
						<view class="ui-select">
							<uni-data-select v-model="bindForm.familyId" @change="familyChange" :clear="false"
								:localdata="famliyList"></uni-data-select>
						</view>
					</view>
					<view class="ui-add-box">
						<u-text size="28rpx" prefixIcon="home-fill" iconStyle="font-size: 36rpx" text="选择房间"></u-text>
						<view class="ui-select">
							<uni-data-select v-model="bindForm.roomId" :clear="false" :localdata="rangRoomList">
							</uni-data-select>
						</view>
					</view>
					<view class="wd-btn-gloup"><button @click="bindSubmit">确定</button></view>
				</view>
			</u-popup>
			<!-- /绑定房间 -->
			<device-edit :editFrom="editSubmit" ref="editRef" @confirm="editConfirm"></device-edit>
			<!-- 修改名称 -->
			<!-- <u-popup :closeable="true" :round="10" :show="isEditShow" mode="center" @close="eidtClose">
				<view class="wd-add ui-change">
					<u-text prefixIcon="edit-pen" :iconStyle="{ fontSize: '38rpx', color: '#ea942f' }" color="#ea942f"
						size="30rpx" text="修改名称"></u-text>
					<view class="ui-add-box">
						<view>
							<u-text size="28rpx" prefixIcon="home" iconStyle="font-size: 36rpx" text="设备名称"></u-text>
							<u-input placeholder="请输入设备名称" :maxlength="6" v-model="editFrom.deviceName" border="bottom"
								clearable>
							</u-input>
						</view>
					</view>
					<view class="wd-btn-gloup"><button @click="editSubmit">确定</button></view>
				</view>
			</u-popup> -->
			<!-- /修改名称 -->
			<u-action-sheet :actions="addHandle.list" :closeOnClickOverlay="true" :safeAreaInsetBottom="true"
				:closeOnClickAction="true" @close="addHandle.show = false" :show="addHandle.show" @select="sheetSelect"
				cancelText="取消">
			</u-action-sheet>
		</app-body>
	</view>
</template>

<script>
	import {
		getRoomList,
		PostDeviceList,
		PosteditDevice,
		setDevice
	} from '@/common/http/api.js';
	import {
		mapState,
		mapActions
	} from 'vuex';

	export default {
		data() {
			return {
				isEditShow: false,
				/**绑定房间**/
				bindRoomShow: false,
				roomList: [],
				bindForm: {
					familyId: '',
					roomId: '',
					deviceId: '',
					roomLeft: 0,
					roomHeight: 0,
					roomRight: 0,
					roomLength: 0

				},
				editFrom: {
					deviceId: '',
					deviceName: '',
					deviceType: '',
					deviceNo: '',
					roomLeft: 0,
					roomHeight: 0,
					roomRight: 0,
					roomLength: 0
				},
				addHandle: {
					show: false,
					list: [{
							name: '雷达波',
							url: '/pages/equipment/radar'
						},
						{
							name: '监护设备',
							url: '/pages/equipment/monitor'
						},
					]
				}
			};
		},
		computed: {
			...mapState({
				/**所有设备列表**/
				list: state => state.devicesList,
				/**家庭列表**/
				famliyList: state => {
					return state.familyList.map(ele => ({
						text: ele.name,
						value: ele.familyId
					}));
				}
			}),
			/**房间列表**/
			rangRoomList() {
				return this.roomList.map((ele => ({
					text: ele.name,
					value: ele.roomId
				})))
			}
		},
		methods: {
			...mapActions(['getAllDevices', 'getAllFamily']),

			/**
			 * 关闭弹窗
			 */
			close() {
				this.bindForm.familyId = ''
				this.bindForm.roomId = ''
				this.bindForm.deviceId = ''
				this.bindRoomShow = false;
			},

			/**
			 * 编辑浮层打开
			 */
			edit(item) {
				const {
					name,
					deviceId,
					type,
					no,
					roomLeft,
					roomHeight,
					roomRight,
					roomLength
				} = item;
				Object.assign(this.editFrom, {
					deviceName: name,
					deviceId,
					deviceType: type,
					deviceNo: no,
					roomLeft,
					roomHeight,
					roomRight,
					roomLength
				});
				this.$refs.editRef.open();
				// console.log(this.editFrom, 'this.editFrom');
				// this.isEditShow = true;
			},

			/**
			 * 修改设备
			 */
			editSubmit(editFrom) {
				if (editFrom.deviceName) {
					PosteditDevice({
						...this.editFrom
					}).then(res => {
						uni.$u.toast(res.msg);
						this.eidtClose();
						setTimeout(() => {
							this.init();
						}, 1000);
					});
				} else {
					uni.$u.toast('请填写新名称');
				}

			},

			/**
			 * 编辑浮层关闭
			 */
			eidtClose() {
				this.isEditShow = false;
			},

			/**
			 * 绑定
			 */
			binding(item) {
				this.bindForm.deviceId = item.deviceId;
				this.bindRoomShow = true;
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
							setDevice({
								deviceId,
								familyId: '',
								roomId: ''
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
			 * 绑定房间
			 */
			bindRoom() {
				this.bindRoomShow = true;
			},

			/**
			 * 选择家庭
			 */
			familyChange(id) {
				id && getRoomList({
					familyId: id
				}).then(res => {
					console.log(res);
					this.roomList = res.rows || [];
					this.bindForm.roomId = ''
				});
			},
			/**
			 * 绑定提交
			 */
			bindSubmit() {
				if (this.bindForm.familyId && this.bindForm.roomId) {
					setDevice({
						...this.bindForm
					}).then(res => {
						uni.$u.toast(res.msg);
						this.close();
						setTimeout(() => {
							this.init();
						}, 1000);
					})
				} else {
					uni.$u.toast('请选择要绑定的房间');
				}
			},

			/**
			 * 创建设备
			 */
			sheetSelect({
				url
			}) {
				uni.navigateTo({
					url
				})
			},

			/**
			 * 初始化设备
			 */
			init() {
				Promise.all([
					this.getAllDevices(),
					this.getAllFamily()
				])
			}
		},
		onShow() {
			this.init();
		},
	};
</script>

<style lang="scss">
	.ui-menu {
		margin: 70rpx 0;
		padding: 0 78rpx;
		min-height: 600rpx;

		.ui-menu-item {
			position: relative;
			margin-top: 58rpx;
			padding: 20rpx 0;
			display: flex;
			box-sizing: border-box;
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

			&>* {
				flex: 1;

				&:nth-child(1) {
					flex: 2;
				}
			}

			text {
				display: inline-flex;
				width: 70%;
				height: 40rpx;
				line-height: 60rpx;
				align-items: center;
				justify-content: center;
			}

			.grid-text {
				&:nth-child(3) {
					font-size: 24rpx;
				}
			}

			.ui-wifi {
				position: absolute;
				bottom: 10rpx;
				right: 10rpx;
				z-index: 10;
			}

			.ui-close {
				position: absolute;
				top: -10rpx;
				right: -10rpx;
			}
		}

		.ui-btn {
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
	}

	.ui-add-btn {
		text-align: center;
		margin-bottom: 20rpx;

		button {
			width: 276rpx;
			height: 74rpx;
			font-size: 30rpx;
			line-height: 74rpx;
			border-radius: 60rpx;
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

	.wd-add {
		width: 582rpx;
		height: 606rpx;
		border-radius: 20rpx;
		filter: drop-shadow(0 0 5rpx rgba(7, 5, 5, 0.34));
		background-image: linear-gradient(-36deg, #e4e4e4 0%, #f8f8f8 100%);
		padding: 53rpx 31rpx;

		&.ui-change {
			height: 400rpx;

			.ui-add-box {
				border-bottom: 1px solid #e4e4e4;
			}
		}

		&>view {
			margin-top: 52rpx;

			&.ui-add-box {
				padding: 10rpx 20rpx;

				&>* {
					margin-top: 30rpx;
				}
			}
		}

		.wd-btn-gloup {
			text-align: center;
			margin-top: 70rpx;

			button {
				width: 237rpx;
				height: 71rpx;
				font-size: 28rpx;
				color: #ffffff;
			}
		}
	}
</style>
