import React from 'react';
import {connect} from 'dva';
import {Modal, Input} from 'antd';

import {checkPwd} from '../../../utils/validate';
import Password from './Password';
import {mapStateToProps, mapDispatchToProps} from '../../connect/SysConnect';

const ResetPassword = ({sys, dispatch}) => {
  const {title, visible, oldPwd, newPwd, reNewPwd} = sys;
  const params = {title, visible};

  return (
    <Modal {...params} onOk={() => {
        checkPwd(oldPwd, newPwd, reNewPwd) && (() => {
          dispatch({type: 'sys/resetPwd', payload: {
            password: oldPwd, change_pwd: newPwd
          }});
        })();
      }} onCancel={() => {
        dispatch({type: 'sys/setSysParams', payload: {visible: false}});
        dispatch({type: 'sys/clearPwd'});
      }}>
      <div style={{textAlign: 'center', marginTop: 20}}>
        <Password />
      </div>
    </Modal>
  );
};

ResetPassword.propTypes = {};

export default connect(mapStateToProps, mapDispatchToProps)(ResetPassword);
