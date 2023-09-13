<template>
	<view>
		<view class="ui-tabs">
			<view class="tab-box">
				<text @tap="handleTab(item.key)" class="tab-item" :class="{'active': tabKey === item.key }"
					v-for="item in tabList" :key="item.key">
					{{ item.name }}
				</text>
			</view>
		</view>
		<component @change="change" v-on="$listeners" v-bind="$attrs" ref="comRef" :is="tabKey"></component>
	</view>
</template>

<script>
	import date from './components/date.vue'
	import week from './components/week.vue'
	export default {
		components: {
			week,
			date,
		},
		name: "date-picker",
		data() {
			return {
				tabList: [{
					key: 'date',
					name: '日'
				}, {
					key: 'week',
					name: '周'
				}],
				tabKey: 'date',
			}
		},
		methods: {
			handleTab(key) {
				this.tabKey = key
			},
			change(val) {
				console.log('changechange');
				uni.$u.debounce(() => {
					this.$emit('onSelect', {
						type: this.tabKey,
						value: val.value
					})
				}, 500)
			}
		}
	}
</script>

<style lang="scss" scoped>
	.ui-tabs {
		width: 600rpx;
		height: 64rpx;
		margin: 0 auto;

		.tab-box {
			background: #FFFFFF;
			border-radius: 50rpx;
			padding: 10rpx;
			display: flex;
			align-items: center;
			justify-content: space-around;

			.tab-item {
				flex: 1;
				height: 64rpx;
				line-height: 64rpx;
				border-radius: 32rpx;
				text-align: center;
				font-size: 30rpx;
				color: #353535;
				// border-radius: 28rpx;
			}

			.active {
				color: #FFFFFF !important;
				background-image: linear-gradient(90deg, #FFB24D 0%, #FD913B 100%);
			}
		}
	}
</style>