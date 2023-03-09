<template>
	<app-body :bg="false">
		<view class="ui-logo">
			<app-logo color="#353535" text="编辑个人资料"></app-logo>
		</view>
		<view class="ui-form">
			<u-form>
				<u-form-item label="上传头像" borderBottom labelWidth="100">
					<u-upload :fileList="fileList" @afterRead="afterRead" @delete="deletePic" name="1" multiple
						:maxCount="1" accept="image"></u-upload>
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
		PostUpdateAvatar,
	} from '@/common/http/api.js';
	export default {
		data() {
			return {
				fileList: [],
			}
		},

		methods: {
			afterRead(info) {
				const {
					url,
					size,
					name
				} = info.file[0]
				const fileType = name.substring(name.lastIndexOf("."))
				if (!['.png', '.jpeg', '.jpg'].includes(fileType)) return uni.$u.toast('不支持该格式图片')
				if (size / 1024 / 1024 > 5) return uni.$u.toast('图片大小不能超过5M')
				console.log(fileType)
				let fileList = [...info.file]
				fileList = fileList.slice(-1)
				this.fileList = fileList
			},
			deletePic(info) {
				this.fileList = []
			},
			handleSumbit() {
				if (!this.fileList.length) return uni.$u.toast('请上传图片')
				// let formData = new FormData()
				// formData.append('avatarfile', this.fileList[0].url)
				PostUpdateAvatar({
					path: this.fileList[0].url,
					name: 'avatarfile',
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
