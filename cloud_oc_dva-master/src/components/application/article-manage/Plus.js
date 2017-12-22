import React from 'react';
import {connect} from 'dva';
import {Button} from'antd';

import {mapStateToProps, mapDispatchToProps} from '../../connect/AppConnect';

const Plus = ({app, dispatch}) => {
  const params = {
    icon: 'plus-circle-o',
    onClick: () => {
      dispatch({type: 'app/setAppParams', payload: {
        modalVisible: true,
        modalTitle: '新增文章',
        modalHandle: 0,
        where: 1
      }});
      dispatch({type: 'app/setFormData', payload: {
        fd: 'formData',
        key: 'category_id',
        value: `${app.categories[0].id}`
      }});
    }
  }

  return (
    <Button {...params}>增加</Button>
  )
}

Plus.propTypes = {};

export default connect(mapStateToProps, mapDispatchToProps)(Plus);
