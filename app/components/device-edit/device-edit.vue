<!--
* @Author: zhanch
* @Date: 2023-01-31 15:18:07
* @FilePath: /components/device-edit/device-edit.vue
* @Description: 设备信息修改
-->

<template>
	<view>
		<u-popup :closeable="true" :round="10" :show="mode === 'edit'" mode="center" @close="close">
			<view class="wd-add-edit ui-change">
				<u-text prefixIcon="edit-pen" :iconStyle="{ fontSize: '38rpx', color: '#ea942f' }" color="#ea942f"
					size="30rpx" text="修改设备"></u-text>
				<view class="ui-add-box">
					<view>
						<u-text size="28rpx" prefixIcon="home" iconStyle="font-size: 36rpx" text="设备名称"></u-text>
						<u-input placeholder="请输入设备名称" :maxlength="6" v-model="editFrom.deviceName" border="bottom"
							clearable>
						</u-input>
					</view>
					<view class="ui-input ui-radio">
						<u-text size="28rpx" prefixIcon="map" iconStyle="font-size: 36rpx" text="设备位置"></u-text>
						<u-radio-group v-model="editFrom.location" placement="row">
							<u-radio :customStyle="{margin: '20rpx'}" v-for="item of locationList" :key="item"
								activeColor="#1aa208" :name="item" :label="item"></u-radio>
						</u-radio-group>
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
				<view class="wd-btn-group">
					<button @click="more">更多选项</button>
					<button class="green" @click="editSubmit">确定</button>
				</view>
			</view>
		</u-popup>
		<u-popup :closeable="true" :round="10" :show="mode === 'more'" mode="center" @close="close">
			<!-- 区域设置 -->
			<view class="ui-setting">
				<view>
					<view class="ui-switch">
						<u-switch space="2" v-model="editFrom.fallFlag" activeValue="1" inactiveValue="0"
							activeColor="#85B224" size="20" inactiveColor="rgb(230, 230, 230)">
						</u-switch>
						<text>跌倒监控</text>
					</view>
					<view class="ui-switch">
						<u-switch space="2" v-model="editFrom.existFlag" activeValue="1" inactiveValue="0"
							activeColor="#85B224" size="20" inactiveColor="rgb(230, 230, 230)">
						</u-switch>
						<text>进出监控</text>
					</view>
					<view class="ui-date-list">
						<u-text prefixIcon="play-circle" iconStyle="font-size: 38rpx" size="28rpx" text="开始监控时间">
						</u-text>
						<!-- <u-icon name="play-circle"></u-icon>
						<text></text> -->
						<view class="ui-date active" @click="openDate('startTime')">
							<text>{{editFrom.startTime || '未设置'}}</text>
							<u-icon name="calendar" color="#414141" size="40rpx"></u-icon>
						</view>
					</view>
					<view class="ui-date-list ui-margin">
						<u-text prefixIcon="pause-circle" iconStyle="font-size: 38rpx" size="28rpx" text="结束监控时间">
						</u-text>
						<!-- 	<text></text> -->
						<view class="ui-date active" @click="openDate('endTime')">
							<text>{{editFrom.endTime || '未设置'}}</text>
							<u-icon name="calendar" color="#414141" size="40rpx"></u-icon>
						</view>
					</view>
					<view class="ui-timing">
						<view class="ui-timing-pos">
							<view class="ui-timing-switch">
								<u-switch space="2" v-model="editFrom.inMonitorFlag" activeValue="1" inactiveValue="0"
									activeColor="#85B224" size="20" inactiveColor="rgb(230, 230, 230)">
								</u-switch>
								<text>进入监控区域超时报警时间(分钟)</text>
							</view>
							<view class="ui-timing-switch">
								<u-switch space="2" v-model="editFrom.outMonitorFlag" activeValue="1" inactiveValue="0"
									activeColor="#85B224" size="20" inactiveColor="rgb(230, 230, 230)">
								</u-switch>
								<text>离开监控区域超时报警时间(分钟)</text>
							</view>

						</view>
						<view class="ui-timing-pos">
							<view class="ui-timing-active active">
								<!-- <text>{{editFrom.entryTime || '请选择'}}</text>
								<u-icon name="arrow-down" size="28rpx"></u-icon> -->
								<u-number-box v-model="editFrom.entryTime">
								</u-number-box>
							</view>
							<view class="ui-timing-active active">
								<!-- <text>{{editFrom.departureTime || '请选择'}}</text>
								<u-icon name="arrow-down" size="28rpx"></u-icon> -->
								<u-number-box v-model="editFrom.departureTime">
								</u-number-box>
							</view>
						</view>
					</view>
					<!-- <view class="ui-zone active">
						<text>子区域设置</text>
					</view> -->
					<view class="wd-btn-group ui-btn-group">
						<button class="gray" @click="back">返回</button>
						<button class="green" @click="setZone">子区域设置</button>
						<button class="blue" @click="editSubmit">确定</button>
					</view>
					<u-datetime-picker v-if="dateHandle.show" :show="dateHandle.show" @confirm="dateConfirm"
						@cancel="dateHandle.show = false" :mode="dateHandle.mode"></u-datetime-picker>
				</view>
			</view>
			<!-- /区域设置 -->
		</u-popup>
	</view>

