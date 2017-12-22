import React from 'react';
import {connect} from 'dva';
import {Modal} from 'antd';

import Form from './Form';

import {mapStateToProps, mapDispatchToProps} from '../../connect/AppConnect';

import styles from '../../component.css';

const iModal = ({app, dispatch}) => {
  const {fbModalVisible, fbModalTitle, fbModalConfirmLoading, fbFormData} = app;
  const params = {
    className: styles['ant-modal'],
    visible: fbModalVisible,
    title: fbModalTitle,
    confirmLoading: fbModalConfirmLoading,
    onOk: () => {
      const {id, status} = fbFormData;
      dispatch({type: 'app/setAppParams', payload: {fbModalConfirmLoading: true}});
      dispatch({type: 'app/updateFeedBackStatus', payload: {id, status}});
    },
    onCancel: () => {
      dispatch({type: 'app/setAppParams', payload: {fbModalVisible: false}});
    }
  }

  return (
    <Modal {...params}>
      <Form />
    </Modal>
  )
}

iModal.propTypes = {};

export default connect(mapStateToProps, mapDispatchToProps)(iModal);
