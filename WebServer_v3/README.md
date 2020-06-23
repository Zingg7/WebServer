WebServer_v3

本版本中我们设计一个类: HttpRequest,使用这个类的每个实例表示一个客户端发送过来的请求内容

实现步骤:
1) 创建一个新的包com.webserver.http
   使用这个包保存所有有关HTTP协议内容的类
2) 在http包中定义请求对象:HTTPRequest
   并在这个包中定义一个请求信息中各项数据对应的属性
3) 定义构造方法,以及解析请求各个部分的细节方法
4) 在ClientHandler中实例化HttpRequest完成请求的解析工作
