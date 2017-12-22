package com.uway.mobile.util;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import org.springframework.stereotype.Component;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
@Component
public class FreemarkerUtil {
	private Configuration configuration = null;  
  
    @SuppressWarnings("deprecation")
	public File createDoc(Map<String, Object> paraMap,String templetFileName,String outputFileName) {  
    	configuration = new Configuration();  
        configuration.setDefaultEncoding("UTF-8");
        // 这里我们的模板是放在src.model包下面  
        configuration.setClassForTemplateLoading(this.getClass(),"../../../../");  
       // configuration.setClassForTemplateLoading(this.getClass(),this.getClass().getClassLoader().getResource("").getPath()); 
        Template t = null;  
        try {  
            t = configuration.getTemplate(templetFileName); // 装载模板  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        // 输出文档路径及名称  
        File outFile = new File(System.getProperty("user.dir")+"/"+outputFileName);  
        Writer out = null;  
        try {  
            out = new BufferedWriter(new OutputStreamWriter(  
                    new FileOutputStream(outFile),"utf-8"));  
        } catch (Exception e1) {  
            e1.printStackTrace();  
        }  
  
        try {  
            t.process(paraMap, out);  
            out.flush();
            out.close();
        } catch (TemplateException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } 
        return outFile;
    }  
}
