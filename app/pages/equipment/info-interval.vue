<!--
* @Author: zhanghaowei
* @Date: 2023年2月28日16:16:00
* @FilePath: 
* @Description: 编辑时段禁用
-->

<template>
	<app-body :bg="false" :bodyStyle="{backgroundColor:'#FFF'}" title="编辑禁用时段">
		<!-- <app-logo color="#353535" text="编辑禁用时段"></app-logo> -->
		<view class="ui-cell">
			<u-cell-group>
				<u-cell title="名称">
					<u-input inputAlign="right" placeholder="请输入名称" border="none" slot="value" v-model="name">
					</u-input>
				</u-cell>
				<u-cell title="开始" arrow-direction="right" isLink>
					<text slot="value" class="u-slot-value">
						<!-- {{ defaultValue.length ? `${defaultValue[0]}  至 ${defaultValue[1]}` : '请选择'}} -->
						<picker mode="time" :value="starttime" start="00:00" end="24:00" @change="bindstarttime">
							<view class="uni-input">{{starttime||'请选择'}}</view>
						</picker>
					</text>
				</u-cell>
				<u-cell title="结束" arrow-direction="right" isLink>
					<text slot="value" class="u-slot-value">
						<!-- {{ defaultValue.length ? `${defaultValue[0]}  至 ${defaultValue[1]}` : '请选择'}} -->
						<picker mode="time" :value="endtime" start="00:00" end="24:00" @change="bindendtime">
							<view class="uni-input">{{endtime||'请选择'}}</view>
						</picker>
					</text>
				</u-cell>
			</u-cell-group>
		</view>
		<view class="changeday">
			<view class="typebtn" v-for="(item,index) in daylist" :class="item.act?'actitem':''"
				@click="changetype(index)">
				{{item.name}}
			</view>
		</view>
		<view class="weeklist">
			<view class="weekitem" v-for="(item,index) in week" :class="item.act?'actitem':''"
				@click="changeweek(index)">
				{{item.name}}
			</view>
		</view>
		<view class="ui-btn">
			<view class="btn-box">
				<view class="cancel-btn" @click="handleDel">
					删除
				</view>
				<view class="save-btn" @tap="handleSave">
					保存
				</view>
			</view>
		</view>
		<!-- <time-picker :show="showPicker" format="yyyy-mm-dd hh:ii" type="rangetime" :value="defaultValue"
			:show-tips="true" :begin-text="'开始'" :end-text="'结束'" :show-seconds="false" @confirm="onSelected"
			@cancel="showPicker=false">
		</time-picker> -->
	</app-body>
</template>

