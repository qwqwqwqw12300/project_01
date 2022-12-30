<!--
* @Author: zhanch
* @Date: 2022-12-28 11:01:47
* @FilePath: /pages/details/details.vue
* @Description: 信息详情
-->

<template>
	<app-body>
		<view id="details">
			<!-- 筛选框 -->
			<view class="ui-screen">
				<view class="ui-list">
					<text>我的家庭:</text>
					<view class="ui-select">
						<uni-data-select v-model="value" :clear="false" :localdata="range"></uni-data-select>
					<!-- 	<view class="ui-icon"><u-icon name="arrow-down-fill" color="#414141" size="30rpx"></u-icon></view> -->
					</view>
				</view>
				<view class="ui-list">
					<text>开始时间:</text>
					<view class="ui-date">
						<text>2022/12/28</text>
						<u-icon name="calendar" color="#414141" size="40rpx"></u-icon>
					</view>
				</view>
				<view class="ui-list">
					<text>结束时间:</text>
					<view class="ui-date">
						<text>2022/12/28</text>
						<u-icon name="calendar" color="#414141" size="40rpx"></u-icon>
					</view>
				</view>
				<view class="ui-list">
					<text>设备类型:</text>
					<view class="ui-select">
						<uni-data-select v-model="value" :clear="false"  :localdata="range"></uni-data-select>
					
					</view>
				</view>
				<view class="ui-list">
					<text>事件类型:</text>
					<view class="ui-select">
						<uni-data-select v-model="value" :clear="false"  :localdata="range"></uni-data-select>
						
					</view>
				</view>
				<view class="ui-list">
					<text>已读标识:</text>
					<view class="ui-select">
						<uni-data-select v-model="value" :clear="false"  :localdata="range"></uni-data-select>
						
					</view>
				</view>
			</view>
			<!-- /筛选框 -->
			<!-- 已读按钮 -->
			<view class="ui-read active">
				<u-text prefixIcon="checkmark-circle"  align="center" :iconStyle="{fontSize: '30rpx', color: '#fff'}" color="#fff" size="24rpx" text="全部标记为已读"></u-text>
			</view>
			<!-- /已读按钮 -->
			<!-- 未读事件列表 -->
			<scroll-view scroll-y="true" class="ui-scroll">
				<view class="ui-message">
					<u-icon name="chat" color="#414141" size="40rpx"></u-icon>
					<text>2022/12/26 10:00:00</text>
					<text>xx设备</text>
					<text>人员离开</text>
				</view>
				<!-- /sos事件 -->
				<view class="ui-sos">
					<view>
						<u-icon name="arrow-right" color="#414141" size="40rpx"></u-icon>
						<text>2022/12/26 10:00:00</text>
						<text>xx设备</text>
						<text>人员摔倒</text>
					</view>
					<view class="ui-sos-btn active">
						<u-text prefixIcon="phone" align="center" :block="false" iconStyle="font-size: 40rpx" size="24rpx" text="紧急电话"></u-text>
						<text>132222222222</text>
					</view>
				
				</view>
				<!-- /sos事件 -->
			<view class="ui-message">
				<u-icon name="chat" color="#414141" size="40rpx"></u-icon>
				<text>2022/12/26 10:00:00</text>
				<text>xx设备</text>
				<text>人员离开</text>
			</view>
			</scroll-view>
			<!-- /未读事件列表 -->
		</view>
	</app-body>
</template>

<script>
export default {
	data() {
		return {
			value: 0,
			range: [{ value: 0, text: '设备一' }, { value: 1, text: '设备二' }, { value: 2, text: '设备三' }]
		};
	},
	methods: {}
};
</script>

<style lang="scss">
view {
	box-sizing: border-box;
}
#details {
	display: inline-block;
	box-sizing: border-box;
	margin-top: 60rpx;
	padding: 0 57rpx;
	text-align: center;
	width: 100%;
	.ui-screen {
		position: relative;
		z-index: 5;
		height: 550rpx;
		width: 100%;
		padding: 30rpx 40rpx;
		border-radius: 10rpx;
		filter: drop-shadow(7.824px 10.382px 8px rgba(7, 5, 5, 0.08));
		background-image: linear-gradient(96deg, #f5f5f5 0%, #e5e5e5 100%);
		.ui-list {
			margin-top: 10rpx;
			width: 100%;
			display: inline-flex;
			justify-content: space-between;
			align-items: center;
			font-size: 28rpx;
			color: #414141;
		}
		.ui-select,
		.ui-date {
			position: relative;
			height: 70rpx;
			width: 68%;
			text-align: left;
			border-radius: 10px;
			background-color: #dcdcdc;
			display: flex;
		}
		.ui-date {
			align-items: center;
			justify-content: space-between;
			padding: 0 8rpx 0 20rpx;
		}
		.ui-icon {
			position: absolute;
			right: 10rpx;
			top: 50%;
			transform: translateY(-50%);
			pointer-events: none;
		}
	}
	.ui-read {
		background-color: rgba(0, 0, 0, 0.4);
		width: 100%;
		height: 60rpx;
		line-height: 60rpx;
		border-radius: 10rpx;
	}
	.ui-scroll {
		margin-top: 20rpx;
		height: calc(100vh - (var(--window-bottom) + 610rpx + 80rpx));
		.ui-message,.ui-sos  {
			margin-top: 20rpx;
			border-radius: 10rpx;
			padding: 0 20rpx;
			font-size: 28rpx;
			filter: drop-shadow(7.824rpx 10.382rpx 8rpx rgba(7,5,5,0.08));
			background-image: linear-gradient(96deg, #f5f5f5 0%, #e5e5e5 100%);
		}
		.ui-message {
			color: #414141;
			display: flex;
			align-items: center;	
			min-height: 80rpx;
			&>*{
				margin-left: 15rpx;
				text-align: left;
				// min-width: 100rpx;
				&:nth-child(2) {
					margin-left: 2rpx;
				}
			}
		}
		.ui-sos {
			padding: 20rpx;
			min-height: 200rpx;
			&>view {
				display: flex;
				align-items: center;
				&>*{
					margin-left: 15rpx;
					text-align: left;
					// min-width: 100rpx;
					&:nth-child(2) {
						margin-left: 2rpx;
					}
				}
				&:nth-child(1) {
					margin-bottom: 30rpx;
				}
			}
			.ui-sos-btn {
				flex-direction: column;
				justify-content: center;
				margin: 0 auto;
				padding: 20rpx;
				width: 545rpx;
				height: 120rpx;
				border-radius: 10rpx;
				background-image: linear-gradient(-43deg, #fdc92d 0%, #ffe383 100%);
				&>* {
					flex: 1;
					font-size: 24rpx;
					color: #414141;
				}
			}
		}
	}
}
</style>
