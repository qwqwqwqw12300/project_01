<template>
	<app-body :bodyStyle="{background: 'rgb(247,247,247)' }" :bg="false">
		<view class="ui-logo">
			<app-logo color="#353535" text="设置电子围栏"></app-logo>
		</view>
		<view class="ui-map">
			<view class="ui-map-box">
				<map id="map" ref="map" style=" width: 100%; height: 600rpx;" :latitude="latitude"
					:longitude="longitude" :markers="covers" :circles="circles">
				</map>
			</view>
		</view>
	</app-body>
</template>

<script>
	export default {
		data() {
			return {
				latitude: 39.909,
				longitude: 116.39742,
				covers: [],
				circles: [],
			}
		}
	}
</script>

<style lang="scss" scoped>
	.ui-logo {
		background-color: #fff;
		padding-bottom: 50rpx;
	}

	.ui-map {
		background-color: #fff;
	}
</style>
