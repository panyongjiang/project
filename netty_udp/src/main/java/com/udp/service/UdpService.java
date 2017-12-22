package com.udp.service;
import org.springframework.data.redis.connection.RedisClusterConnection;

import com.udp.entity.AckUrlCheck;
import com.udp.entity.NtfUrlEnil;
import com.udp.entity.ReqAuthNumber;
import com.udp.entity.ReqUrlCheck;
import com.udp.entity.RspAuthNumber;
import com.udp.entity.UdpHead;

public interface UdpService {

	public AckUrlCheck getReqCheck(RedisClusterConnection connection,UdpHead uh,ReqUrlCheck rc);
	public void setNtfUrlEnil(UdpHead uh,NtfUrlEnil nue) throws Exception;
	
	public RspAuthNumber saveReqAuth(RedisClusterConnection connection,UdpHead uh,ReqAuthNumber rea);	
}
