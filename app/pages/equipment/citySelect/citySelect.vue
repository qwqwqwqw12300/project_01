<!-- 城市选择页面 -->
<template>
	<view class="ui-box">
		<city-select @cityClick="cityClick" :formatName="formatName" :activeCity="activeCity" :obtainCitys="obtainCitys"
			:isSearch="true" ref="citys"></city-select>
	</view>
</template>

<script>
	import citys from './citys.js' //导入城市列表
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
			/**
			 * 选择城市
			 */
			cityClick(item) {
				uni.$emit('citySelect', item)
				uni.navigateBack()
			}
		}
	}
</script>

<style lang="scss" scoped>
	.ui-box {
		padding-top: calc(var(--status-bar-height) + 20rpx);
	}
</style>