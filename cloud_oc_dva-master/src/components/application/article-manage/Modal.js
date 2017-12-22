import React from 'react';
import {connect} from 'dva';
import {Modal} from 'antd';

import Form from './Form';
import Review from './Review';
import Message from '../../Message';

import {mapStateToProps, mapDispatchToProps} from '../../connect/AppConnect';

import styles from '../../component.css';

const iModal = ({app, dispatch}) => {
  const {modalVisible, modalTitle, modalHandle, modalType,
    modalConfirmLoading, kEditor, formData, category, fileList} = app;
  console.log('iModal', app)
  const params = {
    className: styles['ant-modal'],
    visible: modalVisible,
    title: modalTitle,
    confirmLoading: modalConfirmLoading,
    onOk: () => {
      if (fileList.length > 0) {
        dispatch({type: 'app/setAppParams', payload: {modalConfirmLoading: true}});
        formData.content = kEditor.html();
        dispatch({type: 'app/handleArticle', payload: {
          handle: modalHandle,
          data: formData,
          category
        }});
      } else {
        Message.warn('请上传图片');
        return;
      }
    },
    onCancel: () => {
      modalType != 1 ?
        dispatch({type: 'app/clearForm'}) :
        dispatch({type: 'app/setAppParams', payload: {
          where: 0,
          modalVisible: false
        }});
    }
  }

  return (
    <Modal {...params}>
      {
        modalType ? <Review /> : <Form />
      }
    </Modal>
  )
}

iModal.propTypes = {};

export default connect(mapStateToProps, mapDispatchToProps)(iModal);
