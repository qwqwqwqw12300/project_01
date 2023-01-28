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
				<!-- <u-divider text="100米"></u-divider>
				<u-divider text="100米"></u-divider>
				<u-divider text="100米"></u-divider>
				<u-divider text="100米"></u-divider> -->
				<movable-area>
					<movable-view v-for="(item, index) of roomZones" :key="index" :x="item.x" :y="item.y"
						:style="{ height: item.height * 10 + 'rpx', width: item.width * 10 + 'rpx' }" direction="all"
						@change="
							e => {
								onChange(e, index);
							}
						">
						<view class="ui-device" v-if="item.zoneName === '设备'">
							<text class="ui-zone-name">设备</text>
							<text>x：{{ item.old.x }}</text>
							<text>y：{{ item.old.y }}</text>
						</view>
						<template v-else>
							<text class="ui-zone-name">{{ item.zoneName || '未命名' }}</text>
							<text>x：{{ item.old.x }}</text>
							<text>y：{{ item.old.y }}</text>
						</template>
					</movable-view>
				</movable-area>
			</view>
			<view class="ui-add">
				<view>
					<u-text prefixIcon="file-text" iconStyle="font-size: 40rpx" text="子区域名称" color="#444" size="28rpx">
					</u-text>
					<view class="ui-input">
						<u-input v-model="zoneInfo.zoneName" placeholder="请输入子区域名称" :border="'none'" fontSize="28rpx"
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
				<view class="ui-add-btn"><button class="wd-sms" @click="addZone">添加子区域</button></view>
			</view>
			<view class="ui-list">
				<template v-for="(item, index) of roomZones">
					<button :class="activeZone === index ? 'active' : ''" @click="acitve(index)" :key="index"
						v-if="index!==0">{{item.zoneName || '未命名'}}</button>
				</template>

			</view>
			<!-- 区域设置 -->
			<view class="ui-setting" :key="activeZone" v-if="roomZones.length > 1">
				<view>
					<u-checkbox-group @change="monitorChange($event, 'isFall')" placement="column">
						<u-checkbox activeColor="#1aa208" :checked="roomZones[activeZone].zoneType === 1"
							:customStyle="{ marginBottom: '8px' }" label="私人区域" name="zoneType"></u-checkbox>
					</u-checkbox-group>
					<u-checkbox-group @change="monitorChange($event, 'isFall')" placement="column">
						<u-checkbox activeColor="#1aa208" :checked="roomZones[activeZone].fallFlag === 1"
							:customStyle="{ marginBottom: '8px' }" label="跌倒监控" name="fallFlag"></u-checkbox>
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
							<u-checkbox-group placement="column" @change="monitorChange($event, 'inFlag')">
								<u-checkbox activeColor="#1aa208" :checked="roomZones[activeZone].inFlag == 1"
									:customStyle="{ marginBottom: '40rpx' }" label="进入监控区域超时报警时间" name="inFlag">
								</u-checkbox>
							</u-checkbox-group>
							<u-checkbox-group placement="column" @change="monitorChange($event, 'outFlag')">
								<u-checkbox activeColor="#1aa208" :checked="roomZones[activeZone].outFlag == 1"
									label="离开监控区域超时报警时间" name="outFlag"></u-checkbox>
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
					<view class="ui-setting-btn wd-btn-gloup">
						<button @click="radarDevice">保存</button>
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
		assignDeep
	} from '../../common/utils/util';
	import {
		mapState
	} from 'vuex';
	import {
		GetRoomZone,
		PostRoomList,
		PostRadarDevice
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
		zoneName: '',
		/*监控类型 0-监控区域  1-私人区域**/
		zoneType: 0,
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
		entryTime: '',
		/**离开报警区域时间**/
		departureTime: '',
		/**开始监控时间**/
		startTime: '',
		/**结束监控时间**/
		endTime: '',
		/**进入监控区域报警**/
		inFlag: 0,
		/**离开监控区域报警**/
		outFlag: 0
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
				activeZone: 1,
				roomZones: [],
				/**设备位置**/
				devices: {
					x: 0,
					y: 0
				}
			};
		},
		computed: {
			...mapState({
				deviceInfo: state => state.deviceInfo
			})
		},
		onLoad() {
			Promise.all([
				this.getRoomInfo(),
				this.getRoomZone()
			]);
		},
		methods: {
			/**
			 * 添加区域
			 */
			addZone() {
				// this.roomZones.push();
				// this.zoneInfo = assignDeep({}, ZONE);
				this.roomZones.push(assignDeep({}, ZONE, this.zoneInfo, {
					roomId: this.deviceInfo.roomId,
					deviceId: this.deviceInfo.deviceId
				}));
				this.radarDevice().then(r => r, err => this.roomZones.pop());

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
				this.roomZones[this.activeZone][type] = !!active;
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
			dateConfirm({
				value
			}) {
				this.roomZones[this.activeZone][this.dateHandle.type] = (typeof value === 'number') ? uni.$u.date(value,
					'yyyy-mm-dd hh:MM:ss') : value;
				this.dateHandle.show = false;
			},

			/**
			 * 创建/修改设备
			 */
			radarDevice() {
				const x = this.roomZones[0].x1,
					y = this.roomZones[0].y1;
				const payload = this.roomZones.map(ele => {
					const {
						old,
						width,
						height
					} = ele;
					return {
						...ele,
						x2: old.x - x,
						x1: old.x - x - width,
						y2: old.y - y,
						y1: old.y - y - height,
					}
				});
				delete payload.old;
				delete payload.x;
				delete payload.y;
				return new Promise((resolve, reject) => {
					PostRadarDevice(payload).then(res => {
						resolve();
					}, error => reject());
				});
			},

			/**
			 * 获取房间信息
			 */
			getRoomInfo() {
				const {
					familyId,
					roomId
				} = this.deviceInfo;
				PostRoomList({
					familyId
				}).then(res => {
					this.roomInfo = res.rows.filter(ele => ele.roomId === roomId)[0] || {};
					console.log(this.roomInfo, 'this.roomInfo');
				});
			},

			/**
			 * 获取子区域列表
			 */
			getRoomZone() {
				return new Promise(async resolve => {
					const {
						rows = []
					} = GetRoomZone({
						deviceId: this.deviceInfo.deviceId
					});
					if (!rows.length) {
						rows.push(assignDeep({}, ZONE, {
							zoneName: '设备',
							roomId: this.deviceInfo.roomId,
							deviceId: this.deviceInfo.deviceId
						}))
					}
					const x = rows[0].x1,
						y = rows[0].y1;
					rows.forEach((ele, idx) => {
						if (idx !== 0) {
							const {
								x1 = 0, x2 = 0, y1 = 0, y2 = 0
							} = ele;
							Object.assign(ele, {
								height: y1 - y2,
								width: x1 - x2,
								x: (x1 + x2) / 2 + x,
								y: (y1 + y2) / 2 + y,
								old: {
									x: (x1 + x2) / 2 + x,
									y: (y1 + y2) / 2 + y,
								}
							});
						}
					});
					this.roomZones = rows;
					resolve(rows);
				})
			},
			/**
			 * 删除子区域
			 */
			deleteZone() {
				const {
					roomZoneId
				} = this.roomZones[this.activeZone];
				PostRemRadarDevice({
					roomZoneId
				}).then(res => {
					this.roomZones.splice(this.activeZone, 1);
				})
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
			font-size: 16rpx;

			.ui-zone-name {
				font-size: 20rpx;
			}

			.ui-device {
				min-height: 120rpx;
				min-width: 120rpx;
				display: flex;
				align-items: center;
				justify-content: center;
				flex-direction: column;
				text-align: center;
				background-color: #00eaff;
				border-radius: 50% 50%;
			}

			@each $idx,
			$bg in (2: unset, 3: #e5004f, 4: #ec6941, 5: #448aca) {
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
				margin-top: 10rpx;
				width: 100%;
				display: inline-flex;
				justify-content: space-between;
				align-items: center;
				font-size: 30rpx;
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
						width: 200rpx;
					}

					.ui-timing-active {
						display: flex;
						justify-content: right;
						align-items: center;
						font-size: 30rpx;
						line-height: 50rpx;
						height: 50rpx;
						width: 170rpx;
						border: 1rpx solid #e2e2e2;
						padding: 0 10rpx;
						box-sizing: border-box;

						text {
							display: inline-block;
							margin-right: 30rpx;
							width: 160rpx;
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
