import React from 'react';
import {connect} from 'dva';
import {Table, Icon, Popconfirm, Tooltip} from 'antd';

import Modal from './Modal';
import {Pagination} from '../../Pagination';
import Search from './Search';

import {mapStateToProps, mapDispatchToProps} from '../../connect/ProductConnect';

import styles from '../../component.css';
const tableStyle = `${styles.center} ${styles.tableTheadTh}`;

const UserManager = ({product, dispatch}) => {
  const {umDataSource, umLoading} = product;
  const params = {
    columns: [
      {key: 'id', title: '序号', dataIndex: 'id', className: `${tableStyle}`},
      {key: 'userName', title: '账号', dataIndex: 'userName', className: `${tableStyle}`},
      {
        key: 'operation', title: '操作', className: `${tableStyle}`,
        render: (text, record) => {
          const {id, status} = record;

          return (
            <div>
              <a onClick={()=>{
                dispatch({type: 'main/setMainParams', payload: {spinning: true}});
                dispatch({type: 'product/getUser', payload: {userId: id}});
              }}>
                <Tooltip placement="top" title="服务管理"><Icon type="setting" /></Tooltip>
              </a>
              <span className="ant-divider"/>
              <a onClick={() => {

              }}>
                <Tooltip placement="top" title="信息修改"><Icon type="edit" /></Tooltip>
              </a>
              <span className="ant-divider"/>
              <a onClick={() => {

              }}>
                <Tooltip placement="top" title="密码重置"><Icon type="safety" /></Tooltip>
              </a>
              <span className="ant-divider"/>
              <Popconfirm title="是否确定删除该用户?" onConfirm={() => {

                    }} okText="确定" cancelText="取消">
                <a href="#"><Icon type="delete" /></a>
              </Popconfirm>
            </div>
          );
        }
      }
    ],
    dataSource: umDataSource,
    loading: umLoading,
    bordered: true,
    pagination: Pagination
  }

  return (
    <div>
      <div style={{marginBottom: 10}}>
        <Search />
      </div>
      <Table {...params} />
      <Modal />
    </div>
  );
};

UserManager.propTypes = {};

export default connect(mapStateToProps, mapDispatchToProps)(UserManager);
