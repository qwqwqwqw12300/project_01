<!--
* @Author: zhanch
* @Date: 2022-12-27 15:53:29
* @FilePath: /pages/index/index.vue
* @Description: 应用首页
-->

<template>
	<view @click="closeDevice">
		<app-body :hideTitle="true">
			<view class="ui-scan">
				<view class="ui-sys-message">
					<u-badge :isDot="!!readInfo.length" absolute :offset="[0, 0]"></u-badge>
					<u-icon name="chat" @click="goPage('/pages/service/system-messages')" class="active" color="#000"
						size="50rpx"></u-icon>
				</view>
				<u-icon name="plus-circle" @click="addDevice" :stop="true" class="active" color="#000" size="45rpx">
				</u-icon>
				<!-- 下拉框 -->
				<!-- <view class="ui-select" v-if="isAddShow">
					<view class="ui-select-item active" @click="goPage('/pages/equipment/add/radar')">
						<u-icon name="../../static/images/leida-nm.png" class="active" color="#fff" size="48rpx">
						</u-icon>
						<text>雷达波设备</text>
					</view>
					<view class="ui-select-item active" @click="goPage('/pages/equipment/add/monitor')">
						<u-icon name="../../static/images/dzqgk.png" class="active" color="#fff" size="48rpx">
						</u-icon>
						<text>电子牵挂卡</text>
					</view>
					<view class="ui-select-item active" @click="goPage('/pages/equipment/add/monitor')">
						<u-icon name="../../static/images/watch.png" class="active" color="#fff" size="48rpx">
						</u-icon>
						<text>监测手表</text>
					</view>
					<view class="ui-select-item active" @click="isAddShow=false"> 
						<text>取消</text>
					</view>
				</view> -->
				<AppHandle :isShow="isAddShow" @cancle="isAddShow = false"></AppHandle>
				<!-- /下拉框 -->
			</view>
			<view class="ui-logo">
				<image src="../../static/images/index/logo.png"></image>
				<text>艾吉通</text>
			</view>

			<!-- 家庭tab -->
			<view class="ui-tab" v-if="tabList.length">
				<u-tabs @change="tabChange" :list="tabList" lineColor="unset"
					:activeStyle="{color: '#353535', fontSize: '40rpx'}"
					:inactiveStyle="{color: '#888888', fontSize: '40rpx'}"></u-tabs>
			</view>
			<!-- /家庭tab -->
			<!-- 家庭列表 -->
			<template v-if="familyList.length">
				<view class="ui-group" v-for="(familyItem, index) of familyList" :key="'family' + index">
					<view class="ui-title">
						<view>
							<u-text :text="familyItem.name" size="32rpx" :iconStyle="{height: '40rpx', width: '40rpx'}"
								prefixIcon="../../static/images/index/home.png"></u-text>
						</view>
						<view class="ui-share-box">
							<u-text @click="goPage('/pages/myself/famliy-manage')"
								prefixIcon="../../static/images/index/edit.png" size="32rpx" :align="'right'"
								:block="false" :iconStyle="{
								height: '40rpx', width: '40rpx'
							}" text="编辑" v-if="familyItem.shareFlag === '2'"></u-text>
							<u-text
								@click="goPage('/pages/share/share?familyId='+ familyItem.familyId + '&name='+ familyItem.name)"
								prefixIcon="../../static/images/index/share.png" size="32rpx" :align="'right'"
								:block="false" :iconStyle="{
								height: '40rpx', width: '40rpx'
							}" text="分享" v-if="familyItem.shareFlag === '2'"></u-text>
						</view>

					</view>
					<view class="ui-device">
						<view class="ui-list" v-for="room of familyItem.rooms" :key="room.roomId">
							<template v-if="getDeives(room).deviceId">
								<view class="ui-list-box active" @click="goDeciveDetails(getDeives(room))">
									<image src="../../static/images/leida-nm.png"></image>
									<view class="ui-device-info">
										<text>{{room.name || '未命名房间'}}</text>
										<view class="ui-device-name">
											<view class="ui-list-static"
												:class="{online: getDeives(room).onlineFlag === '1'}"></view>
											<text class="ui-list-static-font">{{getDeives(room).onlineFlag === '1' ? '在线': getDeives(room).onlineFlag === '0' ? '离线' : ''}}</text>
											<view class="ui-list-people"
												:class="{online: getDeives(room).onlineFlag === '0'}"></view>
											{{getDeives(room).onlineFlag === '1' ? '有人': getDeives(room).onlineFlag === '0' ? '无人' : ''}}
										</view>
									</view>
									<u-badge v-if="getDeives(room).msgNum > 1" color="#fff" :offset="[-1, 0]"
										:value="getDeives(room).msgNum" absolute>
									</u-badge>
								</view>
							</template>
							<!-- 空房间 -->
							<template v-else>
								<view class="ui-list-box ui-list-room active"
									@click="bindDevice(room,familyItem.shareFlag)">
									<view>
										<u-text :block="false" :text="room.name || '未命名房间'"
											:prefixIcon="getRoomIcon(room.roomType)" size="36rpx"
											:iconStyle="{height: '48rpx', width: '48rpx'}"></u-text>
									</view>
									<text v-if="familyItem.shareFlag == '2'" class="ui-link">点击绑定设备</text>
									<text v-else>暂无设备</text>
								</view>
							</template>
							<!-- /空房间 -->
						</view>
						<!-- 新增房间 -->
						<view class="ui-list ui-list-add" v-if="familyItem.shareFlag == '2'">
							<view class="ui-list-box" @click="addRoom(familyItem)">
								<u-icon name="../../static/images/index/add.png" size="56rpx"></u-icon>
								<text>添加</text>
							</view>
						</view>
						<!-- /新增房间 -->
					</view>
				</view>

			</template>
			<!-- /家庭列表 -->
			<!-- 空户 -->
			<template v-else>
				<view class="ui-default">
					<image src="../../static/images/index/empty.png" mode=""></image>
					<text>未添加任何家庭</text>
				</view>
			</template>
			<!-- /空户 -->
			<!-- 添加 -->
			<view class="ui-add-btn">
				<image @click="addStep" src="../../static/images/index/add-family.png" mode=""></image>
				<text>新建家庭</text>
			</view>
			<add-step ref="addStepRef"></add-step>
			<bind-device :payload="bindPayload" @next="handleInitList" ref="indexBindDev" />
			<room-pop ref="indexAddRoom" @update="handleInitList" />
		</app-body>
	</view>


