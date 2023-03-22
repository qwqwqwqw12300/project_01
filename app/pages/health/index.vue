<template>
	<app-body :hideTitle="true">
		<view class="ui-banner">
			<swiper class="ui-swiper" circular :indicator-dots="true" :autoplay="false" @change="swiperChange"
				:current="current">
				<swiper-item>
					<swiper-device></swiper-device>
				</swiper-item>
			</swiper>
		</view>
	</app-body>
</template>

<script>
	import SwiperDevice from './components/swiper-device.vue';
	export default {
		components: {
			SwiperDevice
		},
		data() {
			return {
				current: 0,
			}
		},
		methods: {
			swiperChange(val) {

			}
		}
	}
</script>

<style lang="scss" scoped>
	.ui-banner {
		margin-top: 60rpx;
		padding: 0 20rpx;
	}

	.ui-swiper {
		height: 320rpx;
	}
</style>
