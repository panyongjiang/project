package com.uway.mobile.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.metrics.sum.InternalSum;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.dial.DialBackground;
import org.jfree.chart.plot.dial.DialCap;
import org.jfree.chart.plot.dial.DialPlot;
import org.jfree.chart.plot.dial.DialPointer;
import org.jfree.chart.plot.dial.DialTextAnnotation;
import org.jfree.chart.plot.dial.DialValueIndicator;
import org.jfree.chart.plot.dial.StandardDialFrame;
import org.jfree.chart.plot.dial.StandardDialRange;
import org.jfree.chart.plot.dial.StandardDialScale;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.StandardGradientPaintTransformer;

public class ChartUtils {
	
	public static void main(String[] args) {
//		System.out.println(System.getProperty("user.dir"));
//		Map<String,Integer> data = new HashMap<String,Integer>();
//		data.put("zj", 1);
//		createPieChart("zz","zz",data);
//		String[][] arr = {{"A","1"},{"B","2"}};
//		createBarChart("zz","zz",arr);
//		genLineChart();
	}
	
	public static String createDialplot(int value,String title,String name){
				// 数据集合对象此处为DefaultValueDataset
				DefaultValueDataset dataset = new DefaultValueDataset();
				// 当前指针指向的位置，即：我们需要显示的数据
				dataset = new DefaultValueDataset(value);
				// 实例化DialPlot
				DialPlot dialplot = new DialPlot();
				dialplot.setView(0.0D, 0.0D, 1.0D, 1.0D);
				// 设置数据集合
				dialplot.setDataset(dataset);
				// 开始设置显示框架结构(仪表盘属性外框)
				StandardDialFrame standardDialFrame = new StandardDialFrame();
				// 外框背景色
				standardDialFrame.setBackgroundPaint(Color.lightGray);
				// 外框前景色
				standardDialFrame.setForegroundPaint(Color.darkGray);
				dialplot.setDialFrame(standardDialFrame);
				// 结束设置显示框架结构(仪表盘颜色)
				GradientPaint gradientpaint = new GradientPaint(new Point(), new Color(255, 255, 255), new Point(), new Color(170, 170, 220)); 
				DialBackground dialbackground = new DialBackground(gradientpaint);
				dialbackground.setGradientPaintTransformer(new StandardGradientPaintTransformer(GradientPaintTransformType.VERTICAL));
				dialplot.setBackground(dialbackground);
				// 设置显示在表盘中央位置的信息
				DialTextAnnotation dialtextannotation = new DialTextAnnotation(name);
				dialtextannotation.setFont(new Font("Dialog", 1, 14));
				dialtextannotation.setRadius(0.69999999999999996D);
				dialplot.addLayer(dialtextannotation);
				DialValueIndicator dialvalueindicator = new DialValueIndicator(0);
				dialplot.addLayer(dialvalueindicator);
				// 根据表盘的直径大小（0.80），设置总刻度范围
				StandardDialScale standarddialscale = new StandardDialScale(10D, 100D, -120D, -300D, 10D, 1);
				standarddialscale.setTickRadius(0.80D);
				standarddialscale.setTickLabelOffset(0.14999999999999999D);
				standarddialscale.setTickLabelFont(new Font("Dialog", 0, 14));
				// 主意是dialplot.addScale（）不是dialplot.addLayer（）
				dialplot.addScale(0, standarddialscale);
				// 设置刻度范围（红色）
				StandardDialRange standarddialrange = new StandardDialRange(10D, 100D, Color.red);
				standarddialrange.setInnerRadius(0.52000000000000002D);
				standarddialrange.setOuterRadius(0.55000000000000004D);
				dialplot.addLayer(standarddialrange);
				// 设置刻度范围（橘黄色）
				StandardDialRange standarddialrange1 = new StandardDialRange(10D, 40D, Color.orange);
				standarddialrange1.setInnerRadius(0.52000000000000002D);
				standarddialrange1.setOuterRadius(0.55000000000000004D);
				dialplot.addLayer(standarddialrange1);
				// 设置刻度范围（绿色）
				StandardDialRange standarddialrange2 = new StandardDialRange(-40D, 10D, Color.green);
				standarddialrange2.setInnerRadius(0.52000000000000002D);
				standarddialrange2.setOuterRadius(0.55000000000000004D);
				dialplot.addLayer(standarddialrange2);
				// 设置指针
				DialPointer.Pointer pointer = new DialPointer.Pointer();
				dialplot.addLayer(pointer);
				// 实例化DialCap
				DialCap dialcap = new DialCap();
				dialcap.setRadius(0.10000000000000001D);
				dialplot.setCap(dialcap);// 生成chart对象
				JFreeChart jfreechart = new JFreeChart(dialplot);
				
				//setFont(jfreechart);
				
				// 设置标题
				jfreechart.setTitle(title);
				String imgPath = System.getProperty("user.dir") + "/a.png";
				File f = new File(imgPath);
				try {
					ChartUtilities.saveChartAsPNG(f, jfreechart, 400, 300);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return getImgCodeBase64(imgPath);
	}
	
	public static String createPieChart(String title,String name,Map<String,Integer> data){
		setChartTheme();

		DefaultPieDataset dpd = new DefaultPieDataset(); // 建立一个默认的饼图
		for(Map.Entry<String, Integer> entry : data.entrySet()){
			dpd.setValue(entry.getKey(), entry.getValue()); // 输入数据
		}
		// 第一个参数是标题，第二个参数是一个数据集，第三个参数表示是否显示Legend，第四个参数表示是否显示提示，第五个参数表示图中是否存在URL
		JFreeChart chart = ChartFactory.createPieChart(title, dpd, true, true, false);
		
		//setFont(chart);
		
		PiePlot pieplot = (PiePlot)chart.getPlot();
		pieplot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} {2}",NumberFormat.getNumberInstance(),new DecimalFormat("0.00%")));
		String imgPath = System.getProperty("user.dir") + "/b.png";
		try {
			ChartUtilities.saveChartAsJPEG(new File(imgPath), chart, 550, 250);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return getImgCodeBase64(imgPath);
	}
	
	
	public static String createPieChart(String title,String name,List<Bucket> data){
		setChartTheme();

		DefaultPieDataset dpd = new DefaultPieDataset(); // 建立一个默认的饼图
		for (Bucket bucket : data) {
			dpd.setValue(bucket.getKeyAsString(),bucket.getDocCount()); // 输入数据
		}

		// 第一个参数是标题，第二个参数是一个数据集，第三个参数表示是否显示Legend，第四个参数表示是否显示提示，第五个参数表示图中是否存在URL
		JFreeChart chart = ChartFactory.createPieChart(title, dpd, true, true, false);
		//setFont(chart);
		PiePlot pieplot = (PiePlot)chart.getPlot();
//		pieplot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}--{1}%"));
		pieplot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} {2}",NumberFormat.getNumberInstance(),new DecimalFormat("0.00%")));

		String imgPath = System.getProperty("user.dir") + "/h.png";
		try {
			ChartUtilities.saveChartAsJPEG(new File(imgPath), chart, 600, 400);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return getImgCodeBase64(imgPath);
	}
	
	public static String createBarChart(String title,String name,List<Bucket> data){
		setChartTheme();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();       
        int size = data.size();
        if(data.size()>10){
        	size = 10;
        }
        for(int i=0;i<size;i++){
        	Bucket bucket = data.get(i);
        	dataset.addValue(bucket.getDocCount(), name, bucket.getKey().toString());
        }
        JFreeChart chart = ChartFactory.createBarChart(title, "类型",       
                    "数量", dataset, PlotOrientation.HORIZONTAL, true, true, false);
        
        //setFont(chart);
        
		String imgPath = System.getProperty("user.dir") + "/c.png";
		try {
			ChartUtilities.saveChartAsPNG(new File(imgPath), chart, 500, 400);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return getImgCodeBase64(imgPath);
	}
	
	public static String createBarChart(String title,String month1,String month2,List<Bucket> data1,List<Bucket> data2){
		setChartTheme();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();       
        for(Bucket b1:data1){
//        	System.out.println("key1:"+b1.getKeyAsString()+" --->"+b1.getDocCount());
        	dataset.addValue(b1.getDocCount(), month1, b1.getKeyAsString());
        }
        for(Bucket b2:data2){
//        	System.out.println("key2:"+b2.getKeyAsString()+" --->"+b2.getDocCount());
        	dataset.addValue(b2.getDocCount(), month2, b2.getKeyAsString());
        }

        JFreeChart    chart = ChartFactory.createBarChart(title, "类型",       
                    "数量", dataset, PlotOrientation.HORIZONTAL, true, true, false);
        
        //setFont(chart);
        
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setItemMargin(0.0);
     // 设置柱子宽度
        renderer.setMaximumBarWidth(0.04);
                   
        String imgPath = System.getProperty("user.dir") + "/d.png";
        try {
			ChartUtilities.saveChartAsJPEG(new File(imgPath), chart, 800, 600);
		} catch (IOException e) {
			e.printStackTrace();
		}
        return getImgCodeBase64(imgPath);
	}
	
	public static String genLineChart(String title,String name,List<Bucket> buckets){  
		setChartTheme();
        // A网站的访问量统计  
        DefaultCategoryDataset lineDataset = new DefaultCategoryDataset();  
        for (Bucket bucket : buckets) {
        	String key = new BigDecimal(bucket.getKeyAsString()).toPlainString();
//        	System.out.println(key+"**************"+new BigDecimal(key).toPlainString());
        	lineDataset.addValue(bucket.getDocCount(), name, key.substring(0, 4)+"-"+key.substring(4,6)+"-"+key.substring(6,8));
        }
          
     // 定义图表对象
        JFreeChart chart = ChartFactory.createLineChart(title, //折线图名称
	         "时间", // 横坐标名称
	         "数量", // 纵坐标名称
	         lineDataset, // 数据
	         PlotOrientation.VERTICAL, // 水平显示图像
	         true, // include legend
	         true, // tooltips
	         false // urls
         );
        
        //setFont(chart);
        
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setRangeGridlinesVisible(true); //是否显示格子线
        plot.setBackgroundAlpha(0.3f); //设置背景透明度
        NumberAxis rangeAxis = (NumberAxis)plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setAutoRangeIncludesZero(true);
        rangeAxis.setUpperMargin(0.20);
        rangeAxis.setLabelAngle(Math.PI / 2.0);
        
        String imgPath = System.getProperty("user.dir") + "/e.png";  
        //最后返回组成的折线图数值  
        try {
			ChartUtilities.saveChartAsPNG(new File(imgPath),chart, 700, 500);
		} catch (IOException e) {
			e.printStackTrace();
		}  
          
        return getImgCodeBase64(imgPath);
    }
	
	
	public static String genCompareLineChart(String title,String name1,String name2,
			List<Bucket> buckets1,List<Bucket> buckets2,List<String> dateList){
		setChartTheme();
		DefaultCategoryDataset lineDataset = new DefaultCategoryDataset();
		List<String> keyList1 = new ArrayList<String>();
		for (Bucket bucket : buckets1) {	        	
        	String key = new BigDecimal(bucket.getKeyAsString()).toPlainString();
        	keyList1.add(key);			        	
        }
		List<String> keyList2 = new ArrayList<String>();
		for (Bucket bucket : buckets2) {	        	
        	String key = new BigDecimal(bucket.getKeyAsString()).toPlainString();
        	keyList2.add(key);			        	
        }		
		for (String date : dateList) {
			if(keyList1.size() > 0){
				if(keyList1.contains(date)){
			        for (Bucket bucket : buckets1) {	        	
			        	String key = new BigDecimal(bucket.getKeyAsString()).toPlainString();
			        	if(key.equals(date)){			        		
				        	InternalSum sub = bucket.getAggregations().get("sumFlow");
					        lineDataset.addValue(sub.getValue(), name1, key.substring(6,8));			        		
					        break;
			        	}else{
			        		continue;
			        	}
			        }
				}else{
					lineDataset.addValue(0, name1, date.substring(6,8));
				}
			}else{
				lineDataset.addValue(0, name1, date.substring(6,8));
			}
			if(keyList2 .size() > 0){
				if(keyList2.contains(date)){
			        for (Bucket bucket : buckets2) {
			        	String key = new BigDecimal(bucket.getKeyAsString()).toPlainString();
			        	if(key.equals(date)){			        
					        InternalSum sub = bucket.getAggregations().get("sumFlow");
					        lineDataset.addValue(sub.getValue(), name2, key.substring(6,8));
					        break;
			        	}else{
			        		continue;
			        	}
			        }
				}else{
					lineDataset.addValue(0, name2, date.substring(6,8));
				}
			}else{
				lineDataset.addValue(0, name2, date.substring(6,8));
			}
		}  
     // 定义图表对象
        JFreeChart chart = ChartFactory.createLineChart(title, //折线图名称
	         "时间", // 横坐标名称
	         "数量", // 纵坐标名称
	         lineDataset, // 数据
	         PlotOrientation.VERTICAL, // 水平显示图像
	         true, // include legend
	         true, // tooltips
	         false // urls
         );
        
        //setFont(chart);
        
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setRangeGridlinesVisible(true); //是否显示格子线
        plot.setBackgroundAlpha(0.3f); //设置背景透明度
        NumberAxis rangeAxis = (NumberAxis)plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setAutoRangeIncludesZero(true);
        rangeAxis.setUpperMargin(0.20);
        rangeAxis.setLabelAngle(Math.PI / 2.0);
        
        String imgPath = System.getProperty("user.dir") + "/f.png";  
        //最后返回组成的折线图数值  
        try {
			ChartUtilities.saveChartAsPNG(new File(imgPath),chart, 800, 500);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return getImgCodeBase64(imgPath);
	}
	
	public static String genBarChart(String title,String name,String[][] arrs,Map<String,Aggregation> dataMap){
		setChartTheme();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();      
        for(int i=0;i<arrs.length;i++){
        	InternalSum aggregation = (InternalSum)dataMap.get(arrs[i][1]);
        	dataset.addValue(aggregation.getValue(), name, arrs[i][0]);
        }
        JFreeChart chart = ChartFactory.createBarChart(title, "类型",       
                    "数量", dataset, PlotOrientation.HORIZONTAL, true, true, false);
        //setFont(chart);
        
		String imgPath = System.getProperty("user.dir") + "/j.png";
		try {
			ChartUtilities.saveChartAsPNG(new File(imgPath), chart, 800, 600);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return getImgCodeBase64(imgPath);
	}
	
	@SuppressWarnings("unused")
	private static void setFont(JFreeChart jfreechart){
		Font font = new Font("宋体", Font.BOLD, 22);  
		jfreechart.getTitle().setFont(font); // 标题  
		  
		font = new Font("宋体", Font.PLAIN, 14);  
		jfreechart.getLegend().setItemFont(font); // 列类型的文字字体  
		          
		font = new Font("宋体", Font.PLAIN, 16);  
		CategoryPlot categoryplot = jfreechart.getCategoryPlot();  
		CategoryAxis categoryaxis = categoryplot.getDomainAxis();  
		
		categoryaxis.setLabelFont(font); // x轴名称的字体  
		categoryplot.getRangeAxis().setLabelFont(font); // y轴名称的字体  
		  
		font = new Font("宋体", Font.PLAIN, 12);  
		categoryaxis.setTickLabelFont(font); // x轴上的刻度名称字体  
		categoryplot.getRangeAxis().setTickLabelFont(font); // y轴上的刻度名称字体  
		          
		font = new Font("宋体", Font.PLAIN, 18);  
		categoryplot.setNoDataMessage("暂无数据");  
		categoryplot.setNoDataMessageFont(font); // 没有数据时的提示
	}

	private static void setChartTheme() {
		// 创建主题样式       
        StandardChartTheme standardChartTheme = new StandardChartTheme("CN");       
        // 设置标题字体       
        standardChartTheme.setExtraLargeFont(new Font("隶书", Font.BOLD, 20));       
        // 设置图例的字体       
        standardChartTheme.setRegularFont(new Font("宋书", Font.PLAIN, 15));       
        // 设置轴向的字体       
        standardChartTheme.setLargeFont(new Font("宋书", Font.PLAIN, 15));       
        // 应用主题样式       
        ChartFactory.setChartTheme(standardChartTheme);
	}
	
	/**
	 * 对图片文件进行Base64编码
	 * @param imgPath
	 * @return
	 */
	public static String getImgCodeBase64(String imgPath){
		InputStream in = null;
        byte[] data = null;
        try {
            in = new FileInputStream(imgPath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String imageCodeBase64 = Base64.encodeBase64String(data);
        
        File imgFile = new File(imgPath);
        if(imgFile.exists()){
        	imgFile.delete();
        }
        
		return imageCodeBase64;
	}

}
