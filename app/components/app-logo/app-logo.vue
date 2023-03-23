<template>
	<view class="wd-logo" :style="{marginTop: top, color: color}">
		<text>
			{{text}}
		</text>
		<u-icon v-if="isShare" name="../../../static/images/share@3x.png" size="26"></u-icon>
		<slot></slot>
	</view>

</template>

<script>
	export default {
		name: "app-logo",
		props: {
			/**是否展示客服**/
			text: {
				default: '艾吉通'
			},
			/**距离顶部高度**/
			top: {
				default: '64rpx'
			},
			/**是否需要背景**/
			color: {
				type: String,
				default: '#35353',
			},
			isShare: {
				type: Boolean,
				default: false
			}
		},
		data() {
			return {

			};
		}
	}
</script>

<style lang="scss">
	.wd-logo {
		// margin: 20rpx 0 0 32rpx;
		padding: 0 32rpx;
		margin-top: 20rpx;
		// display: inline-flex;
		// flex-direction: row;
		display: flex;
		justify-content: space-between;
		font-size: 50rpx;
		font-weight: bold;
		// color: #ffffff;
	}
</style>
