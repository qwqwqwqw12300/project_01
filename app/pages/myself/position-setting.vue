<!--
* @Author: zhanch
* @Date: 2023-01-05 17:15:25
* @FilePath: /pages/myself/position-setting.vue
* @Description: 区域设置
-->

<template>
	<app-body>
		<view id="setting">
			<view class="ui-movable">
				<movable-area>
					<movable-view
						v-for="(item, index) of list"
						:key="index"
						:x="item.x"
						:y="item.y"
						direction="all"
						@change="
							e => {
								onChange(e, index);
							}
						"
					>
						{{ item.old.x }}
					</movable-view>
				</movable-area>
			</view>
			<view class="ui-add">
				<view>
					<u-text prefixIcon="file-text" iconStyle="font-size: 40rpx" text="子区域名称" color="#444" size="28rpx"></u-text>
					<view class="ui-input"><u-input placeholder="请输入子区域名称" :border="'none'" fontSize="28rpx" clearable></u-input></view>
				</view>
				<view>
					<u-text prefixIcon="file-text" iconStyle="font-size: 40rpx" text="子区域尺寸" color="#444" size="28rpx"></u-text>
					<view class="ui-shape">
						<view>
							<text>长</text>
							<view class="ui-input"><u-input placeholder="请输入长度" :border="'none'" fontSize="28rpx"></u-input></view>
						</view>
						<view>
							<text>宽</text>
							<view class="ui-input"><u-input placeholder="请输入宽度" :border="'none'" fontSize="28rpx"></u-input></view>
						</view>
					</view>
				</view>
				<view class="ui-add-btn"><button class="wd-sms">添加子区域</button></view>
			</view>
			<view class="ui-list">
				<button class="active">子区域</button>
				<button>子区域</button>
				<button>子区域</button>
				<button>子区域</button>
				<button>子区域</button>
			</view>
			<view class="ui-setting">
				<view>
					<u-checkbox-group v-model="checkboxValue1" placement="column">
						<u-checkbox activeColor="#1aa208" :customStyle="{ marginBottom: '8px' }" label="跌倒监控" name="fall"></u-checkbox>
						<u-checkbox activeColor="#1aa208" :customStyle="{ marginBottom: '8px' }" label="进出监控" name="access"></u-checkbox>
					</u-checkbox-group>
					<view class="ui-date-list">
						<text>开始监控时间:</text>
						<view class="ui-date active" @click="openDate('start')">
							<text>2022-01-01 00:00:00</text>
							<u-icon name="calendar" color="#414141" size="40rpx"></u-icon>
						</view>
					</view>
					<view class="ui-date-list ui-margin">
						<text>结束监控时间:</text>
						<view class="ui-date active" @click="openDate('start')">
							<text>2022-01-01 00:00:00</text>
							<u-icon name="calendar" color="#414141" size="40rpx"></u-icon>
						</view>
					</view>
					<view class="ui-timing">
						<view class="ui-timing-pos">
							<u-checkbox-group v-model="checkboxValue1" placement="column">
								<u-checkbox activeColor="#1aa208" :customStyle="{ marginBottom: '40rpx' }" label="进入监控区域超时报警时间" name="fall"></u-checkbox>
								<u-checkbox activeColor="#1aa208" label="离开监控区域超时报警时间" name="access"></u-checkbox>
							</u-checkbox-group>
						</view>
						<view class="ui-timing-pos">
							<uni-data-select v-model="date" :clear="false" :localdata="range"></uni-data-select>
							<uni-data-select v-model="date" :clear="false" :localdata="range"></uni-data-select>
						</view>
						
					</view>
				
				</view>
				
			</view>
		</view>
	</app-body>
</template>

<script>
export default {
	data() {
		return {
			list: [
				{
					x: 0,
					y: 0,
					scale: 2,
					old: {
						x: 0,
						y: 0,
						scale: 2
					},
					name: '床'
				}
			],
			count: 1,
			range: [
				{
					value: '0',
					text: '五分钟',
				},
				{
					value: '1',
					text: '十分钟',
				}
			],
			date: '0'
		};
	},
	methods: {
		onChange: function(e, index) {
			this.list[index].old.x = e.detail.x;
			this.list[index].old.y = e.detail.y;
		},
		add() {
			this.list.push({
				x: 0,
				y: 0,
				scale: 2,
				old: {
					x: 0,
					y: 0,
					scale: 2
				},
				name: '床' + this.count++
			});
		},
		del() {
			this.list.pop();
		}
	}
};
</script>

<style lang="scss">
.ui-movable {
	// height: 300rpx;
	width: 100%;
	padding: 100rpx;
	box-sizing: border-box;
}

movable-area {
	height: 600rpx;
	width: 100%;
	background-color: #d8d8d8;
	overflow: hidden;
}

movable-view {
	display: flex;
	align-items: center;
	justify-content: center;
	height: 150rpx;
	width: 150rpx;
	background-color: #007aff;
	color: #fff;
}

.ui-item {
	display: flex;
	height: 115rpx;
	width: 640rpx;
	margin: 0 calc((100% - 641rpx) / 2);
	border-radius: 10px;
	filter: drop-shadow(7.824px 10.382px 8px rgba(7, 5, 5, 0.08));
	background-image: linear-gradient(96deg, #f5f5f5 0%, #e5e5e5 100%);
}
.ui-add {
	padding: 0 100rpx;
}
.ui-input {
	margin: 34rpx 0 60rpx 0;
	border-bottom: 1px solid #e2e2e2;
}
.wd-sms {
	width: 276rpx;
	height: 82rpx;
	border-radius: 35rpx;
	font-size: 28rpx;
	line-height: 82rpx;
}
.ui-shape {
	display: flex;
	flex-direction: row;
	justify-content: space-between;
	width: 100%;
	margin: 34rpx 0 60rpx 0;
	font-size: 28rpx;
	> view {
		width: 250rpx;
		display: inline-flex;
		flex-direction: row;
		align-items: center;
		vertical-align: middle;
		.ui-input {
			margin: 0 0 0 10rpx;
		}
	}
}
.ui-list {
	margin: 40rpx 0;
	padding: 0 40rpx;
	box-sizing: border-box;
	.active {
		background: #f8b551;
		color: #fff;
	}
	button {
		height: 56rpx;
		width: 140rpx;
		color: #444;
		background: #e5e5e5;
		font-size: 20rpx;
		display: inline-block;
		margin: 10rpx;
	}
}
.ui-setting {
	width: 100%;
	text-align: center;
	>view {
		padding: 35rpx;
		box-sizing: border-box;
		display: inline-block;
		width: 538rpx;
		height: 645rpx;
		border-radius: 10rpx;
		background-image: linear-gradient(0deg, #eeeeee 0%, #ffffff 100%);
		border: 1rpx solid #bfbfbf;
	}
	.ui-date-list {
		margin-top: 10rpx;
		width: 100%;
		display: inline-flex;
		justify-content: space-between;
		align-items: center;
		font-size: 20rpx;
		color: #414141;
	}
	.ui-margin {
		margin-bottom: 60rpx;
	}
	.ui-date {
		position: relative;
		height: 60rpx;
		width: 60%;
		text-align: left;
		border-radius: 10rpx;
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
	
	.ui-timing {
		display: flex;
		flex-direction: row;
		justify-content: space-between;
		.ui-timing-pos {
			width: 250rpx;
			&:nth-child(2) {
				width: 180rpx;
			}
			&>* {
				margin-bottom: 30rpx;
			}
		}
		
	}
}
</style>
