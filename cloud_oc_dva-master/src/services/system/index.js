import request from '../../utils/request';
const {method, headers, api} = require('./api.json');

import {setSID} from '../';

/**
 * 修改密码
 * @param body
 * @returns {Object}
 */
export async function resetPwd(body) {
  return request(api[0], {
    method: method[1],
    headers: setSID(headers[0]),
    body: JSON.stringify(body)
  });
}

/**
 * 获取敏感词集合
 * @returns {Object}
 */
export async function getWords() {
  return request(api[1], {
    method: method[1],
    headers: setSID(headers[0])
  });
}

/**
 * 增加敏感词
 * @param body
 * @returns {Object}
 */
export async function addWord(body) {
  return request(api[2], {
    method: method[1],
    headers: setSID(headers[0]),
    body: JSON.stringify(body)
  });
}

/**
 * 删除敏感词
 * @param body
 * @returns {Object}
 */
export async function delWord (body) {
  return request(api[3], {
    method: method[1],
    headers: setSID(headers[0]),
    body: JSON.stringify(body)
  });
}
