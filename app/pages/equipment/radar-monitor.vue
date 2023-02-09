<template>
	<view>
		<monitor-echarts ref="echartsRef"></monitor-echarts>
		<view class="ui-tip">
			<view class="ui-item">
				<view class="span1"></view>
				<text>隐私区域</text>
			</view>
			<view class="ui-item">
				<view class="span2"></view>
				<text>监控区域</text>
			</view>
			<view class="ui-item">
				<u-icon name="../../static/images/person.png" color="#fff" size="50rpx"></u-icon>
				<text>人员</text>
			</view>
			<view class="ui-item">
				<u-icon name="../../static/images/person-down.png" color="#fff" size="44rpx"></u-icon>
				<text>跌倒</text>
			</view>
		</view>
		<view class="ui-set">
			<button @click="handleDetail">查看明细</button>
			<button @click="handleSet">设置</button>
		</view>
		<u-popup :closeable="true" :round="10" :show="show" mode="center" @close="show=false">
			<view class="ui-popup">
				<scroll-view scroll-y="true" @refresherrefresh="onRefresh" :refresher-triggered="triggered"
					refresher-enabled style="height: 580px" class="ui-scroll">
					<template v-if="messageList.length">
						<template v-for="(item, index) of messageList">
							<view class="ui-message active" @click="open(item.msgId)"
								v-if="item.eventLevel !== 'urgent'" :key="index + 'list'">
								<view class="ui-content-time">
									<u-icon name="chat" color="#414141" size="40rpx"></u-icon>
									<text>{{$u.timeFormat(item.createTime, 'mm/dd hh:MM:ss') || '--'}}</text>
									<text class="ui-content-name">{{item.deviceName || '未命名设备'}}</text>
								</view>
								<text class="ui-content">{{item.content || '--'}}</text>
							</view>
							<!-- /sos事件 -->
							<view class="ui-sos active" v-else @click="open(item.msgId)" :key="index + 'listSos'">
								<view>
									<view class="ui-content-time">
										<u-icon name="arrow-right" color="#414141" size="40rpx"></u-icon>
										<text>{{$u.timeFormat(item.createTime, 'mm/dd hh:MM:ss') || '--'}}</text>
										<text class="ui-content-name">{{item.deviceName || '未命名设备'}}</text>
									</view>
									<text class="ui-content">{{item.content || '--'}}</text>
								</view>
								<view class="ui-sos-btn active" v-if="contactsList[0]"
									@click="call(contactsList[0].phone)">
									<u-text prefixIcon="phone" align="center" :block="false"
										iconStyle="font-size: 40rpx" size="24rpx" text="紧急电话"></u-text>
									<text>{{contactsList[0].phone}}</text>
								</view>
							</view>
						</template>
						<!-- /sos事件 -->
					</template>
					<u-empty v-else mode="list" text="暂无数据" marginTop="80rpx"></u-empty>
				</scroll-view>
			</view>
		</u-popup>
	</view>


</template>

<script>
	import {
		mapState
	} from 'vuex';
	import {
		GetStartDevice,
		GetEndDevice,
		getMessage,
	} from '../../common/http/api';
	export default {
		data() {
			return {
				show: false,
				triggered: false,
				messageList: [],
				/**紧急联系人**/
				contactsList: []
			}
		},
		computed: {
			...mapState({
				deviceInfo: state => state.deviceInfo
			})
		},
		onUnload() {
			this.handleEnd()
		},
		onLoad() {
			this.handleStart()
		},
		mounted() {

		},
		methods: {
			onRefresh() {

			},
			close() {

			},
			handleStart() {
				GetStartDevice({
					deviceId: this.deviceInfo.deviceId
				}).then(res => {
					// this.handleQuery()
				})
			},
			handleEnd() {
				GetEndDevice({
					deviceId: this.deviceInfo.deviceId
				})
			},
			getMessage() {
				return new Promise(resolve => {
					const {
						type,
						familyId,
						deviceId
					} = this.deviceInfo;
					getMessage({
						deviceType: type,
						familyId,
						deviceId,
						readFlag: this.navActive,
						eventlevel: ''
					}).then(res => {
						this.messageList = res.rows || [];
						resolve(this.messageList);
					})
				});
			},
			handleDetail() {
				console.log(943785778)
				this.getMessage()
				this.show = true
			},
			handleSet() {

			}
		}
	}
</script>

<style lang="scss">
	.ui-tip {
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0rpx 70rpx;
		margin-top: -30rpx;
	}

	.ui-item {
		display: flex;
		flex-direction: row;
		align-items: center;

		.span1 {
			width: 60rpx;
			height: 40rpx;
			border-radius: 10rpx;
			border: 1px solid gray;
			background-color: #ffffff;
			margin-right: 10rpx;
		}

		.span2 {
			width: 60rpx;
			height: 40rpx;
			border-radius: 10rpx;
			border: 1px solid gray;
			background-color: #CCFFCC;
			margin-right: 14rpx;
		}

		text {
			font-size: 24rpx;
		}
	}


	.ui-set {
		display: flex;
		margin-top: 50rpx;

		button {
			padding-left: 100rpx;
			padding-right: 100rpx;
			font-size: 28rpx;
			border-radius: 8px !important;
		}
	}

	.ui-popup {
		box-sizing: border-box;
		width: calc(100vw - 50rpx);
		border-radius: 20rpx;
		padding: 20rpx 20rpx;
		padding-top: 40rpx;
		height: 630px;
	}

	.ui-scroll {
		margin-top: 20rpx;
		//height: calc(100vh - (var(--window-bottom) + 610rpx + 120rpx + var(--status-bar-height)));

		.ui-message,
		.ui-sos {
			margin-top: 20rpx;
			border-radius: 10rpx;
			font-size: 28rpx;
			filter: drop-shadow(7.824rpx 10.382rpx 8rpx rgba(7, 5, 5, 0.08));
			background-image: linear-gradient(96deg, #f5f5f5 0%, #e5e5e5 100%);
			color: #414141;
			display: flex;
			// align-items: center;
			flex-direction: column;
			min-height: 80rpx;
			padding: 20rpx 32rpx;

			.ui-content-time {
				display: flex;
				flex-direction: row;
				align-items: center;
			}

			.ui-content-name {
				margin-left: 20rpx;
			}

			.ui-content {
				color: #777;
				margin: 15rpx 15rpx 0 38rpx;
				font-size: 30rpx;
				// width: 100%;
				overflow: hidden;
				white-space: nowrap;
				text-overflow: ellipsis;
			}
		}

		.ui-message {
			color: #414141;
			display: flex;
			// align-items: center;
			flex-direction: column;

		}
	}
</style>
