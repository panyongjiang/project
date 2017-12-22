import React from 'react';
import {connect} from 'dva';
import {Upload, Button, Icon} from 'antd';

import Message from '../../Message';
import Form from './Form';
import {mapStateToProps, mapDispatchToProps} from '../../connect/AppConnect';

const Image = ({app, dispatch}) => {
  const {fileList, kEditor} = app;
  const params = {
    fileList: fileList,
    accept: 'image/png,image/jpg',
    onChange: ({file, fileList}) => {
      if (fileList.length == 1) {
        fileList[0].status = 'done';
        dispatch({type: 'app/setFormData', payload: {fd: 'formData', key: 'content', value: kEditor.html()}});
        dispatch({type: 'app/setAppParams', payload: {fileList}});
        dispatch({type: 'app/setFormData', payload: {
          fd: 'formData', key: 'pic_file', value: file.originFileObj
        }});
      } else if (fileList.length > 1) Message.warn('最多上传一张图片');
    },
    onRemove: () => {
      dispatch({type: 'app/setAppParams', payload: {fileList: []}});
      dispatch({type: 'app/setFormData', payload: {
        fd: 'formData', key: 'pic_file', value: null
      }});
    }
  }

  return (
    <Upload {...params}>
      <Button>
        <Icon type="upload"/>图片上传
      </Button>
    </Upload>
  )
}

Image.propTypes = {};

export default connect(mapStateToProps, mapDispatchToProps)(Image);
