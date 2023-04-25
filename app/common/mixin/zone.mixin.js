/*
 * @Date: 2023-04-25 09:19:09
 * @FilePath: /common/mixin/zone.mixin.js
 * @Description: 子区域mixin
 */
import { numberUtil } from '@/common/utils/numberUtil';
export const zoneMixin = {
	data() {
		return {
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
				size: 0.2
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
			/**区域尺寸**/
			roomSize: {
				roomLeft: 0,
				roomRight: 0,
				roomHeight: 0,
				roomLength: 0,
				roomFront: 0,
				roomBehind: 0
			},
			/**当前页面处理的子区域类型 1 -隐私区域 2 -床**/
			targetZoneType: '' // 每个页面覆盖该值
		}
	},
	computed: {
		getStyle() {
			return {
				width: this.sizeInfo.box.width + 'px',
				height: this.sizeInfo.box.height + 'px'
			};
		},
	},
	methods: {
		zoneInit(roomInfo, rows) {
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
				const w = numberUtil.floatMod(roomLeft + roomRight, this.cell.size);
				const h = numberUtil.floatMod(roomBehind + roomFront, this.cell.size);
				if (w !== 0) {
					roomLeft = this.controlSize(roomLeft),
						roomRight = this.controlSize(roomRight)
				}
				if (h !== 0) {
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
				roomLeft = numberUtil.floatDiv(this.roomSize.roomLeft, cellSize);
				roomRight = numberUtil.floatDiv(this.roomSize.roomRight, cellSize);
			
				roomHeight = numberUtil.floatDiv(this.roomSize.roomHeight, cellSize);
				roomLength = numberUtil.floatDiv(this.roomSize.roomLength, cellSize);
				roomFront = numberUtil.floatDiv(this.roomSize.roomFront, cellSize);
				roomBehind = numberUtil.floatDiv(this.roomSize.roomBehind, cellSize);
			
				// 盒子比例
				const scale = {
						x: numberUtil.floatDiv(width, (roomLeft + roomRight)),
						y: numberUtil.floatDiv(width, (roomLeft + roomRight)) // 以横向距离作为基准
					},
					/**设备位置**/
					x = numberUtil.times(roomLeft, scale.x),
					y = parameter.installPosition === '0' ? numberUtil.times(roomLength, scale.y) : numberUtil.times(roomBehind, scale.y),
					/**父容器**/
					box = {
						width: 300,
						height: parameter.installPosition === '0' ? numberUtil.times(roomLength, scale.y) : numberUtil.times(roomBehind +
							roomFront, scale.y),
					};
				/**网格**/
				this.cell = {
					width: numberUtil.floatDiv(100, Math.floor(numberUtil.floatDiv(box.width, scale.x))) + '%',
					height: numberUtil.floatDiv(100, Math.floor(numberUtil.floatDiv(box.height, scale.y))) + '%',
					size: cellSize
				};
				/**网格个数**/
				let idx = 0;
				this.area = new Array(Math.ceil(numberUtil.floatDiv(box.width, scale.x)) * Math.ceil(numberUtil.floatDiv(box.height, scale.y)))
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
							zoneType: this.targetZoneType,
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
					x1 = numberUtil.floatDiv(x1, this.cell.size);
					x2 = numberUtil.floatDiv(x2, this.cell.size);
					y1 = numberUtil.floatDiv(y1, this.cell.size);
					y2 = numberUtil.floatDiv(y2, this.cell.size);
					const activeList = this.getArrByAxis(x1, x2, y1, y2);
					this.area.forEach(area => {
						if (activeList.includes(area.index)) {
							area.active = true;
							area.zoneType = ele.zoneType;
							area.roomZoneId = roomZoneId;
						}
					});
					Object.assign(ele, {
						height: Math.abs(numberUtil.minus(numberUtil.times(y1, this.cell.size), numberUtil.times(y2, this.cell.size))),
						width: Math.abs(numberUtil.minus(numberUtil.times(x1, this.cell.size), numberUtil.times(x2, this.cell.size))),
						// 离地高度
						above: z1,
						// 物体高度
						stature: z2 - z1,
						x1,
						x2,
						y1,
						y2,
						shadowZoneName: name
					});
					if (this.targetZoneType==='2' && ele.zoneType === '2') { // 床区域特殊处理
						numberUtil.floatDiv(ele.leaveBedWarnParameter.intervalTime = ele.leaveBedWarnParameter
							.intervalTime, 60);
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
		},

		/**
		 * 长度处理（每格大小是0.2，所以不足0.2的需要补足保证格子大小）
		 */
		controlSize(num) {
			const remainder = numberUtil.floatMod(num, this.cell.size);
			if (!remainder) {
				return num;
			} else {
				return num + 0.1;
			}
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
		 * 触摸移动
		 */
		touchMove(event, item, index) {
			const {
				clientX,
				clientY
			} = event.touches[0];
			if (clientX < numberUtil.floatDiv(this.windowWidth - 300, 2) || clientX > numberUtil.floatDiv(this
					.windowWidth - 300, 2) + 300) return;
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
		 * 获取格子参数
		 * returns { width- 每个格子的宽度，height: 格子高度，line：一行几个格子，column：一列几个格子， total：一共几个格子}   
		 */
		getCellSize() {
			// 每个格子的宽度、高度
			const width = this.sizeInfo.scale.x,
				height = this.sizeInfo.scale.y; // 高度
			// 一行几个格子
			const line = Math.floor(numberUtil.floatDiv(this.sizeInfo.box.width, width)),
				// 一列几个格子
				column = Math.floor(numberUtil.floatDiv(this.sizeInfo.box.height, height)),
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
			const countX = Math.ceil(numberUtil.floatDiv(endX - x, width)),
				countY = Math.ceil(numberUtil.floatDiv(endY - y, height));
			// 当前在第几行
			let currentLine = Math.ceil(numberUtil.floatDiv(index, line)),
				// 当前在第几格
				currentCol = Math.ceil(numberUtil.floatMod(index, line));
			if (countY + currentLine > column) return [];
			if (countX + currentCol + 1 > line) return [];
			for (let i = 0; i <= Math.abs(countY); i++) {
				for (let j = 0; j <= Math.abs(countX); j++) {
					if (countX >= 0) {
						// 正向移动
						if (currentLine === 0 || index + 1 <= (currentLine === 0 && countY >= 0) ? line :
							currentLine *
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
			const zoneHeight = Math.floor(numberUtil.floatDiv(max, line)) - Math.floor(numberUtil.floatDiv(min,
					line)) + 1,
				zongWidth = numberUtil.floatMod(max, line) - numberUtil.floatMod(min, line) + 1;
			return {
				height: numberUtil.times(zoneHeight, this.cell.size),
				width: numberUtil.times(zongWidth, this.cell.size),
			};
		},

		/**
		 * 根据坐标获取范围数组
		 */
		getArrByAxis(x1, x2, y1, y2) {
			console.log(x1, x2, y1, y2, 'x1, x2, y1, y2');
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
			let pointY = Math.floor(numberUtil.round(numberUtil.floatDiv(y, scale.y)) - y2),
				pointX;
			if (x1 < 0) {
				pointX = Math.floor(numberUtil.round(numberUtil.floatDiv(x, scale.x)) - Math.abs(x1) + 1);
			} else {
				pointX = Math.floor(numberUtil.round(numberUtil.floatDiv(x, scale.x)) + x1) + 1;
			}
			console.log(pointY, pointX, 'pointY, pointX');
			const start = pointY * line + pointX - 1; // (x1, y1)的点
			const pointW = x2 - x1,
				pointH = y2 - y1; // 区域宽度
			// arr.push(start);
			console.log(start, pointW, pointH, 'start, pointW, pointH');
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
			const x1 = numberUtil.floatDiv((numberUtil.floatMod(start, line) * scale.x - x), scale.x),
				y2 = column - Math.floor(numberUtil.floatDiv(start, line)) - (installPosition === '1' ? numberUtil
					.floatDiv(this.roomSize.roomBehind, this
						.cell.size) : 0);
			// 起始点在第几列
			const x2 = x1 + width,
				y1 = y2 - height;
			return {
				x1: uni.$u.priceFormat(x1 * this.cell.size, 1),
				x2: uni.$u.priceFormat(x2 * this.cell.size, 1),
				y1: uni.$u.priceFormat(y1 * this.cell.size, 1),
				y2: uni.$u.priceFormat(y2 * this.cell.size, 1)
			};
		}
	}
}