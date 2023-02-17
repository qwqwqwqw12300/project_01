<template>
	<view class="">
		<view class="ui-action">
			<u-icon name="list" size="18"></u-icon>
			<text @click="readMsgAll">
				全部标记已读
			</text>
		</view>
		<scroll-view scroll-y="true" @refresherrefresh="onRefresh" :refresher-triggered="triggered" refresher-enabled
			:style="{height: srollHeight}" class="ui-scroll">
			<template v-if="msgList.length">
				<msg-card v-for="(item,index) in msgList" :key="index" :msgInfo="item"></msg-card>
			</template>
			<u-empty v-else mode="list" text="暂无数据" marginTop="80rpx"></u-empty>
		</scroll-view>
	</view>
</template>

<script>
	import {
		getMessage,
		PostSetBatchMsgInfo,
	} from '@/common/http/api';
	export default {
		props: {
			deviceInfo: {
				type: Object,
				default: () => {},
			},
			srollHeight: {
				type: String,
				default: '1000rpx',
			}
		},
		data() {
			return {
				triggered: false,
				msgList: [],
			}
		},
		watch: {
			deviceInfo: {
				handler(val) {
					if (val.deviceId) {
						this.handleInit()
					}
				},
				immediate: true,
				deep: true,
			}
		},
		methods: {
			readMsgAll() {
				const msgFlags = this.msgList.map(ele => {
					return {
						msgId: ele.msgId,
						msgFlag: '1'
					}
				});
				PostSetBatchMsgInfo({
					msgFlags
				}).then(res => {
					uni.$u.toast('全部标记成功')
					setTimeout(() => {
						this.handleInit()
					}, 500)
				})
			},
			handleInit() {
				const {
					type,
					familyId,
					deviceId
				} = this.deviceInfo;
				getMessage({
					type,
					familyId,
					deviceId,
					readFlag: 1,
					eventlevel: ''
				}).then(res => {
					this.msgList = res.rows.map(n => {
						n.content = uni.$u.trim(n.content)
						return n
					})
				}).finally(() => {
					this.triggered = false
				})
			},


			onRefresh() {
				this.triggered = true;
				this.handleInit()
			}
		}
	}
</script>

<style lang="scss" scoped>
	.ui-action {
		// width: 100%;
		// text-align: center;
		color: #333;
		font-size: 28rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		// line-height: 30rpx;
		margin-bottom: 16rpx;
		text{
			margin-left: 4rpx;
		}
	}
</style>
