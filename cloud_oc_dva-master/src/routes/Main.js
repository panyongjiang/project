import React from 'react';
import {Spin} from 'antd';
import {connect} from 'dva';

import {mapStateToProps} from '../components/connect/MainConnect';

import Header from '../components/main/Header';
import Sider from '../components/main/Sider';
import Content from '../components/main/Content';
import Footer from '../components/main/Footer';

import ResetPassword from '../components/system/reset-password';

import './Main.css';


const Main = ({main}) => {
  console.log('Main', main)
  const {menuNames, spinning, tip, currentRoute} = main;
  menuNames.length > 0 && (!menuNames.some((m) => {
    return m == currentRoute;
  }) && (location.href = '/404.html'));

  const spinParams = {spinning, tip};

  return (
    <div className='theme-blue'>
      <Spin {...spinParams}>
        <Header />
        <Sider />
        <Content />
        <Footer />
        <div>
          <ResetPassword />
        </div>
      </Spin>
    </div>
  );
}

Main.propTypes = {};

export default connect(mapStateToProps)(Main);
