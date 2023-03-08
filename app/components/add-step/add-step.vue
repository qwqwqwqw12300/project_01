<template>
	<view>
		<family-pop ref="addFamily" mode="add" @update="familyNext" />
		<room-pop ref="addRoom" @update="roomNext" />
		<bind-device :payload="{familyId, ...roomInfo}" @next="deviceNext" ref="bindDev" />
	</view>
</template>

<script>
	import {
		mapActions
	} from 'vuex';
	export default {
		data() {
			return {
				familyId: '',
				roomInfo: {},
				deviceList: []
			};
		},
		methods: {
			...mapActions(['getAllFamily', 'getAllDevices']),
			/**
			 * 添加家庭
			 */
			open() {
				// this.$refs.bindDev.open();
				this.$refs.addFamily.open();
			},
			/**
			 * 添加家庭下一步
			 */
			familyNext({
				familyId
			}) {
				this.getAllFamily();
				this.familyId = familyId;
				this.$refs.addRoom.open({
					familyId
				});
			},
			/**
			 * 添加房间下一步
			 */
			roomNext(info) {
				this.getAllFamily();
				this.roomInfo = info;
				this.$refs.bindDev.open();
			},
			/**
			 * 添加设备下一步
			 */
			deviceNext() {
				Promise.all([
					this.getAllFamily(),
					this.getAllDevices()
				]);
			}
		}
	}
</script>

<style lang="scss">

</style>
