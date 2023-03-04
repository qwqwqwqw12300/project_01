<template>
	<app-body :bg="false">
		<view class="ui-body">
			<app-logo text="离床时间设置"></app-logo>
				<view class="ui-edit">
					<u-cell-group>
						<u-cell title="选择时间格式">
							<view  slot="right-icon">
								<u-radio-group
								    placement="row">
									<u-radio activeColor="#FEAE43"  :customStyle="{marginRight: '32rpx'}" label="按日期"></u-radio>
									<u-radio activeColor="#FEAE43" label="按星期"></u-radio>
								</u-radio-group>
							</view>
							
						</u-cell>
						<u-cell title="日期" isLink value="2023/02/21 至 2023/02/23"></u-cell>
						<u-cell title="星期" isLink value="星期一，星期二，星期三"></u-cell>
						<u-cell title="时间" isLink value="13:00 至 14:00"></u-cell>
					</u-cell-group>
				</view>
				<view class="ui-btn-group">
					<view>删除规则</view>
					<view>保存</view>
				</view>	
		</view>
	</app-body>
</template>

<script>
	export default {
		data() {
			return {
				
			};
		}
	}
</script>

<style lang="scss">
	.ui-body {
		display: flex;
		width:100%;
		flex-direction: column;
		position: relative;
		background: #fff;
		height: calc(100vh - 88rpx - var(--status-bar-height));
	} 
	
	.ui-edit {
		display: flex;
		height: 100%;
		margin-top: 64rpx;
	}
	
	.ui-btn-group {
		height: 100rpx;
		width: 100%;
		position: fixed;
		bottom: 0;
		display: flex;
		align-items: center;
		flex-direction: row;
		&>view {
			width: 50%;
			height: 100%;
			text-align: center;
			line-height: 100rpx;
			font-size: 36rpx;
			@extend .active;
			&:nth-child(1) {
				background: #FFFFFF;
				border-top: 1rpx solid #e2e2e2;
				color: #E95656;
			}
			&:nth-child(2) {
				background-image: linear-gradient(90deg, #FFB24D 0%, #FD913B 100%);
				color: #FFFFFF;
			}
		}
		
	}
</style>
