import {message} from 'antd';

/**
 * 全局消息组件
 */

const Config = {
    top: 0,
    duration: 1
};

const Message = {
    success: (content, duration, onclose) => {
        message.config(Config);
        message.success(content, duration, onclose);
    },
    error: (content, duration, onclose) => {
        message.config(Config);
        message.error(content.length > 0 ? content : '连接服务器异常', duration, onclose);
    },
    info: (content, duration, onclose) => {
        message.config(Config);
        message.info(content, duration, onclose);
    },
    warning: (content, duration, onclose) => {
        message.config(Config);
        message.warning(content, duration, onclose);
    },
    warn: (content, duration, onclose) => {
        message.config(Config);
        message.warn(content, duration, onclose);
    },
    loading: (content, duration, onclose) => {
        message.config(Config);
        message.loading(content, duration, onclose);
    }
}

export default Message;
