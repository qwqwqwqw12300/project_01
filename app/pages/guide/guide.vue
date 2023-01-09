<template>
	<view class="ui-container">
		<view class="ui-img">
			<!-- <image src="../../static/componentIndex.png" mode="aspectFit"></image> -->
		</view>
		<u-cell-group>
			<u-cell v-for="(item, index) in list" :key="item.id" :title="item.name" isLink :url="item.url"></u-cell>
		</u-cell-group>
		<button @click="localPush">本地推送</button>
		<button @click="getRegistrationID">用户id</button>

	</view>
</template>
<script>
	import {
		push
	} from '@/common/sdk/push.js';
	export default {
		props: {
			hasLeftWin: {
				type: Boolean
			},
			leftWinActive: {
				type: String
			}
		},
		data() {
			return {
				list: [{
					id: 'range',
					url: '/pages/guide/range',
					name: '检测范围'
				}, ]
			}
		},
		methods: {
			localPush() {
				try {
					push.addLocalNotification();
				} catch (e) {
					console.log(e);
				}

			},
			getRegistrationID() {
				push.getRegistrationID().then(res => {
					console.log(res, 'getRegistrationID');
				});
			}
		},
	}
</script>

<style lang="scss">
	.ui-img {
		height: 150rpx;
		width: 100%;
		text-align: center;
		display: flex;
		justify-content: center;
		align-items: center;
	}

	.ui-container {
		height: 100%;
		width: 100%;
		background: #fff;
	}
</style>
