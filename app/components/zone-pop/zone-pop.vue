<!--
* @Author: zhanch
* @Date: 2023-02-08 09:32:31
* @FilePath: /components/zone-pop/zone-pop.vue
* @Description: 区域设置
-->

<template>
	<view>
		<u-popup :closeable="true" :round="10" :show="mode === 'add'" mode="center" @close="cancel">
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
							<button class="gray" @click="cancel">取消</button>
							<button class="green" @click="submit">确定</button>
						</view>
					</view>
				</view>
			</view>
		</u-popup>

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
				zongTypeList: [
					// {
					// 	name: '0',
					// 	label: '普通区域'
					// },
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
			cancel() {
				this.$emit('cancel');
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
			 * 提交修改
			 */
			submit() {
				if (!this.form.name) return uni.$u.toast('区域名称不能为空');
				if (this.form.width < 0 || this.form.height < 0) return uni.$u.toast('宽高不能小于0');
				let obj = assignDeep({}, this.form);
				switch (obj.zoneType) {
					case '1': // // 隐私
						Object.assign(obj, {
							existFlag: 1 // 私人区域不做监控
						})
						break;
					case '2': // 床

						break;
					default:
						break;
				}
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

			// .ui-add-box {
			// 	border-bottom: 1px solid #e4e4e4;
			// }
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
