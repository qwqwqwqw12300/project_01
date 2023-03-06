<template>
	<app-body :bodyStyle="{background: 'rgb(247,247,247)' }" :bg="false">
		<view class="ui-logo">
			<app-logo color="#353535" text="设置电子围栏"></app-logo>
		</view>
		<view class="ui-map">
			<view class="ui-map-box">
				<map id="map" ref="map" style=" width: 100%; height: 563rpx; border-radius: 20rpx;" :latitude="latitude"
					:longitude="longitude" :markers="covers" :circles="circles">
				</map>
			</view>
		</view>
		<view class="ui-set">
			<view class="address-set">
				<text class="label">
					地址
				</text>
				<view class="input">
					<u--input placeholder="请输入地址" border="none" clearable></u--input>
				</view>
			</view>
			<view class="radius-set">
				<view class="label">
					<text>
						设置半径长度
					</text>
				</view>
				<view class="slider">
					<u-slider @change="onSlider" max="500" activeColor="#eeaa3d" blockColor="#eeaa3d"
						inactiveColor="#c0c4cc" />
				</view>
			</view>
		</view>
		<view class="address-list">
			<view class="title">
				<text></text>
				地址列表
			</view>
			<view class="list-item">
				<text class="label">
					新大陆科技园
				</text>
				<u-icon name="/static/images/position.png" size="44rpx" style="margin-right: 6rpx;" />
			</view>
			<view class="list-item">
				<text class="label">
					新大陆科技园
				</text>
				<u-icon name="/static/images/position.png" size="44rpx" style="margin-right: 6rpx;" />
			</view>
			<view class="list-item">
				<text class="label">
					新大陆科技园
				</text>
				<u-icon name="/static/images/position.png" size="44rpx" style="margin-right: 6rpx;" />
			</view>
		</view>
		<view class="ui-btn">
			<button class="default" @click="handleSubmit">提交</button>
		</view>
	</app-body>
</template>

<script>
	export default {
		data() {
			return {
				latitude: 26.08,
				longitude: 119.18,
				covers: [],
				circles: [],
				radius:''
			}
		},
		methods: {
			handleSubmit(){
				
			},
			onSlider(value){
				console.log(value)
				this.radius = value
			}
		}
	}
</script>

<style lang="scss" scoped>
	::v-deep {
		uni-slider {
			margin: 10rpx 18rpx !important;
		}
	}

	.ui-logo {
		background-color: #fff;
		padding-bottom: 30rpx;
	}

	.ui-map {
		background-color: #fff;
		padding: 32rpx;

		.ui-map-box {
			width: 100%;
			height: 563rpx;
			overflow: hidden;
			border-radius: 30rpx;
		}
	}

	.ui-set {
		margin-top: 20rpx;
		background-color: #fff;
		padding: 0 32rpx;

		.address-set {
			height: 128rpx;
			display: flex;
			justify-content: space-between;
			align-items: center;
			border-bottom: solid 2px #f7f7f7;

			.label {
				font-size: 34rpx;
				color: #353535;
			}

			.input {
				width: 240rpx;
			}
		}

		.radius-set {
			.label {
				height: 128rpx;
				line-height: 128rpx;

				text {
					font-size: 34rpx;
					color: #353535;
				}
			}

			.slider {
				margin-top: 20rpx;
				box-sizing: border-box;
				height: 80rpx;
				// padding-top: 20rpx;
				// display: flex;
				// align-items: center;
			}
		}
	}

	.address-list {
		margin-top: 20rpx;
		background-color: #fff;
		padding: 0 32rpx;

		.title {
			height: 100rpx;
			display: flex;
			align-items: center;
			font-size: 36rpx;
			color: #FEAE43;
			font-weight: 600;
			border-bottom: solid 2px #f7f7f7;

			text {
				height: 50rpx;
				border-left: solid 14rpx #FEAE43;
				border-radius: 4rpx;
				margin-right: 20rpx;

			}
		}

		.list-item {
			// padding: 48rpx 32rpx;
			height: 128rpx;
			display: flex;
			align-items: center;
			justify-content: space-between;
			border-bottom: solid 2px #f7f7f7;
		}
	}

	.ui-btn {
		padding: 32rpx;
		margin-top: 50rpx;
	}
</style>
