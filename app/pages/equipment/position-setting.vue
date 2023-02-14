<!--
* @Author: zhanch
* @Date: 2023-01-05 17:15:25
* @FilePath: /pages/myself/position-setting.vue
* @Description: 区域设置
-->

<template>
	<app-body :bg="false" leftText="返回">
		<!-- <app-logo text="子区域设置"></app-logo> -->
		<view id="setting">
			<view class="ui-set-box">
				<view class="ui-title">
					子区域设置
				</view>
				<view class="ui-movable">
					<view class="mova-box">
						<movable-area :style="getStyle">
							<view class="ui-cell" @touchstart="touchstart($event, item, index)"
								@touchmove.stop.prevent="touchMove($event, item, index)"
								@touchend="touchend($event, item)"
								:class="{active: item.active, hover: item.status === 'hover', edit: roomZones.length&&roomZones[activeZone].roomZoneId === item.roomZoneId}"
								:style="cell" v-for="(item, index) of area" :key="index + 'c'">
							</view>
							<movable-view :x="sizeInfo.x - 10" :y="sizeInfo.y + 10">
								<view class="ui-device"><text class="ui-zone-name">设备</text></view>
							</movable-view>
						</movable-area>
					</view>
				</view>
			</view>
			<view class="ui-set-box" v-if="roomZones[activeZone]">
				<!-- 优化 -->
				<view class="ui-list">
					<u-tabs lineWidth="108rpx" :list="getTabList" @click="acitve">
					</u-tabs>
				</view>
				<u-cell-group>
					<u-cell title="区域监控">
						<u-switch space="2" @change="onSwitch($event, 'existFlag')"
							v-model="roomZones[activeZone].existFlag" activeValue="0" inactiveValue="1" size="20"
							slot="right-icon" activeColor="#FEAE43" inactiveColor="rgb(230, 230, 230)">
						</u-switch>
					</u-cell>
					<u-cell title="有人员停留的超时报警间隔">
						<u-switch space="2" @change="onSwitch($event, 'inMonitorFlag')"
							v-model="roomZones[activeZone].inMonitorFlag" activeValue="0" inactiveValue="1" size="20"
							slot="right-icon" activeColor="#FEAE43" inactiveColor="rgb(230, 230, 230)">
						</u-switch>
					</u-cell>
					<u-cell>
						<view slot="title" class="ui-slot-title">
							<view class="ui-date-list">
								<u-button type="primary" :plain="true" size="mini" text="1分钟"></u-button>
								<u-button type="primary" :plain="true" size="mini" text="五分钟"></u-button>
								<u-button type="primary" :plain="true" size="mini" text="十分钟"></u-button>
							</view>
						</view>
						<view slot="right-icon">
							<u-radio-group placement="column">
								<u-radio activeColor="#FEAE43" label="自定义时间" :name="1">
								</u-radio>
							</u-radio-group>
						</view>
					</u-cell>
					<u-cell>
						<view slot="title" class="ui-slot-title">
							<text>选择自定义时间</text>
						</view>
						<view slot="right-icon">
							<u-number-box></u-number-box>
						</view>
					</u-cell>
				</u-cell-group>
				<!-- /优化 -->
				<view class="ui-list">
					<template v-for="(item, index) of roomZones">
						<button :class="activeZone === index ? 'active' : ''" @click="acitve(index)"
							:key="index">{{ item.name || '未命名' }}</button>
					</template>
				</view>
				<!-- 区域设置 -->
				<view class="ui-setting" :key="activeZone" v-if="roomZones && roomZones.length">
					<view class="ui-setting-box" v-if="roomZones[activeZone].zoneType != 1">
						<!-- /跌倒监控默认不开启 -->
						<view class="ui-switch">
							<u-switch space="2" @change="onSwitch($event, 'existFlag')"
								v-model="roomZones[activeZone].existFlag" activeValue="0" inactiveValue="1"
								activeColor="#85B224" size="20" inactiveColor="rgb(230, 230, 230)">
							</u-switch>
							<text>区域监控</text>
						</view>
						<view class="ui-inMonitor" v-if="roomZones[activeZone].existFlag == 0">
							<view class="ui-switch">
								<u-switch space="2" @change="onSwitch($event, 'inMonitorFlag')"
									v-model="roomZones[activeZone].inMonitorFlag" activeValue="0" inactiveValue="1"
									activeColor="#85B224" size="20" inactiveColor="rgb(230, 230, 230)">
								</u-switch>
								<text>有人员停留的超时报警间隔</text>
							</view>
							<view class="ui-inMonitor-date"
								:class="animation.inMonitorFlag.doing && animation.inMonitorFlag.class"
								:style="{height: roomZones[activeZone].inMonitorFlag == 0 ? '280rpx' : '0'}">
								<view class="ui-date-list">
									<view :class="{active: roomZones[activeZone].entryTime === item}"
										:key="item + 'time'" v-for="item of timeList"
										@click="onTimeBtn(item, 'entryTime')">
										{{item}}分钟
									</view>
								</view>
								<view class="ui-inMonitor-custom">
									<text>自定义时间(分钟):</text>
									<u-number-box v-model="roomZones[activeZone].entryTime">
									</u-number-box>
								</view>
							</view>
							<view class="ui-switch">
								<u-switch space="2" @change="onSwitch($event, 'outMonitorFlag')"
									v-model="roomZones[activeZone].outMonitorFlag" activeValue="0" inactiveValue="1"
									activeColor="#85B224" size="20" inactiveColor="rgb(230, 230, 230)">
								</u-switch>
								<text>区域无人报警</text>
							</view>
							<view class="ui-inMonitor-date"
								:class="animation.outMonitorFlag.doing && animation.outMonitorFlag.class"
								:style="{height: roomZones[activeZone].outMonitorFlag == 0 ? '280rpx' : '0'}">
								<view class="ui-date-list">
									<view :class="{active: roomZones[activeZone].departureTime === item}"
										:key="item + 'inMonitor'" v-for="item of timeList"
										@click="onTimeBtn(item, 'departureTime')">
										{{item}}分钟
									</view>
								</view>
								<view class="ui-inMonitor-custom">
									<text>自定义时间(分钟):</text>
									<u-number-box :iconStyle="{
										background: 'red'
									}" v-model="roomZones[activeZone].departureTime">
										<view class="" slot="minus">
											<u-icon name="plus" size="12"></u-icon>
										</view>
										<view class="" slot="plus">
											<u-icon name="plus" color="#FFFFFF" size="12"></u-icon>
										</view>
									</u-number-box>
								</view>
							</view>
						</view>
						<view class="ui-setting-btn wd-btn-group">
							<button @click="deleteZone">删除</button>
							<button @click="radarDevice(roomZones[activeZone])" class="green">保存</button>
						</view>
					</view>
					<view class="ui-setting-box" v-else>
						<view class="ui-setting-btn">
							<button @click="deleteZone">删除</button>
						</view>
					</view>
				</view>
				<!-- /区域设置 -->
			</view>
		</view>
		<u-datetime-picker v-if="dateHandle.show" :show="dateHandle.show" @confirm="dateConfirm"
			@cancel="dateHandle.show = false" :mode="dateHandle.mode"></u-datetime-picker>
		<zone-pop ref="zonePopRef" @cancel="clearCell" @confirm="confirm"></zone-pop>
	</app-body>
