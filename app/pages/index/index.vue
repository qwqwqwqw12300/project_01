<!--
* @Author: zhanch
* @Date: 2022-12-27 15:53:29
* @FilePath: /pages/index/index.vue
* @Description: 应用首页
-->

<template>
	<view @click="closeDevice">
		<app-body :hideTitle="true">
			<view class="ui-scan">
				<view class="ui-sys-message">
					<u-badge :isDot="!!readInfo.length" absolute :offset="[0, 0]"></u-badge>
					<u-icon name="chat" @click="goPage('/pages/service/system-messages')" class="active" color="#000"
						size="50rpx"></u-icon>
				</view>
				<u-icon name="plus-circle" @click="addDevice" :stop="true" class="active" color="#000" size="45rpx">
				</u-icon>
			</view>
			<view class="ui-logo">
				<image src="../../static/images/index/logo.png"></image>
				<text>艾吉通</text>
			</view>

			<!-- 家庭tab -->
			<view class="ui-tab" v-if="tabList.length">
				<u-tabs @change="tabChange" :list="tabList" lineColor="unset"
					:activeStyle="{color: '#353535', fontSize: '45rpx'}"
					:inactiveStyle="{color: '#888888', fontSize: '40rpx'}"></u-tabs>
			</view>
			<!-- /家庭tab -->

			<!-- 家庭列表 -->
			<template v-if="loading">
				<view class="ui-loading">
					<u-loading-icon mode="semicircle" :vertical="true" text="加载中" textSize="18"></u-loading-icon>
				</view>
			</template>
			<template v-else-if="familyList.length">
				<scroll-view :scroll-y="true" class="ui-scroll" refresher-enabled :refresher-triggered="isRefresh"
					@refresherrefresh="pullDownRefresh" refresher-background="transparent" lower-threshold="10">
					<view class="ui-group" v-for="(familyItem, index) of familyList" :key="'family' + index">
						<view class="ui-title">
							<view>
								<u-text :text="familyItem.name" size="32rpx"
									:iconStyle="{height: '40rpx', width: '40rpx'}"
									prefixIcon="../../static/images/index/home.png"></u-text>
							</view>
							<view class="ui-share-box">
								<u-text @click="goPage('/pages/myself/famliy-manage')"
									prefixIcon="../../static/images/index/edit.png" size="32rpx" :align="'right'"
									:block="false" :iconStyle="{
									height: '40rpx', width: '40rpx'
								}" text="编辑" v-if="familyItem.shareFlag === '2'"></u-text>
								<u-text
									@click="goPage('/pages/share/share?familyId='+ familyItem.familyId + '&name='+ familyItem.name)"
									prefixIcon="../../static/images/index/share.png" size="32rpx" :align="'right'"
									:block="false" :iconStyle="{
									height: '40rpx', width: '40rpx'
								}" text="分享" v-if="familyItem.shareFlag === '2'"></u-text>
							</view>

						</view>
						<view class="ui-device">
							<!-- 房间 -->
							<room-card :rooms="familyItem.rooms" :shareFlag="familyItem.shareFlag"
								@confirm="handleInitList" />
							<!-- 房间 -->
							<human-card :humans="familyItem.humans" :shareFlag="familyItem.shareFlag"
								@confirm="handleInitList" />
							<!-- 人员 -->
							<!-- 新增房间 -->
							<view class="ui-list ui-list-add" v-if="familyItem.shareFlag == '2'">
								<view class="ui-list-box" @click="addRoom(familyItem)">
									<u-icon name="../../static/images/index/add.png" size="56rpx"></u-icon>
									<text>添加</text>
								</view>
							</view>
							<!-- /新增房间 -->
						</view>
					</view>
					<!-- 添加 -->
					<view class="ui-add-btn">
						<image @click="addStep" src="../../static/images/index/add-family.png" mode=""></image>
						<text>新建家庭</text>
					</view>
				</scroll-view>
			</template>
			<!-- /家庭列表 -->

			<!-- 空户 -->
			<template v-else>
				<view class="ui-default">
					<image src="../../static/images/index/empty.png" mode=""></image>
					<text>未添加任何家庭</text>
				</view>
				<!-- 添加 -->
				<view class="ui-add-btn">
					<image @click="addStep" src="../../static/images/index/add-family.png" mode=""></image>
					<text>新建家庭</text>
				</view>
			</template>
			<!-- /空户 -->

			<add-step ref="addStepRef"></add-step>
			<bind-device :payload="bindPayload" @next="handleInitList" ref="indexBindDev" />
			<room-pop ref="indexAddRoom" @update="handleInitList" />
			<!-- 下拉框 -->
			<AppHandle :isShow="isAddShow" @cancle="isAddShow = false"></AppHandle>
			<!-- /下拉框 -->
		</app-body>
	</view>


