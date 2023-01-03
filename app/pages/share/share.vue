<template>
	<app-body>
		<view id="share">
			<u-navbar leftText="首页" @rightClick="rightClick" :autoBack="true" bgColor="transparent" leftIconColor="#fff"
				:fixed="false" />
			<view class="ui-user">
				<text>已分享给</text>
				<view>
					<view class="ui-user-item">
						<u--text prefixIcon="account-fill" :iconStyle="{ color: '#fff', fontSize: '36rpx' }"
							color="#fff" text="用户1"></u--text>
						<u-icon class="active" name="close-circle" color="#fff600" size="36rpx"></u-icon>
					</view>
					<view class="ui-user-item">
						<u--text prefixIcon="account-fill" :iconStyle="{ color: '#fff', fontSize: '36rpx' }"
							color="#fff" text="用户1"></u--text>
						<u-icon class="active" name="close-circle" color="#fff600" size="36rpx"></u-icon>
					</view>
				</view>
			</view>
			<view class="ui-form">
				<view class="ui-form-item">
					<u-text prefixIcon="phone" iconStyle="font-size: 30rpx" text="分享人手机号码" color="#444" size="28rpx">
					</u-text>
					<view class="ui-input">
						<u--input placeholder="请输入手机号码" :border="'none'" fontSize="28rpx" clearable></u--input>
					</view>
				</view>
				<view class="ui-form-item">
					<u-text prefixIcon="email" iconStyle="font-size: 32rpx" text="短信码" color="#444" size="28rpx">
					</u-text>
					<view class="ui-input">
						<u-input placeholder="请输入短信认证码" :border="'none'" fontSize="28rpx" clearable>
							<template slot="suffix">
								<button class="wd-sms ui-mini" size="mini">验证码</button>
							</template>
						</u-input>
					</view>
				</view>
				<view class="wd-btn-gloup"><button @click="submit">提交</button></view>
			</view>
		</view>
		<u-popup :closeable="true" :round="10" :show="show" mode="center" @close="close">
			<view class="ui-code"><canvas id="qrcode" canvas-id="qrcode"
					:style="{ width: `${size}px`, height: `${size}px` }"></canvas></view>
		</u-popup>
	</app-body>
</template>

<script>
	import UQRCode from '@/uni_modules/Sansnn-uQRCode/js_sdk/uqrcode/uqrcode.js';
	export default {
		data() {
			return {
				show: false,
				text: 'uQRCode',
				size: 200
			};
		},
		methods: {
			submit() {
				this.show = true;
				/* 获取uQRCode实例 */
				this.$nextTick(() => {
					const qr = new UQRCode();
					/* 设置二维码内容 */
					qr.data = this.text;
					/* 设置二维码大小，必须与canvas设置的宽高一致 */
					qr.size = this.size;
					/* 设置二维码前景图 */
					qr.foregroundImageSrc = '/static/images/tb.png';
					qr.foregroundImagePadding = 4;
					qr.foregroundImageBorderRadius = 4;
					qr.foregroundImageShadowOffsetX = 0;
					qr.foregroundImageShadowOffsetY = 0;
					/* 调用制作二维码方法 */
					qr.make();
					const canvasContext = uni.createCanvasContext('qrcode');
					/* 设置uQRCode实例的canvas上下文 */
					qr.canvasContext = canvasContext;
					/* 调用绘制方法将二维码图案绘制到canvas上 */
					qr.drawCanvas();
				});
			},

			close() {
				this.show = false;
			}
		}
	};
</script>
r
<style lang="scss">
	#share {
		text-align: center;
	}

	.ui-code {
		height: 600rpx;
		width: 500rpx;
		padding: 50rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.ui-user {
		margin-top: 90rpx;
		box-sizing: border-box;
		padding: 25rpx 47rpx;
		display: inline-block;
		width: 684rpx;
		min-height: 200rpx;
		border-radius: 40rpx;
		background-color: rgba(0, 0, 0, 0.4);
		color: #fff;
		text-align: left;
		font-size: 30rpx;

		.ui-user-item {
			margin: 10rpx 20rpx 0 0;
			display: inline-flex;
			align-items: center;

			&>*:nth-child(2) {
				margin-left: 10rpx;
				margin-top: 10rpx;
			}
		}
	}

	.ui-form {
		margin-top: 156rpx;
		padding: 0 80rpx;
		padding-bottom: 120rpx;

		.ui-input {
			margin: 34rpx 0 60rpx 0;
			border-bottom: 1px solid #e2e2e2;
		}

		.ui-bot {
			display: flex;
			align-items: center;
			justify-content: space-between;
			font-size: 26rpx;
			color: #484848;
		}

		.ui-mini {
			width: 160rpx;
			// height: 60rpx;
		}

		.wd-btn-gloup {
			margin: 120rpx 0;
		}
	}
</style>
