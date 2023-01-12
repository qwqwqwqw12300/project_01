<template>
	<app-body>
		<view class="ui-title">
			<text>{{ type === 'add'? '添加': '修改' }}联系人电话</text>
		</view>
		<view class="ui-form">
			<view class="ui-form-item">
				<u-text prefixIcon="/static/images/order.png" iconStyle="font-size: 32rpx" text="选择优先级" color="#444"
					size="28rpx"></u-text>
				<view class="ui-input">
					<uni-data-select v-model="form.orderNum" :clear="false" :localdata="range"></uni-data-select>
				</view>
			</view>
			<view class="ui-form-item">
				<u-text prefixIcon="account" iconStyle="font-size: 40rpx" text="联系人姓名" color="#444" size="28rpx">
				</u-text>
				<view class="ui-input">
					<u-input v-model="form.phoneName" placeholder="输入紧急联系人名字" :border="'none'" fontSize="28rpx"
						clearable>
					</u-input>
				</view>
			</view>
			<view class="ui-form-item">
				<u-text prefixIcon="/static/images/tel.png" :iconStyle="{width: '30rpx',height:'40rpx'}" text="手机号码"
					color="#444" size="28rpx"></u-text>
				<view class="ui-input">
					<u-input v-model="form.phone" placeholder="输入紧急联系手机号码" :border="'none'" fontSize="28rpx" clearable>
					</u-input>
				</view>
			</view>
			<view class="ui-btn">
				<button @click="handleSubmit">提交</button>
			</view>
		</view>
	</app-body>
</template>

<script>
	import {
		PostAddContacts,
		PostEditContacts,
	} from '@/common/http/api.js';
	const interList = {
		edit: PostEditContacts,
		add: PostAddContacts,
	}
	export default {
		data() {
			const range = [{
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
				form: {
					orderNum: '',
					phone: '',
					phoneName: '',
					memberContactsId: '',
				},
				type: 'add',
				range,

			}
		},
		methods: {
			handleSubmit() {
				interList[this.type]({
					...this.form
				}).then(res => {
					uni.$u.toast(res.msg)
					this.handleBack()
				})
			},
			handleBack() {
				let pages = getCurrentPages(); // 当前页面
				const prevPage = pages[pages.length - 2]; //上一页页面实例
				if (typeof(prevPage) == "undefined") {
					//没上一页面
					return;
				}
				//把数据返回给上一个页面
				prevPage.$vm.handleInit(); //上一页面的刷新方法
				setTimeout(() => {
					uni.navigateBack()
				}, 1000)
			}
		},
		onLoad(e) {
			this.type = e.type
			if (this.type === 'edit') {
				const {
					phone,
					name,
					memberContactsId,
					orderNum
				} = JSON.parse(decodeURIComponent(e.data))
				this.form.phone = phone
				this.form.phoneName = name
				this.form.orderNum = orderNum + ''
				this.form.memberContactsId = memberContactsId
			}

		}
	}
</script>

<style lang="scss" scoped>
	::v-deep {
		.uni-select {
			border: none;
		}
	}

	.ui-title {
		margin-top: 10rpx;
		padding-left: 40rpx;

		text {
			font-size: 72rpx;
			font-weight: bold;
			color: #ffffff;
		}
	}

	.ui-form {
		margin-top: 356rpx;
		padding: 0 80rpx;
		padding-bottom: 120rpx;

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
</style>
