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
						:style="{ height: item.height + 'rpx', width: item.width + 'rpx' }"
						direction="all"
						@change="
							e => {
								onChange(e, index);
							}
						"
					>
					<view class="ui-device" v-if="item.zoneName === '设备'">
						<text>设备</text>
						<text>x：{{ item.old.x }}</text>
						<text>y：{{ item.old.y }}</text>
					</view>
					<template v-else>
						<text>{{ item.zoneName || '未命名' }}</text>
						<text>x：{{ item.old.x }}</text>
						<text>y：{{ item.old.y }}</text>
					</template>
						
					</movable-view>
				</movable-area>
			</view>
			<view class="ui-add">
				<view>
					<u-text prefixIcon="file-text" iconStyle="font-size: 40rpx" text="子区域名称" color="#444" size="28rpx"></u-text>
					<view class="ui-input"><u-input v-model="zoneInfo.zoneName" placeholder="请输入子区域名称" :border="'none'" fontSize="28rpx" clearable></u-input></view>
				</view>
				<view>
					<u-text prefixIcon="file-text" iconStyle="font-size: 40rpx" text="子区域尺寸" color="#444" size="28rpx"></u-text>
					<view class="ui-shape">
						<view>
							<text>长</text>
							<view class="ui-input"><u-input type="number" v-model="zoneInfo.height" placeholder="请输入长度" :border="'none'" fontSize="28rpx"></u-input></view>
						</view>
						<view>
							<text>宽</text>
							<view class="ui-input"><u-input type="number" v-model="zoneInfo.width" placeholder="请输入宽度" :border="'none'" fontSize="28rpx"></u-input></view>
						</view>
					</view>
				</view>
				<view class="ui-add-btn"><button class="wd-sms" @click="addZone">添加子区域</button></view>
			</view>
			<view class="ui-list">
				<template v-for="(item, index) of list" >
					<button :class="activeZone === index ? 'active' : ''" @click="acitve(index)" :key="index" v-if="index!==0">{{item.zoneName || '未命名'}}</button>
				</template>
				
			</view>
			<!-- 区域设置 -->
			<view class="ui-setting" :key="activeZone" v-if="list.length > 1">
				<view>
					<u-checkbox-group @change="monitorChange($event, 'isFall')" placement="column">
						<u-checkbox activeColor="#1aa208" :checked="list[activeZone].isFall" :customStyle="{ marginBottom: '8px' }" label="跌倒监控" name="isFall"></u-checkbox>
					</u-checkbox-group>
					<u-checkbox-group @change="monitorChange($event, 'isAccess')"  placement="column">
						<u-checkbox activeColor="#1aa208" :checked="list[activeZone].isAccess" :customStyle="{ marginBottom: '8px' }" label="进出监控" name="isAccess"></u-checkbox>
					</u-checkbox-group>
					
					<view class="ui-date-list">
						<text>开始监控时间</text>
						<view class="ui-date active" @click="openDate('startTime')">
							<text>{{list[activeZone].startTime || '未设置'}}</text>
							<u-icon name="calendar" color="#414141" size="40rpx"></u-icon>
						</view>
					</view>
					<view class="ui-date-list ui-margin">
						<text>结束监控时间</text>
						<view class="ui-date active" @click="openDate('endTime')">
							<text>{{list[activeZone].endTime || '未设置'}}</text>
							<u-icon name="calendar" color="#414141" size="40rpx"></u-icon>
						</view>
					</view>
					<view class="ui-timing">
						<view class="ui-timing-pos">
							<u-checkbox-group  placement="column"  @change="monitorChange($event, 'isEntry')">
								<u-checkbox activeColor="#1aa208"  :checked="list[activeZone].isEntry" :customStyle="{ marginBottom: '40rpx' }" label="进入监控区域超时报警时间" name="isEntry"></u-checkbox>
							</u-checkbox-group>
							<u-checkbox-group placement="column" @change="monitorChange($event, 'isDeparture')">
								<u-checkbox activeColor="#1aa208" :checked="list[activeZone].isDeparture"  label="离开监控区域超时报警时间" name="isDeparture"></u-checkbox>
							</u-checkbox-group>
						</view>
						<view class="ui-timing-pos">
							<view class="ui-timing-active active"  @click="openDate('entryTime')">
								<text>{{list[activeZone].entryTime || '请选择'}}</text>
								<u-icon name="arrow-down" size="28rpx"></u-icon>
							</view>
							<view class="ui-timing-active active"  @click="openDate('departureTime')">
								<text>{{list[activeZone].departureTime || '请选择'}}</text>
								<u-icon name="arrow-down" size="28rpx"></u-icon>
							</view>
						</view>
					</view>
				</view>
			</view>
			<!-- /区域设置 -->
		</view>
		<u-datetime-picker v-if="dateHandle.show" :show="dateHandle.show" @confirm="dateConfirm" @cancel="dateHandle.show = false" :mode="dateHandle.mode"></u-datetime-picker>
	</app-body>
