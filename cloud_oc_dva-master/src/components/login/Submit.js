import React from 'react';
import {connect} from 'dva';
import {Button} from 'antd';
import {mapStateToProps, mapDispatchToProps} from '../connect/AuthConnect';

import {encryptByDES, getTimestamp} from '../../utils';
import {checkLogin} from '../../utils/validate';

const Submit = ({auth, dispatch}) => {
  const params = {
    loading: auth.loading,
    icon: 'login',
    className: 'pull-right'
  }

  return (
    <Button {...params} onClick={() => {
      submit({auth, dispatch});
    }} onKeyUp={(event) => {
      console.log(event)
      //const e = event || window.event || arguments.callee.caller.arguments[0];
      //if (e && e.keyCode == 13) submit({auth, dispatch});
    }}>登录</Button>
  );
};

Submit.propTypes = {};

function submit({auth, dispatch}) {
  const {userName, password} = auth;

  checkLogin(userName, password) && (() => {
    dispatch({type: 'auth/setLoginParam', payload: {
      loading: true
    }});

    dispatch({type: 'auth/login', payload: {
      user_name: auth.userName,
      password: encryptByDES(auth.password, auth.desKey),
      verifyCode: auth.verifyCode,
      uid: auth.timestamp
    }});
  })();
}

export default connect(mapStateToProps, mapDispatchToProps)(Submit);
