package com.uway.mobile.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * 序列化和反序列化
 * 
 * @author 我
 *
 */
public class SerializeUtil {

	public static final Logger log = Logger.getLogger(SerializeUtil.class);

	public static <T> byte[] serialize(List<T> value) throws Exception {
		if (value == null) {
			throw new NullPointerException("Can't serialize null");
		}
		byte[] rv = null;
		ByteArrayOutputStream bos = null;
		ObjectOutputStream os = null;
		try {
			bos = new ByteArrayOutputStream();
			os = new ObjectOutputStream(bos);
			for (T t : value) {
				os.writeObject(t);
			}
			os.writeObject(null);
			rv = bos.toByteArray();
		} catch (IOException e) {
			throw new IllegalArgumentException("Non-serializable object", e);
		} finally {
			os.close();
			bos.close();
		}
		return rv;
	}

	public static <T> List<T> deserialize(byte[] in) throws Exception {
		List<T> list = new ArrayList<T>();
		ByteArrayInputStream bis = null;
		ObjectInputStream is = null;
		try {
			if (in != null) {
				bis = new ByteArrayInputStream(in);
				is = new ObjectInputStream(bis);
				while (true) {
					@SuppressWarnings("unchecked")
					T t = (T) is.readObject();
					if (t == null) {
						break;
					} else {
						list.add(t);
					}
				}

			}
		} catch (IOException e) {
			log.warn("Caught IOException decoding %d bytes of data");
		} finally {
			is.close();
			bis.close();
		}
		return list;
	}

}
/*
 * static class ListTranscoder{ public static byte[] serialize(Object value)
 * throws IOException { if (value == null) { throw new NullPointerException(
 * "Can't serialize null"); } byte[] rv=null; ByteArrayOutputStream bos = null;
 * ObjectOutputStream os = null; try { bos = new ByteArrayOutputStream(); os =
 * new ObjectOutputStream(bos); os.writeObject(value); os.close(); bos.close();
 * rv = bos.toByteArray(); } catch (IOException e) { throw new
 * IllegalArgumentException("Non-serializable object", e); } finally {
 * os.close(); bos.close(); } return rv; }
 * 
 * public static Object deserialize(byte[] in) throws Exception { Object
 * rv=null; ByteArrayInputStream bis = null; ObjectInputStream is = null; try {
 * if(in != null) { bis=new ByteArrayInputStream(in); is=new
 * ObjectInputStream(bis); rv=is.readObject(); is.close(); bis.close(); } }
 * catch (IOException e) { e.printStackTrace(); } finally { is.close();
 * bis.close(); } return rv; } }
 */
