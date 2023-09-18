<template>
	<!-- 联系人选择-->
	<view class="ui-box">
		<view class="ui-search">
			<u--input class="search-input" @input="keyInput" :placeholder="placeholder" prefixIcon="search"
				prefixIconStyle="font-size: 22px;color: #909399" />
		</view>
		<checkbox-group class="block" @change="checkboxChange">
			<scroll-view :style="{'height': height+ 'px'}" :scroll-top="scrollTop" scroll-y="true"
				class="ui-select-main" id="ui-select-main" :scroll-into-view="toView">
				<!-- 联系人列表(搜索前) -->
				<view class="tel-list" v-if="!serachValue">
					<view v-for="(record, index) in sortItems" :key="index" v-show="record.isCity" class="select-row">
						<view class="citys-item-letter"
							:id="'city-letter-' + (record.name === '#' ? '0' : record.name)">
							{{ record.name }}{{index+1}}
						</view>
						<view class="citys-item" v-for="(item, inx) in record.citys" :key="inx"
							@click="contactTrigger(item)">
							<checkbox v-if="!isSingle" class='round' :value="item.oldData.phone"
								:checked="item.checked" />
							<text>{{ item.oldData.name }}</text>
						</view>
					</view>
				</view>
				<!-- 联系人列表(搜索后)  -->
				<view class="tel-list" v-if="serachValue">
					<view v-for="(item, index) in searchDatas" :key="index" class="select-row">
						<view class="citys-item" :key="index" @click="contactTrigger(item)">
							<checkbox v-if="!isSingle" class='round' :value="item.phone"
								:checked="item.checked" />
							<text>{{ item.name }}</text>
						</view>
					</view>
				</view>
			</scroll-view>
		</checkbox-group>
		<!-- 联系人选择索引-->
		<view class="tel-indexs-view" v-if="!serachValue">
			<view class="tel-indexs">
				<view v-for="(cityIns, index) in handleTels" class="tel-indexs-text"
					:class="{ 'active': cityIns.forName === forName }" v-show="cityIns.isCity" :key="index"
					@click="telIndex(cityIns.forName)">
					{{ cityIns.name }}
				</view>
			</view>
		</view>
		<view class="ui-div"></view>
		<view class="ui-footer" v-if="!isSingle">
			<view class="footer-box">
				<button class="select-btn" @click="selectAll">{{ checkbox_all ? '取消': '全选' }}</button>
				<text>已选({{ selectData.length }})</text>
				<button @click="handleComplete">完成</button>
			</view>
		</view>
	</view>
</template>

