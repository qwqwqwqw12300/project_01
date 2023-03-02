<!-- 添加联系人 -->
<template>
	<app-body :bg="false">
		<view class="ui-logo">
			<app-logo color="#353535" text="新建守护位置"></app-logo>
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
						<u-switch @change="handleSwitch" v-model="flag" activeValue="1" inactiveValue="0"
							activeColor="#FEAE43" inactiveColor="rgb(138, 138, 138)" size="20"> >
						</u-switch>
					</view>
				</view>
				<view class="item-input">
					<u-cell-group>
						<u-cell title="地址" arrow-direction="right" isLink>
							<text slot="value" class="u-slot-value">
								{{item.address}}
							</text>
						</u-cell>
						<u-cell @tap="handleSelect" title="到达日期" arrow-direction="right" isLink>
							<text slot="value" class="u-slot-value">
								{{item.date}}
							</text>
						</u-cell>
						<u-cell @tap="handleTime" title="到达时间" arrow-direction="right" isLink>
							<text slot="value" class="u-slot-value">
								{{item.time}}
							</text>
						</u-cell>
					</u-cell-group>
				</view>
			</view>
		</view>
		<view class="ui-add">
			<image class="ui-add-icon" src="@/static/images/add-guard.png"></image>
			<text>添加地点</text>
		</view>
		<view class="ui-btn">
			<view class="btn-box">
				<view class="cancel-btn" @click="handleCancel">
					取消
				</view>
				<view class="save-btn" @tap="handleSave">
					保存
				</view>
			</view>
		</view>
		<u-calendar :show="showDate" :mode="mode" @confirm="confirm"></u-calendar>
		<u-datetime-picker
			:show="showTime"
			v-model="value4"
			mode="time"
			closeOnClickOverlay
			@confirm="confirms"
			@cancel="cancel"
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
				showDate:false,
				mode:'single',
				showTime:false,
				value4:'15:26',
				contactList: [{
						orderNum: 1,
						orderName: '地点1',
						address:'新大陆科技园',
						date:'2023-03-01',
						time:'15:32'
					},
					{
						orderNum: 2,
						orderName: '地点2',
						address:'新大陆壹号',
						date:'2023-03-01',
						time:'15:32'
					},
				],
			}
		},
		methods: {
			handleSelect(){
				this.showDate = true
			},
			handleTime(){
				this.showTime = true
			},
			confirm(e) {
				console.log(e);
				this.showDate = false
			},
			cancel() {
				this.showTime = false
			},
			confirms(e) {
				this.showTime = false
				this.result(e.value, e.mode)
			},
			result(time, mode) {
				console.log(time,'time')
			},
			handleCancel() {
				uni.navigateBack()
			},
			handleSwitch(){
				
			}
		},
		onShow() {
			
		}
	}
</script>

<style lang="scss">
	.ui-logo {
		background: #ffffff;
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
