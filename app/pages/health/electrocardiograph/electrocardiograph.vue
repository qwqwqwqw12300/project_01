<template>
	<app-body :bg="false" >
		<app-logo text="心电" :isShare="true"></app-logo>
		<view class="box">
			<date-picker @onSelect="onSelect"></date-picker>
		</view>
		<component ref="comRef" :is="tabKey" :time="time"></component>
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
				tabKey:'date',
				time:''
			}
		},
		created() {
			
		},
		methods: {
			onSelect(val) {
				console.log(val, '000')
				this.tabKey = val.type
				// this.time = val.value
				this.time = '2023-1-10'
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
