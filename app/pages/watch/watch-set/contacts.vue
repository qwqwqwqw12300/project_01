<template>
	<app-body :bodyStyle="{background: 'rgb(247,247,247)' }" :bg="false">
		<view class="ui-navbar">
			<view class="header">
				<text class="title">常用联系人</text>
				<view class="action" @click="handleAdd">
					<u-icon name="/static/images/add-contact.png" size="44rpx" style="margin-right: 6rpx;" />
					添加
				</view>
				<view class="action" @click="openTelBooks">
					<u-icon name="/static/images/tel-book.png" size="44rpx" style="margin-right: 6rpx;" />
					通讯录
				</view>
			</view>
			<view style="margin-top: 48rpx;">
				<u--input style="background-color:#F2F2F2;" placeholder="请输入您想搜索的内容" border="surround" shape="circle"
					prefixIcon="search" prefixIconStyle="font-size: 22px;color: #909399"></u--input>
			</view>
		</view>
		<view class="ui-content">
			<u-swipe-action>
				<u-swipe-action-item :options="item.options" v-for="(item, index) in options4" :key="index"
					@click="handleDel(index,item)">
					<view class="cell">
						<view class="cell-box">
							<view class="input">
								<u--input v-model="item.name" maxlength="6" placeholder="请输入姓名" border="none" clearable>
								</u--input>
							</view>
							<view class="input">
								<u--input v-model="item.number" maxlength="11" type="number" placeholder="请输入手机号"
									border="none" clearable>
								</u--input>
							</view>
						</view>
					</view>
				</u-swipe-action-item>
			</u-swipe-action>
		</view>
		<view class="ui-div"></view>
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
		<tel-books ref="telBookRefs" @select="phoneSelect"></tel-books>
	</app-body>
</template>

<script>
	import {
		GetWatchAddressBook,
		PostWatchContactsSync,
		PostWatchDeleteAddressBook
	} from '@/common/http/api';
	import {
		phoneValidator
	} from '@/common/utils/util';
	import {
		isApp
	} from '@/common/utils/util.js';
	import {
		mapState,
	} from 'vuex';
	export default {
		computed: {
			...mapState({
				deviceInfo: state => state.deviceInfo
			}),

		},
		data() {
			return {
				options4: [],
			}
		},
		methods: {
			//初始化数据
			initData() {
				this.options4 = []
				GetWatchAddressBook({
					deviceId: this.deviceInfo.deviceId
				}).then(res => {
					this.options4 = res.data.map(n => {
						n.options = [{
							text: '删除',
							style: {
								backgroundColor: '#f56c6c'
							}
						}]
						return n
					})
				})
			},
			handleCancel() {
				uni.navigateBack()
			},
			//批量保存和修改
			handleSave() {
				const list = uni.$u.deepClone(this.options4)
				for (let i = 0; i < list.length; i++) {
					if (!list[i].name) return uni.$u.toast('请填写联系人姓名')
					if (!phoneValidator(list[i].number)) return uni.$u.toast('手机号不正确')
				}
				const addressBooks = []
				this.options4.forEach(item => {
					console.log(item, 'item')
					if (item.addressBookId != undefined) {
						addressBooks.push({
							name: item.name,
							number: item.number,
							addressBookId: item.addressBookId,
							sos: item.sos
						})
					} else {
						addressBooks.push({
							name: item.name,
							number: item.number,
							sos: false
						})
					}
				})
				PostWatchContactsSync({
					deviceId: this.deviceInfo.deviceId,
					watchSyncList: addressBooks
				}).then(res => {
					console.log(res, 'res')
					uni.$u.toast(res.msg)
					setTimeout(() => {
						this.initData()
					}, 1000);
					// setTimeout(() => {
					// 	uni.navigateBack()
					// }, 1000);
				})
			},
			//添加输入框
			handleAdd() {
				this.options4.push({
					phoneName: '',
					phoneNumber: '',
					options: [{
						text: '删除',
						style: {
							backgroundColor: '#f56c6c'
						}
					}],
				})
			},
			//单个删除
			handleDel(id, list) {
				uni.showModal({
					title: '提示',
					content: '是否确认删除？',
					success: res => {
						console.log(res, 'res')
						if (res.confirm) {
							if (list.addressBookId != undefined) {
								PostWatchDeleteAddressBook({
									deviceId: this.deviceInfo.deviceId,
									addressBookId: list.addressBookId,
									phoneNumber: list.phoneNumber
								}).then(res => {
									uni.$u.toast(res.msg)
									setTimeout(() => {
										this.initData()
									}, 1000);
								})
							} else {
								this.options4.splice(this.options4.findIndex((item, index) => index == id), 1)
							}
						}
					}
				});
			},
			openTelBooks() {
				if (isApp()) {
					this.$refs.telBookRefs.show(false)
				}
			},
			//通讯录导入
			phoneSelect(data) {
				// console.log(data)
				data.map(item => {
					this.options4.push({
						phoneName: item.name,
						phoneNumber: item.phone,
						options: [{
							text: '删除',
							style: {
								backgroundColor: '#f56c6c'
							}
						}],
					})
				})
			}
		},
		onShow() {
			this.initData()
		}

	}
</script>

<style lang="scss" scoped>
	.ui-navbar {
		padding: 32rpx;
		background-color: #fff;

		.header {
			height: 60rpx;
			display: flex;
			justify-content: space-between;
			align-items: center;

			.title {
				font-size: 50rpx;
				color: #353535;
				font-weight: 600;
				margin-right: 40rpx;
				// flex: 1;
			}

			.action {
				// margin-left: 20rpx;
				display: flex;
				align-items: center;
			}
		}
	}

	.ui-content {
		margin-top: 30rpx;
		background-color: #fff;

		.cell {
			padding: 0rpx 32rpx;

			.cell-box {
				height: 128rpx;
				border-bottom: solid 2px #f7f7f7;
				display: flex;
				align-items: center;
				justify-content: space-between;

				.input {
					width: 240rpx;
				}
			}
		}
	}

	.ui-div {
		height: 90rpx;
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
