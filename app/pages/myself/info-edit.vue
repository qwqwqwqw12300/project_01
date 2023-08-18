<template>
	<app-body :bg="false" title="编辑个人资料">
		<!-- <view class="ui-logo">
			<app-logo color="#353535" text="编辑个人资料"></app-logo>
		</view> -->
		<view class="ui-info">
			<u-cell-group>
				<u-cell title="头像" size="large" :isLink="true" @tap="routerJump('/pages/myself/edit-avatar')">
					<view slot="value" class="">
						<u-avatar mode="aspectFill" :src="userInfo.avatar" size="32">
						</u-avatar>
					</view>
				</u-cell>
				<u-cell title="昵称" size="large" :isLink="true" :value="userInfo.nickname || '暂无昵称'"
					@tap="routerJump('/pages/myself/edit-nick')">
				</u-cell>
				<u-cell title="手机号" size="large" :isLink="true" :value="userInfo.phone"
					@tap="routerJump('/pages/myself/mobile/select-way')">
				</u-cell>
			</u-cell-group>
		</view>
		<view class="ui-button">
			<button @click="loginOut">
				注销
			</button>
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
		MemberCancel,
	} from '@/common/http/api.js';
	import {
		removeToken
	} from '@/common/utils/auth.js';
	export default {
		data() {
			return {

			}
		},
		computed: {
			userInfo() {
				const obj = uni.$u.deepClone(this.$store.getters.userInfo)
				obj.avatar = `${env.basePath}${obj.avatar}`
				obj.phone = phoneHide(obj.phone || '')
				return obj
			}
		},
		methods: {
			routerJump(url) {
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
							MemberCancel({}).then(() => {
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
	.ui-logo {
		margin-top: 20rpx;
		background: #ffffff;
		padding-bottom: 100rpx;
	}

	.ui-info {
		background: #ffffff;
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
