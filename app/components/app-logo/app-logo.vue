<template>
	<text class="wd-logo" :style="{marginTop: top}">
		{{text}}
	</text>
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
		},
		data() {
			return {

			};
		}
	}
</script>

<style lang="scss">
	.wd-logo {
		margin: 64rpx 0 0 44rpx;
		display: inline-block;
		font-size: 72rpx;
		font-weight: bold;
		color: #ffffff;
	}
</style>
