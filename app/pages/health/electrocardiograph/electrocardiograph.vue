<template>
	<app-body :bg="false" >
		<app-logo text="心电" :isShare="true"></app-logo>
		<view class="box">
			<date-picker @onSelect="onSelect"></date-picker>
		</view>
		<component ref="comRef" :is="tabKey"></component>
	</app-body>
</template>

<script>
	import date from './components/electrocardiograph-date.vue'
	import week from './components/electrocardiograph-week.vue'
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
