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
						<view class="ui-cell" @touchstart="touchstart($event, index)"
							@touchmove="touchstart($event, index)" :style="sizeInfo.cell"
							v-for="(item, index) of sizeInfo.cell.area" :key="index">
						</view>
						<movable-view v-for="(item, index) of roomZones" :key="index" :x="item && item.x"
							:y="item && item.y" :style="{ height: item.height + 'px', width: item.width + 'px' }"
							direction="all" @change="
								e => {
									onChange(e, index);
								}
							">
							<template>
								<text class="ui-zone-name">{{ item.name || '未命名' }}</text>
								<text>x:{{ ($u.priceFormat((item.old.x - sizeInfo.x) / sizeInfo.scale.x), 1) }}</text>
								<text>y:{{ (0 - $u.priceFormat((item.old.y - sizeInfo.y) / sizeInfo.scale.y), 1)}}</text>
							</template>
						</movable-view>
						<movable-view :x="sizeInfo.x" :y="sizeInfo.y">
							<view class="ui-device">
								<text class="ui-zone-name">设备</text>
							</view>
						</movable-view>
					</movable-area>
				</view>
			</view>
			<view class="ui-add">
				<view>
					<u-text prefixIcon="file-text" iconStyle="font-size: 40rpx" text="子区域名称" color="#444" size="28rpx">
					</u-text>
					<view class="ui-input">
						<u-input v-model="zoneInfo.name" placeholder="请输入子区域名称" :border="'none'" fontSize="28rpx"
							clearable></u-input>
					</view>
				</view>
				<view>
					<u-text prefixIcon="file-text" iconStyle="font-size: 40rpx" text="子区域尺寸" color="#444" size="28rpx">
					</u-text>
					<view class="ui-shape">
						<view>
							<text>长</text>
							<view class="ui-input">
								<u-input type="number" v-model="zoneInfo.height" placeholder="请输入长度" :border="'none'"
									fontSize="28rpx">
									<template slot="suffix">
										<text>米</text>
									</template>
								</u-input>
							</view>
						</view>
						<view>
							<text>宽</text>
							<view class="ui-input">
								<u-input type="number" v-model="zoneInfo.width" placeholder="请输入宽度" :border="'none'"
									fontSize="28rpx">
									<template slot="suffix">
										<text>米</text>
									</template>
								</u-input>
							</view>
						</view>
					</view>
				</view>
				<view class="ui-add-btn">
					<button class="wd-sms" @click="addZone">添加子区域</button>
					<button @click="radarDevice">全部保存</button>
				</view>
			</view>
			<view class="ui-list">
				<template v-for="(item, index) of roomZones">
					<button :class="activeZone === index ? 'active' : ''" @click="acitve(index)"
						:key="index">{{item.name || '未命名'}}</button>
				</template>
			</view>
			<!-- 区域设置 -->
			<view class="ui-setting" :key="activeZone" v-if="roomZones && roomZones.length">
				<view>
					<u-checkbox-group @change="monitorChange($event, 'existFlag')" placement="column">
						<u-checkbox activeColor="#1aa208" labelSize="28rpx"
							:checked="roomZones[activeZone].existFlag == 1" :customStyle="{ marginBottom: '8px' }"
							label="进出监控" name="existFlag"></u-checkbox>
					</u-checkbox-group>
					<u-checkbox-group @change="monitorChange($event, 'fallFlag')" placement="column">
						<u-checkbox activeColor="#1aa208" labelSize="28rpx"
							:checked="roomZones[activeZone].fallFlag == 1"
							:cusmonitorChangetomStyle="{ marginBottom: '8px' }" label="跌倒监控" name="fallFlag">
						</u-checkbox>
					</u-checkbox-group>

					<!-- <u-checkbox-group @change="monitorChange($event, 'isAccess')" placement="column">
						<u-checkbox activeColor="#1aa208" :checked="list[activeZone].isAccess"
							:customStyle="{ marginBottom: '8px' }" label="进出监控" name="isAccess"></u-checkbox>
					</u-checkbox-group> -->

					<view class="ui-date-list">
						<text>开始监控时间</text>
						<view class="ui-date active" @click="openDate('startTime')">
							<text>{{roomZones[activeZone].startTime || '未设置'}}</text>
							<u-icon name="calendar" color="#414141" size="40rpx"></u-icon>
						</view>
					</view>
					<view class="ui-date-list ui-margin">
						<text>结束监控时间</text>
						<view class="ui-date active" @click="openDate('endTime')">
							<text>{{roomZones[activeZone].endTime || '未设置'}}</text>
							<u-icon name="calendar" color="#414141" size="40rpx"></u-icon>
						</view>
					</view>
					<view class="ui-timing">
						<view class="ui-timing-pos">
							<u-checkbox-group placement="column" @change="monitorChange($event, 'inMonitorFlag')">
								<u-checkbox activeColor="#1aa208" :checked="roomZones[activeZone].inMonitorFlag == 1"
									:customStyle="{ marginBottom: '40rpx'}" labelSize="28rpx" label="进入监控区域超时报警时间"
									name="inMonitorFlag">
								</u-checkbox>
							</u-checkbox-group>
							<u-checkbox-group placement="column" @change="monitorChange($event, 'outMonitorFlag')">
								<u-checkbox activeColor="#1aa208" labelSize="28rpx" :checked="
									roomZones[activeZone].outMonitorFlag==1" label="离开监控区域超时报警时间" name="outMonitorFlag">
								</u-checkbox>
							</u-checkbox-group>
						</view>
						<view class="ui-timing-pos">
							<view class="ui-timing-active active" @click="openDate('entryTime')">
								<text>{{roomZones[activeZone].entryTime || '请选择'}}</text>
								<u-icon name="arrow-down" size="28rpx"></u-icon>
							</view>
							<view class="ui-timing-active active" @click="openDate('departureTime')">
								<text>{{roomZones[activeZone].departureTime || '请选择'}}</text>
								<u-icon name="arrow-down" size="28rpx"></u-icon>
							</view>
						</view>
					</view>
					<view class="ui-setting-btn">
						<button @click="deleteZone">删除</button>
					</view>
				</view>
			</view>
			<!-- /区域设置 -->
		</view>
		<u-datetime-picker v-if="dateHandle.show" :show="dateHandle.show" @confirm="dateConfirm"
			@cancel="dateHandle.show = false" :mode="dateHandle.mode"></u-datetime-picker>
	</app-body>
