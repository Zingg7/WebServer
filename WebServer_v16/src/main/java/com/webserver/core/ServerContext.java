package com.webserver.core;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.webserver.servlet.HttpServlet;

/**
 * 服务端所有配置信息
 */
public class ServerContext {
	/*
	 * 所有Servlet
	 * key: 请求路径
	 * value: 具体处理对应业务的Servlet实例 
	 */
	private static Map<String, HttpServlet> SERVLET_MAPPING = new HashMap<>();
	
	// 定义初始化方法,并在静态块中调用,确保ServerContext加载时进行初始化操作
	static {
		// 初始化所有的Servlet
		initServletMapping();
	}
	private static void initServletMapping(){
//		// key保存所有的请求,value保存处理对应请求的Servlet实例  
//		SERVLET_MAPPING.put("/myweb/reg", new RegServlet());
//		SERVLET_MAPPING.put("/myweb/login", new LoginServlet());
		// 加载conf/servlets.xml,将根标签下所有的servlet标签获取到,并将其path属性的值设置为key
		// 将className属性的值取出并利用反射实例化对应的Servlet实例作为value,
		// 保存到SERVLET_MAPPING这个Map中完成初始化
		// 注: 利用反射加载每个Servlet并实例化后,返回的都是Object,但是这些Servlet都继承自HttpServlet
		// 所以价格它们造型成HttpServlet即可,然后以value形式存入Map
		try{
			// 创建SAXReader
			SAXReader reader = new SAXReader();
			// 读取指定文件
			Document doc = reader.read(new File("conf/servlets.xml"));
			Element root = doc.getRootElement(); // Servlets
			List<Element> list = root.elements();  // Servlet
			// String key = list.getValue("servlet");
			
				for (Element ele: list){
					// Element keyEle = ele.element("servlet");    // 子标签下标签设置为key
					String key = ele.attributeValue("path");
					String valueTemp = ele.attributeValue("className");
					Class cls = Class.forName(valueTemp);
					Object o = cls.newInstance();
					HttpServlet value = (HttpServlet)(o);
					SERVLET_MAPPING.put(key, value);   // 放入MAP
				}
			} catch(Exception e){
				e.printStackTrace();
			}
	}
	
	// 根据请求路径获取对应的Servlet
	public static HttpServlet getServlet(String path){
		return SERVLET_MAPPING.get(path);
	}
	
	public static void main(String[] args) {
		HttpServlet servlet = getServlet("/myweb/reg");
		System.out.println(servlet);
	}
}
