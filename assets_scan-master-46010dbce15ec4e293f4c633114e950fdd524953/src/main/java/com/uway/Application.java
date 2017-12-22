package com.uway;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import com.uway.service.CommonService;
import com.uway.service.GenerateFileService;

@SpringBootApplication
public class Application{
    public static final Logger log = Logger.getLogger("Application.class");
    
    @Autowired(required=true)
    private GenerateFileService generateFileService;
    @Autowired(required=true)
    private CommonService commonService;
    @Value("scan.input.result.file")
    private String inputFile;
    @Value("scan.output.result.file")
    private String outputFile;
    
    public static void main(String[] args) throws Exception {
//        SpringApplication.run(Application.class, args);
//        log.debug("============= SpringBoot Start Success =============");
    	System.out.println("Application start");
    	ConfigurableApplicationContext context = null;
    	try {
    		context = new SpringApplicationBuilder()
    				.sources(Application.class).run(args);
    		
    		Application application = context.getBean(Application.class);
    		application.generate();
    		System.out.println("Application end");
		} finally {
			if(context != null)	context.close();
		}
    }
    
    public void generate() throws Exception{
    	System.out.println("generate start");
    	
    	Properties pps = new Properties();
		pps.load(Application.class.getResourceAsStream("/"	+ "application.properties"));
		inputFile = pps.getProperty("scan.input.result.file");
		outputFile = pps.getProperty("scan.output.result.file");
    	
    	HSSFWorkbook wb = commonService.getExcelContent(inputFile);
    	generateFileService.generateScanOutput(wb, outputFile);
    }

}