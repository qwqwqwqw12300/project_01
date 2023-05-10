/*
 * @Date: 2023-05-10 16:19:38
 * @LastEditTime: 2023-05-10 16:19:38
 * @FilePath: /common/utils/toBufUttils.js
 * @Description: buffer数据操作
 */
export function inArray(arr, key, val) {
	for (let i = 0; i < arr.length; i++) {
		if (arr[i][key] === val) {
			return i;
		}
	}
	return -1;
}

// ArrayBuffer转16进度字符串示例
export function ab2hex(buffer) {
	var hexArr = Array.prototype.map.call(new Uint8Array(buffer), function(bit) {
		return ('00' + bit.toString(16)).slice(-2)
	})
	return hexArr.join('');
}


export function hexToBuf(hex) {
	var typedArray = new Uint8Array(hex.match(/[\da-f]{2}/gi).map(function(h) {
		return parseInt(h, 16)
	}));

	return typedArray.buffer;
}

export function hexToString(hex) {

	var arr = hex.split("")
	var out = ""
	for (var i = 0; i < arr.length / 2; i++) {
		var tmp = "0x" + arr[i * 2] + arr[i * 2 + 1]
		var charValue = String.fromCharCode(tmp);
		out += charValue
	}
	return out
}

export function buf2hex(arrayBuffer) {
	return Array.prototype.map.call(new Uint8Array(arrayBuffer), x => ('00' + x.toString(16)).slice(-2)).join('');
}

export function hexToStr(hex, encoding) {
	var trimedStr = hex.trim();
	var rawStr = trimedStr.substr(0, 2).toLowerCase() === "0x" ? trimedStr.substr(2) : trimedStr;
	var len = rawStr.length;
	if (len % 2 !== 0) {
		alert("Illegal Format ASCII Code!");
		return "";
	}
	var curCharCode;
	var resultStr = [];
	for (var i = 0; i < len; i = i + 2) {
		curCharCode = parseInt(rawStr.substr(i, 2), 16);
		resultStr.push(curCharCode);
	}
	// encoding为空时默认为utf-8
	var bytesView = new Uint8Array(resultStr);
	var str = new TextDecoder(encoding).decode(bytesView);
	return str;
}


export function stringToHex(str, joinFlag) {
	var arr = [];
	for (var i = 0; i < str.length; i++) {
		arr[i] = (str.charCodeAt(i).toString(16));
	}
	return arr.join(joinFlag);
}

//ASCII码转16进制
export function strToHexCharCode(str) {
	if (str === "") {
		return "";
	} else {
		var hexCharCode = [];
		hexCharCode.push("0x");
		for (var i = 0; i < str.length; i++) {
			hexCharCode.push((str.charCodeAt(i)).toString(16));
		}
		return hexCharCode.join("");
	}
}

// 字符串转byte
export function stringToBytes(str) {
	var array = new Uint8Array(str.length);
	for (var i = 0, l = str.length; i < l; i++) {
		array[i] = str.charCodeAt(i);
	}
	console.log(array);
	return array.buffer;
}

// 16进制字符串转ArrayBuffer
export function hex2ArrayBuffer(hex_str) {
	let typedArray = new Uint8Array(hex_str.match(/[\da-f]{2}/gi).map(function(h) {
		return parseInt(h, 16)
	}))
	let buffer = typedArray.buffer
	return buffer
}