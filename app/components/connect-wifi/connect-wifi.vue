<!--
* @Author: 连接wifi
* @Date: 2023-01-28 16:38:01
* @FilePath: /components/connect-wifi/connect-wifi.vue
* @Description: In User Settings Edit
-->

<template>
	<u-popup :closeable="true" :round="10" :show="show" mode="center" @close="back">
		<view class="wd-add ui-change">
			<u-text prefixIcon="setting" :iconStyle="{ fontSize: '38rpx', color: '#ea942f' }" color="#ea942f"
				size="30rpx" text="选择网络"></u-text>
			<view class="ui-add-box">
				<view class="ui-list" v-if="step === 'list'">
					<view v-for="(item, index) in list" :key="index" @tap="handleSelect(item)" class="ui-list-item"
						:class="{'active': item.ssid === info.wifi.ssid}">
						<u-icon name="wifi" size="40rpx"></u-icon>
						<text class="text">{{item.ssid}}</text>
						<image v-show="item.ssid === info.wifi.ssid" class="tick" src="../../static/images/tick.png" />
					</view>
				</view>
				<view class="ui-pwd" v-else>
					<u-text size="28rpx" prefixIcon="lock" iconStyle="font-size: 36rpx" text="密码">
					</u-text>
					<u--input v-model="info.pwd" type="password" placeholder="请输入wifi密码" border="bottom" clearable>
					</u--input>
				</view>
			</view>
			<view class="wd-btn-group">
				<button @click="back" v-if="step !== 'list'">返回</button>
				<button @click="next">确定</button>
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
				show: false,
				/**密码**/
				info: {
					pwd: '',
					wifi: {}
				},
				/**当前步骤**/
				step: 'list'
			};
		},
		created() {},
		methods: {
			close() {
				this.show = false;
			},

			open() {
				this.show = true;
				this.info.wifi = this.list[0] || {};
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
		width: 582rpx;
		min-height: 500rpx;
		border-radius: 20rpx;
		filter: drop-shadow(0 0 5rpx rgba(7, 5, 5, 0.34));
		background-image: linear-gradient(-36deg, #e4e4e4 0%, #f8f8f8 100%);
		padding: 53rpx 31rpx;

		&.ui-change {
			// .ui-add-box {
			// 	border-bottom: 1px solid #e4e4e4;
			// }
		}

		&>view {
			margin-top: 30rpx;

			&.ui-add-box {
				padding: 10rpx 20rpx;

				// &>* {
				// 	margin-top: 30rpx;
				// }
			}
		}

		.wd-btn-group {
			button {
				&:nth-child(2) {
					@extend .wd-sms;
				}
			}
		}


		.ui-list {
			padding: 20rpx 60rpx 20rpx 20rpx;
			display: flex;
			flex-direction: column;
			width: 100%;
			height: 300rpx;
			overflow-y: scroll;

			.ui-list-item {
				// margin-left: 80rpx;
				padding: 18rpx 34rpx;
				border-radius: 40rpx;
				display: flex;
				align-items: center;
				justify-content: space-between;

				.text {
					flex: 1;
					font-size: 36rpx;
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
	}
</style>
