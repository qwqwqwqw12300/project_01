<template>
	<view v-if="fixboxtop">
		<view :class="{'fixedboxTouch' : isend}" class="fixedbox"
			:style="{'height':windowHeight + 'px','width':windowWidth + 'px','top':fixboxtop +'px','border-top-left-radius':radius,'border-top-right-radius':radius}"
			@touchstart="getStart" @touchmove="getMove($event)" @touchend="getend">
			<view class="content" :style="{'height':windowHeight + 'px'}">
				<view class="tapBoxTouchLine" v-if="showLine">
					<view class="line" @tap.stop.prevent="handleBack"></view>
				</view>
				<slot />
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		props: {
			radius: {
				type: String,
				default: '50rpx'
			},
			minHeight: {
				type: Number,
				default: 0.2
			},
			maxHeight: {
				type: Number,
				default: 0.5
			},
			touchHeight: {
				type: Number,
				default: 0
			},
			showLine: {
				type: Boolean,
				default: true
			},
		},
		mounted() {
			this.$nextTick(() => {
				this.windowWidth = uni.getSystemInfoSync().windowWidth;
				this.windowHeight = uni.getSystemInfoSync().windowHeight;
				const defaultHeight = this.windowHeight * (1 - this.minHeight)
				this.firstMaxtop = this.windowHeight * (1 - this.maxHeight)
				this.firsttop = defaultHeight
				this.phonetop = (this.windowHeight * this.maxHeight + this.windowHeight * this.minHeight) /
					2
				this.fixboxtop = defaultHeight
				this.$emit('currentHeight', (this.windowHeight - this.fixboxtop))
				this.$emit('maxtHeight', (this.windowHeight * this.maxHeight))
			})
		},
		data() {
			return {
				windowHeight: 0, // 屏幕高度
				windowWidth: 0, // 屏幕宽度
				firsttop: 0, // 默认高度
				firstMaxtop: 0,
				fixboxtop: 0, // 实际高度
				phonetop: 200, // 默认滑动分界线 - 后面计算为最低与最高的一半
				isend: false, // 触摸结束
				isfirst: true, // 手指第一次触摸
				tapboxtop: 0, // 手指距离顶部距离

				startClientY: '', //开始滑动
				endClientY: '', //结束滑动

			}
		},
		methods: {
			getStart(e) {
				this.startClientY = e.touches[0].clientY
				// console.log(e, 'eeeeeerrrll---------')
			},
			getMove(e) {
				// console.log(e, 'move--------------')
				let screenY
				screenY = e.touches[0].clientY
				// 这里特殊处理 解决：在滑动框内如果存在滚动元素，则会出现滑动时滑动框和内部滚动同时滑的问题
				if (this.touchHeight !== 0) {
					if (screenY - this.fixboxtop > this.touchHeight) {
						return false;
					}
				}
				this.isend = false
				if (this.isfirst) {
					this.isfirst = false
					this.tapboxtop = screenY - this.fixboxtop
				}
				this.fixboxtop = screenY - this.tapboxtop
				if (this.fixboxtop > this.firsttop) { // 往下滑 不允许
					this.fixboxtop = this.firsttop
				} else { // 往上滑
					if (this.fixboxtop <= this.windowHeight * (1 - this.maxHeight)) {
						this.fixboxtop = this.windowHeight * (1 - this.maxHeight)
					}
				}
				// this.$emit('currentHeight', (this.windowHeight - this.fixboxtop))
			},
			getend(e) {
				this.isend = true
				this.isfirst = true
				const endClientY = e.changedTouches[0].clientY
				const deltaY = endClientY - this.startClientY
				if (Math.abs(deltaY) > 90) {
					//下滑
					if (deltaY > 0) {
						this.fixboxtop = this.firsttop
					} else {
						this.fixboxtop = this.firstMaxtop
					}
				} else {
					console.log(this.fixboxtop, this.phonetop, 'ssssssssssssssss-------')
					if (this.fixboxtop - this.phonetop > 0) {
						this.fixboxtop = this.firsttop
					} else {
						this.fixboxtop = this.firstMaxtop
					}
				}
				this.$emit('currentHeight', (this.windowHeight - this.fixboxtop))
			},
			handleBack() {
				this.isend = true
				this.isfirst = true
				if (this.fixboxtop == this.firsttop) {
					this.fixboxtop = this.firstMaxtop
				} else {
					this.fixboxtop = this.firsttop
				}
			}
		}
	}
</script>

<style lang="scss" scoped>
	.fixedbox {
		position: fixed;
		left: 0;
		background-color: #FFFFFF;
		box-sizing: border-box;
		padding: 0 12px;
		z-index: 999;
	}

	.fixedboxTouch {
		transition-property: top;
		transition-duration: .3s;
	}

	.tapBoxTouchLine {
		margin-top: 20rpx;
		padding: 6rpx 0;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.line {
		margin: 0px;
		vertical-align: middle;
		border-bottom: 12rpx solid rgb(214, 215, 217);
		width: 80rpx;
		transform: scaleY(0.5);
		border-top-color: rgb(214, 215, 217);
		border-right-color: rgb(214, 215, 217);
		border-left-color: rgb(214, 215, 217);
	}
</style>