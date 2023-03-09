<!--
* @Author: zhanghaowei
* @Date: 2023年3月1日14:43:51
* @FilePath: 
* @Description: 查看位置守护
-->
<!-- 添加联系人 -->
<template>
	<app-body :bg="false">
		<view class="ui-logo">
			<view class="ui-edit">
				<app-logo color="#353535" text="编辑守护位置"></app-logo>
				<view class="ui-edit-right" @click="handleEdit">
					<text v-if="editBtn">删除</text>
					<text v-else>编辑</text>
				</view>
			</view>
			<view style="margin-top:64rpx;">
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
		</view>
		<view class="ui-form">
			<view class="ui-form-item" v-for="(item,index) in contactList" :key="index">
				<view class="item-title">
					<view class="title-left">
						<text></text>
						{{ item.orderName }}
					</view>
					<view  >
						<text  v-if="editBtn" class="ui-form-del" @click="handleDel(index,item.orderName)">删除</text>
						<u-switch v-else @change="handleSwitch" v-model="item.flag" activeValue="1" inactiveValue="0"
							activeColor="#FEAE43" inactiveColor="rgb(138, 138, 138)" size="20"> 
						</u-switch>
					</view>
				</view>
				<view class="item-input">
					<u-cell-group>
						<u-cell title="地址" arrow-direction="right" isLink @click="handleJump(index)"> 
							<text slot="value" class="u-slot-value ui-cell" >
								{{item.address}}
							</text>
						</u-cell>
						<u-cell @tap="handleSelect(index,item.date)" title="到达日期" arrow-direction="right" isLink>
							<text slot="value" class="u-slot-value">
								{{item.date}}
							</text>
						</u-cell>
						<u-cell @tap="handleTime(index,item.time)" title="到达时间" arrow-direction="right" isLink>
							<text slot="value" class="u-slot-value">
								{{item.time}}
							</text>
						</u-cell>
					</u-cell-group>
				</view>
			</view>
		</view>
		<view class="ui-add" @click="addPlace">
			<image class="ui-add-icon" src="@/static/images/add-guard.png"></image>
			<text>添加地点</text>
		</view>
		<view class="ui-div"></view>
		<view class="ui-btn" v-if="editBtn==false">
			<view class="btn-box">
				<view class="cancel-btn" @click="handleCancel">
					删除
				</view>
				<view class="save-btn" @tap="handleSave">
					保存
				</view>
			</view>
		</view>
		<u-calendar :show="showDate" :defaultDate="defaultDate" :mode="mode" @confirm="confirm" @close="close" closeOnClickOverlay></u-calendar>
		<u-datetime-picker
			ref="datetimePicker"
			:show="showTime"
			v-model="time"
			mode="time"
			closeOnClickOverlay
			@confirm="confirmTime"
			@close="showTime=false"
			@cancel="cancelTime"
		></u-datetime-picker>
		<time-picker :show="showPicker" format="yyyy-mm-dd hh:ii" type="rangetime" :value="defaultValue" :show-tips="true" :begin-text="'开始'"
		    :end-text="'结束'" :show-seconds="false" @confirm="onSelected"  @cancel="showPicker=false">
		</time-picker>
	</app-body>
</template>

