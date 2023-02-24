<!-- 隐私协议 -->
<template>
	<app-body :bg="false">
		<view class="ui-body">
			<app-logo :text="title"></app-logo>
			<view class="uni-agreement" v-html="content">
			</view>
		</view>

	</app-body>
</template>

<script>
	import {
		// 普通协议
		PostSelectAgreement,
		// 用户协议
		PostUserAgreement
	} from '@/common/http/api.js';
	export default {
		data() {
			return {
				content: '',
				title: ''
			}
		},
		methods: {
			handleInit(type) {
				let agreementHandle;
				if (type) { // 普通协议
					agreementHandle = PostSelectAgreement({
						type
					});
				} else { // 用户协议
					agreementHandle = PostUserAgreement({});
				}
				agreementHandle.then(res => {
					this.content = res.data.content
				})
			}
		},
		onLoad(option) {
			const {
				type
			} = option;
			switch (type) {
				case '0':
					this.title = '隐私协议'
					break;
				case '1':
					this.title = 'APP协议'
					break;
				default:
					this.title = '用户协议'
					break;
			}
			this.handleInit(type)
		}
	}
</script>

<style lang="scss" scoped>
	.ui-body {
		margin-top: 20rpx;
		background: #fff;
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

	.uni-agreement {
		margin-top: 30rpx;
		padding: 10px 32rpx;
		box-sizing: content-box;
		word-wrap: break-word;
		word-break: normal;
		text-indent: 2em;

	}
</style>
