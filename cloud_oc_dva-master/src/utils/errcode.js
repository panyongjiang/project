import Message from '../components/Message';
import {setCookie, reload} from '../utils';
const {SID_KEY, UID_KEY} = require('../config/cookies.json');

const ERR_CODE = require('../config/errcode.json');

export default function errCode(res, isshow = false){
  const {code, msg} = res;
  let ret = 0;

  if (code == ERR_CODE.SUCCESS) {
    isshow && Message.success(msg);
    ret = 1;
  } else if (code == ERR_CODE.RE_LOGIN) {
    Message.error(msg);
    setCookie(SID_KEY, '', -1);
    setCookie(UID_KEY, '', -1);
    setTimeout(reload, 1000);
  } else {
    Message.error(msg);
    ret = 0;
  }

  return ret;
}
