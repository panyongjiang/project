package com.udp.service.impl;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.data.redis.connection.RedisClusterConnection;

import com.udp.entity.AckUrlCheck;
import com.udp.entity.NtfUrlEnil;
import com.udp.entity.ReqAuthNumber;
import com.udp.entity.ReqUrlCheck;
import com.udp.entity.RspAuthNumber;
import com.udp.entity.UdpHead;
import com.udp.service.UdpService;
import com.udp.util.Constants;
import com.udp.util.ElasticSearchUtils;
import com.udp.util.NettyUtil;
import com.udp.util.StringUtil;
import com.udp.util.Utilities;

//获取redis es数据
public class UdpServiceImpl implements UdpService {

	public static final Logger log = Logger.getLogger(UdpServiceImpl.class);
	public static final String[] typeArr = { "secPhishingSite", "secCheatWebsite", "secTortContent", "secShamAdv",
			"secIllegalGamble", "secHarmProgram", "secIllegalSite", "secSexyInfo", "secSexySite" };
	public static String STRATEGY_PREFIX = "SEC";

	// 2002消息，获取当前url是否为黑白名单
	@Override
	public AckUrlCheck getReqCheck(RedisClusterConnection connection, UdpHead uh, ReqUrlCheck rc) {
		log.info("getReqCheck 参数" + uh.toString() + "rcParam" + rc.toString());
		AckUrlCheck ac = new AckUrlCheck();
		// TODO Auto-generated method stub
		String url = new String(rc.getUrl());// 加密url
		String urls = new String(rc.getUrlsrc());// 原始url
		// 从redis取对象key==uwayurl
		byte[] obj = connection.hGet(Constants.URLBEFS.getBytes(), rc.getUrl());
		int preIndex = urls.indexOf('?');
		try {
			if (preIndex != -1) {
				String preUrls = urls.substring(0, urls.indexOf('?'));// 截取参数之前的部分
				url = new String(StringUtil.getMD5(preUrls.getBytes()));
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		int index=0;
		if (obj != null) {
			// result 1为黑名单 0位白名单
			String value = "";
			try {
				//编码转码
				value = new String(obj, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			String[] res = value.split("\\|");
			ac.setNumber(rc.getNumber()); ////取出报文序号赋值到应答2003
			index = Integer.parseInt(res[1]);
			String key = STRATEGY_PREFIX + "-" + uh.getPlugID();
			// 根据索引取对应值
			String field = typeArr[index];
			// 到redis中查看此设备中安全类型field是否开启
			byte[] val = connection.hGet(key.getBytes(), field.getBytes());
			if (val != null) {
				String valStr = new String(val);
				if ("1".equals(valStr)) {
					ac.setResult(1);
				} else {
					ac.setResult(0);
				}
			} else {
				ac.setResult(0);
			}
			ac.setType((byte) index);
		} else {
			// 没有，则默认为白名单，并把数据 rpush到redis消息队列
			connection.rPush(Constants.DKEYS.getBytes(), (url + "|0|" + urls).getBytes());// 定义一个常亮作为KEY
			ac.setNumber(rc.getNumber());
			ac.setResult(0);
			ac.setType((byte) index);
		}
		return ac;
	}

	// 上传数据导es
	@Override
	public void setNtfUrlEnil(UdpHead uh, NtfUrlEnil nue) throws Exception {
		// TODO Auto-generated method stub
		log.info("setNtfUrlEnil 参数" + uh.toString() + "nueParam" + nue.toString());
		//ElasticSearchUtils.createMapper(Constants.esIndex, Constants.esType);//创建索引
		ElasticSearchUtils.insertES(Constants.esIndex, Constants.esType, transBean2Map(uh, nue));
	}

	// 存储某设备的最新序号和密钥
	@Override
	public RspAuthNumber saveReqAuth(RedisClusterConnection connection, UdpHead uh, ReqAuthNumber rea) {
		log.info("saveReqAuth 参数" + uh.toString() + "reaParam" + rea.toString());
		RspAuthNumber rsqn = new RspAuthNumber();
		byte[] strRandom = StringUtil.getSBytes();
		byte[] tims = Utilities.intToByteArray((int) (System.currentTimeMillis() / 1000));

		byte[] strRandoms = NettyUtil.addBytes(strRandom, tims);

		rsqn.setNumber(rea.getNumber());
		rsqn.setRandomStr(strRandom);

		connection.hSet((Constants.BEFSS).getBytes(), Utilities.longToBytes(uh.getPlugID()),
				Utilities.intToByteArray(uh.getEncryptSeq()));// 保留最新的加密序号
		connection.set((Constants.BEFS + uh.getPlugID() + uh.getEncryptSeq()).getBytes(), strRandoms);
		connection.expire((Constants.BEFS + uh.getPlugID() + uh.getEncryptSeq()).getBytes(), Constants.reTimeout * 3);// 保留最近的3次序号的密钥

		return rsqn;
	}

	public static Map<String, Object> transBean2Map(UdpHead uh, Object obj) {

		if (obj == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor property : propertyDescriptors) {
				String key = property.getName();

				// 过滤class属性
				if (!key.equals("class")) {
					// 得到property对应的getter方法
					Method getter = property.getReadMethod();
					Object value = getter.invoke(obj);

					if (key.equals("url") || key.equals("mac")) {
						map.put(key, new String((byte[]) value));
					} else {
						map.put(key, value);
					}
				}

			}
			map.put("srcFmtIp", Utilities.fmtInt2Ip(Integer.parseInt(map.get("srcIp").toString())));
			map.put("dstFmtIp", Utilities.fmtInt2Ip(Integer.parseInt(map.get("dstIp").toString())));
		} catch (Exception e) {
			log.error("transBean2Map Error " + e);
		}

		map.put("createTime", System.currentTimeMillis());
		map.put("createDate", Constants.sdf.format(new Date()));
		map.put("deviceid", String.valueOf(uh.getPlugID()));
		System.out.println(map.get("deviceid"));
		return map;
	}
}
