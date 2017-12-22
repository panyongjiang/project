package com.udp.client;

import java.net.*;
import java.security.MessageDigest;
import java.util.Date;

import com.udp.util.Constants;
import com.udp.util.FileOperate;
import com.udp.util.StringUtil;

import udp.head;
import udp.req_url_check;

public class R2002S implements Runnable {

	int sucRecvPacketsNum, totalSendPacketsNum,failReceTotal,abc1,abc2;
	private String name;
	private int m,n;

	public R2002S(String name,int m,int n) {
		sucRecvPacketsNum = 0;
		failReceTotal = 0;
		totalSendPacketsNum = 10;
		abc1=0;
		abc2=0;
		this.name = name;
		this.m = m;
		this.n = n;
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
			//return new BigInteger(1, md.digest()).toString(16);
			return md.digest();
		} catch (Exception e) {
			throw new Exception("MD5加密出现错误");
		}
	}
	
	@Override
	public void run() {
		try {
			DatagramSocket client = new DatagramSocket(null);
			int port = 31000+(int)Thread.currentThread().getId();
			//System.out.println(port);
			try{
				client.bind(new InetSocketAddress(port));
			}catch (Exception e){
				e.printStackTrace();
			}
			client.setSoTimeout(100);
			client.setReuseAddress(true);
			System.out.println("开始"+Constants.sdf.format(new Date()));
			InetAddress addr = InetAddress.getByName("127.0.0.1");
			
			head h = new head();
			req_url_check r = new req_url_check();
			
			r.setNew((byte) 1);
			r.setNumber(33532);

			h.setCommand((short) 2002);
			h.setPlugID(80910);
			h.setEncryptSeq((short)1);
			
			String path1="E:/netty/whiteUrls.txt";
			String path2="E:/netty/blackUrls.txt";
			//String path3="E:/netty/middleUrls.txt";
			//System.out.println("开始读取名单"+Constants.sdf.format(new Date()));
			//查询白名单
			if(name.trim().equals("str1")){
				String str1=FileOperate.readFileByBybeBuffer(path1);
				this.sendAndReceive(str1, client, r, h, addr);
				//System.out.println("查询白名单结束"+Constants.sdf.format(new Date()));
				String str2=FileOperate.readFileByBybeBuffer(path2);
				this.sendAndReceive(str2, client, r, h, addr);
				
				/*String str3=FileOperate.readFileByBybeBuffer(path3);	
				this.sendAndReceive(str3, client, r, h, addr);*/
			}else if(name.trim().equals("str2")){
				//查询黑名单
				String str2=FileOperate.readFileByBybeBuffer(path2);
				this.sendAndReceive(str2, client, r, h, addr);
				//System.out.println("查询黑名单结束"+Constants.sdf.format(new Date()));
			}else if(name.trim().equals("str3")){
				//redis中不存在的url
				/*String str3=FileOperate.readFileByBybeBuffer(path3);	
				this.sendAndReceive(str3, client, r, h, addr);
				this.sendAndReceive(str3, client, r, h, addr);
				this.sendAndReceive(str3, client, r, h, addr);*/
				//System.out.println("查询其他结束"+Constants.sdf.format(new Date()));
			}

			//System.out.println("一共查询10000条，其中成功"+sucRecvPacketsNum+"条，失败"+failReceTotal+"条"+Constants.sdf.format(new Date()));
			client.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendAndReceive(String str,DatagramSocket client,req_url_check r,head h,InetAddress addr) throws Exception{
		if(str!=null&&!str.trim().equals("")){
			byte[] randoms={3, 46, 63, -113, -71, -1, -62, -16, -33, 44, 99, -6, 17, 58, -17, -91};
			String[] strs1=str.split("\n");
			int ak=0;
			for(int a=m;a<n;a++){	
				//Thread.sleep(1000);
				ak++;
				byte[] sendBuf, bh, br,brs;
				String s = "#Ffs$SV5%^$"+strs1[a].replaceAll("\r", "");
				byte[] mds = R2002S.getMD5(s.getBytes());						
				r.setURL(mds);
				r.setURLLen((short) mds.length);
				r.setURLSRC(s.getBytes());	
				r.setURLSRCLen((short) s.length());				
				
			    bh = h.toData();
			    brs = r.toData();
			    br = StringUtil.enCodeBodys(null,randoms, brs);

				int len = br.length + bh.length;
				int lenss = brs.length + bh.length;
				h.setLength((short)(lenss));
				h.setZLength((short)len);
				bh = h.toData();
				int lens = br.length + bh.length;
				sendBuf = new byte[lens];

				System.arraycopy(bh, 0, sendBuf, 0, bh.length);
				System.arraycopy(br, 0, sendBuf, bh.length, br.length);				

				//TEA.writs(sendBuf, "E:/text2.xml");
				try{
					//if(a==5000) Thread.sleep(100);
					DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length, addr, /*port*/9999);
					client.send(sendPacket);	
					abc1++;
				}catch(Exception e){
					abc2++;
				}
							
			}
			System.out.println("abc1=="+abc1+"abc2=="+abc2);
			System.out.println(ak+"开始发收！"+Constants.sdf.format(new Date()));
			/*for(int a=0;a<10;a++){	
				byte[] recvBuf = new byte[100];
				DatagramPacket recvPacket = new DatagramPacket(recvBuf, recvBuf.length);
				try{
					client.receive(recvPacket);
					sucRecvPacketsNum++;
					byte[] res = recvPacket.getData();
					byte[] hea = new byte[12];
					System.arraycopy(res, 0, hea, 0, 12);
					h.assign(hea);
					byte[] bt = new byte[h.getZLength() - 12];
					System.arraycopy(res, 12, bt, 0, h.getZLength() - 12);
					
					ack_url_check au=new ack_url_check();
			        byte[] bts=StringUtil.deCodeBodys(randoms, bt);
			        au.assign(bts);		

					//System.out.println(h.getCommand()+"=="+au.getNumber()+"-=="+au.getResult()+"=="+au.getType());
				}catch(Exception e){
					failReceTotal++;
					//System.out.println(e.getMessage());
					continue;
				}
			}*/
		}
	}
	
	public static void main(String[] args){
		Thread t1=new Thread(new R2002S("str1",0,1));
		t1.start();
		/*Thread t2=new Thread(new R2002S("str2",0,10000));
		t2.start();*/
			
		
		/*Thread t1=new Thread(new R2002S("str1",0,200));
		t1.start();		
		Thread t2=new Thread(new R2002S("str1",200,400));
		t2.start();
		Thread t3=new Thread(new R2002S("str1",400,600));
		t3.start();
		Thread t4=new Thread(new R2002S("str1",600,800));
		t4.start();
		Thread t5=new Thread(new R2002S("str1",800,1000));
		t5.start();*/
		
		/*Thread t12=new Thread(new R2002S("str1",5000,6000));
		t12.start();
		Thread t22=new Thread(new R2002S("str1",6000,7000));
		t22.start();
		Thread t32=new Thread(new R2002S("str1",7000,8000));
		t32.start();
		Thread t42=new Thread(new R2002S("str1",8000,9000));
		t42.start();
		Thread t52=new Thread(new R2002S("str1",9000,10000));
		t52.start();
		
		Thread t11=new Thread(new R2002S("str2",0,2000));
		t11.start();
		Thread t21=new Thread(new R2002S("str2",2000,4000));
		t21.start();
		Thread t31=new Thread(new R2002S("str2",4000,6000));
		t31.start();
		Thread t41=new Thread(new R2002S("str2",6000,8000));
		t41.start();
		Thread t51=new Thread(new R2002S("str2",8000,10000));
		t51.start();*/
	}
}
