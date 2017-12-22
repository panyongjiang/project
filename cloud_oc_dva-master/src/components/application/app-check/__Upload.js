import React from 'react';
import {connect} from 'dva';
import {Upload as AUpload, Icon} from 'antd';
import Message from '../../Message';

import {mapStateToProps, mapDispatchToProps} from '../../connect/AppConnect';

const Upload = ({app, dispatch}) => {
  const params = {
    name: 'file',
    showUploadList: false,
    action: app.uploadURL,
    data: {
      sid: app.sid,
      id: '',
      remark: ''
    },
    onChange: (info) => {
      dispatch({type: 'main/setMainParams', payload: {
        spinning: true, tip: '正在上传'
      }});

      const {status} = info.file;

      if (status == 'done') {
        Message.success('上传成功');
        dispatch({type: 'app/getApps'});
      } else Message.success('上传失败');
    }
  }

  return (
    <AUpload {...params}>
      <Icon type="upload" />
    </AUpload>
  )
}

Upload.propTypes = {};

export default connect(mapStateToProps, mapDispatchToProps)(Upload);
