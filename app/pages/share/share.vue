<template>
	<app-body :bodyStyle="{ backgroundPositionY: '-100rpx' }">
		<view id="share">
			<view class="ui-user">
				<template>
					<text>{{shareList.length ? '已分享给' : '暂无分享信息'}}</text>
					<view>
						<view class="ui-user-item" v-for="(item, idx) of shareList" :key="idx">
							<u--text prefixIcon="account-fill" :iconStyle="{ color: '#fff', fontSize: '36rpx' }"
								color="#fff" :text="item.phone || '未注册用户'"></u--text>
							<u-icon @click="remShare(item)" class="active" name="close-circle" color="#fff600"
								size="36rpx">
							</u-icon>
						</view>
					</view>
				</template>
			</view>
			<view class="ui-form">
				<view class="ui-form-item">
					<u-text prefixIcon="phone" iconStyle="font-size: 30rpx" text="分享人手机号码" color="#444" size="28rpx">
					</u-text>
					<view class="ui-input">
						<u-input length="11" type="number" v-model="shareForm.phone" placeholder="请输入手机号码"
							:border="'none'" fontSize="28rpx" clearable></u-input>
					</view>
				</view>
				<view class="ui-form-item">
					<graphic-input ref="graphicRef"></graphic-input>
				</view>
				<view class="ui-form-item">
					<sms-input ref="sms" @reset="codeReset" :payload="smsPayload" @checked="checkedBySms" />
				</view>
				<view class="wd-btn-gloup"><button @click="submit">提交</button></view>
			</view>
		</view>
		<u-popup :closeable="true" :round="10" :show="show" mode="center" @close="close">
			<view class="ui-code"><canvas id="qrcode" canvas-id="qrcode"
					:style="{ width: `${size}px`, height: `${size}px` }"></canvas></view>
		</u-popup>
	</app-body>
</template>

<script>
	import UQRCode from '@/uni_modules/Sansnn-uQRCode/js_sdk/uqrcode/uqrcode.js';
	import {
		PostRemShareFamily,
		PostShareFamily,
		PostSharelist
	} from '../../common/http/api';
	export default {
		data() {
			return {
				show: false,
				text: 'uQRCode',
				size: 200,
				/**已分享家庭列表**/
				shareList: [],
				/**分享请求参数**/
				shareForm: {
					phone: '',
					code: '',
					familyId: '',
					smsUuid: '',
					familyPhone: ''
				}
			}
		},
		onLoad: function({
			familyId,
			phone
		}) {
			Object.assign(this.shareForm, {
				familyId
			});
			this.getShareList();
		},
		methods: {
			async submit() {
				if (this.shareForm.smsUuid) {
					const res = await this.shareFamilys();
					this.getShareList();
					uni.showToast({
						icon: 'none',
						title: '分享成功'
					});

				} else {
					uni.showToast({
						icon: 'none',
						title: '请先填写页面信息'
					})
				}

			},

			close() {
				this.show = false;
			},
			/**
			 * 获取分享列表
			 */
			getShareList() {
				PostSharelist({
					familyId: this.shareForm.familyId
				}).then(res => {
					this.shareList = res.data;
				});
			},

			/**
			 * 删除家庭
			 */
			remShare(info) {
				uni.showModal({
					title: '提示',
					content: '是否确认删除设备',
					success: res => {
						if (res.confirm) {
							const {
								familyId,
								memberId
							} = info;
							PostRemShareFamily({
								shareFamilyId: familyId,
								shareMemberId: memberId
							}).then(res => {
								uni.showToast({
									icon: 'none',
									title: '删除成功'
								});
								this.getShareList();
							});
						} else if (res.cancel) {
							console.log('用户点击取消');
						}
					}
				});

			},

			/**
			 * 创建家庭
			 */
			shareFamilys() {
				return new Promise(resolve => {
					PostShareFamily({
						...this.shareForm
					}).then(res => {
						resolve(res);
					}, err => {
						this.shareForm.smsUuid = '';
						this.$refs.sms.reset();
					});
				});
			},

			/**
			 * 获取短信请求参数
			 */
			smsPayload() {
				const {
					code,
					uuid
				} = this.$refs.graphicRef.returnCodeData();
				return {
					uuid,
					captcha: code,
					phone: this.$store.state.userInfo.phone
				};
			},

			/**
			 * 短信认证通过
			 */
			checkedBySms(smsInfo) {
				Object.assign(this.shareForm, {
					code: smsInfo.code,
					smsUuid: smsInfo.uuid
				});
			},

			/**
			 * 重置验证码
			 */
			codeReset() {
				this.$refs.graphicRef.handleGetCaptcha();
			},
		}
	};
</script>
r
<style lang="scss">
	#share {
		text-align: center;
	}

	.ui-code {
		height: 600rpx;
		width: 500rpx;
		padding: 50rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.ui-user {
		margin-top: 90rpx;
		box-sizing: border-box;
		padding: 25rpx 47rpx;
		display: inline-block;
		width: 684rpx;
		min-height: 200rpx;
		border-radius: 40rpx;
		background-color: rgba(0, 0, 0, 0.4);
		color: #fff;
		text-align: left;
		font-size: 30rpx;

		.ui-user-item {
			margin: 10rpx 20rpx 0 0;
			display: inline-flex;
			align-items: center;

			&>*:nth-child(2) {
				margin-left: 10rpx;
				margin-top: 10rpx;
			}
		}
	}

	.ui-form {
		margin-top: 156rpx;
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
	}
</style>
