import React from 'react';
import {connect} from 'dva';
import {Table, Icon, Tooltip, Popconfirm} from 'antd';

import {Pagination} from '../../Pagination';
import Modal from './Modal';

import {mapStateToProps, mapDispatchToProps} from '../../connect/AppConnect';

import styles from '../../component.css';
const tableStyle = `${styles.center} ${styles.tableTheadTh}`;

const TrialApplication = ({app, dispatch}) => {
  const {taDataSource, taLoading} = app;
  const params = {
    columns: [
      {key: 'createTime', title: '时间', dataIndex: 'createTime', className: `${tableStyle}`},
      {key: 'user_name', title: '账号', dataIndex: 'user_name', className: `${tableStyle}`},
      {key: 'trial1', title: '申请产品', dataIndex: 'trial1', className: `${tableStyle}`},
      {key: 'trialStatue', title: '状态', dataIndex: 'trialStatue', className: `${tableStyle}`},
      {
        key: 'operation', title: '操作', className: `${tableStyle}`,
        render: (text, record) => {
          return (
            <div>
              <a onClick={() => {
                dispatch({type: 'main/setMainParams', payload: {spinning: true}});
                dispatch({type: 'app/getTrial', payload: {id: record.id}});
              }}>
                <Tooltip placement="top" title="详情">
                  <Icon type="file-text" />
                </Tooltip>
              </a>
              <span className="ant-divider" />
              <Popconfirm title="是否确定删除?" onConfirm={() => {
                dispatch({type: 'app/delTrial', payload: {id: record.id}});
              }} okText="确定" cancelText="取消">
                <a><Icon type="delete" /></a>
              </Popconfirm>
            </div>
          )
        }
      }
    ],
    dataSource: taDataSource,
    loading: taLoading,
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

TrialApplication.propTypes = {};

export default connect(mapStateToProps, mapDispatchToProps)(TrialApplication);
