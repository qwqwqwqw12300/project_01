<!--
* @Author: zhanch
* @Date: 2023-03-04 15:03:16
* @LastEditors: zhanch
* @LastEditTime: 2023-03-05 20:03:01
* @FilePath: /pages/equipment/setting/select-week.vue
* @Description: 选择星期
-->


<template>
	<app-body :bg="false">
		<view class="ui-body">
			<app-logo text="选择开始星期"></app-logo>
				<view class="ui-edit">
					<u-cell-group>
						<u-cell v-for="(item, index) of list" :key="index" :title="item.text" @click="select(index)">
							<u-icon name="checkmark" v-if="item.check" slot="right-icon" size="32rpx"></u-icon>
						</u-cell>
					</u-cell-group>
					<view class="ui-btn">
						<button class="default" @click="confirm">确定</button>
					</view>
				</view>
		</view>
	</app-body>
</template>

<script>
	export default {
		data() {
			return {
				list: [
					{
						text: '星期一',
						value: '0',
						check: false
					},
					{
						text: '星期二',
						value: '1',
						check: false
					},
					{
						text: '星期三',
						value: '2',
						check: false
					},
					{
						text: '星期四',
						value: '3',
						check: false
					},
					{
						text: '星期五',
						value: '4',
						check: false
					},
					{
						text: '星期六',
						value: '5',
						check: false
					},
					{
						text: '星期日',
						value: '6',
						check: false
					},
				],
			};
		},
		onLoad() {
			const weeks = this.$getCache('weeks') || [];
			this.list.forEach(ele => {
				if(weeks.includes(ele.value)) {
					ele.check = true;
				}
			});
		},
		methods: {
			/**
			 * 选择日期
			 */
			select(index) {
				this.list[index].check = !this.list[index].check;
			},
			
			/**
			 * 确认选择
			 */
			confirm() {
				const weeks = this.list.filter(ele => ele.check);
				uni.$emit('selectWeek', weeks);
				uni.navigateBack();
				
			}
		},
		
		onBackPress($event) {
			uni.$off('selectWeek');
		}
	}
</script>

<style lang="scss">
	.ui-body {
		box-sizing: border-box;
		width:100%;
		background: #fff;
		height: calc(100vh - 88rpx - var(--status-bar-height));
	} 
	
	.ui-edit {
		box-sizing: border-box;
		padding-top: 64rpx;
	}
	
	.ui-btn {
		margin-top: 104rpx;
		width: 100%;
		box-sizing: border-box;
		padding:  0 32rpx;
	}
</style>
