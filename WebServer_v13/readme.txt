WebServer_v13

���汾����û���¼����

����ע�����������û���¼

ʵ��:
1) ��webapps/mywebĿ¼���½�����ҳ��
  login.html��¼ҳ��
  �����ҳ���ж���һ��form��,actionָ��"./login", �����ڱ��ж������������ֱ����������û���������,
  ������������name���Էֱ�Ϊ:username,password
  login_success.html
  login_fail.html
  
2) ��com.webserver.servlet���ж����û������¼ҵ�����:LoginServlet������service����

3) ��ClientHandler�������󲿷����һ���µķ�֧,������·����"/myweb/login"��ʵ����LoginServlet��������service���������¼ҵ��


LoginServlet�ĵ�¼ҵ���������
1) ����ͨ��request��ȡ�û��ڱ���������û���������
2) ʹ��RandomAccessFile��ȡuser.dat�ļ�
3) ˳���ȡÿ����¼,���ȶ��û���������
  ���бȶԳɹ���,������response��Ӧ��¼�ɹ�ҳ��
  ���û�����ȷ�����벻��ȷ,��user.dat�ļ���û�и��û�,������response��Ӧ��¼ʧ��ҳ��