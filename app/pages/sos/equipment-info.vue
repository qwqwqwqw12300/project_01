<!-- 修改设备名称 -->
<template>
	<app-body :bg="false">
		<view class="app-main">
			<view style="background-color: #ffffff;">
				<u-cell-group>
				    <u-cell
				        title="IMEI"
				        :value="record.no"
				        center
				    ></u-cell>
				    <u-cell
				        title="设备类型"
				        value="4G网关"
				        center
				    ></u-cell>
				    <u-cell
				        title="设备型号"
				        value="G4N-T"
				        center
				    ></u-cell>
				    <u-cell
				        title="ICCID"
				        :value="record.iccId"
				        center
				    ></u-cell>
				</u-cell-group>
			</view>
		</view>
	</app-body>
</template>

<script>
	import {
		getSosGatewayInfo
	} from '@/common/http/api.js';

	import {
		mapState,
	} from 'vuex';
	export default {
		data() {
			return {
				record: []
			}
		},
		computed: {
			...mapState({
				deviceInfo: state => state.deviceInfo
			}, )
		},
		created() {
			this.getList()
		},
		methods: {
			async getList() {
				const res = await getSosGatewayInfo({ deviceId: this.deviceInfo.deviceId })
				this.record = res.data
			}
		}
	}
</script>

<style lang="scss" scoped>
	.app-main {
		width: 100%;
		padding-top: 28rpx;
		box-sizing: border-box;
	}
	
</style>