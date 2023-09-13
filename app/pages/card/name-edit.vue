<!-- 修改设备名称 -->
<template>
	<app-body :bg="false" title="修改设备名称">
		<view class="ui-logo">
			<!-- <app-logo color="#353535" text="修改设备名称"></app-logo> -->
		</view>
		<view class="ui-form">
			<u-form>
				<u-form-item label="设备名称" borderBottom labelWidth="100">
					<u-input v-model="deviceName" maxlength="8" border="none" placeholder="请输入" clearable />
				</u-form-item>
				<u-form-item>
					<u-button class="ui-button default" type="primary" text="提交" @click="handleSumbit"></u-button>
				</u-form-item>
			</u-form>
		</view>
	</app-body>
</template>

<script>
	import {
		mapState,
	} from 'vuex';
	import {
		PosteditDevice
	} from '@/common/http/api.js';
	export default {
		data() {
			return {
				deviceName: '',
			}
		},
		computed: {
			...mapState({
				deviceInfo: state => state.deviceInfo
			}, ),
		},
		mounted() {
			this.deviceName = this.deviceInfo.name
		},
		methods: {
			handleSumbit() {
				if (!this.deviceName) return uni.$u.toast('请填写设备名称');
				PosteditDevice({
					deviceName: this.deviceName,
					deviceId: this.deviceInfo.deviceId
				}).then(res => {
					uni.$u.toast(res.msg)
					this.$store.dispatch('updateDevacesInfo');
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
		margin-top: 20rpx;
		background: #ffffff;
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