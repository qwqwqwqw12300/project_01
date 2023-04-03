<!-- 个人信息 -->
<template>
	<app-body :bg="false" :bodyStyle="{backgroundColor:'#FFF'}">
		<app-logo text="个人信息"></app-logo>
		<view class="ui-cell">
			<u-cell-group>
				<u-cell title="选择性别" arrow-direction="right" titleStyle="font-size: 15px;color: #303133;">
					<view slot="value" class="u-slot-value">
						<view>
							<u-radio-group v-model="sexValue" placement="row">
								<u-radio :customStyle="{margin: '20rpx'}" v-for="(item,index) in sexList" :key="index"
									activeColor="#fd963e" :name="item.name">
									<u-icon :name="item.icon" :color="item.color" size="42rpx"></u-icon>
									<text style="margin-left: 10rpx;">{{item.name}}</text>
								</u-radio>
							</u-radio-group>
						</view>
					</view>
				</u-cell>
			</u-cell-group>
			<u-cell-group>
				<u-cell :title="item.name" v-for="(item,index) in cellList" :key="index" arrow-direction="right"
					titleStyle="font-size: 15px;color: #303133;">
					<view slot="value" class="u-slot-value">
						<u-number-box v-model="item.value" :step='item.step' :min="item.min" :max="item.max"
							:showMinus="item.value > item.min">
							<view slot="minus" class="minus">
								<u-icon name="minus" size="12"></u-icon>
							</view>
							<input slot="input" style="width: 50px;text-align: center;" class="input"
								v-model="item.value"></input>
							<view slot="plus" class="plus">
								<u-icon name="plus" color="#FFFFFF" size="12"></u-icon>
							</view>
						</u-number-box>
					</view>
				</u-cell>
				<u-cell title="生日" arrow-direction="right" isLink tleStyle="font-size: 15px;color: #303133;" @click="showPicker = true">
					<view slot="value" class="u-slot-value" >
						{{defaultValue}}
					</view>
				</u-cell>
			</u-cell-group>
		</view>
		<view class="ui-confirm">
			<button class="ui-btn" @click="cancel">跳过</button>
			<button class="default" @click="submit">下一步</button>
		</view>
		<time-picker :show="showPicker" format="yyyy-mm-dd" type="date" :value="defaultValue" :show-tips="true" :begin-text="'开始'"
		    :end-text="'结束'" :show-seconds="false" @confirm="onSelected"  @cancel="showPicker=false">
		</time-picker>
	</app-body>
</template>

<script>
	import {
		mapState,
	} from 'vuex';
	import {
		PostCreateHumanIfo
	} from '@/common/http/api';
	import timePicker from '@/components/term-picker/term-picker.vue';
	export default {
		components:{
			timePicker
		},
		data() {
			return {
				cellList: [{
						name: '身高',
						min:25,
						max:270,
						step:25,
						value: 0
					},
					{
						name: '体重',
						min:25,
						max:200,
						step:25,
						value: 0
					}
				],
				sexValue: '男',
				sexList: [{
						name: '男',
						icon: 'man',
						color: '#1F91F2'
					},
					{
						name: '女',
						icon: 'woman',
						color: '#FC7265'
					},
				],
				showPicker:false,
				defaultValue:'',
			}
		},
		computed: {
			...mapState({
				deviceInfo: state => state.deviceInfo
			}, )
		},
		onShow() {
			const newData = new Date()
			this.defaultValue = uni.$u.timeFormat(newData, 'yyyy-mm-dd')
		},
		methods: {
			onSelected(e){
				console.log(e,'e')
				this.defaultValue = [...e.value]
				this.showPicker = false
			},
			cancel() {
				uni.navigateBack()
			},
			submit() {
				// PostCreateHumanIfo({
				// 	calorie:this.cellList[0].value,
				// 	stepNum:this.cellList[1].value,
				// 	dration:this.cellList[2].value
				// }).then(res=>{
				// 	console.log(res,'res')
				// })
			}
		}
	}
</script>

<style lang="scss" scoped>
	.ui-cell {
		margin-top: 64rpx;
	}

	.ui-confirm {
		width: 100%;
		display: flex;
		margin-top: 104rpx;

		button {
			width: 320rpx;
		}
	}

	.minus {
		width: 22px;
		height: 22px;
		border-width: 1px;
		border-color: #E6E6E6;
		border-style: solid;
		border-top-left-radius: 100px;
		border-top-right-radius: 100px;
		border-bottom-left-radius: 100px;
		border-bottom-right-radius: 100px;
		@include flex;
		justify-content: center;
		align-items: center;
	}

	.input {
		padding: 0 10px;
	}

	.plus {
		width: 22px;
		height: 22px;
		background-color: #fd963e;
		border-radius: 50%;
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
		justify-content: center;
		align-items: center;
	}

	.ui-btn {
		background-color: #FFF !important;
		color: #fd963e;
		border: 1px solid #fd963e;
	}
</style>