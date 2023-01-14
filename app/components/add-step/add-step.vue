<template>
	<view>
		<add-family ref="addFamily" @update="familyNext" />
		<add-room ref="addRoom" @next="roomNext" />
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
			familyNext() {
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