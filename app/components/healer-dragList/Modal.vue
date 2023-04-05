<template>
	<view class="cu-modal" :class="modalShow?'show':''">
		<view class="cu-dialog">
			<view class="cu-bar bg-white justify-end">
				<view class="content">{{title}}</view>
				<view class="action" @tap="ModalStatus">
					<text class="cuIcon-close text-red"></text>
				</view>
			</view>
			<view class="">
				<slot></slot>
			</view>
			<view class="cu-bar bg-white">
				<view class="action1 justify-between">
					<button class="cu-btn line-green text-green" @tap="ModalStatus">{{cancelText}}</button>
					<button class="cu-btn bg-green margin-left" @tap="confirm">{{confirmText}}</button>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		name:"Modal",
		props:{
			title:{
				type:String,
				default:"提示"
			},
			cancelText:{
				type:String,
				default:"取消"
			},
			// cancelColor:{
			// 	type:String,
			// 	default:"取消"
			// },
			confirmText:{
				type:String,
				default:"确定"
			},
			// confirmColor:{
			// 	type:String,
			// 	default:"确定"
			// },
		},
		data() {
			return {
				modalShow:false,
			};
		},
		methods:{
			ModalStatus() {
				this.modalShow = !this.modalShow
			},
			confirm(){
				this.$emit("confirm")
				this.ModalStatus()
			}
		}
	}
</script>

<style>
.action1{
	width: 100%;
	padding:0 40rpx;
	display: flex;
}
</style>
