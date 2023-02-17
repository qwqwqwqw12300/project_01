<!-- 添加联系人 -->
<template>
	<app-body :bg="false">
		<view class="ui-logo">
			<app-logo color="#353535" text="紧急联系人设置"></app-logo>
		</view>
		<view class="ui-form" v-for="item in contactList" :key="item.orderNum">
			<view class="ui-form-title">
				<view class="title-text">
					<text />{{ item.orderName }}
				</view>
			</view>
			<view class="ui-form-item">
				<view class="title">
					姓名
				</view>
				<u-input v-model="item.name" placeholder="输入紧急联系人名字" :border="'none'" fontSize="28rpx" clearable
					inputAlign="right" cursor="0" />
			</view>
			<view class="ui-form-item">
				<view class="title">
					电话号码
				</view>
				<u-input v-model="item.phone" maxlength="11" type="number" placeholder="输入紧急联系手机号码" :border="'none'"
					fontSize="28rpx" clearable inputAlign="right" />
			</view>
		</view>
		<view class="ui-btn">
			<button @click="handleSubmit" class="default">确定</button>
		</view>

	</app-body>
</template>

<script>
	import {
		GetContactsList,
		PostSetContacts,
	} from '@/common/http/api.js';
	import {
		phoneValidator
	} from '../../common/utils/util';
	// import {
	// 	phoneValidator
	// } from '../../common/utils/util';
	export default {
		data() {

			return {
				contactList: [{
						orderNum: 1,
						orderName: '第一紧急联系人'
					},
					{
						orderNum: 2,
						orderName: '第二紧急联系人'
					},
					{
						orderNum: 3,
						orderName: '第三紧急联系人'
					}
				],
			}
		},
		methods: {
			handleInit() {
				GetContactsList({}).then(res => {
					console.log(res, 'res')
					this.contactList = this.contactList.map(item => {
						const data = res.rows.find(n => {
							return n.orderNum === item.orderNum
						})
						return data ? {
							...data,
							orderName: item.orderName
						} : item
					})
					console.log(this.contactList, 'llllllllll')
				})
			},
			handleSubmit() {
				const list = uni.$u.deepClone(this.contactList)
				for (let i = 0; i < list.length; i++) {
					if (list[i].name || list[i].phone) {
						if (!list[i].name) return uni.$u.toast(`${list[i].orderName}姓名不能为空`)
						if (!phoneValidator(list[i].phone)) return uni.$u.toast(`${list[i].orderName}手机号不正确`)
					}
				}
				PostSetContacts(this.contactList).then(res => {
					uni.$u.toast(res.msg)
					this.$store.dispatch('GetContactsList')
					setTimeout(() => {
						this.handleInit()
					}, 500)
				})
				console.log(this.contactList, '9999')
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
		padding: 0 44rpx;
		margin-bottom: 20rpx;
		background-color: #ffffff;

		.ui-form-title {
			display: flex;
			align-items: center;
			min-height: 120rpx;
			border-bottom: solid 2px #f9f9f9;

			.title-text {
				height: 100%;
				color: #FEAE43;
				font-size: 44rpx;
				font-weight: 550;

				text {
					border-left: solid 14rpx #FEAE43;
					border-radius: 3rpx;
					margin-right: 16rpx;
				}
			}
		}

		.ui-form-item {
			display: flex;
			align-items: center;
			justify-content: space-between;
			min-height: 130rpx;
			border-bottom: solid 2px #f9f9f9;

			.title {
				font-size: 34rpx;
				color: #353535;
				font-weight: 400;
				width: 180rpx;
			}
		}
	}

	.ui-btn {
		padding: 44rpx;
	}
</style>
