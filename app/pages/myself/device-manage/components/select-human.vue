<!--
* @Author: 选择人员
* @Date: 2023-02-25 13:57:47
* @FilePath: /components/select-human/select-human.vue
* @Description: 绑定人员
-->

<template>
	<view>
		<u-popup :closeOnClickOverlay="false" :closeable="true" :round="10" :show="bindHumanShow" mode="center"
			@close="close">
			<view class="wd-add">
				<u-text prefixIcon="plus-circle" :iconStyle="{ fontSize: '38rpx', color: '#ea942f' }" color="#ea942f"
					size="30rpx" text="绑定设备"></u-text>
				<view class="wd-add-box">
					<u-text size="28rpx" prefixIcon="home" iconStyle="font-size: 36rpx" text="选择家庭"></u-text>
					<view class="wd-select" @click="sheetFamilyShow">
						<u-input placeholder="请选择家庭" border="surround" v-model="familyName" readonly
							:suffixIcon="sheetFamily ? 'arrow-up' : 'arrow-down'"></u-input>
					</view>
					<view class="wd-add-family active" @click="goAddFamily"><text>添加家庭</text></view>
				</view>
				<view class="wd-add-box wd-add-box-human">
					<u-text size="28rpx" prefixIcon="home-fill" iconStyle="font-size: 36rpx" text="选择人员"></u-text>
					<view class="wd-select" @click="sheetHumanShow">
						<!-- <uni-data-select v-model="bindForm.humanId" :clear="false" :localdata="rangHumanList">
						</uni-data-select> -->
						<u-input placeholder="请选择人员" border="surround" v-model="name" readonly
							:suffixIcon="sheetHuman ? 'arrow-up' : 'arrow-down'"></u-input>
					</view>
				</view>
				<view class="wd-btn-group"><button class="default" @click="bindSubmit">确定</button></view>
			</view>
		</u-popup>
		<u-action-sheet :actions="famliyList" :closeOnClickOverlay="true" :safeAreaInsetBottom="true"
			@select="selectFamily" :closeOnClickAction="true" @close="sheetFamily = false" :show="sheetFamily"
			cancelText="取消"></u-action-sheet>
		<u-action-sheet :actions="rangHumanList" :closeOnClickOverlay="true" :safeAreaInsetBottom="true"
			@select="selectHuman" :closeOnClickAction="true" @close="sheetHuman = false" :show="sheetHuman"
			cancelText="取消"></u-action-sheet>
	</view>
</template>

<script>
	import {
		PostSelectTHumanListByFamilyId
	} from '@/common/http/api.js';
	import {
		mapState,
		mapActions
	} from 'vuex';
	export default {
		name: 'select-human',
		data() {
			return {
				bindHumanShow: false,
				sheetFamily: false,
				sheetHuman: false,
				humanList: [],
				familyName: '',
				name: '',
				form: {
					familyId: '',
					humanId: '',
					deviceNo: ''

				}
			};
		},
		computed: {
			...mapState({
				/**家庭列表**/
				famliyList: state => {
					return state.familyList
						.filter(n => n.shareFlag === '2')
						.map(ele => ({
							name: ele.name,
							value: ele.familyId
						}));
				}
			}),
			/**人员列表**/
			rangHumanList() {
				return this.humanList
					.filter(ele => !ele.devices || !ele.devices.length)
					.map(ele => ({
						name: ele.name,
						value: ele.humanId
					}));
			}
		},
		methods: {
			open(deviceNo) {
				this.form = {
					familyId: '',
					humanId: '',
					deviceNo
				};
				this.familyName = this.name = '';
				this.bindHumanShow = true;
			},

			sheetFamilyShow() {
				this.sheetFamily = true;
			},

			sheetHumanShow() {
				this.sheetHuman = true;
			},
			/**
			 * 选择家庭
			 */
			selectFamily(item) {
				const {
					name,
					value
				} = item;
				this.familyName = name;
				value &&
					PostSelectTHumanListByFamilyId({
						familyId: value
					}).then(res => {
						this.humanList = res.data || [];
						Object.assign(this.form, {
							familyId: value,
							humanId: ''
						});
					});
			},

			/**
			 * 选择人员
			 */
			selectHuman(item) {
				console.log(item);
				const {
					name,
					value
				} = item;
				this.name = name;
				this.form.humanId = value;
			},

			bindSubmit() {
				if (this.form.familyId && this.form.humanId) {
					this.$emit('comfirm', this.form);
					this.bindHumanShow = false;
				} else {
					uni.$u.toast('请选择要绑定的人员');
				}
			},

			/**
			 * 添加家庭
			 * @param {Object} id
			 */
			goAddFamily() {
				uni.navigateTo({
					url: '/pages/myself/famliy-manage'
				})
			},

			close() {
				this.sheetFamily = false;
				this.sheetHuman = false;
				this.bindHumanShow = false;
			}
		}
	};
</script>

<style lang="scss">
	.wd-add {
		width: 582rpx;
		min-height: 606rpx;
		border-radius: 20rpx;
		filter: drop-shadow(0 0 5rpx rgba(7, 5, 5, 0.34));
		background-image: linear-gradient(-36deg, #e4e4e4 0%, #f8f8f8 100%);
		padding: 53rpx 31rpx;

		&.wd-change {
			height: 400rpx;

			.wd-add-box {
				border-bottom: 1px solid #e4e4e4;
			}
		}

		.wd-add-family {
			width: 100%;
			text-align: right;
			margin-top: 20rpx;
			color: rgb(41, 121, 255);
			font-size: 24rpx;
			text-decoration: underline;
		}

		&>view {
			margin-top: 52rpx;

			&.wd-add-box {
				padding: 10rpx 20rpx;

				&>* {
					margin-top: 30rpx;
				}
			}
		}

		.wd-add-box-human {
			margin-top: 10rpx;
		}

		.wd-btn-group {
			text-align: center;
			margin-top: 70rpx;

			button {
				width: 237rpx;
				height: 71rpx;
				font-size: 28rpx;
				color: #ffffff;
			}
		}
	}
</style>
