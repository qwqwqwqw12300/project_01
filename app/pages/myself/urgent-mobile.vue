<!-- 添加联系人 -->
<template>
	<app-body :bg="false">
		<view class="ui-logo">
			<app-logo color="#353535" text="紧急联系人设置"></app-logo>
			<view class="ui-screen-icon " style="margin-right: 20rpx;" @click="familyShow = true">
				<text>{{familyName}}</text>
				<u-icon size="35rpx" :name="familyShow ? 'arrow-down-fill' : 'arrow-up-fill'"></u-icon>
			</view>
		</view>
		<view class="ui-form">
			<view class="ui-form-item" v-for="(item,index) in contactList" :key="item.orderNum">
				<view class="item-title">
					<view class="title-left">
						<text></text>
						{{ item.orderName }}
					</view>
					<view class="title-right" @tap="getContact(index)">
						<u-icon name="/static/images/tel-book.png" size="44rpx"></u-icon>
					</view>
				</view>
				<view class="item-input">
					<view class="input-left">
						<u--input v-model="item.name" placeholder="请输入姓名" border="none" clearable></u--input>
					</view>
					<view class="input-right">
						<u--input v-model="item.phone" maxlength="11" type="number" placeholder="请输入手机号" border="none"
							placeholderStyle="text-align:right;color: rgb(192, 196, 204);" clearable></u--input>
					</view>
				</view>
			</view>
		</view>
		<view class="ui-btn">
			<view class="btn-box">
				<view class="cancel-btn" @click="handleCancel">
					取消
				</view>
				<view class="save-btn" @tap="handleSave">
					保存
				</view>
			</view>
		</view>
		<tel-books ref="telBookRef" @select="phoneSelect"></tel-books>
		<u-action-sheet :closeOnClickOverlay="true" :safeAreaInsetBottom="true" :closeOnClickAction="true"
			@close="familyShow = false" :show="familyShow" cancelText="取消">
			<view>
				<view @click="sheetSelect(item)" class="ui-sheet" v-for="(item, index) of getFamilyList"
					:key="'sheet' + index">
					<u-icon name="home" class="active" size="38rpx" v-if="item.value">
					</u-icon>
					<text>{{item.text}}</text>
				</view>
			</view>
		</u-action-sheet>
	</app-body>
</template>

