<!--
* @Author: zhanch
* @Date: 2023-02-08 09:32:31
* @FilePath: /components/zone-pop/zone-pop.vue
* @Description: 区域设置
-->

<template>
	<view>
		<u-popup :closeable="true" :round="10" :show="mode === 'add'" mode="center" @close="cencel">
			<view class="wd-add ui-change">
				<u-text prefixIcon="edit-pen" :iconStyle="{ fontSize: '38rpx', color: '#ea942f' }" color="#ea942f"
					size="30rpx" text="新增区域"></u-text>
				<view class="ui-add-box">
					<view>
						<u-text size="28rpx" prefixIcon="home" iconStyle="font-size: 36rpx" text="区域名称"></u-text>
						<u-input placeholder="区域名称" :maxlength="6" v-model="form.name" border="bottom" clearable>
						</u-input>
					</view>
					<view>
						<u-text size="28rpx" prefixIcon="grid" iconStyle="font-size: 36rpx" text="区域类型"></u-text>
						<u-radio-group v-model="form.zoneType" placement="row">
							<u-radio v-for="(item, index) of zongTypeList" :key="index" :customStyle="{margin: '20rpx'}"
								activeColor="#1aa208" :name="item.name" :label="item.label">
							</u-radio>
						</u-radio-group>
					</view>
					<view>
						<u-text prefixIcon="file-text" iconStyle="font-size: 40rpx" text="子区域尺寸" color="#444"
							size="28rpx">
						</u-text>
						<view class="ui-shape">
							<view>
								<text>长</text>
								<view class="ui-input">
									<u-input type="number" v-model="form.height" placeholder="请输入长度" :border="'none'"
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
									<u-input type="number" v-model="form.width" placeholder="请输入宽度" :border="'none'"
										fontSize="28rpx">
										<template slot="suffix">
											<text>米</text>
										</template>
									</u-input>
								</view>
							</view>
						</view>
						<view class="wd-btn-gloup">
							<button @click="more">区域配置</button>
							<button class="green" @click="submit">确定</button>
						</view>
					</view>
				</view>
			</view>
		</u-popup>
		<u-popup :closeable="true" :round="10" :show="mode === 'more'" mode="center" @close="cencel">
			<!-- 区域设置 -->
			<view class="wd-add ui-change">
				<view class="ui-setting">
					<view>
						<u-checkbox-group @change="monitorChange($event, 'existFlag')" placement="column">
							<u-checkbox activeColor="#1aa208" labelSize="28rpx" :checked="form.existFlag == 1"
								:customStyle="{ marginBottom: '8px' }" label="进出监控" name="existFlag"></u-checkbox>
						</u-checkbox-group>
						<u-checkbox-group @change="monitorChange($event, 'fallFlag')" placement="column">
							<u-checkbox activeColor="#1aa208" labelSize="28rpx" :checked="form.fallFlag == 1"
								:cusmonitorChangetomStyle="{ marginBottom: '8px' }" label="跌倒监控" name="fallFlag">
							</u-checkbox>
						</u-checkbox-group>

						<!-- <u-checkbox-group @change="monitorChange($event, 'isAccess')" placement="column">
									<u-checkbox activeColor="#1aa208" :checked="list.isAccess"
										:customStyle="{ marginBottom: '8px' }" label="进出监控" name="isAccess"></u-checkbox>
								</u-checkbox-group> -->

						<view class="ui-date-list">
							<text>开始监控时间</text>
							<view class="ui-date active" @click="openDate('startTime')">
								<text>{{ form.startTime || '未设置' }}</text>
								<u-icon name="calendar" color="#414141" size="40rpx"></u-icon>
							</view>
						</view>
						<view class="ui-date-list ui-margin">
							<text>结束监控时间</text>
							<view class="ui-date active" @click="openDate('endTime')">
								<text>{{ form.endTime || '未设置' }}</text>
								<u-icon name="calendar" color="#414141" size="40rpx"></u-icon>
							</view>
						</view>
						<view class="ui-timing">
							<view class="ui-timing-pos">
								<u-checkbox-group placement="column" @change="monitorChange($event, 'inMonitorFlag')">
									<u-checkbox activeColor="#1aa208" :checked="form.inMonitorFlag == 1"
										:customStyle="{ marginBottom: '40rpx' }" labelSize="28rpx" label="进入监控区域超时报警时间"
										name="inMonitorFlag"></u-checkbox>
								</u-checkbox-group>
								<u-checkbox-group placement="column" @change="monitorChange($event, 'outMonitorFlag')">
									<u-checkbox activeColor="#1aa208" labelSize="28rpx"
										:checked="form.outMonitorFlag == 1" label="离开监控区域超时报警时间" name="outMonitorFlag">
									</u-checkbox>
								</u-checkbox-group>
							</view>
							<view class="ui-timing-pos">
								<view class="ui-timing-active active" @click="openDate('entryTime')">
									<text>{{ form.entryTime || '请选择' }}</text>
									<u-icon name="arrow-down" size="28rpx"></u-icon>
								</view>
								<view class="ui-timing-active active" @click="openDate('departureTime')">
									<text>{{ form.departureTime || '请选择' }}</text>
									<u-icon name="arrow-down" size="28rpx"></u-icon>
								</view>
							</view>
						</view>
						<view class="wd-btn-gloup">
							<button class="gray" @click="back">返回</button>
							<button class="green" @click="submit">确定</button>
						</view>
					</view>
				</view>
			</view>
			<!-- /区域设置 -->
		</u-popup>
		<u-datetime-picker v-if="dateHandle.show" :show="dateHandle.show" @confirm="dateConfirm"
			@cancel="dateHandle.show = false" :mode="dateHandle.mode"></u-datetime-picker>
	</view>

