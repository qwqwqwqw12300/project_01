<!-- 添加联系人 -->
<template>
	<app-body :bg="false">
		<view class="ui-logo">
			<app-logo color="#353535" text="接警号码"></app-logo>
		</view>
		<view class="ui-form">
			<view class="ui-form-item" v-for="(item,index) in contactList" :key="item.index">
				<view class="item-title">
					<view class="title-left">
						<text></text>
						{{ item.buttonName }}
					</view>
					<view class="title-right">
						<view @tap="handleClear(item)">
							<u-icon name="/static/images/read-all.png" size="44rpx"></u-icon>
							<text>清除</text>
						</view>
						<view @tap="getContact(index)">
							<u-icon name="/static/images/tel-book.png" size="44rpx"></u-icon>
							<text>通讯录</text>
						</view>
					</view>
				</view>
				<view class="item-input">
					<view class="input-left">
						<u--input v-model="item.telName" placeholder="请输入姓名" border="none" maxlength="8" clearable></u--input>
					</view>
					<view class="input-right">
						<u--input v-model="item.telPhoneNumber" maxlength="11" type="number" placeholder="请输入手机号" border="none"
							placeholderStyle="text-align:right;color: rgb(192, 196, 204);" clearable></u--input>
					</view>
				</view>
			</view>
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
		<tel-books ref="telBookRef" @select="phoneSelect"></tel-books>
	</app-body>
</template>

<script>
	import {
		getSosGatewayTelPhone,
		setGatewayTelPhone
	} from '@/common/http/api.js';
	import {
		phoneValidator
	} from '@/common/utils/util';
	import {
		mapState,
	} from 'vuex';
	export default {
		computed: {
			...mapState({
				/**所有家庭列表**/
				deviceInfo: state => state.deviceInfo
			}),
		},
		data() {
			return {
				index: 0,
				contactDict: {
					1: '接警号1',
					2: '接警号2',
					3: '接警号3',
				},
				contactList: [],
			}
		},
		methods: {
			getContact(index) {
				this.index = index
				this.$refs.telBookRef.show(true)
			},
			phoneSelect(data) {
				console.log(data, 'dataa')
				return
				const {
					
					telPhoneNumber,
					telName
				} = data[0]
				this.contactList[this.index].telPhoneNumber = phone
				this.contactList[this.index].telName = name
				this.contactList = [...this.contactList]
			},
			handleClear(item) {
				item.telPhoneNumber = ''
				item.telName = ''
			},
			handleInit() {
				getSosGatewayTelPhone({
					deviceId: this.deviceInfo.deviceId
				}).then(res => {
					if(res.data && res.data.length) {
						this.contactList = res.data.map(n => {
							n.buttonName = this.contactDict[n.telPhoneNo]
							return n
						})
						return
					}
					this.contactList = [
						{
							buttonName: '接警号1',
							telPhoneNo: '1',
							telName: '',
							telPhoneNumber: ''
						},
						{
							buttonName: '接警号2',
							telPhoneNo: '2',
							telName: '',
							telPhoneNumber: ''
						},
						{
							buttonName: '接警号3',
							telPhoneNo: '3',
							telName: '',
							telPhoneNumber: ''
						}
					]
				})
			}, 
			handleSave() {
				const list = uni.$u.deepClone(this.contactList)
				for (let i = 0; i < list.length; i++) {
					if (list[i].telName || list[i].telPhoneNumber) {
						if (!list[i].telName) return uni.$u.toast(`${list[i].buttonName}姓名不能为空`)
						if (!list[i].telPhoneNumber) return uni.$u.toast(`${list[i].buttonName}手机号不能为空`)
						if (!phoneValidator(list[i].telPhoneNumber)) return uni.$u.toast(`${list[i].buttonName}手机号不正确`)
					}
				}
				if (!list[0].telName && !list[0].telPhoneNumber) return uni.$u.toast('请最少设置一个紧急联系人')
				setGatewayTelPhone({
					deviceId: this.deviceInfo.deviceId,
					phoneList: this.contactList,
				}).then(res => {
					uni.$u.toast('保存成功')
					setTimeout(() => {
						this.handleCancel()
					}, 1000)
				})
			},
			handleCancel() {
				uni.navigateBack()
			}
		},
		onShow() {
			this.handleInit()
		}
	}
</script>

<style lang="scss">
	.ui-logo {
		background: #ffffff;
		padding-bottom: 50rpx;
	}

	.ui-form {
		// padding: 0 44rpx;
		margin-top: 20rpx;

		.ui-form-item {
			background-color: #ffffff;
			margin-bottom: 20rpx;
			padding: 4rpx 30rpx;

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

				.title-right {
					display: flex;
					align-items: center;

					view {
						margin-left: 20rpx;
						display: flex;
						align-items: center;
						font-size: 30rpx;
						color: #353535;
						letter-spacing: 0;
						font-weight: 400;
					}
				}

			}

			.item-input {
				min-height: 128rpx;
				padding: 0 10rpx;
				border-bottom: solid 2px #f7f7f7;
				display: flex;
				align-items: center;
				justify-content: space-between;

				.input-left {
					width: 340rpx;
				}
				.input-right {
					width: 300rpx;
				}

			}
		}
	}

	.ui-btn {
		width: 100%;
		position: fixed;
		bottom: 0;
		left: 0;
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