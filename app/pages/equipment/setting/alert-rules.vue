<!--
* @Author: zhanch
* @Date: 2023-02-28 23:34:54
* @FilePath: /pages/equipment/setting/alert-rules/alert-rules.vue
* @Description: 预警规则
-->

<template>
	<app-body :bg="false">
		<view class="ui-body">
			<app-logo text="新建无人预警规则"></app-logo>
			<view class="ui-edit">
				<u-cell-group>
					<u-cell title="规则名称"><u-input v-model="form.warnRuleName" inputAlign="right" placeholder="请输入名称" border="none" slot="right-icon"></u-input></u-cell>
					<u-cell title="选择时间格式">
						<view slot="right-icon">
							<u-radio-group placement="row" v-model="form.dateType">
								<u-radio activeColor="#FEAE43" :customStyle="{ marginRight: '32rpx' }" label="按日期" name="0"></u-radio>
								<u-radio activeColor="#FEAE43" label="按星期" name="1"></u-radio>
							</u-radio-group>
						</view>
					</u-cell>
					<u-cell
						title="日期"
						v-if="form.dateType == 0"
						isLink
						@click="calendarShow = true"
						:value="form.startDate && form.endDate ? form.startDate + '至' + form.endDate : '请选择日期'"
					></u-cell>
					<u-cell title="星期" v-else isLink :value="weekText" @click="selectWeek"></u-cell>
					<u-cell title="时间" @click="timeShow = true" isLink :value="form.startTime && form.endTime ? form.startTime + '至' + form.endTime : '请选择时间'"></u-cell>
				</u-cell-group>
			</view>
			<view class="ui-btn-group">
				<view @click="deleteRule">删除规则</view>
				<view @click="save">保存</view>
			</view>
		</view>
		<u-calendar :show="calendarShow" mode="range" @confirm="calendarConfirm" @close="calendarShow = false"></u-calendar>
		<smh-time-range :isUnder="timeShow" @confrim="confrim" @cancel="timeShow = false"></smh-time-range>
	</app-body>
</template>

<script>
import { PostDelRadarWaveNobodyWarn, PostSetRadarWaveNobodyWarn } from '../../../common/http/api';
import { wddkAbbreviation } from '../../../common/utils/util';
import { WEEKMAP } from '../../../config/db';
export default {
	data() {
		return {
			form: {
				deviceId: '',
				//规则名称
				warnRuleName: '',
				ruleNo: '',
				//日期类型 ： 0 按日期  1 按星期
				dateType: '0',
				//星期 ： 日期类型为 1时使用
				week: [],
				//开始日期 ：日期类型为 0时使用
				startDate: '',
				//结束日期 ：日期类型为 0时使用
				endDate: '',
				//开始时间：必传
				startTime: '',
				//结束时间：必传
				endTime: ''
			},
			calendarShow: false,
			timeShow: false
		};
	},
	onLoad(option) {
		const cache = this.$getCache('alertRuleCache') || {};
		Object.assign(this.form, cache || {});
	},
	computed: {
		/**日期信息**/
		weekText() {
			return wddkAbbreviation(this.form.week) || '请选择星期';
		}
	},
	methods: {
		/**
		 * 选择星期
		 */
		selectWeek() {
			uni.$once('selectWeek', weeks => {
				if (weeks.length) {
					this.form.week = weeks.map(ele => ele.value);
				}
			});
			this.$setCache('weeks', this.form.week);
			uni.navigateTo({
				url: '/pages/equipment/setting/select-week'
			});
		},

		/**
		 * 日期选择成功
		 */
		calendarConfirm(date) {
			this.calendarShow = false;
			if (date.length) {
				this.form.startDate = date[0];
				this.form.endDate = date[date.length - 1];
			}
		},

		/**
		 * 选择时间
		 */
		confrim(event) {
			const { aboveTime, underTime } = event;
			if (aboveTime && underTime) {
				Object.assign(this.form, {
					startTime: aboveTime,
					endTime: underTime
				});
				this.timeShow = false;
			}
		},

		/**
		 * 保存
		 */
		save() {
			if (this.form.warnRuleName) {
				PostSetRadarWaveNobodyWarn({
					...this.form
				}).then(res => {
					uni.$u.toast('修改成功');
					uni.$emit('alertRule', {
						type: 'update',
						data: res.data
					});
					setTimeout(() =>{
						this.$store.dispatch('updateDevacesInfo'); // 更新设备数据
						uni.navigateBack();
					}, 2000);

				});
			} else {
				uni.$u.toast('名称不为空');
			}
		},

		/**
		 * 删除区域
		 */
		deleteRule() {
			PostDelRadarWaveNobodyWarn({
				//设备号
				deviceId: this.form.deviceId,
				//预警规则
				ruleNo: this.form.ruleNo
			}).then(res => {
				uni.$u.toast('删除成功');
				uni.$emit('alertRule', {
					type: 'delete'
				});
				setTimeout(() =>{
					this.$store.dispatch('updateDevacesInfo'); // 更新设备数据
					uni.navigateBack();
				}, 2000);
			});
		}
	}
};
</script>

<style lang="scss">
.ui-body {
	display: flex;
	width: 100%;
	flex-direction: column;
	position: relative;
	background: #fff;
	height: calc(100vh - 88rpx - var(--status-bar-height));
}

.ui-edit {
	display: flex;
	height: 100%;
	margin-top: 64rpx;
}

.ui-btn-group {
	height: 100rpx;
	width: 100%;
	position: fixed;
	bottom: 0;
	display: flex;
	align-items: center;
	flex-direction: row;
	& > view {
		width: 50%;
		height: 100%;
		text-align: center;
		line-height: 100rpx;
		font-size: 36rpx;
		@extend .active;
		&:nth-child(1) {
			background: #ffffff;
			border-top: 1rpx solid #e2e2e2;
			color: #e95656;
		}
		&:nth-child(2) {
			background-image: linear-gradient(90deg, #ffb24d 0%, #fd913b 100%);
			color: #ffffff;
		}
	}
}
</style>
