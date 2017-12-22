import React from 'react';
import {connect} from 'dva';
import {Modal} from 'antd';

import Form from './Form';

import {mapStateToProps, mapDispatchToProps} from '../../connect/AppConnect';

import styles from '../../component.css';

const iModal = ({app, dispatch}) => {
  const {taModalVisible, taModalTitle, taModalConfirmLoading, taFormData} = app;
  const params = {
    className: styles['ant-modal'],
    visible: taModalVisible,
    title: taModalTitle,
    confirmLoading: taModalConfirmLoading,
    onOk: () => {
      const {id, trialStatue} = taFormData;
      dispatch({type: 'app/setAppParams', payload: {taModalConfirmLoading: true}});
      dispatch({type: 'app/updateTrialStatus', payload: {id, trial_status: trialStatue}});
    },
    onCancel: () => {
      dispatch({type: 'app/setAppParams', payload: {taModalVisible: false}});
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
