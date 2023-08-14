<template>
	<view class="app-main">
		<view class="equipment" v-for="(item, index) in imgList" :style="{'padding-right': index === 3 || index % 3 ? '0' : '10rpx'}" :key="index">
			<image class="equipment-icon" :src="item.img" mode=""></image>
			<view class="equipment-name">{{item.name}}</view>
			<view>({{getNum(item.type)}})</view>
		</view>
	</view>
</template>

<script>
	import {
		mapState,
	} from 'vuex';
	export default {
		props: {
			list: {
				type: Array,
				default: () => []
			}
		},
		data() {
			return {
				imgList: [
					{
						img: require('@/static/images/sos/sos-equipment_2.png'),
						name: '紧急按钮',
						type: '02'
					},
					// {
					// 	img: require('@/static/images/sos/sos-equipment_2.png'),
					// 	name: '4G网关'
					// },
					{
						img: require('@/static/images/sos/sos-equipment_3.png'),
						name: '门磁报警器',
						type: '02'
					},
					{
						img: require('@/static/images/sos/sos-equipment_4.png'),
						name: '水侵报警器',
						type: '08'
					},
					{
						img: require('@/static/images/sos/sos-equipment_5.png'),
						name: '燃气报警器',
						type: '04'
					},
					{
						img: require('@/static/images/sos/sos-equipment_6.png'),
						name: '烟雾报警器',
						type: '03'
					},
					{
						img: require('@/static/images/sos/sos-equipment_7.png'),
						name: '遥控器',
						type: '06'
					},
					{
						img: require('@/static/images/sos/sos-equipment_8.png'),
						name: '红外报警器',
						type: '01'
					},
					// {
					// 	img: require('@/static/images/sos/sos-equipment_9.png'),
					// 	name: 'SOS手环'
					// }
				]
			}
		},
		onShow() {
			this.getList();
		},
		computed: {
			...mapState({
				deviceInfo: state => state.deviceInfo
			}, ),
			getNum() {
				return function(type) {
					let num = 0
					this.list.map((item, index) => {
						console.log(item, 'asdasdasdasd')
						if(type === item.accessoryType) {
							console.log(item.num, 'aaaaaaaaaa')
							num = item.num
						}
					})
					return num
				}
			}
		},
		methods: {
			
		}
	}
</script>

<style lang="scss" scoped>
	.app-main {
		height: 100%;
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