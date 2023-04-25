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
				<app-logo text="隐私区域设置" color="#353535"></app-logo>
				<view class="ui-movable">
					<view class="mova-box">
						<movable-area :style="getStyle">
							<template v-for="(item, index) of area">
								<view :key="index + 'c'" class="ui-cell" @touchstart="touchstart($event, item, index)"
									@touchmove.stop.prevent="touchMove($event, item, index)"
									@touchend.stop.prevent="touchend($event, item)"
									:class="{ active: item.active, hover: item.status === 'hover', edit: roomZones.length && activeZone.roomZoneId === item.roomZoneId }"
									:style="cell" v-if="item.zoneType === targetZoneType"></view>
								<view :key="index + 'ec'" v-else :style="cell" class="ui-cell disable"></view>
							</template>

							<movable-view :x="sizeInfo.x - 10"
								:y="sizeInfo.y + (sizeInfo.installPosition == 0 ? 10 : -10)">
								<view class="ui-device"><text class="ui-zone-name"></text></view>
							</movable-view>
						</movable-area>
						<view class="ui-tips">
							<text>每个方块区域大小为 0.2米 x 0.2米</text>
							<text>选中</text>
							<text>未选中</text>
						</view>
					</view>
				</view>
			</view>
			<!-- 配置区域 -->
			<view class="ui-set-box" v-if="getTabList.length">
				<view class="ui-list"><u-tabs lineWidth="108rpx" lineColor="#FEAE43" :list="getTabList"
						@click="acitve"></u-tabs></view>
				<!-- 床区域设置 -->
				<template>
					<u-cell-group>
						<u-cell title="区域名称">
							<u-input placeholder="请输入区域名称" v-model="activeZone.shadowZoneName" clearable border="none"
								inputAlign="right" slot="right-icon"></u-input>
						</u-cell>
						<u-cell title="屏蔽区域长x宽（米）">
							<view class="ui-zooe-size" slot="right-icon" @click="onSizeInput">
								<!-- <u-input border="none" placeholder="请输入" @click="onSizeInput" type="number" readonly inputAlign="left" v-model="activeZone.height" clearable></u-input> -->
								<text>{{activeZone.height}}</text>
								<text>×</text>
								<text>{{activeZone.width}}</text>
								<!-- <u-input border="none" placeholder="请输入" @click="onSizeInput" type="number"  readonly inputAlign="right" v-model="activeZone.width" clearable></u-input> -->
							</view>
						</u-cell>
						<u-cell title="离地高度（米）">
							<u-input type="number" placeholder="请输入离地高度" v-model="activeZone.above" clearable
								border="none" inputAlign="right" slot="right-icon"></u-input>
						</u-cell>
						<u-cell title="物体高度（米）">
							<u-input type="number" placeholder="请输入物体高度" v-model="activeZone.stature" clearable
								border="none" inputAlign="right" slot="right-icon"></u-input>
						</u-cell>
					</u-cell-group>
					<view class="ui-setting-btn wd-btn-group">
						<button class="plain" @click="deleteZone">删除</button>
						<button class="default" @click="updateZone(activeZone)">保存</button>
					</view>
				</template>
			</view>
		</view>
		<zone-pop ref="zonePopRef" @cancel="clearCell" @confirm="confirm"></zone-pop>
	</app-body>
</template>

