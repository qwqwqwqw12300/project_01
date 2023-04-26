import {
	PsotSetJGInfo
} from "../../common/http/api";
import { push } from "../../common/sdk/push";
import {
	isIos,
	isApp
} from "../../common/utils/util";

export default {
	state: {
		/**设备id**/
		registrationId: ''
	},
	getters: {

	},
	mutations: {
		setRegistrationID(state, id) {
			state.registrationId = id;
		}
	},
	actions: {
		/**设置设备id**/
		setJGInfo(ctx) {
			return new Promise(async resolve => {
				if (!isApp()) return;
				let id;
				if (this.registrationId) {
					id = this.registrationId;
				} else {
					id = await push.getRegistrationID();
					ctx.commit('setRegistrationID', id);
				}
				PsotSetJGInfo({
					registrationType: isIos() ? '1' : '0',
					registrationId: id
				});
			});
		},
	}
}