</template>

<script>
	import {
		assignDeep,
		getHoursTime,
		getMinute,
		minuteToTime
	} from '../../common/utils/util';
	import {
		mapState
	} from 'vuex';
	import {
		GetRoomZone,
		PostRoomList,
		PostRadarDevice,
		PostRemRadarDevice
	} from '../../common/http/api';
	import {
		ZONE
	} from '../../config/db';

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
				range: [{
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
				windowWidth: uni.getSystemInfoSync().windowWidth,
				/**动画样式**/
				animation: {
					...ANIMATION_INIT
				},
				timeList: [1, 5, 10, 15]
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
					const {
						width
					} = this.sizeInfo.box;
					console.log(this.deviceInfo, '设备信息');
					// 设备距离墙壁范围
					const {
						parameter
					} = this.deviceInfo;
					if (parameter && parameter.deviceLocation) {
						const {
							roomLeft,
							roomRight,
							roomLength,
							roomHeight
						} = parameter.deviceLocation;
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
						this.area = new Array((box.width / scale.x) * (box.height / scale.y)).fill('').map(ele =>
							({
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
							const {
								x1 = 0, x2 = 0, y1 = 0, y2 = 0, roomZoneId
							} = ele;
							const activeList = this.getArrByAxis(x1, x2, y1, y2);
							this.area.forEach(ele => {
								if (activeList.includes(ele.index)) {
									ele.active = true;
									ele.roomZoneId = roomZoneId;
								}
							})
							Object.assign(ele, {
								height: Math.abs((y1 - y2) * scale.y),
								width: Math.abs((x1 - x2) * scale.x),
								departureTime: ele.departureTime / 60 || 0,
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
							title: '请先设置雷达波区域'
						});
					}
				});
			},

			onSwitch(status, type) {
				if (this.animation[type]) {
					this.animation[type].doing = true;
					this.animation[type].class = status === '0' ? 'up' : 'down';
					setTimeout(() => {
						this.animation[type].doing = false;
					}, 500);
				}
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
			dateConfirm({
				value
			}) {
				this.roomZones[this.activeZone][this.dateHandle.type] = typeof value === 'number' ? uni.$u.date(value,
					'yyyy/mm/dd hh:MM:ss') : value;
				this.dateHandle.show = false;
			},

			/**
			 * 清除格子信息
			 */
			clearCell() {
				this.area.forEach(ele => {
					if (ele.status === 'hover') {
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
				const {
					roomZoneId,
					width,
					height
				} = form,
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
			 * 添加/修改设备
			 */
			radarDevice(form) {
				const {
					x,
					y,
					scale
				} = this.sizeInfo;
				console.log(form);
				const {
					width,
					height,
					startTime,
					endTime,
					entryTime,
					departureTime
				} = form,
				obj = assignDeep({}, {
					...form,
					startTime: getHoursTime(startTime),
					endTime: getHoursTime(endTime),
					entryTime: minuteToTime(entryTime),
					departureTime: minuteToTime(departureTime)
				});
				delete obj.height;
				delete obj.width;
				PostRadarDevice(obj).then(() => {
					uni.showToast({
						icon: 'none',
						title: '保存成功'
					});
					setTimeout(() => {
						this.init();
					}, 500)
				}, err => {
					this.init();
				});
			},

			/**
			 * 获取房间信息
			 */
			getRoomInfo() {
				return new Promise(resolve => {
					const {
						familyId,
						roomId
					} = this.deviceInfo;
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
					const {
						rows = []
					} = await GetRoomZone({
						deviceId: this.deviceInfo.deviceId
					});
					resolve(rows);
				});
			},
			/**
			 * 删除子区域
			 */
			async deleteZone() {
				const {
					roomZoneId
				} = this.roomZones[this.activeZone];
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
				const {
					clientX,
					clientY
				} = event.touches[0];
				const {
					roomZoneId
				} = item;
				Object.assign(this.touchInfo, {
					x: clientX,
					y: clientY,
					isAdd: !roomZoneId,
					index
				});
				console.log(roomZoneId, 'roomZoneId');
				if (roomZoneId) { // 修改
					this.area.forEach((ele) => {
						if (ele.roomZoneId === roomZoneId) ele.status = 'hover';
					});
				}
			},

			/**
			 * 拖拽事件
			 */
			touchMove(event, item, index) {
				const {
					clientX,
					clientY
				} = event.touches[0];
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
					{
						isAdd
					} = this.touchInfo;
				const {
					width,
					height
				} = this.getZoneSize(list);
				if (isAdd) {
					// 添加的话弹窗填写信息
					console.log(width, height, 'width, height');
					this.$refs.zonePopRef.open({
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
					this.confirm(assignDeep({}, zone[0], {
						width,
						height
					}));
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
				const {
					x,
					y
				} = this.touchInfo;
				let {
					index
				} = this.touchInfo;
				const {
					width,
					height,
					line,
					column,
					total
				} = this.getCellSize();
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
							if (currentLine === 0 || index + 1 <= (currentLine === 0 && countY >= 0) ? line : currentLine *
								line) {
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
				const {
					line,
					column
				} = this.getCellSize();
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
				const {
					width,
					height,
					line,
					column,
					total
				} = this.getCellSize(),
					// 设备位置
					{
						x,
						y,
						scale
					} = this.sizeInfo;
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
				const {
					line
				} = this.getCellSize(), arr = [];
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
				const {
					scale,
					x,
					y
				} = this.sizeInfo, {
					line,
					column,
					total
				} = this.getCellSize();
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
					y2: uni.$u.priceFormat(y2, 1),
				});
				return {
					x1: uni.$u.priceFormat(x1, 1),
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

			@each $idx,
			$bg in (2: #f8b551, 3: #e5004f, 4: #ec6941, 5: #448aca) {
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

			.ui-date-list {
				width: 400rpx;
				display: flex;
				align-items: center;

				button {
					width: 106rpx;
					height: 50rpx;

				}
			}
		}

		.ui-list {
			margin: 20rpx 0;
			padding: 0 46rpx;
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

			>view {
				padding: 35rpx;
				box-sizing: border-box;
				display: inline-block;
				width: 608rpx;
				min-height: 860rpx;
				border-radius: 10rpx;
				background-image: linear-gradient(0deg, #eeeeee 0%, #ffffff 100%);
				border: 1rpx solid #bfbfbf;
			}

			.ui-switch {
				margin-top: 20rpx;
				font-size: 28rpx;
				display: flex;
				flex-direction: row;
				align-items: center;

				text {
					margin-left: 20rpx;
					color: #515151;
				}
			}

			@keyframes up {
				from {
					height: 0rpx;
				}

				to {
					height: 280rpx;
				}
			}

			@keyframes down {
				from {
					height: 280rpx;
				}

				to {
					height: 0;
				}
			}

			@keyframes inMonitorUp {
				from {
					height: 0rpx;
				}

				to {
					height: 606rpx;
				}
			}

			@keyframes inMonitorDown {
				from {
					max-height: 606rpx;
				}

				to {
					max-height: 0;
				}
			}

			.ui-inMonitor {
				min-height: 606rpx;
				// overflow: hidden;
				animation-duration: 600ms;

				&.up {
					animation-name: up;
				}

				&.down {
					animation-name: down;
					height: 0;
				}
			}

			.ui-inMonitor-date {
				height: 280rpx;
				animation-duration: 600ms;
				font-size: 28rpx;
				overflow: hidden;

				&.up {
					animation-name: up;
				}

				&.down {
					animation-name: down;
					height: 0;
				}

				.ui-date-list {
					margin-top: 40rpx;
					color: rgb(96, 98, 102);
					display: inline-block;
					text-align: center;
					padding: 0 32rpx;
					width: 100%;
					box-sizing: border-box;
					justify-content: center;

					>view {
						display: inline-block;
						width: calc(50% - 28rpx);
						height: 55rpx;
						line-height: 55rpx;
						float: left;
						margin: 10rpx 10rpx;
						border: 1px solid #f8b551;
						border-radius: 15rpx 15rpx;

						&.active {
							background: #f8b551;
							color: #fff;
						}
					}
				}

				.ui-inMonitor-custom {
					padding: 0 32rpx;
					display: flex;
					flex-direction: row;
					align-items: center;
					justify-content: space-between;
					margin-top: 15rpx;

				}
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
