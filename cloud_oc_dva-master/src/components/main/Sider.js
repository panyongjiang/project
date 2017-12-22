import React from 'react';
import {connect} from 'dva';
import {Link} from 'dva/router';
import {mapStateToProps, mapDispatchToProps} from '../connect/MainConnect';

const specialMenus = require('../../config/special.menus.json');

const Sider = ({main, dispatch}) => {
  return (
    <div className="sidebar-nav">
      <ul>
        {
          main.menus.map((m) => {
            if (!m.root) {
              return (
                <li key={m.id}>
                  <Link to={`/${m.link}`} className="nav-header">
                    <i className={`fa fa-fw ${m.fa}`}></i>
                    {m.txt}
                  </Link>
                </li>
              )
            } else {
              return (
                <div key={m.id}>
                  <li>
                    <a data-target=".dashboard-menu" className="nav-header" data-toggle="collapse">
                      <i className={`fa fa-fw ${m.fa}`}></i>
                      {m.txt}
                      <i className="fa fa-collapse"></i>
                    </a>
                  </li>
                  <li>
                    <ul className="dashboard-menu nav nav-list collapse in">
                      {
                        m.children.map((c) => {
                          let exist = specialMenus.some((m) => {
                            return m == c.link;
                          });

                          if (!exist) {
                            return (
                              <li key={c.id}>
                                <Link to={`/${c.link}`}>
                                  <span className="fa fa-caret-right"></span>
                                  {c.txt}
                                </Link>
                              </li>
                            )
                          }
                        })
                      }
                    </ul>
                  </li>
                </div>
              )
            }
          })
        }
      </ul>
    </div>
  );
};

Sider.propTypes = {};

export default connect(mapStateToProps, mapDispatchToProps)(Sider);

