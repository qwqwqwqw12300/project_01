<template>
	<app-body >
		<app-logo color="#353535" text="历史位置" ></app-logo>
		<view class="ui-map">
			<view class="ui-input">
				<u-input border="surround" style="border-radius: 36rpx;"
					v-model="content" prefixIcon="search" font-size="32rpx" placeholder="请输入您想搜索的内容" >
				</u-input>
			</view>
			<u-cell-group>
				<u-cell @tap="handleSelect" title="日期"  arrow-direction="right" isLink>
					<text slot="value" class="u-slot-value">
						{{ startDate }} 至 {{endDate}}
					</text>
				</u-cell>
				<u-cell @tap="handleTime" title="时间"  arrow-direction="right" isLink>
					<text slot="value" class="u-slot-value">
						{{ startTime }} 至 {{endTime}}
					</text>
				</u-cell>
			</u-cell-group>
			<smh-time-range :isUnder="timeShow" :time="defaultTime" @confrim="confrim" @cancel="cancel"></smh-time-range>
			<view class="map-box">
				<map id="map" ref="map" style=" width: 100%; height: 560rpx;border-radius: 20rpx;" :latitude="latitude"
					:longitude="longitude" />
			</view>
			<u-cell-group> 
				<u-cell :title="item.name"  v-for="(item,index) in list" :key="index">
					<text slot="value" class="u-slot-value">
						{{item.dateTime}}
					</text>
				</u-cell>
			</u-cell-group>
		</view>
		<time-picker :show="showPicker" type="range" :value="defaultValue" :show-tips="true" :begin-text="'开始'"
		    :end-text="'结束'" :show-seconds="true" @confirm="onSelected"  @cancel="showPicker=false">
		</time-picker>
		
	</app-body>
</template>

<script>
	import timePicker from '@/components/term-picker/term-picker.vue';
	export default {
		components:{
			timePicker
		},
		data() {
			return {
				latitude: 39.909,
				longitude: 116.39742,
				content:'',
				defaultValue:['2023-02-27 14:00', '2023-03-05 13:59'],
				startDate: "2023/02/27",
				endDate: "2023/03/05",
				startTime: "14:00",
				endTime: "15:00",
				timeShow:false,
				showPicker: false,
				defaultTime:[0, 0, 0, 23, 59],
				list:[{
					name:'新大陆科技园',
					dateTime:'2023/02/28 13:00'
				},
				{
					name:'新大陆壹号',
					dateTime:'2023/02/28 13:30'
				},
				{
					name:'中国移动',
					dateTime:'2023/02/28 14:00'
				},
				{
					name:'中国海关',
					dateTime:'2023/02/28 14:20'
				},
				{
					name:'名城国际',
					dateTime:'2023/02/28 15:38'
				},
				]
			}
		},
		methods:{
			handleSelect() {
				this.showPicker = true
			},
			handleTime(){
				this.timeShow = true
			},
			confrim(e){
				console.log(e)//确定事件 =>12:30-17:30
				this.startTime = e.aboveTime
				this.endTime = e.underTime
				this.timeShow = false
				
				// this.defaultTime = this.defaultTime.forEach(item=>{
				// 	item = parseInt(item)
				// })
				console.log(this.defaultTime)
			},
			cancel(e){
				this.timeShow = false
			},
			onSelected(e){
				console.log(e.value,'e')
				this.startDate = e.value[0]
				this.endDate = e.value[1]
				this.showPicker = false
			},
		}
	}
</script>

<style lang="scss" scoped>
	.ui-map {
		padding: 32rpx;
		.ui-input{
			margin-top: 32rpx;
			margin-bottom: 34rpx;
		}
		.map-box {
			margin-top:32rpx;
			margin-bottom:32rpx;
			width: 100%;
			height: 560rpx;
			border: 2rpx solid black;
			border-radius: 16rpx;
		}

		.map-position {
			margin-top: 30rpx;
			display: flex;
			justify-content: space-between;

			.label {
				width: 160rpx;
				font-size: 32rpx;
				color: #888888;
			}

			.content {
				white-space: wrap;
				font-size: 32rpx;
				color: #353535;
				font-weight: 600;
			}
		}

		.map-address {
			margin-top: 30rpx;
			padding: 0 32rpx;
			background-color: #fff;
			border-radius: 16rpx;

			.address-box {
				display: flex;
				flex-direction: column;


				.list-item {
					// padding: 48rpx 32rpx;
					height: 128rpx;
					display: flex;
					align-items: center;
					justify-content: space-between;
					border-bottom: solid 2px #f7f7f7;
				}
			}
		}
		
		.ui-btn{
			margin-top: 50rpx;
		}
	}
</style>
