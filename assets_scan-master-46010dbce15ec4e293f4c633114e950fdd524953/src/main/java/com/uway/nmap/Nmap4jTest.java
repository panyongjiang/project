package com.uway.nmap;

import com.uway.common.util.UUIDUtil;
import com.uway.nmap.core.nmap.NMapExecutionException;
import com.uway.nmap.core.nmap.NMapInitializationException;
import com.uway.nmap.data.NMapRun;

public class Nmap4jTest {
	
	private String filePath = "/home/uway/com.uway.rid/";
	
	public static void main(String[] args) throws Exception{
		Nmap4jTest nmap4jTest = new Nmap4jTest();
		nmap4jTest.basicNmap4jUsageTest();
	}
	
	private String getFileFullName() throws Exception{
		return filePath + "nmap_" + UUIDUtil.genUUID() + ".xml";
	}
	
	public void basicNmap4jUsageTest() throws Exception {
		
		try {
//			String path = "/usr/local" ;
			String path = "E:\\software\\Nmap\\" ;
			
//			String command = "nmap -T4 -A -v -Pn 192.168.27.235";
//			String command = path + "nmap.exe -T4 -A -v -Pn 192.168.28.210";
			String command = path + "nmap.exe -T4 -A -v -Pn 192.168.28.168";
			
			String[] cmds = command.split(" ");
			
			StringBuilder newCmdBuilder = new StringBuilder();
			 for (String param : cmds) {
	                if (!param.equals(cmds[cmds.length - 1])) {
	                	if("-A".equals(param)){
	                		newCmdBuilder.append(param).append(" ").append("--script=smb-vuln-*.nse").append(" ");
	                	}else{
	                		newCmdBuilder.append(param).append(" ");
	                	}
	                }
	            }
			String fileName = getFileFullName();
			newCmdBuilder.append("-oX ").append(fileName + " ");
			newCmdBuilder.append(cmds[cmds.length -1]);
			
			System.out.println("basicNmap4jUsageTest newCmdBuilder : " + newCmdBuilder.toString());
			
			Nmap4j nmap4j = new Nmap4j(fileName, newCmdBuilder.toString() ) ;
//			nmap4j.setOutFilePath("d:/doc/test.xml");
//			nmap4j.addFlags( "-sS" ) ;
//			nmap4j.includeHosts( "192.168.27.235" ) ;
			nmap4j.execute() ;
			if( !nmap4j.hasError() ) {
				NMapRun nmapRun = nmap4j.getResult() ;
				System.out.println("nmapRun: " + nmapRun.toString());
				String output = nmap4j.getOutput() ;
				System.out.println("result: " + output);
//				if( output == null ) {
//					fail() ;
//				}
				String errors = nmap4j.getExecutionResults().getErrors() ;
				System.out.println("errors :" + errors);
//				if (errors == null ) {
//					fail() ;
//				}
			}
		} catch (NMapInitializationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NMapExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
