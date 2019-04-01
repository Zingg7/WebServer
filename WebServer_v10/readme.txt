WebServer_v10

本版本完成两个优化:
1) 每次在响应客户端内容前,通过HttpResponse的setEntity方法设置响应的具体资源文件时,
  后面都要再调用两个添加说明响应正文长度和类型的响应头:Content-Type和Conten-Length.
  因此我们可以将这两个响应头的设置工作迁移到setEntity方法中完成.
  这样每次只需要设置响应正文,而这两个响应头就同时添加了
2) HttpContext中现在支持的资源类型与Content-Type只有几个,Tomcat对此已经整理了所有的类型,共1000多个
  我们直接使用它提供的文件来初始化,使得我们也能支持所有类型 
  
