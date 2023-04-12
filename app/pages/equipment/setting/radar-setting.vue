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
								:iconStyle="{ fontSize: '36rpx', color: '#3c9cff', marginLeft: '5rpx' }" text="设备位置">
							</u-text>
						</view>
						<u-radio-group v-model="editFrom.installPosition" slot="right-icon" placement="row">
							<u-radio :customStyle="{ margin: '20rpx' }" v-for="item of locationList" :key="item.value"
								activeColor="#FEAE43" :name="item.value" :label="item.text"></u-radio>
						</u-radio-group>
					</u-cell>
					<view class="ui-box" v-if="deviceInfo.roomId">
						<!-- 壁挂 -->
						<template v-if="editFrom.installPosition == 0">
							<view class="ui-slider-box">
								<view class="ui-slider-tit">前距离</view>
								<view class="ui-slider">
									<u-slider min="0" max="4" step="0.1"
										v-model="editFrom.deviceLocationWall.roomLength" activeColor="#eeaa3d"
										blockColor="#eeaa3d" inactiveColor="#c0c4cc" />
									<text>{{ $u.priceFormat(editFrom.deviceLocationWall.roomLength, 2) + '米' }}</text>
								</view>
							</view>
							<view class="ui-slider-box">
								<view class="ui-slider-tit">左距离</view>
								<view class="ui-slider">
									<u-slider min="0" max="4" @changing="levelChanging($event, 'left')" step="0.1"
										v-model="editFrom.deviceLocationWall.roomLeft" activeColor="#eeaa3d"
										blockColor="#eeaa3d" inactiveColor="#c0c4cc" />
									<text>{{ $u.priceFormat(editFrom.deviceLocationWall.roomLeft, 2) + '米' }}</text>
								</view>
							</view>
							<view class="ui-slider-box">
								<view class="ui-slider-tit">右距离</view>
								<view class="ui-slider">
									<u-slider min="0" max="4" step="0.1" @changing="levelChanging($event, 'right')"
										v-model="editFrom.deviceLocationWall.roomRight" activeColor="#eeaa3d"
										blockColor="#eeaa3d" inactiveColor="#c0c4cc" />
									<text>{{ $u.priceFormat(editFrom.deviceLocationWall.roomRight, 2) + '米' }}</text>
								</view>
							</view>
						</template>
						<!-- /壁挂 -->
						<!-- 顶挂 -->
						<template v-if="editFrom.installPosition == 1">
							<view class="ui-slider-box">
								<view class="ui-slider-tit">高度</view>
								<view class="ui-slider">
									<u-slider min="0" max="4" step="0.1" v-model="editFrom.deviceLocationTop.roomHeight"
										activeColor="#eeaa3d" blockColor="#eeaa3d" inactiveColor="#c0c4cc" />
									<text>{{ $u.priceFormat(editFrom.deviceLocationTop.roomHeight, 2) + '米' }}</text>
								</view>
							</view>
							<view class="ui-slider-box">
								<view class="ui-slider-tit">前距离</view>
								<view class="ui-slider">
									<u-slider min="0" max="4" @changing="aroundChanging($event, 'front')" step="0.1"
										v-model="editFrom.deviceLocationTop.roomFront" activeColor="#eeaa3d"
										blockColor="#eeaa3d" inactiveColor="#c0c4cc" />
									<text>{{ $u.priceFormat(editFrom.deviceLocationTop.roomFront, 2) + '米' }}</text>
								</view>
							</view>
							<view class="ui-slider-box">
								<view class="ui-slider-tit">后距离</view>
								<view class="ui-slider">
									<u-slider min="0" max="4" step="0.1" @changing="aroundChanging($event, 'behind')"
										v-model="editFrom.deviceLocationTop.roomBehind" activeColor="#eeaa3d"
										blockColor="#eeaa3d" inactiveColor="#c0c4cc" />
									<text>{{ $u.priceFormat(editFrom.deviceLocationTop.roomBehind, 2) + '米' }}</text>
								</view>
							</view>
							<view class="ui-slider-box">
								<view class="ui-slider-tit">左距离</view>
								<view class="ui-slider">
									<u-slider min="0" max="4" step="0.1" @changing="levelChanging($event, 'left')"
										v-model="editFrom.deviceLocationTop.roomLeft" activeColor="#eeaa3d"
										blockColor="#eeaa3d" inactiveColor="#c0c4cc" />
									<text>{{ $u.priceFormat(editFrom.deviceLocationTop.roomLeft, 2) + '米' }}</text>
								</view>
							</view>
							<view class="ui-slider-box">
								<view class="ui-slider-tit">右距离</view>
								<view class="ui-slider">
									<u-slider min="0" max="4" step="0.1" @changing="levelChanging($event, 'right')"
										v-model="editFrom.deviceLocationTop.roomRight" activeColor="#eeaa3d"
										blockColor="#eeaa3d" inactiveColor="#c0c4cc" />
									<text>{{ $u.priceFormat(editFrom.deviceLocationTop.roomRight, 2) + '米' }}</text>
								</view>
							</view>
						</template>
						<!-- /顶挂 -->
					</view>
					<u-cell v-if="deviceInfo.roomId" isLink @click="setZone" title="隐私区域设置"></u-cell>
				</u-cell-group>
			</view>
		</view>
		<!-- 区域设置 -->
		<template v-if="deviceInfo.roomId && editFrom.deviceWarnParameter.nobodyWarn">
			<view class="ui-form">
				<u-cell-group>
					<u-cell>
						<view class="ui-subtit" slot="title">预警设置</view>
					</u-cell>
					<u-cell title="跌倒预警" :value="editFrom.deviceWarnParameter.fallWarn == '1' ? '已启用' : '已关闭'"></u-cell>
					<u-cell title="离床预警" isLink @click="goBed"></u-cell>
					<u-cell title="无人预警">
						<u-switch space="2" v-model="editFrom.deviceWarnParameter.nobodyWarn.noBody" activeValue="1"
							inactiveValue="0" size="20" slot="right-icon" activeColor="#FEAE43"
							inactiveColor="rgb(230, 230, 230)"></u-switch>
					</u-cell>
					<view class="ui-existFlag" v-if="editFrom.deviceWarnParameter.nobodyWarn.noBody == '1'">
						<u-cell title="连续24小时无人预警">
							<u-switch space="2" v-model="editFrom.deviceWarnParameter.nobodyWarn.noBodyContinue"
								activeValue="1" inactiveValue="0" size="20" slot="right-icon" activeColor="#FEAE43"
								inactiveColor="rgb(230, 230, 230)"></u-switch>
						</u-cell>
						<u-swipe-action
							:key="'swipe-action' + editFrom.deviceWarnParameter.nobodyWarn.warnRules.length">
							<u-swipe-action-item :options="swipe"
								v-for="(item, index) of editFrom.deviceWarnParameter.nobodyWarn.warnRules"
								:key="'wall' + index" @click="actionItem($event, item)">
								<view class="ui-rule">
									<view class="" @click="addWarnRule(item)">
										<text>{{ item.warnRuleName }}</text>
										<u-icon name="arrow-right" color="#909193" size="36rpx"></u-icon>
									</view>
									<view>
										<view class="ui-rule-time" v-if="item.dateType == 0">
											<text>{{ item.startDate }} 至 {{ item.endDate }}</text>
											<text>{{ item.startTime }} 至 {{ item.endTime }}</text>
										</view>
										<text v-else>{{ weekText(item.week) }}</text>
										<u-switch space="2" v-model="item.ruleSwitch" activeValue="1" inactiveValue="0"
											size="20" activeColor="#FEAE43" inactiveColor="rgb(230, 230, 230)">
										</u-switch>
									</view>
								</view>
							</u-swipe-action-item>
						</u-swipe-action>

						<u-cell isLink @click="addWarnRule()" title="新增规则"></u-cell>
					</view>
				</u-cell-group>
			</view>
			<!-- <view class="ui-form">
				<u-cell-group>
					<u-cell isLink @click="setZone" title="子区域设置"></u-cell>
				</u-cell-group>
			</view> -->
		</template>
		<view class="ui-confirm"><button class="default" @click="submit">保存</button></view>
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
		PosteditDevice,
		PostSetRadarWaveDevice,
		PostDelRadarWaveNobodyWarn
	} from '../../../common/http/api';
	import {
		assignDeep,
		getHoursTime,
		wddkAbbreviation
	} from '../../../common/utils/util';
	import {
		mapState
	} from 'vuex';
	export default {
		data() {
			return {
				editFrom: {
					//设备id
					deviceId: '',
					//设备名称
					deviceName: '',
					//设备安装位置: 0 壁挂 1 顶挂
					installPosition: '0',
					// 顶挂配置
					deviceLocationTop: {
						roomBehind: 0,
						roomFront: 0,
						roomHeight: 0,
						roomLeft: 0,
						roomRight: 0
					},
					// 壁挂配置
					deviceLocationWall: {
						roomHeight: 0,
						roomLeft: 0,
						roomLength: 0,
						roomRight: 0
					},
					// 隐私参数
					deviceWarnParameter: {}
				},
				tipShow: false,
				/**时间列表**/
				timeList: [1, 5, 10],
				locationList: [{
					text: '壁挂',
					value: '0'
				}, {
					text: '顶挂',
					value: '1'
				}],
				swipe: [{
					text: '删除',
					style: {
						backgroundColor: '#f56c6c'
					}
				}]
			};
		},
		computed: {
			/**日期信息**/
			weekText() {
				return week => {
					return wddkAbbreviation(week) || '请选择星期';
				};
			},
			...mapState({
				deviceInfo: state => state.deviceInfo
			}),

		},
		onLoad() {
			this.dataInit(this.deviceInfo);
		},
		methods: {
			/**
			 * 添加预警规则
			 */
			addWarnRule(rule) {
				uni.$on('alertRule', ({
					type,
					data
				}) => {
					switch (type) {
						case 'update':
							if (rule) {
								Object.assign(rule, data);
							} else {
								this.editFrom.deviceWarnParameter.nobodyWarn.warnRules.push(data);
							}
							break;
						case 'delete':
							const index = this.editFrom.deviceWarnParameter.nobodyWarn.warnRules.indexOf(rule);
							console.log(index, 'index');
							this.editFrom.deviceWarnParameter.nobodyWarn.warnRules.splice(index, 1);
							break;
						default:
							break;
					}
				});
				this.$setCache('alertRuleCache', {
					deviceId: this.deviceInfo.deviceId,
					...(rule || {})
				});
				uni.navigateTo({
					url: `/pages/equipment/setting/alert-rules`
				});
			},

			/**
			 * 提交
			 **/
			submit() {
				const {
					deviceName,
					roomRight,
					roomLeft
				} = this.editFrom;
				if (!deviceName) return uni.$u.toast('设备名字不能为空');
				if (roomLeft + roomRight > 60) return uni.$u.toast('检测测左右总长不能超过4米');
				PostSetRadarWaveDevice(this.editFrom).then(res => {
					uni.$u.toast(res.msg);
					setTimeout(() => {
						this.$store.dispatch('updateDevacesInfo');
					}, 2000);
				});
			},

			/**
			 * 设置隐私区域
			 */
			setZone() {
				this.setConfirm().then(res => {
					if (res) {
						uni.navigateTo({
							url: '/pages/equipment/setting/privacy-zone'
						});
					}
				})

			},

			/**
			 * 数据处理
			 */
			dataInit(device) {
				console.log(device, 'device');
				const {
					name,
					deviceId,
					parameter: {
						deviceLocationTop = {},
						// 壁挂配置
						deviceLocationWall = {},
						// 隐私参数
						deviceWarnParameter = {},
						installPosition
					} = {}
				} = device;
				Object.assign(
					this.editFrom,
					assignDeep({}, {
						deviceName: name,
						deviceId,
						installPosition,
						deviceLocationTop,
						deviceLocationWall,
						deviceWarnParameter
					})
				);
			},

			/**
			 * 跳转离床预警
			 */
			goBed() {
				this.setConfirm().then(res => {
					if (res) {
						uni.navigateTo({
							url: '/pages/equipment/setting/out-bed'
						});
					}
				})
			},

			setConfirm() {

				return new Promise(resolve => {
					resolve(true);
					// uni.showModal({
					// 	title: '',
					// 	content: '请确认当前页面信息已保存',
					// 	success: res => {
					// 		console.log(res.confirm, 'res.confirm');
					// 		resolve(res.confirm);
					// 	}
					// });
				})

			},

			/**
			 * 水平距离移动
			 */
			levelChanging(num, type) {
				uni.$u.throttle(() => {
					const location = this.editFrom.installPosition === '0' ? this.editFrom.deviceLocationWall :
						this
						.editFrom
						.deviceLocationTop;
					if (type == 'left') { // 壁挂
						if (num + location.roomRight < 4) return;
						location.roomRight = 4 - num;
					} else {
						if (num + location.roomLeft < 4) return;
						location.roomLeft = 4 - num;
					}
				}, 100, false);

			},

			/**
			 * 前后距离移动
			 */
			aroundChanging(num, type) {
				uni.$u.throttle(() => {
					const location = this.editFrom.deviceLocationTop;
					if (type == 'front') { // 钱距离
						if (num + location.roomBehind < 4) return;
						location.roomBehind = 4 - num;
					} else {
						if (num + location.roomFront < 4) return;
						location.roomFront = 4 - num;
					}
				}, 100, false);
			},

			/**
			 * 	无人预警规则删除 
			 */
			actionItem($event, rule) {
				console.log(rule, 'rule');
				const {
					ruleNo
				} = rule;
				PostDelRadarWaveNobodyWarn({
					deviceId: this.deviceInfo.deviceId,
					ruleNo
				}).then(res => {
					const index = this.editFrom.deviceWarnParameter.nobodyWarn.warnRules.indexOf(rule);
					this.editFrom.deviceWarnParameter.nobodyWarn.warnRules.splice(index, 1);
					uni.$u.toast('删除成功');
					setTimeout(() => {
						this.$store.dispatch('updateDevacesInfo'); // 更新设备数据
					}, 2000);


				});
			}
		}
	};
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

	.ui-slider-box {
		display: flex;
		justify-content: space-between;
		align-items: center;
		font-size: 15px;
		padding: 25rpx 20rpx 25rpx 0;
		border-bottom: 0.5px solid rgb(214, 215, 217);
	}

	.ui-slider-tit {
		min-width: 100rpx;
	}

	.ui-slider {
		display: flex;
		flex-direction: row;
		align-items: center;
		justify-content: space-between;
		padding: 0 32rpx 0 0;
		box-sizing: border-box;

		&>* {
			:nth-child(1) {
				width: 400rpx;
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
		border-bottom: 0.5px solid rgb(214, 215, 217);

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

		.ui-rule-time {
			display: flex;
			flex-direction: column;

			>text {
				&:nth-child(2) {
					margin-top: 10rpx;
				}
			}
		}
	}
</style>
