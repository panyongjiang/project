import React from 'react';
import {connect} from 'dva';
import {Button} from'antd';

import {mapDispatchToProps} from '../../connect/AppConnect';

const Plus = ({dispatch}) => {
  const params = {
    icon: 'plus-circle-o',
    onClick: () => {
      dispatch({type: 'app/addInviteCode'});
    }
  }

  return (
    <Button {...params}>生成</Button>
  )
}

Plus.propTypes = {};

export default connect(mapDispatchToProps)(Plus);
