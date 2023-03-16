<template>
	<view>
		<circle-map :record="sliderValue"></circle-map>
		<touch-popup :minHeight="0.1" :maxHeight="0.7" :touchHeight="64" radius="30rpx">
			<view class="title">
				电子围栏设置
			</view>
			<view class="address">
				<text class="label">
					地址
				</text>
				<view class="input">
					<text class="input-text" @click="goSearch">{{siteInfo || '请输入地址'}}</text>
				</view>
			</view>
			<view class="radius">
				<text class="label">
					半径
				</text>
				<view class="slider">
					<u-slider @change="onSlider" max="500" v-model="sliderValue" activeColor="#eeaa3d"
						blockColor="#eeaa3d" inactiveColor="#c0c4cc" />
				</view>
				<text class="value" style="text-align: center;">
					{{sliderValue}}米
				</text>
			</view>
			<view class="address-list">
				<view class="address-list-title">
					<text class="title-icon"></text>
					地址列表
				</view>
				<view class="list-item" v-for="(n, index) in poiList" :key="index" @click="mapMarker(n)">
					<text class="list-item-label">
						{{ n.name }}
					</text>
					<u-icon :name="getMapIcon(n)" size="44rpx" style="margin-right: 6rpx;" />
				</view>
			</view>
		</touch-popup>
	</view>
</template>

<script>
	import {
		mapState,
	} from 'vuex';
	export default {
		data() {
			return {
				siteInfo: '',
				sliderValue: 0,
				currentSelect: '',
				poiList: [], //搜索地址
			}
		},
		computed: {
			...mapState({
				deviceInfo: state => state.deviceInfo
			}, ),
			getMapIcon() {
				return function(item) {
					return this.currentSelect === item.index ? '/static/images/map-select.png' :
						'/static/images/position.png'
				}
			},
		},
		methods: {
			mapMarker(data) {

			},
			goSearch() {

			},
			onSlider() {

			}
		}
	}
</script>

<style lang="scss" scoped>
	.title {
		height: 120rpx;
		display: flex;
		align-items: center;
		border-bottom: solid 2px #f7f7f7;
		font-size: 34rpx;
		color: #353535;
		font-weight: 550;
	}

	.address {
		height: 128rpx;
		display: flex;
		justify-content: space-between;
		align-items: center;
		border-bottom: solid 2px #f7f7f7;

		.label {
			font-size: 34rpx;
			width: 150rpx;
			color: #353535;
		}

		.input {
			flex: 1;

			// width: 220rpx;
			.input-text {
				font-size: 30rpx;
				color: #D4D4D4;
			}
		}
	}

	.radius {
		height: 128rpx;
		display: flex;
		justify-content: space-between;
		align-items: center;
		border-bottom: solid 2px #f7f7f7;

		.label,
		.value {
			font-size: 34rpx;
			width: 120rpx;
			color: #353535;
		}

		.slider {
			flex: 1;
		}
	}

	.address-list {
		margin-top: 20rpx;
		background-color: #fff;

		.address-list-title {
			height: 100rpx;
			display: flex;
			flex-direction: row;
			align-items: center;
			font-size: 36rpx;
			color: #FEAE43;
			font-weight: 600;
			border-bottom: solid 2px #f7f7f7;

			.title-icon {
				height: 50rpx;
				border-left: solid 10rpx #FEAE43;
				margin-right: 20rpx;
			}
		}

		.list-item {
			// padding: 48rpx 32rpx;
			height: 128rpx;
			display: flex;
			flex-direction: row;
			align-items: center;
			justify-content: space-between;
			border-bottom: solid 2px #f7f7f7;

			.list-item-label {
				font-size: 34rpx;
				color: #353535;
			}
		}
	}
</style>
