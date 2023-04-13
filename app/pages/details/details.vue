<!--
* @Author: zhanch
* @Date: 2022-12-28 11:01:47
* @FilePath: /pages/details/details.vue
* @Description: 信息详情
-->
<template>
	<app-body :hideTitle="true">
		<view class="ui-body">
			<view class="ui-title">
				<text>报告</text>
				<!-- <u-icon size="40" name="https://cdn.uviewui.com/uview/example/button.png"></u-icon> -->
				<!-- <text class="active" @click="readMsgAll">全部标记已处理</text> -->
				<u-icon name="/static/images/read-all.png" size="50rpx" @click="readMsgAll"></u-icon>
			</view>
			<view class="ui-screen">
				<view class="ui-screen-icon ui-screen-family active" @click="familyShow = true">
					<text>{{eventInfo.familyName || '全部家庭'}}</text>
					<u-icon size="35rpx" :name="familyShow ? 'arrow-down-fill' : 'arrow-up-fill'"></u-icon>
				</view>
				<view class="ui-screen-icon active" @click="goSreen">
					<u-icon size="32rpx" name="../../static/images/screen.png"></u-icon>
					<text>筛选</text>
				</view>
			</view>
			<!-- 消息列表 -->
			<scroll-view class="ui-msg" scroll-y="true" @scrolltolower="pageNext" lower-threshold="10"
				@refresherrefresh="onRefresh" :refresher-triggered="triggered" refresher-enabled
				refresher-background="transparent">
				<!-- 家庭列表 -->
				<template v-if="loading">
					<view class="ui-loading">
						<u-loading-icon mode="semicircle" :vertical="true" text="加载中" textSize="18"></u-loading-icon>
					</view>
				</template>
				<template v-else-if="messageList.length">
					<view class="ui-scroll">
						<msg-card v-for="(item, index) of messageList" :key="index" :msgInfo="item"></msg-card>
						<u-loadmore marginBottom="30" dashed :status="loadmore" />
					</view>
				</template>
				<view class="ui-empty" v-else>
					<u-empty mode="list" text="暂无数据"></u-empty>
				</view>
			</scroll-view>
			<!-- /消息列表 -->
		</view>
		<u-action-sheet :closeOnClickOverlay="true" :safeAreaInsetBottom="true" :closeOnClickAction="true"
			@close="familyShow = false" :show="familyShow" cancelText="取消">
			<view>
				<view @click="sheetSelect(item)" class="ui-sheet" v-for="(item, index) of getFamilyList"
					:key="'sheet' + index">
					<u-icon name="home" class="active" size="38rpx" v-if="item.value">
					</u-icon>
					<text>{{item.text}}</text>
				</view>
			</view>
		</u-action-sheet>
	</app-body>
</template>

