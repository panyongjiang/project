package com.uway.tasks.common;

public class Constant {
	// 每页数据条数
	public static int PAGE_SIZE = 15;
	// 主页每次新增主机卡片数量
	public static int BLOCK_SIZE = 12;
	// 主机状态
	public static short HOST_OFF_LINE = 3;
	public static short HOST_PROTECTED = 2;
	
	// 返回参数定义
	// 成功
	public static int RESPONSE_SUCCESS = 200;
	// 内部错误
	public static int RESPONSE_INNER_ERROR = 500;
	// 参数为空
	public static int RESPONSE_PARAM_EMPTY = 501;
	// cron表达式有误，不能被解析
	public static int RESPONSE_CRON_ERROR = 502;
	// 该任务名称和任务组已经存在
	public static int RESPONSE_NAME_EXISTS_ERROR = 503;
	// 告警信息不存在
	public static int RESPONSE_ALARM_ERROR = 504;
	
	// 参数格式错误
	public static int RESPONSE_PARAM_ERROR = 505;
	// 冻结用户
	public static int RESPONSE_USER_FROZEN = 507;
	// 请用户输入验证码
 	public static int RESPONSE_USER_VERIFICATION = 508;
	// 用户验证码错误
	public static int RESPONSE_USER_VERIFICATION_ERROR = 509;
	// 密码错误
	public static int RESPONSE_PWD_ERROR = 510;
	// 用户不属于该项目组
	public static int RESPONSE_USER_NOTIN_PROJECT = 511;
	// 快捷登录失败
	public static int RESPONSE_QUICK_ERROR = 512;
	// 已存在
	public static int RESPONSE_VAL_REPEAT = 521;
	// 权限错误
	public static int RESPONSE_POWER_ERROR = 522;
	
	public static short USER_CITY_ROLE = 2;
	
	public static String DEFAULT_PASSWORD = "ynyd!@34SEC";
	
	public static String REDIS_USER_PRE = "assets:user:";
	// redis存储用户信息
	public static String REDIS_USER_INFO = "assets:user_info:";
	
	//上传的app后缀名
	public static String APK_SUFFIX = ".apk";
	
	public static String QUICK_KEY = "uway_quick_key";
	
	public static  String EXPORT_USER_TOOL = "/template/chartReport.xlsx";
	
	public static  String FILE_NAME = "用户工具使用统计";
	
}
