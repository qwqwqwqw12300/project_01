<!--
* @Author: zhanch
* @Date: 2022-12-29 17:47:02
* @FilePath: /components/add-room/add-room.vue
* @Description: 房间设置
-->

<template>
	<u-popup :closeable="true" :round="10" :show="show" mode="center" @close="close">
		<view class="ui-add">
			<view class="ui-title">
				<text class="ui-title-font">{{mode === 'add' ? '新建家庭': '修改家庭'}}</text>
			</view>
			<view class="ui-add-box">
				<view class="ui-input">
					<text class="ui-input-font">房间名称</text>
					<u-input :maxlength="6" v-model="form.name" placeholder="请输入房间名称" border="bottom" clearable>
					</u-input>
				</view>
				<text class="ui-input-font">房间名称</text>
				<view class="ui-tags">
					<view class="ui-tags-item" v-for="(item, index) in tagsList" :key="index">
						<u-tag :text="item.name" :plain="!item.checked" type="warning" :name="item.name"
							@click="radioClick">
						</u-tag>
					</view>
				</view>
				<!-- <view v-if="mode === 'edit'">
					<u-text size="28rpx" prefixIcon="plus-circle" iconStyle="font-size: 40rpx" text="绑定雷达波">
					</u-text>
					<view class="ui-select">
						<uni-data-select v-model="deviceId" :clear="false" :localdata="devices"></uni-data-select>
					</view>
				</view> -->
			</view>
			<view class="ui-hr"></view>
			<view class="ui-btn">
				<view @click="next">{{ subTitle }}</view>
			</view>
		</view>
	</u-popup>
</template>

<script>
	import {
		PostAddRoom,
		PostEditRoom,
	} from '@/common/http/api.js';
	export default {
		props: {
			/**模式add-添加家庭 edit-修改家庭**/
			mode: {
				default: 'add'
			},
			subTitle: {
				default: '提交'
			}
		},
		data() {
			return {
				show: false,
				familyId: '',
				form: {
					name: '', //房间名称
					familyId: '', // 家庭id
					roomId: '' // 房间id
				},
				/**要绑定的雷达波设备**/
				deviceId: '',
				// 0:其他、1:书房、2:客厅、3:卧室、4:浴室、5:厨房、6:餐厅
				tagsList: [{
						name: '书房',
						value: 1,
						checked: false
					},
					{
						name: '客厅',
						value: 2,
						checked: false
					}, {
						name: '卧室',
						value: 3,
						checked: false
					}, {
						name: '浴室',
						value: 4,
						checked: false
					}, {
						name: '厨房',
						value: 5,
						checked: false
					}, {
						name: '餐厅',
						value: 6,
						checked: false
					}
				],
			}
		},
		computed: {
			devices: function() {
				return this.$store.getters.filterDevice({
					roomId: 0
				}).map(ele => ({
					text: ele.name,
					value: ele.deviceId
				}))
			}
		},
		methods: {
			radioClick(name) {
				// this.form.name = name
				this.form = {
					...this.form,
					name,
				}
				console.log(this.form.name, 'fff')
				this.tagsList.map((item) => {
					item.checked = item.name === name ? true : false
				})
			},
			close() {
				this.form = {}
				this.show = false;
			},
			open(obj = {}) {
				this.show = true;
				this.form = {
					...obj
				};
			},
			next() {
				const {
					name,
				} = this.form;
				if (!name) {
					return uni.$u.toast('请完善房间信息');
				}
				let roomType = '0';
				const roomInfo = this.tagsList.find(ele => ele.name === name);
				if (roomInfo) {
					roomType = roomInfo.value;
				}
				const handle = this.mode === 'add' ? PostAddRoom({
					...this.form,
					roomType
				}) : PostEditRoom({
					...this.form,
					roomType
				});
				handle.then(res => {
					uni.$u.toast('操作成功')
					this.close();
					setTimeout(() => {
						this.$emit('update', res.data && res.data.roomId)
					}, 500);
				})
			},
		}
	};
</script>

<style lang="scss">
	.ui-add {
		width: 582rpx;
		min-height: 300rpx;
		border-radius: 20rpx;
		filter: drop-shadow(0 0 5rpx rgba(7, 5, 5, 0.34));
		background-image: linear-gradient(-36deg, #e4e4e4 0%, #f8f8f8 100%);
		border: 1px solid #ffcb3d;
		padding: 20rpx 31rpx;

		.ui-title {
			display: flex;
			align-items: center;
			justify-content: center;

			.ui-title-font {
				font-family: PingFangSC-Medium;
				font-size: 36rpx;
				color: #333333;
				letter-spacing: 0;
				text-align: center;
				line-height: 46rpx;
				font-weight: 500;
			}
		}

		.ui-select {
			width: 500rpx;
			margin: 20rpx 0 0 20rpx;
			border-radius: 20rpx;
			background-color: #e4e4e4;
		}

		&>view {
			margin-top: 10rpx;

			.ui-input {
				position: relative;
				margin-bottom: 50rpx;

				&::after {
					bottom: 10rpx;
					left: 40rpx;
					content: '';
					width: 500rpx;
					height: 1rpx;
					background: #e4e4e4;
					position: absolute;
				}


			}

			.ui-tags {
				padding: 12rpx 20rpx;
				margin-top: 0 !important;

				.ui-tags-item {
					float: left;
					margin-top: 14rpx;
					margin-right: 20rpx;
				}
			}

			&.ui-add-box {
				margin-top: 20rpx;
				padding: 10rpx 0rpx;


				// border-bottom: 1px solid #e4e4e4;

				&>* {
					margin-top: 20rpx;
				}

			}
		}

		.ui-input-font {
			margin-left: 20rpx;
			font-family: PingFangSC-Medium;
			font-size: 30rpx;
			color: #333333;
			letter-spacing: 0;
			line-height: 30rpx;
			font-weight: 500;
		}

		.ui-slider {
			width: 100%;
			display: flex;
			flex-direction: row;
			align-items: center;
			justify-content: space-between;

			text {
				font-size: 26rpx;
				color: #999;
			}

			&>* {
				:nth-child(1) {
					width: 320rpx;
				}
			}
		}

		.ui-hr {
			width: 100%;
			position: absolute;
			left: 0rpx;
			bottom: 110rpx;
			height: 1rpx;
			opacity: 0.1;
			background: #000000;
		}

		.ui-btn {
			text-align: center;
			margin: 230rpx 0rpx 30rpx 0rpx;

			view {
				width: 100%;
				height: 30rpx;
				font-size: 28rpx;
				font-size: 36rpx;
				color: #599FFF;
				letter-spacing: 0;
				text-align: center;
				line-height: 36rpx;
				font-weight: 500;
			}
		}

		::v-deep .u-tag--warning {
			background-image: linear-gradient(#FFCF4D, #FD913B);
		}

		::v-deep .u-tag--medium {
			width: 70rpx;
			display: flex;
			align-items: center;
			justify-content: center;

		}
	}
</style>
