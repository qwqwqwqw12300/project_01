<!-- 添加联系人 -->
<template>
	<app-body :bg="false">
		<view class="ui-logo">
			<view class="ui-edit">
				<app-logo color="#353535" text="新建守护位置"></app-logo>
				<view class="ui-edit-right" @click="handleEdit">
					<text v-if="editBtn">删除</text>
					<text v-else>编辑</text>
				</view>
			</view>
			<u-cell title="名称" class="u-cell-title">
				<u-input inputAlign="right" placeholder="请输入名称" border="none" slot="right-icon"
					v-model="name"></u-input>
			</u-cell>
		</view>
		<view class="ui-form">
			<view class="ui-form-item" v-for="(item,index) in contactList" :key="item.orderNum">
				<view class="item-title">
					<view class="title-left">
						<text></text>
						{{ item.orderName }}
					</view>
					<view  >
						<text  v-if="editBtn" class="ui-form-del" @click="handleDel(item.orderNum,item.orderName)">删除</text>
						<u-switch v-else @change="handleSwitch" v-model="item.flag" activeValue="1" inactiveValue="0"
							activeColor="#FEAE43" inactiveColor="rgb(138, 138, 138)" size="20"> 
						</u-switch>
					</view>
				</view>
				<view class="item-input">
					<u-cell-group>
						<u-cell title="地址" arrow-direction="right" isLink @tap="handleJump()"> 
							<text slot="value" class="u-slot-value">
								{{item.address}}
							</text>
						</u-cell>
						<u-cell @tap="handleSelect(item.orderNum)" title="到达日期" arrow-direction="right" isLink>
							<text slot="value" class="u-slot-value">
								{{item.date}}
							</text>
						</u-cell>
						<u-cell @tap="handleTime(item.orderNum)" title="到达时间" arrow-direction="right" isLink>
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
		<view class="ui-btn" v-if="editBtn==false">
			<view class="btn-box">
				<view class="cancel-btn" @click="handleCancel">
					取消
				</view>
				<view class="save-btn" @tap="handleSave">
					保存
				</view>
			</view>
		</view>
		<u-calendar :show="showDate" :mode="mode" @confirm="confirm" @close="close" closeOnClickOverlay></u-calendar>
		<u-datetime-picker
			:show="showTime"
			v-model="time"
			mode="time"
			closeOnClickOverlay
			@confirm="confirmTime"
			@cancel="cancelTime"
		></u-datetime-picker>
	</app-body>
</template>

<script>
	
	export default {
		data() {

			return {
				index: 0,
				name:'',
				flag:'0',
				editBtn:false,
				showDate:false,
				mode:'single',
				showTime:false,
				time:'15:26',
				id:0,
				contactList: [{
						orderNum: 1,
						orderName: '地点1',
						address:'新大陆科技园',
						date:'2023-03-01',
						time:'15:32',
						flag:'1'
					},
					{
						orderNum: 2,
						orderName: '地点2',
						address:'新大陆壹号',
						date:'2023-03-01',
						time:'15:32',
						flag:'1'
					},
				],
			}
		},
		methods: {
			// 日期
			handleSelect(id){
				this.id = id
				this.showDate = true
			},
			confirm(e) {
				console.log(e);
				this.contactList.map(item=>{
					if(item.orderNum == this.id){
						item.date = e[0]
					}
				})
				this.showDate = false
			},
			close() {
				this.showDate = false
			},
			//时间
			handleTime(id){
				this.id = id
				this.showTime = true
			},
			confirmTime(e) {
				this.showTime = false
				this.result(e.value, e.mode)
			},
			cancelTime(){
				this.showTime = false
			},
			result(time, mode) {
				this.contactList.map(item=>{
					if(item.orderNum == this.id){
						item.time = time
					}
				})
			},
			handleJump(){
				uni.navigateTo({
					url:'/pages/equipment/new-address'
				})
			},
			handleCancel() {
				uni.navigateBack()
			},
			handleSave(){
				console.log(this.name,'this.name')
				console.log(this.contactList,'this.contactList')
			},
			handleSwitch(){
				
			},
			addPlace(){
				let date = Date.now()
				let rund = Math.ceil(Math.random()*1000)
				let orderNum = date + '' + rund
				console.log(date,'date')
				this.contactList.push({
					orderNum,
					orderName: '地点' + (this.contactList.length+1),
					address:'默认地址',
					date:'2023-03-01',
					time:'15:32',
					flag:'1'
				})
				console.log(this.contactList,'this.contactList')
			},
			handleEdit(){
				this.editBtn =! this.editBtn
			},
			handleDel(id,orderName){
				uni.showModal({
					title: '提示',
					content: `是否确认删除${orderName}？`,
					success: res => {
						this.contactList.splice(this.contactList.findIndex(item=>item.orderNum==id),1)
					}
				});
			}
		},
		onShow() {
			
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
	
	.ui-btn {
		width: 100%;


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
