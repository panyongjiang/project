package com.uway.mobile.common;

public class Constance {
	// 每页数据条数
	public static int PAGE_SIZE = 10;
	// 取数值最大的前几位
	public static int TOP_N = 5;

	// 按时间分组时分组个数
	public static int GROUP_SIZE = 6;
	// 返回参数定义
	// 成功
	public static int RESPONSE_SUCCESS = 200;
	// 内部错误
	public static int RESPONSE_INNER_ERROR = 500;
	// 参数为空
	public static int RESPONSE_PARAM_EMPTY = 501;
	// 用户不存在
	public static int RESPONSE_USER_ERROR = 502;
	// 主机不存在
	public static int RESPONSE_HOST_ERROR = 503;
	// 告警信息不存在
	public static int RESPONSE_ALARM_ERROR = 504;
	// 参数格式错误
	public static int RESPONSE_PARAM_ERROR = 505;
	// 注册时，用户已存在
	public static int RESPONSE_USER_EXISTED_ERROR = 506;
	// 冻结用户
	public static int RESPONSE_USER_FROZEN = 507;
	// 用户验证码错误
	public static int RESPONSE_USER_VERIFICATION_ERROR = 509;
	// 密码错误
	public static int RESPONSE_PWD_ERROR = 510;
	// 含有敏感词
	public static int RESPONSE_SENSITIVE_EXIST = 511;
	// 无权操作
	public static int RESPONSE_AUTH_ERROR = 520;

	// 默认密码
	public static String DEFAULT_PASSWORD = "123";
	// redis用户ID存储结构
	public static String REDIS_USER_PRE = "yn_mobile:user:";
	// redis存储用户信息
	public static String REDIS_USER_INFO = "yn_mobile:user_info:";
	// redis存储用户登录IP的信息
	public static String REDIS_LOGIN_IP = "yn_mobile:login_ip:";
	// redis存储相应的敏感词信息
	public static String REDIS_SENSITIVE_WORDS = "yn_mobile:sensitive:list_word";
	// 密码密钥
	public static String KEY = "YN@uway#mobile";

	public static int ARTICLE_BANNER_COUNT = 4;

	// 上传的app后缀名
	public static String APK_SUFFIX = ".apk";

	// api_id
	public static String WAF_API_ID = "58b666b8df224f93e86bc827";
	// api_key
	public static String WAF_API_KEY = "1173263aa38a40af832e63c47458a9f6";

	public static String ESINDEX = "cloud";

	public static String QUICK_KEY = "uway_quick_key";
}
