<!--
* @Author: zhanch
* @Date: 2023-02-28 23:34:54
* @FilePath: /pages/equipment/setting/alert-rules/alert-rules.vue
* @Description: 预警规则
-->

<template>
	<app-body :bg="false">
		<view class="ui-body">
			<app-logo text="新建无人预警规则"></app-logo>
				<view class="ui-edit">
					<u-cell-group>
						<u-cell title="规则名称">
							<u-input inputAlign="right" placeholder="请输入名称" border="none" slot="right-icon"
								></u-input>
						</u-cell>
						<u-cell title="规则名称" isLink value="2023/02/21 至 2023/02/23"></u-cell>
						<u-cell title="时间" isLink value="13:00 至 14:00"></u-cell>
					</u-cell-group>
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
		background: #fff;
	}
	.ui-edit {
		margin-top: 64rpx;
	}
</style>
