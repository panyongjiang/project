package com.uway.mobile.domain;

public class Param_hengtong {
	
	private String RouterUUID;  //设备ID
	private String CommandType; //指令参数
	private String Command;     //指令参数
	private String CmdStatus;   //指令处理结果
	private String Response ;   //返回结果
	//wifi设置  type为 WIFI_SETING
	private Integer Enable;      //wifi状态  0为关闭 1为开启
	private String SSID ;       //长度为1~32
	private String Encryption;  //加密方式，取值范围为, none,psk,psk2,psk+psk2
	private String Key;         //加密方式为none,key为空,密码长度为8~63
	private Integer Channel;     //信道,2.4Gwifi和2.4G guest wifi取值范围为0~13,默认值为0 5G取值范围:只能是以下其中之一,149,153,157,161,165
	private String Txpower;     //信号强度，取值范围为0-100
	private Integer Hidessid;    //隐藏ssid。0为不隐藏，1为隐藏
	private Integer Htmode;      //HT模式。0为20/40MHZ，1为40MHZ, 2为5G 80MHZ
    //黑白名单设置 type 为MAC_FILTER_SETTING
	private String Policy;       //0：禁止；1：Allow；2. Deny 
	private String Action;       //0添加 1删除
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
	private String Port;       //pptp or l2tp,ipsec
	private String UserName;    //用户名
	private String Password;    //密码
	private Integer CPU;        //CPU占用
	private String Device_Model; //设备型号
	private Integer Memory_Usage;
	private Integer Uptime;     //在线时长
	//APN设置
	private String Configfile_Name; 
	private String Access_Name;
	private String Auth_Mode;
	//LAN口设置
	private String ip;  //LAN口 ip
	private String start; //起始ip
	private String end;   //终止ip
	private Integer Dhcp_Switch; //开关
	private Integer Lease_Time; //LAN口时间
	
	
	public String getRouterUUID() {
		return RouterUUID;
	}
	public void setRouterUUID(String routerUUID) {
		RouterUUID = routerUUID;
	}
	public String getCommandType() {
		return CommandType;
	}
	public void setCommandType(String commandType) {
		CommandType = commandType;
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
	public String getCommand() {
		return Command;
	}
	public void setCommand(String command) {
		Command = command;
	}
	public Integer getEnable() {
		return Enable;
	}
	public void setEnable(Integer enable) {
		Enable = enable;
	}
	public String getSSID() {
		return SSID;
	}
	public void setSSID(String sSID) {
		SSID = sSID;
	}
	public String getEncryption() {
		return Encryption;
	}
	public void setEncryption(String encryption) {
		Encryption = encryption;
	}
	public String getKey() {
		return Key;
	}
	public void setKey(String key) {
		Key = key;
	}
	public Integer getChannel() {
		return Channel;
	}
	public void setChannel(Integer channel) {
		Channel = channel;
	}
	public String getTxpower() {
		return Txpower;
	}
	public void setTxpower(String txpower) {
		Txpower = txpower;
	}
	public Integer getHidessid() {
		return Hidessid;
	}
	public void setHidessid(Integer hidessid) {
		Hidessid = hidessid;
	}
	public Integer getHtmode() {
		return Htmode;
	}
	public void setHtmode(Integer htmode) {
		Htmode = htmode;
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
	public String getPort() {
		return Port;
	}
	public void setPort(String port) {
		Port = port;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUsername(String userName) {
		UserName = userName;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	public Integer getCPU() {
		return CPU;
	}
	public void setCPU(Integer cPU) {
		CPU = cPU;
	}
	public String getDevice_Model() {
		return Device_Model;
	}
	public void setDevice_Model(String device_Model) {
		Device_Model = device_Model;
	}
	public Integer getMemory_Usage() {
		return Memory_Usage;
	}
	public void setMemory_Usage(Integer memory_Usage) {
		Memory_Usage = memory_Usage;
	}
	public Integer getUptime() {
		return Uptime;
	}
	public void setUptime(Integer uptime) {
		Uptime = uptime;
	}
	public String getConfigfile_Name() {
		return Configfile_Name;
	}
	public void setConfigfile_Name(String configfile_Name) {
		Configfile_Name = configfile_Name;
	}
	public String getAccess_Name() {
		return Access_Name;
	}
	public void setAccess_Name(String access_Name) {
		Access_Name = access_Name;
	}
	public String getAuth_Mode() {
		return Auth_Mode;
	}
	public void setAuth_Mode(String auth_Mode) {
		Auth_Mode = auth_Mode;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	public Integer getDhcp_Switch() {
		return Dhcp_Switch;
	}
	public void setDhcp_Switch(Integer dhcp_Switch) {
		Dhcp_Switch = dhcp_Switch;
	}
	public Integer getLease_Time() {
		return Lease_Time;
	}
	public void setLease_Time(Integer lease_Time) {
		Lease_Time = lease_Time;
	}
	public String getAction() {
		return Action;
	}
	public void setAction(String action) {
		Action = action;
	}
	
	
}
