<!--
* @Author: zhanch
* @Date: 2023-02-15 19:11:57
* @FilePath: /pages/equipment/add/add-steps.vue
* @Description: 雷达波绑定步骤
-->

<template>
	<app-body :bg="false">
		<view class="ui-add">
			<app-logo text="添加设备" color="#353535"></app-logo>
			<view class="ui-steps">
				<u-steps :current="current" activeColor="#FEAE43" direction="column">
					<u-steps-item v-for="(item, index) of list" :key="index" iconSize="27" :title="item.title"
						:desc="current <= index ? item.desc : item.success" :error="item.error">
						<view class="ui-slot-icon" v-if="current === index && !item.error" slot="icon">
							<u-loading-icon></u-loading-icon>
						</view>
						<u-icon name="checkmark-circle-fill" size="48rpx" slot="icon" v-if="current > index"
							color="#49DE91">
						</u-icon>
					</u-steps-item>
				</u-steps>
			</view>
			<view class="ui-confirm">
				<button :disabled="connectStatic !== 'init'" :class="connectStatic === 'init' ? 'default' : 'cancel'"
					@click="next">开始添加设备</button>
			</view>
		</view>
		<!-- 修改名称 -->
		<u-popup :closeable="true" :round="10" :show="isEditShow" mode="center" @close="eidtClose">
			<view class="wd-add ui-change">
				<u-text prefixIcon="edit-pen" :iconStyle="{ fontSize: '38rpx', color: '#ea942f' }" color="#ea942f"
					size="30rpx" text="设备设置"></u-text>
				<view class="ui-input">
					<u-text size="28rpx" prefixIcon="home" iconStyle="font-size: 36rpx" text="设备名称"></u-text>
					<u-input placeholder="请输入设备名称" :maxlength="6" v-model="editForm.deviceName" border="bottom"
						clearable>
					</u-input>
				</view>
				<view class="ui-radio">
					<u-text size="28rpx" prefixIcon="map" iconStyle="font-size: 36rpx" text="设备位置"></u-text>
					<u-radio-group v-model="editForm.location" placement="row">
						<u-radio :customStyle="{margin: '20rpx'}" v-for="item of locationList" :key="item"
							activeColor="#1aa208" :name="item" :label="item"></u-radio>
					</u-radio-group>
				</view>
				<view class="wd-btn-group"><button @click="add" class="default">确定</button></view>
			</view>
		</u-popup>
		<!-- /修改名称 -->
		<connect-wifi :list="wifiList" ref="wifiRef" @confirm="wifiConfirm"></connect-wifi>
	</app-body>

</template>

