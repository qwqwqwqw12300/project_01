<template>
	<view>
		<view class="cu-form-group">
			<view class="title">{{title}}</view>
			<input :placeholder="placeholder" v-model="value" name="input"></input>
		</view>
	</view>
</template>

<script>
	export default {
		name:"UnifyInput",
		props:{
			title:{
				type:String,
				default:""
			},
			placeholder:{
				type:String,
				default:""
			},
		},
		data() {
			return {
				value:''
			};
		},
		methods:{
			resetVal(){
				this.value = ''
			}
		}
	}
</script>

<style>

</style>