</template>

<script>
	import {
		assignDeep,
		getHoursTime
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

	/**监控区域**/
	const ZONE = {
		/**区域id**/
		roomZoneId: '',
		/**设备id**/
		deviceId: '',
		/**房间id**/
		roomId: '',
		/**区域名称**/
		name: '',
		/*监控类型 0-监控区域  1-私人区域**/
		existFlag: 0,
		/**跌倒监控**/
		fallFlag: 0,
		x1: 0,
		x2: 0,
		y1: 0,
		y2: 0,
		z1: 100,
		z2: 100,
		old: {
			x: 0,
			y: 0,
		},
		x: 0,
		y: 0,
		/**进入报警区域时间**/
		entryTime: null,
		/**离开报警区域时间**/
		departureTime: null,
		/**开始监控时间**/
		startTime: null,
		/**结束监控时间**/
		endTime: null,
		/**进入监控区域报警**/
		inMonitorFlag: 0,
		/**离开监控区域报警**/
		outMonitorFlag: 0
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
					/**网格大小**/
					cell: {
						width: 10,
						height: 10
					},
					/**雷达波所处的位置**/
					x: 125,
					y: 300,
					/**因为图像大小限制，需要按照比例缩放**/
					scale: {
						x: 10,
						y: 10
					}
				},
				/**拖拽信息保存**/
				touchInfo: {
					startX: 0,
					startY: 0,
					startIndex: 0,
					isAdd: true
				}
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
				}
			},
			/**
			 * 获取背景单元格样式
			 */
			getCellStyle() {
				const obj = {}
				return {}
			}
		},
		onLoad() {
			Promise.all([
				this.getRoomInfo(),
				this.getRoomZone()
			]).then(([roomInfo, rows]) => {
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
					console.log(roomLeft,
						roomRight,
						roomLength,
						roomHeight, '设备范围');
					// 盒子比例
					const scale = {
							x: width / (roomLeft + roomRight),
							y: width / (roomLeft + roomRight) // 以横向距离作为基准
						},
						/**设备位置**/
						x = roomLeft * scale.x - 10,
						y = roomLength * scale.y + 10,
						/**父容器**/
						box = {
							width: 300,
							height: roomLength * scale.y
						},
						/**网格**/
						cell = {
							/**网格个数**/
							area: new Array((box.width / scale.x) * (box.height / scale.y)).fill({
								status: 'none',
								axis: {
									x: 0,
									y: 0
								}
							}),
							width: 100 / (box.width / scale.x) + '%',
							height: 100 / (box.height / scale.y) + '%',
						}
					Object.assign(this.sizeInfo, {
						x,
						y,
						scale,
						box,
						cell,
					});
					console.log(this.sizeInfo, 'this.sizeInfo');
					rows.forEach((ele, idx) => {
						const {
							x1 = 0, x2 = 0, y1 = 0, y2 = 0
						} = ele;
						Object.assign(ele, {
							height: Math.abs((y1 - y2) * scale.y),
							width: Math.abs((x1 - x2) * scale.x),
							x: x2 * scale.x + x,
							y: (0 - y2 * scale.y + y),
							old: {
								x: x2 * scale.x + x,
								y: (0 - y2 * scale.y + y),
							},
							departureTime: uni.$u.timeFormat(ele.departureTime, 'hh:MM'),
							entryTime: uni.$u.timeFormat(ele.entryTime, 'hh:MM'),
							/**开始监控时间**/
							startTime: uni.$u.timeFormat(ele.startTime, 'hh:MM'),
							/**结束监控时间**/
							endTime: uni.$u.timeFormat(ele.endTime, 'hh:MM'),
						});
					});
					this.roomZones = rows;
				} else {
					uni.showToast({
						icon: 'none',
						title: '请先设置雷达波区域'
					});
					// setTimeout(() => {
					// 	uni.navigateBack();
					// }, 3000)

				}

			})

		},
		methods: {
			/**
			 * 添加区域
			 */
			addZone() {
				const {
					name,
					width,
					height
				} = this.zoneInfo;
				if (this.zoneInfo.name && this.zoneInfo.width && this.zoneInfo.height) {
					const obj = assignDeep({}, ZONE, {
						roomId: this.deviceInfo.roomId,
						deviceId: this.deviceInfo.deviceId,
						z1: 0,
						z2: this.deviceInfo.parameter.deviceLocation.roomHeight,
						name,
						width: width * this.sizeInfo.scale.x,
						height: height * this.sizeInfo.scale.y,
					});
					this.roomZones.push(obj);
				} else {
					uni.showToast({
						icon: 'none',
						title: '请完善区域信息'
					})
				}
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
			dateConfirm({
				value
			}) {
				this.roomZones[this.activeZone][this.dateHandle.type] = (typeof value === 'number') ? uni.$u.date(
					value,
					'yyyy/mm/dd hh:MM:ss') : value;
				this.dateHandle.show = false;
			},

			/**
			 * 添加/修改设备
			 */
			radarDevice() {
				const {
					x,
					y,
					scale
				} = this.sizeInfo;

				const list = this.roomZones.map(ele => {
					const {
						old,
						width,
						height,
						startTime,
						endTime,
						entryTime,
						departureTime
					} = ele,
					obj =
						assignDeep({}, {
							...ele,
							x2: uni.$u.priceFormat((old.x - x) / scale.x, 1),
							x1: uni.$u.priceFormat((old.x - x - width) / scale.x, 1),
							y2: uni.$u.priceFormat(0 - ((old.y - y) / scale.y), 1),
							y1: uni.$u.priceFormat(0 - ((old.y - y - height) / scale.y), 1),
							startTime: getHoursTime(startTime),
							endTime: getHoursTime(endTime),
							entryTime: getHoursTime(entryTime),
							departureTime: getHoursTime(departureTime)
						});
					console.log(old.y, 'old.y');
					delete obj.old;
					delete obj.x;
					delete obj.y;
					delete obj.height;
					delete obj.width;
					return PostRadarDevice(obj);
				});

				Promise.all(list).then(res => {
					uni.showToast({
						icon: 'none',
						title: '保存成功'
					});
				})
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
				})
			},
			/**
			 * 删除子区域
			 */
			async deleteZone() {
				const {
					roomZoneId
				} = this.roomZones[this.activeZone];
				if (roomZoneId) { // 已经保存过的
					await PostRemRadarDevice({
						roomZoneId
					});
					this.roomZones.splice(this.activeZone, 1);
					this.activeZone = 0;
				} else {
					this.roomZones.splice(this.activeZone, 1);
					this.activeZone = 0;
				}

			},

			/**
			 * 触摸事件
			 */
			touchstart(event, item, index) {
				Object.assign(this.touchInfo, {
					x: event.target.offsetLeft,
					y: event.target.offsetTop,
					isAdd: item.status === 'none',
					index
				});
				console.log(event, 'event');
			},

			/**
			 * 拖拽事件
			 */
			touchMove(event, item, index) {
				const {
					offsetLeft,
					offsetTop
				} = event.target;


			},

			/**
			 * 获取格子个数
			 */
			getCellCount() {

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

			>view {
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

			>view {
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

	}
</style>
