import {resetPwd, getWords, addWord, delWord} from '../services/system';

import {getCookie, withKey} from '../utils';
const {UID_KEY, SID_KEY} = require('../config/cookies.json');

import errCode from '../utils/errcode';

export default {
  namespace: 'sys',
  state: {
    // 密码修改
    title: '密码修改',
    visible: false,
    oldPwd: '',
    newPwd: '',
    reNewPwd: '',
    // 敏感词
    swDataSource: [],
    swLoading: true,
    word: ''
  },
  subscriptions: {
    setup({ dispatch, history }) {
      history.listen((path) => {
        console.log('SysConfig subscriptions path', path);
        (path.pathname == '/sensitive_word' && getCookie(SID_KEY))
        && dispatch({type: 'getWords'});
      });
    }
  },
  effects: {
    *resetPwd({payload}, {call, put}) {
      let res = yield call(() => {
        payload.user_id = getCookie(UID_KEY);
        return resetPwd(payload);
      });

      errCode(res.data, true) && (yield put({type: 'setSysParams', payload: {visible: false}}));
      yield put({type: 'clearPwd'});
    },
    *getWords(action, {call, put}) {
      let res = yield call(() => {
        return getWords();
      });
      const {data} = res;

      if (errCode(data)) {
        const swDataSource = withKey(data.data, 'word');
        yield put({type: 'setSysParams', payload: {swDataSource, swLoading: false}});
      }
    },
    *addWord({payload}, {call, put}) {
      let res = yield call(() => {
        return addWord(payload);
      });
      const {data} = res;

      if (errCode(data, true)) {
        yield put({type: 'getWords'});
        yield put({type: 'setSysParams', payload: {word: ''}});
      }
    },
    *delWord({payload}, {call, put}) {
      let res = yield call(() => {
        return delWord(payload);
      });
      const {data} = res;

      errCode(data, true) && (yield put({type: 'getWords'}));
    }
  },
  reducers: {
    setSysParams(state, {payload}) {
      Object.assign(state, payload);
      return {
        ...state
      };
    },
    clearPwd(state) {
      Object.assign(state, {oldPwd: '', newPwd: '', reNewPwd: ''});
      return {
        ...state
      };
    }
  }
}
