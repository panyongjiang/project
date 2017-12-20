package com.uway.mobile.util;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.uway.mobile.domain.Aurora;
import com.uway.mobile.domain.ChangeUrl;
import com.uway.mobile.domain.Hole;
import com.uway.mobile.domain.Keypage;
import com.uway.mobile.domain.Keyword;
import com.uway.mobile.domain.Malware;
import com.uway.mobile.domain.Page;
import com.uway.mobile.domain.Param;
import com.uway.mobile.domain.Smooth;
import com.uway.mobile.domain.Vul;
import com.uway.mobile.domain.Vuls;

public class ReadXmlUtil {
	public static final List<Keypage> keyPage = new ArrayList<Keypage>();
	/**
	 * xml转对象
	 * @param xmlStr
	 * @return
	 */
	public static Aurora xmlToObject(String xmlStr){
		XStream xStream = new XStream(new DomDriver());
		xStream.alias("aurora",Aurora.class);
		xStream.alias("vul_keypage",keyPage.getClass());
		xStream.alias("malware",Malware.class);
		xStream.alias("pagecrack",ChangeUrl.class);
		xStream.alias("darkchain",ChangeUrl.class);
		xStream.alias("keyword",Keyword.class);
		xStream.alias("smooth",Smooth.class);
		xStream.alias("page",Page.class);		
		xStream.alias("vuls", Vuls.class);
		xStream.alias("hole", Hole.class);
		xStream.alias("vul", Vul.class);
		xStream.alias("param", Param.class);
		
		xStream.useAttributeFor(Param.class, "attr");
		xStream.ignoreUnknownElements();
		xStream.autodetectAnnotations(true);
		Aurora aurora = (Aurora) xStream.fromXML(xmlStr); 
		return aurora;
	}
	
	/**
	 * document转字符串
	 * @param doc
	 * @return
	 */
	public static String getXmlString(Document doc){
        TransformerFactory tf = TransformerFactory.newInstance();
        try {
            Transformer t = tf.newTransformer();
            t.setOutputProperty(OutputKeys.ENCODING,"UTF-8");//解决中文问题，试过用GBK不行  
            t.setOutputProperty(OutputKeys.METHOD, "html");
            t.setOutputProperty(OutputKeys.VERSION, "4.0");
            t.setOutputProperty(OutputKeys.INDENT, "no");
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            t.transform(new DOMSource(doc), new StreamResult(bos));
            return bos.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
