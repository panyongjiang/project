import React from 'react';
import {connect} from 'dva';
import {Breadcrumb as BC, Icon} from 'antd';
const BCItem = BC.Item;

import {mapStateToProps} from '../connect/MainConnect';

const Breadcrumb = ({main}) => {
  const {preLevel, curLevel} = handleBreadcrumb(main);

  return (
    <BC>
      <BCItem>
        <Icon type="user"/>
        <span>{preLevel}</span>
      </BCItem>
      <BCItem>
        {curLevel}
      </BCItem>
    </BC>
  )
}

function handleBreadcrumb({menus, currentRoute}) {
  let bcObj = {};
  menus.map((m) => {
    if (m.root != 0) {
      m.children.map((c) => {
        if (c.link == currentRoute) {
          bcObj.preLevel = m.txt;
          bcObj.curLevel = c.txt;
        }
      });
    }
  });
  return bcObj;
}

Breadcrumb.propTypes = {};

export default connect(mapStateToProps)(Breadcrumb);