</template>
<script>
	import {
		getDeviceList,
		getFamilyList,
		GetReadInfo
	} from '../../common/http/api';
	import {
		mapState,
		mapActions,
		mapGetters
	} from 'vuex';
	import
		AppHandle
	 from '@/components/add-handle/add-handle.vue';
	export default {
		components: {
			AppHandle
		},
		data() {
			return {
				/**是否展示添加弹窗**/
				isAddShow: false,
				/**公告状态**/
				readInfo: [],
				/**绑定信息**/
				bindPayload: {},
				/**当前选中的家庭**/
				currentTab: ''
			}
		},
		computed: {
			...mapState({
				/**所有家庭列表**/
				familyList: function(state) {
					if (this.currentTab) {
						return state.familyList.filter(ele => ele.familyId === this.currentTab);
					} else {
						return state.familyList;
					}
				},
				tabList: function(state) {
					const list = state.familyList.map(ele => ({
						name: ele.name,
						id: ele.familyId
					}));
					return [{
						name: '全部',
						id: ''
					}, ...list];
				},
				getDeives: state => {
					return room => {
						let obj = {};
						if (state.familyList.length) {
							const devices = state.familyList.find(ele => ele.familyId === room.familyId)
								.devices.filter(ele => ele.roomId === room.roomId);
							obj = devices[0] || {};
						}
						return obj;

					}
				},

			}, ),
			...mapGetters(['filterDevice']),
			/**获取房间图标**/
			getRoomIcon: () => {
				return (type) => {
					let pic;
					switch (type) { //  0:其他、1:书房、2:客厅、3:卧室、4:浴室、5:厨房、6:餐厅
						case '1':
						case '2':
							pic = `../../static/images/index/room${type}.png`;
							break;
						default:
							pic = `../../static/images/index/room0.png`;
							break;
					}
					return pic;
				}
			}

		},
		onShow() {
			this.handleInitList();
		},
		methods: {
			...mapActions(['getAllFamily', 'getAllDevices', 'getPushMsgState', 'getAllRoom']),

			handleInitList() {
				Promise.all([
					this.getAllFamily(),
					this.getAllDevices(),
					this.getReadInfo(),
					this.getPushMsgState()
				]);
			},
			/**
			 * 打开添加按钮
			 */
			addDevice() {
				setTimeout(() => {
					this.isAddShow = true;
				}, 10);
			},
			/**
			 * 关闭添加按钮
			 */
			closeDevice() {
				this.isAddShow = false;
			},
			/**
			 * 页面跳转
			 */
			goPage(url) {
				if (url === '/pages/equipment/add/monitor') return uni.$u.toast('暂不支持添加该设备');
				uni.navigateTo({
					url
				});
			},
			/**
			 * 跳转设备详情
			 */
			goDeciveDetails(info) {
				this.$store.commit('setDeviceInfo', info);
				let url;
				if (info.type === '0') { // 雷达波
					url = '/pages/equipment/radar-detail/radar-detail';
				} else { // 监控设备
					url = '/pages/equipment/watch-detail';
				}
				this.$setCache('familyId', info.familyId);
				this.goPage(url);

			},

			/**
			 * 添加家庭
			 */
			addStep() {
				this.$refs.addStepRef.open();
			},

			/**
			 * 添加房间
			 */
			addRoom(item) {
				const {
					shareFlag,
					familyId
				} = item
				if (shareFlag !== '2') return uni.$u.toast('该家庭不支持新增房间')
				this.$refs.indexAddRoom.open({
					familyId
				});
			},

			/**
			 * 获取公告状态
			 */
			getReadInfo() {
				return new Promise(resolve => {
					GetReadInfo().then(res => {
						this.readInfo = res.data;
						resolve();
					});
				})
			},


			/**
			 * 绑定设备
			 */
			bindDevice({
				familyId,
				roomId
			}, shareFlag) {
				if (shareFlag !== '2') return;
				this.bindPayload = {
					familyId,
					roomId
				};
				this.$refs.indexBindDev.open();
			},

			/**
			 * tab切换
			 */
			tabChange($e) {
				this.currentTab = $e.id;
			}
		}
	}
