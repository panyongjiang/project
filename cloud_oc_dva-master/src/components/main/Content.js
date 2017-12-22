import React from 'react';
import {connect} from 'dva';

import {components} from '../../router.mapping';
import {mapStateToProps} from '../connect/MainConnect';
import Breadcrumb from './Breadcrumb';

// content部分最小高度 header、footer 50px border 2px
const minHeight = document.body.clientHeight - (50 + 30 + 1 + 1);

const Content = ({main}) => {
  return (
    <div className="content" style={{minHeight}}>
      <div className="header">
        <Breadcrumb />
      </div>
      <div className="main-content">
      {
        components[main.currentRoute] || (location.href = '/404.html')
      }
      </div>
    </div>
  );
};

Content.propTypes = {};

export default connect(mapStateToProps)(Content);