</template>

<script>
	import {
		getHoursTime,
		getMinute
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
				locationList: [
					'壁挂',
					'顶挂',
				],
				// 设备源数据
				source: {}
			}
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
					entryTime: getMinute(entryTime) || 0,
					departureTime: getMinute(departureTime) || 0
				});
				this.close();
			},

			/**
			 * 开启
			 */
			open(form) {
				const {
					departureTime,
					entryTime,
					startTime,
					endTime,
					roomLeft,
					roomHeight,
					roomRight,
					roomLength,
					source
				} = form;
				this.source = source;
				this.editFrom = {
					...form,
					departureTime: getMinute(departureTime),
					entryTime: getMinute(entryTime),
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

			close() {
				this.mode = 'none';
				this.editFrom = {
					...INIT_DEIVCE_SET
				};
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

			/**
			 * 设置子区域
			 */
			setZone() {
				this.$store.commit('setDeviceInfo', this.source);
				uni.navigateTo({
					url: '/pages/equipment/position-setting'
				})
			}
		}
	}
</script>

<style lang="scss">
	.wd-add-edit {
		// box-sizing: ;
		width: 582rpx;
		min-height: 1006rpx;
		border-radius: 20rpx;
		filter: drop-shadow(0 0 5rpx rgba(7, 5, 5, 0.34));
		background-image: linear-gradient(-36deg, #e4e4e4 0%, #f8f8f8 100%);
		padding: 53rpx 31rpx;

		&.ui-change {
			// min-height: 1000rpx;

			.ui-add-box {
				border-bottom: 1px solid #e4e4e4;
			}
		}

		&>view {
			margin-top: 52rpx;

			&.ui-add-box {
				padding: 10rpx 20rpx;

				&>* {
					margin-top: 30rpx;
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
				color: #999;
			}

			&>* {
				:nth-child(1) {
					width: 320rpx;
				}
			}
		}

		.wd-btn-group {
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
		padding: 60rpx 0;

		.wd-btn-group {
			margin-top: 50rpx;
		}

		>view {
			padding: 35rpx;
			box-sizing: border-box;
			display: inline-block;
			width: 608rpx;
			min-height: 645rpx;
			border-radius: 10rpx;
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
			width: 40%;
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

			.ui-timing-switch {
				font-size: 24rpx;
				display: flex;
				flex-direction: row;
				align-items: center;

				text {
					margin-left: 10rpx;
				}

				&:nth-child(1) {
					margin-bottom: 15rpx;
				}

			}

			.ui-timing-pos {
				// width: 50%;

				// &:nth-child(2) {
				// 	width: 200rpx;
				// }

				.ui-timing-active {
					display: flex;
					justify-content: space-evenly;
					align-items: center;
					margin-left: 30rpx;
					font-size: 30rpx;
					line-height: 50rpx;
					height: 50rpx;
					width: 230rpx;
					// border: 1rpx solid #e2e2e2;
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

		.ui-zone {
			margin-top: 15rpx;
			text-align: left;
			color: #2979FF;
			text-decoration: underline;
		}

		.ui-btn-group {
			display: flex;

			button {
				width: 135rpx;
				height: 70rpx;
				border-radius: 35rpx;
				font-size: 28rpx;

				&:nth-child(2) {
					width: 206rpx;
					height: 70rpx;

				}
			}
		}
	}
</style>
