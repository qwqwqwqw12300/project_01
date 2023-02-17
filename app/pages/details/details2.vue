<!--
* @Author: zhanch
* @Date: 2022-12-28 11:01:47
* @FilePath: /pages/details/details.vue
* @Description: 信息详情
-->
<template>
	<app-body :bg="false" :hideTitle="true">
		<view class="ui-body">
			<view class="ui-title">
				<text>详情</text>
				<text class="active">全部标记已读</text>
			</view>
			<view class="ui-screen">
				<view class="ui-family active">
					<u-text suffixIcon="arrow-down-fill" iconStyle="font-size: 35rpx" size="32rpx" :text="'我的家庭'">
					</u-text>
				</view>
				<view class="ui-screen-icon active">
					<u-icon size="32rpx" name="../../static/images/screen.png"></u-icon>
					<text>筛选</text>
				</view>
			</view>
			<!-- 消息列表 -->
			<view class="ui-msg">
				<scroll-view scroll-y="true" @refresherrefresh="onRefresh" :refresher-triggered="triggered"
					refresher-enabled refresher-background="transparent">
					<template v-if="messageList.length">
						<view class="ui-scroll">
							<msg-card v-for="(item, index) of messageList" :key="index" :msgInfo="item"></msg-card>
						</view>

					</template>
					<view class="ui-empty" v-else>
						<u-empty mode="list" text="暂无数据"></u-empty>
					</view>

				</scroll-view>
			</view>
			<!-- /消息列表 -->
		</view>
	</app-body>
</template>

<script>
	import {
		mapState
	} from 'vuex';
	import {
		getMessage
	} from '../../common/http/api';
	export default {
		data() {
			return {
				/**事件信息**/
				eventInfo: {
					/**家庭id**/
					familyId: '',
					/**开始时间**/
					startDate: '',
					/**结束时间**/
					endDate: '',
					/**事件等级**/
					eventlevel: '',
					/**是否已读**/
					readFlag: '',
					/**设备类型**/
					deviceType: '',
				},
				/**消息列表**/
				messageList: [],
				/**下拉状态**/
				triggered: false
			};
		},
		computed: {
			...mapState({
				getFamilyList: state => {
					const list = state.familyList;
					return list.map(ele => ({
						value: ele.familyId,
						text: ele.name
					}));
				}
			})
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
				const msgFlags = this.list.map(ele => {
					return {
						msgId: ele.msgId,
						msgFlag: '1'
					}
				});
				PostSetBatchMsgInfo({
					msgFlags
				}).then(res => {
					this.getMsgList();
				})
			},

			/**
			 * 查询设备消息
			 */
			getMsgList() {
				return new Promise(resolve => {
					getMessage({
						...this.eventInfo,
					}).then(res => {
						this.messageList = res.rows || [];
						resolve();
					}, err => resolve());
				});
			},


		}
	}
</script>

<style lang="scss">
	.ui-body {
		color: #353535;

		.ui-title {
			padding: 34rpx 32rpx;
			background: #fff;
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

			.ui-family {
				width: 180rpx;
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
			overflow-y: scroll;
			height: calc(100vh - 300rpx - var(--window-bottom) - var(--status-bar-height));
		}

		.ui-empty {
			background: #fff;
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
</style>
