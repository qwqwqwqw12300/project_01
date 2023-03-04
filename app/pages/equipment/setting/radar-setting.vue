<!--
* @Author: zhanch
* @Date: 2023-02-07 09:20:38
* @FilePath: /pages/equipment/radar-setting/radar-setting.vue
* @Description: 雷达波设置
-->

<template>
	<app-body :bg="false">
		<view class="ui-form">
			<app-logo text="设置跌倒检测器" color="#353535"></app-logo>
			<view class="ui-edit">
				<u-cell-group>
					<u-cell title="设备名称">
						<u-input inputAlign="right" placeholder="请输入设备名称" border="none" slot="right-icon"
							v-model="editFrom.deviceName"></u-input>
					</u-cell>
					<u-cell>
						<view slot="title">
							<u-text @click="tipShow = true" suffixIcon="question-circle"
								:iconStyle="{fontSize: '36rpx', color: '#3c9cff', marginLeft: '5rpx'}" text="设备位置">
							</u-text>
						</view>
						<u-radio-group v-model="editFrom.location" slot="right-icon" placement="row">
							<u-radio :customStyle="{margin: '20rpx'}" v-for="item of locationList" :key="item"
								activeColor="#FEAE43" :name="item" :label="item"></u-radio>
						</u-radio-group>
					</u-cell>
					<view class="ui-box" v-if="editFrom.roomId">
						<view class="ui-slider-box">
							<view class="ui-slider-tit">检测高度</view>
							<view class="ui-slider">
								<u-slider min="10" max="40" v-model="editFrom.roomHeight" activeColor="#eeaa3d"
									blockColor="#eeaa3d" inactiveColor="#c0c4cc" />
								<text>{{$u.priceFormat(editFrom.roomHeight/10, 2) + '米'}}</text>
							</view>
						</view>
						<view class="ui-slider-box">
							<view class="ui-slider-tit">检测前距离</view>
							<view class="ui-slider">
								<u-slider min="10" max="40" v-model="editFrom.roomLength" activeColor="#eeaa3d"
									blockColor="#eeaa3d" inactiveColor="#c0c4cc" />
								<text>{{$u.priceFormat(editFrom.roomLength/10, 2) + '米'}}</text>
							</view>
						</view>
						<view class="ui-slider-box">
							<view class="ui-slider-tit">检测左距离</view>
							<view class="ui-slider">
								<u-slider min="10" max="40" v-model="editFrom.roomLeft" activeColor="#eeaa3d"
									blockColor="#eeaa3d" inactiveColor="#c0c4cc" />
								<text>{{$u.priceFormat(editFrom.roomLeft/10, 2) + '米'}}</text>
							</view>
						</view>
						<view class="ui-slider-box">
							<view class="ui-slider-tit">检测右长度</view>
							<view class="ui-slider">
								<u-slider min="10" max="40" v-model="editFrom.roomRight" activeColor="#eeaa3d"
									blockColor="#eeaa3d" inactiveColor="#c0c4cc" />
								<text>{{$u.priceFormat(editFrom.roomRight/10, 2) + '米'}}</text>
							</view>
						</view>
						<!-- <u-cell isLink title="检测高度" @click="sliderSet('roomHeight')"
							:value="$u.priceFormat(editFrom.roomHeight/10, 2) + '米'"></u-cell>
						<u-cell isLink title="检测长度" @click="sliderSet('roomLength')"
							:value="$u.priceFormat(editFrom.roomLength/10, 2) + '米'"></u-cell>
						<u-cell isLink title="检测左长度" @click="sliderSet('roomLeft')"
							:value="$u.priceFormat(editFrom.roomLeft/10, 2) + '米'"></u-cell>
						<u-cell isLink title="检测右长度" @click="sliderSet('roomRight')"
							:value="$u.priceFormat(editFrom.roomRight/10, 2) + '米'"></u-cell> -->
					</view>
					<u-cell isLink @click="setZone" title="隐私区域设置"></u-cell>
				</u-cell-group>
			</view>
		</view>
		<!-- 区域设置 -->
		<template v-if="editFrom.roomId">
			<view class="ui-form">
				<u-cell-group>
					<u-cell>
						<view class="ui-subtit" slot="title">
							预警设置
						</view>
					</u-cell>
					<u-cell title="进出监控" value="已启用"></u-cell>
					<u-cell title="离床预警" isLink @click="goBed"></u-cell>
					<u-cell title="无人预警">
						<u-switch space="2" v-model="editFrom.existFlag" activeValue="0" inactiveValue="1" size="20"
							slot="right-icon" activeColor="#FEAE43" inactiveColor="rgb(230, 230, 230)">
						</u-switch>
					</u-cell>
					<view class="ui-existFlag" v-if="editFrom.existFlag == 0">
						<u-cell title="连续24小时无人预警">
							<u-switch space="2" v-model="editFrom.inMonitorFlag" activeValue="0" inactiveValue="1"
								size="20" slot="right-icon" activeColor="#FEAE43" inactiveColor="rgb(230, 230, 230)">
							</u-switch>
						</u-cell>
						<view class="ui-rule">
							<view class="">
								<text>我的规则2</text>
								<u-icon name="arrow-right" color="#909193" size="36rpx"></u-icon>
							</view>
							<view>
								<text>2023/02/21 至 2023/02/23 12:00 至 23:59</text>
								<u-switch space="2" v-model="editFrom.inMonitorFlag" activeValue="0" inactiveValue="1"
									size="20" activeColor="#FEAE43" inactiveColor="rgb(230, 230, 230)">
								</u-switch>
							</view>

						</view>
						<!-- 	<u-cell title="我的规则2" label="2023/02/21 至 2023/02/23  12:00 至 23:59">
							<view slot="right-icon" class="ui-right-icon">
								<view>
									<u-icon name="arrow-right" color="#909193" size="28"></u-icon>
								</view>
							</view>
						</u-cell> -->
					</view>
				</u-cell-group>
			</view>
			<view class="ui-form">
				<u-cell-group>
					<u-cell isLink @click="setZone" title="子区域设置"></u-cell>
				</u-cell-group>
			</view>
		</template>
		<view class="ui-confirm">
			<button class="default" @click="submit">保存</button>
		</view>
		<u-datetime-picker v-if="dateHandle.show" :show="dateHandle.show" @confirm="dateConfirm"
			@cancel="dateHandle.show = false" :mode="dateHandle.mode"></u-datetime-picker>
		<u-modal title="区域调节" showCancelButton :show="rangeHandle.show" @cancel="rangeHandle.show = false"
			@confirm="modalConfirm">
			<view class="ui-slider">
				<u-slider min="10" :max="rangeHandle.type === 'roomHeight' ? 40 : 60" v-model="rangeHandle.num"
					activeColor="#eeaa3d" blockColor="#eeaa3d" inactiveColor="#c0c4cc" />
				<text>{{$u.priceFormat(rangeHandle.num/10, 2)}}米</text>
			</view>
		</u-modal>
		<u-popup :show="tipShow" @close="tipShow = false" mode="center" round="16rpx" closeable>
			<view class="ui-tip-box">
				<view class="ui-tip-title">设备安装帮助</view>
				<view class="ui-tip-img">
					<text>顶挂</text>
					<image src="@/static/images/setting/device-tip1.png" mode=""></image>
				</view>
				<view class="ui-tip-img">
					<text>壁挂</text>
					<image src="@/static/images/setting/device-tip2.png" mode=""></image>
				</view>
			</view>
		</u-popup>
	</app-body>
