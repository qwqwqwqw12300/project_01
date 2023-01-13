<!-- 协议信息 -->
<template>
	<app-body>
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
	export default {
		data() {
			return {
				baseList: [{
						title: '隐私协议',
						pic: 'privacy.png',
						url: '/pages/myself/privacy-info'
					},
					{
						title: 'APP协议',
						pic: 'agreement.png',
						url: '/pages/myself/app-agreement-info'
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
