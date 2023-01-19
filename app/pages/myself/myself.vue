<!--
* @Author: zhanch
* @Date: 2022-12-28 18:25:27
* @LastEditors: zhanch
* @Description: 我的页面
-->

<template>
	<app-body :hideTitle="true">
		<view class="ui-user">
			<text>{{ userInfoPhone || '暂无手机号' }}</text>
			<u-icon @click="editMobile" name="edit-pen" color="#fff" size="50rpx"></u-icon>
			<button @click="loginOut">注销</button>
		</view>
		<view class="ui-menu">
			<u-grid>
				<u-grid-item v-for="(baseListItem, baseListIndex) in baseList" :key="baseListIndex">
					<view class="ui-menu-item" :border="false" @click="gridClick(baseListItem.url)">
						<u-icon :customStyle="{ paddingTop: 20 + 'rpx' }"
							:name="'/static/images/myself/' + baseListItem.pic" size="53rpx"></u-icon>
						<text class="grid-text">{{ baseListItem.title }}</text>
					</view>
				</u-grid-item>
			</u-grid>
		</view>
	</app-body>
</template>

<script>
	import {
		PostLoginOut,
	} from '@/common/http/api.js';
	import {
		removeToken
	} from '@/common/utils/auth.js';
	export default {
		data() {
			return {
				baseList: [{
						title: '家庭组管理',
						pic: 'home.png',
						url: '/pages/myself/famliy-manage'
					},
					{
						title: '设备管理',
						pic: 'device.png',
						url: '/pages/myself/device-manage'
					},
					{
						title: '设置紧急电话',
						pic: 'phone.png',
						url: '/pages/myself/urgent-mobile'
					},
					{
						title: '消息设置',
						pic: 'info.png',
						url: '/pages/myself/push-set'
					},
					{
						title: '密码修改',
						pic: 'pwd.png',
						url: '/pages/myself/pwd'
					},
					{
						title: '协议信息',
						pic: 'ag.png',
						url: '/pages/myself/agreement-info'
					},
					// {
					// 	title: '常见问题',
					// 	pic: 'question.png',
					// 	url: ''
					// },
					{
						title: '问题与建议',
						pic: 'complaint.png',
						url: '/pages/myself/suggestion'
					},
					{
						title: '版本管理',
						pic: 'version-mg.png',
						url: '/pages/myself/version-manage'
					}
				]
			};
		},
		computed: {
			userInfoPhone() {
				const phone = this.$store.getters.userInfo.phone,
					pho = /(\d{3})\d*(\d{4})/;
				return phone.replace(pho, '$1****$2');
			}
		},
		methods: {
			/**
			 * 菜单点击
			 */
			gridClick(url) {
				uni.navigateTo({
					url
				})
			},

			/**
			 * 修改手机号
			 */
			editMobile() {
				uni.navigateTo({
					url: '/pages/myself/mobile'
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
									console.log('54565')
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
		},
		onShow() {}
	};
</script>

<style lang="scss">
	.ui-user {
		margin: 60rpx 0 0 40rpx;
		display: inline-flex;
		align-items: center;
		justify-content: flex-start;
		color: #ffffff;

		&>* {
			margin-left: 5rpx;
		}

		text {
			font-size: 30rpx;
		}

		button {
			margin-left: 23rpx;
			display: inline-block;
			background: #ffffff;
			color: #f8b203;
			height: 45rpx;
			width: 105rpx;
			font-size: 20rpx;
			line-height: 45rpx;
		}
	}

	.ui-menu {
		margin-top: 214rpx;
		padding: 0 78rpx;

		.ui-menu-item {
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
			box-sizing: border-box;
			padding-bottom: 20rpx;

			text {
				display: inline-flex;
				align-items: center;
				margin-top: 15rpx;
				width: 70%;
				height: 60rpx;
			}
		}
	}
</style>
