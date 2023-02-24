<template>
	<app-body>
		<view id="share">
			<view class="ui-user">
				<template>
					<text>{{shareList.length ? '已分享给' : '暂无分享信息'}}</text>
					<view>
						<view class="ui-user-item" v-for="(item, idx) of shareList" :key="idx">
							<u--text prefixIcon="account-fill" :iconStyle="{ color: '#fff', fontSize: '36rpx' }"
								color="#fff" :text="item.phone + (item.regFlag === '0' ? '(未注册)' : '') || '未注册用户'">
							</u--text>
							<u-icon @click="remShare(item)" class="active" name="close-circle" color="#fff600"
								size="36rpx">
							</u-icon>
						</view>
					</view>
				</template>
			</view>
			<view class="ui-form">
				<view class="ui-form-item">
					<u-input maxlength="11" type="number" v-model="shareForm.phone" placeholder="请输入手机号码"
						:border="'none'" prefixIcon="../../static/images/login/phone.png"
						:prefixIconStyle="{height: '48rpx', width: '48rpx'}" fontSize="40rpx" clearable>
						<template slot="suffix">
							<u-icon @tap="getContact" name="/static/images/phone-book.png" size="24"></u-icon>
						</template>
					</u-input>
				</view>
				<view class="ui-form-item">
					<figure-code prefixIcon="../../static/images/login/code.png"
						:prefixIconStyle="{height: '48rpx', width: '48rpx'}" fontSize="40rpx" size="4" ref="graphicRef">
					</figure-code>
				</view>
				<view class="ui-form-item">
					<verify-code prefixIcon="../../static/images/login/sms.png"
						:prefixIconStyle="{height: '48rpx', width: '48rpx'}" fontSize="40rpx" ref="sms"
						@reset="codeReset" :payload="smsPayload" smsType="1" @checked="checkedBySms">
					</verify-code>
				</view>
				<view class="ui-btn"><button @click="submit" class="default">提交</button></view>
			</view>
		</view>
		<u-popup :closeable="true" :round="10" :show="show" mode="center" @close="close">
			<view class="ui-code"><canvas id="qrcode" canvas-id="qrcode"
					:style="{ width: `${size}px`, height: `${size}px` }"></canvas></view>
		</u-popup>
		<u-popup :round="10" :show="contactShow" mode="bottom" @close="contactShow = false">
			<view style="height: 1300rpx;">
				<contact-select placeholder="请输入联系人姓名" @cityClick="phoneClick" formatName="name"
					:obtainCitys="contactList" :isSearch="true"></contact-select>
			</view>
		</u-popup>
	</app-body>
</template>

<script>
	import {
		PostRemShareFamily,
		PostShareFamily,
		PostSharelist
	} from '../../common/http/api';
	import {
		phoneValidator,
		phoneRemove
	} from '../../common/utils/util';
	export default {
		data() {
			return {
				show: false,
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
				},
				contactList: [],
				contactShow: false,
				/**家庭名称**/
				name: ''
			}
		},
		onLoad({
			familyId,
			name
		}) {
			this.name = name;
			Object.assign(this.shareForm, {
				familyId
			});
			this.getShareList();
		},
		mounted() {},
		methods: {
			async submit() {
				if (!phoneValidator(this.shareForm.phone)) {
					return uni.$u.toast('请填写正确的手机号码')
				}
				if (this.shareForm.code.length !== 4) {
					return uni.$u.toast('请填写正确的验证码')
				}
				if (this.shareForm.smsUuid) {
					const res = await this.shareFamilys();
					this.getShareList();
					uni.showToast({
						icon: 'none',
						title: res.msg
					});
					this.init();

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
					content: `是否禁止会员${info.phone}共享${this.name || '家庭'}的所有设备`,
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

			/**
			 * 获取手机联系人
			 */
			getContact() {
				this.contactList = [];
				Promise.all([this.getTypeContact(plus.contacts.ADDRESSBOOK_PHONE), this.getTypeContact(plus.contacts
					.ADDRESSBOOK_SIM)]).then(res => {
					const data = [...res[0], ...res[1]];
					const obj = {};
					this.contactList = data.reduce(function(item, next) {
						obj[next.phone] ? '' : obj[next.phone] = true && item.push(next);
						return item;
					}, []);
					this.contactShow = true;
				})
			},
			/**
			 * 获取手机联系人
			 */
			getTypeContact(type) {
				return new Promise((resolve, reject) => {
					plus.contacts.getAddressBook(type, res => {

						res.find([], data => {

							const list = data.map(n => {
								const {
									displayName: name,
									phoneNumbers,
								} = n
								return {
									name,
									phone: phoneRemove(phoneNumbers[0]?.value || '')
								}
							}).filter(item => {
								return item.phone !== ''
							});

							console.log(list, '手机电话');
							resolve(list)
							// this.contactList.contacts(res)
							// this.contactShow = true
						});

					}, error => {
						reject()
						uni.showToast({
							title: '获取通讯录失败',
							duration: 2000
						})
					})
				})
			},
			/**
			 * 选择手机联系人
			 */

			phoneClick(item) {
				this.shareForm.phone = item.phone;
				console.log(item, '选择的手机号');
				console.log(this.shareForm, '界面表单');
				this.contactShow = false
			},

			/**
			 * 重置
			 */
			init() {
				// this.shareForm = {
				// 	phone: '',
				// 	code: '',
				// 	// familyId: '',
				// 	smsUuid: '',
				// 	familyPhone: ''
				// };
				this.shareForm.phone = ''
				this.shareForm.code = ''
				this.shareForm.smsUuid = ''
				this.familyPhone = ''
				this.$refs.sms.reset('验证码');
			}
		}
	};
</script>
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
		padding-bottom: 40rpx;

		.ui-form-item {
			padding: 44rpx 0;
			border-bottom: 1rpx solid #e2e2e2;
		}

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

		.ui-btn {
			margin: 30rpx 0;
		}
	}

	.ui-cont {
		width: 250rpx;
		height: 100rpx;
	}
</style>
