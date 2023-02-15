<!--
* @Author: zhanch
* @Date: 2023-02-07 09:20:38
* @FilePath: /pages/equipment/radar-setting/radar-setting.vue
* @Description: 雷达波设置
-->

<template>
	<app-body :bg="false">
		<view class="ui-form">
			<app-logo text="设置雷达波设备" color="#353535"></app-logo>
			<view class="ui-edit">
				<u-cell-group>
					<u-cell title="设备名称">
						<u-input inputAlign="right" placeholder="请输入设备名称" slot="right-icon"
							v-model="editFrom.deviceName"></u-input>
					</u-cell>
					<u-cell title="设备位置">
						<u-radio-group v-model="editFrom.location" slot="right-icon" placement="row">
							<u-radio :customStyle="{margin: '20rpx'}" v-for="item of locationList" :key="item"
								activeColor="#FEAE43" :name="item" :label="item"></u-radio>
						</u-radio-group>
					</u-cell>
					<u-cell isLink title="检测高度" :value="$u.priceFormat(editFrom.roomHeight/10, 2) + '米'"></u-cell>
					<u-cell isLink title="检测长度" :value="$u.priceFormat(editFrom.roomLength/10, 2) + '米'"></u-cell>
					<u-cell isLink title="检测左长度" :value="$u.priceFormat(editFrom.roomRight/10, 2) + '米'"></u-cell>
					<u-cell isLink title="检测右长度" :value="$u.priceFormat(editFrom.roomLeft/10, 2) + '米'"></u-cell>
				</u-cell-group>
			</view>
		</view>
		<view class="ui-form">
			<u-cell-group>
				<u-cell>
					<view class="ui-subtit" slot="title">
						监控设置
					</view>
				</u-cell>
				<u-cell title="进出监控">
					<u-switch space="2" v-model="editFrom.existFlag" activeValue="0" inactiveValue="1" size="20"
						slot="right-icon" activeColor="#FEAE43" inactiveColor="rgb(230, 230, 230)">
					</u-switch>
				</u-cell>
				<view class="ui-existFlag" v-if="editFrom.existFlag == 0">
					<u-cell isLink title="监控开始时间" @click="openDate('startTime')" :value="editFrom.startTime || '未设置'">
					</u-cell>
					<u-cell isLink title="监控结束时间" @click="openDate('endTime')" :value="editFrom.endTime || '未设置'">
					</u-cell>
					<u-cell title="进入监控区域超时报警时间">
						<u-switch space="2" v-model="editFrom.inMonitorFlag" activeValue="0" inactiveValue="1" size="20"
							slot="right-icon" activeColor="#FEAE43" inactiveColor="rgb(230, 230, 230)">
						</u-switch>
					</u-cell>
					<template v-if="editFrom.inMonitorFlag == 0">
						<u-cell>
							<view slot="title" class="ui-slot-title">
								<view class="ui-date-list">
									<u-button :key="item + 'time'" v-for="item of timeList"
										@click="onTimeBtn(item, 'entryTime')" type="primary"
										:plain="!(editFrom.entryTime === item)" size="mini" :text="item + '分钟'">
									</u-button>
								</view>
							</view>
							<view slot="right-icon">
								<u-checkbox-group shape="circle" placement="column"
									v-model="editFrom.isDepartureTimeCus">
									<u-checkbox activeColor="#FEAE43" label="自定义时间" :name="true">
									</u-checkbox>
								</u-checkbox-group>
							</view>
						</u-cell>
						<u-cell v-if="editFrom.isDepartureTimeCus[0]">
							<view slot="title" class="ui-sub-title">
								<text>选择自定义时间</text>
							</view>
							<view slot="right-icon">
								<u-number-box v-model="editFrom.entryTime"></u-number-box>
							</view>
						</u-cell>
					</template>

					<u-cell title="离开监控区域超时报警时间">
						<u-switch space="2" v-model="editFrom.outMonitorFlag" activeValue="0" inactiveValue="1"
							size="20" slot="right-icon" activeColor="#FEAE43" inactiveColor="rgb(230, 230, 230)">
						</u-switch>
					</u-cell>
					<template v-if="editFrom.outMonitorFlag == 0">
						<u-cell>
							<view slot="title" class="ui-slot-title">
								<view class="ui-date-list">
									<u-button :key="item + 'departureTime'" v-for="item of timeList"
										@click="onTimeBtn(item, 'departureTime')" type="primary"
										:plain="!(editFrom.departureTime === item)" size="mini" :text="item + '分钟'">
									</u-button>
								</view>
							</view>
							<view slot="right-icon">
								<u-checkbox-group shape="circle" placement="column" v-model="editFrom.isEntryTimeCus">
									<u-checkbox activeColor="#FEAE43" label="自定义时间" :name="true">
									</u-checkbox>
								</u-checkbox-group>
							</view>
						</u-cell>
						<u-cell v-if="editFrom.isEntryTimeCus[0]">
							<view slot="title" class="ui-sub-title">
								<text>选择自定义时间</text>
							</view>
							<view slot="right-icon">
								<u-number-box v-model="editFrom.departureTime"></u-number-box>
							</view>
						</u-cell>
					</template>
				</view>
			</u-cell-group>
		</view>
		<view class="ui-form">
			<u-cell-group>
				<u-cell isLink @click="setZone" title="子区域设置"></u-cell>
			</u-cell-group>
		</view>
		<view class="ui-confirm">
			<button class="default" @click="editSubmit">保存</button>
		</view>
		<u-datetime-picker v-if="dateHandle.show" :show="dateHandle.show" @confirm="dateConfirm"
			@cancel="dateHandle.show = false" :mode="dateHandle.mode"></u-datetime-picker>
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
				editFrom: {
					...INIT_DEIVCE_SET,

				},
				dateHandle: {
					type: '',
					show: false,
					mode: 'time'
				},
				/**时间列表**/
				timeList: [1, 5, 10],
				locationList: [
					'壁挂',
					'顶挂',
				],
				// 设备源数据
				source: {}
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
				if (!editFrom.deviceName) return uni.$u.toast('请填写新名称');
				if (roomLeft + roomRight > 60) return uni.$u.toast('检测测左右总长不能超过6米');
				setDevice({
					...this.editFrom,
					roomLeft: roomLeft / 10,
					roomHeight: roomHeight / 10,
					roomRight: roomRight / 10,
					roomLength: roomLength / 10,
					startTime: getHoursTime(startTime) || 0,
					endTime: getHoursTime(endTime) || 0,
					entryTime: entryTime * 60 || 0,
					departureTime: departureTime * 60 || 0,
					flag: '2'
				}).then(res => {
					uni.$u.toast(res.msg);
				});
				this.$emit('confirm', );
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
					url: '/pages/equipment/position-setting'
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
</style>
