import {Pagination} from '../components/Pagination';

// 用户管理
import {getUsers, getUser, updateUserPwd,
  delUser, updateUser, getIndustries} from '../services/product';

import {getCookie, withKey, limitLen} from '../utils';
const {SID_KEY} = require('../config/cookies.json');
const SID = getCookie(SID_KEY);

import errCode from '../utils/errcode';

export default {
  namespace: 'product',
  state: {
    sid: SID,
    // 用户管理
    umDataSource: [],
    umLoading: true,
    umFormData: {
      //user_id: '',
      //industryName: '',
      //business: '',
      //company: '',
      //position: '',
      //url: '',
      //province: '',
      //provinceName: '',
      //city: '',
      //cityName: '',
      //address: '',
      //phone: '',
      //person: '',
      //email: ''
    },
    umModalTitle: '',
    umModalVisible: false,
    umModalConfirmLoading: false,
    keyword: '',
    // 服务管理
    industries: []
  },
  subscriptions: {
    setup({dispatch, history}) {
      history.listen((path) => {
        SID && (() => {
          Pagination.dispatch = dispatch;
          Pagination.payload = {};
          Pagination.current = 1;
          Pagination.pageSize = 10;

          const {pathname} = path;
          const {current, pageSize} = Pagination;

          if (pathname == '/user_manage') {
            Pagination.dispatchType = 'getUsers';
            dispatch({type: 'setProductParams', payload: {keyword: ''}});
            dispatch({type: 'getUsers', payload: {page_num: current, page_size: pageSize}});
          }
        })();
      });
    }
  },
  effects: {
    // ----------用户管理---------- //
    *getUsers({payload}, {call, put}) {
      console.log('*getUsers payload', payload);
      payload = payload || {};
      payload.user_name != undefined && (Pagination.params.user_name = payload.user_name);

      yield put({type: 'setProductParams', payload: {umLoading: true}});

      const res = yield call(() => {
        payload.page_num || (payload.page_num = Pagination.current);
        payload.page_size || (payload.page_size = Pagination.pageSize);
        (payload.user_name != undefined && payload.user_name.trim().length == 0) && (delete payload.user_name);
        return getUsers(payload);
      });
      const {data} = res;

      if (errCode(data)) {
        const {details, totalNum} = data.data;
        const umDataSource = withKey(details);

        Pagination.total = totalNum;
        yield put({type: 'setProductParams', payload: {umDataSource, umLoading: false}});
      }
    },
    *getUser({payload}, {call, put}) {
      console.log('*getUser payload', payload)
      // 行业参数
      yield put({type: 'getIndustries'});
      // 其它参数
      const res = yield call(() => {
        return getUser(payload);
      });
      const {data} = res;

      if (errCode(data)) {
        yield put({type: 'main/setMainParams', payload: {spinning: false}});
        yield put({
          type: 'setProductParams', payload: {
            umModalTitle: '服务管理',
            umFormData: Object.assign(data.data.user, {user_id: payload.userId}),
            umModalVisible: true
          }
        });
      }
    },
    *updateUserPwd({payload}, {call, put}) {
      console.log('*updateUserPwd payload', payload)
      const res = yield call(() => {
        return updateUserPwd(payload);
      });
      const {data} = res;

      if (errCode(data, true)) {
        yield put({
          type: 'setProductParams', payload: {
            umModalVisible: false,
            umModalConfirmLoading: false
          }
        });
        yield put({type: 'getUsers'});
      }
    },
    *delUser({payload}, {call, put}) {
      console.log('*delUser payload', payload);
      const res = yield call(() => {
        return delUser(payload);
      });
      const {data} = res;

      errCode(data, true) && (yield put({type: 'getUsers'}));
    },
    *updateUser({payload}, {call, put}) {
      console.log('*updateUser payload', payload);
      const res = yield call(() => {
        return updateUser(payload);
      });
      const {data} = res;

      if (errCode(data, true)) {
        yield put({type: 'setProductParams', payload: {umModalVisible: false}});
        yield put({type: 'getUsers'});
      }
    },
    // ----------服务管理---------- //
    *getIndustries({payload}, {call, put}) {
      console.log('*getIndustries payload', payload);
      payload = payload || {};

      const res = yield call(() => {
        return getIndustries(payload);
      });
      const {data} = res;

      errCode(data) && (yield put({type: 'setProductParams', payload: {industries: data.data}}));
    },
    *getIndustries({payload}, {call, put}) {
      console.log('*getIndustries payload', payload);
      payload = payload || {};

      const res = yield call(() => {
        return getIndustries(payload);
      });
      const {data} = res;

      errCode(data) && (yield put({type: 'setProductParams', payload: {industries: data.data}}));
    }
  },
  reducers: {
    setProductParams(state, {payload}) {
      Object.assign(state, payload);
      return {
        ...state
      }
    },
    setFormData(state, {payload}) {  // 表单元素赋值（支持多元素同时赋值）
      const {fd, ds} = payload;
      let formData = state[fd];
      let ret = {
        ...state
      };

      if (ds) {
        ds.map((d) => {
          const {key, value} = d;
          formData[key] = value;
        });
      } else {
        const {key, value} = payload;
        formData[key] = value;
      }

      ret[state[fd]] = formData;

      return ret;
    }
  }
}
