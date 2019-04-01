package com.webserver.http;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * HTTPЭ����ض�������
 * 
 * ���������Ŀ���ǽ�����HTTPЭ�鶨������ݶ���������
 * �������������ĸ�����Ҫ�õ�HTTPЭ��Ķ�����,�������������ҵ�
 * @author soft01
 *
 */
public class HttpContext {
	// Content-Type����Դ���͵�ӳ��
	// key: ��Դ����(��׺��
	// value: Content-Type��Ӧ��ֵ
	private static final Map<String, String> MIME_MAPPING = new HashMap<>();
	
	static{
		// ��ʼ��
		initMimeMapping();
	}
	
	// ��ʼ��MIME_MAPPING
	private static void initMimeMapping(){
		// ͨ������conf/web.xml�ļ�,�����е����ͳ�ʼ������
		// 1 ����SAXReader����ȡconfĿ¼�µ�web.xml�ļ�
		// 2 ����Ԫ����������Ϊ<mime-mapping>���ӱ�ǩ��ȡ����
		// 3 �������е�<mime-mapping>��ǩ,�������ӱ�ǩ
		//   <extension>�м���ı���Ϊkey
		//   <mime-type>�м���ı���Ϊvalue
		// ���浽MIME_MAPPING���Map����ɳ�ʼ��
		
		try {
			SAXReader reader = new SAXReader();
			Document doc = reader.read(new File("conf/web.xml"));
			Element root = doc.getRootElement();
			List<Element> list = root.elements("mime-mapping");
			for (Element ele: list){
				Element keyEle = ele.element("extension");
				String key = keyEle.getTextTrim();
				Element valueEle = ele.element("mime-type");
				String value = valueEle.getTextTrim();
				MIME_MAPPING.put(key, value);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		// �������׮���Map��sizeӦ��1000���
		System.out.println(MIME_MAPPING.size());
		
		
	}
	
	// ������Դ��������ȡ��Ӧ��Content-Typeֵ
	// @param ext ��Դ������(��png,html,css��)
	public static String getMimeType(String ext){
		return MIME_MAPPING.get(ext);
	}
	
	public static void main(String[] args) {
		String fileName = "xxx.png";
	
		int index = fileName.lastIndexOf(".")+1;
		String ext = fileName.substring(index);
		String line = HttpContext.getMimeType(ext);
		System.out.println(line);
	
	}
}