</template>

<script>
import { assignDeep } from '../../common/utils/util';
const ZONE = {
	width: 100,
	height: 100,
	zoneName: '',
	x: 0,
	y: 0,
	z: 0,
	scale: 2,
	old: {
		x: 0,
		y: 0,
		scale: 2
	},
	/**是否开启进入报警**/
	isEntry: false,
	/**是否开启离开报警**/
	isDeparture: false,
	/**进入报警时间**/
	entryTime: '',
	/**离开报警时间**/
	departureTime: '',
	/**开始监控时间**/
	startTime: '',
	/**结束监控时间**/
	endTime: '',
	/**是否开启跌倒监控**/
	isFall: false,
	/**是否开启进出监控**/
	isAccess: false
};
export default {
	data() {
		return {
			zoneInfo: assignDeep({}, ZONE),
			list: [
				{
					zoneName: '设备',
					x: 120,
					y: 100,
					z: 0,
					scale: 2,
					old: {
						x: 0,
						y: 0,
						scale: 2
					},
				}
			],
			checkboxValue1: [],
			range: [
				{
					value: '0',
					text: '五分钟'
				},
				{
					value: '1',
					text: '十分钟'
				}
			],
			dateHandle: {
				type: '',
				show: false,
				mode: 'datetime'
			},
			value: null,
			/**选择的子区域**/
			activeZone: 1
		};
	},
	mounted() {},
	methods: {
		/**
		 * 添加区域
		 */
		addZone() {
			this.list.push(this.zoneInfo);
			this.zoneInfo = assignDeep({}, ZONE);
		},
		/**
		 * 区域修改
		 * @param {Object} e
		 * @param {string} 下标 
		 */
		onChange: function(e, index) {
			this.list[index].old.x = e.detail.x;
			this.list[index].old.y = e.detail.y;
		},
		/**
		 * 选择子区域
		 * @param {string} 下标 
		 */
		acitve(idx) {
			this.activeZone = idx;
		},
		del() {
			this.list.pop();
		},
		
		/**
		 * 监控区域修改
		 */
		monitorChange([active], type) {
			this.list[this.activeZone][type] = !!active;
		},
		/**
		 * 开启选择时间
		 */
		openDate(type) {
			const MAP = {
				startTime: 'datetime',
				endTime: 'datetime',
				departureTime: 'time',
				entryTime: 'time'
			};
			Object.assign(this.dateHandle, {
				type,
				show: true,
				mode: MAP[type]
			});
			
		},
		
		/**
		 * 选择时间完成
		 */
		dateConfirm({value}) {
			this.list[this.activeZone][this.dateHandle.type] = (typeof value === 'number') ? uni.$u.date(value, 'yyyy-mm-dd hh:MM:ss') : value;
			this.dateHandle.show = false;
		}
	}
};
</script>

<style lang="scss">
#setting {
	padding-bottom: 30rpx;
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
		flex-direction: column;
		min-height: 100rpx;
		min-width: 100rpx;
		background-color: #007aff;
		color: #fff;
		font-size: 10rpx;
		.ui-device {
			min-height: 120rpx;
			min-width: 120rpx;
			display: flex;
			flex-direction: column;
			text-align: center;
			background-color: #00eaff;
			border-radius: 50% 50%;
		}
		
		@each $idx, $bg in (2: unset, 3: #e5004f, 4: #ec6941, 5: #448aca) {
			&:nth-child(#{$idx}) {
				background-color: $bg;
			}
		}
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
		> view {
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
			
				.ui-timing-active {
					display: flex;
					justify-content: right;
					align-items: center;
					font-size: 20rpx;
					line-height: 50rpx;
					height: 50rpx;
					width: 170rpx;
					border: 1rpx solid #e2e2e2;
					padding: 0 10rpx;
					box-sizing: border-box;
					text {
						display: inline-block;
						margin-right: 30rpx;
					}
					&:nth-child(2) {
						margin-top: 44rpx;
					}
				}
			}
		}
	}
}
</style>
