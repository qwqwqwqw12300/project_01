<template>
	<u-popup :round="14" :show="visible" mode="bottom" @close="visible = false">
		<view style="height: 1400rpx;">
			<contact-select placeholder="请输入联系人姓名" @cityClick="phoneClick" formatName="name" :obtainCitys="contactList"
				:isSearch="true"></contact-select>
		</view>
	</u-popup>
</template>

<script>
	import {
		phoneRemove
	} from '@/common/utils/util';
	export default {
		data() {
			return {
				visible: false,
				contactList: [{
					name: '阿一',
					phone: '13554636343'
				}, {
					name: '斌二',
					phone: '13654636340'
				}, {
					name: '陈三',
					phone: '13344306340'
				}],
			}
		},
		methods: {
			show() {
				this.visible = true
				// this.getContact()
			},
			/**
			 * 选择手机联系人
			 */
			phoneClick(data) {
				this.$emit('select', data.phone)
				this.visible = false
				console.log(data)
			},
			/**
			 * 获取手机联系人
			 */
			getContact() {
				this.contactList = [];
				console.log(plus.contacts.ADDRESSBOOK_PHONE, 'ppppp000000000000000000')
				Promise.all([this.getTypeContact(plus.contacts.ADDRESSBOOK_PHONE), this.getTypeContact(plus.contacts
					.ADDRESSBOOK_SIM)]).then(res => {
					console.log(res, 'rerere')
					const data = [...res[0], ...res[1]];
					const obj = {};
					this.contactList = data.reduce(function(item, next) {
						obj[next.phone] ? '' : obj[next.phone] = true && item.push(next);
						return item;
					}, []);
					this.visible = true;
				})
			},
			/**
			 * 获取手机联系人
			 */
			getTypeContact(type) {
				return new Promise((resolve, reject) => {
					plus.contacts.getAddressBook(type, res => {
						res.find([], data => {
							console.log(data, 'ddddddd')
							const list = data.map(n => {
								const {
									displayName: name,
									phoneNumbers,
								} = n
								return {
									name,
									phone: phoneRemove(phoneNumbers[0]?.value || '')
								}
							}).filter(item => {
								return item.phone !== ''
							});
							console.log(list, 'sssssssssssssss')
							resolve(list)
						});

					}, error => {
						reject()
						uni.showToast({
							title: '获取通讯录失败',
							duration: 2000
						})
					})
				})
			},
		}
	}
</script>

<style lang="scss" scoped>
</style>
