<!--
* @Author: zhanch
* @Date: 2023-01-05 17:15:25
* @FilePath: /pages/myself/position-setting.vue
* @Description: 区域设置
-->

<template>
	<app-body leftText="返回">
		<view id="setting">
			<view class="ui-movable">
				<view class="mova-box">
					<movable-area :style="getStyle">
						<view
							class="ui-cell"
							@touchstart="touchstart($event, item, index)"
							@touchmove.native.prevent="touchMove($event, item, index)"
							@touchend="touchend($event, item)"
							:class="{active: item.active, hover: item.status === 'hover', edit: roomZones[activeZone].roomZoneId === item.roomZoneId}"
							:style="cell"
							v-for="(item, index) of area"
							:key="index + 'c'"
						>
						</view>
						<movable-view :x="sizeInfo.x - 10" :y="sizeInfo.y + 10">
							<view class="ui-device"><text class="ui-zone-name">设备</text></view>
						</movable-view>
					</movable-area>
				</view>
			</view>
			<view class="ui-list">
				<template v-for="(item, index) of roomZones">
					<button :class="activeZone === index ? 'active' : ''" @click="acitve(index)" :key="index">{{ item.name || '未命名' }}</button>
				</template>
			</view>
			<!-- 区域设置 -->
			<view class="ui-setting" :key="activeZone" v-if="roomZones && roomZones.length">
				<view>
					<u-checkbox-group @change="monitorChange($event, 'existFlag')" placement="column">
						<u-checkbox
							activeColor="#1aa208"
							labelSize="28rpx"
							:checked="roomZones[activeZone].existFlag == 1"
							:customStyle="{ marginBottom: '8px' }"
							label="进出监控"
							name="existFlag"
						></u-checkbox>
					</u-checkbox-group>
					<u-checkbox-group @change="monitorChange($event, 'fallFlag')" placement="column">
						<u-checkbox
							activeColor="#1aa208"
							labelSize="28rpx"
							:checked="roomZones[activeZone].fallFlag == 1"
							:cusmonitorChangetomStyle="{ marginBottom: '8px' }"
							label="全天跌倒监控"
							name="fallFlag"
						></u-checkbox>
					</u-checkbox-group>

					<view class="ui-date-list">
						<text>开始监控时间</text>
						<view class="ui-date active" @click="openDate('startTime')">
							<text>{{ roomZones[activeZone].startTime || '未设置' }}</text>
							<u-icon name="calendar" color="#414141" size="40rpx"></u-icon>
						</view>
					</view>
					<view class="ui-date-list ui-margin">
						<text>结束监控时间</text>
						<view class="ui-date active" @click="openDate('endTime')">
							<text>{{ roomZones[activeZone].endTime || '未设置' }}</text>
							<u-icon name="calendar" color="#414141" size="40rpx"></u-icon>
						</view>
					</view>
					<view class="ui-timing">
						<view class="ui-timing-pos">
							<u-checkbox-group placement="column" @change="monitorChange($event, 'inMonitorFlag')">
								<u-checkbox
									activeColor="#1aa208"
									:checked="roomZones[activeZone].inMonitorFlag == 1"
									:customStyle="{ marginBottom: '40rpx' }"
									labelSize="28rpx"
									label="进入监控区域超时报警时间"
									name="inMonitorFlag"
								></u-checkbox>
							</u-checkbox-group>
							<u-checkbox-group placement="column" @change="monitorChange($event, 'outMonitorFlag')">
								<u-checkbox
									activeColor="#1aa208"
									labelSize="28rpx"
									:checked="roomZones[activeZone].outMonitorFlag == 1"
									label="离开监控区域超时报警时间"
									name="outMonitorFlag"
								></u-checkbox>
							</u-checkbox-group>
						</view>
						<view class="ui-timing-pos">
							<view class="ui-timing-active active" @click="openDate('entryTime')">
								<text>{{ roomZones[activeZone].entryTime || '请选择' }}</text>
								<u-icon name="arrow-down" size="28rpx"></u-icon>
							</view>
							<view class="ui-timing-active active" @click="openDate('departureTime')">
								<text>{{ roomZones[activeZone].departureTime || '请选择' }}</text>
								<u-icon name="arrow-down" size="28rpx"></u-icon>
							</view>
						</view>
					</view>
					<view class="ui-setting-btn wd-btn-gloup">
						<button @click="deleteZone">删除</button>
						<button @click="deleteZone" class="green">保存</button>
					</view>
				</view>
			</view>
			<!-- /区域设置 -->
		</view>
		<u-datetime-picker v-if="dateHandle.show" :show="dateHandle.show" @confirm="dateConfirm" @cancel="dateHandle.show = false" :mode="dateHandle.mode"></u-datetime-picker>
		<zone-pop ref="zonePopRef" @cancel="clearCell" @confirm="confirm"></zone-pop>
	</app-body>
