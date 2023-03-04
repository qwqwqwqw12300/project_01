<!--
* @Author: zhanch
* @Date: 2023-03-04 15:37:43
* @FilePath: /pages/equipment/setting/privacy-zone.vue
* @Description: 隐私区域设置
-->

<template>
	<app-body :bg="false">
		<!-- <app-logo text="子区域设置"></app-logo> -->
		<view id="setting">
			<view class="ui-set-box">
				<app-logo text="屏蔽区域设置" color="#353535"></app-logo>
				<view class="ui-movable">
					<view class="mova-box">
						<movable-area :style="getStyle">
							<view
								class="ui-cell"
								@touchstart="touchstart($event, item, index)"
								@touchmove.stop.prevent="touchMove($event, item, index)"
								@touchend="touchend($event, item)"
								:class="{ active: item.active, hover: item.status === 'hover', edit: roomZones.length && roomZones[activeZone].roomZoneId === item.roomZoneId }"
								:style="cell"
								v-for="(item, index) of area"
								:key="index + 'c'"
							></view>
							<movable-view :x="sizeInfo.x - 10" :y="sizeInfo.y + 10">
								<view class="ui-device"><text class="ui-zone-name">设备</text></view>
							</movable-view>
						</movable-area>
						<view class="ui-tips">每个方块区域大小为  0.5米 x 0.5米</view>
					</view>
					
				</view>
			</view>
			<!-- 配置区域 -->
			<view class="ui-set-box" v-if="roomZones[activeZone]">
				<view class="ui-list"><u-tabs lineWidth="108rpx" lineColor="#FEAE43" :list="getTabList" @click="acitve"></u-tabs></view>
				<!-- 床区域设置 -->
				<template>
					<u-cell-group>
						<u-cell title="区域名称"><u-input placeholder="请输入区域名称" clearable border="none" inputAlign="right" slot="right-icon"></u-input></u-cell>
						<u-cell title="屏蔽区域长x宽（米）">
							<view class="ui-zooe-size" slot="right-icon">
								<u-input border="none" placeholder="请输入" type="number" inputAlign="center" clearable></u-input>
								<text>×</text>
								<u-input border="none" placeholder="请输入" type="number" inputAlign="center" clearable></u-input>
							</view>
						</u-cell>
						<u-cell title="物体离地高度（米）"><u-input type="number" placeholder="请输入离地高度" clearable border="none" inputAlign="right" slot="right-icon"></u-input></u-cell>
						<u-cell title="物体高度（米）"><u-input type="number" placeholder="请输入物体高度" clearable border="none" inputAlign="right" slot="right-icon"></u-input></u-cell>
					</u-cell-group>
					<view class="ui-setting-btn wd-btn-group">
						<button class="plain" @click="deleteZone">删除</button>
						<button class="default" @click="radarDevice(roomZones[activeZone])">保存</button>
					</view>
				</template>
			</view>
		</view>
		<zone-pop ref="zonePopRef" @cancel="clearCell" @confirm="confirm"></zone-pop>
	</app-body>
</template>

<script>
import { assignDeep, getHoursTime, getMinute, minuteToTime } from '../../../common/utils/util';
import { mapState } from 'vuex';
import { GetRoomZone, PostRoomList, PostRadarDevice, PostRemRadarDevice } from '../../../common/http/api';
import { ZONE } from '../../../config/db';

const ANIMATION_INIT = {
	existFlag: {
		class: '',
		doing: false
	},
	inMonitorFlag: {
		class: '',
		doing: false
	},
	outMonitorFlag: {
		class: '',
		doing: false
	}
};

