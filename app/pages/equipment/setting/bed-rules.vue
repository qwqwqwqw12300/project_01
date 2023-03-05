<template>
	<app-body :bg="false">
		<view class="ui-body">
			<app-logo text="离床时间设置"></app-logo>
				<view class="ui-edit">
					<u-cell-group>
						<u-cell title="选择时间格式">
							<view  slot="right-icon">
								<u-radio-group
								    placement="row"
									v-model="form.dateType"
									>
									<u-radio activeColor="#FEAE43"  :customStyle="{marginRight: '32rpx'}" label="按日期" name="0"></u-radio>
									<u-radio activeColor="#FEAE43" label="按星期" name="1"></u-radio>
								</u-radio-group>
							</view>
							
						</u-cell>
						<u-cell title="日期"
						v-if="form.dateType == 0"
						isLink
						@click="calendarShow = true"
						:value="form.startDate && form.endDate ? form.startDate + '至' + form.endDate : '请选择日期'"></u-cell>
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
	import { PostDelRadarWaveLeaveBedRulesDate, PostSetRadarWaveLeaveBedRulesDate } from '../../../common/http/api';
import { wddkAbbreviation } from '../../../common/utils/util';
	export default {
		data() {
			return {
				form: {
					deviceId: '',
					//规则名称
					roomZoneId: '',
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
		computed: {
			/**日期信息**/
			weekText() {
				return wddkAbbreviation(this.form.week) || '请选择星期';
			}
		},
		onLoad(option) {
			
			const cache = this.$getCache('bedRuleCache') || {};
			console.log(cache, 'cache');
			Object.assign(this.form, cache || {});
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
				PostSetRadarWaveLeaveBedRulesDate({
					...this.form
				}).then(res => {
					uni.$u.toast('修改成功');
					uni.$emit('bedRule', {
						type: 'update',
						data: res.data
					});
					setTimeout(() =>{
						this.$store.dispatch('updateDevacesInfo'); // 更新设备数据
						uni.navigateBack();
					}, 2000);
							
				});
			},
			
			/**
			 * 删除区域
			 */
			deleteRule() {
				PostDelRadarWaveLeaveBedRulesDate({
					//设备号
					deviceId: this.form.deviceId,
					//预警规则
					roomZoneId: this.form.roomZoneId
				}).then(res => {
					uni.$u.toast('删除成功');
					uni.$emit('bedRule', {
						type: 'delete'
					});
					setTimeout(() =>{
						this.$store.dispatch('updateDevacesInfo'); // 更新设备数据
						uni.navigateBack();
					}, 2000);
				});
			}
		},
		onBackPress() {
			uni.$off('bedRule');
		}
	}
</script>

<style lang="scss">
	.ui-body {
		display: flex;
		width:100%;
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
		&>view {
			width: 50%;
			height: 100%;
			text-align: center;
			line-height: 100rpx;
			font-size: 36rpx;
			@extend .active;
			&:nth-child(1) {
				background: #FFFFFF;
				border-top: 1rpx solid #e2e2e2;
				color: #E95656;
			}
			&:nth-child(2) {
				background-image: linear-gradient(90deg, #FFB24D 0%, #FD913B 100%);
				color: #FFFFFF;
			}
		}
		
	}
</style>
