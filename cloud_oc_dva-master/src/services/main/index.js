import request from '../../utils/request';
const {method, headers, api} = require('./api.json');

const menus = [
  {
    id: 1, txt: '产品服务管理', fa: 'fa-flag', root: 1,
    children: [
      {
        id: 2, txt: '用户管理', link: 'user_manage', root: 0
      }
    ]
  },
  {
    id: 3, txt: '其它应用', fa: 'fa-pencil', root: 1,
    children: [
      {
        id: 4, txt: '试用申请', link: 'trial_application', root: 0
      },
      {
        id: 5, txt: '问题反馈', link: 'issue_feedback', root: 0
      },
      {
        id: 6, txt: '文章发布', link: 'article_manage', root: 0
      },
      {
        id: 7, txt: 'APP安全检测', link: 'app_check', root: 0
      },
      {
        id: 8, txt: '邀请码管理', link: 'invite_code', root: 0
      }
    ]
  },
  {
    id: 9, txt: '系统设置', fa: 'fa-cog', root: 1,
    children: [
      {
        id: 10, txt: '密码修改', link: 'reset_password', root: 0
      },
      {
        id: 11, txt: '敏感词管理', link: 'sensitive_word', root: 0
      }
    ]
  }
];

/**
 * 获取menu
 * @returns {Object}
 */
export async function getMenus() {
  return new Promise((resovle, reject) => {
    resovle({data: {menus}});
  });
  //return request(api[0], {
  //  method: method[1],
  //  headers: headers[0]
  //});
}
