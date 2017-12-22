import React from 'react';
import {connect} from 'dva';
import {Form, Input, Select, Cascader} from 'antd';
const FormItem = Form.Item;
const Option = Select.Option;

import {getCookie} from '../../../utils';
const {UNAME_KEY} = require('../../../config/cookies.json');

import Industry from './Industry';
import City from './City';

import {mapStateToProps, mapDispatchToProps} from '../../connect/ProductConnect';

import styles from '../../component.css';

//class ServerForm extends React.Component {
//  render() {
//    const {formItem, decorator, product} = this.props;
//    const {umFormData, industries} = product;
//    const {industryName, business, company, position, url,
//      provinceName, cityName, address, phone, person, email} = umFormData;
//
//    return (
//      <Form>
//        <FormItem {...formItem} label="用户名">
//          <Input value={getCookie(UNAME_KEY)} disabled={true}/>
//        </FormItem>
//        <FormItem {...formItem} label="行业">
//          {decorator('industryName', {
//            initialValue: industryName,
//            rules: [{
//              required: true,
//              message: '请选择行业'
//              }]
//          })(
//            <Select style={{width: 325}}>
//              {
//                industries.map((t) => {
//                  return (<Option key={t.id}>{t.industryName}</Option>);
//                })
//              }
//            </Select>
//          )}
//        </FormItem>
//        <FormItem {...formItem} label="主营业务">
//          {decorator('business', {
//            initialValue: business,
//            rules: [{
//              required: true,
//              message: '请输入主营业务'
//            }]
//          })(
//            <Input placeholder="请输入主营业务"/>
//          )}
//        </FormItem>
//      </Form>
//    )
//  }
//}
//
//ServerForm.defaultProps = {
//  formItem: {
//    labelCol: {span: 6},
//    wrapperCol: {span: 16},
//    className: styles.formItemMarginBottom
//  }
//};

const ServerForm = (props) => {
  console.log('ServerForm props', props);

  return (
    <div></div>
  );
};


//const ServerForm = ({product, dispatch}) => {
//  const params = {
//    labelCol: {span: 6},
//    wrapperCol: {span: 16},
//    className: styles.formItemMarginBottom
//  };
//  const handleChange = (e) => {
//    const dom = e.target;
//    const key = dom.getAttribute('name');
//    const value = dom.value;
//    dispatch({type: 'product/setFormData', payload: {fd: 'umFormData', key, value}});
//  }
//  const {industryName, industries, business, company, position, url,
//    provinceName, cityName, address, phone, person, email} = product.umFormData;
//
//  return (
//    <div>
//      <FormItem {...params} label="用户名">
//        <Input value={getCookie(UNAME_KEY)} onChange={handleChange} disabled={true}/>
//      </FormItem>
//      <FormItem {...params} label="行业">
//        <Industry />
//      </FormItem>
//      <FormItem {...params} label="主营业务">
//        <Input value={business} onChange={handleChange} name="business"/>
//      </FormItem>
//      <FormItem {...params} label="工作单位">
//        <Input value={company} onChange={handleChange} name="company"/>
//      </FormItem>
//      <FormItem {...params} label="职位/职责">
//        <Input value={position} onChange={handleChange} name="position"/>
//      </FormItem>
//      <FormItem {...params} label="网址">
//        <Input value={url} onChange={handleChange} name="url"/>
//      </FormItem>
//      <FormItem {...params} label="所属地区">
//        <City />
//      </FormItem>
//      <FormItem {...params} label="详细地址">
//        <Input value={address} onChange={handleChange} name="address"/>
//      </FormItem>
//      <FormItem {...params} label="联系电话">
//        <Input value={phone} onChange={handleChange} name="phone"/>
//      </FormItem>
//      <FormItem {...params} label="联系人">
//        <Input value={person} onChange={handleChange} name="person"/>
//      </FormItem>
//      <FormItem {...params} label="邮箱">
//        <Input value={email} onChange={handleChange} name="email"/>
//      </FormItem>
//    </div>
//  )
//}

//ServerForm.propTypes = {};

//export default ServerForm;


export default connect(mapStateToProps, mapDispatchToProps)(ServerForm);
