<template>
	<view>
		<add-family ref="addFamily" @update="familyNext" />
		<add-room ref="addRoom" :familyId="familyId" @next="roomNext" />
		<bind-device ref="bindDev" />
	</view>
</template>

<script>
	import {
		mapActions
	} from 'vuex';
	export default {
		data() {
			return {
				/**新增的房间id**/
				familyId: 's'
			};
		},
		methods: {
			...mapActions(['getAllFamily', 'getAllDevices']),
			/**
			 * 添加家庭
			 */
			open() {
				this.$refs.addFamily.open();
			},
			/**
			 * 添加家庭下一步
			 */
			familyNext(familyId) {
				this.familyId = familyId;
				this.getAllFamily();
				this.$refs.addRoom.open();
			},
			/**
			 * 添加房间下一步
			 */
			roomNext() {
				this.getAllDevices();
				this.$refs.bindDev.open();
			},
		}
	}
</script>

<style lang="scss">

</style>