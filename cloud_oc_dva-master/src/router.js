import React from 'react';
import {Router, Route, Redirect} from 'dva/router';


import Index from './routes/Index';

function RouterConfig({ history }) {
  return (
    <Router history={history}>
      <Route path="/*" component={Index} />
    </Router>
  );
}

export default RouterConfig;
