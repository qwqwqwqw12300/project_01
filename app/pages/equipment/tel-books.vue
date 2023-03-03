<template>
	<app-body :bodyStyle="{background: 'rgb(247,247,247)' }" :bg="false">
		<view class="ui-navbar">
			<view class="header">
				<text class="title">通讯录白名单</text>
				<view class="action">
					<u-icon name="/static/images/add-contact.png" size="44rpx" style="margin-right: 6rpx;" />
					添加
				</view>
				<view class="action" @click="openTelBooks">
					<u-icon name="/static/images/tel-book.png" size="44rpx" style="margin-right: 6rpx;" />
					通讯录
				</view>
			</view>
			<card-title title="白名单已启用" content="您可以设置允许呼入的联系人" backGroundImg="/static/images/tel-book-bg.png">
			</card-title>
		</view>
		<view class="ui-content">
			<u-swipe-action>
				<u-swipe-action-item :options="options2">
					<view class="cell">
						<view class="cell-box">
							<view class="input">
								<u--input placeholder="请输入姓名" border="none" clearable></u--input>
							</view>
							<view class="input">
								<u--input placeholder="请输入手机号" border="none" clearable></u--input>
							</view>
						</view>
					</view>
				</u-swipe-action-item>
				<u-swipe-action-item :options="options2">
					<view class="cell">
						<view class="cell-box">
							<view class="input">
								<u--input placeholder="请输入姓名" border="none" clearable></u--input>
							</view>
							<view class="input">
								<u--input placeholder="请输入手机号" border="none" clearable></u--input>
							</view>
						</view>
					</view>
				</u-swipe-action-item>
				<u-swipe-action-item :options="options2">
					<view class="cell">
						<view class="cell-box">
							<view class="input">
								<u--input placeholder="请输入姓名" border="none" clearable></u--input>
							</view>
							<view class="input">
								<u--input placeholder="请输入手机号" border="none" clearable></u--input>
							</view>
						</view>
					</view>
				</u-swipe-action-item>
			</u-swipe-action>
		</view>
		<view class="ui-btn">
			<view class="btn-box">
				<view class="cancel-btn" @click="handleCancel">
					取消
				</view>
				<view class="save-btn" @tap="handleSave">
					保存
				</view>
			</view>
		</view>
		<tel-books ref="telBookRefs"></tel-books>
	</app-body>
</template>

<script>
	export default {
		data() {
			return {
				options2: [{
					text: '删除',
					style: {
						backgroundColor: '#f56c6c'
					}
				}],
			}
		},
		methods: {
			handleCancel() {

			},
			handleSave() {

			},
			openTelBooks() {
				this.$refs.telBookRefs.show(false)
			}
		}

	}
</script>

<style lang="scss" scoped>
	.ui-navbar {
		padding: 32rpx;
		background-color: #fff;

		.header {
			height: 60rpx;
			display: flex;
			justify-content: space-between;
			align-items: center;

			.title {
				font-size: 50rpx;
				color: #353535;
				font-weight: 600;
				margin-right: 40rpx;
				// flex: 1;
			}

			.action {
				// margin-left: 20rpx;
				display: flex;
				align-items: center;
			}
		}
	}

	.ui-content {
		margin-top: 30rpx;

		background-color: #fff;

		.cell {
			padding: 0rpx 32rpx;

			.cell-box {
				height: 128rpx;
				border-bottom: solid 2px #f7f7f7;
				display: flex;
				align-items: center;
				justify-content: space-between;

				.input {
					width: 240rpx;
				}
			}
		}
	}

	.ui-btn {
		width: 100%;
		position: fixed;
		bottom: 0;
		left: 0;

		.btn-box {
			height: 100rpx;
			line-height: 100rpx;
			display: flex;
			font-size: 36rpx;

			.cancel-btn {
				flex: 1;
				background-color: #fff;
				color: #E95656;
				text-align: center;
			}

			.save-btn {
				flex: 1;
				background-image: linear-gradient(90deg, #FFB24D 0%, #FD913B 100%);
				color: #fff;
				text-align: center;
			}
		}
	}
</style>
