<!--
* @Author: zhanch
* @Date: 2022-12-20 10:45:19
* @FilePath: /components/app-body/app-body.vue
* @Description: 页面主体
-->

<template>
	<view class="wd-body">
		<!-- 	<view class="wd-top"></view> -->
		<view class="wd-bg" :style="bodyStyle">
			<u-navbar v-if="!hideTitle" leftText="首页" @rightClick="rightClick" :safeAreaInsetTop="false"
				:autoBack="true" bgColor="transparent" leftIconColor="#fff" :fixed="false" />
			<slot></slot>
		</view>
		<view class="wd-service" v-if="needService" @touchmove="onMove" @click="goService" @touchstart="onStart"
			:style="{top: serviceTop + 'px'}">
		</view>

	</view>

</template>

<script>
	import {
		mapState
	} from 'vuex';

	export default {
		props: {
			/**是否展示客服**/
			needService: {
				default: true
			},
			/**是否隐藏返回头**/
			hideTitle: {
				default: false
			},
			/**返回头文字**/
			leftText: {
				default: '首页'
			},
			/**背景图高度**/
			bodyStyle: {
				default: () => {
					{}
				}
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
					if (pageY > windowTop && pageY < windowHeight - 100) this.$store.commit('setServiceAxisY',
						pageY);
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
	.wd-top {
		position: absolute;
		width: 100%;
		height: var(--status-bar-height);
		background: #FEC845;
		top: 0;
		z-index: 99;
	}

	.wd-body {
		padding-top: var(--status-bar-height);
		box-sizing: border-box;
		height: calc(100vh - var(--window-bottom));
		width: 100%;
		overflow: scroll;
	}

	.wd-bg {
		box-sizing: border-box;
		min-height: calc(100vh - var(--window-bottom) - var(--status-bar-height));
		position: relative;
		height: 100%;
		width: 100%;
		background-image: url('@/static/images/bg2.png');
		background-repeat: no-repeat;
		background-size: cover;
		background-color: #fff;
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
