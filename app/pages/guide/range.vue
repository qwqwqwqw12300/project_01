<template>
	<view class="page-body">
		<view class="uni-padding-wrap uni-common-mt">
			<movable-area>
				<movable-view v-for="(item, index) of list" :key="index" :x="item.x" :y="item.y" direction="all"
					@change="(e) => { onChange(e, index)}">{{item.old.x}}
				</movable-view>
			</movable-area>
		</view>
		<text>list[0].x</text>
		<text>y</text>
		<view style="padding: 20px;">
			<u-button type="primary" text="新增" @click="add"></u-button>
			<u-button type="primary" text="删除" customStyle="margin-top: 20px" @click="del"></u-button>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				list: [{
					x: 0,
					y: 0,
					scale: 2,
					old: {
						x: 0,
						y: 0,
						scale: 2
					},
					name: '床'
				}],
				count: 1
			}
		},
		methods: {
			onChange: function(e, index) {
				this.list[index].old.x = e.detail.x
				this.list[index].old.y = e.detail.y
			},
			add() {
				this.list.push({
					x: 0,
					y: 0,
					scale: 2,
					old: {
						x: 0,
						y: 0,
						scale: 2
					},
					name: '床' + this.count++
				})
			},
			del() {
				this.list.pop();
			}
		}
	}
</script>

<style lang="scss">
	movable-view {
		display: flex;
		align-items: center;
		justify-content: center;
		height: 150rpx;
		width: 150rpx;
		background-color: #007AFF;
		color: #fff;
	}

	movable-area {
		height: 300rpx;
		width: 100%;
		background-color: #D8D8D8;
		overflow: hidden;
	}

	.max {
		width: 500rpx;
		height: 500rpx;
	}
</style>
