<!--
* @Author: zhanch
* @Date: 2023-01-03 15:28:23
* @FilePath: /components/graphic-input/graphic-input.vue
* @Description: 图形验证码
-->

<template>
	<view>
		<u-text prefixIcon="photo" iconStyle="font-size: 32rpx" text="验证码" color="#444" size="28rpx">
		</u-text>
		<view class="ui-input">
			<u-input placeholder="请输入图形验证码" :border="'none'" fontSize="28rpx" clearable>
				<template slot="suffix">
					<button class="ui-mini" size="mini">验证码</button>
				</template>
			</u-input>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {

			};
		}
	}
</script>

<style lang="scss">
	.ui-input {
		margin: 34rpx 0 60rpx 0;
		border-bottom: 1px solid #e2e2e2;
	}

	.ui-mini {
		width: 200rpx;
	}
</style>