<script>
	import {
		PostSetPeriodDisable,
		PostDeletePeriodDisable
	} from '@/common/http/api';
	import {
		mapState,
	} from 'vuex';
	export default {
		data() {
			return {
				name: '',
				id: '',
				deviceInfo: '',
				enable: '',
				starttime: '',
				endtime: '',
				period: '',
				daylist: [{
						name: '每天',
						type: 'allday',
						act: true
					},
					{
						name: '工作日',
						type: 'work',
						act: false
					},
					{
						name: '周末',
						type: 'weekend',
						act: false
					}
				],
				week: [{
						name: '日',
						type: '7',
						act: false
					},
					{
						name: '一',
						type: '1',
						act: false
					},
					{
						name: '二',
						type: '2',
						act: false
					},
					{
						name: '三',
						type: '3',
						act: false
					},
					{
						name: '四',
						type: '4',
						act: false
					},
					{
						name: '五',
						type: '5',
						act: false
					},
					{
						name: '六',
						type: '6',
						act: false
					},
				]
			};
		},
		mounted() {
			// const newData = new Date()
			// const endTime = uni.$u.timeFormat(newData, 'yyyy-mm-dd hh:MM')
			// const startTime = uni.$u.timeFormat(new Date(newData.getTime() - 24 * 60 * 60 * 1000),
			// 	'yyyy-mm-dd hh:MM') //前一天
			// this.defaultValue = [startTime, endTime]
		},
		methods: {
			handleDel() {
				const list = []
				list.push(this.id)
				uni.showModal({
					title: '',
					content: '是否确认删除？',
					success: res => {
						if (res.confirm) {
							PostDeletePeriodDisable({
								deviceId: this.deviceInfo.deviceId,
								uuidList: list
							}).then(res => {
								uni.$u.toast(res.msg)
								setTimeout(() => {
									uni.navigateBack()
								}, 1000);
							})
						}
					}
				});
			},
			changetype(index) {
				for (var i = 0; i < this.daylist.length; i++) {
					if (i == index) {
						this.daylist[i].act = true
					} else {
						this.daylist[i].act = false
					}
				}
				var type = this.daylist[index].type;
				switch (type) {
					case 'allday':
						for (var i = 0; i < this.week.length; i++) {
							if (this.daylist[index].act == true) {
								this.week[i].act = true
							} else {
								this.week[i].act = false
							}
						}
						break;
					case 'work':
						for (var i = 0; i < this.week.length; i++) {
							if (this.daylist[index].act == true && this.week[i].type <= 5) {
								this.week[i].act = true
							} else {
								console.log(this.week[i].name)
								this.week[i].act = false
							}
						}
						break;
					case 'weekend':
						for (var i = 0; i < this.week.length; i++) {
							if (this.daylist[index].act == true && this.week[i].type >= 6) {
								this.week[i].act = true
							} else {
								this.week[i].act = false
							}
						}
						break;
					default:
						console.log('不满足上述所有case时, 执行默认')
				}
			},
			changeweek(index) {
				let arr = this.week
				if(index != undefined){
					this.week[index].act = !this.week[index].act
				}
				this.daylist[0].act = false
				this.daylist[1].act = false
				this.daylist[2].act = false
				if (arr[0].act == true && arr[1].act == true && arr[2].act == true && arr[3].act == true && arr[4].act == true && arr[5].act == true && arr[6].act == true) {
					this.daylist[0].act = true
					this.daylist[1].act = false
					this.daylist[2].act = false
					return
				}
				if (arr[0].act == false && arr[1].act == true && arr[2].act == true && arr[3].act == true && arr[4].act == true && arr[5].act == true && arr[6].act == false) {
					this.daylist[0].act = false
					this.daylist[1].act = true
					this.daylist[2].act = false
					return
				}
				if (arr[0].act == true && arr[1].act == false && arr[2].act == false && arr[3].act == false && arr[4].act == false && arr[5].act == false && arr[6].act == true) {
					this.daylist[0].act = false
					this.daylist[1].act = false
					this.daylist[2].act = true
					return
				}
			},
			bindstarttime(e) {
				this.starttime = e.detail.value
			},
			bindendtime(e) {
				this.endtime = e.detail.value
			},
			handleSave() {
				if (!this.name) return uni.$u.toast('名称不能为空')
				if (!this.starttime) return uni.$u.toast('请选择开始时间')
				if (!this.endtime) return uni.$u.toast('请选择结束时间')
				PostSetPeriodDisable({
					deviceId: this.deviceInfo.deviceId,
					periodDisableTag: this.name,
					beginTime: this.starttime,
					endTime: this.endtime,
					uuid: this.id,
					enable: this.enable
				}).then(res => {
					console.log(res, 'res')
					uni.$u.toast(res.msg)
					setTimeout(() => {
						uni.navigateBack()
					}, 1000);
				})
			},
		},
		onLoad(option) {
			this.deviceInfo = this.$store.state.deviceInfo
			const list = JSON.parse(option.list)
			this.name = list.periodDisableTag
			this.starttime = list.beginTime
			this.endtime = list.endTime
			this.id = list.uuid
			this.enable = list.enable
			this.period = list.period
			let arr = this.week
			let periods = list.period.split(',')
			for (var i = 0; i < periods.length; ++i) {
				if(periods[i] == 0){
					arr[0].act = true
				}
				if(periods[i] == 1){
					arr[1].act = true
				}
				if(periods[i] == 2){
					arr[2].act = true
				}
				if(periods[i] == 3){
					arr[3].act = true
				}
				if(periods[i] == 4){
					arr[4].act = true
				}
				if(periods[i] == 5){
					arr[5].act = true
				}
				if(periods[i] == 6){
					arr[6].act = true
				}
			}
			this.changeweek()
		}
	};
</script>

<style lang="scss">
	.ui-section {
		width: 500rpx;

	}

	.ui-cell {
		margin-top: 64rpx;
	}

	.ui-date-select {
		display: flex;
		justify-content: space-between;
		align-items: center;

		.ui-text {
			width: 200rpx;
			height: 60rpx;
			line-height: 60rpx;
			text-align: center;
			background: #F7F7F7;
			border: 1rpx solid rgba(136, 136, 136, 1);
			border-radius: 8rpx;
		}
	}

	.changeday {
		display: flex;
		justify-content: space-around;
		margin-top: 24rpx;

		.typebtn {
			width: 154rpx;
			height: 60rpx;
			border-radius: 30px;
			background: #D9D9D9;
			color: #000;
			font-family: PingFang SC;
			font-size: 32rpx;
			font-style: normal;
			line-height: 60rpx;
			text-align: center;
		}

		.actitem {
			background: #FE963D;
			color: white;
		}
	}

	.weeklist {
		display: flex;
		justify-content: space-around;
		margin-top: 40rpx;
		padding: 0 30rpx;

		.weekitem {
			width: 50rpx;
			height: 50rpx;
			border-radius: 30px;
			background: #D9D9D9;
			line-height: 50rpx;
			text-align: center;
			font-family: PingFang SC;
			font-size: 32rpx;
			font-style: normal;
		}

		.actitem {
			background: #FE963D;
			color: white;
		}
	}

	.ui-btn {
		width: 100%;
		position: fixed;
		bottom: 0;
		left: 0;

		.btn-box {
			height: 100rpx;
			line-height: 100rpx;
			display: flex;
			font-size: 36rpx;

			.cancel-btn {
				flex: 1;
				background-color: #fff;
				color: #E95656;
				text-align: center;
			}

			.save-btn {
				flex: 1;
				background-image: linear-gradient(90deg, #FFB24D 0%, #FD913B 100%);
				color: #fff;
				text-align: center;
			}
		}
	}
</style>