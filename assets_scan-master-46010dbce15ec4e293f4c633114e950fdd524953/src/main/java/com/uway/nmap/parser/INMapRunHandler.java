/*
 * Copyright (c) 2010, nmap4j.org
 *
 * All rights reserved.
 *
 * This license covers only the Nmap4j library.  To use this library with
 * Nmap, you must also comply with Nmap's license.  Including Nmap within
 * commercial applications or appliances generally requires the purchase
 * of a commercial Nmap license (see http://nmap.org/book/man-legal.html).
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 *    * Redistributions of source code must retain the above copyright notice, 
 *      this list of conditions and the following disclaimer.
 *    * Redistributions in binary form must reproduce the above copyright 
 *      notice, this list of conditions and the following disclaimer in the 
 *      documentation and/or other materials provided with the distribution.
 *    * Neither the name of the nmap4j.org nor the names of its contributors 
 *      may be used to endorse or promote products derived from this software 
 *      without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.uway.nmap.parser;

import org.xml.sax.Attributes;

import com.uway.nmap.data.NMapRun;
import com.uway.nmap.data.host.Address;
import com.uway.nmap.data.host.Cpe;
import com.uway.nmap.data.host.Distance;
import com.uway.nmap.data.host.Hostnames;
import com.uway.nmap.data.host.IpIdSequence;
import com.uway.nmap.data.host.Os;
import com.uway.nmap.data.host.Ports;
import com.uway.nmap.data.host.Status;
import com.uway.nmap.data.host.TcpSequence;
import com.uway.nmap.data.host.TcpTsSequence;
import com.uway.nmap.data.host.Times;
import com.uway.nmap.data.host.Uptime;
import com.uway.nmap.data.host.os.OsClass;
import com.uway.nmap.data.host.os.OsMatch;
import com.uway.nmap.data.host.os.PortUsed;
import com.uway.nmap.data.host.ports.ExtraPorts;
import com.uway.nmap.data.host.ports.Port;
import com.uway.nmap.data.nmaprun.Debugging;
import com.uway.nmap.data.nmaprun.Host;
import com.uway.nmap.data.nmaprun.RunStats;
import com.uway.nmap.data.nmaprun.ScanInfo;
import com.uway.nmap.data.nmaprun.Verbose;
import com.uway.nmap.data.nmaprun.host.ports.extraports.ExtraReasons;
import com.uway.nmap.data.nmaprun.host.ports.port.Service;
import com.uway.nmap.data.nmaprun.host.ports.port.State;
import com.uway.nmap.data.nmaprun.hostnames.Hostname;
import com.uway.nmap.data.nmaprun.runstats.Finished;
import com.uway.nmap.data.nmaprun.runstats.Hosts;

/**
 * This interface defines the functionality necessary to create the various
 * nmap XML objects based on the parsed data.  It's primary purpose is to
 * allow a discrete way to handle creating data objects from the XML data.
 * <p>
 * The methods defined here are specifically for loading the XML attributes
 * and not the child elements.  That is handled in the DefaultHandler
 * implementation.  In essence, this is a utility class for creating Objects 
 * from the XML attributes.
 * 
 * @author jsvede
 *
 */
public interface INMapRunHandler {
	
	public NMapRun createNMapRun( Attributes attributes ) ;
	
	public Host createHost( Attributes attributes ) ;
	
	public Distance createDistance( Attributes attributes ) ;

	public Address createAddress(  Attributes attributes ) ; 
	
	public Hostnames createHostnames(  Attributes attributes ) ;
	
	public Hostname createHostname( Attributes attributes ) ;
	
	public IpIdSequence createIpIdSequence(  Attributes attributes ) ;
	
	public Os createOs(  Attributes attributes ) ;
	
	public Ports createPorts(  Attributes attributes ) ;
	
	public Status createStatus(  Attributes attributes ) ;
	
	public TcpSequence createTcpSequence(  Attributes attributes ) ;
	
	public TcpTsSequence createTcpTsSequence(  Attributes attributes ) ;
	
	public Times createTimes(  Attributes attributes ) ;
	
	public Uptime createUptime(  Attributes attributes ) ;

	public OsClass createOsClass(  Attributes attributes ) ;
	
	public OsMatch createOsMatch(  Attributes attributes ) ;
	
	public PortUsed createPortUsed(  Attributes attributes ) ;
	
	public ExtraPorts createExtraPorts(  Attributes attributes ) ;
	
	public Port createPort(  Attributes attributes ) ;
	
	public Debugging createDebugging(  Attributes attributes ) ;
	
	public RunStats createRunStats(  Attributes attributes ) ;
	
	public ScanInfo createScanInfo(  Attributes attributes ) ;
	
	public Verbose createVerbose(  Attributes attributes ) ;
	
	public ExtraReasons createExtraReasons(  Attributes attributes ) ;
	
	public Service createService(  Attributes attributes ) ;
	
	public State createState(  Attributes attributes ) ;
	
	public Finished createFinished(  Attributes attributes ) ;
	
	public Hosts createHosts(  Attributes attributes ) ;
	
	public Cpe createCpe( Attributes attributes ) ;
	
}