<script>
	import citySelect from './citySelect.js';
	export default {
		props: {
			//查询提示文字
			placeholder: {
				type: String,
				default: '请输入联系人'
			},
			//传入要排序的名称
			formatName: {
				type: String,
				default: 'cityName'
			},
			//联系人数据
			obtainTels: {
				type: Array,
				default: () => []
			},
			//是否有搜索
			isSearch: {
				type: Boolean,
				default: true
			},
			isSingle: {
				type: Boolean,
				default: true
			}
		},
		data() {
			return {
				height: 500,
				contactList: [], //联系人列表
				handleTels: [], // 处理后的联系人数据
				telIndexs: [], // 城市索引
				serachValue: '', // 搜索的联系人
				scrollTop: 0, //scroll-view 滑动的距离
				toView: 'city-letter-Find', //锚链接 初始值
				forName: '',
				checkbox_all: false, //是否全选
				selectData: [], //已选取的数据，
				contactData: [],
			}
		},
		created() {
			const height = (uni.getSystemInfoSync().windowHeight) * 0.8
			this.height = (height > 650 ? height : 650) - (this.isSingle ? 120 : 160)
			// 初始化城市数据
			this.contactList = this.obtainTels;
			this.contactData = this.obtainTels
			this.initializationTel();
			this.buildTelindexs();
		},
		computed: {
			findCheck: function() {
				return function(item) {
					return this.contactData.length && this.contactData.find(n => {
						return n.phone === item.phone
					}).checked
				}
			},
			findValue() {
				return function(item) {
					return this.contactData.length && this.contactData.find(n => {
						return n.phone === item.phone
					}).phone
				}
			},
			getAddress: function() {
				return function(item) {
					const {
						province,
						city,
						district,
						address
					} = item;
					return province + city + district + address;
				}
			},
			/**
			 * @desc 城市列表排序
			 * @return  Array
			 */
			sortItems() {
				for (let index = 0; index < this.handleTels.length; index++) {
					if (this.handleTels[index].isCity) {
						let cityArr = this.handleTels[index].citys;
						cityArr = cityArr.sort(function(a, b) {
							const value1 = a.unicode;
							const value2 = b.unicode;
							return value1 - value2;
						});
					}
				}
				return this.handleTels;
			},
			/**
			 * @desc 搜索后的城市列表
			 * @return Array
			 */
			searchDatas() {
				const searchData = [];
				for (let i = 0; i < this.contactList.length; i++) {
					if (this.contactList[i][this.formatName] && this.contactList[i][this.formatName].indexOf(this.serachValue) !== -1) {
						searchData.push({
							oldData: this.contactList[i],
							phone: this.contactList[i].phone,
							name: this.contactList[i][this.formatName]
						});
					}
				}
				console.log(searchData)
				return searchData.map((n, i) => {
					n.index = i
					n.checked = false
					return n
				})
			}
		},
		watch: {
			obtainTels(newData) {
				this.updateData(newData);
			}
		},
		methods: {
			/**
			 * 监听输入框的值
			 */
			keyInput(value) {
				this.serachValue = value;
				this.contactData = [...this.searchDatas]
			},
			/**
			 * 初始化
			 */
			updateData(data) {
				if (data?.length) {
					this.contactList = data;
					this.initializationTel();
					this.buildTelindexs();
				}
			},
			/**
			 * @desc 初始化联系人数据
			 * @return undefind
			 */
			initializationTel() {
				this.handleTels = [];
				const telLetterArr = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
					'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '#'
				];
				for (let index = 0; index < telLetterArr.length; index++) {
					this.handleTels.push({
						name: telLetterArr[index],
						isCity: false, // 用于区分是否含有当前字母开头的城市
						citys: [], // 存放城市首字母含是此字母的数组
						forName: 'city-letter-' + (telLetterArr[index] == '#' ? '0' : telLetterArr[
							index]) //label的绑定
					});
				}
			},
			/**
			 * @desc 构建联系人索引
			 */
			buildTelindexs() {
				this.telIndexs = [];
				for (let i = 0; i < this.contactList.length; i++) {
					// 获取首字母
					const cityLetter = this.getLetter(this.contactList[i][this.formatName]).firstletter;
					// 获取当前城市首字母的unicode，用作后续排序
					const unicode = this.getLetter(this.contactList[i][this.formatName]).unicode;

					const index = this.cityIndexPosition(cityLetter);
					if (index === undefined) {
						continue;
					}
					if (this.telIndexs.indexOf(cityLetter) === -1) {
						this.handleTels[index].isCity = true;
						this.telIndexs.push(cityLetter);
					}

					this.handleTels[index].citys.push({
						cityName: this.contactList[i][this.formatName],
						phone: this.contactList[i].phone,
						index: i,
						unicode: unicode,
						oldData: this.contactList[i],
						checked: false,
					});
				}
				this.handleTels.forEach(n => {
					this.contactData = [...this.contactData, ...n.citys]
				})
			},
			/**
			 * @desc 得到城市的首字母
			 * @param str String
			 */
			getLetter(str) {
				return citySelect.getFirstLetter(str[0]);
			},
			/**
			 * @desc 获取城市首字母的unicode
			 * @param letter String 城市索引
			 */
			cityIndexPosition(letter) {
				if (!letter) {
					return undefined;
				}
				const ACode = 65;
				return letter === '#' ? 26 : letter.charCodeAt(0) - ACode;
			},
			/** @desc 联系人列表点击事件
			 *  @param Object
			 */
			contactTrigger(item) {
				if (!this.isSingle) return
				// 传值到父组件
				this.$emit('click', item.oldData ? [item.oldData] : [item]);
			},
			/**
			 * @desc 滑动到城市索引所在的地方
			 * @param id String 城市索引
			 */
			telIndex(id) {
				this.forName = id
				this.toView = id;
			},
			/**
			 * @desc 选择全部
			 */
			selectAll() {
				if (this.checkbox_all) {
					this.selectData = []
					this.contactData.map(n => {
						n.checked = false
						return n
					})
				} else {
					this.contactData.map(n => {
						n.checked = true
						return n
					})
					this.selectData = this.contactData.map(n => {
						return n.oldData
					})
				}
				this.checkbox_all = !this.checkbox_all
				this.contactData = [...this.contactData]
				this.$forceUpdate()
			},
			/**
			 * @desc 勾选回调
			 */
			checkboxChange(val) {
				this.selectData = []
				// this.selectData = val.detail.value
				this.contactData.map(n => {
					n.checked = false
					return n
				})
				val.detail.value.forEach(n => {
					const item = this.contactData.find(item => {
						return item.phone === n
					})
					this.$set(item, 'checked', true)
					let oldData = {
						"name":item.name,
						"phone":item.phone
					}
					this.selectData.push(oldData)
				})
				this.contactData = [...this.contactData]
			},
			handleComplete() {
				this.$emit('click', this.selectData);
			}
		},
	}