export default {
	data() {
		return {
			zoneInfo: {},
			checkboxValue1: [],
			dateHandle: {
				type: '',
				show: false,
				mode: 'datetime'
			},
			value: null,
			/**选择的子区域**/
			activeZone: 0,
			roomZones: [],
			/**房间信息**/
			roomInfo: {},
			/**雷达波信息**/
			sizeInfo: {
				/**容器盒子大小**/
				box: {
					width: 300,
					height: 100
				},
				// /**网格大小**/
				// cell: {
				// 	width: 10,
				// 	height: 10,
				// 	area: [] // 网格列表
				// },
				/**雷达波所处的位置**/
				x: 125,
				y: 300,
				/**因为图像大小限制，需要按照比例缩放**/
				scale: {
					x: 10,
					y: 10
				}
			},
			/**网格信息**/
			cell: {
				/**每格多少px**/
				width: 10,
				height: 10,
				/**一格等于多少米**/
				size: 0.5
			},
			/**网格列表**/
			area: [],
			/**拖拽信息保存**/
			touchInfo: {
				startX: 0,
				startY: 0,
				startIndex: 0,
				isAdd: true,
				invalid: false
			},
			/**页面宽度**/
			windowWidth: uni.getSystemInfoSync().windowWidth,
			/**动画样式**/
			animation: {
				...ANIMATION_INIT
			},
			/**时间列表**/
			timeList: [1, 5, 10]
		};
	},
	computed: {
		...mapState({
			deviceInfo: state => state.deviceInfo
		}),
		getStyle() {
			const minBox = 50;
			return {
				width: this.sizeInfo.box.width + 'px',
				height: this.sizeInfo.box.height + 'px'
			};
		},
		/**
		 * 获取背景单元格样式
		 */
		getCellStyle() {
			const obj = {};
			return {};
		},
		/**获取子区域tab列表**/
		getTabList() {
			let list = [];
			if (this.roomZones.length) {
				list = this.roomZones.map((ele, index) => ({
					name: ele.name,
					index
				}));
			}
			return list;
		}
	},
	onLoad() {
		this.init();
	},
	methods: {
		tabClick(obj, index) {
			console.log(obj, index, 'obj');
		},
		init() {
			Promise.all([this.getRoomInfo(), this.getRoomZone()]).then(([roomInfo, rows]) => {
				// 盒子容器信息
				const { width } = this.sizeInfo.box;
				console.log(this.deviceInfo, '设备信息');
				// 设备距离墙壁范围
				const { parameter } = this.deviceInfo;
				const location = parameter.installPosition === '0' ? parameter.deviceLocationWall : parameter.deviceLocationTop;
				const { roomLeft, roomRight, roomHeight, roomLength = 0, roomFront= 0, roomBehind= 0 } = location;
				if (parameter) {
					const { roomLeft, roomRight, roomLength, roomHeight } = parameter.deviceLocation;
					
					console.log(roomLeft, roomRight, roomLength, roomHeight, '设备范围');
					// 盒子比例
					const scale = {
							x: width / (roomLeft + roomRight) * this.cell.size ,
							y: width / (roomLeft + roomRight) * this.cell.size // 以横向距离作为基准
						},
						/**设备位置**/
						x = roomLeft * scale.x ,
						y =  (roomBehind || roomLength)  * scale.y ,
						/**父容器**/
						box = {
							width: 300,
							height: (roomBehind || roomLength) * scale.y
						};
						console.log(x, roomLeft, scale.x, '宽度距离');
					/**网格**/
					this.cell = {
						width: 100 / (box.width / scale.x) + '%',
						height: 100 / (box.height / scale.y) + '%'
					};
					/**网格个数**/
					let idx = 0;
					this.area = new Array((box.width / scale.x) * (box.height / scale.y)).fill('').map(ele => ({
						status: 'none',
						axis: {
							x: 0,
							y: 0
						},
						index: idx++,
						roomZoneId: '',
						active: false
					}));
					Object.assign(this.sizeInfo, {
						x,
						y,
						scale,
						box
					});
					rows.forEach((ele, idx) => {
						const { x1 = 0, x2 = 0, y1 = 0, y2 = 0, roomZoneId } = ele;
						const activeList = this.getArrByAxis(x1, x2, y1, y2);
						this.area.forEach(ele => {
							if (activeList.includes(ele.index)) {
								ele.active = true;
								ele.roomZoneId = roomZoneId;
							}
						});
						Object.assign(ele, {
							height: Math.abs((y1 - y2) * scale.y),
							width: Math.abs((x1 - x2) * scale.x),
							departureTime: ele.departureTime / 60 || 0,
							isDepartureTimeCus: [this.timeList.includes(ele.departureTime / 60)],
							isEntryTimeCus: [this.timeList.includes(ele.entryTime / 60)],
							entryTime: ele.entryTime / 60 || 0,
							/**开始监控时间**/
							startTime: uni.$u.timeFormat(ele.startTime, 'hh:MM'),
							/**结束监控时间**/
							endTime: uni.$u.timeFormat(ele.endTime, 'hh:MM')
						});
					});
					this.roomZones = rows;
				} else {
					uni.showToast({
						icon: 'none',
						title: '请先设置跌倒检测器区域'
					});
				}
			});
		},
		
		/**
		 * 选择子区域
		 * @param {string} 下标
		 */
		acitve(item) {
			this.activeZone = item.index;
		},
		del() {
			this.roomZones.pop();
		},

		/**
		 * 监控区域修改
		 */
		monitorChange([active], type) {
			this.roomZones[this.activeZone][type] = this.roomZones[this.activeZone][type] == 1 ? 0 : 1;
		},
		/**
		 * 开启选择时间
		 */
		openDate(type) {
			// const MAP = {
			// 	startTime: 'datetime',
			// 	endTime: 'datetime',
			// 	departureTime: 'time',
			// 	entryTime: 'time'
			// };
			Object.assign(this.dateHandle, {
				type,
				show: true,
				mode: 'time'
			});
		},

		/**
		 * 选择时间
		 */
		onTimeBtn(time, type) {
			this.roomZones[this.activeZone][type] = time;
		},
		/**
		 * 选择时间完成
		 */
		dateConfirm({ value }) {
			this.roomZones[this.activeZone][this.dateHandle.type] = typeof value === 'number' ? uni.$u.date(value, 'yyyy/mm/dd hh:MM:ss') : value;
			this.dateHandle.show = false;
		},

		/**
		 * 清除格子信息
		 */
		clearCell() {
			this.area.forEach(ele => {
				if (ele.status === 'hover') {
					ele.status = 'none';
				}
			});
		},

		/**
		 * 确认修改/添加
		 * @param {Object} form
		 */
		confirm(form) {
			// 根据保存的长宽做调整
			const { roomZoneId, width, height } = form,
				list = this.area.filter(ele => ele.status === 'hover').map(ele => ele.index),
				newList = this.getArrBySize(Math.min(...list), width, height);
			if (this.area.find(ele => ele.active && newList.includes(ele.index) && ele.roomZoneId !== roomZoneId)) {
				this.clearCell();
				return uni.$u.toast('监测区域重叠，请重新选择');
			}
			Object.assign(form, this.getAxisBySize(Math.min(...list), width, height));
			if (roomZoneId) {
				// 修改
				const zone = this.roomZones.filter(ele => ele.roomZoneId === roomZoneId);
				if (zone.length) Object.assign(zone[0], form);
			} else {
				// 添加
				this.roomZones.push(form);
			}
			this.radarDevice(form);
		},

		/**
		 * 添加/修改区域
		 */
		radarDevice(form) {
			const { x, y, scale } = this.sizeInfo;
			console.log(form);
			const { width, height, startTime, endTime, entryTime, departureTime } = form,
				obj = assignDeep(
					{},
					{
						...form,
						startTime: startTime,
						endTime: endTime,
						entryTime: minuteToTime(entryTime),
						departureTime: minuteToTime(departureTime)
					}
				);
			delete obj.height;
			delete obj.width;
			PostRadarDevice(obj).then(
				() => {
					uni.showToast({
						icon: 'none',
						title: '保存成功'
					});
					setTimeout(() => {
						this.init();
					}, 500);
				},
				err => {
					this.init();
				}
			);
		},

		/**
		 * 获取房间信息
		 */
		getRoomInfo() {
			return new Promise(resolve => {
				const { familyId, roomId } = this.deviceInfo;
				PostRoomList({
					familyId
				}).then(res => {
					this.roomInfo = res.rows.filter(ele => ele.roomId === roomId)[0] || {};
					resolve(this.roomInfo);
				});
			});
		},

		/**
		 * 获取子区域列表
		 */
		getRoomZone() {
			return new Promise(async resolve => {
				const { rows = [] } = await GetRoomZone({
					deviceId: this.deviceInfo.deviceId
				});
				resolve(rows);
			});
		},
		/**
		 * 删除子区域
		 */
		async deleteZone() {
			const { roomZoneId } = this.roomZones[this.activeZone];
			// 已经保存过的
			await PostRemRadarDevice({
				roomZoneId
			});
			this.init();
			this.activeZone = 0;
		},

		/**
		 * 触摸开始
		 */
		touchstart(event, item, index) {
			const { clientX, clientY } = event.touches[0];
			const { roomZoneId } = item;
			Object.assign(this.touchInfo, {
				x: clientX,
				y: clientY,
				isAdd: !roomZoneId,
				index
			});
			console.log(roomZoneId, 'roomZoneId');
			if (roomZoneId) {
				// 修改
				this.area.forEach(ele => {
					if (ele.roomZoneId === roomZoneId) ele.status = 'hover';
				});
			}
		},

		/**
		 * 拖拽事件
		 */
		touchMove(event, item, index) {
			const { clientX, clientY } = event.touches[0];
			if (clientX < (this.windowWidth - 300) / 2 || clientX > (this.windowWidth - 300) / 2 + 300) return;
			this.touchInfo.invalid = false;
			const arr = this.getCellArr(clientX, clientY);
			this.area.forEach((ele, idx) => {
				if (ele.active && ele.roomZoneId !== item.roomZoneId) {
					if (arr.includes(idx)) {
						console.log(ele.index, 'ele');
						this.touchInfo.invalid = true;
					}
					return;
				}
				if (arr.includes(idx)) {
					this.area[idx].status = 'hover';
				} else {
					this.area[idx].status = 'none';
				}
			});
		},

		/**
		 * 触摸结束
		 */
		touchend(event, item) {
			if (this.touchInfo.invalid) {
				this.clearCell();
				return uni.$u.toast('监测区域重叠，请重新选择');
			}
			if (this.roomZones.length > 4) {
				this.clearCell();
				return uni.$u.toast('子区域不能超过4个');
			}
			const list = this.area.filter(ele => ele.status === 'hover').map(item => item.index),
				{ isAdd } = this.touchInfo;
			const { width, height } = this.getZoneSize(list);
			if (isAdd) {
				// 添加的话弹窗填写信息
				console.log(width, height, 'width, height');
				this.$refs.zonePopRef.open(
					{
						roomId: this.deviceInfo.roomId,
						deviceId: this.deviceInfo.deviceId,
						z1: 0,
						z2: 1,
						width,
						height
					},
					'add'
				);
			} else {
				const zone = this.roomZones.filter(ele => ele.roomZoneId === item.roomZoneId);
				this.confirm(
					assignDeep({}, zone[0], {
						width,
						height
					})
				);
			}
		},

		/**
		 * 获取格子参数
		 */
		getCellSize() {
			console.log(this.sizeInfo, 'this.sizeInfo');
			// 每个格子的宽度、高度
			const width = this.sizeInfo.scale.x,
				height = this.sizeInfo.scale.y; // 高度
			// 一行几个格子
			const line = Math.floor(this.sizeInfo.box.width / width),
				// 一列几个格子
				column = Math.floor(this.sizeInfo.box.height / height),
				// 一共几个格子
				total = this.area.length;
			console.log(width, height, line, column, total, 'height');
			return {
				width,
				height,
				line,
				column,
				total
			};
		},

		/**
		 * 获取格子个数
		 */
		getCellArr(endX, endY) {
			const cellArr = [];
			const { x, y } = this.touchInfo;
			let { index } = this.touchInfo;
			const { width, height, line, column, total } = this.getCellSize();
			// 触摸地址距离起始点几个格子
			const countX = Math.ceil((endX - x) / width),
				countY = Math.ceil((endY - y) / height);
			// 当前在第几行
			let currentLine = Math.ceil(index / line),
				// 当前在第几格
				currentCol = Math.ceil(index % line);
			if (countY + currentLine > column) return [];
			if (countX + currentCol + 1 > line) return [];
			for (let i = 0; i <= Math.abs(countY); i++) {
				for (let j = 0; j <= Math.abs(countX); j++) {
					if (countX >= 0) {
						// 正向移动
						if (currentLine === 0 || index + 1 <= (currentLine === 0 && countY >= 0) ? line : currentLine * line) {
							cellArr.push(index++);
						} else {
							break;
						}
					} else {
						// 反向移动
						if (index >= (currentLine - 1) * line) {
							cellArr.push(index--);
						} else {
							break;
						}
					}
				}
				if (countY >= 0) {
					index = currentLine * line + currentCol;
					currentLine++;
					if (index > total) break;
				} else {
					index = (currentLine - 1) * line + currentCol;
					currentLine--;
					// currentLine--;
					if (index < 0) {
						break;
					}
				}
			}
			return cellArr;
		},

		/**
		 * 根据数组获取长框
		 */
		getZoneSize(arr) {
			const min = Math.min(...arr),
				max = Math.max(...arr);
			const { line, column } = this.getCellSize();
			console.log(min, max, 'min, max', Math.floor(max / line), Math.floor(min / line));
			const zoneHeight = Math.floor(max / line) - Math.floor(min / line) + 1,
				zongWidth = (max % line) - (min % line) + 1;
			return {
				height: zoneHeight,
				width: zongWidth
			};
		},

		/**
		 * 根据坐标获取范围数组
		 */
		getArrByAxis(x1, x2, y1, y2) {
			// 格子参数
			const { width, height, line, column, total } = this.getCellSize(),
				// 设备位置
				{ x, y, scale } = this.sizeInfo;
			let pointY = Math.floor(y / scale.y - y2),
				pointX;
			if (x1 < 0) {
				pointX = Math.floor(x / scale.x - Math.abs(x1) + 1);
			} else {
				pointX = Math.floor(x / scale.x + x1) + 1;
			}
			const start = pointY * line + pointX - 1; // (x1, y1)的点
			const pointW = x2 - x1,
				pointH = y2 - y1; // 区域宽度
			// arr.push(start);
			console.log(start, pointW, pointH, 'getArrByAxis');
			return this.getArrBySize(start, pointW, pointH);
		},

		/**
		 * 根据起始点和宽高计算区域
		 */
		getArrBySize(start, width, height) {
			const { line } = this.getCellSize(),
				arr = [];
			for (let i = height, count = start; i > 0; i--) {
				for (let j = 0; j < width; j++) {
					arr.push(count++);
				}
				count = count + line - width;
			}
			return arr;
		},

		/**
		 * 根据起始点和宽高计算x1 y1....
		 */
		getAxisBySize(start, width, height) {
			console.log(start, width, height, 'start, width, height');
			const { scale, x, y } = this.sizeInfo,
				{ line, column, total } = this.getCellSize();
			console.log(this.sizeInfo, 'sizeInfo');
			// 起始点坐标
			console.log(column - Math.floor(start / line) - 1, 'start % line * scale.x - x');
			const x1 = ((start % line) * scale.x - x) / scale.x,
				y2 = column - Math.floor(start / line);
			const x2 = x1 + width,
				y1 = y2 - height;
			console.log({
				x1: uni.$u.priceFormat(x1, 1),
				x2: uni.$u.priceFormat(x2, 1),
				y1: uni.$u.priceFormat(y1, 1),
				y2: uni.$u.priceFormat(y2, 1)
			});
			return {
				x1: uni.$u.priceFormat(x1, 1),
				x2: uni.$u.priceFormat(x2, 1),
				y1: uni.$u.priceFormat(y1, 1),
				y2: uni.$u.priceFormat(y2, 1)
			};
		}
	}
};
</script>

