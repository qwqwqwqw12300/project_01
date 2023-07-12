<template>
	<view class="app-main">
		<!-- <view class="equipment" v-for="(item, index) in imgList" :style="{'padding-right': index === 3 || index % 3 ? '0' : '10rpx'}" :key="index">
			<image class="equipment-icon" :src="item.img" mode=""></image>
			<view class="equipment-name">{{item.name}}</view>
		</view> -->
	</view>
</template>

<script>
	import {
		GetLastPoint,
		GetsetAddressBook,
		GetNowLocation
	} from '@/common/http/api';
	export default {
		data() {
			return {
				// imgList: [
				// 	{
				// 		img: require('@/static/images/sos/sos-equipment_1.png'),
				// 		name: '4G网关'
				// 	},
				// 	{
				// 		img: require('@/static/images/sos/sos-equipment_2.png'),
				// 		name: '紧急按钮'
				// 	},
				// 	{
				// 		img: require('@/static/images/sos/sos-equipment_3.png'),
				// 		name: '门磁报警器'
				// 	},
				// 	{
				// 		img: require('@/static/images/sos/sos-equipment_4.png'),
				// 		name: '水侵报警器'
				// 	},
				// 	{
				// 		img: require('@/static/images/sos/sos-equipment_5.png'),
				// 		name: '声光警号'
				// 	},
				// 	{
				// 		img: require('@/static/images/sos/sos-equipment_6.png'),
				// 		name: '烟雾报警器'
				// 	},
				// 	{
				// 		img: require('@/static/images/sos/sos-equipment_7.png'),
				// 		name: '遥控器'
				// 	},
				// 	{
				// 		img: require('@/static/images/sos/sos-equipment_8.png'),
				// 		name: '红外报警器'
				// 	},
				// 	{
				// 		img: require('@/static/images/sos/sos-equipment_9.png'),
				// 		name: '紧急按钮'
				// 	}
				// ]
			}
		},
		mounted() {
			
		},
		computed: {
			
		},
		methods: {
			
		}
	}
</script>

<style lang="scss" scoped>
	.app-main {
		display: flex;
		flex-wrap: wrap;
	}
	.equipment {
		width: 33.3%;
		box-sizing: border-box;
		text-align: center;
		padding: 10rpx 10rpx 28rpx 0;
		// display: flex;
		// justify-content: center;
		// align-items: center;
		// flex-wrap: wrap;
		&-icon {
			width: 100rpx;
			height: 100rpx;
		}
		&-name {
			width: 100%;
		}
	}
</style>