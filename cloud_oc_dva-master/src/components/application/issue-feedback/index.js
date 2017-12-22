import React from 'react';
import {connect} from 'dva';
import {Table, Icon, Tooltip, Popconfirm} from 'antd';

import {Pagination} from '../../Pagination';
import Modal from './Modal';

import {mapStateToProps, mapDispatchToProps} from '../../connect/AppConnect';

import styles from '../../component.css';
const tableStyle = `${styles.center} ${styles.tableTheadTh}`;

const IssueFeedBack = ({app, dispatch}) => {
  const {fbDataSource, fbLoading} = app;
  const params = {
    columns: [
      {key: 'createTime', title: '时间', dataIndex: 'createTime', className: `${tableStyle}`},
      {key: 'content', title: '反馈问题', dataIndex: 'content', className: `${tableStyle}`},
      {key: 'phone', title: '电话号码', dataIndex: 'phone', className: `${tableStyle}`},
      {key: 'status', title: '状态', dataIndex: 'status', className: `${tableStyle}`},
      {
        key: 'operation', title: '操作', className: `${tableStyle}`,
        render: (text, record) => {
          return (
            <div>
              <a onClick={() => {
                dispatch({type: 'main/setMainParams', payload: {spinning: true}});
                dispatch({type: 'app/getFeedBack', payload: {id: record.id}});
              }}>
                <Tooltip placement="top" title="详情">
                  <Icon type="file-text" />
                </Tooltip>
              </a>
              <span className="ant-divider" />
              <Popconfirm title="是否确定删除?" onConfirm={() => {
                dispatch({type: 'app/delFeedBack', payload: {id: record.id}});
              }} okText="确定" cancelText="取消">
                <a><Icon type="delete" /></a>
              </Popconfirm>
            </div>
          )
        }
      }
    ],
    dataSource: fbDataSource,
    loading: fbLoading,
    bordered: true,
    pagination: Pagination
  }

  return (
    <div>
      <Table {...params} />
      <Modal />
    </div>
  );
};

IssueFeedBack.propTypes = {};

export default connect(mapStateToProps, mapDispatchToProps)(IssueFeedBack);
