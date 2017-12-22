import React from 'react';
import {connect} from 'dva';
import {Form as FM, Input} from 'antd';
const FormItem = FM.Item;

import Image from './Image';
import Editor from './Editor';
import Category from './Category';

import {mapStateToProps, mapDispatchToProps} from '../../connect/AppConnect';

import styles from '../../component.css';

const Form = ({app, dispatch}) => {
  const params = {
    labelCol: {span: 4},
    wrapperCol: {span: 16},
    className: styles.formItemMarginBottom
  };

  const handleChange = (e) => {
    const dom = e.target;
    const key = dom.getAttribute('name');
    const value = dom.value;
    dispatch({type: 'app/setFormData', payload: {fd: 'formData', key, value}});
    dispatch({type: 'app/setFormData', payload: {fd: 'formData', key: 'content', value: app.kEditor.html()}});
  }

  const {title, sub_title, content, remark, description, author} = app.formData;

  setTimeout(() => {
    app.kEditor && app.kEditor.html(content);
  }, 0);

  return (
    <div>
      <FormItem {...params} label='标题'>
        <Input name='title' value={title} placeholder='文章标题' onChange={handleChange}/>
      </FormItem>
      <FormItem {...params} label='副标题'>
        <Input name='sub_title' value={sub_title} placeholder='文章副标题' onChange={handleChange}/>
      </FormItem>
      <FormItem {...params} label='图片文件'>
        <Image />
      </FormItem>
      <FormItem {...params} label='内容'>
        <Editor dispatch={dispatch} />
      </FormItem>
      <FormItem {...params} label='文章类型'>
        <Category />
      </FormItem>
      <FormItem {...params} label='备注'>
        <Input name='remark' type="textarea" rows={2} value={remark} placeholder='备注'
               onChange={handleChange}/>
      </FormItem>
      <FormItem {...params} label='描述'>
        <Input name='description' type="textarea" rows={2} value={description} placeholder='描述'
               onChange={handleChange}/>
      </FormItem>
      <FormItem {...params} label='作者'>
        <Input name='author' value={author} placeholder='作者' onChange={handleChange}/>
      </FormItem>
    </div>
  )
}

Form.propTypes = {};

export default connect(mapStateToProps, mapDispatchToProps)(Form);