<script>
	import {
		mapState
	} from 'vuex';
	import {
		GetContactsList,
		PostSetContacts,
		GetMemberContactskList,
		PostSetMemberConWithFamilyId
	} from '@/common/http/api.js';
	import {
		phoneValidator
	} from '../../common/utils/util';
	// import {
	// 	phoneValidator
	// } from '../../common/utils/util';
	let contactList = [{
			orderNum: 1,
			orderName: '第一紧急联系人',
			familyId:0
		},
		{
			orderNum: 2,
			orderName: '第二紧急联系人',
			familyId:0
		},
		{
			orderNum: 3,
			orderName: '第三紧急联系人',
			familyId:0
		},
	]
	export default {
		data() {
			return {
				index: 0,
				/**家庭列表是否展示**/
				familyShow: false,
				familyName:'',
				contactList
			}
		},
		computed: {
			...mapState({
				getFamilyList: state => {
					const familyList = state.familyList.map(ele => ({
						value: ele.familyId,
						text: ele.name
					}));
					
					return [ ...familyList]
		
				}
			})
		},
		methods: {
			getContact(index) {
				this.index = index
				this.$refs.telBookRef.show(true)
			},
			phoneSelect(data) {
				const { phone,name } = data[0]
				this.contactList[this.index].phone = phone
				this.contactList[this.index].name = name
				this.contactList = [...this.contactList]
			},
			handleInit(familyId) {
				console.log(familyId,'familyId')
				GetMemberContactskList({
					familyId:familyId
				}).then(res=>{
					console.log(res,'res')
					// console.log(res,'res-------------------------')
					this.contactList = contactList.map(item => {
						item.familyId = familyId
						const data = res.rows.find(n => {
							return n.orderNum === item.orderNum
						})
						return data!=undefined ? {
							...data,
							orderName: item.orderName
						} : {...item}
					})
					console.log(this.contactList, 'llllllllll')
				})
				// GetContactsList({}).then(res => {
				// 	console.log(res,'res++++++++++++++++++++++++++')
				// 	this.contactList = this.contactList.map(item => {
				// 		const data = res.rows.find(n => {
				// 			return n.orderNum === item.orderNum
				// 		})
				// 		return data ? {
				// 			...data,
				// 			orderName: item.orderName
				// 		} : item
				// 	})
				// 	console.log(this.contactList, 'llllllllll')
				// })
			},
			handleSave() {
				const list = uni.$u.deepClone(this.contactList)
				for (let i = 0; i < list.length; i++) {
					if (list[i].name || list[i].phone) {
						if (!list[i].name) return uni.$u.toast(`${list[i].orderName}姓名不能为空`)
						if (!phoneValidator(list[i].phone)) return uni.$u.toast(`${list[i].orderName}手机号不正确`)
					}
				}
				PostSetMemberConWithFamilyId(this.contactList).then(res => {
					console.log(res,'res')
					uni.$u.toast(res.msg)
					this.$store.commit('setContactInfo',res.rows)
					setTimeout(() => {
						// this.$store.dispatch('GetContactsList')
						this.handleCancel()
					}, 1000)
				})
			},
			handleCancel() {
				uni.navigateBack()
			},
			/**
			 *  选择家庭
			 **/
			sheetSelect(item) {
				console.log(item,'item')
				this.familyName = item.text
				this.handleInit(item.value)
				this.familyShow = false;
			},
		},
		onShow() {
			this.familyName = this.getFamilyList[0].text || '暂无家庭'
			this.handleInit(this.getFamilyList[0].value)
		}
	}
</script>

<style lang="scss">
	.ui-logo {
		display: flex;
		align-items: flex-end;
		justify-content: space-between;
		background: #ffffff;
		padding-bottom: 50rpx;
	}

	.ui-form {
		// padding: 0 44rpx;
		margin-top: 40rpx;

		.ui-form-item {
			background-color: #ffffff;
			margin-bottom: 40rpx;
			padding: 4rpx 30rpx;

			.item-title {
				display: flex;
				align-items: center;
				min-height: 100rpx;
				justify-content: space-between;
				border-bottom: solid 2px #f7f7f7;
				padding: 0 5rpx;

				.title-left {
					display: flex;
					align-items: center;
					height: 100%;
					justify-content: center;
					font-size: 36rpx;
					color: #FEAE43;
					font-weight: 600;

					text {
						height: 50rpx;
						border-left: solid 14rpx #FEAE43;
						border-radius: 4rpx;
						margin-right: 20rpx;
					}
				}
			}

			.item-input {
				min-height: 128rpx;
				padding: 0 10rpx;
				border-bottom: solid 2px #f7f7f7;
				display: flex;
				align-items: center;
				justify-content: space-between;

				.input-left,
				.input-right {
					width: 240rpx;
				}

			}
		}
	}

	.ui-btn {
		width: 100%;
		position: fixed;
		bottom: 0;
		left: 0;

		.btn-box {
			height: 100rpx;
			line-height: 100rpx;
			display: flex;
			font-size: 36rpx;

			.cancel-btn {
				flex: 1;
				background-color: #fff;
				color: #E95656;
				text-align: center;
			}

			.save-btn {
				flex: 1;
				background-image: linear-gradient(90deg, #FFB24D 0%, #FD913B 100%);
				color: #fff;
				text-align: center;
			}
		}
	}
	.ui-sheet {
		border-bottom: 1rpx solid #e2e2e2;
		display: flex;
		align-items: center;
		justify-content: center;
		height: 120rpx;
		width: 100%;
	
		text {
			margin-left: 10rpx;
		}
	}
	.ui-screen-icon {
		display: flex;
		flex-direction: row;
		align-items: center;
	
		>text {
			margin-left: 10rpx;
		}
	}
</style>
