<!--
* @Author: zhanch
* @Date: 2023-01-05 10:58:39
* @FilePath: /pages/service/system-messages.vue
* @Description: 系统服务
-->

<template>
	<app-body>
		<view id="system">
			<app-logo text="系统消息"></app-logo>
			<view class="ui-list">
				<view class="ui-item" v-for="item in messageList" :key="item.noticeId">
					<view class="ui-date">
						{{ item.createTime }}
						<!-- 2022年10月11日 11：22 -->
					</view>
					<view class="ui-box active" @click="details(item)">
						<text>{{item.noticeTitle}}</text>
						<u-text size="25rpx" color="#666" :lines="3" :text="item.noticeContent"></u-text>
						<view class="ui-detail">
							<text>立即查看</text>
							<u-icon name="arrow-right" size="40rpx"></u-icon>
						</view>
					</view>
				</view>
			</view>
		</view>
	</app-body>
</template>

<script>
	import {
		GetSysNotice,
		postSetNoticeFlag,
	} from '@/common/http/api.js';
	export default {
		data() {
			return {
				messageList: [],
			}
		},
		methods: {
			details({
				noticeId,
				noticeContent
			}) {
				postSetNoticeFlag({
					noticeId
				}).then(res => {
					console.log(this.$store, 'ccc')
					this.$store.commit('setReadInfo', noticeContent);
					uni.navigateTo({
						url: `/pages/service/message-details`
					})
				});

			},
			initData() {
				GetSysNotice().then(res => {
					this.messageList = res.rows
				})
			}
		},
		mounted() {
			this.initData()
		}
	}
</script>

<style lang="scss">
	.ui-list {
		margin-top: 60rpx;
		width: 100%;
		box-sizing: border-box;
		padding: 0 44rpx 44rpx 44rpx;

		.ui-item {
			margin-top: 40rpx;
			box-sizing: border-box;
			text-align: center;

			&:nth-last-child(1) {
				margin-bottom: 30rpx;
			}

			.ui-date {
				display: inline-flex;
				align-items: center;
				justify-content: center;
				width: 300rpx;
				border-radius: 10rpx;
				padding: 10rpx;
				margin: 16rpx 0;
				font-size: 24rpx;
				color: #fff;
				background-color: rgba(0, 0, 0, 0.3);
			}

			.ui-box {
				margin-top: 20rpx;
				padding: 30rpx 20rpx 0 20rpx;
				border-radius: 10px;
				filter: drop-shadow(7.824px 10.382px 8px rgba(7, 5, 5, 0.08));
				background: #fff;
				text-align: left;

				text {
					width: 100%;

					&:nth-child(1) {
						margin-bottom: 10rpx;
						display: inline-block;
						font-size: 30rpx;

					}
				}

				.ui-detail {
					margin-top: 20rpx;
					display: inline-flex;
					align-items: center;
					justify-content: space-between;
					width: 100%;
					height: 80rpx;
					border-top: 1px solid #e2e2e2;

					text {
						font-size: 20rpx;
					}
				}
			}


		}
	}
</style>