</template>

<script>
import { assignDeep, getHoursTime } from '../../common/utils/util';
import { mapState } from 'vuex';
import { GetRoomZone, PostRoomList, PostRadarDevice, PostRemRadarDevice } from '../../common/http/api';
import { ZONE } from '../../config/db';

export default {
	data() {
		return {
			zoneInfo: {},
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
				width: 10,
				height: 10
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
			 windowWidth: uni.getSystemInfoSync().windowWidth
		};
	},
	computed: {
		...mapState({
			deviceInfo: state => state.deviceInfo
		}),
		getStyle() {
			const minBox = 50;
			// let width = Math.max(this.roomZones.map(ele => ele.width));
			// let height = Math.max(this.roomZones.map(ele => ele.height));
			// width = width > minBox ? width : minBox;
			// height = height > minBox ? height : minBox;
			// // 划分格子，1格子等于1平方米
			// console.log(width, height, '距离', this.sizeInfo.box.width / x, y);
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
		}
	},
	onLoad() {
		this.init();
	},
	methods: {
		init() {
			Promise.all([this.getRoomInfo(), this.getRoomZone()]).then(([roomInfo, rows]) => {
				// 盒子容器信息
				const { width } = this.sizeInfo.box;
				console.log(this.deviceInfo, '设备信息');
				// 设备距离墙壁范围
				const { parameter } = this.deviceInfo;
				if (parameter && parameter.deviceLocation) {
					const { roomLeft, roomRight, roomLength, roomHeight } = parameter.deviceLocation;
					console.log(roomLeft, roomRight, roomLength, roomHeight, '设备范围');
					// 盒子比例
					const scale = {
							x: width / (roomLeft + roomRight),
							y: width / (roomLeft + roomRight) // 以横向距离作为基准
						},
						/**设备位置**/
						x = roomLeft * scale.x,
						y = roomLength * scale.y,
						/**父容器**/
						box = {
							width: 300,
							height: roomLength * scale.y
						};
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
						const {x1 = 0, x2 = 0, y1 = 0, y2 = 0, roomZoneId } = ele;
						const activeList = this.getArrByAxis(x1, x2, y1, y2);
						this.area.forEach(ele => {
							if(activeList.includes(ele.index)) {
								ele.active = true;
								ele.roomZoneId = roomZoneId;
								console.log(ele, '变色');
							}
						})
						Object.assign(ele, {
							height: Math.abs((y1 - y2) * scale.y),
							width: Math.abs((x1 - x2) * scale.x),
							departureTime: uni.$u.timeFormat(ele.departureTime, 'hh:MM'),
							entryTime: uni.$u.timeFormat(ele.entryTime, 'hh:MM'),
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
						title: '请先设置雷达波区域'
					});
				}
			});
		},

		/**
		 * 区域修改
		 * @param {Object} e
		 * @param {string} 下标
		 */
		onChange: function(e, index) {
			this.roomZones[index].old.x = e.detail.x;
			this.roomZones[index].old.y = e.detail.y;
		},
		/**
		 * 选择子区域
		 * @param {string} 下标
		 */
		acitve(idx) {
			this.activeZone = idx;
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
				if(ele.status === 'hover') {
					ele.status = 'none'
				}
			});
		},
		
		/**
		 * 确认修改/添加
		 * @param {Object} form
		 */
		confirm(form) {
			// 根据保存的长宽做调整
			const { roomZoneId, width, height }  = form,
			 list = this.area.filter(ele => ele.status === 'hover').map(ele => ele.index),
			newList = this.getArrBySize(Math.min(...list), width, height);
			if(this.area.find(ele => ele.active && newList.includes(ele.index) && ele.roomZoneId !== roomZoneId )) {
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
			this.radarDevice();
		},

		/**
		 * 添加/修改设备
		 */
		radarDevice(from = {}) {
			const { x, y, scale } = this.sizeInfo;
			const list = this.roomZones.map(ele => {
				const { width, height, startTime, endTime, entryTime, departureTime } = ele,
					obj = assignDeep(
						{},
						{
							...ele,
							startTime: getHoursTime(startTime),
							endTime: getHoursTime(endTime),
							entryTime: getHoursTime(entryTime),
							departureTime: getHoursTime(departureTime)
						}
					);
				delete obj.height;
				delete obj.width;
				return PostRadarDevice(obj);
			});

			Promise.all(list).then(res => {
				uni.showToast({
					icon: 'none',
					title: '保存成功'
				});
				setTimeout(() => {
					this.init();
				}, 500)
			});
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
			if(roomZoneId) {  // 修改
				this.area.forEach((ele) => {
					if(ele.roomZoneId === roomZoneId) ele.status = 'hover';
				});
			}
			console.log(event, '开始触摸');
		},

		/**
		 * 拖拽事件
		 */
		touchMove(event, item, index) {
			const { clientX, clientY } = event.touches[0];
			if(clientX < (this.windowWidth - 300) / 2 || clientX > (this.windowWidth - 300) / 2 + 300) return;
			this.touchInfo.invalid = false;
			const arr = this.getCellArr(clientX, clientY);
			this.area.forEach((ele, idx) => {
				if(ele.active && ele.roomZoneId !==  item.roomZoneId) {
					if(arr.includes(idx)) {
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
			
			if(this.touchInfo.invalid) {
				this.clearCell();
				return uni.$u.toast('监测区域重叠，请重新选择');
			}
			if(this.roomZones.length > 4) {
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
						z2: this.deviceInfo.parameter.deviceLocation.roomHeight,
						width,
						height
					},
					'add'
				);
			} else {
				const zone = this.roomZones.filter(ele => ele.roomZoneId === item.roomZoneId);
				this.confirm(assignDeep({}, zone[0], {
					width, height
				}));
			}
		},
		

		/**
		 * 获取格子参数
		 */
		getCellSize() {
			// 每个格子的宽度、高度
			const width = this.sizeInfo.scale.x,
				height = this.sizeInfo.scale.y; // 高度
			// 一行几个格子
			const line = Math.floor(this.sizeInfo.box.width / width),
				// 一列几个格子
				column = Math.floor(this.sizeInfo.box.height / height),
				// 一共几个格子
				total = this.area.length;
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
			const { line } = this.getCellSize(), arr = [];
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
			 y2= column - Math.floor(start / line);
			const x2 = x1 +  width, y1 = y2 - height;
			console.log({
				x1: uni.$u.priceFormat(x1 , 1),
				x2: uni.$u.priceFormat(x2, 1), 
				y1: uni.$u.priceFormat(y1, 1), 
				y2: uni.$u.priceFormat(y2, 1), 
			});
			return {
				x1: uni.$u.priceFormat(x1 , 1),
				x2: uni.$u.priceFormat(x2, 1), 
				y1: uni.$u.priceFormat(y1, 1), 
				y2: uni.$u.priceFormat(y2, 1), 
			}
			 
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
		padding: 30rpx 50rpx;
		box-sizing: border-box;
	}

	.mova-box {
		width: 100%;
		text-align: center;
	}

	movable-area {
		display: inline-block;
		box-sizing: border-box;
		// height: 300px;
		// width: 250px;
		background-color: #fff;
		border: 1rpx solid #4c65a4;
		overflow: hidden;

		.ui-cell {
			box-sizing: border-box;
			display: inline-block;
			float: left;
			border-top: 1rpx solid #4c65a4;
			border-left: 1rpx solid #4c65a4;

			&.hover {
				opacity: 0.3 !important;
				background: #007aff;
			}

			&.active {
				opacity: 1;
				background: #007aff;
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

	.ui-add-btn {
		width: 100%;
		display: flex;
		align-items: center;

		button {
			width: 276rpx;
			height: 82rpx;
			border-radius: 35rpx;
			font-size: 28rpx;
			line-height: 82rpx;

			&:nth-child(1) {
				margin-right: 20rpx;
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
			line-height: 56rpx;
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
			width: 608rpx;
			min-height: 645rpx;
			border-radius: 10rpx;
			background-image: linear-gradient(0deg, #eeeeee 0%, #ffffff 100%);
			border: 1rpx solid #bfbfbf;
		}

		.ui-date-list {
			margin-top: 40rpx;
			width: 100%;
			display: inline-flex;
			justify-content: space-between;
			align-items: center;
			font-size: 28rpx;
			color: rgb(96, 98, 102);
		}

		.ui-margin {
			margin-bottom: 40rpx;
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

				// &:nth-child(2) {
				// 	width: 200rpx;
				// }

				.ui-timing-active {
					display: flex;
					justify-content: space-evenly;
					align-items: center;
					font-size: 30rpx;
					line-height: 50rpx;
					height: 50rpx;
					width: 250rpx;
					border: 1rpx solid #e2e2e2;
					color: #606266;
					padding: 0 10rpx;
					box-sizing: border-box;

					text {
						display: inline-block;
						margin-right: 30rpx;
						// width: 160rpx;
					}

					&:nth-child(2) {
						margin-top: 44rpx;
					}
				}
			}
		}

		.ui-setting-btn {
			margin-top: 60rpx;

			button {
				font-size: 25rpx;
				width: 200rpx;
			}
		}
	}

	.ui-temp {
		height: 100rpx;
		width: 100%;
		display: flex;
		justify-content: center;
		align-items: center;
	}
}
</style>
