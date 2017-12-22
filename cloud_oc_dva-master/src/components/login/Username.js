import React from 'react';
import {connect} from 'dva';

import {mapStateToProps, mapDispatchToProps} from '../connect/AuthConnect';

const Username = ({auth, dispatch}) => {
  const params = {
    type: 'text',
    placeholder: '请输入用户名',
    className: 'form-control',
    value: auth.userName
  };

  return (
      <input {...params} onChange={(e) => {
        dispatch({type: 'auth/setLoginParam', payload: {userName: e.target.value}});
      }} />
  );
};

Username.propTypes = {};

export default connect(mapStateToProps, mapDispatchToProps)(Username);
