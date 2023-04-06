<template>
	<app-body :bg="false" :needService="false">
		<app-logo text="心电" :isShare="true"></app-logo>
		<view class="box">
			<dates @change="change"></dates>
		</view>
		<date :time="time"></date>
	</app-body>
</template>

<script>
	import date from './components/electrocardiograph-date.vue'
	import dates from '@/components/date-picker/components/date.vue'
	export default {
		components: {
			date,
			dates
		},
		data() {
			return {
				time:''
			}
		},
		created() {
			
		},
		methods: {
			change(val){
				console.log(val,'val')
				this.time = val.value
				// this.time = '2023-1-10'
			}
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
