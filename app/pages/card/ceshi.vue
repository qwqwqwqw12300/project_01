<template>
	<view class="box">
		<date-picker :lightDot="dotArr" @onSelect="onSelect" @month="onMonth"></date-picker>
	</view>
</template>

<script>
	export default {
		data() {
			return {
					dotArr: ['2023-03-01', '2023-03-05']
			}
		},
		methods: {
			onSelect(val) {
				console.log(val, '000')
			},
			onMonth(e) {
				console.log(e, 'eee')
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
