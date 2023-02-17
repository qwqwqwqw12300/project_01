<!--
* @Author: zhanch
* @Date: 2022-12-28 11:01:47
* @FilePath: /pages/details/details.vue
* @Description: 信息详情
-->
<template>
	<app-body :bg="false" :hideTitle="true">
		<view class="ui-body">
			<view class="ui-title">
				<text>详情</text>
				<text>全部标记已读</text>
			</view>
			<view class="ui-screen">
				<view class="ui-family">
					<view class="">

					</view>
					<!-- 下拉框 -->
					<view class="ui-select" v-if="isfamilyShow">
						<view class="ui-select-item active">
							<u-icon name="home" class="active" color="#fff" size="40rpx">
							</u-icon>
							<text>vayyar</text>
						</view>
					</view>
					<!-- /下拉框 -->
				</view>
				<view class="ui-screen-icon">
					<u-icon size="32rpx" name="../../static/images/screen.png"></u-icon>
					<text>筛选</text>
				</view>
			</view>
		</view>
	</app-body>
</template>

<script>
	import {
		mapState
	} from 'vuex';
	export default {
		data() {
			return {
				/**事件信息**/
				eventInfo: {
					/**家庭id**/
					familyId: '',
					/**开始时间**/
					startDate: '',
					/**结束时间**/
					endDate: '',
					/**事件等级**/
					eventlevel: '',
					/**是否已读**/
					readFlag: '',
					/**设备类型**/
					deviceType: '',
				},
			};
		},
		computed: {
			...mapState({
				getFamilyList: state => {
					const list = state.familyList;
					return list.map(ele => ({
						value: ele.familyId,
						text: ele.name
					}));
				}
			})
		},
	}
</script>

<style lang="scss">
	.ui-body {
		color: #353535;

		.ui-title {
			padding: 34rpx 32rpx;
			background: #fff;
			padding-top: 50rpx;
			display: flex;
			justify-content: space-between;
			align-items: center;

			text {
				&:nth-child(1) {
					font-size: 50rpx;
				}

				&:nth-child(2) {
					font-size: 30rpx;
				}
			}
		}

		.ui-screen {
			padding: 0 32rpx;
			display: flex;
			align-items: center;
			justify-content: space-between;
			height: 100rpx;

			.ui-family {
				width: 180rpx;
			}
		}
	}
</style>
