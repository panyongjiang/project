import React from 'react';

class Editor extends React.Component {
  componentDidMount() {
    const kEditor = K.create('textarea[name="content"]', {
      resizeType: 1,
      allowPreviewEmoticons: false,
      allowImageUpload: false,
      showLocal: false,
      items: [
        'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
        'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
        'insertunorderedlist', '|', 'emoticons', 'image', 'link']
    });

    this.props.dispatch({type: 'app/setAppParams', payload: {kEditor}});
  }
  render() {
    return (
      <textarea name="content" defaultValue='' style={{visibility: 'hidden'}}/>
    );
  }
}

export default Editor;
