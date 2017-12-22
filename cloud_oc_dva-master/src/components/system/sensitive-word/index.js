import React from 'react';
import {connect} from 'dva';
import {Table, Popconfirm, Icon} from 'antd';

import Plus from './Plus';
import Word from './Word';

import {mapStateToProps, mapDispatchToProps} from '../../connect/SysConnect';

import styles from '../../component.css';

const SensitiveWord = ({sys, dispatch}) => {
  const {swDataSource, swLoading} = sys;
  const params = {
    columns: [
      {key: 'word', title: '敏感词', dataIndex: 'word', className: `${styles.center} ${styles.tableTheadTh}`},
      {
        key: 'operation', title: '操作', className: `${styles.center} ${styles.tableTheadTh}`,
        render: (text, record) => {
          return (
            <div>
              <Popconfirm title='确定要删除吗?' okText='确定' cancelText='取消'
                          onConfirm={() => {
                             dispatch({type: 'sys/delWord', payload: {word: record.word}});
                          }}>
                <a><Icon type="delete"/></a>
              </Popconfirm>
            </div>
          )
        }
      }
    ],
    bordered: true,
    dataSource: swDataSource,
    loading: swLoading
  }

  return (
    <div>
      <div style={{marginBottom: 10}}>
        <Word />&nbsp;
        <Plus />
      </div>
      <Table {...params} />
    </div>
  );
};

SensitiveWord.propTypes = {};

export default connect(mapStateToProps, mapDispatchToProps)(SensitiveWord);
