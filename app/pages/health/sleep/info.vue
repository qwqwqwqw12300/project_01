<template>
	<app-body :bg="false" >
		<app-logo :text="text" :isShare="false"></app-logo>
		
	</app-body>
</template>

<script>
	import {
		WEEKMAP
	} from '@/config/db';
	export default {
		data() {
			return {
				text:''
			}
		},
		created() {
			
		},
		methods: {
			
			
		},
		onLoad(option) {
			this.text = option.name
		}
	}
</script>

<style lang="scss" scoped>
	
</style>
