<template>
	<text class="wd-logo">
		{{text}}
	</text>
</template>

<script>
	export default {
		name: "app-logo",
		props: {
			/**是否展示客服**/
			text: {
				default: '新大陆米家'
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
		margin: 64rpx 0 0 44rpx;
		display: inline-block;
		font-size: 72rpx;
		font-weight: bold;
		color: #ffffff;
	}
</style>
