<template>
	<view class="box">
		<date-picker @onSelect="onSelect"></date-picker>
	</view>
</template>

<script>
	export default {
		data() {
			return {

			}
		},
		methods: {
			onSelect(val) {
				console.log(val, '000')
			}
		}
	}
</script>

<style lang="scss" scoped>
	.box {
		padding-top: 100rpx;
		width: 100vw;
		height: 100vh;
		background-color: #F7F7F7;
	}
</style>
