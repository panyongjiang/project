import React from 'react';
import {connect} from 'dva';
import {mapStateToProps, mapDispatchToProps} from '../connect/MainConnect';

import {getCookie} from '../../utils';
const {UNAME_KEY} = require('../../config/cookies.json');


const Header = ({main, dispatch}) => {
  return (
    <div className="navbar navbar-default" role="navigation">
      <div className="navbar-header">
        <a className="" href="index.html">
          <span className="navbar-brand">云平台管理</span>
        </a>
      </div>
      <div className="navbar-collapse collapse" style={{height: 1}}>
        <ul id="main-menu" className="nav navbar-nav navbar-right">
          <li className="dropdown hidden-xs">
            <a href="#" className="dropdown-toggle" data-toggle="dropdown">
              <span className="glyphicon glyphicon-user padding-right-small" style={{position:'relative',top: 3}}></span>
              <span className="padding-right-small">{getCookie(UNAME_KEY)}</span>
              <i className="fa fa-caret-down"></i>
            </a>
            <ul className="dropdown-menu">
              <li>
                <a onClick={() => {
                  dispatch({type: 'sys/setSysParams', payload: {visible: true}});
                }}>密码修改</a>
              </li>
              <li className="divider"></li>
              <li>
                <a onClick={() => {
                  dispatch({type: 'main/setMainParams', payload: {spinning: true, tip: '正在退出系统'}});
                  dispatch({type: 'auth/logout'});
                }}>系统退出</a>
              </li>
            </ul>
          </li>
        </ul>
      </div>
    </div>
  );
};

Header.propTypes = {};

export default connect(mapStateToProps, mapDispatchToProps)(Header);

