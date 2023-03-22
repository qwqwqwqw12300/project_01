<template>
	<app-body :bg="false" >
		<app-logo text="血压">
			<u-icon class="ui-icon" :name="'../../../static/images/share@3x.png'" size="40rpx"></u-icon>
		</app-logo>
		<view class="box">
			<date-picker @onSelect="onSelect"></date-picker>
		</view>
		<component ref="comRef" :is="tabKey"></component>
	</app-body>
</template>

<script>
	import date from './components/blood-date.vue'
	import week from './components/blood-week.vue'
	export default {
		components: {
			week,
			date,
		},
		data() {
			return {
				tabKey:'date'
			}
		},
		created() {
			
		},
		methods: {
			onSelect(val) {
				console.log(val, '000')
				this.tabKey = val.type
			},
			
		}
	}
</script>

<style lang="scss" scoped>
	.box {
		margin-top: 68rpx;
	}
	.ui-icon{
		margin-left: 546rpx;
	}
</style>
