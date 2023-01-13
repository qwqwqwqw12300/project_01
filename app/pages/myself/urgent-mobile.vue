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
							<u-icon @tap="handleDel(item.memberContactsId)" name="close-circle" size="33" color="#000" />
						</view>
					</view>
				</view>
			</u-list-item>

		</u-list>
		<view class="ui-btn">
			<button @click="handleAdd">添加紧急联系人</button>
		</view>
	</app-body>
</template>

<script>
	import {
		GetContactsList,
		PostDelContacts,
	} from '@/common/http/api.js';
	export default {
		data() {
			return {
				contactList: [],
				orderNumDict: {
					0: '第一紧急联系人',
					1: '第二紧急联系人',
					2: '第三紧急联系人',
				}
			}
		},
		methods: {
			handleAdd() {
				uni.navigateTo({
					url: '/pages/myself/add-contact?type=add'
				})
			},
			handleDel(memberContactsId) {
				PostDelContacts({
					memberContactsId,
				}).then(res => {
					console.log(res, 'uu')
					uni.$u.toast(res.msg)
					this.handleInit()
				})
			},
			handleEidt(item){
				uni.navigateTo({
					url: '/pages/myself/add-contact?type=edit&data=' + encodeURIComponent(JSON.stringify(item))
				})
			},
			handleInit() {
				GetContactsList({}).then(res => {
					this.contactList = res.rows.map(n => {
						n.contactName = this.orderNumDict[n.orderNum]
						return n
					})
				})
			}
		},
		mounted() {
			this.handleInit()
		}
	}
</script>

<style lang="scss">
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
</style>
