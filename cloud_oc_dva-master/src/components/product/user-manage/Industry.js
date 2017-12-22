import React from 'react';
import {connect} from 'dva';
import {Select} from'antd';
const Option = Select.Option;

import {mapStateToProps, mapDispatchToProps} from '../../connect/ProductConnect';


class Industry extends React.Component {
  render() {
    const {industries, industryName} = this.props;
    console.log(this.props);
    return (
      <Select style={{width: 325}} value={industryName}>
        {
          industries.map((t) => {
            return (<Option key={t.id}>{t.industryName}</Option>);
          })
        }
      </Select>
    )
  }
}

//const Industry = ({product, dispatch}) => {
//  const {industries} = product;
//  const {industryName} = product.umFormData;
//  const params = {
//    //value: where ? `${formData.category_id}` : category,
//    onChange: (value) => {
//      dispatch({type: 'app/setFormData', payload: {
//        fd: 'formData', key: 'industryName', value: industries[value - 1].industryName}});
//      //dispatch({type: 'product/setCategory', payload: {category_id: value}});
//      //dispatch({type: 'app/setFormData', payload: {fd: 'formData', key: 'content', value: kEditor ? kEditor.html() : ''}});
//      //app.where || dispatch({type: 'app/getArticles', payload: {category_id: value}});
//    }
//  }
//  //const options = [];
//
//  //where || options.push(<Option key='-1'>全部</Option>);
//  //industries.map((t) => {
//  //  return options.push(<Option key={t.id}>{t.industryName}</Option>);
//  //})
//
//  return (
//    <Select {...params} style={{width: 325}} value={industryName}>
//      {
//        industries.map((t) => {
//          return (<Option key={t.id}>{t.industryName}</Option>);
//        })
//      }
//    </Select>
//  )
//}

//Industry.propTypes = {};
//
//export default connect(mapStateToProps, mapDispatchToProps)(Industry);

export default Industry;
