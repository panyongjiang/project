package com.uway.mobile.domain;

import java.util.List;

public class Strategy {

	private int id;
	private String type;
	private String name;
	private String status;
	private String secCheatWebsite;
	private String secShamAdv;
	private String secSexyInfo;
	private String secHarmProgram;
	private String secTortContent;
	private String secIllegalGamble;
	private String secIllegalSite;
	private String secPhishingSite;
	private String secSexySite;
	private String apnName;
	private String apnType;
	private String apnProxy;
	private int apnPort;
	private String vpnIp;
	private int vpnPort;
	private String vpnAuth;
	private int enable;
	private int createUser;
	private List<BlackWhiteList> bList;//黑名单
	private List<BlackWhiteList> wList;//白名单
	private String optSeq;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSecCheatWebsite() {
		return secCheatWebsite;
	}

	public void setSecCheatWebsite(String secCheatWebsite) {
		this.secCheatWebsite = secCheatWebsite;
	}

	public String getSecShamAdv() {
		return secShamAdv;
	}

	public void setSecShamAdv(String secShamAdv) {
		this.secShamAdv = secShamAdv;
	}

	public String getSecSexyInfo() {
		return secSexyInfo;
	}

	public void setSecSexyInfo(String secSexyInfo) {
		this.secSexyInfo = secSexyInfo;
	}

	public String getSecHarmProgram() {
		return secHarmProgram;
	}

	public void setSecHarmProgram(String secHarmProgram) {
		this.secHarmProgram = secHarmProgram;
	}

	public String getSecTortContent() {
		return secTortContent;
	}

	public void setSecTortContent(String secTortContent) {
		this.secTortContent = secTortContent;
	}

	public String getSecIllegalGamble() {
		return secIllegalGamble;
	}

	public void setSecIllegalGamble(String secIllegalGamble) {
		this.secIllegalGamble = secIllegalGamble;
	}

	public String getSecIllegalSite() {
		return secIllegalSite;
	}

	public void setSecIllegalSite(String secIllegalSite) {
		this.secIllegalSite = secIllegalSite;
	}

	public String getSecPhishingSite() {
		return secPhishingSite;
	}

	public void setSecPhishingSite(String secPhishingSite) {
		this.secPhishingSite = secPhishingSite;
	}

	public String getSecSexySite() {
		return secSexySite;
	}

	public void setSecSexySite(String secSexySite) {
		this.secSexySite = secSexySite;
	}

	public String getApnName() {
		return apnName;
	}

	public void setApnName(String apnName) {
		this.apnName = apnName;
	}

	public String getApnType() {
		return apnType;
	}

	public void setApnType(String apnType) {
		this.apnType = apnType;
	}

	public String getApnProxy() {
		return apnProxy;
	}

	public void setApnProxy(String apnProxy) {
		this.apnProxy = apnProxy;
	}

	public int getApnPort() {
		return apnPort;
	}

	public void setApnPort(int apnPort) {
		this.apnPort = apnPort;
	}

	public String getVpnIp() {
		return vpnIp;
	}

	public void setVpnIp(String vpnIp) {
		this.vpnIp = vpnIp;
	}

	public int getVpnPort() {
		return vpnPort;
	}

	public void setVpnPort(int vpnPort) {
		this.vpnPort = vpnPort;
	}

	public String getVpnAuth() {
		return vpnAuth;
	}

	public void setVpnAuth(String vpnAuth) {
		this.vpnAuth = vpnAuth;
	}

	public int getCreateUser() {
		return createUser;
	}

	public void setCreateUser(int createUser) {
		this.createUser = createUser;
	}

	public List<BlackWhiteList> getbList() {
		return bList;
	}

	public void setbList(List<BlackWhiteList> bList) {
		this.bList = bList;
	}

	public List<BlackWhiteList> getwList() {
		return wList;
	}

	public void setwList(List<BlackWhiteList> wList) {
		this.wList = wList;
	}

	public int getEnable() {
		return enable;
	}

	public void setEnable(int enable) {
		this.enable = enable;
	}

	public String getOptSeq() {
		return optSeq;
	}

	public void setOptSeq(String optSeq) {
		this.optSeq = optSeq;
	}

}
