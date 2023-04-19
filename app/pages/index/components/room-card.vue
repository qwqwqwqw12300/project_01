<!--
* @Author: zhanch
* @Date: 2023-03-24 09:31:14
* @FilePath: /pages/index/components/room-card.vue
* @Description: 房间设备卡片
-->

<template>
	<view class="wd-card-box">
		<view class="wd-list" v-for="room of rooms" :key="'r' + room.roomId">
			<template v-if="getDeives(room).deviceId">
				<!-- 雷达波设备 -->
				<view class="wd-list-box active" @click="goDeciveDetails(getDeives(room))">
					<image src="../../../static/images/leida-nm.png"></image>
					<view class="wd-device-info">
						<text>{{room.name || '未命名房间'}}</text>
						<view class="wd-device-name">
							<view class="wd-list-static" :class="{online: getDeives(room).onlineFlag === '1'}"></view>
							<text class="wd-list-static-font">{{getDeives(room).onlineFlag==='1' ? '在线':'离线'}}</text>
							<view class="wd-list-people" :class="{online: getDeives(room).hasPerson === '1'}"></view>
							<text class="wd-list-static-font">{{getDeives(room).hasPerson==='1' ? '有人':'无人'}}</text>
						</view>
					</view>
					<u-badge v-if="getDeives(room).msgNum >= 1" color="#fff" :offset="[-1, 0]"
						:value="getDeives(room).msgNum" absolute>
					</u-badge>
				</view>
			</template>
			<!-- 空房间 -->
			<template v-else>
				<view class="wd-list-box wd-list-room active" @click="bindDevice(room)">
					<view>
						<u-text :block="false" :text="room.name || '未命名房间'" :prefixIcon="getRoomIcon(room.roomType)"
							size="36rpx" :iconStyle="{height: '48rpx', width: '48rpx',marginRight:'20rpx'}"></u-text>
					</view>
					<text v-if="shareFlag == '2'" class="wd-link">点击绑定设备</text>
					<text v-else>暂无设备</text>
				</view>
			</template>
			<!-- /空房间 -->
		</view>
		<bind-device :payload="bindPayload" @next="next" ref="roomCardBind" />
	</view>
</template>

<script>
	import {
		mapState,
		mapActions,
		mapGetters
	} from 'vuex';
	export default {
		name: 'room-card',
		props: {
			/**数据列表**/
			rooms: Array,
			/**是否允许分享**/
			shareFlag: String
		},
		data() {
			return {
				/**绑定信息**/
				bindPayload: {},
			};
		},
		computed: {
			...mapState({
				getDeives: state => {
					return item => {
						let arr = [];
						if (state.familyList.length) {
							const devices = state.familyList.find(ele => ele.familyId === item.familyId)
								.devices;
							arr = devices.filter(ele => ele.roomId === item.roomId)
						}
						return arr[0] || {};;

					}
				}
			}),
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
		methods: {
			/**
			 * 跳转设备详情
			 */
			goDeciveDetails(info) {
				this.$store.commit('setDeviceInfo', info);
				this.$setCache('familyId', info.familyId);
				this.goPage('/pages/equipment/device-detail/device-detail');
			},

			/**
			 * 页面跳转
			 */
			goPage(url) {
				uni.navigateTo({
					url
				});
			},

			/**
			 * 绑定设备
			 */
			bindDevice({
				familyId,
				roomId,
				humanId
			}) {
				if (this.shareFlag !== '2') return;
				this.bindPayload = {
					familyId,
					id: roomId,
					type: 'room'
				};
				this.$refs.roomCardBind.open();
			},

			/**
			 * 操作完成
			 */
			next() {
				this.$emit('confirm');
			}

		}
	}
</script>

<style lang="scss" scoped>
	@import "./common.scss";
</style>
