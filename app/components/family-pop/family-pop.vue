<!--
* @Author: zhanch
* @Date: 2022-12-29 16:04:13
* @FilePath: /components/add-family/add-family.vue
* @Description: 添加家庭
-->

<template>
	<u-popup :closeable="true" :round="10" :show="show" mode="center" @close="close">
		<view class="wd-add">
			<view class="wd-title">
				<text class="wd-title-font">{{mode === 'add' ? '新建家庭': '修改家庭'}}</text>
			</view>
			<view class="wd-input">
				<text class="wd-input-font">家庭名称</text>
				<u-input border="surround" v-model="form.familyName" placeholder="请输入家庭名称" maxlength="6" clearable>
				</u-input>
			</view>
			<view class="wd-input">
				<text class="wd-input-font">家庭地址(必填)</text>
				<view class="addressbox">
					<view class="" style="width: 90%;padding-left: 10rpx;" v-if="form.address">
						<view class="addname">
							{{form.communityName}}
						</view>
						<view class="address">
							{{form.address}}
						</view>
					</view>
					<image class="wd-input-image" src="@/static/images/position@3x.png" v-if="form.address"
						@click="goMap"></image>
					<view class="" @click="goMap" style="width: 90%;padding-left: 10rpx;display: flex;align-items: center;padding-left: 10px;" v-if="!form.address">
						<view class="address">
							点击选择地址
						</view>
						<image class="wd-input-image" src="@/static/images/position@3x.png" style="margin-left: 10px;" ></image>
					</view>

				</view>
				<!-- <u-input v-model="" placeholder="请输入家庭地址" border="surround" clearable> -->
				<!-- <template slot="suffix"> -->
				<!-- <u-icon name="map-fill" class="active" @click="goMap" size="38rpx"></u-icon> -->
				<!-- </template> -->
				<!-- </u-input> -->
			</view>
			<view class="wd-input">
				<text class="wd-input-font">具体楼栋/单元</text>
				<u-input border="surround" v-model="form.detailedAddress" placeholder="请输入具体楼栋/单元" clearable>
				</u-input>
			</view>
			<view class="wd-hr"></view>
			<view class="wd-btn">
				<view @click="next">{{ btnName }}</view>
			</view>
		</view>
	</u-popup>
</template>

<script>
	import {
		PostAddFamily,
		PostEditFamily
	} from '@/common/http/api.js';
	export default {
		props: {
			btnName: {
				type: String,
				default: '确认'
			},
			/**修改模式**/
			mode: {
				type: String,
				/**add 添加家庭 edit 修改家庭**/
				default: 'add'
			},
			id: {

			}
		},
		data() {
			return {
				show: false,
				form: {
					familyName: '', //家庭名称
					address: '', //家庭地址
					detailedAddress: '', //详细地址,
					familyId: '',
					province: '',
					city: '',
					district: '',
					communityName: '',
					latitude: '',
					longitude: '',
					address_name: ''
				},
			};
		},
		onShow() {
			if (this.mode == 'edit') {
				this.open()
			}
		},
		methods: {
			close() {
				this.form = {}
				this.show = false;
			},
			/**
			 * 打开家庭
			 */
			open(data = {}) {
				this.$store.dispatch('setLocation').then(res => {
					console.log(res, 'setLocation');
					if (res) {
						this.form.address = res.address || ''
					}
					Object.assign(this.form, data);
				})
				this.show = true;
			},
			next() {
				const {
					familyName,
					address
				} = this.form
				if (!familyName) {
					return uni.$u.toast('请填写家庭名称')
				}
				if (!address) {
					return uni.$u.toast('请填写家庭地址')
				}
				const handle = this.mode === 'add' ? PostAddFamily : PostEditFamily;
				handle({
					familyName: this.form.familyName,
					address: this.form.address,
					address_name: this.form.address_name,
					latitude: this.form.latitude,
					longitude: this.form.longitude,
					province: this.form.province,
					city: this.form.city,
					district: this.form.district,
					familyId: this.id,
					detailedAddress: this.form.detailedAddress,
					communityName: this.form.communityName
				}).then(res => {
					uni.$u.toast(res.msg);
					this.close();
					setTimeout(() => {
						this.$emit('update', {
							familyId: res.data && res.data.familyId,
							name: familyName
						});
					}, 1000);
				})
			},

			/**
			 * 跳转选择地址
			 */
			goMap() {
				uni.$on('searchData', res => {
					console.log('resresresres', res)
					const {
						province,
						city,
						district,
						address,
						name,
						longitude,
						latitude
					} = res
					this.form.address = province + city + district + address
					this.form.address_name = address
					this.form.province = province
					this.form.city = city
					this.form.district = district
					this.form.longitude = res.location.longitude
					this.form.latitude = res.location.latitude
					this.form.communityName = name
					this.form = {
						...this.form
					};

					console.log(this.form, 'this.formthis.formthis.form')
				});
				uni.navigateTo({
					url: '/pages/equipment/search'
				});
			}
		}
	};
</script>

<style lang="scss">
	.wd-add {
		width: 582rpx;
		min-height: 500rpx;
		border-radius: 20rpx;
		padding: 20rpx 31rpx 0 20rpx;

		.wd-title {
			display: flex;
			align-items: center;
			justify-content: center;

			.wd-title-font {
				font-family: PingFangSC-Medium;
				font-size: 36rpx;
				color: #333333;
				letter-spacing: 0;
				text-align: center;
				line-height: 46rpx;
				font-weight: 500;
			}
		}

		.wd-input-image {
			width: 48rpx;
			height: 48rpx;
		}

		.addressbox {
			display: flex;
			align-items: center;
			justify-content: space-between;

			.addname {
				font-size: 28rpx;
				font-weight: 600;
				margin-bottom: 5rpx;
			}

			.address {
				font-size: 26rpx;
				font-weight: 400;
			}
		}

		&>view {
			margin-top: 10rpx;

			&.wd-input {
				position: relative;
				padding: 10rpx 20rpx;

				.wd-input-font {
					margin: 0 0 20rpx 20rpx;
					display: inline-block;
					font-size: 30rpx;
					color: #333333;
					letter-spacing: 0;
					line-height: 30rpx;
					font-weight: 500;
				}

				&:nth-child(3) {
					margin-top: 20rpx;
				}
			}
		}

		.wd-hr {
			width: 100%;
			position: absolute;
			left: 0rpx;
			bottom: 90rpx;
			height: 1rpx;
			opacity: 0.1;
			background: #000000;
		}

		.wd-btn {
			text-align: center;
			// margin: 0rpx 0rpx 40rpx 0rpx;
			height: 90rpx;
			line-height: 90rpx;

			view {
				display: inline;
				width: 100%;
				font-size: 36rpx;
				color: #599FFF;
				letter-spacing: 0;
				text-align: center;
				font-weight: 500;
			}


		}
	}
</style>