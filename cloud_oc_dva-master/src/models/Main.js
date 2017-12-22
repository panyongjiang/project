import {getMenus} from '../services/main';
import errCode from '../utils/errcode';
const iSpecialMenus = require('../config/special.menus.json');
import {routeTo} from '../router.mapping';

export default {
  namespace: 'main',
  state: {
    // 加载信息
    spinning: true,
    tip: '加载中',
    // 菜单
    menus: [],
    menuNames: [],
    specialMenus: [], // 特殊菜单项（在页面的其它地方显示）
    // 当前路由
    currentRoute: ''
  },
  subscriptions: {
    setup({ dispatch, history }) {
      history.listen((path) => {
        console.log('Main subscriptions path', path);
        let currentRoute = routeTo(path.pathname);
        dispatch({type: 'setMainParams', payload: {currentRoute}});
      })
    }
  },
  effects: {
    *getMenus(action, { call, put }) {
      const res = yield call(() => {
        return getMenus();
      });
      const {menus} = res.data;

      yield put({
        type: 'setMainParams', payload: {menus}
      });

      // 菜单名称集合、特殊菜单集合
      let menuNames = [], specialMenus = [];
      menus.map((m) => {
        if (m.root) {
          m.children.map((c) => {
            iSpecialMenus.some((m) =>{
              return m == c.link;
            }) && specialMenus.push(c);

            menuNames.push(c.link);
          });
        } else {
          menuNames.push(m.link);
        }
      });
      yield put({
        type: 'setMainParams', payload: {
          menuNames,
          specialMenus,
          spinning: false
        }
      });
    }
  },
  reducers: {
    setMainParams(state, {payload}) {
      console.log('reducers setMainParams payload', payload);
      Object.assign(state, payload);
      return {
        ...state
      };
    }
  }
}
