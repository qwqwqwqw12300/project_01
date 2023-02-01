<!--
* @Author: zhanch
* @Date: 2023-01-06 15:44:29
* @FilePath: /components/msg-list/msg-list.vue
* @Description: 消息列表
-->

<template>
	<view>
		<view class="ui-nav" v-if="needNav">
			<u-tabs lineWidth="130rpx" lineColor="#fec92e"
				:itemStyle="{width: '180rpx', height: '80rpx', fontSize: '30rpx'}" :list="navList" @click="navClick">
			</u-tabs>
		</view>
		<!-- 已读按钮 -->
		<view class="ui-read active" @click="readMsgAll">
			<u-text prefixIcon="checkmark-circle" align="center" :iconStyle="{fontSize: '30rpx', color: '#fff'}"
				color="#fff" size="24rpx" text="全部标记为已读"></u-text>
		</view>
		<!-- /已读按钮 -->
		<!-- 未读事件列表 -->
		<scroll-view scroll-y="true" @refresherrefresh="onRefresh" :refresher-triggered="triggered" refresher-enabled
			:style="{height: srollHeight}" class="ui-scroll">
			<template v-if="list.length">
				<template v-for="(item, index) of list">
					<view class="ui-message active" @click="open(item.msgId)" v-if="item.eventLevel !==0"
						:key="index + 'list'">
						<u-icon name="chat" color="#414141" size="40rpx"></u-icon>
						<text>{{$u.timeFormat(item.createTime, 'mm/dd hh:MM:ss') || '--'}}</text>
						<text class="ui-content-date">{{item.deviceName || '--'}}</text>
						<text class="ui-content">{{item.content || '--'}}</text>
					</view>
					<!-- /sos事件 -->
					<view class="ui-sos active" v-else @click="open(item.msgId)" :key="index + 'listSos'">
						<view>
							<u-icon name="arrow-right" color="#414141" size="40rpx"></u-icon>
							<text>{{$u.timeFormat(item.createTime, 'mm/dd hh:MM:ss') || '--'}}</text>
							<text class="ui-content-date">{{item.deviceName || '--'}}</text>
							<text class="ui-content">{{item.content || '--'}}</text>
						</view>
						<view class="ui-sos-btn active" v-if="contactsList[0]" @click="call(contactsList[0].phone)">
							<u-text prefixIcon="phone" align="center" :block="false" iconStyle="font-size: 40rpx"
								size="24rpx" text="紧急电话"></u-text>
							<text>{{contactsList[0].phone}}</text>
						</view>

					</view>
				</template>
				<!-- /sos事件 -->
			</template>
			<u-empty v-else mode="list" text="暂无数据" marginTop="80rpx"></u-empty>
		</scroll-view>
		<!-- /未读事件列表 -->
		<u-popup :closeable="true" :round="10" :show="show" mode="center" @close="close">
			<view class="ui-pop-box">
				<view class="ui-pop-msg">
					<view>
						<text>设备名称</text>
						<text>{{details.deviceName}}</text>
					</view>
					<view>
						<text>所属家庭</text>
						<text>2022-01-01 00：00：00</text>
					</view>
					<view>
						<text>所属房间</text>
						<text>房间名称</text>
					</view>
					<view>
						<text>时间</text>
						<text>{{details.createTime || '--'}}</text>
					</view>
					<view>
						<text>内容</text>
						<text>{{details.content || '--'}}</text>
					</view>
				</view>
				<view class="ui-sos-list">
					<u-button icon="phone" @click="call(item.phone)" :text="item.name" size="small" :key="index"
						v-for="(item, index) of contactsList"></u-button>
				</view>
				<view class="ui-pop-btn">
					<button @click="readMsg(details.msgId)">我知道了</button>
				</view>
			</view>
		</u-popup>
	</view>
</template>

<script>
	import {
		GetContactsList,
		PostMessageDeatil,
		PostSetMsgInfo,
		PostGetMessage,
		PostSetBatchMsgInfo
	} from '../../common/http/api';
	export default {
		props: {
			/**是否需要导航栏**/
			needNav: {
				default: true
			},
			/**混动容器高度**/
			srollHeight: String,
			/**消息列表**/
			list: {
				default: []
			}
		},
		data() {
			return {
				/**消息列表**/
				navList: [{
					name: '未读',
				}, {
					name: '已读',
				}],
				/**查询中标志**/
				triggered: true,
				/**是否展示浮层**/
				show: false,
				/**消息详情**/
				details: {},
				/**紧急联系人**/
				contactsList: []
			};
		},
		mounted() {
			this.getContactsList();
		},
		methods: {
			navClick({
				index
			}) {
				this.$emit('navClick', index);
			},
			/**
			 * 上拉刷新
			 * @param {Object} $e
			 */
			onRefresh($e) {
				const close = () => {
					this.triggered = false;
				}
				this.triggered = true;
				// setTimeout(() => {
				// 	this.triggered = false;
				// }, 3000);
				this.$emit('onRefresh', close);
				console.log($e);
			},
			/**
			 * 关闭详情
			 */
			close() {
				this.show = false;
			},

			/**
			 * 单条用户信息已读
			 */
			readMsg(msgId) {
				PostSetMsgInfo({
					msgId,
					msgFlag: '1'
				}).then(res => {
					this.close();
				})
			},

			/**
			 * 单条用户信息已读
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
					this.close();
				})
			},

			/**
			 * 打开浮层
			 */
			open(msgId) {
				if (msgId) {
					PostMessageDeatil({
						msgId
					}).then(res => {
						this.details = res.rows[0] || {};
						this.show = true;
					})
				}

			},

			/**
			 * 获取紧急联系人
			 */
			getContactsList() {
				GetContactsList({}).then(res => {
					this.contactsList = res.rows || [];
				});
			},

			call(phoneNumber) {
				console.log(phoneNumber, 'phoneNumber');
				uni.makePhoneCall({
					phoneNumber,
					success: res => {
						console.log(res, 'res');
					},
					fail: res => {
						console.log(res, 'fail');
					}
				});
			}
		}
	}
