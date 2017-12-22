import React from 'react';
const {year, company, remark} = require('../../config/source.json');

const Footer = () => {
  return (
    <footer>
      <p className="pull-right">
        <a href="http://www.portnine.com/bootstrap-themes" target="_blank">
          {remark}
        </a>
      </p>
      <p>© {year}&nbsp;
        <a href="http://www.portnine.com" target="_blank">{company}</a>
      </p>
    </footer>
  );
};

Footer.propTypes = {};

export default Footer;

