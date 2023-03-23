<template>
	<view>
		<view class="ui-box">
			<view class="content">
				<u-icon name="/static/images/back.png" size="25" @tap="handleBack"></u-icon>
				<text>{{ dateFormat }}</text>
				<u-icon name="/static/images/forward.png" size="25" @tap="handleForward"></u-icon>
			</view>
			<view class="icon">
				<u-icon name="/static/images/room-edit.png" size="25" @tap="handleSelect"></u-icon>
			</view>
		</view>
		<!-- 		<u-calendar :show="show" :defaultDate="dateTime" monthNum="4" :mode="'single'" :maxDate="maxDate"
			:minDate="minDate" @confirm="confirm" @close="show = false">
		</u-calendar> -->
		<term-date-picker :value="dateFormat1" :showHoliday="false" :show="show" @confirm="confirm" @cancel="show=false"
			type="date" v-bind="$attrs" @monthChange="monthChange">
		</term-date-picker>
	</view>
</template>

<script>
	import TermDatePicker from './term-date-picker.vue'
	export default {
		components: {
			TermDatePicker
		},
		data() {
			return {
				show: false,
				maxDate: '',
				minDate: '',
				dateTime: '',
			}
		},
		props: {
			init: {
				type: Boolean,
				default: true
			}
		},
		computed: {
			dateFormat() {
				// const date = this.dateTime
				return uni.$u.timeFormat(this.dateTime, 'mm/dd')
			},
			dateFormat1() {
				return uni.$u.timeFormat(this.dateTime, 'yyyy/mm/dd')
			}
		},
		mounted() {
			this.dateTime = new Date()
			this.maxDate = uni.$u.timeFormat(new Date(), 'yyyy-mm-dd')
			this.minDate = uni.$u.timeFormat((new Date().setMonth(new Date().getMonth() - 3)), 'yyyy-mm-dd')
		},
		watch: {
			dateTime: {
				handler(val) {
					this.$emit('change', {
						value: uni.$u.timeFormat(val, 'yyyy-mm-dd')
					})
				}
			}
		},
		methods: {
			handleBack() {
				this.dateTime = new Date(this.dateTime.setDate(this.dateTime.getDate() - 1))
			},
			handleForward() {
				this.dateTime = new Date(this.dateTime.setDate(this.dateTime.getDate() + 1))
			},
			handleSelect() {
				this.show = true
			},
			confirm(e) {
				// console.log(e, 'eee')
				this.dateTime = e.date
				this.show = false
				// console.log(e, 'eee')
			},
			monthChange(val) {
				this.$emit('month', val)
			}
		}
	}
</script>

<style scoped lang="scss">
	.ui-box {
		margin-top: 60rpx;
		height: 50rpx;
		padding: 0 40rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		position: relative;

		.content {
			display: flex;
			align-items: center;

			text {
				margin: 0 40rpx;
				font-size: 32rpx;
				color: #353535;
			}
		}

		.icon {
			position: absolute;
			right: 50rpx;
		}
	}
</style>
