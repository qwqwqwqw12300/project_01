<!--
* @Author: white_queen
* @Date: 2023-03-03 15:37:17
* @FilePath: /pages/equipment/setting/out-bed.vue
* @Description: 离床预警
-->

<template>
	<app-body :bg="false">
		<!-- <app-logo text="子区域设置"></app-logo> -->
		<view id="setting">
			<view class="ui-set-box">
				<app-logo text="离床预警"></app-logo>
				<view class="ui-movable">
					<view class="mova-box">
						<movable-area :style="getStyle">
							<template v-for="(item, index) of area">
								<view :key="index + 'c'" class="ui-cell" @touchstart="touchstart($event, item, index)"
									@touchmove.stop.prevent="touchMove($event, item, index)"
									@touchend="touchend($event, item)"
									:class="{ active: item.active, hover: item.status === 'hover', edit: roomZones.length && activeZone.roomZoneId === item.roomZoneId }"
									:style="cell" v-if="item.zoneType === '2'">
								</view>
								<view :key="index + 'ec'" v-else :style="cell" class="ui-cell disable">
								</view>
							</template>

							<movable-view :x="sizeInfo.x - 10"
								:y="sizeInfo.y + (sizeInfo.installPosition == 0 ? 10 : -10)">
								<view class="ui-device"><text class="ui-zone-name"></text></view>
							</movable-view>
						</movable-area>
						<view class="ui-tips">
							<text>每个方块区域大小为 0.5米 x 0.5米</text>
							<text>选中</text>
							<text>未选中</text>
						</view>

					</view>
				</view>
			</view>
			<!-- 配置区域 -->
			<view class="ui-set-box" v-if="roomZones && activeZone">
				<view class="ui-list">
					<u-tabs lineWidth="108rpx" lineColor="#FEAE43" :list="getTabList" @click="acitve">
					</u-tabs>
				</view>
				<!-- 床区域设置 -->
				<template v-if="getParameter">
					<u-cell-group v-if="getParameter.setRuleDate">
						<view class="ui-time" @click="setRule(getParameter.setRuleDate)">
							<view class="ui-time-box">
								<text>时间设置</text>
								<view class="ui-time-details" v-if="getParameter.setRuleDate.dateType == 0">
									<text>{{ getParameter.setRuleDate.startDate +'至' + getParameter.setRuleDate.endDate}}</text>
									<text>{{ getParameter.setRuleDate.startTime + '至' +  getParameter.setRuleDate.endTime}}
									</text>
								</view>
								<text class="ui-week" v-else>{{ weekText(getParameter.setRuleDate.week) }}</text>
							</view>
							<u-icon name="arrow-right" color="#909193" size="36rpx"></u-icon>
						</view>
						<u-cell title="离床间隔">
							<u-switch space="2" v-model="getParameter.leaveBedInterval" activeValue="1"
								inactiveValue="0" size="20" slot="right-icon" activeColor="#FEAE43"
								inactiveColor="rgb(230, 230, 230)"></u-switch>
						</u-cell>
						<template v-if="getParameter.leaveBedInterval === '1'">
							<u-cell>
								<view slot="title" class="ui-slot-title">
									<view class="ui-date-list">
										<u-tag :key="item + 'time'" v-for="item of timeList" @click="onTimeBtn(item)"
											:plain="!(getParameter.intervalTime === item)" size="mini"
											:text="item + '分钟'" type="warning" :name="item">
										</u-tag>
									</view>
								</view>
								<view slot="right-icon">
									<u-checkbox-group shape="circle" placement="column" v-model="activeZone.timeCus">
										<u-checkbox activeColor="#FEAE43" label="自定义时间" :name="true"></u-checkbox>
									</u-checkbox-group>
								</view>
							</u-cell>
							<u-cell v-if="activeZone.timeCus && activeZone.timeCus[0]">
								<view slot="title" class="ui-sub-title"><text>选择自定义时间</text></view>
								<view slot="right-icon">
									<u-number-box v-model="getParameter.intervalTime"></u-number-box>
								</view>
							</u-cell>
						</template>
					</u-cell-group>
					<view class="ui-setting-btn wd-btn-group">
						<button class="plain" @click="deleteZone">删除</button>
						<button class="default" @click="updateZone(activeZone)">保存</button>
					</view>
				</template>
				<!-- /床区域设置 -->
			</view>
		</view>
		<bed-pop ref="zonePopRef" @cancel="clearCell" @confirm="confirm"></bed-pop>
	</app-body>
</template>