<script>
	import {
		assignDeep,
		getHoursTime,
		getMinute,
		minuteToTime
	} from '../../../common/utils/util';
	import {
		mapState
	} from 'vuex';
	import {
		GetRoomZone,
		PostRoomList,
		PostRadarDevice,
		PostRemRadarDevice,
		PostSetRadarWareShadowZone
	} from '../../../common/http/api';
	import {
		numberUtil
	} from '../../../common/utils/numberUtil';
	import {
		zoneMixin
	} from '@/common/mixin/zone.mixin.js';

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
				/**时间列表**/
				timeList: [1, 5, 10],
				/**当前页面处理的子区域类型 1 -隐私区域 2 -床**/
				targetZoneType: '1'
			};
		},
		computed: {
			...mapState({
				deviceInfo: state => state.deviceInfo
			}),
			/**获取子区域tab列表**/
			getTabList() {
				let list = [];
				if (this.roomZones.length) {
					list = this.roomZones
						.map((ele, index) => ({
							name: ele.name,
							idx: index,
							zoneType: ele.zoneType
						}))
						.filter(ele => ele.zoneType === this.targetZoneType);
				}
				return list;
			}
		},
		mixins: [zoneMixin], // 网格相关的处理都在mixins中
		onLoad() {
			this.init();
		},
		methods: {
			init() {
				Promise.all([this.getRoomInfo(), this.getRoomZone()]).then(([roomInfo, rows]) => {
					this.zoneInit(roomInfo, rows);
					this.roomZones = rows;
					this.indexInit();
				});
			},

			indexInit() {
				this.activeZone = this.roomZones.filter(ele => ele.zoneType === this.targetZoneType)[0] || {};
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
			onTimeBtn(time, type) {
				this.activeZone[type] = time;
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
			 * 修改区域
			 */
			updateZone(zone) {
				let {
					x1,
					x2,
					y1,
					y2,
					above,
					stature
				} = zone;
				const obj = Object.assign(assignDeep({}, zone), {
					x1: x1 * this.cell.size,
					x2: x2 * this.cell.size,
					y1: y1 * this.cell.size,
					y2: y2 * this.cell.size,
					z1: above,
					z2: stature + above
				});
				console.log(obj, 'updateZone');
				this.radarDevice(obj);
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
				console.log(isAdd, 'isAdd------', this.roomZones);
				if (isAdd) {
					if (this.roomZones.length >= 4) {
						this.clearCell();
						return uni.$u.toast('屏蔽区域与离床区域的子区域总数不能超过4个');
					}
					// 添加的话弹窗填写信息
					this.$refs.zonePopRef.open({
							roomId: this.deviceInfo.roomId,
							deviceId: this.deviceInfo.deviceId,
							// 离地高度
							above: 0,
							// 物体高度
							stature: this.roomSize.roomHeight,
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
			 * 添加/修改区域
			 */
			radarDevice(form) {
				PostSetRadarWareShadowZone({
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
			 * 确认修改/添加
			 * @param {Object} form
			 */
			confirm(form) {
				// 根据保存的长宽做调整
				let {
					width,
					height,
					above,
					stature
				} = form;
				console.log(width, height, 'width, height');
				if (width > this.roomSize.roomLeft + this.roomSize.roomRight) {
					this.clearCell();
					return uni.$u.toast('区域超出检测范围，请重新选择');
				}
				width =  numberUtil.floatDiv(width, this.cell.size);
				height = numberUtil.floatDiv(height, this.cell.size);
				const {
					roomZoneId
				} = form,
				list = this.area.filter(ele => ele.status === 'hover').map(ele => ele.index),
					newList = this.getArrBySize(Math.min(...list), width, height);
				if (this.area.find(ele => ele.active && newList.includes(ele.index) && ele.roomZoneId !== roomZoneId)) {
					this.clearCell();
					return uni.$u.toast('监测区域重叠，请重新选择');
				}
				Object.assign(
					form, {
						z1: above,
						z2: stature + above
					},
					this.getAxisBySize(Math.min(...list), width, height)
				);
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

			onSizeInput() {
				uni.$u.toast('请尝试拖动网格进行修改');
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
						background: #ff6969;
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
			border: 1rpx solid #979797;
			overflow: hidden;

			.ui-cell {
				box-sizing: border-box;
				display: inline-block;
				float: left;
				border-top: 1rpx solid #979797;
				border-left: 1rpx solid #979797;

				&.disable {
					background: #e2e2e2 !important;
				}

				&.hover {
					opacity: 0.3 !important;
					background: #ff6969;
				}

				&.active {
					opacity: 1;
					background: #ff6969;
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
			width: 250rpx;
			display: flex;
			align-items: center;
			justify-content: space-between;
		}
	}
</style>