</script>

<style lang="scss">
	.ui-nav {
		display: flex;
		align-items: center;
		justify-content: center;
		width: 100%;
		height: 80rpx;
		border-radius: 10rpx;
		margin-top: 20rpx;
		filter: drop-shadow(7.824px 10.382px 8px rgba(7, 5, 5, 0.08));
		background-image: linear-gradient(96deg, #f5f5f5 0%, #e5e5e5 100%);
		text-align: center;
	}

	.ui-read {
		margin-top: 20rpx;
		background-color: rgba(0, 0, 0, 0.4);
		width: 100%;
		height: 60rpx;
		line-height: 60rpx;
		border-radius: 10rpx;
	}

	.ui-scroll {
		margin-top: 20rpx;
		//height: calc(100vh - (var(--window-bottom) + 610rpx + 120rpx + var(--status-bar-height)));

		.ui-message,
		.ui-sos {
			margin-top: 20rpx;
			border-radius: 10rpx;
			padding: 0 20rpx;
			font-size: 24rpx;
			filter: drop-shadow(7.824rpx 10.382rpx 8rpx rgba(7, 5, 5, 0.08));
			background-image: linear-gradient(96deg, #f5f5f5 0%, #e5e5e5 100%);
		}

		.ui-message {
			color: #414141;
			display: flex;
			align-items: center;
			min-height: 80rpx;

			&>* {
				margin-left: 10rpx;
				text-align: left;

				// min-width: 100rpx;
				&:nth-child(2) {
					margin-left: 2rpx;
				}

				// &:nth-child(3) {
				// 	width: 120rpx;
				// }
			}


			.ui-content {
				width: 280rpx;
				overflow: hidden;
				white-space: nowrap;
				text-overflow: ellipsis;
			}

			.ui-content-date {
				width: 100rpx;
				overflow: hidden;
				white-space: nowrap;
				text-overflow: ellipsis;
			}
		}

		.ui-sos {
			padding: 20rpx;
			min-height: 200rpx;

			&>view {
				display: flex;
				align-items: center;

				&>* {
					margin-left: 15rpx;
					text-align: left;

					// min-width: 100rpx;
					&:nth-child(2) {
						margin-left: 2rpx;
					}
				}

				&:nth-child(1) {
					margin-bottom: 30rpx;
				}
			}
		}
	}

	.ui-sos-btn {
		flex-direction: column;
		justify-content: center;
		margin: 0 auto;
		padding: 20rpx;
		width: 545rpx;
		height: 120rpx;
		border-radius: 10rpx;
		background-image: linear-gradient(-43deg, #fdc92d 0%, #ffe383 100%);

		&>* {
			flex: 1;
			font-size: 24rpx;
			color: #414141;
		}
	}

	.ui-pop-box {
		box-sizing: border-box;
		width: 582rpx;
		border-radius: 20rpx;
		filter: drop-shadow(0 0 5rpx rgba(7, 5, 5, 0.34));
		background-image: linear-gradient(-36deg, #e4e4e4 0%, #f8f8f8 100%);
		border: 1px solid #ffcb3d;
		padding: 53rpx 31rpx;

		.ui-pop-msg {
			>view {
				margin-top: 20rpx;
				display: flex;
				justify-content: left;
				align-items: top;

				&:nth-child(1) {
					margin-top: 60rpx;
				}

				text {
					display: inline-block;
					font-size: 25rpx;
					color: #999;
					width: 20%;
					text-align: right;

					&:nth-child(2) {
						text-align: left;
						color: #444;
						width: 70%;
						margin-left: 40rpx;
					}

				}
			}

		}

		.ui-sos-list {
			margin-top: 60rpx;
			display: flex;
			align-items: center;
			justify-content: center;

			button {
				width: 160rpx;
				background-image: linear-gradient(-43deg, #fdc92d 0%, #ffe383 100%);
				border: 1px solid #ffcb3d;
				box-shadow: unset;
			}
		}

		.ui-pop-btn {
			width: 100%;
			text-align: center;
			margin-top: 60rpx;

			button {
				width: 260rpx;
				height: 80rpx;
				line-height: 80rpx;
				font-size: 32rpx;
			}
		}
	}
</style>
