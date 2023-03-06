import Vue from 'vue';
import Vuex from 'vuex';
import sdk from './sdk/sdk.js';
import service from './service/service.js';
import {
	getFamilyList,
	getRoomList,
	PostGetPushMsgState,
	getDeviceListState,
	GetContactsList
} from '@/common/http/api.js';


Vue.use(Vuex);

//Vuex.Store 构造器选项
const store = {
	state: {
		/**临时缓存对象**/
		cache: new Map(),
		/**智能客服坐标**/
		serviceAxisY: 300,
		/**设备列表**/
		devicesList: [],
		/**家庭列表**/
		familyList: [],
		/**房间列表**/
		roomList: [],
		/**设备信息**/
		deviceInfo: {},
		/**用户信息**/
		userInfo: {},
		/**紧急联系人列表**/
		contactList: [],
	},
	mutations: {

		/**
		 * 设置缓存
		 */
		setCache(state, {
			key,
			value
		}) {
			state.cache.set(key, value);
		},

		/**
		 * 清除某个缓存
		 * @param {Object} state
		 * @param {Object} key
		 */
		removeCache(state, key) {
			state.cache.delete(key);
		},

		/**
		 * 人工客服Y轴
		 * @param {Object} state
		 * @param {Object} axisY
		 */
		setServiceAxisY(state, axisY) {
			state.serviceAxisY = axisY;
		},
		/**
		 * 设置设备列表
		 * @param {Object} state
		 * @param {Object} devicesList 设备列表
		 */
		setDevicesList(state, devicesList) {
			state.devicesList = devicesList;
		},
		/**
		 * 设置房间列表
		 * @param {Object} state
		 * @param {Object} roomList 房间列表
		 */
		setRoomList(state, roomList) {
			state.roomList = roomList;
		},
		/**
		 * 设置家庭列表
		 * @param {Object} state
		 * @param {Object} familyList 家庭列表
		 */
		setFamilyList(state, familyList) {
			state.familyList = familyList;
		},
		/**
		 * 设置设备信息
		 * @param {Object} state
		 * @param {Object} info
		 */
		setDeviceInfo(state, info) {
			state.deviceInfo = info;
		},
		/**
		 * 设置设置紧急联系人列表
		 * @param {Object} state
		 * @param {Object} info
		 */
		setSosMobileBook(state, list) {
			state.sosMobileBook = list;
		},
		/**
		 * 设置用户信息
		 */
		setUserInfo(state, info) {
			state.userInfo = info;
		},

		/**
		 * 设置紧急联系人
		 */
		setContactInfo(state, info) {
			state.contactList = info;
		},



	},
	actions: {

		/**
		 * 查询所有设备
		 */
		getAllDevices(ctx) {
			return new Promise(resolve => {
				getDeviceListState({}).then(({
					rows = []
				}) => {
					ctx.commit('setDevicesList', rows);
					resolve(rows);
				});
			})
		},

		/**
		 * 查询所有家庭
		 */
		getAllFamily(ctx) {
			return new Promise(resolve => {
				getFamilyList({
					pageSize: 10000
				}).then(({
					rows = []
				}) => {
					ctx.commit('setFamilyList', rows);
					resolve(rows);
				});
			})
		},

		/**
		 * 查询所有房间
		 */
		getAllRoom(ctx) {
			return new Promise(resolve => {
				getRoomList({
					family: ''
				}).then(({
					rows = []
				}) => {
					ctx.commit('setRoomList', rows);
					resolve(rows);
				});
			})
		},

		/**查询用户信息**/
		getPushMsgState(ctx) {
			return new Promise(resolve => {
				PostGetPushMsgState({}, {
					showLoading: false,
					error: false
				}).then(res => {
					console.log(res, 'res');
					ctx.commit('setUserInfo', res.data);
					resolve(true);
				}, err => {
					ctx.commit('setUserInfo', {});
					resolve(false)
				});
			});
		},
		/**查询紧急联系人信息**/
		GetContactsList(ctx) {
			return new Promise(resolve => {
				GetContactsList({}).then(res => {
					ctx.commit('setContactInfo', res.rows);
					resolve(true);
				}, err => {
					ctx.commit('setContactInfo', []);
					resolve(false)
				});
			});
		},
		/**更新设备数据**/
		updateDevacesInfo(ctx) {
			ctx.dispatch('getAllFamily').then(res => {
				const {
					deviceId,
					familyId
				} = this.state.deviceInfo;
				const family = this.state.familyList.find(ele => ele.familyId === familyId);
				const device = family.devices.find(ele => ele.deviceId === deviceId);
				if (device) {
					console.log('设备信息修改', device);
					ctx.commit('setDeviceInfo', device);
				}

			});
		}

	},
	getters: {
		/**
		 * 获取指定条件的设备j
		 */
		filterDevice: state => obj => {
			let devices = state.devicesList || [];
			for (let item in obj) {
				devices = devices.filter(ele => ele[item] === obj[item]);
			}
			return devices;
		},

		/**
		 * 获取家庭中得所有房间
		 */
		filterRoom: state => id => state.roomList.filter(item => item.familyId === id),

		/**
		 * 获取用户信息
		 */
		userInfo: state => state.userInfo,

		/**
		 * 读取缓存
		 */
		getCache: state => key => state.cache.get(key),

		/**
		 * 获取紧急联系人列表
		 */
		contactList: state => state.contactList,
	}
}

export default new Vuex.Store({
	...store,
	modules: {
		sdk,
		service
	}
})
