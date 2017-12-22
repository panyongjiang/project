import request from '../../utils/request';
const {method, headers, api} = require('./api.json');
const {trailApplication, issueFeedBack, articleManage,
  appCheck, inviteCode} = api;
import {setSID, formData} from '../';

// ----------试用申请---------- //
/**
 * 获取试用申请
 * @param body
 * @returns {Object}
 */
export async function getTrials(body) {
  return request(trailApplication[0], {
    method: method[1],
    headers: setSID(headers[0]),
    body: JSON.stringify(body)
  });
}

/**
 * 删除试用申请
 * @param body
 * @returns {Object}
 */
export async function delTrial(body) {
  return request(trailApplication[1], {
    method: method[1],
    headers: setSID(headers[0]),
    body: JSON.stringify(body)
  });
}

/**
 * 获取单个试用申请
 * @param body
 * @returns {Object}
 */
export async function getTrial(body) {
  return request(trailApplication[2], {
    method: method[1],
    headers: setSID(headers[0]),
    body: JSON.stringify(body)
  });
}

/**
 * 修改试用申请状态
 * @param body
 * @returns {Object}
 */
export async function updateTrialStatus(body) {
  return request(trailApplication[3], {
    method: method[1],
    headers: setSID(headers[0]),
    body: JSON.stringify(body)
  });
}

// ----------问题反馈---------- //
/**
 * 获取反馈
 * @param body
 * @returns {Object}
 */
export async function getFeedBacks(body) {
  return request(issueFeedBack[0], {
    method: method[1],
    headers: setSID(headers[0]),
    body: JSON.stringify(body)
  });
}

/**
 * 获取单个反馈
 * @param body
 * @returns {Object}
 */
export async function getFeedBack(body) {
  return request(issueFeedBack[1], {
    method: method[1],
    headers: setSID(headers[0]),
    body: JSON.stringify(body)
  });
}

/**
 * 删除反馈
 * @param body
 * @returns {Object}
 */
export async function delFeedBack(body) {
  return request(issueFeedBack[2], {
    method: method[1],
    headers: setSID(headers[0]),
    body: JSON.stringify(body)
  });
}

/**
 * 修改状态
 * @param body
 * @returns {Object}
 */
export async function updateFeedBackStatus(body) {
  return request(issueFeedBack[3], {
    method: method[1],
    headers: setSID(headers[0]),
    body: JSON.stringify(body)
  });
}


// ----------文章管理---------- //
/**
 * 获取文章
 * @param body
 * @returns {Object}
 */
export async function getArticles(body) {
  return request(articleManage[0], {
    method: method[1],
    headers: setSID(headers[0]),
    body: JSON.stringify(body)
  });
}

/**
 * 获取文章类型
 * @returns {Object}
 */
export async function getArticleTypes() {
  return request(articleManage[1], {
    method: method[1],
    headers: setSID(headers[0])
  });
}

/**
 * 增加、编辑文章
 * @param body
 * @param type
 * @returns {Object}
 */
export async function handleArticle(body, type) {
  return request(articleManage[(type ? 3 : 2)], {
    method: method[1],
    body: formData(body)
  });
}

/**
 * 获取单个文章
 * @param body
 * @returns {Object}
 */
export async function getArticle(body) {
  return request(articleManage[4], {
    method: method[1],
    headers: setSID(headers[0]),
    body: JSON.stringify(body)
  });
}

/**
 * 删除文章
 * @param body
 * @returns {Object}
 */
export async function delArticle(body) {
  return request(articleManage[5], {
    method: method[1],
    headers: setSID(headers[0]),
    body: JSON.stringify(body)
  });
}

/**
 * 发布文章
 * @param body
 * @returns {Object}
 */
export async function publishArticle(body) {
  return request(articleManage[6], {
    method: method[1],
    headers: setSID(headers[0]),
    body: JSON.stringify(body)
  });
}

// ----------APP安全检测---------- //
/**
 * 获取需要检测的APP
 * @param body
 * @returns {Object}
 */
export async function getApps(body) {
  return request(appCheck[0], {
    method: method[1],
    headers: setSID(headers[0]),
    body: JSON.stringify(body)
  });
}

// ----------邀请码管理---------- //
/**
 * 获取邀请码
 * @param body
 * @returns {Object}
 */
export async function getInviteCodes(body) {
  return request(inviteCode[0], {
    method: method[1],
    headers: setSID(headers[0]),
    body: JSON.stringify(body)
  });
}

/**
 * 生成邀请码
 * @returns {Object}
 */
export async function addInviteCode() {
  return request(inviteCode[1], {
    method: method[1],
    headers: setSID(headers[0])
  });
}
