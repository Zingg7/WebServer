WebServer_v11

本版本开始,实现服务端处理业务工作

实际上网时,我们经常在某网站做注册,登录等操作,这时需要用户在页面上提交某些数据
并且后台服务端要通过业务逻辑代码配合完成该操作

本版本先完成如何让用户在页面上可以提交数据,并且服务端如何通过解析请求得到用户提交的数据

实现:
1) 首先在myweb这个webapp中添加一个注册页面:reg.html
  在这个页面完成用户提交注册信息的操作
2) 修改HttpRequest添加对含有参数的请求的解析工作
  由于当卡户端使用get请求提交一个form表单时,请求中含有了参数
  因此,一个请求中的请求行就有两种情况:
  	不含有参数的,如:myweb/index.html
  	有参数的,如:/myweb/reg?username=zhangsan&password=123&...
  所以我们要对请求中请求行的url部分做进一步的解析工作
  
  在HttpRequest中在定义三个属性:
  requestURI: 保存url中?左侧的请求部分
  queryString: 保存url中?右侧的参数部分
  Map parameters: 用来保存每一组参数
  
  再定义一个方法:parseURL,用来进一步解析url,并在解析请求行parseRequestLine方法中得到url后调用
  