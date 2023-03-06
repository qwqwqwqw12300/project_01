<!--
* @Author: zhanghaowei
* @Date: 2023年2月28日16:16:00
* @FilePath: 
* @Description: 编辑时段禁用
-->

<template>
	<app-body>
		<app-logo color="#353535" text="编辑禁用时段" ></app-logo>
		<view class="ui-cell">
			<u-cell-group>
				<u-cell title="名称">
					<u-input inputAlign="right" placeholder="请输入名称" border="none" slot="right-icon"
						v-model="name"></u-input>
				</u-cell>
				<u-cell @tap="handleSelect" title="日期"  arrow-direction="right" isLink>
					<text slot="value" class="u-slot-value">
						{{ startDate }} 至 {{endDate}}
					</text>
				</u-cell>
				<u-cell @tap="handleTime" title="时间"  arrow-direction="right" isLink>
					<text slot="value" class="u-slot-value">
						{{ startTime }} 至 {{endTime}}
					</text>
				</u-cell>
			</u-cell-group>
		</view>
		<view class="ui-btn">
			<view class="btn-box">
				<view class="cancel-btn" @click="handleCancel">
					删除
				</view>
				<view class="save-btn" @tap="handleSave">
					保存
				</view>
			</view>
		</view>
		<time-picker :show="showPicker" type="range" :value="defaultValue" :show-tips="true" :begin-text="'开始'"
		    :end-text="'结束'" :show-seconds="true" @confirm="onSelected"  @cancel="showPicker=false">
		</time-picker>
		<smh-time-range :isUnder="timeShow" :time="defaultTime" @confrim="confrim" @cancel="cancel"></smh-time-range>
	</app-body>
</template>

<script>
	import timePicker from '@/components/term-picker/term-picker.vue';
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
				startDate: "2023/02/27",
				endDate: "2023/03/05",
				startTime: "14:00",
				endTime: "15:00",
				timeShow:false,
				defaultTime:[0, 0, 0, 23, 59]
			};
		},
		computed:{
			pickTime(){
				console.log('jinlai')
				// return this.startTime ? `${moment(this.startTime).format('YYYY-MM-DD')}~${moment(this.endTime).format('YYYY-MM-DD')}`: ''
			}
		},
		mounted() {
			
		},
		methods: {
			handleSelect() {
				this.showPicker = true
			},
			handleTime(){
				this.timeShow = true
			},
			confrim(e){
				console.log(e)//确定事件 =>12:30-17:30
				this.startTime = e.aboveTime
				this.endTime = e.underTime
				this.timeShow = false
				
				// this.defaultTime = this.defaultTime.forEach(item=>{
				// 	item = parseInt(item)
				// })
				console.log(this.defaultTime)
			},
			cancel(e){
				this.timeShow = false
			},
			onSelected(e){
				console.log(e.value,'e')
				this.startDate = e.value[0]
				this.endDate = e.value[1]
				this.showPicker = false
			},
			handleCancel() {
				uni.navigateBack()
			},
			handleSave() {
				
			},
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
