import React from 'react';
import {connect} from 'dva';
import {mapStateToProps, mapDispatchToProps} from '../connect/AuthConnect';

import {getTimestamp} from '../../utils';

const {protocol, host, port, projectName} = require('../../config/server.json');
const verifyCodeURL = `${protocol}://${host}:${port}${projectName}/common/rand_ver_image?uid=`;


const VerifyCode = ({auth, dispatch}) => {
  const params = {
    type: 'text',
    placeholder: '请输入验证码',
    className: 'form-control',
    value: auth.verifyCode
  };

  return (
    <div>
      <input {...params} style={{width: 150, display: "inline"}} onChange={(e) => {
        dispatch({type: 'auth/setLoginParam', payload: {verifyCode: e.target.value}});
      }}/>
      <img style={{display: "inline", paddingLeft: 20, paddingBottom: 2, cursor:"pointer"}}
           src={`${verifyCodeURL}${auth.timestamp}`}
           onClick={() => {
          dispatch({type: 'auth/setLoginParam', payload: {timestamp: getTimestamp()}});
       }}/>
    </div>
  );
};

VerifyCode.propTypes = {};

export default connect(mapStateToProps, mapDispatchToProps)(VerifyCode);
