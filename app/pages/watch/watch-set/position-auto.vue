<template>
	<app-body :bg="false" :bodyStyle="{backgroundColor:'#FFF'}">
		<app-logo text="自动定位"></app-logo>
		<view class="ui-cell">
			<view class="cell-box">
				<u-cell-group>
					<u-cell title="自动定位"   titleStyle="font-size: 15px;color: #303133;" >
						<view slot="value" class="u-slot-value">
							<u-switch space="2" v-model="switchValue" activeValue="1" inactiveValue="0" @click="handleSwitch"
								size="20" activeColor="#FEAE43" inactiveColor="rgb(230, 230, 230)"></u-switch>
						</view>
					</u-cell>
					<u-cell title="定位频率"  arrow-direction="right" @click="show = true"
						isLink titleStyle="font-size: 15px;color: #303133;" >
						<view slot="value" class="u-slot-value">
							{{frequency}}
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
		PostCareCardUnBind
	} from '@/common/http/api';
	export default {
		data() {
			return {
				switchValue:'1',
				actions: [{
						name: '30分钟',
					},
					{
						name: '40分钟',
					},
					{
						name: '50分钟',
					},
					{
						name: '60分钟',
					}
				],
				show:false,
				frequency:'30分钟'
			}
		},
		computed: {
			...mapState({
				deviceInfo: state => state.deviceInfo
			}, ),
		},
		mounted() {},
		methods: {
			handleSwitch(){
				
			},
			handleSelect(e) {
				this.frequency = e.name
			},
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
