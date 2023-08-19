<template>
	<u-popup :round="14" :show="visible" mode="bottom" @close="visible = false" :closeable="true">
		<view :style="{'height': height+ 'px'}">
			<contact-select placeholder="请输入联系人姓名" @click="phoneClick" formatName="name" :obtainTels="contactList"
				:isSearch="true" :isSingle="isSingle"></contact-select>
		</view>
	</u-popup>
</template>

<script>
	import {
		phoneRemove,
		isIos,
	} from '@/common/utils/util';
	export default {
		data() {
			return {
				height: 1300,
				visible: false,
				isSingle: true,
				contactList: [],
			}
		},
		created() {
			const height = (uni.getSystemInfoSync().windowHeight) * 0.8
			this.height = height > 650 ? height : 650
			console.log(this.height, 'ooo---------------------')
		},
		methods: {
			show(params) {
				console.log(params)
				this.isSingle = false
				// this.visible = true
				this.getContact()
			},
			/**
			 * 选择手机联系人
			 */
			phoneClick(data) {
				this.$emit('select', data)
				this.visible = false
			},
			/**
			 * 获取手机联系人
			 */
			getContact() {
				// this.contactList = [];
				if (this.contactList.length) return this.visible = true
				uni.showLoading({
					title: '获取中'
				})
				const readInfo = isIos() ? [this.getTypeContact(plus.contacts.ADDRESSBOOK_PHONE)] : [this.getTypeContact(
					plus.contacts.ADDRESSBOOK_PHONE), this.getTypeContact(plus.contacts
					.ADDRESSBOOK_SIM)]
				Promise.all(readInfo).then(res => {
					let data = []
					res.forEach(n => {
						data = [...data, ...n]
					})
					const obj = {};
					this.contactList = data.reduce(function(item, next) {
						obj[next.phone] ? '' : obj[next.phone] = true && item.push(next);
						return item;
					}, []);
					this.visible = true;
					uni.hideLoading()
				})
			},
			/**
			 * 获取手机联系人
			 */
			getTypeContact(type) {
				return new Promise((resolve, reject) => {
					plus.contacts.getAddressBook(type, res => {
						res.find([], data => {
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
							resolve(list)
						});

					}, error => {
						reject()
						uni.$u.toast('获取通讯录失败')
					})
				})
			},
		}
	}
</script>

<style lang="scss" scoped>
</style>