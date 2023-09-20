<template>
	<view style="height:100%">
		<scroll-view scroll-y="true" @scrolltolower="pageNext" refresher-background="transparent"
			@refresherrefresh="onRefresh" :refresher-triggered="triggered" refresher-enabled :style="{ height: '100%' }"
			class="ui-scroll">
			<template v-if="loading">
				<view class="ui-loading">
					<u-loading-icon mode="semicircle" :vertical="true" text="加载中" textSize="18"></u-loading-icon>
				</view>
			</template>
			<template>
				<view v-if="msgList.length">
					<msg-card @call="handleCall" v-for="(item, index) in msgList" :key="(index+item.createTime)"
						:msgInfo="item"></msg-card>
					<u-loadmore marginBottom="30" dashed :status="loadmore" />
				</view>
			</template>
			<view class="ui-empty" v-if="!msgList.length&&!loading">
				<u-empty mode="list" text="暂无数据"></u-empty>
			</view>
		</scroll-view>
		<u-action-sheet :actions="contactsList" :show="show" title="选择紧急联系人" round="10" cancelText="取消"
			@close="show = false" @select="handleSelect"></u-action-sheet>
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
		computed: {
			contactsList() {
				const list = uni.$u.deepClone(this.$store.getters.contactList)
				const obj = {
					1: '第一紧急联系人',
					2: '第二紧急联系人',
					3: '第三紧急联系人',
				}
				return list.filter(n => {
					return n.phone !== '' && n.familyId === this.deviceInfo.familyId
				}).map(item => {
					item.name = `${obj[item.orderNum]} ${item.name}`
					return item
				})

			},
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
				loadmore: 'loadmore', // loadmore-加载更多 loading-加载中 nomore-没有更多
				show: false,
				/**是否初始化查询**/
				loading: true,
			}
		},
		watch: {
			deviceInfo: {
				handler(val) {
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
			handleCall() {
				if (!this.contactsList.length) return uni.$u.toast('您暂未设置联系人')
				this.show = true
			},
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
			readMsgAll() {
				const {
					familyId,
					deviceId,
					type
				} = this.deviceInfo
				console.log(this.deviceInfo, 'fff----')
				PostSetBatchMsgInfo({
					deviceType: type,
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
				this.loading = true;
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
					this.loading = false
				})
			},

			pageNext() {
				console.log('打印咯');
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
				this.$emit('refresh')
				// this.handleInit()
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