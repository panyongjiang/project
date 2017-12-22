package com.uway.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uway.nmap.Nmap4j;
import com.uway.nmap.data.NMapRun;
import com.uway.domain.NmapScanningResult;
import com.uway.nmap.data.host.Status;
import com.uway.nmap.data.nmaprun.Host;
import com.uway.nmap.data.nmaprun.ScanInfo;
import com.uway.common.util.ExportUtil;
import com.uway.domain.NmapScanningPortDetails;
import com.uway.nmap.data.host.Address;
import com.uway.nmap.data.host.Ports;
import com.uway.nmap.data.host.ports.Port;
import com.uway.nmap.data.nmaprun.host.ports.port.State;
import com.uway.service.CommonService;

@Service
public class CommonServiceImpl implements CommonService {
	
	@Autowired
    private Nmap4j nmap4j;
	
	@Override
	public List<NmapScanningResult> getAssetScanDetails(String filePath) throws Exception {
		
		 nmap4j.setOutFilePath(filePath);
	     NMapRun nmapRun = nmap4j.getResult();
	     
	     List<NmapScanningResult> scanningResults = new ArrayList<NmapScanningResult>();
	     NmapScanningResult scanningResult = null;
         ArrayList<Host> hosts = nmapRun.getHosts();
         ScanInfo scanInfo = nmapRun.getScanInfo();

         Status status = null;
         ArrayList<Address> addresses = null;
         for (Host hostDtl : hosts) {
            scanningResult = new NmapScanningResult();
            status = hostDtl.getStatus();
            addresses = hostDtl.getAddresses();
            if(addresses != null && !addresses.isEmpty()){
            	scanningResult.setHostIp(addresses.get(0).getAddr());
            }
            scanningResult.setHostState(status.getState());
            scanningResult.setScanPort((int) scanInfo.getNumservices());
            getScanPortDetails(scanningResult,hostDtl);
            scanningResults.add(scanningResult);
         }
		return scanningResults;
	}
	
	@Override
	public HSSFWorkbook getExcelContent(String filePath) throws Exception {
		List<NmapScanningResult> scanningResults = getAssetScanDetails(filePath);
		
		List<Map<String, Object>> content = getScanDetails(scanningResults);
        List<String> titleName = new ArrayList<String>();
        List<String> paramList = new ArrayList<String>();
        String title = "";
        // 当前警告
        titleName.add("主机ip地址");
        titleName.add("主机状态");
        titleName.add("协议");
        titleName.add("端口号");
        titleName.add("服务");
        titleName.add("版本");
        
        paramList.add("ip");
        paramList.add("state");
        paramList.add("protocol");
        paramList.add("port");
        paramList.add("serviceName");
        paramList.add("serviceProduct");

        title = "主机扫描结果";
        
		return ExportUtil.getExcel(title, titleName, paramList, content);
	}

	private List<Map<String, Object>> getScanDetails(List<NmapScanningResult> scanningResults) throws Exception{
		
		List<Map<String, Object>> content = new ArrayList<Map<String, Object>>();
		Map<String, Object> rowData = null;
		List<NmapScanningPortDetails> scanningPortDetails = null;
		for (NmapScanningResult nmapScanningResult : scanningResults) {
			scanningPortDetails = nmapScanningResult.getScanningPortDetails();
			if(scanningPortDetails == null || scanningPortDetails.isEmpty()){
				rowData = new HashMap<String, Object>();
				rowData.put("ip", nmapScanningResult.getHostIp());
				rowData.put("state", nmapScanningResult.getHostState());
				rowData.put("protocol", null);
				rowData.put("port", null);
//			rowData.put("port_state", nmapScanningPortDetails.getState());
				rowData.put("serviceName", null);
				rowData.put("serviceProduct", null);
				content.add(rowData);
				rowData = null;
			}else{
				for (NmapScanningPortDetails nmapScanningPortDetails : scanningPortDetails) {
					rowData = new HashMap<String, Object>();
					rowData.put("ip", nmapScanningResult.getHostIp());
					rowData.put("state", nmapScanningResult.getHostState());
					rowData.put("protocol", nmapScanningPortDetails.getProtocol());
					rowData.put("port", nmapScanningPortDetails.getPort());
//				rowData.put("port_state", nmapScanningPortDetails.getState());
					rowData.put("serviceName", nmapScanningPortDetails.getServiceName());
					rowData.put("serviceProduct", nmapScanningPortDetails.getServiceProduct());
					content.add(rowData);
					rowData = null;
				}
			}
		}
		
		return content;
	}

	/**
     * 得到扫描的端口信息
     * 
     * @param scanResultId
     * @param hostDtl
     * @throws Exception
     */
    private void getScanPortDetails(NmapScanningResult scanningResult,Host hostDtl) throws Exception {
        Ports ports = hostDtl.getPorts();
        if (ports == null) {
            return;
        }
        ArrayList<Port> portList = ports.getPorts();
        if (portList == null || portList.isEmpty()) {
            return;
        }

        State state = null;
        com.uway.nmap.data.nmaprun.host.ports.port.Service service = null;
        List<NmapScanningPortDetails> scanningPortDetails = new ArrayList<NmapScanningPortDetails>();
        NmapScanningPortDetails nmapScanningPortDetails = null;
        for (Port port : portList) {
            nmapScanningPortDetails = new NmapScanningPortDetails();
            nmapScanningPortDetails.setPort((int) port.getPortId());
            nmapScanningPortDetails.setProtocol(port.getProtocol());
            state = port.getState();
            service = port.getService();
            if(state != null)
            	nmapScanningPortDetails.setState(state.getState());
            if(service != null){
            	nmapScanningPortDetails.setServiceName(service.getName());
            	nmapScanningPortDetails.setServiceProduct(service.getProduct());
            }
            scanningPortDetails.add(nmapScanningPortDetails);
        }
        scanningResult.setScanningPortDetails(scanningPortDetails);
    }

}
