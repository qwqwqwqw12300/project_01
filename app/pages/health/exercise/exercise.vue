<template>
	<view>
		<app-body :bg="false" :hideTitle="true">
			<view class="ui-body">
				<scroll-view :scroll-x="true" class="scrollview-box">
					<view class="ui-scroll-block">
						<view class="ui-block" v-for="(item,index) in iconList" :key="index" @click="tabIndex = index" :style="{ 'background': tabIndex === index ? item.selectedBg : '#FFFFFF'}">
							<u-icon size="40rpx" :name="tabIndex === index ? item.selectedIconPath : item.iconPath "></u-icon>
							<view class="ui-font-50 ui-mar-l-10">5000</view>
						</view>
					</view>
				</scroll-view>
			</view>
		</app-body>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				tabIndex:0,
				iconList: [{
						"iconPath": "/static/images/bushu.png",
						"selectedIconPath": "/static/images/bushu_white.png",
						"selectedBg":"#61AAF7",
						"text": "5000"
					},
					{
						"iconPath": "/static/images/xiaohao.png",
						"selectedIconPath": "/static/images/xiaohao_white.png",
						"selectedBg":"#E43552",
						"text": "300"
					},
					{
						"iconPath": "/static/images/timeclock.png",
						"selectedIconPath": "/static/images/timeclock_white.png",
						"selectedBg":"#69CA5F",
						"text": "30"
					},
					{
						"iconPath": "/static/images/stand2.png",
						"selectedIconPath": "/static/images/stand2_white.png",
						"selectedBg":"#60D2C9",
						"text": "10"
					}
				]
			}
		},
		created() {

		},
		methods: {

		}
	}
</script>

<style>
	.scrollview-box {
		white-space: nowrap; // 滚动必须加的属性
		width: 100%;
		padding: 20rpx 20rpx 20rpx 20rpx;
	}

	.ui-body {
		padding: 32rpx;
	}

	.ui-block {
		display: inline-block;
		height: 128rpx;
		width: 200rpx;
		background: #FFFFFF;
		border-radius: 16rpx;
		padding: 10rpx;
	}

	.ui-flex-center {
		display: flex;
		justify-content: center;
		flex-wrap: nowrap;
		align-items: center;
	}

	.ui-scroll-block .ui-block~.ui-block {
		margin-left: 20rpx;
	}

	.ui-font-50 {
		font-size: 50rpx;
		color: #353535;
		text-align: left;
		font-weight: 700;
	}

	.ui-img-size-40 {
		width: 40rpx;
		height: 40rpx;
	}

	.ui-mar-l-10 {
		margin-left: 10rpx;
	}
</style>
