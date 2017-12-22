import React from 'react';
import {connect} from 'dva';
import {Table, Icon, Popconfirm} from 'antd';

import {Pagination} from '../../Pagination';
import Message from '../../Message';
import Plus from './Plus';
import Modal from './Modal';
import Category from './Category';
import {mapStateToProps, mapDispatchToProps} from '../../connect/AppConnect';

import styles from '../../component.css';
const tableStyle = `${styles.center} ${styles.tableTheadTh}`;

const ArticleManage = ({app, dispatch}) => {
  const {amDataSource, amLoading, categories} = app;
  const showModal = (articleId, modalType) => {
    dispatch({type: 'main/setMainParams', payload: {spinning: true}});
    dispatch({type: 'app/getArticle', payload: {id: articleId, modalType: modalType}});
  }
  const params = {
    columns: [
      {key: 'title', title: '标题', dataIndex: '_title', className: `${tableStyle}`},
      {key: 'subTitle', title: '副标题', dataIndex: '_subTitle', className: `${tableStyle}`},
      {
        key: 'categoryId', title: '文件类型', className: `${tableStyle}`,
        render: (text, record) => {
          const category = categories[record.categoryId - 1];

          return (
            <span>
              {
                category ? category.categoryName : '未知'
              }
            </span>
          )
        }
      },
      {key: 'author', title: '作者', dataIndex: 'author', className: `${tableStyle}`},
      {key: 'createTime', title: '新增时间', dataIndex: 'createTime', className: `${tableStyle}`},
      {
        key: 'operation', title: '操作', className: `${tableStyle}`,
        render: (text, record) => {
          const {id, status} = record;

          return (
            <div>
              <a onClick={()=>{
                showModal(id, 1);
              }}>
                <Icon type="eye-o"/>
              </a>
              <span className="ant-divider"/>
              <a onClick={() => {
                showModal(id, 0);
              }}>
                <Icon type="edit"/>
              </a>
              <span className="ant-divider"/>
              <Popconfirm title='确定要删除吗?'
                          okText='确定' cancelText='取消'
                          onConfirm={() => {
                                    dispatch({type: 'app/delArticle', payload: {id}});
                                }}>
                <a><Icon type="delete"/></a>
              </Popconfirm>
              <span className="ant-divider" style={{display: status == 0 ? "inline" : "none"}}/>
              <Popconfirm title='确定要发布吗?'
                          okText='确定' cancelText='取消'
                          onConfirm={() => {
                                    dispatch({type: 'app/publishArticle', payload: {id}});
                                }}>
                <a style={{display: status == 0 ? "inline" : "none"}}><Icon type="cloud-upload-o"/></a>
              </Popconfirm>
            </div>
          );
        }
      }
    ],
    dataSource: amDataSource,
    loading: amLoading,
    bordered: true,
    pagination: Pagination
  }

  return (
    <div>
      <div style={{marginBottom: 10}}>
        <Plus />
        <span className="ant-divider"/>
        <Category />
      </div>
      <Table {...params} />
      <Modal />
    </div>
  );
};

ArticleManage.propTypes = {};

export default connect(mapStateToProps, mapDispatchToProps)(ArticleManage);
