import App from './App';
import Vue from 'vue';
import uView from '@/uni_modules/uview-ui';
import store from './store';

Vue.config.productionTip = false;
Vue.use(uView);
App.mpType = 'app';
Vue.prototype.$store = store
const app = new Vue({
	...App
});
app.$mount();
