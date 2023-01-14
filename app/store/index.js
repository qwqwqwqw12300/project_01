import Vue from 'vue';
import Vuex from 'vuex';
import sdk from './sdk/sdk.js';
import { getFamilyList, getDeviceList, getRoomList } from '@/common/http/api.js';

Vue.use(Vuex);

//Vuex.Store 构造器选项
const store = {
	state: {
		serviceAxisY: 300,
		/**设备列表**/
		devicesList: [],
		/**家庭列表**/
		familyList:[],
		/**房间列表**/
		roomList:[]
	},
	mutations: {
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
		 * @param {Object} familyList 房间列表
		 */
		setFamilyList(state, familyList) {
			state.familyList = familyList;
		}
	},
	actions: {
		
		/**
		 * 查询所有设备
		 */
		getAllDevices(ctx) {
			return new Promise(resolve => {
				getDeviceList({}).then(({rows = []}) => {
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
				getFamilyList().then(({rows = []}) => {
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
				getRoomList().then(({rows = []}) => {
					ctx.commit('setRoomList', rows);
					resolve(rows);
				});
			})
		},
	},
	getters: {
		/**
		 * 获取家庭中得所有设备
		 */
		getDeviceOnFamily: state => id => {
			return state.devicesList.filter(item => item.familyId === id || item.familyId === 106);
		},
		
		/**
		 * 获取家庭中得所有房间
		 */
		getRoomOnFamily: state => id => {
			return state.roomList.filter(item => item.familyId === id);
		}
	}
}

export default new Vuex.Store({
	...store,
	modules: {
		sdk
	}
})
