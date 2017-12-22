import React from 'react';
import {connect} from 'dva';
import {Button, Select} from'antd';
const Option = Select.Option;

import {mapStateToProps, mapDispatchToProps} from '../../connect/AppConnect';

const Category = ({app, dispatch}) => {
  const {where, formData, category, categories, kEditor} = app;
  const params = {
    placeholder: '请选择文件类型',
    value: where ? `${formData.category_id}` : category,
    onChange: (value) => {
      dispatch({type: 'app/setCategory', payload: {category_id: value}});
      dispatch({type: 'app/setFormData', payload: {fd: 'formData', key: 'content', value: kEditor ? kEditor.html() : ''}});
      app.where || dispatch({type: 'app/getArticles', payload: {category_id: value}});
    }
  }

  let options = [];
  where || options.push(<Option key='-1'>全部</Option>);
  categories.map((c) => {
    options.push(<Option key={c.id}>{c.categoryName}</Option>);
  })

  return (
    <Select {...params} style={{width: 150}}>
      {options}
    </Select>
  )
}

Category.propTypes = {};

export default connect(mapStateToProps, mapDispatchToProps)(Category);
