import React from 'react';
import {connect} from 'dva';
import {Form as FM, Input, Select} from 'antd';
const FormItem = FM.Item;
const Option = Select.Option;

import {mapStateToProps, mapDispatchToProps} from '../../connect/AppConnect';

import styles from '../../component.css';

const Form = ({app, dispatch}) => {
  const params = {
    labelCol: {span: 8},
    wrapperCol: {span: 12},
    className: styles.formItemMarginBottom
  };

  const handleChange = (value) => {
    dispatch({type: 'app/setFormData', payload: {
      fd: 'taFormData',
      key: 'trialStatue',
      value: Number(value)
    }});
  }
  const {createTime, user_name, phone, email, person,
    company, position, address, url, trial1, trialStatue} = app.taFormData;

  return (
    <div>
      <FormItem {...params} label="申请时间">
        <Input value={createTime} disabled={true}/>
      </FormItem>
      <FormItem {...params} label="账号">
        <Input value={user_name} disabled={true}/>
      </FormItem>
      <FormItem {...params} label="电话">
        <Input value={phone} disabled={true}/>
      </FormItem>
      <FormItem {...params} label="邮箱">
        <Input value={email} disabled={true}/>
      </FormItem>
      <FormItem {...params} label="姓名">
        <Input value={person} disabled={true}/>
      </FormItem>
      <FormItem {...params} label="所属集团">
        <Input value={company} disabled={true}/>
      </FormItem>
      <FormItem {...params} label="工作职责">
        <Input value={position} disabled={true}/>
      </FormItem>
      <FormItem {...params} label="所在地区">
        <Input value={address} disabled={true}/>
      </FormItem>
      <FormItem {...params} label="网址">
        <Input type="textarea" value={url} disabled={true} autosize={{minRows: 2, maxRows: 4}}/>
      </FormItem>
      <FormItem {...params} label="感兴趣的产品">
        <Input type="textarea" value={trial1} disabled={true} autosize={{minRows: 2, maxRows: 4}}/>
      </FormItem>
      <FormItem {...params} label="状态">
        <Select value={`${trialStatue}`} style={{width: 245}} onChange={handleChange}>
          <Option value="1">已处理</Option>
          <Option value="0">处理中</Option>
        </Select>
      </FormItem>
    </div>
  )
}

Form.propTypes = {};

export default connect(mapStateToProps, mapDispatchToProps)(Form);