<script>
	import timePicker from '@/components/term-picker/term-picker.vue';
	import {
		PostSetLocationGuard,
		PostDeleteLocationGuard
	} from '@/common/http/api';
	export default {
		components:{
			timePicker
		},
		data() {

			return {
				index: 0,
				name:'',
				flag:'0',
				editBtn:false,
				showDate:false,
				mode:'single',
				showTime:false,
				time:'00:00',
				defaultDate:'',
				id:0,
				contactList: [],
				showPicker: false,
				defaultValue:['2023-02-27 14:00', '2023-03-05 13:59'],
				timeShow:false,
				defaultTime:[0, 0, 0, 23, 59],
				startDate: "2023-02-27",
				endDate: "2023-03-05",
				startTime: "14:00",
				endTime: "15:00",
			}
		},
		methods: {
			// 日期
			handleSelectStart(){
				this.showPicker = true
			},
			onSelected(e){
				this.defaultValue = [...e.value]
				this.showPicker = false
			},
			handleConfirm(e){
				console.log(e)//确定事件 =>12:30-17:30
				this.startTime = e.aboveTime
				this.endTime = e.underTime
				this.timeShow = false
				
				// this.defaultTime = this.defaultTime.forEach(item=>{
				// 	item = parseInt(item)
				// })
				console.log(this.defaultTime)
			},
			handleSelect(index,date){
				this.index = index
				this.defaultDate = date
				this.showDate = true
			},
			confirm(e) {
				console.log(e);
				this.contactList[this.index].date = e[0] 
				this.showDate = false
			},
			close() {
				this.showDate = false
			},
			//时间
			handleTime(index,time){
				console.log(time,'time')
				this.$refs.datetimePicker.innerValue = time
				this.index = index
				this.time = time
				console.log(this.time,'this.time')
				this.showTime = true
			},
			confirmTime(e) {
				this.showTime = false
				console.log(e.value)
				this.contactList[this.index].time = e.value
			},
			cancelTime(){
				this.showTime = false
			},
			handleJump(index){
				this.index = index
				uni.$on('getMapData', res => {	
					console.log(res,'res')
					this.contactList[this.index].address = res.siteInfo
					this.contactList[this.index].longitude = res.longitude
					this.contactList[this.index].latitude = res.latitude
					this.contactList[this.index].radius = res.sliderValue
				});
				
				uni.navigateTo({
					url:'/pages/equipment/enclosure'
				})
			},
			handleCancel() {
				const list = []
				list.push(this.id)
				uni.showModal({
					title: '提示',
					content: `是否确认删除？`,
					success: res => {
						if(res.confirm){
							PostDeleteLocationGuard({
								deviceNo: '867977060000248',
								uuidList:list
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
			handleSave(){
				const list = []
				this.contactList.forEach(item=>{
					console.log(item,'item')
					item.estimatedTime = item.date +' '+ item.time
					list.push({
						guardType:'circle',
						address:item.address,
						longitude:item.longitude,
						latitude:item.latitude,
						radius:item.radius,
						enable:item.flag,
						estimatedTime:item.estimatedTime
					})
					
					return item
				})
				const obj = {
					deviceNo:'867977060000248',
					jobName:this.name,
					firstDate:this.defaultValue[0],
					lastDate:this.defaultValue[1],
					uuid:this.id,
					places:list
				}
				console.log(obj,'obj')
				PostSetLocationGuard({
					...obj
				}).then(res=>{
					console.log(res,'res')
					uni.$u.toast(res.msg)
					setTimeout(() => {
						uni.navigateBack()
					}, 1000);
				})
			},
			handleSwitch(){
				
			},
			addPlace(){
				this.contactList.push({
					guardType:'circle',
					address:'',
					longitude:'',
					latitude:'',
					radius:'',
					orderName:'',
					flag:'1',
					estimatedTime:'',
					date:'',
					time:''
				})
				this.contactList.map((item,index)=>{
					item.orderName = `地点${(index+1)}`
					return item
				})
				console.log(this.contactList)
			},
			handleEdit(){
				this.editBtn =! this.editBtn
			},
			handleDel(id,orderName){
				uni.showModal({
					title: '提示',
					content: `是否确认删除${orderName}？`,
					success: res => {
						if(res.confirm){
							this.contactList.splice(this.contactList.findIndex((item, index) => index == id), 1)
						}
					}
				});
			},
		},
		onLoad(option) {
			console.log(JSON.parse(option.list),'list')
			const list = JSON.parse(option.list)
			this.name = list.jobName
			this.defaultValue[0] = list.firstDate
			this.defaultValue[1] = list.lastDate
			this.id = list.uuid
			list.places.forEach(item=>{
				this.contactList.push({
					guardType:'circle',
					address:item.address,
					longitude:item.longitude,
					latitude:item.latitude,
					radius:item.radius,
					orderName:'',
					flag:item.enable,
					estimatedTime:item.estimatedTime,
					date:item.estimatedTime.split(" ")[0],
					time:item.estimatedTime.split(" ")[1]
				})
			})
			this.contactList.map((item,index)=>{
				item.orderName = `地点${(index+1)}`
				return item
			})
		}
	}
</script>

<style lang="scss">
	.ui-logo {
		background: #ffffff;
		.ui-edit{
			display: flex;
			align-items: flex-end;
			justify-content: space-between;
			.ui-edit-right{
				margin-right: 34rpx;
				font-size: 30rpx;
				color: #353535;
				line-height: 30rpx;
				font-weight: 400;
			}
		}
		.u-cell-title{
			margin-top: 64rpx;
		}
	}
	
	.ui-add{
		width: 100%;
		margin-top: 64rpx;
		margin-bottom: 64rpx;
		display:flex;
		align-items: center;
		justify-content: center;
		.ui-add-icon{
			width: 36rpx;
			height: 36rpx;
		}
		text{
			margin-left: 10rpx;
			font-size: 36rpx;
			color: #599FFF;
			text-align: center;
			line-height: 36rpx;
			font-weight: 400;
		}
	}
	.ui-cell{
		width: 80%;
		display: flex;
		align-items: center;
		justify-content: flex-end;
		flex-wrap: wrap;
	}
	.ui-form {
		// padding: 0 44rpx;
		margin-top: 40rpx;

		.ui-form-item {
			background-color: #ffffff;
			margin-bottom: 40rpx;
			padding: 0rpx 30rpx;

			.item-title {
				display: flex;
				align-items: center;
				min-height: 100rpx;
				justify-content: space-between;
				border-bottom: solid 2px #f7f7f7;
				padding: 0 5rpx;

				.title-left {
					display: flex;
					align-items: center;
					height: 100%;
					justify-content: center;
					font-size: 36rpx;
					color: #FEAE43;
					font-weight: 600;

					text {
						height: 50rpx;
						border-left: solid 14rpx #FEAE43;
						border-radius: 4rpx;
						margin-right: 20rpx;
					}
				}
			}
			.ui-form-del{
				font-size: 30rpx;
				color: #E95656;
				line-height: 30rpx;
				font-weight: 400;
			}
			.item-input {
				min-height: 128rpx;
				border-bottom: solid 2px #f7f7f7;
				display: flex;
				align-items: center;
				justify-content: space-between;

				.input-left,
				.input-right {
					width: 240rpx;
				}

			}
		}
	}
	.ui-div{
		height: 90rpx;
	}
	.ui-btn {
		width: 100%;
		position: fixed;
		bottom: 0;
		z-index: 999;
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
