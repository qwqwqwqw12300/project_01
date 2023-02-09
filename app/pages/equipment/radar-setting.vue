<!--
* @Author: zhanch
* @Date: 2023-02-07 09:20:38
* @FilePath: /pages/equipment/radar-setting/radar-setting.vue
* @Description: 雷达波设置
-->

<template>
	<app-body :bodyStyle="{backgroundPositionY: '-200rpx'}">
		<app-logo text="设置雷达波设备"></app-logo>
		<view class="wd-add ui-change">
			<view class="ui-add-box">
				<view class="ui-input">
					<u-text size="28rpx" prefixIcon="home" iconStyle="font-size: 36rpx" text="设备名称"></u-text>
					<u-input placeholder="请输入设备名称" :maxlength="6" v-model="editFrom.deviceName" border="bottom"
						clearable>
					</u-input>
				</view>
				<view>
					<u-text size="28rpx" prefixIcon="setting" iconStyle="font-size: 36rpx" text="检测高度"></u-text>
					<view class="ui-slider">
						<u-slider min="10" max="60" v-model="editFrom.roomHeight" activeColor="#eeaa3d"
							blockColor="#eeaa3d" inactiveColor="#c0c4cc" />
						<text>{{$u.priceFormat(editFrom.roomHeight/10, 2)}}米</text>
					</view>
				</view>
				<view>
					<u-text size="28rpx" prefixIcon="setting" iconStyle="font-size: 36rpx" text="检测长度"></u-text>
					<view class="ui-slider">
						<u-slider min="10" max="60" v-model="editFrom.roomLength" activeColor="#eeaa3d"
							blockColor="#eeaa3d" inactiveColor="#c0c4cc" />
						<text>{{$u.priceFormat(editFrom.roomLength/10, 2)}}米</text>
					</view>
				</view>
				<view>
					<u-text size="28rpx" prefixIcon="setting" iconStyle="font-size: 36rpx" text="检测左长度">
					</u-text>
					<view class="ui-slider">
						<u-slider min="10" max="60" v-model="editFrom.roomRight" activeColor="#eeaa3d"
							blockColor="#eeaa3d" inactiveColor="#c0c4cc" />
						<text>{{$u.priceFormat(editFrom.roomRight/10, 2)}}米</text>
					</view>
				</view>
				<view>
					<u-text size="28rpx" prefixIcon="setting" iconStyle="font-size: 36rpx" text="检测右长度">
					</u-text>
					<view class="ui-slider">
						<u-slider min="10" max="60" v-model="editFrom.roomLeft" activeColor="#eeaa3d"
							blockColor="#eeaa3d" inactiveColor="#c0c4cc" />
						<text>{{$u.priceFormat(editFrom.roomLeft/10, 2)}}米</text>
					</view>
				</view>
			</view>
		</view>
		<!-- 区域设置 -->
		<view class="ui-setting">
			<view>
				<u-checkbox-group @change="monitorChange($event, 'existFlag')" placement="column">
					<u-checkbox activeColor="#1aa208" labelSize="28rpx" :checked="editFrom.existFlag == 1"
						:customStyle="{ marginBottom: '8px' }" label="进出监控" name="existFlag"></u-checkbox>
				</u-checkbox-group>
				<u-checkbox-group @change="monitorChange($event, 'fallFlag')" placement="column">
					<u-checkbox activeColor="#1aa208" labelSize="28rpx" :checked="editFrom.fallFlag == 1"
						:cusmonitorChangetomStyle="{ marginBottom: '8px' }" label="跌倒监控" name="fallFlag">
					</u-checkbox>
				</u-checkbox-group>
				<view class="ui-date-list">
					<text>开始监控时间</text>
					<view class="ui-date active" @click="openDate('startTime')">
						<text>{{editFrom.startTime || '未设置'}}</text>
						<u-icon name="calendar" color="#414141" size="40rpx"></u-icon>
					</view>
				</view>
				<view class="ui-date-list ui-margin">
					<text>结束监控时间</text>
					<view class="ui-date active" @click="openDate('endTime')">
						<text>{{editFrom.endTime || '未设置'}}</text>
						<u-icon name="calendar" color="#414141" size="40rpx"></u-icon>
					</view>
				</view>
				<view class="ui-timing">
					<view class="ui-timing-pos">
						<u-checkbox-group placement="column" @change="monitorChange($event, 'inMonitorFlag')">
							<u-checkbox activeColor="#1aa208" :checked="editFrom.inMonitorFlag == 1"
								:customStyle="{ marginBottom: '40rpx'}" labelSize="28rpx" label="进入监控区域超时报警时间"
								name="inMonitorFlag">
							</u-checkbox>
						</u-checkbox-group>
						<u-checkbox-group placement="column" @change="monitorChange($event, 'outMonitorFlag')">
							<u-checkbox activeColor="#1aa208" labelSize="28rpx" :checked="
								editFrom.outMonitorFlag==1" label="离开监控区域超时报警时间" name="outMonitorFlag">
							</u-checkbox>
						</u-checkbox-group>
					</view>
					<view class="ui-timing-pos">
						<view class="ui-timing-active active" @click="openDate('entryTime')">
							<text>{{editFrom.entryTime || '请选择'}}</text>
							<u-icon name="arrow-down" size="28rpx"></u-icon>
						</view>
						<view class="ui-timing-active active" @click="openDate('departureTime')">
							<text>{{editFrom.departureTime || '请选择'}}</text>
							<u-icon name="arrow-down" size="28rpx"></u-icon>
						</view>
					</view>
				</view>
				<u-datetime-picker v-if="dateHandle.show" :show="dateHandle.show" @confirm="dateConfirm"
					@cancel="dateHandle.show = false" :mode="dateHandle.mode"></u-datetime-picker>
			</view>
		</view>
		<view class="wd-btn-gloup">
			<button @click="more">更多选项</button>
			<button class="green" @click="editSubmit">确定</button>
		</view>
		<!-- /区域设置 -->
	</app-body>
