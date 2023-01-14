<!-- 添加联系人 -->
<template>
	<app-body>
		<u-list height="720">
			<u-list-item v-for="item in contactList" :key="item.memberContactsId">
				<view class="ui-list">
					<view class="ui-list-card">
						<view class="card-header">
							<u-icon name="account-fill" size="33" color="#FDC135" />
							<text class="text">{{ item.contactName }}</text>
							<u-icon @tap="handleEidt(item)" name="edit-pen-fill" size="33" color="#FDC135" />
						</view>
						<view class="card-content">
							<view class="name">
								<u-icon name="phone" size="30" color="#fff" />
								<text>{{ item.name }}</text>
							</view>
							<text class="tel">{{ item.phone }}</text>
							<u-icon @tap="handleDel(item.memberContactsId)" name="close-circle" size="33"
								color="#000" />
						</view>
					</view>
				</view>
			</u-list-item>
		</u-list>
		<view class="ui-btn">
			<button @click="handleAdd">添加紧急联系人</button>
		</view>
		<u-popup :closeable="true" :overlay="false" zIndex="99" :round="10" :show="isEditShow" mode="center"
			@close="handleClose">
			<view class="wd-add">
				<view class="ui-form">
					<view class="ui-form-item">
						<u-text prefixIcon="/static/images/order.png" iconStyle="font-size: 32rpx" text="选择优先级"
							color="#444" size="28rpx"></u-text>
						<view class="ui-input">
							<uni-data-select v-model="form.orderNum" :clear="false" :localdata="orderNumDict">
							</uni-data-select>
						</view>
					</view>
					<view class="ui-form-item">
						<u-text prefixIcon="account" iconStyle="font-size: 40rpx" text="联系人姓名" color="#444"
							size="28rpx">
						</u-text>
						<view class="ui-input">
							<u-input v-model="form.phoneName" placeholder="输入紧急联系人名字" :border="'none'" fontSize="28rpx"
								clearable>
							</u-input>
						</view>
					</view>
					<view class="ui-form-item">
						<u-text prefixIcon="/static/images/tel.png" :iconStyle="{width: '30rpx',height:'40rpx'}"
							text="手机号码" color="#444" size="28rpx"></u-text>
						<view class="ui-input">
							<u-input v-model="form.phone" placeholder="输入紧急联系手机号码" :border="'none'" fontSize="28rpx"
								clearable>
							</u-input>
						</view>
					</view>
				</view>
				<view class="wd-btn-gloup">
					<button @click="handelSubmit">提交</button>
					<button @click="handleClose">取消</button>
				</view>
			</view>
		</u-popup>
	</app-body>
</template>

<script>
	import {
		GetContactsList,
		PostDelContacts,
		PostEditContacts,
	} from '@/common/http/api.js';
	export default {
		data() {
			const orderNumDict = [{
				value: '0',
				text: '第一紧急联系人'
			}, {
				value: '1',
				text: '第二紧急联系人'
			}, {
				value: '2',
				text: '第三紧急联系人'
			}]
			return {
				contactList: [],
				orderNumDict,
				isEditShow: false,
				form: {
					orderNum: '',
					phoneName: '',
					phone: '',
					memberContactsId: '',
				},
			}
		},
		methods: {
			handleAdd() {
				uni.navigateTo({
					url: '/pages/myself/add-contact?type=add'
				})
			},
			handleDel(memberContactsId) {
				uni.showModal({
					title: '提示',
					content: '是否确认删除',
					success: res => {
						if (res.confirm) {
							PostDelContacts({
								memberContactsId,
							}).then(res => {
								uni.$u.toast(res.msg)
								setTimeout(() => {
									this.handleInit()
								}, 500)				
							})
						} else if (res.cancel) {
							console.log('用户点击取消');
						}
					}
				});
			},
			handleEidt(item) {
				const {
					phone,
					name,
					memberContactsId,
					orderNum
				} = item
				this.form.phone = phone
				this.form.phoneName = name
				this.form.orderNum = orderNum + ''
				this.form.memberContactsId = memberContactsId
				this.isEditShow = true
				// uni.navigateTo({
				// 	url: '/pages/myself/add-contact?type=edit&data=' + encodeURIComponent(JSON.stringify(item))
				// })
			},
			handleInit() {
				GetContactsList({}).then(res => {
					this.contactList = res.rows.map(n => {
						n.contactName = this.orderNumDict.find(item => {
							return item.value == n.orderNum
						})['text']
						return n
					})
				})
			},
			handleClose() {
				this.form = {}
				this.isEditShow = false
			},
			handelSubmit() {
				const {
					orderNum,
					phone,
					phoneName,
				} = this.form
				if (!orderNum) {
					return uni.$u.toast('请选择紧急联系人')
				}
				if (!phoneName) {
					return uni.$u.toast('请填写联系人姓名')
				}
				if (!phone) {
					return uni.$u.toast('请填写联系人手机号码')
				}
				PostEditContacts({
					...this.form
				}).then(res => {
					uni.$u.toast(res.msg)
					setTimeout(() => {
						this.handleClose();
						this.handleInit()
					}, 500)
				})
			}
		},
		mounted() {
			this.handleInit()
		}
	}
