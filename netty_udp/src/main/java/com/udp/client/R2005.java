package com.udp.client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.security.MessageDigest;
import java.util.Arrays;

import com.udp.entity.ReqAuthNumber;
import com.udp.entity.RspAuthNumber;

import udp.head;

public class R2005 implements Runnable {

	int sucRecvPacketsNum, totalSendPacketsNum, failReceTotal;

	public R2005() {
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
				// return;
			}
			client.setSoTimeout(5000);
			client.setReuseAddress(true);

			byte[] sendBuf, bh, br;
			// String s = "#Ffs$SV5%^$www.linyiaite.com/";
			// byte[] mds = UDPClients.getMD5(s.getBytes());
			head h = new head();
			h.setCommand((short) 2005);
			h.setPlugID(358611020000107L);  //设备id 358611020000107
			h.setEncryptSeq((short) 0);

			ReqAuthNumber rqa = new ReqAuthNumber();
			rqa.setNumber(33532);
			rqa.setEncryptMethod((byte) 1);
			// String sg = "abc098712345";
			/*
			 * nu.setEvilType((byte)1); nu.setMac(sg.getBytes());
			 * System.out.println("mslen=="+nu.getMac().length);
			 * 
			 * nu.setURLLen((byte)mds.length()); nu.setURL(mds.getBytes());
			 * nu.setSrcPort((short)80); nu.setDstPort((short)81);
			 * nu.setSrcIP(9876); nu.setDstIP(7653);
			 */

			bh = h.toData();
			br = rqa.toData();

			int len = br.length + bh.length;
			// System.out.println(nu.getURLLen()+"len==="+len);
			h.setLength((short) (len));
			h.setZLength((short) (len));
			bh = h.toData();
			int lens = br.length + bh.length;
			sendBuf = new byte[lens];

			System.arraycopy(bh, 0, sendBuf, 0, bh.length);
			System.arraycopy(br, 0, sendBuf, bh.length, br.length);

			System.out.println("begin! len=" + lens);
			long endTimeStamp, beginTimeStamp = System.currentTimeMillis();
			InetAddress addr = InetAddress.getByName("127.0.0.1");
//			InetAddress addr = InetAddress.getByName("114.115.139.225");

			System.out.println("sblen==" + sendBuf.length);
			DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length, addr, /* port */9999);
			for (i = 0; i < this.totalSendPacketsNum; i++) {
				//客户端发送包
				client.send(sendPacket);
				
				byte[] recvBuf = new byte[1024];
				DatagramPacket recvPacket = new DatagramPacket(recvBuf, recvBuf.length);
				try {
					client.receive(recvPacket);
					sucRecvPacketsNum++;
				} catch (Exception e) {
					failReceTotal++;
				}
				// String recvStr = new String(recvPacket.getData() , 0
				// ,recvPacket.getLength());
				byte[] res = recvPacket.getData();
				byte[] hea = new byte[16];		//控制密钥长度
				System.arraycopy(res, 0, hea, 0, 16);
				byte[] bt = new byte[res.length - 16];
				System.arraycopy(res, 16, bt, 0, res.length - 16);
				h.assign(hea);
				RspAuthNumber rsq = new RspAuthNumber();
				rsq.assign(bt);
				System.out.println(Arrays.toString(rsq.getRandomStr()));
				// byte[] random=rsq.getRandomStr();

				// System.out.println(au.getNumber()+"-=="+au.getResult()+"=="+au.getType());
				System.out.println("收到:" + h.getLength() + h.getCommand() + h.getPlugID() + "===" + res.length + "=="
						+ h.getEncryptSeq());
				// System.out.println("receive==="+new
				// BigInteger(bt).toString(16));
			}
			endTimeStamp = System.currentTimeMillis();

			System.out.println("sucRecvPacketsNum=" + sucRecvPacketsNum + " ; failReceTotal=" + failReceTotal
					+ "  ; totalNum=" + i + " total " + (endTimeStamp - beginTimeStamp) / 1000 + " seconds");
			client.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		R2005 udp = new R2005();
		udp.run();
	}
}
