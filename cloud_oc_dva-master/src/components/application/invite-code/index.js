import React from 'react';
import {connect} from 'dva';
import {Table} from 'antd';

import {Pagination} from '../../Pagination';
import {mapStateToProps, mapDispatchToProps} from '../../connect/AppConnect';
import Plus from './Plus';

import styles from '../../component.css';

const InviteCode = ({app}) => {
  const {icDataSource, icLoading} = app;
  const params = {
    columns: [
      {key: 'code', title: '邀请码', dataIndex: 'code', className: `${styles.center} ${styles.tableTheadTh}`}
    ],
    bordered: true,
    pagination: Pagination,
    dataSource: icDataSource,
    loading: icLoading
  }

  return (
    <div>
      <div style={{marginBottom: 10}}>
        <Plus />
      </div>
      <Table {...params} />
    </div>
  );
};

InviteCode.propTypes = {};

export default connect(mapStateToProps, mapDispatchToProps)(InviteCode);
