import React from 'react';
import {Input} from 'antd';
import {connect} from 'dva';

import {mapStateToProps, mapDispatchToProps} from '../../connect/SysConnect';

const PWD_ARR = [
  {name: 'oldPwd', placeholder: '请输入原始密码'},
  {name: 'newPwd', placeholder: '请输入新密码'},
  {name: 'reNewPwd', placeholder: '请重复输入新密码'}
];

const Password = ({sys, dispatch}) => {
  const params = {
    type: 'password',
    onChange: (e) => {
      let dom = e.target;
      let payload = {};

      payload[dom.getAttribute('name')] = dom.value;
      dispatch({type: 'sys/setSysParams', payload});
    }
  }

  return (
    <div>
      {
        PWD_ARR.map((p, i) => {
          return (
            <div key={p.name} style={{ marginBottom: 15 }}>
              <Input name={p.name} {...params} value={sys[p.name]} placeholder={p.placeholder}/>
            </div>
          )
        })
      }
    </div>
  )
}

Password.propTypes = {};

export default connect(mapStateToProps, mapDispatchToProps)(Password);
