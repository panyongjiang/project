package com.uway.mobile.util;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;

@Component
public class JPushUtil {
	public static final Logger LOG = Logger.getLogger("JPushUtil.class");

	public PushPayload buildPushObject_all_all_alert(String message) {
		return PushPayload.alertAll(message);
	}

	public PushPayload buildPushObject_all_alias_alert(String userName, String message) {
		return PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(Audience.alias(userName))
				.setNotification(Notification.alert(message)).build();
	}

	public PushPayload buildPushMessage_all_alias_alert(String userName, String title, String message) {
		return PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(Audience.alias(userName))
				.setMessage(
						Message.newBuilder().setTitle(title).setMsgContent(message).addExtra("from", "JPush").build())
				.build();
	}
}
