import {getCookie} from '../utils';
const {SID_KEY} = require('../config/cookies.json');
const SID = getCookie(SID_KEY);

/**
 * 请求头部增加sid
 * @param headers
 * @returns {*}
 */
export const setSID = (headers) => {
  headers.sid = SID;
  return headers;
}

/**
 * 表单参数处理
 * @param body
 * @returns {*}
 */
export const formData = (body) => {
  let form = new FormData();
  for (let i in body) form.append(i, body[i]);
  form.append('sid', SID);
  return form;
}