</script>

<style lang="scss">
	::v-deep {
		.uni-select {
			border: none;
		}
	}

	.ui-list {
		margin-top: 20rpx;
		padding: 0 50rpx;

		.ui-list-card {
			margin-bottom: 30rpx;
			padding: 30rpx 30rpx;
			border-radius: 14rpx;
			background: linear-gradient(#F5F5F5, #E5E5E5);

			.card-header {
				display: flex;
				align-items: center;
				justify-content: space-between;

				.text {
					flex: 1;
					margin-left: 10rpx;
					color: #333;
					font-size: 38rpx;
					font-weight: bold;
				}
			}

			.card-content {
				margin-top: 50rpx;
				margin-bottom: 20rpx;
				display: flex;
				justify-content: space-between;
				align-items: center;

				.name {
					padding: 20rpx 30rpx 20rpx 20rpx;
					width: 200rpx;
					border-radius: 16rpx;
					background-color: #FEC92E;
					display: flex;
					align-items: center;
					font-size: 32rpx;
					font-weight: bold;

					text {
						margin-left: 8rpx;
						overflow: hidden;
						text-overflow: ellipsis;
					}
				}

				.tel {
					font-size: 38rpx;
					font-weight: bold;
				}
			}
		}
	}

	.ui-btn {
		// margin-top: 60rpx;
		padding: 0 50rpx;
	}

	.wd-add {
		width: 620rpx;
		height: 650rpx;
		border-radius: 20rpx;
		filter: drop-shadow(0 0 5rpx rgba(7, 5, 5, 0.34));
		background-image: linear-gradient(-36deg, #e4e4e4 0%, #f8f8f8 100%);
		padding: 53rpx 31rpx;

		.ui-form {
			margin-top: 20rpx;
			padding: 0 20rpx;

			.ui-input {
				margin: 34rpx 0 60rpx 0;
				border-bottom: 1px solid #e2e2e2;
			}

			.ui-bot {
				display: flex;
				align-items: center;
				justify-content: space-between;
				font-size: 26rpx;
				color: #484848;
			}

			.ui-mini {
				width: 160rpx;
				// height: 60rpx;
			}

			.wd-btn-gloup {
				margin: 120rpx 0;
			}

			.ui-select {
				width: 506rpx;
				border-radius: 20rpx;
				// background-color: #fff;
			}

			.ui-btn {
				margin-top: 100rpx;
			}
		}

		.wd-btn-gloup {
			text-align: center;
			// margin-top: 70rpx;

			button {
				width: 237rpx;
				height: 71rpx;
				font-size: 28rpx;
				color: #ffffff;
			}
		}
	}
</style>
