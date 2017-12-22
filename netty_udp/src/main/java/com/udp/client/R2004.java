package com.udp.client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.security.MessageDigest;

import com.udp.util.StringUtil;

import udp.head;
import udp.ntf_url_evil;

public class R2004 implements Runnable {

	int sucRecvPacketsNum, totalSendPacketsNum, failReceTotal;

	public R2004() {
		sucRecvPacketsNum = 0;
		failReceTotal = 0;
		totalSendPacketsNum = 1;
	}

	/**
	 * 对字符串md5加密
	 *
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static byte[] getMD5(byte[] str) throws Exception {
		try {
			// 生成一个MD5加密计算摘要
			MessageDigest md = MessageDigest.getInstance("MD5");
			// 计算md5函数
			md.update(str);
			// digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
			// BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hashs值
			// return new BigInteger(1, md.digest()).toString(16);
			return md.digest();
		} catch (Exception e) {
			throw new Exception("MD5加密出现错误");
		}
	}

	public void statistic() {
		System.out.println("total send packets num:" + this.totalSendPacketsNum + " suc recv packtes num:"
				+ this.sucRecvPacketsNum);
	}

	@Override
	public void run() {
		try {
			int i;
			DatagramSocket client = new DatagramSocket(null);
			int port = 31000 + (int) Thread.currentThread().getId();
			System.out.println(port);
			try {
				client.bind(new InetSocketAddress(port));
			} catch (Exception e) {
				e.printStackTrace();
			}
			client.setSoTimeout(5000);
			client.setReuseAddress(true);

			byte[] sendBuf, bh, br, brs;

			head h = new head();
			h.setCommand((short) 2004);
			h.setPlugID(358611020000107L);
			h.setEncryptSeq((short)1);

			String sg = "abc098712345";
//			String s = "wap.goukanla.com";
			String s = "www.aotuporn.xyz";			
			byte[] mds = R2004.getMD5(s.getBytes());
			
			ntf_url_evil nu = new ntf_url_evil();
			nu.setEvilType((byte) 1);
			nu.setMac(sg.getBytes());
			//nu.setURLLen((byte) mds.length);
			nu.setURLLen((byte) s.getBytes().length);
			nu.setURL(s.getBytes());
			nu.setSrcPort((short) 4444);
			nu.setDstPort((short) 44);
			nu.setSrcIP(4444);
			nu.setDstIP(4444);

			byte[] randoms = {-24, 114, 30, 85, -98, 10, 33, -113, -96, 42, -72, 26, 10, -32, -23, 13};
			bh = h.toData();
			brs = nu.toData();
			br = StringUtil.enCodeBodys(null, randoms, brs);//加密后总长度（报头+报体）

			int len = br.length + bh.length;
			int lenss = brs.length + 16; //加密前总长度（报头+报体）
			h.setLength((short) (lenss));

			h.setZLength((short) (len));
			bh = h.toData();
			int lens = br.length + bh.length;
			sendBuf = new byte[lens]; //设置发送包总长度

			System.arraycopy(bh, 0, sendBuf, 0, bh.length);
			System.arraycopy(br, 0, sendBuf, bh.length, br.length);

			System.out.println("begin! len=" + lens);
			long endTimeStamp, beginTimeStamp = System.currentTimeMillis();
			InetAddress addr = InetAddress.getByName("127.0.0.1");
//			InetAddress addr = InetAddress.getByName("114.115.139.225");

			System.out.println("sblen==" + sendBuf.length);
			DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length, addr, /* port */9999);
			for (i = 0; i < this.totalSendPacketsNum; i++) {
				//发送包
				client.send(sendPacket);
				byte[] recvBuf = new byte[100];
				DatagramPacket recvPacket = new DatagramPacket(recvBuf, recvBuf.length);
				try {
					client.receive(recvPacket);
					sucRecvPacketsNum++;
				} catch (Exception e) {
					failReceTotal++;
				}
				// String recvStr = new String(recvPacket.getData() , 0
				// ,recvPacket.getLength());
				System.out.println(recvPacket.getLength());
				byte[] res = recvPacket.getData();
				System.out.println("receive===" + new String(res));
				/// System.out.println(au.getNumber()+"-=="+au.getResult()+"=="+au.getType());
				// System.out.println(Thread.currentThread().getId()+"收到:" +
				/// h.getLength()+h.getCommand()+h.getPlugID()+"==="+res.length);

			}
			endTimeStamp = System.currentTimeMillis();

			System.out.println("sucRecvPacketsNum=" + sucRecvPacketsNum + " ；failReceTotal=" + failReceTotal
					+ " ；totalNum=" + i + " total " + (endTimeStamp - beginTimeStamp) / 1000 + " seconds");
			client.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		R2004 udp = new R2004();
		udp.run();
	}
}
