package com.uway.mobile.domain;

import java.sql.Timestamp;

public class Certificate {
	/* 证书id */
	private Integer id;
	/* 证书名称  */
	private String cerName;
	/* 证书存储地址  */
	private String cerUrl;
	/* 绑定的子域名id  */
	private String siteSonId;
	/* 上传人员id */
	private long createUser;
	/* 创建时间  */
	private Timestamp createTime;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCerName() {
		return cerName;
	}
	public void setCerName(String cerName) {
		this.cerName = cerName;
	}
	public String getCerUrl() {
		return cerUrl;
	}
	public void setCerUrl(String cerUrl) {
		this.cerUrl = cerUrl;
	}
	public String getSiteSonId() {
		return siteSonId;
	}
	public void setSiteSonId(String siteSonId) {
		this.siteSonId = siteSonId;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public long getCreateUser() {
		return createUser;
	}
	public void setCreateUser(long createUser) {
		this.createUser = createUser;
	}
	
	public static String pem="-----BEGIN CERTIFICATE-----"+
"MIIKUDCCCTigAwIBAgIMFJSJv/yw//KC5nNrMA0GCSqGSIb3DQEBCwUAMGIxCzAJ"+
"BgNVBAYTAkJFMRkwFwYDVQQKExBHbG9iYWxTaWduIG52LXNhMTgwNgYDVQQDEy9H"+
"bG9iYWxTaWduIEV4dGVuZGVkIFZhbGlkYXRpb24gQ0EgLSBTSEEyNTYgLSBHMzAe"+
"Fw0xNzAzMjIwNjQyMDJaFw0xODAzMTUxMDQyMjNaMIIBTjEdMBsGA1UEDwwUUHJp"+
"dmF0ZSBPcmdhbml6YXRpb24xGzAZBgNVBAUTEjkxNTMwMTAzMzIzMDI2MDk2SDET"+
"MBEGCysGAQQBgjc8AgEDEwJDTjEXMBUGCysGAQQBgjc8AgEBDAbmmIbmmI4xFzAV"+
"BgsrBgEEAYI3PAIBAgwG5LqR5Y2XMQswCQYDVQQGEwJDTjEPMA0GA1UECAwG5LqR"+
"5Y2XMQ8wDQYDVQQHDAbmmIbmmI4xJDAiBgNVBAkMG+ebmOm+meWMuuS4nOS6jOeO"+
"r+i3rzUxN+WPtzESMBAGA1UECwwJ56CU5Y+R6YOoMUUwQwYDVQQKDDzkuK3lm73n"+
"p7vliqjpgJrkv6Hpm4blm6LkupHljZfmnInpmZDlhazlj7jkupLogZTnvZHliIbl"+
"hazlj7gxGTAXBgNVBAMTEHd3dy50b3B5dW5uYW4uY24wggEiMA0GCSqGSIb3DQEB"+
"AQUAA4IBDwAwggEKAoIBAQDeHOtCSWmf5h5egbGoLmFFjkFBAS9+kDM+U619Bo4w"+
"AEtSHDI6eIvVIN7sFre+gPHZwK9Mn/g+H+joHzKLceaJxRxakCHb1UnC4LmSvQhl"+
"uGISNuDqmmegC1/sWyb19ZNd2s02Rmr92wm4wIMobrxiA3/m3laDlz+K/7ef08m0"+
"GLOWkeScp/utNzc7pJ50BmNHAKnFjERJX3Uhlqj5/x3k/IL3aO3w7j1jhe6WxYIG"+
"++elEdmLXd5BGSnLvrGHiVKR6uC3bfZ+NMnYpdDranrux3u0O6OqGkPVCKrTa15T"+
"KrCDTmTOoLORpTWZPhL3RcChGvp3CeAHEitJZLIuzcBxAgMBAAGjggYWMIIGEjAO"+
"BgNVHQ8BAf8EBAMCBaAwgZYGCCsGAQUFBwEBBIGJMIGGMEcGCCsGAQUFBzAChjto"+
"dHRwOi8vc2VjdXJlLmdsb2JhbHNpZ24uY29tL2NhY2VydC9nc2V4dGVuZHZhbHNo"+
"YTJnM3IzLmNydDA7BggrBgEFBQcwAYYvaHR0cDovL29jc3AyLmdsb2JhbHNpZ24u"+
"Y29tL2dzZXh0ZW5kdmFsc2hhMmczcjMwVQYDVR0gBE4wTDBBBgkrBgEEAaAyAQEw"+
"NDAyBggrBgEFBQcCARYmaHR0cHM6Ly93d3cuZ2xvYmFsc2lnbi5jb20vcmVwb3Np"+
"dG9yeS8wBwYFZ4EMAQEwCQYDVR0TBAIwADBFBgNVHR8EPjA8MDqgOKA2hjRodHRw"+
"Oi8vY3JsLmdsb2JhbHNpZ24uY29tL2dzL2dzZXh0ZW5kdmFsc2hhMmczcjMuY3Js"+
"MIICYgYDVR0RBIICWTCCAlWCEHd3dy50b3B5dW5uYW4uY26CDmRzai55bmljaXR5"+
"LmNugg9pbWcwMS5uZXR2YW4uY26CD2ltZzAyLm5ldHZhbi5jboIPaW1nMDMubmV0"+
"dmFuLmNughBmaWxlMDEubmV0dmFuLmNughBmaWxlMDIubmV0dmFuLmNughBmaWxl"+
"MDMubmV0dmFuLmNuggxrZi5uZXR2YW4uY26CEGNhaXl1bnlvdXBpbi5jb22CFXJl"+
"YWwuY2FpeXVueW91cGluLmNvbYIUcmVzLmNhaXl1bnlvdXBpbi5jb22CGXRlc3Rj"+
"eXlwLmNhaXl1bnlvdXBpbi5jb22CE3d3dy5hbmV3c2NlbmVyeS5jb22CDmFvaS55"+
"bmljaXR5LmNughNwYXNzcG9ydC55bmljaXR5LmNughJzc290ZXN0LnluaWNpdHku"+
"Y26CDnljcy55bmljaXR5LmNugg53d3cueW5pY2l0eS5jboIOc21zLnluaWNpdHku"+
"Y26CEXJlcG9ydC55bmljaXR5LmNugg5tb24ueW5pY2l0eS5jboIRdW5pYXJjLnlu"+
"aWNpdHkuY26CEnVuaXRlc3QueW5pY2l0eS5jboITdW5pY2xvdWQueW5pY2l0eS5j"+
"boINYWMueW5pY2l0eS5jboIOYWN0LnluaWNpdHkuY26CEGltZy50b3B5dW5uYW4u"+
"Y26CEmZpbGVzLnRvcHl1bm5hbi5jboIMcXVudWppYW5nLmNugg5lcmt1YWlsaWZl"+
"LmNvbYISd3d3LmVya3VhaWxpZmUuY29tggx0b3B5dW5uYW4uY24wHQYDVR0lBBYw"+
"FAYIKwYBBQUHAwEGCCsGAQUFBwMCMB0GA1UdDgQWBBRUAjlW3WLnDAm12mN2DUNJ"+
"fGk01TAfBgNVHSMEGDAWgBTds+dtqC7oxU5uz3TmdTyUFc7oHTCCAfcGCisGAQQB"+
"1nkCBAIEggHnBIIB4wHhAHYAVhQGmi/XwuzT9eG9RLI+x0Z2ubyZEVzA75SYVdaJ"+
"0N0AAAFa9MBBAQAABAMARzBFAiBFT7399sLSgBPkidAELduHCkXH1Ub5EBgU9pYd"+
"5RlLZAIhAMcN/xNvSXyinp5cObBFr7cLKDhEcOEZjeH9MitSmfMoAHYA3esdK3oN"+
"T6Ygi4GtgWhwfi6OnQHVXIiNPRHEzbbsvswAAAFa9MBB/wAABAMARzBFAiEArwhZ"+
"tXoBsHLph81tUts2cdJ0Rf2IUXyiC12bH/vHzjwCIDn9f5VAt0y1Q62gqAGCVEUU"+
"/TDxPSkTgkzGF3bDmaCLAHcApLkJkLQYWBSHuxOizGdwCjw1mAT5G9+443fNDsgN"+
"3BAAAAFa9MBAxwAABAMASDBGAiEAgWfOpXO9XXZ3REX9KT67UVXLE+z9rOSTQZTn"+
"LbbwzqsCIQDsmNHh3h44cL4hVa/nVS3WCBqPwPJMkreyGHIIzt6MjAB2AO5Lvbd1"+
"zmC64UJpH6vhnmajD35fsHLYgwDEe4l6qP3LAAABWvTAQ/MAAAQDAEcwRQIgDTt/"+
"jjiqQrjenR21Uw1H/0AR2Mj/Hy149opXRkL0k3sCIQCJdjbZEaelv1JRsj1LJ4bp"+
"ipOCVh267xDnINUY549pDDANBgkqhkiG9w0BAQsFAAOCAQEAkWn6Mlsh62B9KfTM"+
"BDyhI23ljIkuBXd2vjEesOBgkrwjsXCOsF2ppNKUCJ4qHrvmjTOI5UWAUfkyDrO6"+
"LhHYzeyYJWdehByFsryQfdqZYNy3AgnkZLEWbHRNBgtQvPKKZzmUD1QkHqhdSNUj"+
"4KFSQNNGiuI+tQOcyvQhqWeUPBffQq92x+ThaJB4fuF10megAyKULUglYxEsmPm3"+
"H1BaS+BvBmIgp38dNla2qcb3lLJomQ264aH5kPzfS3m8oPNJzssl2wO4o6FNXo8U"+
"Z7geHPppGiIq939DotCdgc51eYjA7NpJEbkwfBAqLlWLrTlSDf4Ximf6UAD/Qv/E"+
"InspKg=="+
"-----END CERTIFICATE-----";

}
