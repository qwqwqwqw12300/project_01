<!--
* @Author: zhanghaowei
* @Date: 2023年2月28日16:16:00
* @FilePath: 
* @Description: 新建时段禁用
-->

<template>
	<app-body>
		<app-logo color="#353535" text="新建禁用时段" ></app-logo>
		<view>
			<u-cell-group>
				<u-cell title="名称">
					<u-input inputAlign="right" placeholder="请输入名称" border="none" slot="right-icon"
						v-model="name"></u-input>
				</u-cell>
				<u-cell title="时间">
					
					
				</u-cell>
				
			</u-cell-group>
		</view>
	</app-body>
</template>

<script>
	export default {
		data() {
			return {
				name:'',
				
				
			};
		},
		mounted() {
			
		},
		methods: {
			
		}
	};
</script>

<style lang="scss">
	.ui-section {
		width: 500rpx;
		
	}
	.ui-date-select {
		display: flex;
		justify-content: space-between;
		align-items: center;
	
		.ui-text {
			width: 200rpx;
			height: 60rpx;
			line-height: 60rpx;
			text-align: center;
			background: #F7F7F7;
			border: 1rpx solid rgba(136, 136, 136, 1);
			border-radius: 8rpx;
		}
	}
</style>