</template>

<script>
	import {
		getHoursTime
	} from '../../common/utils/util';
	import {
		INIT_DEIVCE_SET
	} from '../../config/db';
	export default {
		data() {
			return {
				/**是否开启 none edit more**/
				mode: 'none',
				editFrom: {
					...INIT_DEIVCE_SET
				},
				dateHandle: {
					type: '',
					show: false,
					mode: 'time'
				},
			};
		},
		onLoad() {
			const device = this.$getCache('setDevice') || {};
			const {
				departureTime,
				entryTime,
				startTime,
				endTime,
				roomLeft,
				roomHeight,
				roomRight,
				roomLength
			} = device;
			this.editFrom = {
				...device,
				departureTime: uni.$u.timeFormat(departureTime, 'hh:MM'),
				entryTime: uni.$u.timeFormat(entryTime, 'hh:MM'),
				/**开始监控时间**/
				startTime: uni.$u.timeFormat(startTime, 'hh:MM'),
				/**结束监控时间**/
				endTime: uni.$u.timeFormat(endTime, 'hh:MM'),
				roomLeft: roomLeft * 10,
				roomHeight: roomHeight * 10,
				roomRight: roomRight * 10,
				roomLength: roomLength * 10
			};
			this.mode = 'edit';
		},
		methods: {
			/**
			 * 修改完成
			 */
			editSubmit() {
				const {
					departureTime,
					entryTime,
					startTime,
					endTime,
					roomLeft,
					roomHeight,
					roomRight,
					roomLength
				} = this.editFrom;
				this.$emit('confirm', {
					...this.editFrom,
					roomLeft: roomLeft / 10,
					roomHeight: roomHeight / 10,
					roomRight: roomRight / 10,
					roomLength: roomLength / 10,
					startTime: getHoursTime(startTime) || 0,
					endTime: getHoursTime(endTime) || 0,
					entryTime: getHoursTime(entryTime) || 0,
					departureTime: getHoursTime(departureTime) || 0
				});
				this.close();
			},

			more() {
				this.mode = 'more';
			},

			back() {
				this.mode = 'edit';
			},

			/**
			 * 开启选择时间
			 */
			openDate(type) {
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
				this.editFrom[this.dateHandle.type] = (typeof value === 'number') ? uni.$u.date(
					value,
					'yyyy/mm/dd hh:MM:ss') : value;
				this.dateHandle.show = false;
			},

			/**
			 * 监控区域修改
			 */
			monitorChange([active], type) {
				this.editFrom[type] = this.editFrom[type] == 1 ? 0 : 1;
			},

		}
	}
</script>

<style lang="scss">
	.wd-add {
		box-sizing: border-box;
		width: 100%;
		padding: 0 31rpx;

		&>view {
			margin-top: 52rpx;

			&.ui-add-box {
				padding: 10rpx 20rpx;

				&>* {
					margin-top: 30rpx;
				}
			}

			.ui-input {
				position: relative;

				&::after {
					bottom: 10rpx;
					left: 40rpx;
					content: '';
					width: 500rpx;
					height: 1rpx;
					background: #e4e4e4;
					position: absolute;
				}
			}
		}



		.ui-slider {
			width: 100%;
			display: flex;
			flex-direction: row;
			align-items: center;
			justify-content: space-between;

			text {
				font-size: 26rpx;
				color: #414141;
			}

			&>* {
				:nth-child(1) {
					width: 450rpx;
				}
			}
		}

		.wd-btn-gloup {
			text-align: center;
			margin-top: 70rpx;

			button {
				width: 237rpx;
				height: 71rpx;
				font-size: 28rpx;
				color: #ffffff;
			}
		}
	}

	.ui-setting {
		width: 100%;
		text-align: center;
		padding: 20rpx 0;

		.wd-btn-gloup {
			margin-top: 50rpx;
		}

		>view {
			padding: 35rpx;
			box-sizing: border-box;
			display: inline-block;
			width: 658rpx;
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
</style>