</script>

<style lang="scss" scoped>
	::v-deep {
		.uni-checkbox-input {
			border-radius: 2600px !important;
			margin-right: 20rpx;
		}

		.uni-checkbox-input-checked {
			color: #fff !important;
			background-color: #FD913B !important;
			border: none;
		}

		.uni-checkbox-input {
			// border: none;
		}
	}

	// .active {
	// 	color: #FFFFFF !important;
	// 	background-color: #FD913B;
	// }

	.round {
		border-radius: 2600px !important;
	}

	.ui-box {
		position: relative;
		width: 100vw;
		height: 100%;
	}

	.ui-search {
		margin-top: 50rpx;
		box-sizing: border-box;
		width: 100%;
		padding: 50rpx 32rpx 32rpx 32rpx;

		.search-input {
			background: #F2F2F2;
			border: 1px solid rgba(0, 0, 0, 0.1);
			border-radius: 36px;
		}
	}

	.ui-select-main {
		position: relative;
		width: 100%;
		// height: calc(100vh - 600rpx);
		margin-bottom: 30rpx;
		// background: #f6f5fa;

		// border-radius: 20rpx;
		.tel-list {
			.select-row {
				width: 100%;
				font-size: 14px;
				background: #fff;

				.citys-item-letter {
					height: 60rpx;
					line-height: 60rpx;
					background: #F2F2F2;
					padding-left: 32rpx;
					// margin-left: vww(-18);
					// padding-left: vww(18);
					// margin-top: -1px;
					// width: 100vw;
					// line-height: vww(30);
					// color: #9b9b9b;
					// background: #f6f5fa;
					// border-top: none;
				}

				.citys-item {
					height: 100rpx;
					line-height: 100rpx;
					background: #FFFFFF;
					padding-left: 80rpx;
					border-bottom: 1px solid #ebebf0;
					font-size: 32rpx;
					color: #353535;

					&:last-child {
						border: none;
					}
				}
			}
		}
	}

	.tel-indexs-view {
		position: absolute;
		right: 0;
		top: 0;
		z-index: 9;
		display: flex;
		width: 50rpx;
		height: 100%;
		text-align: center;

		.tel-indexs {
			width: 50rpx;
			text-align: center;
			vertical-align: middle;
			align-self: center;

			.tel-indexs-text {
				border-radius: 100rpx;
				margin-bottom: 10rpx;
				width: 40rpx;
				height: 40rpx;
				line-height: 40rpx;
				font-size: 24rpx;
				color: #353535;

				&:last-child {
					margin-bottom: 0;
				}
			}
		}
	}

	.ui-div {
		height: 120rpx;
	}

	.ui-footer {
		position: fixed;
		z-index: 5;
		width: 100%;
		bottom: 0;
		left: 0;
		background-color: #ffffff;

		.footer-box {
			padding: 0 32rpx;
			display: flex;
			justify-content: space-between;
			align-items: center;
			height: 98rpx;

			.select-btn {
				border: 2px solid rgba(229, 229, 229, 1);
				background-color: #fff;
				color: #333 !important;
			}

			button {
				height: 60rpx;
				width: 148rpx;
				line-height: 55rpx;
				font-size: 28rpx;
			}

			text {
				font-size: 32rpx;
				color: #888888;
				font-weight: 600;
			}
		}
	}
</style>