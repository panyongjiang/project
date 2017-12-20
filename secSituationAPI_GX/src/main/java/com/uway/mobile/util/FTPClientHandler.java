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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.ArrayList;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

import com.uway.mobile.common.StaticFtp;

/**
 *
 * @author <a href="liut@uway.cn">123</>
 * @date 2017年8月3日
 */
public class FTPClientHandler {

	private static Logger LOG = Logger.getLogger(FTPClientHandler.class);

	private static ThreadLocal<FTPClient> ftpThreadLocal = new ThreadLocal<FTPClient>();

	public static FTPClient connect() throws SocketException, IOException {
		FTPClient ftpClient = ftpThreadLocal.get();

		if (ftpClient != null && ftpClient.isConnected()) {
			try {
				// 检查连接是否超时
				boolean flag = ftpClient.sendNoOp();
				if (flag) {
					return ftpClient;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		ftpClient = new FTPClient();
		ftpClient.connect(StaticFtp.host, StaticFtp.port);
		
		ftpClient.setDataTimeout(300000);
		if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
			if (ftpClient.login(StaticFtp.username, StaticFtp.password)) {
				ftpClient.setControlEncoding("UTF-8");
				ftpClient.setConnectTimeout(15000);
				ftpClient.enterLocalPassiveMode();
				LOG.info("FTP连接成功。ftp服务器地址:"+StaticFtp.host+":"+StaticFtp.port);
				ftpThreadLocal.set(ftpClient);
				return ftpClient;
			} else {
				LOG.error("未连接到FTP，用户名或密码错误。");
				throw new IOException("未连接到FTP，用户名或密码错误。");
			}
		} else {
			LOG.error("未连接到FTP，ip端口错误。");
			throw new IOException("未连接到FTP，ip端口错误。");
		}

	}

	/**
	 * 
	 * @param directory
	 *            源文件存储地址
	 * @return
	 * @throws SocketException
	 * @throws IOException
	 */
	public static FTPFile[] listFiles(String directory) throws SocketException, IOException {

		FTPClient ftpClient = connect();

		FTPFile[] files = null;
		ftpClient.enterLocalPassiveMode();
		ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
		ftpClient.changeWorkingDirectory(directory);
		files = ftpClient.listFiles();
		return files;
	}

	/**
	 * 下载文件
	 * 
	 * @throws IOException
	 * @throws SocketException
	 * 
	 */
	public static OutputStream downloadStream(String directory, String downloadFileName)
			throws SocketException, IOException {
		FTPClient ftpClient = connect();
		OutputStream fos = new ByteArrayOutputStream();
		ftpClient.enterLocalPassiveMode();
		ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
		ftpClient.changeWorkingDirectory(directory);

		boolean b = ftpClient.retrieveFile(new String(downloadFileName.getBytes("UTF-8"), "iso-8859-1"), fos);
		if (!b) {
			LOG.error("未找到文件  " + downloadFileName);
			fos = null;
		}

		return fos;
	}

	/**
	 * 下载文件
	 * 
	 * @param directory
	 *            文件存储目录
	 * @param downloadFile
	 *            下载的文件名
	 * @param localFile
	 *            本地文件路径
	 * @param ftp
	 * @return
	 */
	public static ArrayList<String> downloadStream(String directory, String downloadFileName, String localFile,
			FTPClient ftpClient) {
		ArrayList<String> list = new ArrayList<String>();
		try {
			FTPFile[] listFiles = listFiles(directory);
			for (FTPFile ftpFile : listFiles) {
				if (ftpFile.getName().startsWith(downloadFileName)) {
					String path = localFile + "/" + ftpFile.getName();
					list.add(path);
					File dir = new File(localFile);
					if (!dir.exists()) {
						if (!dir.mkdirs()) {
							throw new IOException("创建保存目录失败。");
						}
					}
					OutputStream fos = downloadStream(directory, downloadFileName);
					fos.close();
					LOG.debug(ftpFile.getName() + "下载成功。。。。。。。。。。。。。");
				}

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			LOG.error("没有找到以" + downloadFileName + "开头的文件");
		} catch (SocketException e) {
			e.printStackTrace();
			LOG.error("连接FTP失败");
		} catch (IOException e) {
			e.printStackTrace();
			LOG.info("文件读取错误。");
		}
		return list;
	}

	public static boolean uploadStream(String directory, String uploadFileName, InputStream fis)
			throws SocketException, IOException {

		FTPClient ftpClient = connect();
		boolean flag = false;
		ftpClient.enterLocalPassiveMode();
		ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
		ftpClient.changeWorkingDirectory(directory);

		flag = ftpClient.storeFile(new String(uploadFileName.getBytes("UTF-8"), "iso-8859-1"), fis);

		return flag;
	}

	/**
	 * 
	 * @param sourceFilePath
	 *            源文件
	 * @param destFilePath
	 *            迁移地址
	 * @return boolean
	 * @throws SocketException
	 * @throws IOException
	 */
	public static boolean renameFile(String sourceFilePath, String destFilePath) throws SocketException, IOException {
		FTPClient ftpClient = connect();

		boolean flag = false;

		flag = ftpClient.rename(new String(sourceFilePath.getBytes("UTF-8"), "iso-8859-1"),
				new String(destFilePath.getBytes("UTF-8"), "iso-8859-1"));

		return flag;

	}

	/**
	 *
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		OutputStream fos = new ByteArrayOutputStream();

		fos = FTPClientHandler.downloadStream("/malware", "I_EYKZ_FLOW_14_10_201708022050_B6A480E3_SECOND.csv");
		// fos.flush();
		// System.out.println("fos:" + fos.toString());

		Thread.sleep(5);

		ByteArrayInputStream fis = StreamConvertUtil.parse(fos);
		System.out.println("fis:" + fis.toString());
		FTPClientHandler.uploadStream("/malware_his", "I_EYKZ_FLO080303.csv", fis);

		// FTPClientHandler.renameFile("/malware/I_EYKZ_FLOW_14_10_201708011943_1718ADE9_SECOND.csv",
		// "/malware_his/bak.csv");

	}

}
