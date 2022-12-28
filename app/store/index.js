import Vue from 'vue';
import Vuex from 'vuex';
import sdk from './sdk/sdk.js';
Vue.use(Vuex);

//Vuex.Store 构造器选项
const store = {
	state: {
		serviceAxisY: 300
	},
	mutations: {
		setServiceAxisY(state, axisY) {
			state.serviceAxisY = axisY;
		}
	}
}

export default new Vuex.Store({
	...store,
	modules: {
		sdk
	}
})
