import request from '../../utils/request';
const {method, headers, api} = require('./api.json');
const {userManage, serverManage} = api;
import {setSID} from '../';

// ----------用户管理---------- //
/**
 * 获取用户
 * @param body
 * @returns {Object}
 */
export async function getUsers(body) {
  return request(userManage[0], {
    method: method[1],
    headers: setSID(headers[0]),
    body: JSON.stringify(body)
  });
}

/**
 * 获取单个用户
 * @param body
 * @returns {Object}
 */
export async function getUser(body) {
  return request(userManage[1], {
    method: method[1],
    headers: setSID(headers[0]),
    body: JSON.stringify(body)
  });
}

/**
 * 修改用户密码
 * @param body
 * @returns {Object}
 */
export async function updateUserPwd(body) {
  return request(userManage[2], {
    method: method[1],
    headers: setSID(headers[0]),
    body: JSON.stringify(body)
  });
}

/**
 * 删除用户
 * @param body
 * @returns {Object}
 */
export async function delUser(body) {
  return request(userManage[3], {
    method: method[1],
    headers: setSID(headers[0]),
    body: JSON.stringify(body)
  });
}

/**
 * 修改用户
 * @param body
 * @returns {Object}
 */
export async function updateUser(body) {
  return request(userManage[4], {
    method: method[1],
    headers: setSID(headers[0]),
    body: JSON.stringify(body)
  });
}

// ----------服务管理---------- //
/**
 * 获取行业
 * @param body
 * @returns {Object}
 */
export async function getIndustries(body) {
  return request(serverManage[5], {
    method: method[1],
    headers: setSID(headers[0]),
    body: JSON.stringify(body)
  });
}
