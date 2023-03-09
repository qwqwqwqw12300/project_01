<template>
	<app-body :hideTitle="true">
		<view class="ui-body">
			<view class="ui-header">
				<u-avatar size="60" :src="userInfo.avatar"></u-avatar>
				<view class="info">
					<view class="name">{{ userInfo.nickname || '暂无昵称' }}</view>
					<view class="phone">{{ userInfo.phone }}</view>
				</view>
				<view class="edit" @tap="handleJump('/pages/myself/info-edit')">
					编辑个人资料
					<u-icon style="margin-left: 8rpx;" name="arrow-right" size="18"></u-icon>
				</view>
			</view>

			<view class="ui-manage">
				<view class="mag-box">
					<view class="item" v-for="(item,index) in magList" :key="index" @tap="handleJump(item.url)">
						<u-icon :name="item.icon" size="36"></u-icon>
						<text>{{ item.title }}</text>
					</view>
					<!-- 		<view class="item">
						<u-icon :name="'/static/images/device-mg.png'" size="36"></u-icon>
						<text>设备管理</text>
					</view>
					<view class="item">
						<u-icon :name="'/static/images/urgent-phone.png'" size="36"></u-icon>
						<text>设置紧急电话</text>
					</view> -->
				</view>
			</view>

			<view class="ui-cell">
				<u-cell-group>
					<u-cell v-for="(item,index) in cellList" :key="index" size="large" :title="item.title"
						@tap="handleJump(item.url)" :isLink="true" titleStyle="font-size: 15px;color: #303133;">
						<!-- <u-icon slot="icon" size="25" :color="item.icon.color" :name="item.icon.name"></u-icon> -->
					</u-cell>
				</u-cell-group>
			</view>

			<view class="ui-button">
				<button @click="loginOut">
					注销
				</button>
			</view>

		</view>
	</app-body>
</template>

<script>
	import {
		env
	} from '@/config/env';
	import {
		phoneHide
	} from '../../common/utils/util';
	import {
		PostLoginOut,
	} from '@/common/http/api.js';
	import {
		removeToken
	} from '@/common/utils/auth.js';
	export default {
		data() {
			return {
				magList: [{
					title: '家庭管理',
					icon: '/static/images/home.png',
					url: '/pages/myself/famliy-manage',
				}, {
					title: '设备管理',
					icon: '/static/images/device-mg.png',
					url: '/pages/myself/device-manage/device-manage'
				}, {
					title: '紧急电话',
					icon: '/static/images/urgent-phone.png',
					url: '/pages/myself/urgent-mobile'
				}],
				cellList: [{
					title: '消息设置',
					url: '/pages/myself/push-set',
				}, {
					title: '密码修改',
					url: '/pages/myself/pwd'
				}, {
					title: '协议信息',
					url: '/pages/myself/agreement/agreement-info'
				}, {
					title: '问题与建议',
					url: '/pages/myself/suggestion'
				}, {
					title: '版本管理',
					url: '/pages/myself/version-manage'
				}]
			}
		},
		computed: {
			userInfo() {
				const obj = uni.$u.deepClone(this.$store.getters.userInfo);
				obj.avatar = `${env.basePath}${obj.avatar}`
				obj.phone = phoneHide(obj.phone || '')
				return obj
			}
		},
		mounted() {
			this.$store.dispatch('getPushMsgState')
		},
		methods: {
			handleJump(url) {
				uni.navigateTo({
					url
				})
			},
			/**
			 * 注销
			 */
			loginOut() {
				uni.showModal({
					title: '提示',
					content: '是否确认注销',
					success: res => {
						if (res.confirm) {
							PostLoginOut({}).then(() => {
								removeToken()
								uni.$u.toast('注销成功')
								setTimeout(() => {
									uni.reLaunch({
										url: '/pages/login/login'
									})
								}, 1000)
							})
						} else if (res.cancel) {
							console.log('用户点击取消');
						}
					}
				});
			}
		}
	}
</script>

<style lang="scss" scoped>
	::v-deep {
		.u-cell .u-line {
			margin: 0px !important;
		}
	}

	.ui-body {
		display: inline-block;
		width: 100%;
		box-sizing: border-box;
		margin-top: 100rpx;
		padding: 0 32rpx;
	}

	.ui-header {
		height: 130rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;

		.info {
			flex: 1;
			padding-left: 30rpx;

			.name {
				width: 260rpx;
				overflow: hidden;
				white-space: nowrap;
				text-overflow: ellipsis;
				font-size: 42rpx;
				color: #353535;
				font-weight: 600;
			}

			.phone {
				margin-top: 10rpx;
				font-size: 28rpx;
				color: #888888;
				font-weight: 400;
			}
		}

		.edit {
			display: flex;
			align-items: center;
			font-size: 32rpx;
			color: #353535;
		}
	}

	.ui-manage {
		margin-top: 30rpx;
		background: #FFFFFF;
		border-radius: 18rpx;

		.mag-box {
			padding: 30rpx 50rpx;
			display: flex;
			align-items: center;
			justify-content: space-between;

			.item {
				display: flex;
				flex-direction: column;
				justify-content: center;
				align-items: center;
				height: 154rpx;

				text {
					margin-top: 10rpx;
					color: #353535;
					font-size: 28rpx;
				}
			}
		}
	}

	.ui-cell {
		margin-top: 30rpx;
		background: #fff;
		border-radius: 18rpx;
	}

	.ui-button {
		margin-top: 30rpx;

		button {
			border-radius: 18rpx;
			color: #FF4645;
			background: #FFFFFF;
		}
	}
</style>
