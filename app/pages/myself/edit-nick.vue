<template>
	<app-body :bg="false">
		<view class="ui-logo">
			<app-logo color="#353535" text="编辑个人资料"></app-logo>
		</view>
		<view class="ui-form">
			<u-form>
				<u-form-item label="会员昵称" borderBottom labelWidth="100">
					<u-input v-model="nickname" border="none" placeholder="请输入" />
				</u-form-item>
				<u-form-item>
					<u-button class="ui-button" type="primary" text="提交" @click="handleSumbit"></u-button>
				</u-form-item>
			</u-form>
		</view>
	</app-body>
</template>

<script>
	import {
		PostUpdateNick,
	} from '@/common/http/api.js';
	export default {
		data() {
			return {
				nickname: '',
			}
		},
		mounted() {
			this.nickname = this.$store.getters.userInfo.nickname
		},
		methods: {
			handleSumbit() {
				if (!this.nickname) return uni.$u.toast('请填写会员昵称');
				PostUpdateNick({
					nickname: this.nickname
				}).then(res => {
					uni.$u.toast(res.msg)
					this.$store.dispatch('getPushMsgState')
					setTimeout(() => {
						uni.navigateBack()
					}, 1000)
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.ui-logo {
		background: #ffffff;
		margin-top: 60rpx;
		padding-bottom: 120rpx;
	}

	.ui-form {
		padding: 30rpx;
		background: #ffffff;

		.ui-button {
			margin-top: 40rpx;
		}
	}
</style>
