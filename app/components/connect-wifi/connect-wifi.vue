<!--
* @Author: 连接wifi
* @Date: 2023-01-28 16:38:01
* @FilePath: /components/connect-wifi/connect-wifi.vue
* @Description: In User Settings Edit
-->

<template>
	<u-popup :closeable="true" :round="10" :show="show" mode="center" @close="back">
		<view class="wd-add">
			<view class="wd-title">
				<image src="../../static/images/wifi.png" mode=""></image>
				<text>选择网络</text>
			</view>
			<!-- <u-text prefixIcon="setting" :iconStyle="{ fontSize: '38rpx', color: '#ea942f' }" color="#ea942f"
				size="30rpx" text="选择网络"></u-text> -->
			<view class="ui-add-box">
				<view class="ui-list" v-if="step === 'list'">
					<view v-for="(item, index) in list" :key="index" @tap="handleSelect(item)" class="ui-list-item"
						:class="{'active': item.ssid === info.wifi.ssid}">
						<view class="tick">
							<image v-show="item.ssid === info.wifi.ssid" class="tick"
								src="../../static/images/tick.png" />
						</view>
						<text class="text">{{item.ssid}}</text>
						<u-icon name="wifi" size="40rpx"></u-icon>

					</view>
				</view>
				<view class="ui-pwd" v-else>
					<!-- 		<u-text size="28rpx" prefixIcon="lock" iconStyle="font-size: 36rpx" text="密码">
					</u-text> -->
					<u-input border="surround" prefixIcon="lock" :prefixIconStyle="{fontSize: '48rpx'}" fontSize="40rpx"
						v-model="info.pwd" :type="eyes ? 'password': 'text'" placeholder="请输入wifi密码" clearable>
						<template slot="suffix">
							<u-icon :name="eyes ? 'eye-off' : 'eye-fill'" @click="eyesChange" color="rgb(144, 147, 153)"
								size="48rpx"></u-icon>
						</template>
					</u-input>
				</view>
			</view>
			<view class="wd-btn-group">
				<button @click="back" class="cancel" v-if="step !== 'list'">返回</button>
				<button @click="next" class="default">确定</button>
			</view>
		</view>
	</u-popup>
</template>

<script>
	export default {
		props: {
			list: Array
		},
		data() {
			return {
				show: true,
				/**密码**/
				info: {
					pwd: '',
					wifi: {}
				},
				/**当前步骤**/
				step: 'list',
				/**密码显隐**/
				eyes: true
			};
		},
		created() {},
		methods: {
			close() {
				this.show = false;
			},

			/**
			 * 点击密码眼睛
			 * @param {Object} 
			 */
			eyesChange() {
				this.eyes = !this.eyes;
			},

			open() {
				this.show = true;
				this.$nextTick(() => {
					this.info.wifi = this.list[0] || {};
				});
				console.log(this.info.wifi, 'this.info.wifi');
			},

			/**
			 * 选择wifi
			 */
			handleSelect(item) {
				this.info.wifi = item;
			},

			next() {
				if (this.step === 'list') {
					this.step = 'pwd';
				} else {
					if (this.info.pwd) {
						if (!this.info.wifi.ssid) {
							this.info.wifi = this.list[0];
						}
						this.$emit('confirm', this.info);
						this.close();
					} else {
						uni.showToast({
							icon: 'none',
							title: '请输入wifi密码'
						})
					}

				}
			},

			back() {
				if (this.step === 'pwd') {
					this.step = 'list';
				} else {
					this.$emit('close');
					this.close();
				}
			}
		}
	}
</script>

<style lang="scss">
	.wd-add {
		width: 560rpx;
		min-height: 500rpx;
		background: #FFFFFF;
		border-radius: 16px;
		// filter: drop-shadow(0 0 5rpx rgba(7, 5, 5, 0.34));
		// background-image: linear-gradient(-36deg, #e4e4e4 0%, #f8f8f8 100%);
		padding: 29rpx 32rpx;

		.wd-title {
			display: flex;
			flex-direction: column;
			align-items: center;
			justify-content: center;
			font-size: 32rpx;
			color: #353535;
			margin-bottom: 40rpx;

			image {
				margin-bottom: 10rpx;
				height: 72rpx;
				width: 72rpx;
			}
		}

		.ui-list {
			padding: 0 32rpx;
			display: flex;
			flex-direction: column;
			width: 100%;
			box-sizing: border-box;
			height: 500rpx;
			overflow-y: scroll;

			.ui-list-item {
				// margin-left: 80rpx;
				padding: 24rpx 0;
				display: flex;
				align-items: center;
				justify-content: space-between;
				border-bottom: 1rpx solid #e2e2e2;

				.text {
					flex: 1;
					font-size: 32rpx;
					margin-left: 20rpx;
					color: #000000;
					font-weight: 550;
				}

				.device {
					width: 50rpx;
					height: 50rpx;
				}

				.tick {
					width: 35rpx;
					height: 30rpx;
				}
			}
		}
	}

	.ui-pwd {
		margin: 50rpx 0 130rpx 0;
		border: 1rpx solid #e2e2e2;

	}

	.wd-btn-group {
		margin-top: 30rpx;
	}
</style>
