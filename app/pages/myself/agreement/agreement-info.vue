<!-- 协议信息 -->
<template>
	<app-body :bg="false">
		<view class="ui-menu">
			<app-logo text="协议信息"></app-logo>
			<view class="ui-ag">
				<u-cell-group>
					<u-cell :title="baseListItem.title" v-for="(baseListItem, baseListIndex) in baseList"
						:key="baseListIndex" isLink @click="gridClick(baseListItem.url)"></u-cell>
				</u-cell-group>

			</view>
		</view>
	</app-body>
</template>

<script>
	export default {
		data() {
			return {
				baseList: [{
						title: '隐私协议',
						pic: 'privacy.png',
						url: '/pages/myself/agreement/agreement-detail?type=0'
					},
					{
						title: 'APP协议',
						pic: 'agreement.png',
						url: '/pages/myself/agreement/agreement-detail?type=1'
					}
				]
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
		}
	}
</script>

<style lang="scss" scoped>
	.ui-menu {
		margin-top: 20rpx;
		background: #fff;
		padding: 0 32rpx;

		.ui-ag {
			margin-top: 50rpx;
		}
	}
</style>
