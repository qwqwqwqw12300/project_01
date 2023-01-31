<!--
* @Author: zhanch
* @Date: 2022-12-29 17:47:02
* @FilePath: /components/add-room/add-room.vue
* @Description: 新增房间
-->

<template>
	<u-popup :closeable="true" :round="10" :show="show" mode="center" @close="close">
		<view class="ui-add">
			<u-text prefixIcon="plus-circle" :iconStyle="{ fontSize: '38rpx', color: '#ea942f' }" color="#ea942f"
				size="30rpx" text="新建房间"></u-text>
			<view class="ui-add-box">
				<view>
					<u-text size="28rpx" prefixIcon="home" iconStyle="font-size: 40rpx" text="房间名称"></u-text>
					<u--input class="ui-room-name" v-model="form.roomName" placeholder="请输入房间名称" border="bottom"
						clearable></u--input>
				</view>
				<!-- <view>
					<u-text size="28rpx" prefixIcon="setting" iconStyle="font-size: 36rpx" text="房间高度"></u-text>
					<view class="ui-slider">
						<u-slider v-model="form.roomHeight" activeColor="#eeaa3d" blockColor="#eeaa3d"
							inactiveColor="#c0c4cc" />
						<text>{{$u.priceFormat(form.roomHeight/10, 2)}}米</text>
					</view>
				</view>
				<view>
					<u-text size="28rpx" prefixIcon="setting" iconStyle="font-size: 36rpx" text="房间长度"></u-text>
					<view class="ui-slider">
						<u-slider v-model="form.roomLength" activeColor="#eeaa3d" blockColor="#eeaa3d"
							inactiveColor="#c0c4cc" />
						<text>{{$u.priceFormat(form.roomLength/10, 2)}}米</text>
					</view>
				</view>
				<view>
					<u-text size="28rpx" prefixIcon="setting" iconStyle="font-size: 36rpx" text="房间左长度"></u-text>
					<view class="ui-slider">
						<u-slider v-model="form.roomLeft" activeColor="#eeaa3d" blockColor="#eeaa3d"
							inactiveColor="#c0c4cc" />
						<text>{{$u.priceFormat(form.roomLeft/10, 2)}}米</text>
					</view>
				</view>
				<view>
					<u-text size="28rpx" prefixIcon="setting" iconStyle="font-size: 36rpx" text="房间右长度"></u-text>
					<view class="ui-slider">
						<u-slider v-model="form.roomRight" activeColor="#eeaa3d" blockColor="#eeaa3d"
							inactiveColor="#c0c4cc" />
						<text>{{$u.priceFormat(form.roomRight/10, 2)}}米</text>
					</view>
				</view> -->

			</view>
			<view class="ui-btn"><button @click="next" class="wd-sms">{{ subTitle }}</button></view>
		</view>
	</u-popup>
</template>

<script>
	import {
		PostAddRoom,
	} from '@/common/http/api.js';
	export default {
		props: {
			family: String
		},
		data() {
			return {
				show: false,
				subTitle: '下一步',
				familyId: '',
				form: {
					roomName: '', //房间名称
					// roomHeight: '', //房间高度
					// roomLength: '', //房间长度
					// roomLeft: '', //房间左长度
					// roomRight: "", //房间右长度
				}
			}
		},
		methods: {
			close() {
				this.form = {}
				this.show = false;
			},
			open(obj = {}) {
				this.show = true;
				const {
					id,
					subTitle
				} = obj;
				this.familyId = id
				this.subTitle = subTitle || '下一步'
			},
			next() {
				const {
					roomName,
					roomHeight,
					roomLength,
					roomLeft,
					roomRight
				} = this.form;
				if (!roomName) {
					return uni.showToast({
						icon: 'none',
						title: '请完善房间信息'
					})
				}
				PostAddRoom({
					...this.form,
					roomHeight: uni.$u.priceFormat(roomHeight / 10, 2),
					roomLength: uni.$u.priceFormat(roomLength / 10, 2),
					roomLeft: uni.$u.priceFormat(roomLeft / 10, 2),
					roomRight: uni.$u.priceFormat(roomRight / 10, 2),
					familyId: this.familyId,
				}).then(res => {
					uni.$u.toast(res.msg)
					this.close();
					setTimeout(() => {
						this.$emit('update', res.data.roomId)
					}, 500)
				})

			}
		}
	};
</script>

<style lang="scss">
	.ui-add {
		width: 582rpx;
		min-height: 380rpx;
		border-radius: 20rpx;
		filter: drop-shadow(0 0 5rpx rgba(7, 5, 5, 0.34));
		background-image: linear-gradient(-36deg, #e4e4e4 0%, #f8f8f8 100%);
		border: 1px solid #ffcb3d;
		padding: 53rpx 31rpx;

		&>view {
			margin-top: 52rpx;

			&.ui-add-box {
				padding: 10rpx 20rpx;
				// border-bottom: 1px solid #e4e4e4;

				&>* {
					margin-top: 20rpx;
				}
			}
		}

		.ui-slider {
			width: 100%;
			display: flex;
			flex-direction: row;
			align-items: center;
			justify-content: space-between;

			text {
				font-size: 26rpx;
				color: #999;
			}

			&>* {
				:nth-child(1) {
					width: 320rpx;
				}
			}
		}

		.ui-room-name {
			margin-left: 20rpx;
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
		}
	}
</style>
