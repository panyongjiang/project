import React from 'react';
import {connect} from 'dva';
import {Button} from'antd';

import {mapStateToProps, mapDispatchToProps} from '../../connect/SysConnect';

const Plus = ({sys, dispatch}) => {
  const params = {
    icon: 'plus-circle-o',
    onClick: () => {
      dispatch({
        type: 'sys/addWord', payload: {
          swLoading: true,
          word: sys.word
        }
      });
    }
  }

  return (
    <Button {...params}>添加</Button>
  )
}

Plus.propTypes = {};

export default connect(mapStateToProps, mapDispatchToProps)(Plus);