<style lang="scss">
#setting {
	.ui-title {
		padding: 36rpx 32rpx;
		font-size: 50rpx;
		color: #353535;
	}

	.ui-movable {
		// height: 300rpx;
		width: 100%;
		padding: 50rpx 20rpx;
		box-sizing: border-box;
	}

	.mova-box {
		box-sizing: border-box;
		width: 100%;
		border: 30rpx solid #f2f2f2;
		border-radius: 15rpx;
		background: #fff;
		text-align: center;
	}
	.ui-tips {
		font-size: 26rpx;
		color: #353535;
		margin: 32rpx 0;
	}
	movable-area {
		display: inline-block;
		box-sizing: border-box;
		// height: 300px;
		// width: 250px;
		background-color: #fff;
		border: 1rpx solid #979797;
		overflow: hidden;

		.ui-cell {
			box-sizing: border-box;
			display: inline-block;
			float: left;
			border-top: 1rpx solid #979797;
			border-left: 1rpx solid #979797;

			&.hover {
				opacity: 0.3 !important;
				background: #FF6969;
			}

			&.active {
				opacity: 1;
				background: #FF6969;
			}

			&.edit {
				background: #f8b551;
			}
		}
	}

	movable-view {
		display: flex;
		align-items: center;
		justify-content: center;
		flex-direction: column;
		min-height: 50px;
		min-width: 50px;
		background-color: #007aff;
		color: #fff;
		font-size: 16rpx;

		.ui-zone-name {
			font-size: 20rpx;
		}

		.ui-device {
			height: 20px;
			width: 20px;
			display: flex;
			align-items: center;
			justify-content: center;
			flex-direction: column;
			text-align: center;
			background-color: #00eaff;
			border-radius: 50% 50%;
		}

		@each $idx, $bg in (2: #f8b551, 3: #e5004f, 4: #ec6941, 5: #448aca) {
			&:nth-child(#{$idx}) {
				background-color: $bg;
			}
		}

		&:nth-last-child(1) {
			min-height: 50rpx;
			min-width: 50rpx;
			background-color: unset;
		}
	}

	.ui-set-box {
		background: #fff;
		padding-bottom: 50rpx;

		.ui-list {
			padding: 0 32rpx;
		}

		.ui-date-list {
			width: 400rpx;
			display: flex;
			align-items: center;

			button {
				width: 106rpx;
				height: 50rpx;
			}
		}

		.ui-sub-title {
			padding-left: 32rpx;
		}

		.ui-setting-btn {
			margin-top: 40rpx;

			button {
				width: 320rpx;
				border-radius: 44rpx;
			}
		}

		.ui-delete {
			border-top: 1rpx solid #e2e2e2;
			padding: 104rpx 32rpx 40rpx 32rpx;
		}
	}
	
	.ui-zooe-size {
		width: 300rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
	}
}
</style>
