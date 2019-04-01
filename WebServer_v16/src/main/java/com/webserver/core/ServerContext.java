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
 * ���������������Ϣ
 */
public class ServerContext {
	/*
	 * ����Servlet
	 * key: ����·��
	 * value: ���崦���Ӧҵ���Servletʵ�� 
	 */
	private static Map<String, HttpServlet> SERVLET_MAPPING = new HashMap<>();
	
	// �����ʼ������,���ھ�̬���е���,ȷ��ServerContext����ʱ���г�ʼ������
	static {
		// ��ʼ�����е�Servlet
		initServletMapping();
	}
	private static void initServletMapping(){
//		// key�������е�����,value���洦���Ӧ�����Servletʵ��  
//		SERVLET_MAPPING.put("/myweb/reg", new RegServlet());
//		SERVLET_MAPPING.put("/myweb/login", new LoginServlet());
		// ����conf/servlets.xml,������ǩ�����е�servlet��ǩ��ȡ��,������path���Ե�ֵ����Ϊkey
		// ��className���Ե�ֵȡ�������÷���ʵ������Ӧ��Servletʵ����Ϊvalue,
		// ���浽SERVLET_MAPPING���Map����ɳ�ʼ��
		// ע: ���÷������ÿ��Servlet��ʵ������,���صĶ���Object,������ЩServlet���̳���HttpServlet
		// ���Լ۸��������ͳ�HttpServlet����,Ȼ����value��ʽ����Map
		try{
			// ����SAXReader
			SAXReader reader = new SAXReader();
			// ��ȡָ���ļ�
			Document doc = reader.read(new File("conf/servlets.xml"));
			Element root = doc.getRootElement(); // Servlets
			List<Element> list = root.elements();  // Servlet
			// String key = list.getValue("servlet");
			
				for (Element ele: list){
					// Element keyEle = ele.element("servlet");    // �ӱ�ǩ�±�ǩ����Ϊkey
					String key = ele.attributeValue("path");
					String valueTemp = ele.attributeValue("className");
					Class cls = Class.forName(valueTemp);
					Object o = cls.newInstance();
					HttpServlet value = (HttpServlet)(o);
					SERVLET_MAPPING.put(key, value);   // ����MAP
				}
			} catch(Exception e){
				e.printStackTrace();
			}
	}
	
	// ��������·����ȡ��Ӧ��Servlet
	public static HttpServlet getServlet(String path){
		return SERVLET_MAPPING.get(path);
	}
	
	public static void main(String[] args) {
		HttpServlet servlet = getServlet("/myweb/reg");
		System.out.println(servlet);
	}
}
