<!--
* @Author: zhanghaowei
* @Date: 2023年2月28日16:16:00
* @FilePath: 
* @Description: 编辑时段禁用
-->

<template>
	<app-body :bg="false" :bodyStyle="{backgroundColor:'#FFF'}">
		<app-logo color="#353535" text="编辑禁用时段" ></app-logo>
		<view class="ui-cell">
			<u-cell-group>
				<u-cell title="名称">
					<u-input inputAlign="right" placeholder="请输入名称" border="none" slot="right-icon"
						v-model="name"></u-input>
				</u-cell>
				<u-cell @tap="handleSelectStart" title="日期"  arrow-direction="right" isLink>
					<text slot="value" class="u-slot-value">
						{{ defaultValue.length ? `${defaultValue[0]}  至 ${defaultValue[1]}` : '请选择'}}
					</text>
				</u-cell>
			</u-cell-group>
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
		<time-picker :show="showPicker" format="yyyy-mm-dd hh:ii" type="rangetime" :value="defaultValue"
			:show-tips="true" :begin-text="'开始'" :end-text="'结束'" :show-seconds="false" @confirm="onSelected"
			@cancel="showPicker=false">
		</time-picker>
	</app-body>
</template>

<script>
	import timePicker from '@/components/term-picker/term-picker.vue';
	import {
		PostSetPeriodDisable,
		PostDeletePeriodDisable
	} from '@/common/http/api';
	import {
		mapState,
	} from 'vuex';
	export default {
		components:{
			timePicker
		},
		data() {
			return {
				name:'',
				//日期选择组件
				showPicker: false,
				defaultValue:['2023-02-27 14:00', '2023-03-05 13:59'],
				timeShow:false,
				defaultTime:[0, 0, 0, 23, 59],
				id:'',
				deviceInfo:'',
				enable:''
			};
		},
		computed:{
			pickTime(){
				console.log('jinlai')
				// return this.startTime ? `${moment(this.startTime).format('YYYY-MM-DD')}~${moment(this.endTime).format('YYYY-MM-DD')}`: ''
			}
		},
		mounted() {
			// const newData = new Date()
			// const endTime = uni.$u.timeFormat(newData, 'yyyy-mm-dd hh:MM')
			// const startTime = uni.$u.timeFormat(new Date(newData.getTime() - 24 * 60 * 60 * 1000),
			// 	'yyyy-mm-dd hh:MM') //前一天
			// this.defaultValue = [startTime, endTime]
		},
		methods: {
			handleSelectStart() {
				this.showPicker = true
			},
			handleTime(){
				this.timeShow = true
			},
			cancel(e){
				this.timeShow = false
			},
			onSelected(e){
				let date1 = new Date(e.value[0]) //要对比的时间
				let date2 = new Date(); //获取当前时间对象
				let num = 24 * 60 * 60 * 1000; //一天的毫秒数
				let res = date2.getTime() - date1.getTime(); //两个时间的毫秒差
				if (res > 0) {
					if (res > num) {
						uni.$u.toast('选择的时间已过期，请重新选择')
					} else if (date1.getDate() != date2.getDate()) {
						uni.$u.toast('选择的时间已过期，请重新选择')
					} else {
						uni.$u.toast('选择的时间已过期，请重新选择')
					}
				}else{
					this.defaultValue = [...e.value]
					this.showPicker = false
				}
			},
			handleDel() {
				const list = []
				list.push(this.id)
				uni.showModal({
					title: '',
					content: '是否确认删除？',
					success: res => {
						if (res.confirm) {
							PostDeletePeriodDisable({
								deviceId:this.deviceInfo.deviceId,
								uuidList:list
							}).then(res=>{
								uni.$u.toast(res.msg)
								setTimeout(() => {
									uni.navigateBack()
								}, 1000);
							})
						}
					}
				});
			},
			handleSave() {
				PostSetPeriodDisable({
					deviceId:this.deviceInfo.deviceId,
					periodDisableTag:this.name,
					beginTime:this.defaultValue[0],
					endTime:this.defaultValue[1],
					uuid:this.id,
					enable:this.enable
				}).then(res=>{
					console.log(res,'res')
					uni.$u.toast(res.msg)
					setTimeout(() => {
						uni.navigateBack()
					}, 1000);
				})
			},
		},
		onLoad(option) {	
			this.deviceInfo = this.$store.state.deviceInfo 
			console.log(JSON.parse(option.list),'list')
			const list = JSON.parse(option.list)
			this.name = list.periodDisableTag
			this.defaultValue[0] = list.beginTime
			this.defaultValue[1] = list.endTime
			console.log(this.defaultValue,'defaultValue')
			this.id = list.uuid
			this.enable = list.enable
		}
	};
</script>

<style lang="scss">
	.ui-section {
		width: 500rpx;
		
	}
	.ui-cell{
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
