<template>
	<view class="ui-box">
		<city-select @cityClick="cityClick" :formatName="formatName" :activeCity="activeCity" :obtainCitys="obtainCitys"
			:isSearch="true" ref="citys"></city-select>
	</view>
</template>

<script>
	import citys from './citys.js'
	export default {
		data() {
			return {
				//需要构建索引参数的名称（注意：传递的对象里面必须要有这个名称的参数）
				formatName: 'cityName',
				//当前城市
				activeCity: {},
				//显示的城市数据
				obtainCitys: []
			}
		},
		onLoad() {
			const cache = this.$getCache('position') || '';
			this.activeCity = {
				id: 1,
				cityName: cache
			}
			this.obtainCitys = citys
		},
		methods: {
			cityClick(item) {
				uni.$emit('citySelect', item)
				uni.navigateBack()
				// uni.showToast({
				// 	icon: 'none',
				// 	title: 'item: ' + JSON.stringify(item),
				// 	// #ifdef MP-WEIXIN
				// 	duration: 3000,
				// 	// #endif
				// 	mask: true
				// })
			}
		}
	}
</script>

<style lang="scss" scoped>
	.ui-box {
		padding-top: calc(var(--status-bar-height) + 20rpx);
	}
</style>