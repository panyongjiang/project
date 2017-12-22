import React from 'react';
import {connect} from 'dva';
import {Input} from 'antd';
const Search = Input.Search;

import {Pagination} from '../../Pagination';

import {mapStateToProps, mapDispatchToProps} from '../../connect/ProductConnect';


const iSearch = ({product, dispatch}) => {
  console.log('iSearch product', product);
  const doSearch = (value) => {
    Pagination.current = 1;
    dispatch({type: 'product/setProductParams', payload: {keyword: value}});
    dispatch({type: 'product/getUsers', payload: {user_name: value}});
  }
  const {keyword} = product;
  const params = {
    placeholder: "请输入用户名",
    value: keyword,
    onChange: (e) => {
      doSearch(e.target.value);
    },
    onSearch: (v) => {
      doSearch(v);
    }
  };


  return (
    <Search style={{width: 200}} {...params} />
  );
};

iSearch.propTypes = {};

export default connect(mapStateToProps, mapDispatchToProps)(iSearch);
