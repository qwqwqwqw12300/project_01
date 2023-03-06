<!--
* @Author: zhanch
* @Date: 2023-03-05 17:31:04
* @FilePath: /components/bed-pop/bed-pop.vue
* @Description: 离床设置弹窗
-->

<template>
	<view>
		<u-popup :closeable="true" :round="10" :show="mode === 'add'" mode="center" @close="cancel">
			<view class="wd-add ui-change">
				<u-text size="36rpx" align="center" text="新增区域"></u-text>
				<view class="ui-add-box">
					<view>
						<u-text size="30rpx" text="区域名称"></u-text>
						<u-input placeholder="请输入区域名称" class="wd-input" :maxlength="6" v-model="form.bedName" clearable></u-input>
					</view>
					<view class="wd-size">
						<u-text text="宽×高" color="#444" size="30rpx"></u-text>
						<view class="ui-shape">
							<view>
								<text>长</text>
								<view class="ui-input">
									<u-input type="number" v-model="form.height" placeholder="请输入长度" fontSize="28rpx">
										<template slot="suffix">
											<text>米</text>
										</template>
									</u-input>
								</view>
							</view>
							<view>
								<text>宽</text>
								<view class="ui-input">
									<u-input type="number" v-model="form.width" placeholder="请输入宽度" fontSize="28rpx">
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
import { assignDeep } from '../../common/utils/util';
const DEFAULT_BED = {
	leaveBedWarnParameter: {
		leaveBedInterval: '1',
		intervalTime: '300',
		setRuleDate: {
			//日期类型 ： 0 按日期  1 按星期
			dateType: '1',
			//星期 ： 日期类型为 1时使用
			week: [0, 1, 2, 3, 4, 5, 6],
			//开始日期 ：日期类型为 0时使用
			startDate: '',
			//结束日期 ：日期类型为 0时使用
			endDate: '',
			//开始时间：必传
			startTime: '23:00',
			//结束时间：必传
			endTime: '7:00'
		}
	}
};
export default {
	data() {
		return {
			mode: 'none',
			form: {
				deviceId: '',
				roomZoneId: '',
				roomId: '',
				bedName: '',
				width: '',
				height: '',
				...assignDeep({}, DEFAULT_BED)
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
			this.form = assignDeep(
				{},
				{
					deviceId: '',
					roomZoneId: '',
					roomId: '',
					bedName: '',
					width: '',
					height: '',
					...assignDeep({}, DEFAULT_BED)
				}
			);
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
			if (!this.form.bedName) return uni.$u.toast('区域名称不能为空');
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

	& > view {
		margin-top: 10rpx;

		&.ui-add-box {
			padding: 10rpx 20rpx;

			& > * {
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

		& > * {
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

	> view {
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
