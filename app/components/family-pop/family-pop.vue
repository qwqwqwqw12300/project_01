<!--
* @Author: zhanch
* @Date: 2022-12-29 16:04:13
* @FilePath: /components/add-family/add-family.vue
* @Description: 添加家庭
-->

<template>
	<u-popup :closeable="true" :round="10" :show="show" mode="center" @close="close" @open="open">
		<view class="wd-add">
			<u-text :prefixIcon="mode === 'add' ? 'plus-circle' : 'edit-pen'"
				:iconStyle="{ fontSize: '38rpx', color: '#ea942f' }" color="#ea942f" size="30rpx"
				:text="mode === 'add' ? '新建家庭': '修改家庭'"></u-text>
			<view class="ui-input">
				<u-text size="28rpx" prefixIcon="home-fill" iconStyle="font-size: 40rpx" text="家庭名称"></u-text>
				<u-input v-model="form.familyName" placeholder="请输入家庭名称" maxlength="6" border="bottom" clearable>
				</u-input>
			</view>
			<view class="ui-input">
				<u-text size="28rpx" prefixIcon="map-fill" iconStyle="font-size: 36rpx" text="家庭地址(必填)"></u-text>
				<u--input v-model="form.address" placeholder="请输入家庭地址" border="bottom" clearable></u--input>
			</view>
			<view class="ui-btn"><button @click="next" class="default">{{ btnName }}</button></view>
		</view>
	</u-popup>
</template>

<script>
	import {
		PostAddFamily,
		PostEditFamily
	} from '@/common/http/api.js';
	export default {
		props: {
			btnName: {
				type: String,
				default: '确认'
			},
			/**修改模式**/
			mode: {
				type: String,
				/**add 添加家庭 edit 修改家庭**/
				default: 'add'
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
				this.form = {}
				this.show = false;
			},
			/**
			 * 打开家庭
			 */
			open(data = {}) {
				Object.assign(this.form, data);
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
				const handle = this.mode === 'add' ? PostAddFamily : PostEditFamily;
				handle({
					...this.form
				}).then(res => {
					uni.$u.toast(res.msg);
					this.close();
					setTimeout(() => {
						this.$emit('update', {
							familyId: res.data && res.data.familyId,
							name: familyName
						});
					}, 1000);
				})

			}
		}
	};
</script>

<style lang="scss">
	.wd-add {
		width: 582rpx;
		min-height: 300rpx;
		border-radius: 20rpx;
		filter: drop-shadow(0 0 5rpx rgba(7, 5, 5, 0.34));
		background-image: linear-gradient(-36deg, #e4e4e4 0%, #f8f8f8 100%);
		border: 1px solid #ffcb3d;
		padding: 53rpx 31rpx;

		&>view {
			margin-top: 52rpx;

			&.ui-input {
				margin-top: 40rpx;
				position: relative;
				padding: 10rpx 20rpx;

				&:nth-child(3) {
					margin-top: 20rpx;
				}

				&::after {
					bottom: 10rpx;
					left: 40rpx;
					content: '';
					width: 500rpx;
					height: 1rpx;
					background: #e4e4e4;
					position: absolute;
				}
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
