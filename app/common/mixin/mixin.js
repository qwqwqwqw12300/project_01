const mixin = {
	computed: {
		...mapState({
			getDeviceRange: state => (obj) => {
				let devices = state.devices;
				for(let item in obj) {
					devices = devices.filter(ele => ele[item] === obj[item]);
				}
			}
		})
	},
	mounted() {
		
	}
}
