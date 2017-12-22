package com.uway.common.util;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class UUIDUtil {
	public static String genUUID() {
		UUID uuid = UUID.randomUUID();
		String s = uuid.toString();//
		int p = 0;
		int j = 0;
		char[] buf = new char[32];
		while (p < s.length()) {
			char c = s.charAt(p);
			p += 1;
			if (c == '-')
				continue;
			buf[j] = c;
			j += 1;
		}
		return new String(buf);
	}
}
