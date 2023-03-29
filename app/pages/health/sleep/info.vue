<template>
	<app-body  >
		<app-logo :text="text" :isShare="false"></app-logo>
		<view v-for="(item,index) in list" :key="index" class="ui-window">
			<view class="ui-window-title">{{item.title}}</view>
			<view>{{item.content}}</view>
		</view>
	</app-body>
</template>

<script>
	import {
		clearHeaded,
		lightSleep,
		deepSleep,
		nocturnalSleep
	} from '@/config/db';
	export default {
		data() {
			return {
				text:'',
				list:[]
			}
		},
		created() {
			
		},
		methods: {
			
			
		},
		onLoad(option) {
			this.text = option.name
			this.text == '浅睡' ? this.list = lightSleep : this.text =='深睡' ? this.list = deepSleep : this.text=='清醒' ? this.list = clearHeaded : this.list = nocturnalSleep
		}
	}
</script>

<style lang="scss" scoped>
	.ui-window{
		width: 90%;
		margin: 0 auto;
		margin-top: 30rpx !important;
		.ui-window-title{
			margin-bottom: 20rpx;
			font-weight: bold;
			font-size: 32rpx;
		}
	}
</style>
