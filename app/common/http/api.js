/*
 * @Author: zhanch
 * @Date: 2022-12-13 14:37:03
 * @LastEditors: zhanch
 * @FilePath: /http/api.js
 * @Description: 请求接口
 */

import http from './request.js';

export const getDemo = (data) => http.get('/demo/demo', data);
export const postDemo = (data) => http.post('/demo/demo', data);
