import React from 'react';
import {connect} from 'dva';
import {Input} from'antd';

import {mapStateToProps, mapDispatchToProps} from '../../connect/SysConnect';

const Word = ({sys, dispatch}) => {
  const params = {
    placeholder: '敏感词',
    value: sys.word,
    onChange: (e) => {
      dispatch({type: 'sys/setSysParams', payload: {word: e.target.value}});
    }
  }

  return (
    <Input {...params} style={{width: 150}}/>
  )
}

Word.propTypes = {};

export default connect(mapStateToProps, mapDispatchToProps)(Word);