</template>
<script>
	import {
		getDeviceList,
		getFamilyList,
		GetReadInfo,
		forIndex
	} from '../../common/http/api';
	import {
		mapState,
		mapActions,
		mapGetters
	} from 'vuex';
	import
	AppHandle
	from '@/components/add-handle/add-handle.vue';
	import
	RoomCard
	from './components/room-card.vue';
	import
	HumanCard
	from './components/human-card.vue';

	export default {
		components: {
			AppHandle,
			RoomCard,
			HumanCard
		},
		data() {
			return {
				/**是否展示添加弹窗**/
				isAddShow: false,
				/**公告状态**/
				readInfo: [],
				/**绑定信息**/
				bindPayload: {},
				/**当前选中的家庭**/
				currentTab: '',
				/**下拉刷新状态**/
				isRefresh: false,
				/**轮询定时器**/
				timer: null,
				/**是否在加载状态**/
				loading: true
			}
		},
		computed: {
			...mapState({
				/**所有家庭列表**/
				familyList: function(state) {
					if (this.currentTab) {
						return state.familyList.filter(ele => ele.familyId === this.currentTab);
					} else {
						return state.familyList;
					}
				},
				tabList: function(state) {
					const list = state.familyList.map(ele => ({
						name: ele.name,
						id: ele.familyId
					}));
					return [{
						name: '全部',
						id: ''
					}, ...list];
				}
			}, ),
			...mapGetters(['filterDevice']),
		},
		onShow() {
			this.handleInitList();
			this.timer = setInterval(() => {
				this.forIndexFun()
			}, 1000 * 60)
		},
		onHide() {
			this.timer && clearInterval(this.timer)
		},
		methods: {
			...mapActions(['getPushMsgState', 'getAllRoom']),

			/**
			 * 数据初始化
			 */
			handleInitList() {
				Promise.all([
					this.forIndexFun(),
					this.getReadInfo(),
					this.getPushMsgState()
				]).then(res => {
					this.loading = false;
					this.isRefresh = false;
				})
			},
			/**  
			 * 获取设备与房间信息
			 */
			forIndexFun() {
				return new Promise(resolve => {
					forIndex().then(res => {
						this.$store.commit('setFamilyList', res[0].data.rows);
						this.$store.commit('setDevicesList', res[1].data.rows);
						resolve();
					})
				})
			},
			/**
			 *下拉刷新
			 **/
			pullDownRefresh() {
				this.isRefresh = true;
				this.handleInitList();
			},
			/**
			 * 打开添加按钮
			 */
			addDevice() {
				setTimeout(() => {
					this.isAddShow = true;
				}, 10);
			},
			/**
			 * 关闭添加按钮
			 */
			closeDevice() {
				this.isAddShow = false;
			},
			/**
			 * 页面跳转
			 */
			goPage(url) {
				if (url === '/pages/equipment/add/monitor') return uni.$u.toast('暂不支持添加该设备');
				uni.navigateTo({
					url
				});
			},

			/**
			 * 添加家庭
			 */
			addStep() {
				this.$refs.addStepRef.open();
			},

			/**
			 * 添加房间
			 */
			addRoom(item) {
				const {
					shareFlag,
					familyId
				} = item
				if (shareFlag !== '2') return uni.$u.toast('该家庭不支持新增房间')
				this.$refs.indexAddRoom.open({
					familyId
				});
			},

			/**
			 * 获取公告状态
			 */
			getReadInfo() {
				return new Promise(resolve => {
					GetReadInfo().then(res => {
						this.readInfo = res.data;
						resolve();
					});
				})
			},


			/**
			 * 绑定设备
			 */
			bindDevice({
				familyId,
				roomId,
				humanId
			}, shareFlag) {
				if (shareFlag !== '2') return;
				if (humanId) {
					this.bindPayload = {
						familyId,
						id: humanId,
						type: 'human'
					};
				} else {
					this.bindPayload = {
						familyId,
						id: roomId,
						type: 'room'
					};
				}
				this.$refs.indexBindDev.open();
			},

			/**
			 * tab切换
			 */
			tabChange($e) {
				this.currentTab = $e.id;
			}
		}
	}
</script>

