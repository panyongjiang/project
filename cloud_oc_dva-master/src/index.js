import dva from 'dva';
import './index.css';

// 1. Initialize
const app = dva({
  onError(e, dispatch) {
    console.log('error', e.message);
  }
});

// 2. Plugins
// app.use({});

// 3. Model
app.model(require('./models/Auth'));
app.model(require('./models/Main'));
app.model(require('./models/SysConfig'));
app.model(require('./models/App'));
app.model(require('./models/Product'));

// 4. Router
app.router(require('./router'));

// 5. Start
app.start('#root');
