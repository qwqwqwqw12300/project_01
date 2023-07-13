<!--
* @Author: zhanch
* @Date: 2023-03-24 15:51:21
* @FilePath: /pages/index/components/human-card.vue
* @Description: 人员卡片
-->

<template>
	<view class="wd-card-box">
		<view class="wd-list" v-for="human of humans" :key="'human' + human.humanId">
			<template v-if="getDevice(human).deviceId">
				<view class="wd-list-box active" v-if="getDevice(human).type === '1' || getDevice(human).type === '2'"
					@click="goDeciveDetails(getDevice(human))">
					<image :src="getDeviceImg(getDevice(human).type)"></image>
					<view class="wd-device-info">
						<text>{{human.name || '未命名人员'}}</text>
						<view class="wd-device-name">
							<view class="wd-list-static" :class="{online: getDevice(human).onlineFlag === '1'}"></view>
							<text class="wd-list-static-font" v-if="getDevice(human).onlineFlag === '1'">在线</text>
							<text class="wd-list-static-font" v-else-if="getDevice(human).onlineFlag === '2'">休眠</text>
							<text class="wd-list-static-font" v-else>离线</text>
						</view>
					</view>
					<u-badge v-if="getDevice(human).msgNum >= 1" color="#fff" :offset="[-1, 0]"
						:value="getDevice(human).msgNum" absolute>
					</u-badge>
				</view>
				<view class="wd-list-box active" v-if="getDevice(human).type === '3'"
					@click="goDeciveDetails(getDevice(human))">
					<image :src="getDeviceImg(getDevice(human).type)"></image>
					<view class="wd-device-info">
						<text>{{human.name || '未命名人员'}}</text>
						<view class="wd-device-name">
							<view class="wd-list-static" :class="{online: getDevice(human).onlineFlag === '1'}"></view>
							<text class="wd-list-static-font" v-if="getDevice(human).onlineFlag === '1'">在线</text>
							<text class="wd-list-static-font" v-else-if="getDevice(human).onlineFlag === '2'">休眠</text>
							<text class="wd-list-static-font" v-else>离线</text>
							<!-- <view class="wd-list-static" :class="{online: getDevice(human).onlineFlag === '4'}"></view> -->
							<image src="@/static/images/bell.png" style="width: 36rpx;height: 36rpx;" mode=""></image>
							<text class="wd-list-static-font" v-if="getDevice(human).deploymentState === '1'">蜂鸣</text>
							<text class="wd-list-static-font" v-else>静音</text>
						</view>
					</view>
					<u-badge v-if="getDevice(human).msgNum >= 1" color="#fff" :offset="[-1, 0]"
						:value="getDevice(human).msgNum" absolute>
					</u-badge>
				</view>
			</template>
			<!-- 空人员 -->
			<template v-else>
				<view class="wd-list-box wd-list-room active" @click="bindDevice(human)">
					<view>
						<u-text :block="false" :text="human.name" prefixIcon="/static/images/add-person.png"
							size="36rpx" :iconStyle="{height: '48rpx', width: '48rpx',marginRight:'20rpx'}">
						</u-text>
					</view>
					<text v-if="shareFlag == '2'" class="wd-link">点击绑定设备</text>
					<text v-else>暂无设备</text>
				</view>
			</template>
			<!-- /空人员 -->
		</view>
		<bind-device :payload="bindPayload" @next="next" ref="indexHumanCardBind" />
	</view>

</template>

<script>
	import {
		mapState,
		mapActions,
		mapGetters
	} from 'vuex';
	export default {
		name: 'human-card',
		props: {
			/**数据列表**/
			humans: Array,
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
				getDevice: state => {
					return item => {
						let arr = [];
						if (state.familyList.length) {
							const devices = state.familyList.find(ele => ele.familyId === item.familyId)
								.devices;
							arr = devices.filter(ele => ele.humanId === item.humanId)
						}
						return arr[0] || {};;
					}
				}
			}),
			getDeviceImg() {
				return type => {
					if (type === '1') return '/static/images/dzqgk.png';
					if (type === '2') return '/static/images/watch-device.png';
					if (type === '3') return '/static/images/sos/sos-equipment_1.png';
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
				humanId
			}) {
				if (this.shareFlag !== '2') return;
				this.bindPayload = {
					familyId,
					id: humanId,
					type: 'human'
				};
				this.$refs.indexHumanCardBind.open();
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
