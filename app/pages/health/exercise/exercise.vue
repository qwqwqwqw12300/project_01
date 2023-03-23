<template>
	<app-body :bodyStyle="{background: '#F7F7F7' }">
		<app-logo text="心率" top="36rpx" iconUrl="/static/images/question.png"></app-logo>
		<view class="ui-date">
			<date-picker @onSelect="onSelect"></date-picker>
		</view>
		<view class="ui-echart">
			<exexcise-echart></exexcise-echart>
		</view>
		<view class="ui-tab">
			<scroll-view class="scroll" scroll-x>
				<block v-for="(item,index) in list" :key="index">
					<view class="scroll-item" @click="handleTab(item.key)"
						:style="{ 'background': key === item.key ? item.selectedBg : '#FFFFFF'}">
						<view class="icon">
							<u-icon size="40rpx" :name="key === item.key ? item.selectedIconPath : item.iconPath ">
							</u-icon>
						</view>
						<view class="num" :style="{ 'color': key === item.key ?  '#FFFFFF':'#353535'}">
							{{ item.num }}
						</view>
					</view>
				</block>
			</scroll-view>
		</view>
	</app-body>
</template>

<script>
	import exexciseEchart from './components/erexcise-echart'
	export default {
		components: {
			exexciseEchart,
		},
		data() {
			return {
				key: 'step',
				list: [{
					key: 'step',
					iconPath: "/static/images/bushu.png",
					selectedIconPath: "/static/images/bushu_white.png",
					selectedBg: "#61AAF7",
					num: 3000
				}, {
					key: 'calories',
					iconPath: "/static/images/xiaohao.png",
					selectedIconPath: "/static/images/xiaohao_white.png",
					selectedBg: "#E43552",
					num: 3077
				}, {
					key: 'time',
					iconPath: "/static/images/timeclock.png",
					selectedIconPath: "/static/images/timeclock_white.png",
					selectedBg: "#69CA5F",
					num: 303
				}, {
					key: 'stand',
					iconPath: "/static/images/stand2.png",
					selectedIconPath: "/static/images/stand2_white.png",
					selectedBg: "#60D2C9",
					num: 30
				}]
			}
		},
		created() {

		},
		methods: {
			onSelect() {

			},
			handleTab(key) {
				console.log(key, 'kkkkkkkk')
				this.key = key
			}
		}
	}
</script>

<style lang="scss" scoped>
	.ui-date {
		margin-top: 64rpx;
	}

	.ui-echart {}

	.ui-tab {
		margin-top: 30rpx;
		width: 100%;
		/* 		padding: 10rpx 0; */
		display: flex;
		justify-content: space-between;

		.scroll {
			white-space: nowrap;
			width: 100%;

			.scroll-item {
				display: inline-block;
				height: 128rpx;
				width: 200rpx;
				background: #FFFFFF;
				border-radius: 16rpx;
				padding: 14rpx;
				margin: 0 10rpx;

				.icon {
					margin-top: 4rpx;
					margin-left: 4rpx;
				}

				.num {
					margin-top: 12rpx;
					font-size: 50rpx;
					color: #353535;
					font-weight: 700;
				}

				&:first-child {
					margin-left: 27.27rpx;
				}

				&:last-child {
					margin-right: 27.27rpx;
				}
			}
		}
	}

	scroll-view ::-webkit-scrollbar {
		display: none !important;
		width: 0 !important;
		height: 0 !important;
		-webkit-appearance: none;
		background: transparent;
	}
</style>
