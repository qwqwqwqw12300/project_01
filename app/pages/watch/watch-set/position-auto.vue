<template>
	<app-body :bg="false" :bodyStyle="{backgroundColor:'#FFF'}">
		<app-logo text="自动定位"></app-logo>
		<view class="ui-cell">
			<view class="cell-box">
				<u-cell-group>
					<u-cell title="自动定位"   titleStyle="font-size: 15px;color: #303133;" >
						<view slot="value" class="u-slot-value">
							<u-switch space="2" v-model="switchValue" activeValue="1" inactiveValue="0" @change="handleSwitch" :loading="loading" 
								size="20" activeColor="#FEAE43" inactiveColor="rgb(230, 230, 230)"></u-switch>
						</view>
					</u-cell>
					<u-cell title="定位频率"  arrow-direction="right" @click="show = true"
						isLink titleStyle="font-size: 15px;color: #303133;" >
						<view slot="value" class="u-slot-value">
							{{frequency}}分钟
						</view>
					</u-cell>
				</u-cell-group>
			</view>
		</view>
		<u-action-sheet
			:show="show"
			:actions="actions"
			title="请选择定位频率"
			@close="show = false"
			@select="handleSelect"
		>
		</u-action-sheet>
	</app-body>

</template>

<script>
	import {
		mapState,
	} from 'vuex';
	import {
		PostUpdateAutoLocation,
		GetAutoLocationInfo
	} from '@/common/http/api';
	export default {
		data() {
			return {
				switchValue:'1',
				actions: [{
						name: '30',
					},
					{
						name: '40',
					},
					{
						name: '50',
					},
					{
						name: '60',
					}
				],
				show:false,
				frequency:'30',
				gpsAutoCheck:false,
				loading:false
			}
		},
		computed: {
			...mapState({
				deviceInfo: state => state.deviceInfo
			}, ),
		},
		mounted() {},
		methods: {
			initData(){
				GetAutoLocationInfo({
					deviceId:240
				}).then(res=>{
					this.gpsAutoCheck = res.data.gpsAutoCheck
					if(this.gpsAutoCheck){
						this.switchValue = '1'
					}else{
						this.switchValue = '0'
					}
					this.frequency = res.data.gpsIntervalTime
				})
			},
			handleSwitch(){
				console.log(this.switchValue,'switchValue')
				this.loading = true
				if(this.switchValue=='1'){
					this.gpsAutoCheck = true
					this.handlePost(this.gpsAutoCheck,this.frequency)
				}else{
					this.gpsAutoCheck = false
					this.handlePost(this.gpsAutoCheck,this.frequency)
				}
			},
			handleSelect(e) {
				this.frequency = e.name
				this.loading = true
				this.handlePost(this.gpsAutoCheck,this.frequency)
			},
			handlePost(gpsAutoCheck,gpsIntervalTime){
				PostUpdateAutoLocation({
					deviceId:240,
					gpsAutoCheck:gpsAutoCheck,
					gpsIntervalTime:gpsIntervalTime
				}).then(res=>{
					this.loading = false
					uni.$u.toast(res.msg)
					setTimeout(() => {
						this.initData()
					}, 1000)
				})
			}
		},
		onShow() {
			this.initData()
		}
	}
</script>

<style lang="scss" scoped>
	::v-deep {
		.u-cell .u-line {
			margin: 0px !important;
		}
	}

	.ui-cell {
		margin-top: 64rpx;
		padding: 0 32rpx;

		.cell-box {
			background-color: #fff;
			border-radius: 16rpx;
		}
	}


</style>
