<template>
	<app-body :bodyStyle="{background: '#F7F7F7' }">
		<app-logo text="心率" top="36rpx" iconUrl="/static/images/question.png"></app-logo>
		<view class="ui-date">
			<date-picker @onSelect="onSelect"></date-picker>
		</view>
		<view class="ui-echart">
			<exexcise-echart></exexcise-echart>
		</view>
		<view class="day-title">
			<text>日平均数据</text>
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
		<view class="detail-title">
			详情
		</view>
		<view class="ui-detail">
			<view class="detail-box">
				<view class="box-cell">
					<view class="title">
						20:37 - 20:53 步行
					</view>
					<view class="content">
						<view class="c">
							<u-icon size="30" name="/static/images/bushu.png" />
							<text>97</text>步
						</view>
						<view class="c">
							<u-icon size="40rpx" name="arrow-left" />
							<text>0.1</text>公里
						</view>
						<view class="c">
							<text>0.1</text>千卡
						</view>
					</view>
				</view>
			</view>
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

	.day-title {
		margin-top: 30rpx;
		height: 50rpx;
		line-height: 50rpx;
		padding: 0 32rpx;
		font-size: 32rpx;
		color: #353535;
		font-weight: 550;
	}

	.detail-title {
		margin-top: 30rpx;
		height: 50rpx;
		line-height: 50rpx;
		padding: 0 32rpx;
		font-size: 32rpx;
		color: #353535;
		font-weight: 550;
	}

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

	.ui-detail {
		padding: 0 32rpx;
		margin-top: 20rpx;

		.detail-box {
			background: #FFFFFF;
			border-radius: 16rpx;
			padding: 30rpx 20rpx;

			.box-cell {
				.title {
					height: 50rpx;
					line-height: 50rpx;
					font-size: 30rpx;
					color: #353535;
				}

				.content {
					height: 120rpx;
					display: flex;
					align-items: center;
					justify-content: space-between;
					border-bottom: solid 2px #f7f7f7;

					&:last-child {
						border: none !important;
					}

					.c {

						display: flex;
						align-items: center;
						font-size: 26rpx;
						color: #888888;

						text {
							margin-left: 10rpx;
							font-size: 40rpx;
							color: #353535;
							font-weight: 600;
						}
					}
				}

			}
		}
	}
</style>
