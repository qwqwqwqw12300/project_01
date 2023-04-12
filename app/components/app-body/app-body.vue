<!--
* @Author: zhanch
* @Date: 2022-12-20 10:45:19
* @FilePath: /components/app-body/app-body.vue
* @Description: 页面主体
-->

<template>
	<view>
		<u-modal :show="modal.show" :title="modal.title" :content="modal.content" :showCancelButton="modal.showCancel"
			@confirm="modal.confirm" @cancel="modal.cancel" @close="modal.cancel">
		</u-modal>
		<scroll-view :scroll-y="true" :class="{'wd-body-bg': bg}" class="wd-body" :style="bodyStyle">
			<template v-if="!hideTitle">
				<!-- 需要返回头 -->
				<view class="wd-bg wd-fixed">
					<u-navbar :leftText="leftText" :safeAreaInsetTop="true" :autoBack="!back"
						:bgColor="titleColor || (bg ? 'transparent': '#fff')" :fixed="true" :leftIconColor="'#000'"
						@leftClick="leftClick" />
					<slot></slot>
				</view>
				<!-- /需要返回头 -->
			</template>
			<template v-else>
				<!-- 不需要返回头 -->
				<view class="wd-bg">
					<slot></slot>
				</view>
				<!-- /不需要返回头 -->
			</template>
			<!-- 智能客服 -->
			<view class="wd-service" v-if="needService" @touchmove.prevent="onMove" @click="goService"
				@touchstart.prevent="onStart" :style="{top: serviceTop + 'px'}">
			</view>
			<!-- 智能客服 -->
		</scroll-view>
	</view>
</template>

<script>
	import {
		mapState
	} from 'vuex';
	import {
		isApp
	} from '../../common/utils/util';

	export default {
		props: {
			/**是否展示客服**/
			needService: {
				default: isApp()
			},
			/**是否隐藏返回头**/
			hideTitle: {
				default: false
			},
			/**返回头文字**/
			leftText: {
				default: '返回'
			},
			/**背景图高度**/
			bodyStyle: {
				default: () => {
					{}
				}
			},
			/**自定义返回**/
			back: {
				default: undefined
			},
			/**是否需要背景**/
			bg: {
				default: true
			},
			/**
			 * 返回按钮颜色
			 */
			leftIconColor: {
				default: '#fff'
			},
			/**
			 * 返回头颜色
			 */
			titleColor: {
				default: ''
			},
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
				},
				scrollBox: false
			};
		},
		computed: {
			...mapState({
				serviceTop: state => state.serviceAxisY,
				// 模态框处理
				modal: function(state) {
					if (state.modal) {
						const handle = (config) => {
							state.modal.success && state.modal.success(config)
							this.$store.commit('setModal', {
								...state.modal,
								show: false
							});
						};
						const init = {
							showCancel: true,
							cancelText: '取消',
							confirm: () => handle({
								confirm: true
							}),
							cancel: () => handle({
								cancel: false
							})
						};
						return {
							show: true,
							...init,
							...state.modal,

						}
					}
					return {
						show: false,
						confirm: n => n,
						cancel: n => n
					}
				}
			})
		},
		mounted(options) {

		},
		beforeDestroy() {
			// 页面离开时需要销毁弹窗
			this.$store.commit('setModal', {
				show: false
			});
		},
		methods: {
			/**
			 * 返回上一页
			 * @param {Object}
			 */
			leftClick() {
				this.back && this.back();
			},
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
					url: '/pages/service/customer-service'
				})
			}
		}
	}
</script>

<style lang="scss">
	::v-deep {
		.u-navbar__content__left__text {
			font-size: 34rpx;
		}
	}

	.wd-body {
		border-top: var(--status-bar-height) solid #fff;
		box-sizing: border-box;
		height: calc(100vh - var(--window-bottom));
		width: 100%;
		overflow: scroll;
		background-repeat: no-repeat;
		background-size: cover;
		background-color: #f2f2f2;
	}

	.wd-body-bg {
		border-top: 0;
		padding-top: var(--status-bar-height);
		// background-image: url('@/static/images/bg2.png');
		background-image: linear-gradient(179deg, #FFE7B5 0%, #F7F7F7 39%);
	}

	.wd-fixed {
		box-sizing: border-box;
		padding-top: 60rpx;
	}

	.wd-bg {
		box-sizing: border-box;
		min-height: calc(100vh - var(--window-bottom) - var(--status-bar-height));
		position: relative;
		// height: 100%;
		width: 100%;
	}

	.wd-service {
		z-index: 9999;
		position: fixed;
		right: 10rpx;
		height: 120rpx;
		width: 120rpx;
		background: url('../../static/images/service.png') no-repeat;
		background-size: cover;
		display: inline-block;
	}
</style>
