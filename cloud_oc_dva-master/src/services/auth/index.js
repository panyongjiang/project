import request from '../../utils/request';
const {method, headers, api} = require('./api.json');
import {setSID} from '../';

/**
 * 获取desKey
 * @returns {Object}
 */
export async function getDesKey() {
  return request(api[0], {
    method: method[1],
    headers: setSID(headers[0])
  });
}

/**
 * 登录
 * @param body
 * @returns {Object}
 */
export async function login(body) {
  return request(api[1], {
    method: method[1],
    headers: headers[0],
    body: JSON.stringify(body)
  });
}

/**
 * 登出
 * @returns {Object}
 */
export async function logout() {
  return request(api[2], {
    method: method[1],
    headers: setSID(headers[0])
  });
}

/**
 * 修改密码
 * @param body
 * @returns {Object}
 */
export async function resetPwd(body) {
  return request(api[3], {
    method: method[1],
    headers: setSID(headers[0]),
    body: JSON.stringify(body)
  });
}
