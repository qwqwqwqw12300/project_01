<template>
	<app-body>
		<app-logo text="意见与建议"></app-logo>
		<view class="ui-form">
			<view class="ui-form-item">
				<u-text prefixIcon="/static/images/suggestion.png" iconStyle="font-size: 32rpx" text="您的问题与建议"
					color="#444" size="28rpx"></u-text>
				<view class="ui-textarea">
					<u--textarea height="100" placeholder="请输入您的问题或建议"></u--textarea>
				</view>
			</view>
			<view class="ui-form-item">
				<u-text prefixIcon="/static/images/code.png" iconStyle="font-size: 32rpx" text="验证码" color="#444"
					size="28rpx"></u-text>
				<view class="ui-input">
					<u-input placeholder="请输入短信认证码" :border="'none'" fontSize="28rpx" clearable>
						<template slot="suffix">
							<button class="wd-sms ui-mini" size="mini">验证码</button>
						</template>
					</u-input>
				</view>
			</view>
			<view class="wd-btn-gloup"><button class="ui-button" @click="submit">提交</button></view>
		</view>
	</app-body>
</template>

<script>
	export default {
		data() {
			return{
				
			}
		},
		methods:{
			submit() {
				
			}
		}
	}
</script>

<style lang="scss" scoped>
	.ui-form {
		margin-top: 256rpx;
		padding: 0 80rpx;
		padding-bottom: 120rpx;

		.ui-input {
			margin: 34rpx 0 60rpx 0;
			border-bottom: 1px solid #e2e2e2;
		}

		.ui-textarea {
			margin: 34rpx 0 60rpx 0;
			border-bottom: 1px solid #e2e2e2;
		}
	}
	
	.wd-btn-gloup {
		margin: 120rpx 0;
		.ui-button{
			width: 100%;
			height: 80rpx;
		}
	}
</style>
