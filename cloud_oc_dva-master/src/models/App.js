import {Pagination} from '../components/Pagination';

// 试用申请
import {getTrials, delTrial, getTrial,
  updateTrialStatus} from '../services/application';
// 问题反馈
import {getFeedBacks, getFeedBack, delFeedBack,
  updateFeedBackStatus} from '../services/application';
// 文章管理
import {getArticles, getArticleTypes, handleArticle,
  getArticle, delArticle, publishArticle} from '../services/application';
// APP安全检测
import {getApps, uploadAppReport} from '../services/application';
// 邀请码
import {getInviteCodes, addInviteCode} from '../services/application';

import {getCookie, withKey, limitLen} from '../utils';
const {SID_KEY} = require('../config/cookies.json');
const SID = getCookie(SID_KEY);

import {server, imgServer} from '../utils/url';
const {api} = require('../services/application/api.json');

import errCode from '../utils/errcode';

const LIMIT_LEN = 15;

export default {
  namespace: 'app',
  state: {
    sid: SID,
    // 试用申请
    taDataSource: [],
    taLoading: true,
    taModalVisible: false,
    taModalTitle: '试用申请详情',
    taModalConfirmLoading: false,
    taFormData: {
      id: null,
      address: '',
      city: '',
      company: '',
      createTime: '',
      email: '',
      trial1: '',
      person: '',
      phone: '',
      position: '',
      province: '',
      url: '',
      trialStatue: 0,
      user_id: null,
      user_name: ''
    },
    // 问题反馈
    fbDataSource: [],
    fbLoading: true,
    fbModalVisible: false,
    fbModalTitle: '问题反馈详情',
    fbModalConfirmLoading: false,
    fbFormData: {
      id: null,
      content: '',
      createTime: '',
      phone: '',
      status: 0
    },
    // 文章管理
    amDataSource: [],
    amLoading: true,
    where: 0, // 区分category值取自哪里  0：表头 1：模态
    categories: [],
    category: '-1',
    modalVisible: false,
    modalTitle: '新增文章',
    modalHandle: 0,  // 操作类型 0：新增 1：编辑
    modalType: 0, // 模态类型 0：编辑 1：预览
    modalConfirmLoading: false,
    formData: {
      id: '',
      title: '',
      sub_title: '',
      pic_id: null,
      pic_file: null,
      content: '',
      category_id: null,
      remark: '',
      description: '',
      author: ''
    },
    fileList: [],
    kEditor: null,   // 文本编辑器对象
    // APP安全检测
    acDataSource: [],
    acLoading: true,
    uploadURL: `${server}${api.appCheck[1]}`,
    downloadURL: `${server}${api.appCheck[2]}`,
    // 邀请码
    icDataSource: [],
    icLoading: true
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

          if (pathname == '/invite_code') {
            Pagination.dispatchType = 'getInviteCodes';
            dispatch({
              type: 'getInviteCodes', payload: {
                page_num: current, page_size: pageSize
              }
            });
          } else if (pathname == '/app_check') {
            Pagination.dispatchType = 'getApps';
            dispatch({
              type: 'getApps', payload: {
                page_num: current, page_size: pageSize
              }
            });
          } else if (pathname == '/article_manage') {
            dispatch({type: 'getArticleTypes'});
            Pagination.dispatchType = 'getArticles';
            dispatch({
              type: 'getArticles', payload: {
                page_num: current, page_size: pageSize
              }
            });
          } else if (pathname == '/issue_feedback') {
            Pagination.dispatchType = 'getFeedBacks';
            dispatch({
              type: 'getFeedBacks', payload: {
                page_num: current, page_size: pageSize
              }
            });
          } else if (pathname == '/trial_application') {
            Pagination.dispatchType = 'getTrials';
            dispatch({
              type: 'getTrials', payload: {
                page_num: current, page_size: pageSize
              }
            });
          }
        })();
      });
    }
  },
  effects: {
    // ----------试用申请---------- //
    *getTrials({payload}, { call, put }) {
      console.log('*getTrials payload', payload);
      payload = payload || {};
      yield put({type: 'setAppParams', payload: {taLoading: true}});

      const res = yield call(() => {
        payload.page_num || (payload.page_num = Pagination.current);
        payload.page_size || (payload.page_size = Pagination.pageSize);
        return getTrials(payload);
      });
      const {data} = res;

      if (errCode(data)) {
        const {details, total_num} = data.data;
        const taDataSource = withKey(details);

        Pagination.total = total_num;
        yield put({type: 'setAppParams', payload: {taDataSource, taLoading: false}});
      }
    },
    *getTrial({payload}, { call, put }) {
      console.log('*getTrial payload', payload)
      const res = yield call(() => {
        return getTrial(payload);
      });
      const {data} = res;

      if (errCode(data)) {
        yield put({type: 'main/setMainParams', payload: {spinning: false}});
        yield put({
          type: 'setAppParams', payload: {
            taFormData: Object.assign({}, data.data),
            taModalVisible: true
          }
        });
      }
    },
    *updateTrialStatus({payload}, { call, put }) {
      console.log('*updateTrialStatus payload', payload)
      const res = yield call(() => {
        return updateTrialStatus(payload);
      });
      const {data} = res;

      if (errCode(data, true)) {
        yield put({
          type: 'setAppParams', payload: {
            taModalVisible: false,
            taModalConfirmLoading: false
          }
        });
        yield put({type: 'getTrials'});
      }
    },
    *delTrial({payload}, { call, put }) {
      console.log('*delTrial payload', payload);
      const res = yield call(() => {
        return delTrial(payload);
      });
      const {data} = res;

      errCode(data, true) && (yield put({type: 'getTrials'}));
    },
    // ----------问题反馈---------- //
    *getFeedBacks({payload}, { call, put }) {
      console.log('*getFeedBacks payload', payload);
      payload = payload || {};
      yield put({type: 'setAppParams', payload: {fbLoading: true}});

      const res = yield call(() => {
        payload.page_num || (payload.page_num = Pagination.current);
        payload.page_size || (payload.page_size = Pagination.pageSize);
        return getFeedBacks(payload);
      });
      const {data} = res;

      if (errCode(data)) {
        const {details, totalNum} = data.data;
        const fbDataSource = withKey(details);

        Pagination.total = totalNum;
        yield put({type: 'setAppParams', payload: {fbDataSource, fbLoading: false}});
      }
    },
    *getFeedBack({payload}, { call, put }) {
      console.log('*getFeedBack payload', payload)
      const res = yield call(() => {
        return getFeedBack(payload);
      });
      const {data} = res;

      if (errCode(data)) {
        yield put({type: 'main/setMainParams', payload: {spinning: false}});
        yield put({
          type: 'setAppParams', payload: {
            fbFormData: Object.assign({}, data.data),
            fbModalVisible: true
          }
        });
      }
    },
    *updateFeedBackStatus({payload}, { call, put }) {
      console.log('*updateFeedBackStatus payload', payload)
      const res = yield call(() => {
        return updateFeedBackStatus(payload);
      });
      const {data} = res;

      if (errCode(data, true)) {
        yield put({
          type: 'setAppParams', payload: {
            fbModalVisible: false,
            fbModalConfirmLoading: false
          }
        });
        yield put({type: 'getFeedBacks'});
      }
    },
    *delFeedBack({payload}, { call, put }) {
      console.log('*delFeedBack payload', payload);
      const res = yield call(() => {
        return delFeedBack(payload);
      });
      const {data} = res;

      errCode(data, true) && (yield put({type: 'getFeedBacks'}));
    },
    // ----------文章管理---------- //
    // 获取文章
    *getArticles({payload}, { call, put }) {
      console.log('*getArticles payload', payload)
      payload = payload || {};
      yield put({type: 'setAppParams', payload: {amLoading: true}});

      const res = yield call(() => {
        payload.page_num || (payload.page_num = Pagination.current);
        payload.page_size || (payload.page_size = Pagination.pageSize);
        if (payload.category_id && payload.category_id != -1) {
          payload.category_id = Number(payload.category_id);
        } else delete payload.category_id;
        return getArticles(payload);
      });
      const {data} = res;

      if (errCode(data)) {
        const {details, totalNum} = data.data;
        const amDataSource = withKey(details.map((d) => {
          d._title = limitLen(d.title, LIMIT_LEN);
          d._subTitle = limitLen(d.subTitle, LIMIT_LEN);
          return d;
        }));

        Pagination.total = totalNum;
        yield put({type: 'setAppParams', payload: {amDataSource, amLoading: false}});
      }
    },
    // 获取单个文章
    *getArticle({payload}, { call, put }) {
      console.log('*getArticle payload', payload)
      const {modalType} = payload;
      delete payload.modalType;

      const res = yield call(() => {
        return getArticle(payload);
      });
      const {data} = res;

      // modalType：模态类型  0：编辑  1：预览
      if (errCode(data)) {
        yield put({type: 'main/setMainParams', payload: {spinning: false}});

        const formData = data.data;
        const {subTitle, categoryId, picId, createTime} = formData;

        formData.sub_title = subTitle;
        formData.category_id = categoryId;
        formData.pic_id = picId;
        formData.create_time = createTime;

        yield put({
          type: 'setAppParams', payload: {
            formData: Object.assign({}, formData),
            modalVisible: true,
            modalTitle: modalType ? '预览文章' : '编辑文章',
            modalHandle: modalType || 1,
            modalType,
            where: 1,
            fileList: [{
              uid: picId,
              name: picId,
              status: 'done',
              url: `${imgServer}/${picId}`
            }]
          }
        })
      }
    },
    // 获取文章类型
    *getArticleTypes(action, { call, put }) {
      const res = yield call(() => {
        return getArticleTypes();
      });
      const {data} = res;

      if (errCode(data)) {
        const categories = withKey(data.data);
        yield put({type: 'setAppParams', payload: {categories}});
      }
    },
    // 新增、编辑文章
    *handleArticle({payload}, { call, put }) {
      console.log('*handleArticle payload', payload);
      const res = yield call(() => {
        return handleArticle(payload.data, payload.handle);
      });
      const {data} = res;

      if (errCode(data, true)) {
        yield put({type: 'clearForm'});
        yield put({type: 'getArticles', payload: {category_id: payload.category}});
      }
    },
    // 删除文章
    *delArticle({payload}, { call, put }) {
      console.log('*delArticle payload', payload);
      const res = yield call(() => {
        return delArticle(payload);
      });
      const {data} = res;

      errCode(data, true) && (yield put({type: 'getArticles', payload: {category_id: payload.category}}));
    },
    // 发布文章
    *publishArticle({payload}, { call, put }) {
      console.log('*publishArticle payload', payload);
      const res = yield call(() => {
        return publishArticle(payload);
      });
      const {data} = res;

      errCode(data, true) && (yield put({type: 'getArticles', payload: {category_id: payload.category}}));
    },
    // ----------APP安全检测---------- //
    // 获取需要验证的APP
    *getApps({payload}, { call, put }) {
      yield put({type: 'setAppParams', payload: {acLoading: true}});

      const res = yield call(() => {
        return getApps(payload || {page_num: Pagination.current, page_size: Pagination.pageSize});
      });
      const {data} = res;

      if (errCode(data)) {
        const {details, totalNum} = data.data;
        const acDataSource = withKey(details);

        Pagination.total = totalNum;
        yield put({type: 'setAppParams', payload: {acDataSource, acLoading: false}});
      }
    },
    // ----------邀请码---------- //
    // 获取邀请码
    *getInviteCodes({payload}, { call, put }) {
      yield put({type: 'setAppParams', payload: {icLoading: true}});

      const res = yield call(() => {
        return getInviteCodes(payload);
      });
      const {data} = res;

      if (errCode(data)) {
        const {details, totalNum} = data.data;
        const icDataSource = withKey(details);

        Pagination.total = totalNum;
        yield put({type: 'setAppParams', payload: {icDataSource, icLoading: false}});
      }
    },
    // 生成邀请码
    *addInviteCode(action, { call, put }) {
      yield put({type: 'setAppParams', payload: {icLoading: true}});

      let res = yield call(() => {
        return addInviteCode();
      });

      errCode(res.data) &&
      (yield put({
        type: 'getInviteCodes', payload: {
          page_num: Pagination.current, page_size: Pagination.pageSize
        }
      }));
    }
  },
  reducers: {
    setAppParams(state, {payload}) {
      Object.assign(state, payload);
      return {
        ...state
      }
    },
    setCategory(state, {payload}) {
      if (!state.where) {
        Pagination.current = 1;
        Pagination.params = (payload.category_id != -1 ? payload : {});
        state.category = payload.category_id;
      } else {
        state.formData.category_id = payload.category_id
      }

      return {
        ...state
      }
    },
    setFormData(state, {payload}) {
      const {fd, key, value} = payload;
      let formData = state[fd];
      let ret = {
        ...state
      };

      formData[key] = value;
      ret[state[fd]] = formData;

      return ret;
    },
    clearForm(state) {
      state.kEditor && state.kEditor.html('');
      return {
        ...state,
        where: 0,
        modalVisible: false,
        modalConfirmLoading: false,
        formData: {
          id: '', title: '', sub_title: '', pic_id: null, pic_file: null,
          content: '', category_id: null, remark: '', description: '', author: ''
        },
        fileList: []
      }
    },
    downloadApp(state, {payload}) {
      location.href = `${state.downloadURL}?sid=${SID}&appUrl=${payload.appUrl}`;
      return {
        ...state
      }
    }
  }
}
