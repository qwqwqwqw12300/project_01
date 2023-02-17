<template>
	<view class="ui-box">
		<view style="padding: 20rpx 30rpx;">
			<view class="box-title">
				<text class="device-type">雷达波设备</text>
				<text class="event-date">{{$u.timeFormat(msgDetail.createTime, 'mm/dd hh:MM:ss') || '--'}}</text>
				<view class="event-status">
					<text class="urgent common" v-if="msgDetail.eventLevel === 'urgent'">紧急事件</text>
					<text class="nourgent common" v-else>一般事件</text>
					<text class="nodeal common" v-if="msgDetail.operateFlag === '0'">未处理</text>
					<text class="deal common" v-else>已处理</text>
				</view>
			</view>
			<text class="device-own">
				{{msgDetail.deviceName || '未命名设备'}}
			</text>
			<view class="content">
				<!-- {{item.content || '--'}} -->
				<u-read-more :showHeight="60" :toggle="true" closeText="展开" openText="关闭">
					<rich-text :nodes="$u.trim(msgDetail.content, 'all') || '--'"></rich-text>
				</u-read-more>
				<!-- 设备张三的雷达监测到有人摔倒、请及时处理 -->
			</view>
		</view>
		<view class="action">
			<view class="detail" @click="handleRead(msgDetail.msgId)">我明白了</view>
			<view class="phone" @click="handleCall(msgDetail.phone)">
				<u-icon name="phone" color="white" size="22"></u-icon><text style="margin-left: 8rpx;">亲人处理</text>
			</view>
		</view>
		<u-action-sheet :actions="contactsList" :show="show" title="选择紧急联系人" round="10" cancelText="取消"
			@close="show = false" @select="handleSelect"></u-action-sheet>
	</view>
</template>

<script>
	import {
		PostSetMsgInfo
	} from '@/common/http/api';
	export default {
		props: {
			msgInfo: {
				type: Object,
				default: () => {},
			},
		},
		data() {
			return {
				show: false,
				msgDetail: this.msgInfo,
			}
		},
		computed: {
			contactsList() {
				const list = uni.$u.deepClone(this.$store.getters.contactList)
				const obj = {
					1: '第一紧急联系人',
					2: '第二紧急联系人',
					3: '第三紧急联系人',
				}
				return list.filter(n => {
					return n.phone !== ''
				}).map(item => {
					item.name = `${obj[item.orderNum]} ${item.name}`
					return item
				})
			}
		},
		mounted() {

		},
		methods: {
			/**
			 * 单条用户信息已读
			 */
			handleRead(msgId) {
				PostSetMsgInfo({
					msgId,
					msgFlag: '1'
				}).then(res => {
					uni.$u.toast('标记成功')
					this.msgDetail.msgFlag = '1'
				})
			},
			/**
			 * 点击联系人
			 */
			handleCall(phoneNumber) {
				if (!this.contactsList.length) return uni.$u.toast('您暂未设置联系人')
				this.show = true
			},
			/**
			 * 选择联系人
			 */
			handleSelect(val) {
				uni.makePhoneCall({
					phoneNumber: val.phone,
					success: res => {
						console.log(res, 'res');
					},
					fail: res => {
						console.log(res, 'fail');
					}
				});
			},
		}
	}
</script>

<style lang="scss" scoped>
	.ui-box {
		background: #FFFFFF;
		box-shadow: 0px 2px 20px 0px rgba(220, 205, 180, 0.5);
		border-radius: 20rpx;
		margin-bottom: 30rpx;
		// padding: 20rpx 30rpx;

		.box-title {
			height: 60rpx;
			display: flex;
			align-items: center;
			justify-content: space-between;

			.device-type {
				font-size: 30rpx;
				color: #888888;
			}

			.event-date {
				flex: 1;
				padding-left: 30rpx;
				font-size: 28rpx;
				color: #888888;
			}

			.event-status {

				.common {
					font-size: 22rpx;
					color: #FFFFFF;
					padding: 4rpx 12rpx;
					border-radius: 4px;
					margin: 0 6rpx;
				}

				.urgent {
					background-color: #EF3B45;
				}

				.nourgent {
					background-color: #1DC666;
				}

				.nodeal {
					background-color: #F8E6E6;
					color: #AC0000 !important;
				}

				.deal {
					background-color: #E6F8EE;
					color: #00AC55 !important;
				}
			}
		}

		.device-own {
			margin-top: 10rpx;
			font-size: 36rpx;
			color: #353535;
			font-weight: 550;
			line-height: 50rpx;
		}

		.content {
			padding: 14rpx 0;
			font-size: 28rpx;
			color: #353535;
			// letter-spacing: 0;
			// text-align: left;
			// font-weight: 500;
			// overflow: hidden;
			// text-overflow: ellipsis;
			// display: -webkit-box;
			// -webkit-line-clamp: 2;
			// -webkit-box-orient: vertical;
		}

		.action {
			border-top: 1px solid rgba(0, 0, 0, 0.1);
			margin-top: 30rpx;
			height: 90rpx;
			line-height: 90rpx;
			display: flex;

			.detail {
				flex: 1;
				background-color: #fff;
				color: #5E85F8;
				text-align: center;
				border-radius: 0 0 0 20rpx;
			}

			.phone {
				flex: 1;
				display: flex;
				justify-content: center;
				align-items: center;
				background-image: linear-gradient(90deg, #FFB24D 0%, #FD913B 100%);
				// text-align: center;
				border-radius: 0 0 20rpx 0 !important;
				color: #fff;
			}
		}
	}
</style>