<style lang="scss" scoped>
	.ui-scroll {
		height: calc(100vh - (var(--window-bottom) + 220rpx + 50px + var(--status-bar-height)))
	}

	.ui-scan {
		display: flex;
		position: absolute;
		right: 24rpx;
		top: 120rpx;
		z-index: 2;

		.ui-sys-message {
			position: relative;

		}

		&>* {
			margin-left: 20rpx;
		}

		.ui-select {
			position: fixed;
			left: 50%;
			bottom: var(--window-bottom, 0);
			transform: translate(-50%, 0%);
			margin: 0 auto;
			// transform: translate(-50%,-50%);
			width: 686rpx;
			height: 392rpx;
			border-radius: 24rpx;
			// filter: drop-shadow(0 0 5px rgba(7, 5, 5, 0.34));
			background: #fff;
			border: 1px solid #e2e2e2;

			.ui-select-item {
				// margin-top: 16rpx;
				width: 686rpx;
				height: 98rpx;
				display: flex;
				align-items: center;
				justify-content: center;
				font-size: 26rpx;
				color: #000;
				border-bottom: 1px solid #e2e2e2;

				text {
					display: inline-block;
					margin-left: 20rpx;
					font-size: 34rpx;
					color: #000000;
					text-align: center;
					line-height: 34rpx;
					font-weight: 400;
				}
			}
		}
	}

	.ui-logo {
		display: inline-flex;
		width: 100%;
		align-items: center;
		flex-direction: row;
		margin: 100rpx 32rpx 50rpx 32rpx;
		font-size: 56rpx;
		color: #353535;
		font-weight: bold;

		image {
			height: 80rpx;
			width: 80rpx;
		}

	}

	.ui-tab {
		box-sizing: border-box;
		width: 100%;
		padding: 0 32rpx;
	}

	.ui-group {
		width: 100%;
		margin-top: 61rpx;
		display: inline-flex;
		flex-direction: column;
		align-items: center;

		&:nth-last-child(1) {
			min-height: 550rpx;
		}

		.ui-title {
			box-sizing: border-box;
			padding: 0 16rpx;
			display: flex;
			align-items: center;
			justify-content: space-between;
			height: 80rpx;
			width: calc(100% - 74rpx);
			overflow: hidden;
			font-weight: bold;

			.ui-share-box {
				width: 250rpx;
				display: flex;
				align-items: center;
				flex-direction: row;
			}
		}

		.ui-device {
			display: inline-block;
			width: calc(100%);
			margin-top: 36rpx;
			padding-left: 32rpx;
			padding-right: 32rpx;

			.ui-list {
				margin-left: 20rpx;
				margin-bottom: 20rpx;
				float: left;
				height: 160rpx;
				width: calc(50% - 11rpx);
				text-align: center;

				&:nth-child(even) {
					margin-left: 20rpx;
				}

				.ui-list-box {
					position: relative;
					height: 100%;
					width: 100%;
					display: inline-flex;
					align-items: center;
					flex-direction: row;
					background: #FFFFFF;
					border-radius: 16rpx;
					padding-left: 15rpx;
					box-sizing: border-box;

					image {
						height: 100rpx;
						width: 100rpx;
					}
				}

				&.ui-list-add {
					text-align: left;

					.ui-list-box {
						width: 156rpx;
						height: 160rpx;
						display: flex;
						align-items: center;
						justify-content: center;
						flex-direction: column;
						padding: 0;

						text {
							margin-top: 20rpx;
							font-size: 24rpx !important;
						}
					}

				}

			}
		}
	}

	.ui-default {
		width: 100%;
		box-sizing: border-box;
		display: inline-block;
		margin: 185rpx 0 80rpx 0;
		padding: 0 200rpx;
		text-align: center;

		image {
			max-width: 330rpx;
			max-height: 330rpx;
			margin-bottom: 10rpx;
		}

		text {
			font-size: 36rpx;
			color: #888888;
			font-weight: 500;
		}
	}

	.ui-link {
		color: #0094ff !important;
	}

	.ui-add-btn {
		margin: 50rpx 0;
		display: flex;
		align-items: center;
		justify-content: center;
		flex-direction: column;

		image {
			width: 100rpx;
			height: 100rpx;
			margin-bottom: 10rpx;
		}

		text {
			font-size: 24rpx;
			color: #888888;
		}
	}

	.ui-loading {
		display: flex;
		justify-content: center;
		align-items: center;
		height: 500px;
		width: 100%;
	}
</style>
