<template>
	<view class="">
		<!-- 	<view class="ui-action">
			<u-icon name="list" size="18"></u-icon>
			<text @click="readMsgAll">
				全部标记已处理
			</text>
		</view> -->
		<scroll-view scroll-y="true" @scrolltolower="pageNext" refresher-background="transparent"
			@refresherrefresh="onRefresh" :refresher-triggered="triggered" refresher-enabled
			:style="{height: srollHeight}" class="ui-scroll">
			<template v-if="msgList.length">
				<msg-card v-for="(item,index) in msgList" :key="index" :msgInfo="item"></msg-card>
				<u-loadmore marginBottom="30" dashed :status="loadmore" />
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
				pageOptions: {
					pageSize: 10,
					pageNum: 1,
				},
				/**加载更多管理**/
				loadmore: 'loadmore' // loadmore-加载更多 loading-加载中 nomore-没有更多
			}
		},
		watch: {
			deviceInfo: {
				handler(val) {
					console.log(val, 'data----------')
					if (val.deviceId) {
						this.msgList = []
						this.pageOptions = {
							pageSize: 10,
							pageNum: 1,
						}
						this.handleInit()
					}
				},
				immediate: true,
				deep: true,
			}
		},
		methods: {
			readMsgAll() {
				const {
					familyId,
					deviceId
				} = this.deviceInfo
				PostSetBatchMsgInfo({
					deviceType: "0",
					eventlevel: "",
					familyId,
					readFlag: "1",
					deviceId
				}).then(res => {
					uni.$u.toast('已处理登记成功')
					this.msgList = []
					this.loadmore = 'loadmore'
					this.pageOptions.pageNum = 1
					this.triggered = true;
					setTimeout(() => {
						this.handleInit()
					}, 500)
				})
			},
			handleInit() {
				this.loadmore = 'loadmore';
				const {
					familyId,
					deviceId
				} = this.deviceInfo;
				getMessage({
					familyId,
					deviceId,
					readFlag: 0,
					eventlevel: '',
					...this.pageOptions
				}).then(res => {
					const data = res.rows.map(n => {
						n.content = uni.$u.trim(n.content)
						return n
					})
					this.msgList = this.msgList.concat(data)
					this.loadmore = res.total <= this.msgList.length ? 'nomore' : 'loadmore';
				}).finally(() => {
					this.triggered = false
				})
			},

			pageNext() {
				if (this.loadmore === 'nomore') return;
				this.loadmore = 'loading';
				this.pageOptions.pageNum++
				this.handleInit()
			},
			onRefresh() {
				this.loadmore = 'loadmore'
				this.pageOptions.pageNum = 1
				this.triggered = true;
				this.msgList = []
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

		text {
			margin-left: 4rpx;
		}
	}
</style>