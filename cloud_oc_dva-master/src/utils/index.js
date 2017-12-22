/*
  DES加密
*/
export const encryptByDES = (message, key) => {
  const CryptoJS = require("crypto-js");
  const keyHex = CryptoJS.enc.Utf8.parse(key);
  const encrypted = CryptoJS.DES.encrypt(message, keyHex, {
    mode: CryptoJS.mode.ECB,
    padding: CryptoJS.pad.Pkcs7
  });

  return encrypted.toString();
}

export const setCookie = (name, value, time) => {
  let days = time || 7;   // 默认保存7天
  let exp = new Date();

  exp.setTime(exp.getTime() + days * 24 * 60 * 60 * 1000);
  document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString();
}

export const getCookie = (name) => {
  let arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)")

  if (arr = document.cookie.match(reg))
    return unescape(arr[2]);
  else
    return null;
}

export const reload = () => {
  location.reload();
}

export const substring = (str, len) => {
  if (str.length > len) {
    let _str = str.substring(0, len);
    return `${_str}...`;
  }
  return str;
}

export const randomStr = (num) => {
  return Math.random().toString(36).substr(6);
}

export const getTimestamp = () => {
  return Date.parse(new Date());
}

export const withKey = (datas, dataIndex) => {
  if (typeof datas[0] == 'string') {
    return datas.map((d, i) => {
      let _obj = {};
      _obj.key = getTimestamp() + i;
      _obj[dataIndex] = d;
      return _obj;
    });
  } else {
    return datas.map((d, i) => {
      d.key = getTimestamp() + i;
      return d;
    });
  }
}

/**
 * 限制文本长度
 * @param txt
 * @param len
 */
export const limitLen = (txt, len) => {
  return substring(txt, len);
}

Date.prototype.Format = function (fmt) {
  let o = {
    "M+": this.getMonth() + 1, //月份
    "d+": this.getDate(), //日
    "h+": this.getHours(), //小时
    "m+": this.getMinutes(), //分
    "s+": this.getSeconds(), //秒
    "q+": Math.floor((this.getMonth() + 3) / 3), //季度
    "S": this.getMilliseconds() //毫秒
  };
  if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
  for (let k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
  return fmt;
};

String.prototype.trim = function () {
  return this.replace(/(^\s*)|(\s*$)/g, '');
};
