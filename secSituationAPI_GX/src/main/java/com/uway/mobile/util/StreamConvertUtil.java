/*
 * Copyright (C) 2015 123
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.uway.mobile.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 *
 * @author <a href="liut@uway.cn">123</>
 * @date 2017年7月31日
 */
public class StreamConvertUtil {
	/**
	 * inputStream转outputStream
	 * 
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public static ByteArrayOutputStream parse(InputStream in) throws Exception {
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
		int ch;
		while ((ch = in.read()) != -1) {
			swapStream.write(ch);
		}
		return swapStream;
	}

	/**
	 * outputStream转inputStream
	 * 
	 * @param out
	 * @return
	 * @throws Exception
	 */
	public static ByteArrayInputStream parse(OutputStream out) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos = (ByteArrayOutputStream) out;
		ByteArrayInputStream swapStream = new ByteArrayInputStream(baos.toByteArray());
		return swapStream;
	}

	/**
	 * inputStream转String
	 * 
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public static String parse_String(InputStream in) throws Exception {
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
		int ch;
		while ((ch = in.read()) != -1) {
			swapStream.write(ch);
		}
		return swapStream.toString();
	}

	/**
	 * OutputStream 转String
	 * 
	 * @param out
	 * @return
	 * @throws Exception
	 */
	public static String parse_String(OutputStream out) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos = (ByteArrayOutputStream) out;
		ByteArrayInputStream swapStream = new ByteArrayInputStream(baos.toByteArray());
		return swapStream.toString();
	}

	/**
	 * String转inputStream
	 * 
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public static ByteArrayInputStream parse_inputStream(String in) throws Exception {
		ByteArrayInputStream input = new ByteArrayInputStream(in.getBytes());
		return input;
	}

	/**
	 * String 转outputStream
	 * 
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public static ByteArrayOutputStream parse_outputStream(String in) throws Exception {
		return parse(parse_inputStream(in));
	}
}