</template>

<script>
	import {
		assignDeep
	} from '../../common/utils/util';
	import {
		ZONE
	} from '../../config/db';
	export default {
		data() {
			return {
				mode: 'none',
				step: 'add',
				form: assignDeep({}, ZONE),
				dateHandle: {
					type: '',
					show: false,
					mode: 'datetime'
				},
				zongTypeList: [{
						name: '0',
						label: '普通区域'
					},
					{
						name: '1',
						label: '屏蔽区域'
					},
					{
						name: '2',
						label: '床'
					}
				]
			}
		},
		methods: {

			/**
			 * 开启
			 */
			open(form = null, mode) {
				if (form) {
					Object.assign(this.form, ZONE, form);
				}
				this.mode = this.step = mode;
			},

			close() {
				this.form = assignDeep({}, ZONE);
				this.mode = 'none';
			},

			/**
			 * 取消修改
			 */
			cencel() {
				this.$emit('cencel');
				this.close();
			},

			/**
			 * 设置更多
			 */
			more() {
				this.mode = 'more';
			},

			back() {
				this.mode = this.step;
			},

			/**
			 * 区域配置
			 */
			monitorChange(event, type) {
				this.form[type] = this.form[type] == 1 ? 0 : 1;
			},

			/**
			 * 提交修改
			 */
			submit() {
				this.$emit('confirm', this.form);
				this.close();
			}
		}
	}
</script>

<style lang="scss">
	.wd-add {
		width: 582rpx;
		max-height: 1006rpx;
		border-radius: 20rpx;
		// filter: drop-shadow(0 0 5rpx rgba(7, 5, 5, 0.34));
		background-image: #fff;
		padding: 53rpx 31rpx;
		overflow-y: scroll;

		&.ui-change {
			// min-height: 1000rpx;

			.ui-add-box {
				border-bottom: 1px solid #e4e4e4;
			}
		}

		&>view {
			margin-top: 10rpx;

			&.ui-add-box {
				padding: 10rpx 20rpx;

				&>* {
					margin-top: 20rpx;
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
			// padding: 35rpx;
			box-sizing: border-box;
			display: inline-block;
			width: 100%;
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
</style>
