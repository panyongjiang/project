import Message from '../components/Message';

/**
 * 登录校验
 * @param username
 * @param password
 * @returns {number}
 */
export const checkLogin = (username, password) => {
  let res = 1;

  if (username.trim().length == 0) {
    Message.warn('用户名不能为空');
    return 0;
  } else if (password.trim().length == 0) {
    Message.warn('密码不能为空');
    return 0;
  }

  return res;
}

/**
 * 密码校验
 * @param oldPwd
 * @param newPwd
 * @param reNewPwd
 */
export const checkPwd = (oldPwd, newPwd, reNewPwd) => {
  let res = 1;

  if (oldPwd.trim().length == 0) {
    Message.warn('原始密码不能为空');
    return 0;
  } else if (newPwd.trim().length == 0) {
    Message.warn('新密码不能为空');
    return 0;
  } else if (newPwd != reNewPwd) {
    Message.warn('两次输入的密码不同');
    return 0;
  }

  return res;
}
