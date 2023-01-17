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
				<u-text size="28rpx" prefixIcon="../../static/images/set-form.png" iconStyle="font-size: 25rpx"
					text="房间名称"></u-text>
				<u--input v-model="form.roomName" placeholder="请输入房间名称" border="bottom" clearable></u--input>
				<u-text size="28rpx"  prefixIcon="../../static/images/set-form.png" iconStyle="font-size: 25rpx"
					text="房间高度"></u-text>
				<u--input type="number" v-model="form.roomHeight" placeholder="请输入房间高度" border="bottom" clearable></u--input>
				<u-text size="28rpx" prefixIcon="../../static/images/set-form.png" iconStyle="font-size: 25rpx"
					text="房间长度"></u-text>
				<u--input type="number" v-model="form.roomLength" placeholder="请输入房间长度" border="bottom" clearable></u--input>
				<u-text size="28rpx" prefixIcon="../../static/images/set-form.png" iconStyle="font-size: 25rpx"
					text="房间左长度"></u-text>
				<u--input type="number" v-model="form.roomLeft" placeholder="请输入房间左长度" border="bottom" clearable></u--input>
				<u-text size="28rpx" prefixIcon="../../static/images/set-form.png" iconStyle="font-size: 25rpx"
					text="房间右长度"></u-text>
				<u--input v-model="form.roomRight" placeholder="请输入房间右长度" border="bottom" clearable></u--input>
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
		props: {
			family: String
		},
		data() {
			return {
				show: false,
				subTitle: '',
				familyId: '',
				form: {
					roomName: '', //房间名称
					roomHeight: '', //房间高度
					roomLength: '', //房间长度
					roomLeft: '', //房间左长度
					roomRight: "", //房间右长度
				}
			}
		},
		methods: {
			close() {
				this.form = {}
				this.show = false;
			},
			open(obj) {
				this.show = true;
				const {
					id,
					subTitle
				} = obj
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
				} = this.form
				if (!roomHeight || !roomLeft || !roomRight || !roomLength || !roomName) {
					return uni.$u.toast('请完善房间信息')
				}
				PostAddRoom({
					...this.form,
					familyId: this.familyId,
				}).then(res => {
					uni.$u.toast(res.msg)
					this.close();
					setTimeout(() => {
						this.$emit('update')
					}, 500)
				})

			}
		}
	};
</script>

<style lang="scss">
	.ui-add {
		width: 582rpx;
		height: 806rpx;
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
