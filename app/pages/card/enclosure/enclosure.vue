<template>
	<app-body :bg="false" :bodyStyle="{backgroundColor:'#FFF'}">
		<view style="margin-bottom: 50rpx;">
			<app-logo color="#353535" text="设置电子围栏"></app-logo>
		</view>
		<view class="tab">
			<view class="tab-box">
				<text @tap="handleTab(item.key)" class="tab-item" :class="{'active': tabKey === item.key }"
					v-for="item in tabList" :key="item.key">
					{{ item.name }}
				</text>
			</view>
		</view>
		<component ref="comRef" :is="tabKey"></component>
	</app-body>
</template>

<script>
	import circleMap from './components/circle.vue'
	import polygonMap from './components/polygon.vue'
	export default {
		components: {
			circleMap,
			polygonMap,
		},
		data() {
			return {
				tabList: [{
					key: 'circleMap',
					name: '圆形'
				}, {
					key: 'polygonMap',
					name: '多边形'
				}],
				tabKey: 'circleMap',
			}
		},
		methods: {
			handleTab(key) {
				this.tabKey = key
			},
		}
	}
</script>

<style lang="scss" scoped>
	.tab {
		position: absolute;
		left: 32rpx;
		// top: 800rpx;
		bottom: 250rpx;
		z-index: 9999;

		.tab-box {
			width: 260rpx;
			height: 70rpx;
			padding: 0 10rpx;
			// box-sizing: border-box;
			background-color: #fff;
			border-radius: 28rpx;
			display: flex;
			align-items: center;
			justify-content: space-around;

			.tab-item {
				flex: 1;
				height: 50rpx;
				line-height: 50rpx;
				text-align: center;
				font-size: 30rpx;
				color: #353535;
				border-radius: 28rpx;
			}

			.active {
				color: #FFFFFF !important;
				background-image: linear-gradient(90deg, #FFB24D 0%, #FD913B 100%);
			}

			.t1 {
				flex: 1;
				height: 50rpx;
				line-height: 50rpx;
				text-align: center;
				font-size: 30rpx;
				color: #FFFFFF;
				background-image: linear-gradient(90deg, #FFB24D 0%, #FD913B 100%);
				border-radius: 28rpx;
			}

			.t2 {
				flex: 1;
			}

			// background-color: #FFB24D;
		}
	}
</style>
