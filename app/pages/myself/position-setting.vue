<!--
* @Author: zhanch
* @Date: 2023-01-05 17:15:25
* @FilePath: /pages/myself/position-setting.vue
* @Description: 区域设置
-->

<template>
	<view id="setting">
		<view class="ui-movable">
			<movable-area>
				<movable-view v-for="(item, index) of list" :key="index" :x="item.x" :y="item.y" direction="all"
					@change="(e) => { onChange(e, index)}">{{item.old.x}}
				</movable-view>
			</movable-area>
		</view>
		<view class="ui-list">
			<view class="ui-item">
				<view class="ui-item-title">1</view>
				<view class="ui-item-msg">
					<text class="ui-name">名称</text>
					<text class="ui-size">长宽</text>
				</view>
				<u-icon name="trash" color="#999999" size="28"></u-icon>
			</view>
		</view>
		<view class="ui-btn">
			<button>新增区域</button>
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
			};
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
	.ui-movable {
		// height: 300rpx;
		width: 100%;
	}

	movable-area {
		height: 400rpx;
		width: 100%;
		background-color: #D8D8D8;
		overflow: hidden;
	}

	movable-view {
		display: flex;
		align-items: center;
		justify-content: center;
		height: 150rpx;
		width: 150rpx;
		background-color: #007AFF;
		color: #fff;
	}

	.ui-item {
		display: flex;
		height: 115rpx;
		width: 640rpx;
		margin: 0 calc((100% - 641rpx) / 2);
		border-radius: 10px;
		filter: drop-shadow(7.824px 10.382px 8px rgba(7, 5, 5, 0.08));
		background-image: linear-gradient(96deg, #f5f5f5 0%, #e5e5e5 100%);
	}
</style>
