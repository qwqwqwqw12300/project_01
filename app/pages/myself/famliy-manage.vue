<!--
* @Author: zhanch
* @Date: 2022-12-30 12:38:33
* @FilePath: /pages/myself/famliy-manage.vue
* @Description: 家庭管理
-->

<template>
	<app-body>
		<app-logo text="我的家庭管理"></app-logo>
		<!-- <view class="ui-logo">我的家庭管理</view> -->
		<view class="ui-menu">
			<u-grid>
				<u-grid-item v-for="(baseListItem, baseListIndex) in baseList" :key="baseListIndex">
					<view class="ui-menu-item" :border="false" @click="gridClick(baseListItem.id)">
						<u-icon :customStyle="{ paddingTop: 20 + 'rpx' }" name="/static/images/myself/home.png"
							size="60rpx"></u-icon>
						<text class="grid-text">{{ baseListItem.name }}</text>
						<view class="ui-edit" @click.stop="editFamliy(baseListItem)">
							<u-icon name="edit-pen-fill" size="30rpx" color="#ff9500"></u-icon>
						</view>
						<u-icon @click="onDelete(baseListItem.familyId)" class="ui-close active"
							name="close-circle-fill" size="40rpx">
						</u-icon>
					</view>
				</u-grid-item>
			</u-grid>
		</view>
		<view class="ui-btn"><button @click="add">创建家庭</button></view>
		<u-popup :closeable="true" :overlay="false" zIndex="99" :round="10" :show="isEditShow" mode="center"
			@close="close">
			<view class="wd-add">
				<view>
					<u-text size="28rpx" prefixIcon="home-fill" iconStyle="font-size: 40rpx" text="家庭名称"></u-text>
					<u--input v-model="form.name" placeholder="请输入家庭名称" border="bottom" clearable></u--input>
				</view>
				<view>
					<u-text size="28rpx" prefixIcon="map-fill" iconStyle="font-size: 36rpx" text="家庭地址(必填)"></u-text>
					<u--input v-model="form.address" placeholder="请输入家庭地址" border="bottom" clearable></u--input>
				</view>
				<view class="wd-btn-gloup">
					<button @click="onSubmit">提交</button>
					<button @click="onclose">取消</button>
				</view>
			</view>
		</u-popup>
		<add-family @update="getFamilyList" :btnName="'提交'" ref="addFamily" @next="familyNext" />
	</app-body>
</template>

<script>
	import {
		PostFamilyList,
		PostDelFamily,
		PostEditFamily,
	} from '@/common/http/api.js';
	export default {
		data() {
			return {
				isEditShow: false,
				baseList: [],
				form: {
					name: '', //家庭名称
					address: '', //家庭地址
				}
			};
		},
		methods: {
			/**
			 * 菜单点击
			 */
			gridClick(url) {
				console.log(12);
			},

			/**
			 * 修改家庭
			 */
			editFamliy(item) {
				this.form = item
				this.isEditShow = true;
			},

			/**
			 * 关闭弹窗
			 */
			close() {
				this.form = {}
				this.isEditShow = false;
			},
			/**
			 * 提交
			 */
			onSubmit() {
				const {
					name,
					address,
					familyId,
				} = this.form
				if (!name) {
					return uni.$u.toast('请填写家庭名称')
				}
				if (!address) {
					return uni.$u.toast('请填写家庭地址')
				}
				PostEditFamily({
					familyName: name,
					address,
					familyId
				}).then(res => {
					uni.$u.toast(res.msg)
					setTimeout(() => {
						this.close();
						this.getFamilyList()
					}, 1000)
				})
			},
			/**
			 * 删除弹窗
			 */
			onDelete(familyId) {
				uni.showModal({
					title: '提示',
					content: '是否确认删除家庭',
					success: res => {
						if (res.confirm) {
							this.handleDelete(familyId)
							console.log('用户点击确定');
						} else if (res.cancel) {
							console.log('用户点击取消');
						}
					}
				});
			},
			/**
			 * 添加家庭
			 */
			add() {
				this.$refs.addFamily.open({
					btnName: '提交'
				});
			},

			/**
			 * 家庭添加完成
			 */
			familyNext() {},

			/**
			 * 获取家庭列表
			 */
			getFamilyList() {
				PostFamilyList({}).then(res => {
					this.baseList = res.rows
				})
			},
			/**
			 * 删除家庭列表
			 */
			handleDelete(familyId) {
				PostDelFamily({
					familyId
				}).then(res => {
					uni.$u.toast(res.msg)
					setTimeout(() => {
						this.getFamilyList()
					}, 1000)
				})
			}
		},
		mounted() {
			this.getFamilyList()
		}
	};
</script>

<style lang="scss">
	.ui-menu {
		margin-top: 70rpx;
		padding: 0 78rpx;
		min-height: 600rpx;

		.ui-menu-item {
			position: relative;
			margin-top: 58rpx;
			display: flex;
			align-items: center;
			justify-content: center;
			flex-direction: column;
			font-size: 27rpx;
			color: #414141;
			height: 168rpx;
			width: 168rpx;
			border-radius: 10rpx;
			filter: drop-shadow(7.824rpx 10.382rpx 8rpx rgba(7, 5, 5, 0.08));
			background-image: linear-gradient(96deg, #f5f5f5 0%, #e5e5e5 100%);
			text-align: center;

			text {
				display: inline-block;
				margin-top: 10rpx;
				width: 70%;
				height: 60rpx;
			}

			.ui-edit {
				position: absolute;
				bottom: 10rpx;
				right: 10rpx;
				z-index: 10;
			}

			.ui-close {
				position: absolute;
				right: -10rpx;
				top: -10rpx;
			}
		}
	}

	.ui-btn {
		text-align: center;
		margin-top: 100rpx;

		button {
			width: 276rpx;
			height: 74rpx;
			font-size: 30rpx;
			line-height: 74rpx;
			border-radius: 60rpx;
		}
	}

	.ui-logo {
		height: 69rpx;
		// width: 366rpx;
		font-size: 69rpx;
		color: #fff;
		margin: 20rpx 72rpx;
		display: inline-block;
		font-weight: bold;
	}

	.wd-add {
		width: 582rpx;
		height: 606rpx;
		border-radius: 20rpx;
		filter: drop-shadow(0 0 5rpx rgba(7, 5, 5, 0.34));
		background-image: linear-gradient(-36deg, #e4e4e4 0%, #f8f8f8 100%);
		padding: 53rpx 31rpx;

		&>view {
			margin-top: 52rpx;
			padding: 10rpx 20rpx;
			border-bottom: 1px solid #e4e4e4;

			&:nth-last-of-type(1) {
				padding-left: 0rpx;
				border-bottom: none;
			}
		}

		.wd-btn-gloup {
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
