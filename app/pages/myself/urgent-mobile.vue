<!-- 添加联系人 -->
<template>
	<app-body>
		<view class="ui-list">
			<view class="ui-list-card">
				<view class="card-header">
					<u-icon name="/static/images/contact.png" size="60rpx" />
					<text class="text">第一紧急联系人</text>
				</view>
				<view class="card-content">
					<view class="name">
						<u-icon name="/static/images/phone_white.png" size="44rpx" />
						<text>张三</text>
					</view>
					<text class="tel">15900008888</text>
					<u-icon name="/static/images/close.png" size="54rpx" />
				</view>
			</view>
		</view>
		<view class="ui-btn">
			<button>添加紧急联系人</button>
		</view>
	</app-body>
</template>

<script>
	export default {

	}
</script>

<style lang="scss">
	.ui-list {
		margin-top: 20rpx;
		padding: 0 50rpx;

		.ui-list-card {
			padding: 30rpx 30rpx;
			border-radius: 14rpx;
			background: linear-gradient(#F5F5F5, #E5E5E5);

			.card-header {
				display: flex;
				align-items: center;

				.text {
					margin-left: 26rpx;
					color: #333;
					font-size: 38rpx;
					font-weight: bold;
				}
			}

			.card-content {
				margin-top: 50rpx;
				margin-bottom: 20rpx;
				display: flex;
				justify-content: space-between;
				align-items: center;

				.name {
					padding: 20rpx 60rpx 20rpx 30rpx;
					border-radius: 16rpx;
					background-color: #FEC92E;
					display: flex;
					align-items: center;
					font-size: 36rpx;
					font-weight: bold;

					text {
						margin-left: 14rpx;
					}
				}

				.tel {
					font-size: 38rpx;
					font-weight: bold;
				}
			}
		}
	}

	.ui-btn {
		position: absolute;
		width: 550rpx;
		margin: 0 auto;
		bottom: 80rpx;
		left: 50%;
		transform: translateX(-50%);
		text-align: center;

		text {
			font-size: 30rpx;
			color: #0094ff;
		}

		&>* {
			&:active {
				opacity: 0.8;
			}
		}

		&>*:nth-child(1) {
			margin-bottom: 50rpx;
		}
	}
</style>
