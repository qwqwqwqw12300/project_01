<!-- 增加手表设备 -->
<template>
	<app-body :bg="false" :bodyStyle="{backgroundColor:'#FFF'}">
		<app-logo color="#353535" text="绑定4G手表"></app-logo>
		<view class="ui-scan">
			<view class="scan-box">
				<view class="box-bg" @click="handleScan">
					<image class="img" src="@/static/images/camera.png"></image>
				</view>
			</view>
			<view class="scan-tip">
				<text>请扫描手表-设置-绑定二维码进行绑定</text>
			</view>
		</view>
		<view class="ui-code">
			<view class="code-box">
				<text class="label">设备IMEI码</text>
				<view class="input">
					<u--input v-model="cardInfo.deviceNo" type="number" placeholder="请输入设备IMEI码" border="none"
						clearable>
					</u--input>
				</view>
			</view>
		</view>
		<view class="ui-code">
			<view class="code-box">
				<text class="label">设备名称</text>
				<view class="input">
					<u--input v-model="cardInfo.deviceName" maxlength="6" placeholder="请输入设备名称" border="none" clearable>
					</u--input>
				</view>
			</view>
		</view>
		<view class="ui-btn">
			<button @click="handleSubmit" class="default">立即绑定</button>
		</view>
	</app-body>
</template>

<script>
	import {
		PostcreDevice
	} from '@/common/http/api';
	export default {
		data() {
			return {
				cardInfo: {
					deviceNo: '',
					deviceName: '',
					deviceType: ''
				}
			}
		},
		onLoad(options) {
			console.log(options)
			if(options && options.type) this.cardInfo.deviceType = options.type
		},
		methods: {
			handleScan() {
				uni.scanCode({
					autoDecodeCharset: true,
					// scanType: ['barCode', 'qrCode'],
					success: res => {
						/* h102手表下 */
						if(this.cardInfo.deviceType === '4') {
							let strData = JSON.stringify(res.result).replace(/u0000/g, '').replace(/\\/g, '').replace(/}/, '').replace(/{"dev_info":/, '')
							let str = strData.substr(0, strData.length - 1).substr(1, strData.length - 1)
							let objData = JSON.parse(str)
							this.cardInfo.deviceNo = objData.imei
							this.cardInfo.modeNo = objData.model
							this.cardInfo.version = objData.version
							return
						}
						const result = JSON.parse(res.result)
						this.cardInfo.deviceNo = objData.imei
					},
					false: res => {
						console.log(res, '00000000000000000000000')
					}
				});
			},
			handleSubmit() {
				const {
					deviceNo,
					deviceName,
					modeNo,
					version
				} = this.cardInfo 
				if (!deviceNo) return uni.$u.toast('设备码不能为空')
				if (!deviceName) return uni.$u.toast('设备名称不能为空')
				if (!modeNo) return uni.$u.toast('modeNo不能为空')
				if (!version) return uni.$u.toast('version不能为空')
				PostcreDevice({
					...this.cardInfo
				}).then(res => {
					uni.$u.toast(res.msg)
					setTimeout(() => {
						uni.navigateBack()
					}, 1000)
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.ui-scan {
		margin-top: 80rpx;
		padding: 0 32rpx;
		box-sizing: border-box;

		.scan-box {
			width: 360rpx;
			height: 360rpx;
			margin: 0 auto;
			background: #F2F2F2;
			border-radius: 16px;
			box-sizing: border-box;
			padding: 20rpx;

			.box-bg {
				width: 100%;
				height: 100%;
				background-image: url('@/static/images/QRcode.svg');
				background-size: 100% 100%;
				display: flex;
				align-items: center;
				justify-content: center;

				.img {
					width: 100rpx;
					height: 100rpx;
				}
			}
		}

		.scan-tip {
			margin-top: 26rpx;
			text-align: center;

			text {
				font-size: 24rpx;
				color: #888888;
			}
		}
	}

	.ui-code {
		margin-top: 30rpx;
		padding: 0 32rpx;

		.code-box {
			height: 128rpx;
			display: flex;
			align-items: center;
			justify-content: space-between;
			border-bottom: solid 2px #f7f7f7;

			.label {
				font-size: 34rpx;
				color: #353535;
			}

			.input {
				width: 300rpx;
			}
		}
	}

	.ui-btn {
		padding: 0 32rpx;
		margin-top: 100rpx;
	}
</style>