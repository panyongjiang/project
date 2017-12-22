import React from 'react';
import {connect} from 'dva';
import {Modal, Form} from 'antd';

import ServerForm from './ServerForm';
import Message from '../../Message';

import {mapStateToProps, mapDispatchToProps} from '../../connect/ProductConnect';

import styles from '../../component.css';

//class iModal extends React.Component {
//  constructor(props) {
//    super(props);
//    console.log('props', props);
//  }
//
//  render() {
//    return (
//      <div>111</div>
//    )
//  }
//}


const ProductModal = ({product, dispatch, form}) => {
  const {getFieldDecorator} = form;
  const {umModalTitle, umModalVisible, umModalConfirmLoading,
    umFormData, industries} = product;
  const modalParams = {
    className: styles['ant-modal'],
    visible: umModalVisible,
    title: umModalTitle,
    confirmLoading: umModalConfirmLoading,
    onOk: () => {
      dispatch({type: 'product/setProductParams', payload: {modalConfirmLoading: true}});
      dispatch({type: 'product/updateUser', payload: umFormData});
    },
    onCancel: () => {
      dispatch({type: 'product/setProductParams', payload: {umModalVisible: false}});
    }
  }
  const formParams = {
    decorator: getFieldDecorator,
    product
  }

  // 初始化表单元素值
  //setFieldsInitialValue(umFormData);



  return (
    <Modal {...modalParams}>
       <ServerForm {...formParams} />
    </Modal>
  )
}

//ProductModal.propTypes = {};

export default connect(mapStateToProps, mapDispatchToProps)(Form.create()(ProductModal));
