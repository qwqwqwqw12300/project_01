<template>
	<app-body :bg="false">
		<view class="ui-logo">
			<app-logo color="#353535" text="编辑个人资料"></app-logo>
		</view>
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
	</app-body>
</template>

<script>
	import {
		env
	} from '@/config/env';
	import {
		phoneHide
	} from '../../common/utils/util';
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
			}
		}
	}
</script>

<style lang="scss" scoped>
	.ui-logo {
		background: #ffffff;
		padding-bottom: 100rpx;
	}

	.ui-info {
		background: #ffffff;
	}
</style>
