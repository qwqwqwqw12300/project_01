<!--
* @Author: zhanch
* @Date: 2022-12-30 12:38:33
* @FilePath: /pages/myself/famliy-manage.vue
* @Description: 家庭管理
-->

<template>
	<app-body>
		<u-navbar leftText="首页" @rightClick="rightClick" :autoBack="true" bgColor="transparent" leftIconColor="#fff" :fixed="false" />
		<view class="ui-logo">我的家庭管理</view>
		<view class="ui-menu">
			<u-grid>
				<u-grid-item v-for="(baseListItem, baseListIndex) in baseList" :key="baseListIndex">
					<view class="ui-menu-item" :border="false" @click="gridClick(baseListItem.id)">
						<u-icon :customStyle="{ paddingTop: 20 + 'rpx' }" name="/static/images/myself/home.png" size="60rpx"></u-icon>
						<text class="grid-text">{{ baseListItem.title }}</text>
						<view class="ui-edit" @click.stop="editFamliy">
							<u-icon :customStyle="{ paddingTop: 20 + 'rpx' }" name="edit-pen-fill" size="30rpx" color="#ff9500"></u-icon>
						</view>
					</view>
				</u-grid-item>
			</u-grid>
		</view>
		<view class="ui-btn"><button @click="add">创建家庭</button></view>
		<u-popup :closeable="true" :overlay="false" zIndex="99" :round="10" :show="isEditShow" mode="center" @close="close">
			<view class="wd-add">
				<view>
					<u-text size="28rpx" prefixIcon="home-fill" iconStyle="font-size: 40rpx" text="家庭名称"></u-text>
					<u--input placeholder="请输入家庭名称" border="bottom" clearable></u--input>
				</view>
				<view>
					<u-text size="28rpx" prefixIcon="map-fill" iconStyle="font-size: 36rpx" text="家庭地址(必填)"></u-text>
					<u--input placeholder="请输入家庭名称" border="bottom" clearable></u--input>
				</view>
				<view class="wd-btn-gloup">
					<button type="default" @click="onSubmit">提交</button>
					<button type="default" @click="onDelete">删除</button>
				</view>
			</view>
		</u-popup>
		<add-family ref="addFamily" @next="familyNext"/>
	</app-body>
</template>

<script>
export default {
	data() {
		return {
			isEditShow: false,
			baseList: [
				{
					title: '家庭1',
					id: ''
				}
			]
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
		editFamliy() {
			this.isEditShow = true;
		},

		/**
		 * 关闭弹窗
		 */
		close() {
			this.isEditShow = false;
		},
		/**
		 * 提交
		 */
		onSubmit() {
			this.close();
		},
		/**
		 * 删除
		 */
		onDelete() {
			uni.showModal({
				title: '提示',
				content: '是否确认删除家庭',
				success: res => {
					if (res.confirm) {
						this.close();
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
			this.$refs.addFamily.open();
		},
		
		/**
		 * 家庭添加完成
		 */
		familyNext(){}
		
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
	}
}
.ui-btn {
	text-align: center;
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
	& > view {
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
