<template>
	<u-action-sheet :closeOnClickOverlay="true" :closeOnClickAction="true" :safeAreaInsetBottom="safeShow"
		@close="closeHandle" :show="isShow" cancelText="取消">
		<view>
			<view @click="sheetSelect(item)" class="ui-sheet" v-for="(item, index) of addHandle.list"
				:key="'sheet' + index">
				<u-icon :name="item.icon" class="active" color="#fff" size="48rpx">
				</u-icon>
				<text>{{item.name}}</text>
			</view>
		</view>
	</u-action-sheet>
</template>

<script>
	import {
		isProd
	} from '@/common/utils/util.js'
	export default {
		props: {
			isShow: {
				default: false
			},
			safeShow: {
				default: false
			}
		},
		data() {
			return {
				addHandle: {
					show: false,
					list: [{
							name: '跌倒检测器',
							icon: '/static/images/leida-nm.png',
							url: '/pages/equipment/add/radar'
						},
						{
							name: '电子牵挂卡',
							icon: '/static/images/dzqgk.png',
							url: '/pages/equipment/add/add-card',
							// url: '/pages/equipment/watch-detail/watch-detail',
						},
						{
							name: '4G健康手表',
							icon: '/static/images/watch.png',
							url: '/pages/equipment/add/monitor'
						}
					]
				},
			}
		},
		methods: {
			/**
			 * 创建设备
			 */
			sheetSelect({
				url,
				name
			}) {

				if (isProd()) {
					if (name === '电子牵挂卡' || name === '监测手表') return uni.$u.toast('暂不支持添加该设备');
				}
				this.$emit('cancle')
				uni.navigateTo({
					url
				});
			},
			closeHandle() {
				this.$emit('cancle')
			}
		}
	}
</script>

<style lang="scss">
	.ui-sheet {
		border-bottom: 1rpx solid #e2e2e2;
		display: flex;
		align-items: center;
		justify-content: center;
		height: 120rpx;
		width: 100%;

		text {
			margin-left: 20rpx;
			font-size: 34rpx;
			color: #000000;
			text-align: center;
			line-height: 34rpx;
			font-weight: 400;
		}
	}

	::v-deep .u-action-sheet__cancel-text {
		font-size: 36rpx;
		color: #599FFF;
		line-height: 36rpx;
		font-weight: 500;
	}
</style>
