import React from 'react';
import {connect} from 'dva';
import {Checkbox} from 'antd';
import {mapStateToProps, mapDispatchToProps} from '../connect/AuthConnect';

import {setCookie} from '../../utils';
const {REMEMBER_KEY} = require('../../config/cookies.json');

const Remember = ({auth, dispatch}) => {
  const params = {
    checked: auth.remember
  };

  return (
    <Checkbox {...params} onChange={(e) => {
      let checked = e.target.checked ? 1 : 0;

      dispatch({type: 'auth/setLoginParam', payload: {remember: checked}});
      setCookie(REMEMBER_KEY, checked);
    }}>记住我</Checkbox>
  );
};

Remember.propTypes = {};

export default connect(mapStateToProps, mapDispatchToProps)(Remember);
