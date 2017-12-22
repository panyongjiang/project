import React from 'react';
import {connect} from 'dva';
import {Table, Upload, Icon} from 'antd';

import {Pagination} from '../../Pagination';
import Message from '../../Message';
import {mapStateToProps, mapDispatchToProps} from '../../connect/AppConnect';

import styles from '../../component.css';
const tableStyle = `${styles.center} ${styles.tableTheadTh}`;

const AppCheck = ({app, dispatch}) => {
  const {acDataSource, acLoading} = app;
  const params = {
    columns: [
      {key: 'createTime', title: '时间', dataIndex: 'createTime', className: `${tableStyle}`,
        sorter: (a, b) => {
          return a.timestamps - b.timestamps;
        }
      },
      {key: 'createUserName', title: '账号', dataIndex: 'createUserName', className: `${tableStyle}`},
      {key: 'appStatus', title: '状态', dataIndex: 'appStatus', className: `${tableStyle}`,
        sorter: (a, b) => {
          return b.app_status - a.app_status;
        }
      },
      {key: 'appName', title: 'APP下载', dataIndex: 'appName', className: `${tableStyle}`,
        render: (text, record) => {
          const {appUrl} = record;
          return (
            <a onClick={() => {
              dispatch({type: 'app/downloadApp', payload: {appUrl}});
            }}>{text}</a>
          )
        }
      },
      {key: 'operation', title: '操作', className: `${tableStyle}`,
        render: (text, record) => {
          const params = {
            name: 'file',
            showUploadList: false,
            action: app.uploadURL,
            data: {
              sid: app.sid,
              id: record.id,
              remark: ''
            },
            onChange: (info) => {
              dispatch({type: 'main/setMainParams', payload: {
                spinning: true, tip: '正在上传'
              }});

              const {status} = info.file;

              if (status == 'done') {
                Message.success('上传成功');
                dispatch({type: 'main/setMainParams', payload: {
                  spinning: false
                }});
                dispatch({type: 'app/getApps'});
              } else if (status == 'error') {
                Message.success('上传失败');
                dispatch({type: 'main/setMainParams', payload: {
                  spinning: false
                }});
              }
            }
          }

          return (
            <a>
              <Upload {...params}>
                <Icon type="upload" />
              </Upload>
            </a>
          )
        }
      }
    ],
    bordered: true,
    pagination: Pagination,
    dataSource: acDataSource,
    loading: acLoading
  }

  return (
    <Table {...params} />
  );
};

AppCheck.propTypes = {};

export default connect(mapStateToProps, mapDispatchToProps)(AppCheck);
