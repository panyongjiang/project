import React from 'react';
import styles from './Index.css';

import Login from './Login';
import Main from './Main';

import {getCookie} from '../utils';
const {SID_KEY} = require('../config/cookies.json');

const Index = () => {
  return (
    <div>
      {getCookie(SID_KEY) ? <Main /> : <Login />}
    </div>
  );
}

Index.propTypes = {};

export default Index;
