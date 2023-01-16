<!--
* @Author: zhanch
* @Date: 2022-12-30 16:47:13
* @FilePath: /pages/myself/room-manage.vue
* @Description: 房间管理
-->

<template>
	<view id="roomMagege">
		<app-body>
			<app-logo text="我的房间管理"></app-logo>
			<!-- <view class="ui-logo">我的房间管理</view> -->
			<view class="ui-menu">
				<u-grid>
					<u-grid-item v-for="(item, index) in list" :key="index">
						<view class="ui-menu-item" :border="false">
							<u-icon :customStyle="{ paddingTop: 20 + 'rpx' }" name="/static/images/myself/room.png"
								size="60rpx"></u-icon>
							<text class="grid-text">{{ item.name }}</text>
							<view class="ui-edit" @click.stop="editFamliy(item)">
								<u-icon :customStyle="{ paddingTop: 20 + 'rpx' }" name="edit-pen-fill" size="30rpx"
									color="#ff9500"></u-icon>
							</view>
							<u-icon @click.native.stop="onDelete(item.roomId)" class="ui-close active"
								name="close-circle-fill" size="40rpx">
							</u-icon>
						</view>
					</u-grid-item>
				</u-grid>
			</view>
			<view class="ui-btn"><button @click="add">创建房间</button></view>
			<u-popup :closeable="true" :overlay="false" zIndex="99" :round="10" :show="isEditShow" mode="center"
				@close="close">
				<view class="wd-add">
					<view>
						<u-text size="28rpx" prefixIcon="home-fill" iconStyle="font-size: 40rpx" text="房间名称"></u-text>
						<u--input v-model="form.name" placeholder="请输入房间名称" border="bottom" clearable></u--input>
					</view>
					<view>
						<u-text size="28rpx" prefixIcon="home-fill" iconStyle="font-size: 40rpx" text="房间高度"></u-text>
						<u--input v-model="form.roomHeight" placeholder="请输入房间高度" border="bottom" clearable></u--input>
					</view>
					<view>
						<u-text size="28rpx" prefixIcon="home-fill" iconStyle="font-size: 40rpx" text="房间长度"></u-text>
						<u--input v-model="form.roomLength" placeholder="请输入房间长度" border="bottom" clearable></u--input>
					</view>
					<view>
						<u-text size="28rpx" prefixIcon="home-fill" iconStyle="font-size: 40rpx" text="房间左长度"></u-text>
						<u--input v-model="form.roomRight" placeholder="请输入房间左长度" border="bottom" clearable></u--input>
					</view>
					<view>
						<u-text size="28rpx" prefixIcon="home-fill" iconStyle="font-size: 40rpx" text="房间右长度"></u-text>
						<u--input v-model="form.roomLeft" placeholder="请输入房间右长度" border="bottom" clearable></u--input>
					</view>
					<view class="wd-btn-gloup">
						<button @click="onSubmit">提交</button>
						<button @click="close">取消</button>
					</view>
				</view>
			</u-popup>
			<add-room ref="addRoom" @update="handleInitList" />
		</app-body>
	</view>

</template>

<script>
	import {
		PostDelRoom,
		PostRoomList,
		PostEditRoom,
	} from '@/common/http/api.js';
	export default {
		data() {
			return {
				isEditShow: false,
				form: {
					name: '',
					roomId: '',
					roomHeight: '',
					roomLength: '',
					roomLeft: '',
					roomRight: '',
				},
				list: []
			};
		},
		methods: {
			/**
			 * 菜单点击
			 */
			// gridClick(url) {
			// 	console.log(12);
			// },

			/**
			 * 修改家庭
			 */
			editFamliy(item) {
				this.form = {
					...item
				}
				console.log(this.form, 'fff')
				this.isEditShow = true;
			},

			/**
			 * 关闭弹窗
			 */
			close() {
				this.form = {}
				this.isEditShow = false;
			},
			/**
			 * 提交
			 */
			onSubmit() {
				const {
					name,
					roomHeight,
					roomLength,
					roomLeft,
					roomRight
				} = this.form
				if (!roomHeight || !roomLeft || !roomRight || !roomLength || !name) {
					return uni.$u.toast('请完善房间信息')
				}
				PostEditRoom({
					...this.form
				}).then(res => {
					uni.$u.toast(res.msg)
					setTimeout(() => {
						this.close();
						this.handleInitList()
					}, 500)
				})
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
			 * 添加家庭
			 */
			add() {
				this.$refs.addRoom.open({
					id: this.familyId,
					subTitle: '提交'
				});
			},

			/**
			 * 家庭添加完成
			 */
			roomNext() {},

			/**
			 * 初始化家庭列表
			 */
			handleInitList() {
				PostRoomList({
					familyId: this.familyId,
				}).then(res => {
					this.list = res.rows
				})
			}
		},
		onLoad(options) {
			this.familyId = options.familyId
			this.handleInitList()
		}
	};
</script>

<style lang="scss">
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
			height: 168rpx;
			width: 168rpx;
			border-radius: 10rpx;
			filter: drop-shadow(7.824rpx 10.382rpx 8rpx rgba(7, 5, 5, 0.08));
			background-image: linear-gradient(96deg, #f5f5f5 0%, #e5e5e5 100%);
			text-align: center;

			text {
				display: inline-block;
				margin-top: 10rpx;
				width: 70%;
				height: 60rpx;
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
		height: 880rpx;
		border-radius: 20rpx;
		filter: drop-shadow(0 0 5rpx rgba(7, 5, 5, 0.34));
		background-image: linear-gradient(-36deg, #e4e4e4 0%, #f8f8f8 100%);
		padding: 53rpx 31rpx;

		&>view {
			margin-top: 18rpx;
			padding: 10rpx 20rpx;
			border-bottom: 1px solid #e4e4e4;

			&:nth-last-of-type(1) {
				padding-left: 0rpx;
				border-bottom: none;
			}
		}

		.wd-btn-gloup {
			text-align: center;
			margin-top: 50rpx;

			button {
				width: 237rpx;
				height: 71rpx;
				font-size: 28rpx;
				color: #ffffff;
			}
		}
	}
</style>
