<!--
* @Author: zhanch
* @Date: 2022-12-29 17:47:02
* @FilePath: /components/add-room/add-room.vue
* @Description: 新增房间
-->

<template>
	<u-popup :closeable="true" :round="10" :show="show" mode="center" @close="close" @open="open">
		<view class="ui-add">
			<u-text prefixIcon="plus-circle" :iconStyle="{ fontSize: '38rpx', color: '#ea942f' }" color="#ea942f"
				size="30rpx" text="新建房间"></u-text>
			<view class="ui-add-box">
				<u-text size="28rpx" prefixIcon="../../static/images/set-form.png" iconStyle="font-size: 25rpx"
					text="房间名称"></u-text>
				<u--input v-model="roomName" placeholder="请输入房间名称" border="bottom" clearable></u--input>
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
		emit: [],
		props: {},
		data() {
			return {
				show: false,
				id: '',
				subTitle: '',
				roomName: '',
			}
		},
		methods: {
			close() {
				this.show = false;
			},
			open(obj) {
				this.show = true;
				const {
					id,
					subTitle
				} = obj
				this.id = id
				this.subTitle = subTitle || '下一步'
			},
			next() {
				if (!this.roomName) {
					return uni.$u.toast('请填写房间名称')
				}
				PostRoomList({
					roomName: this.roomName,
					familyId: this.id,
				}).then(res => {
					uni.$u.toast(res.msg)
					this.close();
					setTimeout(() => {
						this.$emit('update')
					}, 1000)
				})

			}
		}
	};
</script>

<style lang="scss">
	.ui-add {
		width: 582rpx;
		height: 406rpx;
		border-radius: 20rpx;
		filter: drop-shadow(0 0 5rpx rgba(7, 5, 5, 0.34));
		background-image: linear-gradient(-36deg, #e4e4e4 0%, #f8f8f8 100%);
		border: 1px solid #ffcb3d;
		padding: 53rpx 31rpx;

		&>view {
			margin-top: 52rpx;

			&.ui-add-box {
				padding: 10rpx 20rpx;
				border-bottom: 1px solid #e4e4e4;

				&>* {
					margin-top: 10rpx;
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
