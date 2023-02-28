<!--
* @Author: zhanch
* @Date: 2022-12-30 12:38:33
* @FilePath: /pages/myself/famliy-manage.vue
* @Description: 家庭管理
-->

<template>
	<app-body :bg="false">
		<view class="ui-logo">
			<app-logo text="我的家庭管理"></app-logo>
		</view>
		<view class="ui-menu">
			<view class="menu-item" v-for="(baseListItem, baseListIndex) in baseList" :key="baseListIndex">
				<view class="item-box">
					<view class="title">
						<u-icon name="/static/images/home.png" size="28"></u-icon>
						<text>{{ baseListItem.name }}</text>
					</view>
					<!-- 	<view class="action" >删除家庭</view> -->
					<view class="action">
						<text class="delete" @click.stop="onDelete(baseListItem.familyId)">删除</text>
						<text class="edit" @click.stop="gridClick(baseListItem)">编辑</text>
					</view>
				</view>
			</view>
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
	.ui-logo {
		background: #ffffff;
		padding-bottom: 50rpx;
	}

	.ui-menu {
		padding: 30rpx 20rpx 120rpx 20rpx;
		display: flex;
		flex-wrap: wrap;
		box-sizing: border-box;

		.menu-item {
			background: #FFFFFF;
			border-radius: 16rpx;
			// padding: 20rpx;
			width: 46%;
			margin: 0 2%;
			margin-bottom: 30rpx;

			.item-box {
				padding: 30rpx;
				display: flex;
				flex-direction: column;
				justify-content: center;
				align-items: center;

				.title {
					line-height: 70rpx;
					display: flex;
					align-items: center;
					justify-content: center;

					text {
						font-size: 36rpx;
						color: #353535;
						font-weight: 550;
					}
				}

				.action {
					margin-top: 20rpx;
					display: flex;
					width: 100%;
					justify-content: space-around;
					align-items: center;
					font-size: 26rpx;

					.delete {
						color: #E95656;
					}

					.edit {
						color: #fff;
						padding: 7rpx 24rpx;
						background-image: linear-gradient(90deg, #FFB24D 0%, #FD913B 100%);
						border-radius: 24px;
					}

					// font-size: 28rpx;
					// color: #E95656;
				}
			}
		}

	}

	.ui-btn {
		width: 100%;
		position: fixed;
		bottom: 0;
		left: 0;

		button {
			border-radius: 0rpx !important;
		}
	}
</style>
