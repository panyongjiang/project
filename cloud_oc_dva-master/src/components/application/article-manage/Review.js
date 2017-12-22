import React from 'react';
import {connect} from 'dva';
import {Form as FM, Input} from 'antd';
const FormItem = FM.Item;

import Image from './Image';
import Editor from './Editor';
import Category from './Category';

import {mapStateToProps} from '../../connect/AppConnect';
import {imgServer} from '../../../utils/url';

import styles from './review.css';

const Review = ({app}) => {
  const {title, author, create_time, sub_title,
    remark, description, pic_id, content} = app.formData;

  setTimeout(() => {
    $('#content').html(content);
  }, 0);

  return (
    <div className={styles.previewBox}>
      <h2 className={styles['preview-h2']}>{title}</h2>
      <div className={styles['article-infos']}>
        <div className={styles['article-info']}>
          <span className={styles['article-name']}>{author}</span>
          <span className={styles['article-time']}>{create_time}</span>
        </div>
        <div className={styles['article-info']}>
          <span className={styles['article-tag']}>{sub_title}</span>
        </div>
        <div className={styles['blockquote']}>
          {remark}
        </div>
        <div className={styles['blockquote']}>
          <i className={styles['iquote']}></i>
          {description}
        </div>
        <div className={styles['article-detail']}>
          <p className={styles['image']}>
            <img className={styles['imgstyle']} src={`${imgServer}/${pic_id}`}/>
          </p>
          <div id='content' className={styles['article-content']}></div>
        </div>
      </div>
    </div>
  )
}

Review.propTypes = {};

export default connect(mapStateToProps)(Review);