</template>

<script>
	import {
		setDevice,
		PosteditDevice
	} from '../../../common/http/api';
	import {
		getHoursTime
	} from '../../../common/utils/util';
	import {
		INIT_DEIVCE_SET
	} from '../../../config/db';
	export default {
		data() {
			return {
				editFrom: {
					...INIT_DEIVCE_SET,

				},
				/**时间设置**/
				dateHandle: {
					type: '',
					show: false,
					mode: 'time'
				},
				/**范围设置**/
				rangeHandle: {
					type: '',
					show: false,
					num: 0
				},
				tipShow: false,
				/**时间列表**/
				timeList: [1, 5, 10],
				locationList: [
					'壁挂',
					'顶挂'
				],
				// 设备源数据
				source: {}
			};
		},
		onLoad() {
			const ceche = this.$getCache('setDevice') || {};
			const device = this.dataInit(ceche);
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
			} = device;
			this.editFrom = {
				...device,
				departureTime: departureTime / 60 || 0,
				entryTime: entryTime / 60 || 0,
				/**开始监控时间**/
				startTime: uni.$u.timeFormat(startTime, 'hh:MM'),
				/**结束监控时间**/
				endTime: uni.$u.timeFormat(endTime, 'hh:MM'),
				roomLeft: roomLeft * 10,
				roomHeight: roomHeight * 10,
				roomRight: roomRight * 10,
				roomLength: roomLength * 10,
				isDepartureTimeCus: [this.timeList.includes(departureTime /
					60)],
				isEntryTimeCus: [this.timeList.includes(entryTime / 60)],
			};
			this.source = source;
		},
		methods: {

			/**
			 * 提交 
			 **/
			submit() {
				if (this.editFrom.roomId) {
					this.editSubmit();
				} else {
					this.unBindSubmit();
				}
			},

			/**
			 * 已绑定设备修改
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
					roomLength,
					deviceName
				} = this.editFrom;
				if (!deviceName) return uni.$u.toast('设备名字不能为空');
				if (roomLeft + roomRight > 60) return uni.$u.toast('检测测左右总长不能超过6米');
				setDevice({
					...this.editFrom,
					roomLeft: roomLeft / 10,
					roomHeight: roomHeight / 10,
					roomRight: roomRight / 10,
					roomLength: roomLength / 10,
					startTime: startTime || '00：00',
					endTime: endTime || '23：59',
					entryTime: entryTime * 60 || 0,
					departureTime: departureTime * 60 || 0,
					flag: '2',
					source: null
				}).then(res => {
					uni.$u.toast(res.msg);
					setTimeout(() => {
						this.$store.dispatch('updateDevacesInfo');
					}, 2000)
				});
			},

			/**
			 * 未绑定设备修改
			 */
			unBindSubmit() {
				if (!this.editFrom.deviceName) return uni.$u.toast('设备名字不能为空');
				PosteditDevice({
					deviceName: this.editFrom.deviceName,
					location: this.editFrom.location,
					deviceId: this.editFrom.deviceId
				}).then(res => {
					uni.$u.toast('修改成功');
					setTimeout(() => {
						this.$store.dispatch('updateDevacesInfo');
					}, 2000);
				})
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
			 * 选择时间
			 */
			onTimeBtn(time, type) {
				this.editFrom[type] = time;
			},

			/**
			 * 设置子区域
			 */
			setZone() {
				this.$store.commit('setDeviceInfo', this.source);
				uni.navigateTo({
					url: '/pages/equipment/setting/position-setting'
				})
			},
			/**
			 * 设置区域
			 */
			sliderSet(type) {
				Object.assign(this.rangeHandle, {
					type,
					num: this.editFrom[type],
					show: true
				});
			},

			/**
			 * 设置区域确认
			 */
			modalConfirm() {
				console.log('modalConfirm');
				const {
					type,
					num
				} = this.rangeHandle;
				this.editFrom[type] = num;
				this.rangeHandle.show = false;
			},

			/**
			 * 数据处理
			 */
			dataInit(device) {
				console.log(device, 'device');
				const {
					name,
					deviceId,
					type,
					no,
					familyId,
					roomId,
					location,
					parameter: {
						deviceLocation = {},
						deviceRoomParameter = {}
					} = {},
				} = device,
				initObj = {
					deviceId: '',
					deviceName: '',
					deviceType: '',
					deviceNo: '',
					roomLeft: 6,
					roomHeight: 4,
					roomRight: 3,
					roomLength: 3
				};
				Object.assign(initObj, {
					deviceName: name,
					deviceId,
					deviceType: type,
					deviceNo: no,
					familyId,
					roomId,
					location,
					...deviceLocation,
					...deviceRoomParameter,
					source: device
				});
				console.log(initObj, 'initObj');
				return initObj;
			},

			/**
			 * 跳转离床预警
			 */
			goBed() {
				console.log(121);
				this.$store.commit('setDeviceInfo', this.source);
				uni.navigateTo({
					url: '/pages/equipment/setting/out-bed'
				})
			}

		}
	}
