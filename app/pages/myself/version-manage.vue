<template>
	<app-body>
		<app-logo color="#353535" text="关于软件"></app-logo>
		<view class="ui-main">
			<view>
				<image class="ui-main-image" src="@/static/images/H&V@3x.png" mode=""></image>
			</view>
			<view>
				<image class="ui-main-image" src="@/static/images/ELINKER@3x.png" mode=""></image>
			</view>

			<view class="ui-main-button">
				<button class="ui-button default" @click="handleUpdate">检查更新</button>
			</view>
		</view>
	</app-body>
</template>

<script>
	import {
		PostVersionInfo,
	} from '@/common/http/api.js';
	import {
		versionCompare,
		isIos,
		isApp
	} from '../../common/utils/util';
	export default {
		data() {
			return {
				appVersion: ''
			}
		},
		methods: {
			handleQuery() {
				plus.runtime.getProperty(plus.runtime.appid, (info) => {
					this.appVersion = info.version
				})
				// PostVersionInfo({
				// 	versionType: isIos ? '0' : '1',
				// }).then(res => {
				// 	this.version = res.data.content
				// })
			},
			handleUpdate() {
				PostVersionInfo({
					versionType: isIos ? '0' : '1',
				}).then(res => {
					const curVersion = res.data.content
					const result = versionCompare(this.appVersion, curVersion)
					if (!result) {
						uni.showModal({
							title: '',
							content: `发现新版本${curVersion}、是否更新？`,
							success: res => {
								if (res.confirm) {
									let appurl = "https://sj.qq.com/"
									plus.runtime.openURL(appurl)
								}
							}
						});
					} else {
						uni.showModal({
							title: '',
							content: '已经是最新版本了',
						});
					}
				})
			}
		},
		mounted() {
			this.handleQuery()
		}
	}
</script>

<style lang="scss" scoped>
	.ui-main {
		height: 1000rpx;
		display: flex;
		flex-direction: column;
		align-items: center;
		padding-top: 150rpx;
		// justify-content: center;
		.ui-main-image{
			width: 390rpx;
			height: 388rpx;
		}

		.ui-main-button {
			margin-top: 100px;
			width: 600rpx;

			.ui-button {
				width: 100%;
				height: 80rpx;
				line-height: 80rpx;
			}
		}
	}
</style>
