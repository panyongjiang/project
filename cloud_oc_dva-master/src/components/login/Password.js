import React from 'react';
import {connect} from 'dva';
import {mapStateToProps, mapDispatchToProps} from '../connect/AuthConnect';


const Password = ({auth, dispatch}) => {
  const params = {
    type: 'password',
    placeholder: '请输入密码',
    className: 'form-control',
    value: auth.password
  };

  return (
    <input {...params} onChange={(e) => {
      dispatch({type: 'auth/setLoginParam', payload: {password: e.target.value}});
    }} />
  );
};

Password.propTypes = {};

export default connect(mapStateToProps, mapDispatchToProps)(Password);
