import React from 'react';
import {connect} from 'dva';
import {Input, Button, Checkbox} from 'antd';

import {getCookie, reload} from '../utils';
const {SID_KEY} = require('../config/cookies.json');

import Username from '../components/login/Username';
import Password from '../components/login/Password';
import VerifyCode from '../components/login/VerifyCode';
import Remember from '../components/login/Remember';
import Submit from '../components/login/Submit';

import styles from './Login.css';

const Login = ({auth})=> {
  return (
    <div className="theme-blue">
      <div className="navbar navbar-default" role="navigation">
        <div className="navbar-header">
          <div className={`navbar-brand ${styles['navbar-logo']}`}></div>
        </div>
      </div>
      <div className="dialog">
        <div className="panel panel-default">
          <p className="panel-heading no-collapse">云平台登录</p>
          <div className="panel-body">
            <div className="form-group">
              <label>用户名</label>
              <Username />
            </div>
            <div className="form-group">
              <label>密码</label>
              <Password />
            </div>
            <div className={`form-group ${auth.repeatTime >= 3 ? '' : 'hidden'}`}>
              <label>验证码</label>
              <VerifyCode />
            </div>
            <div>
              <Submit />
              <label className="remember-me">
                <Remember />
              </label>
              <div className="clearfix"></div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

Login.propTypes = {};

function mapStateToProps(state) {
  getCookie(SID_KEY) && reload();
  return {auth: state.auth};
}

export default connect(mapStateToProps)(Login);
