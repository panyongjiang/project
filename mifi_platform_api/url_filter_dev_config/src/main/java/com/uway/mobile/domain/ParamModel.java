package com.uway.mobile.domain;

public class ParamModel {
	
	private String deviceid;  //设备ID
	private String CommandType; //指令参数
	private String Command;     //指令参数
	private String CmdStatus;   //指令处理结果
	private String Response ;   //返回结果
	//wifi设置  type为 WIFI_SETING
	private String enable;      //wifi状态  0为关闭 1为开启
	private String ssid ;       //长度为1~32
	private String security;  //加密方式，取值范围为, none,psk,psk2,psk+psk2
	private String passwd;         //加密方式为none,key为空,密码长度为8~63
	private String channel;     //信道,2.4Gwifi和2.4G guest wifi取值范围为0~13,默认值为0 5G取值范围:只能是以下其中之一,149,153,157,161,165
	private String Txpower;     //信号强度，取值范围为0-100
	private String hidden;    //隐藏ssid。0为不隐藏，1为隐藏
	private String bw;      //HT模式。0为20/40MHZ，1为40MHZ, 2为5G 80MHZ
    //黑白名单设置 type 为MAC_FILTER_SETTING
	private String Policy;       //0：禁止；1：Allow；2. Deny 
	private String Macaddr;      //Mac地址
	//终端名称修改及限速 type为 CLIENT _SETTING
	private String Client_Mac;   //终端Mac
	private String Client_IP ;   //终端IP
	private String Client_Nickname; //终端昵称
	private String Up_Rate;     //0不允许上网，-1不限制
	private String Down_Rate;   //下载限速 百分比 0~1之间
	//网址黑名单设置 type为 BLACK_URL _SETTING
	private String Url_Lists;   //多个URL使用空格分隔
	//VPN设置 type为 VPN _SETTING
	private String Server;      //IP或者域名
	private String Proto;       //pptp or l2tp,ipsec
	private String Username;    //用户名
	private String Password;    //密码
	public String getDeviceid() {
		return deviceid;
	}
	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}
	public String getCommandType() {
		return CommandType;
	}
	public void setCommandType(String commandType) {
		CommandType = commandType;
	}
	public String getCommand() {
		return Command;
	}
	public void setCommand(String command) {
		Command = command;
	}
	public String getCmdStatus() {
		return CmdStatus;
	}
	public void setCmdStatus(String cmdStatus) {
		CmdStatus = cmdStatus;
	}
	public String getResponse() {
		return Response;
	}
	public void setResponse(String response) {
		Response = response;
	}
	public String getEnable() {
		return enable;
	}
	public void setEnable(String enable) {
		this.enable = enable;
	}
	public String getSsid() {
		return ssid;
	}
	public void setSsid(String ssid) {
		this.ssid = ssid;
	}
	public String getSecurity() {
		return security;
	}
	public void setSecurity(String security) {
		this.security = security;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getTxpower() {
		return Txpower;
	}
	public void setTxpower(String txpower) {
		Txpower = txpower;
	}
	public String getHidden() {
		return hidden;
	}
	public void setHidden(String hidden) {
		this.hidden = hidden;
	}
	public String getBw() {
		return bw;
	}
	public void setBw(String bw) {
		this.bw = bw;
	}
	public String getPolicy() {
		return Policy;
	}
	public void setPolicy(String policy) {
		Policy = policy;
	}
	public String getMacaddr() {
		return Macaddr;
	}
	public void setMacaddr(String macaddr) {
		Macaddr = macaddr;
	}
	public String getClient_Mac() {
		return Client_Mac;
	}
	public void setClient_Mac(String client_Mac) {
		Client_Mac = client_Mac;
	}
	public String getClient_IP() {
		return Client_IP;
	}
	public void setClient_IP(String client_IP) {
		Client_IP = client_IP;
	}
	public String getClient_Nickname() {
		return Client_Nickname;
	}
	public void setClient_Nickname(String client_Nickname) {
		Client_Nickname = client_Nickname;
	}
	public String getUp_Rate() {
		return Up_Rate;
	}
	public void setUp_Rate(String up_Rate) {
		Up_Rate = up_Rate;
	}
	public String getDown_Rate() {
		return Down_Rate;
	}
	public void setDown_Rate(String down_Rate) {
		Down_Rate = down_Rate;
	}
	public String getUrl_Lists() {
		return Url_Lists;
	}
	public void setUrl_Lists(String url_Lists) {
		Url_Lists = url_Lists;
	}
	public String getServer() {
		return Server;
	}
	public void setServer(String server) {
		Server = server;
	}
	public String getProto() {
		return Proto;
	}
	public void setProto(String proto) {
		Proto = proto;
	}
	public String getUsername() {
		return Username;
	}
	public void setUsername(String username) {
		Username = username;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	
	
	
	
	
	
	
}
