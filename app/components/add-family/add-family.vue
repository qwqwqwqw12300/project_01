<!--
* @Author: zhanch
* @Date: 2022-12-29 16:04:13
* @FilePath: /components/add-family/add-family.vue
* @Description: 添加家庭
-->

<template>
	<u-popup :closeable="true" :round="10" :show="show" mode="center" @close="close" @open="open">
		<view class="wd-add">
			<u-text prefixIcon="plus-circle" :iconStyle="{ fontSize: '38rpx', color: '#ea942f' }" color="#ea942f"
				size="30rpx" text="新建家庭"></u-text>
			<view>
				<u-text size="28rpx" prefixIcon="../../static/images/set-form.png" iconStyle="font-size: 25rpx"
					text="家庭名称"></u-text>
				<u--input v-model="form.familyName" placeholder="请输入家庭名称" border="bottom" clearable></u--input>
			</view>
			<view>
				<u-text size="28rpx" prefixIcon="../../static/images/set-form.png" iconStyle="font-size: 25rpx"
					text="家庭地址(必填)"></u-text>
				<u--input v-model="form.address" placeholder="请输入家庭地址" border="bottom" clearable></u--input>
			</view>
			<view class="ui-btn"><button @click="next" class="wd-sms">{{ btnName }}</button></view>
		</view>
	</u-popup>
</template>

<script>
	import {
		PostAddFamily,
	} from '@/common/http/api.js';
	export default {
		props: {
			btnName: {
				type: String,
				default: '下一步'
			}
		},
		data() {
			return {
				show: false,
				form: {
					familyName: '', //家庭名称
					address: '', //家庭地址
				}
			};
		},

		mounted(options) {},
		methods: {
			close() {
				this.show = false;
			},
			open() {
				this.show = true;
			},
			next() {
				const {
					familyName,
					address
				} = this.form
				if (!familyName) {
					return uni.$u.toast('请填写家庭名称')
				}
				if (!address) {
					return uni.$u.toast('请填写家庭地址')
				}
				PostAddFamily({
					...this.form
				}).then(res => {
					uni.$u.toast(res.msg);
					this.close();
					setTimeout(() => {
						this.$emit('update')
					}, 1000);
				})
			}
		}
	};
</script>

<style lang="scss">
	.wd-add {
		width: 582rpx;
		height: 606rpx;
		border-radius: 20rpx;
		filter: drop-shadow(0 0 5rpx rgba(7, 5, 5, 0.34));
		background-image: linear-gradient(-36deg, #e4e4e4 0%, #f8f8f8 100%);
		border: 1px solid #ffcb3d;
		padding: 53rpx 31rpx;

		&>view {
			margin-top: 52rpx;
			padding: 10rpx 20rpx;
			border-bottom: 1px solid #e4e4e4;

			&:nth-child(1),
			&:nth-child(4) {
				padding-left: 0rpx;
				border-bottom: none;
			}
		}

		.ui-btn {
			text-align: center;
			margin-top: 70rpx;

			button {
				width: 237rpx;
				height: 71rpx;
				font-size: 28rpx;
				color: #ffffff;
			}

			;

		}
	}
</style>
