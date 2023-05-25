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
				<u-text size="36rpx" align="center" text="新增区域"></u-text>
				<view class="ui-add-box">
					<view>
						<u-text size="30rpx" text="区域名称"></u-text>
						<u-input placeholder="请输入区域名称" class="wd-input" :maxlength="6" v-model="form.shadowZoneName"
							clearable></u-input>
					</view>
					<view>
						<u-text size="30rpx" text="物体离地高度"></u-text>
						<u-input placeholder="请输入物体离地高度" class="wd-input" :maxlength="6" v-model="form.above" clearable>
							<template slot="suffix">
								<text>米</text>
							</template>
						</u-input>
					</view>
					<view>
						<u-text size="30rpx" text="物体高度"></u-text>
						<u-input placeholder="请输入物体高度" class="wd-input" :maxlength="6" v-model="form.stature" clearable>
							<template slot="suffix">
								<text>米</text>
							</template>
						</u-input>
					</view>

					<view class="wd-size">
						<u-text text="长×宽" color="#444" size="30rpx"></u-text>
						<view class="ui-shape">
							<view>
								<text>长</text>
								<view class="ui-input">
									<u-input type="number" v-model="form.width" placeholder="请输入" fontSize="28rpx">
										<template slot="suffix">
											<text>米</text>
										</template>
									</u-input>
								</view>
							</view>
							<view>
								<text>宽</text>
								<view class="ui-input">
									<u-input type="number" v-model="form.height" placeholder="请输入" fontSize="28rpx">
										<template slot="suffix">
											<text>米</text>
										</template>
									</u-input>
								</view>
							</view>

						</view>
					</view>
					<view class="wd-btn-group">
						<button class="cancel" @click="cancel">取消</button>
						<button class="default" @click="submit">确定</button>
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
				form: {
					//必传房间id
					roomId: '',
					//必传设备id
					deviceId: '',
					//必传区域名称
					shadowZoneName: '',
					height: '',
					width: '',
					// 离地高度
					above: '',
					// 物体高度
					stature: ''
				},
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
			};
		},
		methods: {
			/**
			 * 开启
			 */
			open(form = null, mode) {
				if (form) {
					Object.assign(this.form, form);
				}
				this.mode = mode;
			},

			close() {
				this.form = assignDeep({}, {
					//必传房间id
					roomId: '',
					//必传设备id
					deviceId: '',
					//必传区域名称
					shadowZoneName: '',
					height: '',
					width: '',
					// 离地高度
					above: '',
					// 物体高度
					stature: ''
				});
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
			 * 提交修改
			 */
			submit() {
				if (!this.form.shadowZoneName) return uni.$u.toast('区域名称不能为空');
				if (this.form.width < 0 || this.form.height < 0) return uni.$u.toast('宽高不能小于0');
				this.$emit('confirm', assignDeep({}, this.form));
				this.close();
			}
		}
	};
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

	.wd-input {
		margin: 20rpx 0;
	}

	.wd-size {
		margin-top: 20rpx;
	}

	.ui-shape {
		display: flex;
		flex-direction: row;
		justify-content: space-between;
		width: 100%;
		margin: 20rpx 0 60rpx 0;
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