<script>
	import {
		mapState
	} from 'vuex';
	import {
		getMessage,
		PostSetBatchMsgInfo
	} from '../../common/http/api';
	import {
		INIT_SELECT
	} from '../../config/db';
	export default {
		data() {
			return {
				/**事件信息**/
				eventInfo: {
					...INIT_SELECT,
					/**家庭id**/
					familyId: '',
					/**家庭名称**/
					familyName: ''
				},
				/**消息列表**/
				messageList: [],
				/**下拉状态**/
				triggered: false,
				/**家庭列表是否展示**/
				familyShow: false,
				/**加载更多管理**/
				loadmore: 'loadmore', // loadmore-加载更多 loading-加载中 nomore-没有更多
				/**是否初始化查询**/
				loading: true
			};
		},
		computed: {
			...mapState({
				getFamilyList: state => {
					const familyList = state.familyList.map(ele => ({
						value: ele.familyId,
						text: ele.name
					}));
					return [{
						value: '',
						text: '全部家庭'
					}, ...familyList]

				}
			})
		},
		onLoad() {
			this.getMsgList();
		},
		methods: {
			/**
			 * 上拉刷新
			 * @param {Object} $e
			 */
			onRefresh($e) {
				this.triggered = true;
				this.getMsgList().then(res => {
					this.triggered = false;
				})
			},

			/**
			 * 全部已读
			 */
			readMsgAll() {
				uni.showModal({
					title: '提示',
					content: `是否全部标记已读？`,
					success: res => {
						if (res.confirm) {
							const {
								deviceType,
								eventlevel,
								familyId,
								startDate,
								endDate,
							} = this.eventInfo
							PostSetBatchMsgInfo({
								deviceType,
								eventlevel,
								familyId,
								readFlag: '1',
								startDate,
								endDate
							}).then(res => {
								uni.$u.toast('已处理登记成功');
								setTimeout(() => {
									this.getMsgList();
								}, 1000);

							})
						}
					}
				})
			},
			/**
			 * 查询设备消息
			 */
			getMsgList() {
				return new Promise(resolve => {
					this.messageList = [];
					this.eventInfo.pageNum = 1;
					this.loadmore = 'loadmore';
					// this.loading = true;
					getMessage({
						...this.eventInfo,
					}).then(res => {
						this.loading = false;
						this.messageList = res.rows || [];
						this.loadmore = res.total <= this.messageList.length ? 'nomore' :
							'loadmore';
						resolve();
					}, err => resolve());
				});
			},

			/**
			 * 下一页
			 */
			pageNext() {
				if (this.loadmore === 'nomore') return;
				this.loadmore = 'loading';
				return new Promise(resolve => {
					this.eventInfo.pageNum++;
					getMessage({
						...this.eventInfo
					}).then(res => {
						this.messageList = [...this.messageList, ...res.rows];
						this.loadmore = res.total <= this.messageList.length ? 'nomore' :
							'loadmore';
						resolve();
					}, err => resolve());
				});
			},

			/**
			 * 跳转筛选
			 */
			goSreen() {
				uni.$once('detailsScreenResult', screenInfo => {
					Object.assign(this.eventInfo, screenInfo);
					this.loading = true;
					this.getMsgList();
				});
				this.$setCache('detailsScreenInfo', this.eventInfo);
				uni.navigateTo({
					url: '/pages/details/screen'
				});
			},

			/**
			 *  选择家庭
			 **/
			sheetSelect(item) {
				Object.assign(this.eventInfo, {
					familyId: item.value,
					familyName: item.text
				});
				this.getMsgList();
				this.familyShow = false;
			},

		}
	}
</script>

<style lang="scss">
	.ui-body {
		color: #353535;

		.ui-title {
			padding: 34rpx 32rpx;
			// background: #fff;
			padding-top: 100rpx;
			display: flex;
			justify-content: space-between;
			align-items: center;

			text {
				&:nth-child(1) {
					font-size: 50rpx;
				}

				&:nth-child(2) {
					font-size: 30rpx;
				}
			}
		}

		.ui-screen {
			padding: 0 32rpx;
			display: flex;
			align-items: center;
			justify-content: space-between;
			height: 100rpx;
			font-size: 28rpx;

			.ui-screen-family {
				text {
					margin-right: 10rpx;
				}
			}
		}

		.ui-screen-icon {
			display: flex;
			flex-direction: row;
			align-items: center;

			>text {
				margin-left: 10rpx;
			}
		}

		.ui-msg {
			// overflow-y: scroll;
			height: calc(100vh - 330rpx - var(--window-bottom) - var(--status-bar-height));
		}

		.ui-empty {
			// background: #fff;
			height: 600rpx;
			display: flex;
			justify-content: center;
			align-items: center;
		}

		.ui-scroll {
			box-sizing: border-box;
			padding: 0 32rpx;
		}
	}

	.ui-sheet {
		border-bottom: 1rpx solid #e2e2e2;
		display: flex;
		align-items: center;
		justify-content: center;
		height: 120rpx;
		width: 100%;

		text {
			margin-left: 10rpx;
		}
	}

	.loading {
		margin-top: 200rpx;
	}
</style>
