/*
 * @Author: white_queen
 * @Date: 2023-03-14 10:35:52
 * @FilePath: /common/mixin/map.mixin.js
 * @Description: 地图公共处理
 */

export const mapMixin = {
	methods: {
		loadMap(callBack) {
			const script = document.createElement('script');
			script.src =
				'http://webapi.amap.com/maps?v=1.3&key=3a2d950c2fc2774b4b1ee6da9a8d93dc&plugin=AMap.PolyEditor&callback=mapInit'
			window.mapInit = () => { // 依靠脚本的回调执行初始化
				callBack();
			}
			script.onerror = (error) => {
				this.$apm({
					name: '地图脚本加载错误',
					error
				});
			}
			document.head.appendChild(script);
		}
	}
}
