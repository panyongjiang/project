import React from 'react';

export const Pagination = {
    current: 1,
    pageSize: 10,
    defaultCurrent: 1,
    total: 0,
    // 分页组件以外的自定义参数
    dispatch: null,
    dispatchType: '',
    params: {},
    showTotal: (total, range) => {
        return `第${range[0]}-${range[1]}条  共 ${total} 条`;
    },
    onChange: (page, pageSize) => {
      Pagination.current = page;
      Pagination.pageSize = pageSize;

      const action = {
        type: Pagination.dispatchType,
        payload: {
          page_num: page,
          page_size: pageSize
        }
      };
      Object.assign(action.payload, Pagination.params);

      Pagination.dispatch(action);
    }
    //showSizeChanger: true,
    //onShowSizeChange: null
};
