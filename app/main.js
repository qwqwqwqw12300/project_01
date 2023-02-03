import App from './App';
import Vue from 'vue';
import uView from '@/uni_modules/uview-ui';
import store from './store';

Vue.config.productionTip = false;
Vue.use(uView);
App.mpType = 'app';
Vue.prototype.$store = store;
Vue.prototype.$setCache = (key, value) => store.commit('setCache', {
	key,
	value
});
Vue.prototype.$getCache = key => store.getters.getCache(key);
Vue.prototype.$removeCache = key => store.getters.commit('removeCache', key);

const app = new Vue({
	...App
});
app.$mount();
