<template>
	<app-body :bg="false">
		<view class="ui-logo">
			<app-logo color="#353535" text="编辑个人资料"></app-logo>
		</view>
		<view class="ui-info">
			<u-cell-group>
				<u-cell title="头像" size="large" :isLink="true" @tap="routerJump('/pages/myself/edit-avatar')">
					<view slot="value" class="">
						<u-avatar mode="aspectFill" src="https://cdn.uviewui.com/uview/album/2.jpg" size="32">
						</u-avatar>
					</view>
				</u-cell>
				<u-cell title="昵称" size="large" :isLink="true" value="木木鱼"
					@tap="routerJump('/pages/myself/edit-nick')">
				</u-cell>
				<u-cell title="修改手机号" size="large" :isLink="true" value="13456374888"
					@tap="routerJump('/pages/myself/mobile/new-mobile')">
				</u-cell>
			</u-cell-group>
		</view>
	</app-body>
</template>

<script>
	export default {
		data() {
			return {

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
