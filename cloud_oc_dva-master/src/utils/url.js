const {protocol,
  host, port, projectName,
  imgHost, imgPath} = require('../config/server.json');

export const server = `${protocol}://${host}:${port}${projectName}`;
export const imgServer = `${protocol}://${imgHost}${imgPath}`;

