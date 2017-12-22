// 产品服务&管理
import UserManage from './components/product/user-manage';

// 其它应用
import TrialApplication from './components/application/trial-application';
import IssueFeedBack from './components/application/issue-feedback';
import ArticleManage from './components/application/article-manage';
import AppCheck from './components/application/app-check';
import InviteCode from './components/application/invite-code';

// 系统设置
//import ResetPassword from './components/system/reset-password';
import SensitiveWord from './components/system/sensitive-word';


// 组件映射
export const components = {
  user_manage: <UserManage />,
  trial_application: <TrialApplication />,
  issue_feedback: <IssueFeedBack />,
  article_manage: <ArticleManage />,
  app_check: <AppCheck />,
  invite_code: <InviteCode />,
  //reset_password: <ResetPassword />,
  sensitive_word: <SensitiveWord />
}

// 组件key集合
const componentKeys = [];
for (let i in components) componentKeys.push(i);

// 配置当前路由
export const routeTo = (route) => {
  route == '/' && (route = `${route}${componentKeys[0]}`);
  return route.substr(1);
}

export {componentKeys};