</script>

<style lang="scss">
	.ui-scan {
		display: flex;
		position: absolute;
		right: 24rpx;
		top: 120rpx;
		z-index: 2;

		.ui-sys-message {
			position: relative;

		}

		&>* {
			margin-left: 20rpx;
		}

		.ui-select {
			position: fixed;
			left: 50%;
			bottom:var(--window-bottom,0);
			transform: translate(-50%,0%);
			margin:0 auto;
			// transform: translate(-50%,-50%);
			width: 686rpx;
			height: 392rpx;
			border-radius: 24rpx;
			// filter: drop-shadow(0 0 5px rgba(7, 5, 5, 0.34));
			background: #fff;
			border: 1px solid #e2e2e2;
			.ui-select-item {
				// margin-top: 16rpx;
				width: 686rpx;
				height: 98rpx;
				display: flex;
				align-items: center;
				justify-content: center;
				font-size: 26rpx;
				color: #000;
				border-bottom: 1px solid #e2e2e2;

				text {
					display: inline-block;
					margin-left: 20rpx;
					font-size: 34rpx;
					color: #000000;
					text-align: center;
					line-height: 34rpx;
					font-weight: 400;
				}
			}
		}
	}

	.ui-logo {
		display: inline-flex;
		width: 100%;
		align-items: center;
		flex-direction: row;
		margin: 100rpx 32rpx 50rpx 32rpx;
		font-size: 56rpx;
		color: #353535;
		font-weight: bold;

		image {
			height: 80rpx;
			width: 80rpx;
		}

	}

	.ui-tab {
		box-sizing: border-box;
		width: 100%;
		padding: 0 32rpx;
	}

	.ui-group {
		width: 100%;
		margin-top: 61rpx;
		display: inline-flex;
		flex-direction: column;
		align-items: center;

		&:nth-last-child(1) {
			min-height: 550rpx;
		}

		.ui-title {
			box-sizing: border-box;
			padding: 0 16rpx;
			display: flex;
			align-items: center;
			justify-content: space-between;
			height: 80rpx;
			width: calc(100% - 74rpx);
			overflow: hidden;
			font-weight: bold;

			.ui-share-box {
				width: 250rpx;
				display: flex;
				align-items: center;
				flex-direction: row;
			}
		}

		.ui-device {
			display: inline-block;
			width: calc(100% - 64rpx);
			margin-top: 36rpx;

			.ui-list {
				margin-bottom: 20rpx;
				float: left;
				height: 160rpx;
				width: calc(50% - 11rpx);
				text-align: center;

				&:nth-child(even) {
					margin-left: 20rpx;
				}

				.ui-list-box {
					position: relative;
					height: 100%;
					width: 100%;
					display: inline-flex;
					align-items: center;
					flex-direction: row;
					background: #FFFFFF;
					border-radius: 16rpx;
					padding-left: 15rpx;
					box-sizing: border-box;

					image {
						height: 100rpx;
						width: 100rpx;
					}

					.ui-device-info {
						display: flex;
						flex-direction: column;
						margin-left: 10rpx;
					}

					text {
						display: inline-block;
						font-size: 36rpx;
						color: #353535;
					}

					.ui-device-name {
						display: flex;
						align-items: center;
						flex-direction: row;
						margin-top: 10rpx;
						color: #888888;
						font-size: 26rpx;
						.ui-list-static-font{
							font-size: 26rpx;
							margin-right: 10rpx;
							color: #888888;
						}
					}

					.ui-list-static {
						margin-right: 10rpx;
						margin-bottom: 6rpx;
						height: 36rpx;
						width: 36rpx;
						border-radius: 50% 50%;
						background-image: url('@/static/images/index/off-line.png');
						background-size: 100% 100%;
						&.online {
							background-image: url('@/static/images/index/online.png');
							background-size: 100% 100%;
						}
					}
					.ui-list-people{
						margin-right: 10rpx;
						margin-bottom: 6rpx;
						height: 36rpx;
						width: 36rpx;
						border-radius: 50% 50%;
						background-image: url('@/static/images/index/someone.png');
						background-size: 100% 100%;
						&.online {
							background-image: url('@/static/images/index/nobody.png');
							background-size: 100% 100%;
						}
					}

					&.ui-list-room {
						flex-direction: column;
						align-items: center;
						justify-content: center;
						padding: 0;

						>text {
							margin-top: 26rpx;
							font-size: 24rpx;
						}
					}
				}

				&.ui-list-add {
					text-align: left;

					.ui-list-box {
						width: 156rpx;
						height: 160rpx;
						display: flex;
						align-items: center;
						justify-content: center;
						flex-direction: column;
						padding: 0;

						text {
							margin-top: 20rpx;
							font-size: 24rpx !important;
						}
					}

				}

			}
		}
	}

	.ui-default {
		width: 100%;
		box-sizing: border-box;
		display: inline-block;
		margin: 185rpx 0 80rpx 0;
		padding: 0 200rpx;
		text-align: center;

		image {
			max-width: 330rpx;
			max-height: 330rpx;
			margin-bottom: 10rpx;
		}

		text {
			font-size: 36rpx;
			color: #888888;
			font-weight: 500;
		}
	}

	.ui-link {
		color: #0094ff !important;
	}

	.ui-add-btn {
		margin: 50rpx 0;
		display: flex;
		align-items: center;
		justify-content: center;
		flex-direction: column;

		image {
			width: 100rpx;
			height: 100rpx;
			margin-bottom: 10rpx;
		}

		text {
			font-size: 24rpx;
			color: #888888;
		}
	}
</style>
