import React from 'react';
import {connect} from 'dva';
import {Form as FM, Input, Select} from 'antd';
const FormItem = FM.Item;
const Option = Select.Option;

import {mapStateToProps, mapDispatchToProps} from '../../connect/AppConnect';

const Form = ({app, dispatch}) => {
  const params = {
    labelCol: {span: 6},
    wrapperCol: {span: 16}
  };

  const handleChange = (value) => {
    dispatch({type: 'app/setFormData', payload: {
      fd: 'fbFormData',
      key: 'status',
      value: Number(value)
    }});
  }

  const {createTime, content, phone, status} = app.fbFormData;


  return (
    <div>
      <FormItem {...params} label="反馈时间">
        <Input value={createTime} disabled={true}/>
      </FormItem>
      <FormItem {...params} label="问题详情">
        <Input type="textarea" value={content} disabled={true} autosize={{minRows: 2, maxRows: 6}}/>
      </FormItem>
      <FormItem {...params} label="电话号码">
        <Input value={phone} disabled={true}/>
      </FormItem>
      <FormItem {...params} label="状态">
        <Select name='status' value={`${status}`} style={{width: 120}} onChange={handleChange}>
          <Option value="1">已处理</Option>
          <Option value="0">未处理</Option>
        </Select>
      </FormItem>
    </div>
  )
}

Form.propTypes = {};

export default connect(mapStateToProps, mapDispatchToProps)(Form);
