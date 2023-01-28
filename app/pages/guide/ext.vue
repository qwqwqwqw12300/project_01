<template>
	<div>
		<button type="primary" @click="startPairing">连接</button>
	</div>
</template>

<script>
	// 获取 module 
	var sdkModule = uni.requireNativePlugin("VPairSDKModule")
	export default {
		onLoad() {
			plus.globalEvent.addEventListener('onPairFinish', function(e) {
				if (e.isSuccess) {
					sdkModule.logUni("onPairFinish收到：" + e.isSuccess + ",id为：" + e.deviceId);
				} else {
					sdkModule.logUni("onPairFinish收到：" + e.isSuccess);
				}
			});
			plus.globalEvent.addEventListener('onPairEvent', function(e) {
				sdkModule.logUni("onPairEvent收到：" + e.eventType);
			});
			plus.globalEvent.addEventListener('onWifiShouldSelect', function(e) {
				sdkModule.logUni("onWifiShouldSelect收到：" + e.wifiList);
				var ssid = e.wifiList[0].ssid;
				var bssid = e.wifiList[0].bssid;
				var rssi = e.wifiList[0].rssi;
				var password = "rreewwqq";
				sdkModule.connectWifi(ssid, bssid, rssi, password);
			});
		},
		methods: {
			// initSDK(){
			// 	sdkModule.initSDK();//初始化调用
			// },
			// checkPermission(){
			// 	var dasdsa=sdkModule.checkPermission();
			// 	modal.toast({
			// 		message: dasdsa,
			// 		duration: 1.5
			// 	});
			// 	},
			// requestPermission(){
			// 	sdkModule.requestPermission(1)//根据错误码请求功能
			// },
			// getWifiInfo(){
			// 	sdkModule.getWifiInfo(result =>{
			// 		// var _tvSsid=document.querySelector('#tvSsid');
			// 		// var _tvBssid=document.querySelector('#tvBssid');
			// 		var result=JSON.parse(rest);
			// 		_tvSsid.value=result.ssid;
			// 		_tvBssid.value=result.bssid;
			// 		modal.toast({
			// 			message: result.ssid,
			// 			duration: 1.5
			// 		});
			// 	});//获取当前连接的wifi的SSID和BSSID
			// },
			startPairing() {
				console.log(sdkModule, 'sdkModule');
				// var _tvSsid=document.querySelector('#tvSsid');
				// var _tvBssid=document.querySelector('#tvBssid');
				// var _tvPassword=document.querySelector('#tvPassword');
				// var ssid="alone";
				// var bssid="06:41:bb:9e:6d:47";
				// var password="rreewwqq";
				var userId = "6318455abe36e9a955066e1f";
				var token =
					"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoiNjMxODQ1NWFiZTM2ZTlhOTU1MDY2ZTFmIiwiZW1haWwiOiJlbGlua2VyQGVsaW5rZXIuY2MiLCJpYXQiOjE2NzQwMjg0ODQsImV4cCI6MTY3NDAyODQ4NH0.CclxmukhZ1X-Zb9l_yBb56aH35IsYlOCIt3J5-coGD8";
				sdkModule.startPairing(userId, token);
			}
		}
	}
</script>
