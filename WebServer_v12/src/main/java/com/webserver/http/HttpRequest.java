package com.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 请求对象(解析请求) 
 * 该类的的每一个实例用于表示一个HTTP请求内容
 * 
 * 一个HTTP请求包含三个部分 请求行,消息头,消息正文
 * 
 * @author soft01
 *
 */
public class HttpRequest {
	/*
	 * 相关属性定义
	 */
	// 请求行相关信息定义
	private String method;    // 1.请求方式
	private String url;       // 2.请求资源的抽象路径
	private String protocol;  // 3.请求使用的HTTP协议版本
	
	private String requestURI;    // 保存url中?左侧的请求部分
	private String queryString;   // 保存url中?右侧的参数部分
	private Map<String, String> parameters = new HashMap<>();   // 用来保存每一组参数,key:参数名,value:参数值

	// 消息头相关信息定义
	private Map<String, String> headers = new HashMap<>();
	
	// 消息正文相关信息定义

	//和连接相关消息定义
	private Socket socket;
	private InputStream in;
	
	
	/* 
	 * 构造方法,用来初始化请求对象
	 */
	public HttpRequest(Socket socket) {
		try {
			this.socket = socket;
			this.in = socket.getInputStream();
			/*
			   1. 解析请求行
			   2. 解析消息头
			   3. 解析消息正文
			 */
			parseRequestLine();
			parseHeaders();
			parseContent();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	// 编写构造方法中调用的三步
	// 1. 解析请求行----读取客户端发送过来的内容
	private void parseRequestLine(){
		System.out.println("HttpRequest: 解析请求行");
		try {
			/*
			        通过Socket获取的输入流读取客户端发送过来的请求中第一行字符串
			        这一行应当就是请求行的内容了
			        读取后再将请求行按照空格进行拆分,这时应当可以拆分出三项内容:
			   method, url, protocol
			        我们再将它们设置到对应的属性上即完成解析
			 */
			String line = readLine();
			// 这里后期可以循环接受客户端请求时偶尔会出现下标越界的情况.实际是因为空请求的问题.后期解决
			String[] str = line.split("\\s");
			method = str[0];
			url = str[1];       // 后期这里可能会越界
			parseURL();        // 进一步解析url
			protocol = str[2];
			System.out.println("method: "+method);
			System.out.println("url: "+url);
			System.out.println("protocol: "+protocol);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("HttpRequest: 请求行解析完毕");
	}
	// 进一步解析请求行中的url部分,因为一个url可能有两种情况:含有参数或不含有参数,如果含有参数要对参数进行解析
	private void parseURL(){
		System.out.println("HttpRequest: 进一步解析url");
		/* url有两种情况
		 * 1. 不含有参数的,如:myweb/index.html: 这种情况直接将url的值设置到属性requestURI上即可
		 * 2. 有参数的,如:/myweb/reg?username=zhangsan&password=123&...: 
		 * 这种情况我们首先将url按照"?"拆分成两部分:
		 * 第一部分为请求部分,赋值给requestURI,第二部分为参数部分,赋值给queryString
		 * 然后再对参数部分进一步拆分:
		 * 首先按照"&"拆分出每一组参数,然后每一组参数在按照"="拆分成两部分,分别是参数名和参数值,
		 * 再将它们以key,value存入到parameter这个Map类型属性完成工作0
		 */
		String[] split = url.split("\\?"); 
		requestURI = split[0];
		if (split.length>1){
			queryString = split[1];
			split = queryString.split("&");           // 进一步拆分函数 
			for(String paraStr : split){
				String[] arr = paraStr.split("=");    // 每个单位再按照"="拆分
				if (arr.length > 1){
					parameters.put(arr[0], arr[1]);
				}else{
					parameters.put(arr[0], null);
				}
			}
		}
		
		System.out.println("requestURI:"+requestURI);
		System.out.println("queryString:"+queryString);
		System.out.println("parameters:"+parameters);
		System.out.println("HttpRequest: 解析url完毕");
	}
	
	// 2. 解析消息头
	private void parseHeaders(){
		System.out.println("HttpRequest: 解析消息头");
		/* 
		   parseRequestLine方法已经通过输入流将请求中的请求行内容读取完毕
		        那么到paseHeader这个方法时再通过输入流读取的内容就应当是消息头部分了
		  
		         解析思路:
		         顺序读取若干行字符串,每一行都是一个消息头
		         将消息头按照"冒号空格"(: )拆分乘两部分,分别是消息头的名字与对应的值
		         并将每个消息头的名字作为key,消息头的值作为value,保存到headers这个map中既可完成解析操作
		         如果读取一行字符串时返回的时一个空字符串,即:"",则说明本次单独读取到了CRLF(被trim)
		         那么就可以停止读取工作了
		   
		 */
		try {
			while (true) {
				String line = readLine();
				if ("".equals(line)) {
					break;
				}
				String[] str = line.split(": ");
				headers.put(str[0], str[1]);
			}
			System.out.println("headers: "+headers);
			/*Set<Entry<String, String>> entrySet = headers.entrySet();
			for (Entry<String, String> entry : entrySet) {
				String key = entry.getKey();
				String value = entry.getValue();
				System.out.println(key + ": " + value);
			}*/
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("HttpRequest: 消息头解析完毕");
	}
	
	// 3. 解析消息正文
	private void parseContent(){
		System.out.println("HttpRequest: 解析消息正文");

		
		System.out.println("HttpRequest: 消息正文解析完毕");
	}
	
	// 通过对应客户端的输入流,读取一行客户端发送过来的字符串(1.2.3中均使用)
	private String readLine() throws IOException {
		// 读取客户端发送过来的内容
		// (InputStream输入流已在外构建好)
		StringBuilder builder = new StringBuilder();
		int c1 = -1;
		int c2 = -1;
		while((c2 = in.read() ) != -1){
			if (c1 == 13 && c2 == 10){
				break;
			}
			builder.append((char)c2);
			c1 = c2;
		}
		return builder.toString().trim();
	}

	
	/*
	 * 这里HttpRequest对应属性仅对外提供get方法
	 * 因为请求内容是客户端发送过来的,所以不需要做其他改动,对外是只读的既可
	 */
	public String getMethod() {
		return method;
	}
	public String getUrl() {
		return url;
	}
	public String getProtocol() {
		return protocol;
	}
	// 避免直接返回Map导致用户可以进行put操作
	// 让用户通过输入key获取对应的value
	public String getHeaders(String name) {
		return headers.get(name);
	}

	public String getRequestURI() {
		return requestURI;
	}

	public String getQueryString() {
		return queryString;
	}

	public String getParameters(String name) {
		return parameters.get(name);
	}
	
	
	
}
