<!--
* @Author: zhanch
* @Date: 2022-12-20 10:45:19
* @FilePath: /components/app-body/app-body.vue
* @Description: 页面主体
-->

<template>
	<view class="wd-body">
		<view class="wd-bg">
			<slot></slot>
		</view>
		<view class="wd-service" v-if="needService" @touchmove="onMove" @click="goService" @touchstart="onStart"
			:style="{top: serviceTop + 'px'}"></view>
		
	</view>

</template>

<script>
	import {
		mapState
	} from 'vuex';

	export default {
		props: {
			needService: {
				default: true
			}
		},
		data() {
			return {
				/**客服按钮**/
				serviceBtn: {
					top: '50%',
					oldTop: 0,
					/**屏幕高度**/
					windowHeight: 0,
					/**导航栏高度**/
					headerHeight: 0,
				}
			};
		},
		computed: {
			//使用对象展开运算符将此对象混入到外部对象中
			...mapState({
				serviceTop: state => state.serviceAxisY,
			})
		},
		mounted(options) {

		},
		methods: {
			/**
			 * 移动客服按钮
			 */
			onMove(e) {
				uni.$u.throttle(() => {
					const {
						windowTop,
						windowHeight,
						oldTop
					} = this.serviceBtn;
					const pageY = e.changedTouches[0].pageY - oldTop;
					if (pageY > windowTop && pageY < windowHeight - 100) this.$store.commit('setServiceAxisY',pageY);
				}, 10);

			},
			/**
			 * 选中客服按钮
			 */
			onStart(e) {
				const {
					currentTarget: {
						offsetTop
					},
					changedTouches
				} = e;
				const systemInfo = uni.getSystemInfoSync();
				Object.assign(this.serviceBtn, {
					windowHeight: systemInfo.windowHeight,
					windowTop: systemInfo.windowTop,
					oldTop: changedTouches[0].clientY - offsetTop
				});
			},
			
			/**
			 * 跳转客服对话
			 */
			goService() {
				uni.navigateTo({
					url: '/pages/customer-service/customer-service'
				})
			}
		}

	}
</script>

<style lang="scss">
	.wd-body {
		height: calc(100vh - var(--window-bottom));
		width: 100%;
		overflow: scroll;
	}
	.wd-service {
		z-index: 9999;
		position: fixed;
		right: 0;
		height: 150rpx;
		width: 150rpx;
		background: url('../../static/images/service.png') no-repeat;
		background-size: cover;
		display: inline-block;
	}
</style>
