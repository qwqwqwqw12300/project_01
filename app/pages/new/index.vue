<template>
	<app-body :hideTitle="true" :needService="false">
		<view class="ui-body">

			<view class="ui-cell">
				<u-cell-group>
					<u-cell v-for="(item,index) in cellList" :key="index" size="large" :title="item.title"
						@tap="handleJump(item.url)" :isLink="true" titleStyle="font-size: 15px;color: #303133;">
						<!-- <u-icon slot="icon" size="25" :color="item.icon.color" :name="item.icon.name"></u-icon> -->
					</u-cell>
				</u-cell-group>
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
				
				cellList: [{
					title: '时段禁用',
					url: '/pages/new/time-interval',
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
				},{
					title: '电子牵挂卡',
					url: '/pages/myself/version-manage'
				}]
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