<script>
	import {
		PostcreDevice,
		PosteditDevice
	} from '../../../common/http/api';
	/*#ifdef APP-PLUS*/
	import {
		vpActivation
	} from '../../../common/sdk/vp-activation.js';
	/*#endif*/
	
	import {
		arrayAssign,
		assignDeep,
		getCurPage,
		isApp
	} from '../../../common/utils/util';
	const DEFULT_STATUS = [{
			title: '初始化',
			desc: '点击按钮开始添加设备',
			success: '初始化完成',
			error: false
		},
		{
			title: '连接云端',
			desc: '正在等待上一个步骤完成',
			success: '连接云端成功',
			error: false
		},
		{
			title: '连接设备',
			desc: '正在等待上一个步骤完成',
			success: '设备已连接',
			error: false
		},
		{
			title: '激活设备',
			desc: '正在等待上一个步骤完成',
			success: '设备激活完成',
			error: false
		},
		{
			title: '添加成功',
			desc: '正在等待上一个步骤完成',
			success: '设备添加成功',
			error: false
		},
	];
	export default {
		data() {
			return {
				list: arrayAssign(DEFULT_STATUS),
				/**当前步骤**/
				current: -1,
				/**创建设备信息**/
				editForm: {
					deviceName: '',
					location: '壁挂',
					deviceId: ''
				},
				addForm: {
					deviceName: '未命名设备',
					deviceNo: uni.$u.guid(),
					deviceType: '0',
					location: '壁挂'
				},
				/**编辑展示**/
				isEditShow: false,
				/**事件说明**/
				eventMsg: '启动中...',
				/**设备连接状态 init connect success**/
				connectStatic: 'init',
				wifiList: [],
				locationList: [
					'壁挂',
					'顶挂',
				],
			};
		},
		methods: {
			/**
			 * 添加设备
			 */
			async next() {
				// 界面信息重置
				this.connectStatic = 'connect';
				this.current = 0;
				this.list = arrayAssign(DEFULT_STATUS);
				const currentUrl = getCurPage(); // 页面退出不做操作
				vpActivation.init(res => {
					if (!getCurPage().includes(currentUrl)) return;
					const {
						type,
						data
					} = res;
					switch (type) {
						case 'event': // 事件监听
							const {
								msg, code
							} = data;
							this.current = Math.floor(code / 100) - 1;
							this.list[this.current].desc = msg;
							console.log(this.list[this.current].desc, 'this.list[this.current].desc');
							break;
						case 'wifi': // 选择wifi
							console.log(data, 'wifi信息回调');
							this.openWifi(data);
							break;
						case 'success': // 连接成功
							PostcreDevice({ //  先添加设备
								...this.addForm,
								deviceNo: data
							}).then(res => {
								this.current = this.list.length;
								this.editForm.deviceId = res.data.deviceId;
								this.isEditShow = true;
								this.connectStatic = 'init';
							}, err => {
								Object.assign(this.list[this.current], {
									desc: `服务器异常-${err.msg}`,
									error: true
								});
								this.connectStatic = 'init'; // 异常重新配置
							})
							break;
						default:
							console.log('default----', res);
							this.list[this.current].error = true;
							this.list[this.current].desc = data.code + '-' + data.msg;
							uni.showModal({
								title: '设备添加失败，请重试'
							});
							this.connectStatic = 'init';
							break;
					}
				});
			},

			/**
			 * wifi确认
			 */
			wifiConfirm(info) {
				const {
					ssid,
					bssid,
					rssi
				} = info.wifi;
				vpActivation.connect(ssid, info.pwd);
			},

			/**
			 * 打开wifi
			 */
			openWifi(data) {
				this.wifiList = data;
				this.$refs.wifiRef.open();
			},

			/**
			 * 关闭编辑弹窗
			 */
			eidtClose() {
				this.isEditShow = false;
			},

			/**
			 * 添加设备
			 */
			add() {
				if (this.editForm.location && this.editForm.deviceName) {
					PosteditDevice({
						...this.editForm
					}).then(res => {
						uni.showToast({
							icon: 'none',
							title: '添加成功'
						});
						setTimeout(() => {
							uni.navigateTo({
								url: '/pages/myself/device-manage/device-manage'
							});
						}, 1000);

					})
				} else {
					uni.showToast({
						icon: 'none',
						title: '请完整填写信息'
					})
				}

			},
		},
		beforeDestroy() {
			vpActivation.shopSteps(); //  退出时强制停止sdk
		}
	}
</script>

<style lang="scss">
	.ui-add {
		min-height: calc(100vh - var(--status-bar-height) - 95rpx);
		width: 100%;
		background-color: #fff;
	}

	.ui-steps {
		margin: 60rpx 32rpx;
	}

	.ui-slot-icon {
		position: absolute;
		z-index: 2;
		display: flex;
		align-items: center;
		justify-content: center;
		color: #fff;
		height: 88rpx;
		width: 88rpx;
		border-radius: 50% 50%;
		// background: #FEAE43;
	}

	.ui-confirm {
		padding: 34rpx 32rpx 60rpx 32rpx;
	}

	.wd-add {
		width: 582rpx;
		min-height: 500rpx;
		border-radius: 20rpx;
		filter: drop-shadow(0 0 5rpx rgba(7, 5, 5, 0.34));
		background-image: linear-gradient(-36deg, #e4e4e4 0%, #f8f8f8 100%);
		padding: 53rpx 31rpx;

		.ui-radio {
			padding-top: 15rpx;
		}

		&>view {
			margin-top: 52rpx;

			&.ui-input {
				margin-top: 40rpx;
				position: relative;
				padding: 10rpx 20rpx;

				&:nth-child(3) {
					margin-top: 20rpx;
				}

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

		.wd-btn-group {
			text-align: center;
			margin-top: 70rpx;

			button {
				width: 237rpx;
				height: 71rpx;
				line-height: 71rpx;
				font-size: 28rpx;
			}
		}
	}
</style>