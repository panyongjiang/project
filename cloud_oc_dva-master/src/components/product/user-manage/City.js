import React from 'react';
import {connect} from 'dva';
import {Cascader} from'antd';

/*省市信息*/
const cities = require('../../../config/cities.json');

import {mapStateToProps, mapDispatchToProps} from '../../connect/ProductConnect';

const City = ({product, dispatch}) => {
  const {province, city} = product.umFormData;
  console.log(province, city)
  const params = {
    options: cities,
    value: [province, city],
    //value: where ? `${formData.category_id}` : category,
    onChange: (value) => {
      console.log(value);
      dispatch({type: 'product/setFormData', payload: {
        fd: 'umFormData',
        ds: [{key: 'province', value: value[0]}, {key: 'city', value: value[1]}]}});
      //dispatch({type: 'product/setCategory', payload: {category_id: value}});
      //app.where || dispatch({type: 'app/getArticles', payload: {category_id: value}});
    }
  }
  //const options = [];

  //where || options.push(<Option key='-1'>全部</Option>);
  //industries.map((t) => {
  //  return options.push(<Option key={t.id}>{t.industryName}</Option>);
  //})

  return (
    <Cascader {...params} />
  )
}

City.propTypes = {};

export default connect(mapStateToProps, mapDispatchToProps)(City);
