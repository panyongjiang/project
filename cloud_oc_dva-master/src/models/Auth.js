import {login, getDesKey, logout, resetPwd} from '../services/auth';
import {setCookie, getCookie, getTimestamp, reload} from '../utils';
import errCode from '../utils/errcode';

const {SID_KEY, UID_KEY, UNAME_KEY, REMEMBER_KEY} = require('../config/cookies.json');
const remember = getCookie(REMEMBER_KEY);
const userName = getCookie(UNAME_KEY);

export default {
  namespace: 'auth',
  state: {
    // 加密所需
    desKey: '',
    repeatTime: '',
    // 登录信息
    sid: '',
    userId: '',
    userName: '',
    password: '',
    verifyCode: '',
    remember: 0,
    timestamp: getTimestamp(),
    // 公共
    loading: false
  },
  subscriptions: {
    setup({dispatch}) {
      if (!getCookie(SID_KEY)) {
        // 记住我
        remember == 1 && (() => {
          dispatch({
            type: 'setLoginParam',
            payload: {remember: parseInt(remember), userName: userName || ''}
          });
        })();
        // 获取desKey
        dispatch({type: 'getDesKey'});
      } else {
        dispatch({type: 'main/getMenus', payload: {id: '1'}});
      }
    }
  },
  effects: {
    *getDesKey(action, { call, put }) {
      const res = yield call(() => {
        return getDesKey();
      });
      const {desKey, repeat_times} = res.data.data;

      yield put({
        type: 'setLoginParam', payload: {
          desKey: desKey, repeatTime: repeat_times
        }
      });
    },
    *login({payload}, { call, put }) {
      // 离线测试
      //setCookie(SID_KEY, 123456);
      //setCookie(UNAME_KEY, 'admin');

      let res = yield call(() => {
        return login(payload);
      });

      errCode(res.data) ?
      (yield put({type: 'setLoginInfo', payload: res.data})) :
      (yield put({
        type: 'setLoginParam', payload: {
          password: '', timestamp: getTimestamp(), loading: false
        }
      }))
    },
    *logout(action, {call, put}) {
      let res = yield call(() => {
        return logout();
      });
      errCode(res.data) && (() => {
        setCookie(SID_KEY, '', -1);
        setTimeout(reload, 1000);
      })();
    }
  },
  reducers: {
    setLoginParam(state, {payload}) {
      Object.assign(state, payload);
      return {
        ...state
      };
    },
    setLoginInfo(state, {payload}){
      const {remember} = state;
      const {sid, user_id, userName} = payload.data;

      setCookie(SID_KEY, sid);
      setCookie(UID_KEY, user_id);
      setCookie(UNAME_KEY, userName);

      return {
        ...state,
        sid: sid,
        userId: user_id,
        userName: userName
      }
    }
  }
}