<script>
	import {
		assignDeep,
		getHoursTime,
		getMinute,
		minuteToTime,
		wddkAbbreviation
	} from '../../../common/utils/util';
	import {
		mapState
	} from 'vuex';
	import {
		GetRoomZone,
		PostRoomList,
		PostRadarDevice,
		PostRemRadarDevice,
		PostSetRadarWareShadowZone,
		PostSetRadarWareBedZone
	} from '../../../common/http/api';
	import {
		ZONE
	} from '../../../config/db';

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
					},
					// 悬挂方式 0 壁挂 1 顶挂
					installPosition: '0'
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
				/**时间列表**/
				timeList: [30, 45, 60],
				/**区域尺寸**/
				roomSize: {
					roomLeft: 0,
					roomRight: 0,
					roomHeight: 0,
					roomLength: 0,
					roomFront: 0,
					roomBehind: 0
				}
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
			/**获取子区域tab列表**/
			getTabList() {
				let list = [];
				if (this.roomZones.length) {
					list = this.roomZones.map((ele, index) => ({
						name: ele.name,
						idx: index,
						zoneType: ele.zoneType
					})).filter(ele => ele.zoneType === '2');
				}
				return list;
			},
			/**日期信息**/
			weekText() {
				return week => {
					try {
						return wddkAbbreviation(week) || '请选择星期';
					} catch (err) {
						console.log(err);
					}

				};
			},
			getParameter() {
				return this.activeZone.zoneType === '2' && this.activeZone
					.leaveBedWarnParameter;
			},
		},
		onLoad() {
			this.init();
		},
		methods: {
			/**
			 * 设置规则
			 */
			setRule(rule) {
				uni.$once('bedRule', ({
					type,
					data
				}) => {
					this.init();
				});
				this.$setCache('bedRuleCache', {
					deviceId: this.deviceInfo.deviceId,
					roomZoneId: this.activeZone.roomZoneId,
					...(rule || {})
				});
				uni.navigateTo({
					url: `/pages/equipment/setting/bed-rules`
				});
			},
			init() {
				Promise.all([this.getRoomInfo(), this.getRoomZone()]).then(([roomInfo, rows]) => {
					// 盒子容器信息
					const {
						width
					} = this.sizeInfo.box;
					// 设备距离墙壁范围
					const {
						parameter
					} = this.deviceInfo,
						cellSize = this.cell.size;
					if (parameter) {
						const location = parameter.installPosition === '0' ? parameter.deviceLocationWall :
							parameter.deviceLocationTop;
						let {
							roomLeft,
							roomRight,
							roomHeight,
							roomLength = 0,
							roomFront = 0,
							roomBehind = 0
						} = location;
						const w = (roomLeft + roomRight) % 1;
						const h = (roomBehind + roomFront) % 1;
						if (w !== 0 && w !== 0.5) {
							roomLeft = this.controlSize(roomLeft),
								roomRight = this.controlSize(roomRight)
						}
						if (h !== 0 && h !== 0.5) {
							roomBehind = this.controlSize(roomBehind),
								roomFront = this.controlSize(roomFront)
						}
						this.roomSize = {
							roomLeft,
							roomRight,
							roomHeight,
							roomLength: this.controlSize(roomLength),
							roomFront,
							roomBehind
						};
						// 按每格比例放大
						roomLeft = this.roomSize.roomLeft / cellSize;
						roomRight = this.roomSize.roomRight / cellSize;

						roomHeight = this.roomSize.roomHeight / cellSize;
						roomLength = this.roomSize.roomLength / cellSize;
						roomFront = this.roomSize.roomFront / cellSize;
						roomBehind = this.roomSize.roomBehind / cellSize;

						// 盒子比例
						const scale = {
								x: width / (roomLeft + roomRight),
								y: width / (roomLeft + roomRight) // 以横向距离作为基准
							},
							/**设备位置**/
							x = roomLeft * scale.x,
							y = parameter.installPosition === '0' ? roomLength * scale.y : roomBehind * scale.y,
							/**父容器**/
							box = {
								width: 300,
								height: parameter.installPosition === '0' ? roomLength * scale.y : (roomBehind +
									roomFront) * scale.y,
							};
						/**网格**/
						this.cell = {
							width: 100 / (box.width / scale.x) + '%',
							height: 100 / (box.height / scale.y) + '%',
							size: cellSize
						};
						/**网格个数**/
						let idx = 0;
						this.area = new Array(Math.ceil(box.width / scale.x) * Math.ceil(box.height / scale.y))
							.fill('')
							.map(ele =>
								({
									status: 'none',
									axis: {
										x: 0,
										y: 0
									},
									index: idx++,
									roomZoneId: '',
									zoneType: '2',
									active: false
								}));
						Object.assign(this.sizeInfo, {
							x,
							y,
							scale,
							box,
							installPosition: parameter.installPosition
						});
						rows.forEach((ele, idx) => {
							let {
								x1 = 0, x2 = 0, y1 = 0, y2 = 0, z1 = 0, z2 = 0, roomZoneId, name
							} = ele;
							x1 = x1 / this.cell.size;
							x2 = x2 / this.cell.size;
							y1 = y1 / this.cell.size;
							y2 = y2 / this.cell.size;
							const activeList = this.getArrByAxis(x1, x2, y1, y2);
							this.area.forEach(area => {
								if (activeList.includes(area.index)) {
									area.active = true;
									area.zoneType = ele.zoneType;
									area.roomZoneId = roomZoneId;
								}
							});
							Object.assign(ele, {
								height: Math.abs((y1 * this.cell.size) - (y2 * this.cell.size)),
								width: Math.abs((x1 * this.cell.size) - (x2 * this.cell.size)),
								// 离地高度
								above: z1,
								// 物体高度
								stature: z2 - z1,
								x1,
								x2,
								y1,
								y2
							});
							if (ele.zoneType === '2') { // 床区域特殊处理
								ele.leaveBedWarnParameter.intervalTime = ele.leaveBedWarnParameter
									.intervalTime / 60;
								Object.assign(ele, {
									bedName: name,
									timeCus: [!this.timeList.includes(ele.leaveBedWarnParameter
										.intervalTime)]
								})
							}
						});
						this.roomZones = rows;
						this.indexInit();
					} else {
						uni.showToast({
							icon: 'none',
							title: '请先设置跌倒检测器区域'
						});
					}
				});
			},

			/**
			 * 长度处理（每格大小是0.5，所以不足0.5的需要补足保证格子大小）
			 */
			controlSize(num) {
				const remainder = num % 1;
				if (!remainder) {
					return num;
				} else if (remainder < 0.5) {
					return Math.floor(num) + 0.5;
				} else {
					return Math.ceil(num);
				}

			},
			indexInit() {
				this.activeZone = this.roomZones.filter(ele => ele.zoneType === '2')[0] || {};
			},

			/**
			 * 选择子区域
			 * @param {string} 下标
			 */
			acitve(item) {
				this.activeZone = this.roomZones[item.idx] || {};
			},
			del() {
				this.roomZones.pop();
			},

			/**
			 * 监控区域修改
			 */
			monitorChange([active], type) {
				this.activeZone[type] = this.activeZone[type] == 1 ? 0 : 1;
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
			onTimeBtn(time) {
				this.activeZone.leaveBedWarnParameter.intervalTime = time;
			},
			/**
			 * 选择时间完成
			 */
			dateConfirm({
				value
			}) {
				this.activeZone[this.dateHandle.type] = typeof value === 'number' ? uni.$u.date(value,
					'yyyy/mm/dd hh:MM:ss') : value;
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
			 * 修改区域
			 */
			updateZone(zone) {
				let {
					x1 = 0, x2 = 0, y1 = 0, y2 = 0 } = zone;
				const obj = Object.assign(assignDeep({}, zone), {
					x1: x1 * this.cell.size,
					x2: x2 * this.cell.size,
					y1: y1 * this.cell.size,
					y2: y2 * this.cell.size 
				})
				this.radarDevice(obj);
			},

			/**
			 * 添加/修改区域
			 */
			radarDevice(form) {
				form.leaveBedWarnParameter.intervalTime = form.leaveBedWarnParameter.intervalTime * 60;
				PostSetRadarWareBedZone({
					...form
				}).then(
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
				} = this.activeZone;
				// 已经保存过的
				await PostRemRadarDevice({
					roomZoneId
				});
				this.init();
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
				item.status = 'hover';
				Object.assign(this.touchInfo, {
					x: clientX,
					y: clientY,
					isAdd: !roomZoneId,
					index
				});
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
				const list = this.area.filter(ele => ele.status === 'hover').map(item => item.index),
					{
						isAdd
					} = this.touchInfo;
				const {
					width,
					height
				} = this.getZoneSize(list);
				if (isAdd) {
					if (this.roomZones.length >= 4) {
						this.clearCell();
						return uni.$u.toast('屏蔽区域与离床区域的子区域总数不能超过4个');
					}
					this.$refs.zonePopRef.open({
							roomId: this.deviceInfo.roomId,
							deviceId: this.deviceInfo.deviceId,
							// // 离地高度
							// above: 0,
							// // 物体高度
							// stature: this.roomSize.roomHeight,
							width,
							height
						},
						'add'
					);
				} else {
					console.log(width, height, 'width,height');
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
			 * 确认修改/添加
			 * @param {Object} form
			 */
			confirm(form) {
				// 根据保存的长宽做调整
				let {
					width,
					height
				} = form;
				if (width > (this.roomSize.roomLeft + this.roomSize.roomRight)) {
					this.clearCell();
					return uni.$u.toast('区域超出检测范围，请重新选择');
				}
				width = width / this.cell.size;
				height = height / this.cell.size;
				const {
					roomZoneId
				} = form,
				list = this.area.filter(ele => ele.status === 'hover').map(ele => ele.index),
					newList = this.getArrBySize(Math.min(...list), width, height);
				if (this.area.find(ele => ele.active && newList.includes(ele.index) && ele.roomZoneId !== roomZoneId)) {
					this.clearCell();
					return uni.$u.toast('监测区域重叠，请重新选择');
				}
				Object.assign(form, {
					z1: 0,
					z2: this.roomSize.roomHeight
				}, this.getAxisBySize(Math.min(...list), width, height));
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
				const zoneHeight = Math.floor(max / line) - Math.floor(min / line) + 1,
					zongWidth = (max % line) - (min % line) + 1;
				return {
					height: zoneHeight * this.cell.size,
					width: zongWidth * this.cell.size
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
				return this.getArrBySize(start, pointW, pointH);
			},

			/**
			 * 根据起始点和宽高计算区域
			 */
			getArrBySize(start, width, height) {
				const {
					line
				} = this.getCellSize(),
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
				const {
					scale,
					x,
					y,
					installPosition
				} = this.sizeInfo, {
					line,
					column,
					total
				} = this.getCellSize();
				// 起始点坐标
				const x1 = ((start % line) * scale.x - x) / scale.x,
					y2 = column - Math.floor(start / line) - (installPosition === '1' ? this.roomSize.roomBehind / this
						.cell.size : 0);
				// 起始点在第几列
				const x2 = x1 + width,
					y1 = y2 - height;
				return {
					x1: uni.$u.priceFormat(x1 * this.cell.size, 1),
					x2: uni.$u.priceFormat(x2 * this.cell.size, 1),
					y1: uni.$u.priceFormat(y1 * this.cell.size, 1),
					y2: uni.$u.priceFormat(y2 * this.cell.size, 1)
				};
			},
			
			onSizeInput() {
				uni.$u.toast('请尝试拖动网格进行修改');
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

		.ui-tips {
			font-size: 26rpx;
			color: #353535;
			margin: 32rpx 0;

			>text {
				margin-right: 20rpx;
				position: relative;

				&:nth-child(2) {
					margin-right: 30rpx;

					&::after {
						margin-right: 30rpx;
						margin-left: 5rpx;
						content: '';
						position: absolute;
						top: 10rpx;
						height: 20rpx;
						width: 20rpx;
						border-radius: 50% 50%;
						background: #f8b551;
					}
				}

				&:nth-child(3) {
					&::after {
						margin-right: 30rpx;
						margin-left: 5rpx;
						content: '';
						position: absolute;
						top: 10rpx;
						height: 20rpx;
						width: 20rpx;
						border-radius: 50% 50%;
						background: #007aff;
					}
				}
			}
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

				&.disable {
					background: #e2e2e2 !important;
				}

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
			padding-bottom: 50rpx;

			.ui-list {
				padding: 0 32rpx;
			}

			.ui-date-list {
				width: 330rpx;
				display: flex;
				align-items: center;
				justify-content: space-between;

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

		.ui-time {
			padding: 20rpx 32rpx;
			display: flex;
			align-items: center;
			justify-content: space-between;
			font-size: 34rpx;
			color: #353535;
			position: relative;

			&::after {
				display: inline-block;
				position: absolute;
				content: '';
				width: 100%;
				bottom: 0;
				height: 0.5px;
				background: #e2e2e2;
			}

			.ui-time-box {
				display: flex;
				flex-direction: column;

				.ui-week {
					margin-top: 36rpx;
					font-size: 26rpx;
					color: #888888;
				}
			}

			.ui-time-details {
				margin-top: 36rpx;
				font-size: 26rpx;
				color: #888888;
				display: flex;
				width: 350rpx;
				flex-direction: column;

				&>text {
					&:nth-child(1) {
						margin-bottom: 10rpx;
					}
				}
			}
		}

	}
</style>
