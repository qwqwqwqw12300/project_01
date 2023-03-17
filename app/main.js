import App from './App';
import Vue from 'vue';
import uView from '@/uni_modules/uview-ui';
import store from './store';
import {
	log
} from './common/utils/log';


Vue.config.productionTip = false;
Vue.use(uView);
App.mpType = 'app';
Vue.prototype.$store = store;
// 全局缓存
Vue.prototype.$setCache = (key, value) => store.commit('setCache', {
	key,
	value
});
Vue.prototype.$getCache = key => store.getters.getCache(key);
Vue.prototype.$removeCache = key => store.getters.commit('removeCache', key);
// 页面埋点
Vue.prototype.$apm = (option) => {
	log.apm(option);
};
// 自定义弹窗
uni.showModal = e => store.commit('setModal', e);
const app = new Vue({
	...App
});
app.$mount();
