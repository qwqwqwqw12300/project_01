<!--
* @Author: zhanch
* @Date: 2022-12-30 12:38:33
* @FilePath: /pages/myself/famliy-manage.vue
* @Description: 家庭管理
-->

<template>
	<app-body>
		<app-logo text="我的家庭管理"></app-logo>
		<view class="ui-menu">
			<u-grid>
				<u-grid-item v-for="(baseListItem, baseListIndex) in baseList" :key="baseListIndex">
					<view class="ui-menu-item" :border="false" @click="gridClick(baseListItem)">
						<u-icon :customStyle="{ paddingTop: 20 + 'rpx' }" name="/static/images/myself/home.png"
							size="60rpx"></u-icon>
						<text class="grid-text">{{ baseListItem.name }}</text>
						<u-icon @click.native.stop="onDelete(baseListItem.familyId)" class="ui-close active"
							name="close-circle-fill" size="40rpx">
						</u-icon>
					</view>
				</u-grid-item>
			</u-grid>
		</view>
		<view class="ui-btn"><button class="default" @click="add">创建家庭</button></view>
		<family-pop @update="getFamilyList" mode="add" :btnName="'提交'" ref="addFamily" />
	</app-body>
</template>

<script>
	import {
		GetFamilyList,
		PostDelFamily,
		PostEditFamily,
	} from '@/common/http/api.js';
	import {
		mapState
	} from 'vuex';
	export default {
		data() {
			return {};
		},
		computed: {
			...mapState({
				/**所有家庭列表**/
				baseList: state => state.familyList.filter(item =>
					item.shareFlag === '2'
				)
			}),
		},
		onShow() {
			this.getFamilyList()
		},
		methods: {
			/**
			 * 菜单点击
			 */
			gridClick(item) {
				this.$setCache('familyInfo', item);
				uni.navigateTo({
					url: '/pages/myself/room-manage'
				});
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
			 * 获取家庭列表
			 */
			getFamilyList() {
				this.$store.dispatch('getAllFamily');
			},
			/**
			 * 删除家庭列表
			 */
			handleDelete(familyId) {
				PostDelFamily({
					familyId
				}).then(res => {
					uni.$u.toast(res.msg);
					setTimeout(() => {
						this.getFamilyList()
					}, 1000);
				})
			}
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
			padding-bottom: 20rpx;
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

			.grid-text {
				display: inline-flex;
				align-content: center;
				justify-content: center;
				margin-top: 15rpx;
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
</style>