</script>

<style lang="scss">
	.ui-form {
		background: #fff;

		&:nth-child(3) {
			margin-top: 20rpx;
		}

		&:nth-child(4) {
			margin-top: 20rpx;
		}

		.ui-existFlag {
			padding-left: 32rpx;
		}

		.ui-edit {
			margin-top: 110rpx;
		}

		.ui-subtit {
			font-size: 36rpx;
			color: $u-primary;
			padding-left: 10rpx;
			border-left: 8rpx solid $u-primary;
		}

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
			// padding-left: 32rpx;
		}

		.ui-setting-btn {
			margin-top: 40rpx;

			button {
				width: 320rpx;
				border-radius: 44rpx;
			}
		}
	}

	.ui-confirm {
		padding: 54rpx 32rpx 80rpx 32rpx;
	}

	.ui-slider-tit {
		padding: 20rpx 0;
	}

	.ui-slider {
		width: 100%;
		display: flex;
		flex-direction: row;
		align-items: center;
		justify-content: space-between;
		padding: 0 32rpx 0 0;
		box-sizing: border-box;

		&>* {
			:nth-child(1) {
				width: 430rpx;
			}
		}
	}

	.ui-tip-box {
		font-size: 32rpx;
		width: 660rpx;
		box-sizing: border-box;
		padding: 30rpx;
		text-align: center;

		.ui-tip-title {
			width: 100%;
			color: #353535;
			font-weight: bold;
			margin-bottom: 57rpx;
			box-sizing: border-box;
		}

		.ui-tip-img {
			margin: 16rpx;
			text-align: center;
			display: flex;
			flex-direction: column;
			align-items: center;
			justify-content: center;

			&:nth-child(3) {
				margin-top: 30rpx;
			}

			text {
				margin: 16rpx 0;
				display: inline-block;
			}

			image {
				height: 443rpx;
				width: 443rpx;
			}
		}
	}

	.ui-box {
		padding-left: 30rpx;
	}

	.ui-slider-box {
		font-size: 15px;
		padding: 27rpx 20rpx 43rpx 0;
		border-bottom: 2px solid #f2f2f2;
	}

	.ui-right-icon {
		width: 300rpx;
		height: 100%;
		text-align: right;

		>view {
			text-align: right;
			display: inline-block;
			// width: 200rpx;
		}
	}

	.ui-rule {
		display: flex;
		flex-direction: column;
		width: 100%;
		box-sizing: border-box;
		padding: 47rpx 32rpx;
		min-height: 80px;
		border-bottom: 2px solid #f2f2f2;

		>view {
			display: flex;
			flex-direction: row;
			align-items: center;
			justify-content: space-between;

			&:nth-child(1) {
				font-size: 34rpx;
				color: #353535;
				margin-bottom: 36rpx;
			}

			&:nth-child(2) {
				font-size: 26rpx;
				color: #888888;

				text {
					width: 400rpx;
				}
			}
		}
	}
</style>
