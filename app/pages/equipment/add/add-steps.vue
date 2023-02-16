<!--
* @Author: zhanch
* @Date: 2023-02-15 19:11:57
* @FilePath: /pages/equipment/add/add-steps.vue
* @Description: 雷达波绑定步骤
-->


<template>
	<app-body :bg="false">
		<view class="ui-add">
			<app-logo text="添加设备" color="#353535"></app-logo>
			<view class="ui-steps">
				<u-steps :current="current" activeColor="#FEAE43" direction="column">
					<u-steps-item v-for="(item, index) of list" :key="index" iconSize="27" :title="item.title"
						:desc="item.desc">
						<view class="ui-slot-icon" v-if="current === index" slot="icon">
							<u-loading-icon></u-loading-icon>
						</view>
						<u-icon name="checkmark-circle-fill" size="48rpx" slot="icon" v-if="current > index"
							color="#49DE91">
						</u-icon>
					</u-steps-item>
				</u-steps>
			</view>
			<view class="ui-confirm">
				<button class="default" @click="submit">保存</button>
			</view>
		</view>
	</app-body>

</template>

<script>
	export default {
		data() {
			return {
				list: [{
						title: '初始化',
						desc: '正在完成设备初始化，请稍候'
					},
					{
						title: '创建设备',
						desc: '正在等待上一个步骤完成'
					},
					{
						title: '设置Wi-Fi',
						desc: '正在等待上一个步骤完成'
					},
					{
						title: '注册设备',
						desc: '正在完成设备初始化，请稍候'
					},
					{
						title: '添加成功',
						desc: '正在完成设备初始化，请稍候'
					},
				],
				/**当前步骤**/
				current: 3
			};
		}
	}
</script>

<style lang="scss">
	.ui-add {
		min-height: calc(100vh - var(--status-bar-height) - 95rpx);
		width: 100%;
		background-color: #fff;
	}

	.ui-steps {
		margin: 60rpx 32rpx;
	}

	.ui-slot-icon {
		position: absolute;
		z-index: 2;
		display: flex;
		align-items: center;
		justify-content: center;
		color: #fff;
		height: 88rpx;
		width: 88rpx;
		border-radius: 50% 50%;
		// background: #FEAE43;
	}

	.ui-confirm {
		padding: 34rpx 32rpx 60rpx 32rpx;
	}
</style>
