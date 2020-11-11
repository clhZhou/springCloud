package com.luis.io.tomcat.bio;

import com.luis.io.tomcat.bio.http.BioRequest;
import com.luis.io.tomcat.bio.http.BioResponse;
import com.luis.io.tomcat.bio.http.BioServlet;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author luis
 * @date 2020/11/11
 */
public class BioTomcat {
    //J2EE标准

    //1.配置启动端口 8080 ServerSocket IP默认 localhost
    //2.配置web.xml文件，设置servlet-name、servlet-class、url-pattern 集成HttpServlet
    //3.读取配置，url-pattern 、Servlet 建立映射关系 Map servletMapping
    //4.Http请求，发送数据是字符串（带有协议）（有规律的）
    //5.解析协议，拿到url，获取到相应的Servlet，用反射进行实例化
    //6.调用实例化的service() 方法，执行具体的逻辑doGet(),doPost()
    //7.Request、Response是对IO的封装

    //tomcat源码，全局搜索ServerSocket，是入口
    //Netty封装了Nio，出现了boss线程与worker线程

    private int port = 8080;
    private ServerSocket serverSocket;
    private Map<String, BioServlet> servletMap = new HashMap<String, BioServlet>();
    private Properties webXML = new Properties();

    private void init() {
        try {
            String WEB_INF = this.getClass().getResource("/").getPath();
            FileInputStream fileInputStream = new FileInputStream(WEB_INF + "web.properties");
            webXML.load(fileInputStream);
            for (Object k :
                    webXML.keySet()) {
                String key = k.toString();
                if(key.endsWith(".url")){
                    String servletName = key.replaceAll("\\.url$","");
                    String url = webXML.getProperty(key);
                    String className = webXML.getProperty(servletName + ".className");
                    BioServlet bioServlet = (BioServlet) Class.forName(className).newInstance();
                    servletMap.put(url,bioServlet);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start(){
        //初始化配置文件
        init();
        try {
            serverSocket = new ServerSocket(this.port);
            System.out.println("tomcat 已经启动，监听的端口是" + this.port);
            //等待用户请求，使用一个死循环
            while(true){
                Socket socket = serverSocket.accept();
                process(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void process(Socket socket) {
        try {
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();

            BioRequest bioRequest = new BioRequest(inputStream);
            BioResponse bioResponse = new BioResponse(outputStream);
            String url = bioRequest.getUrl();
            if(servletMap.containsKey(url)){
                servletMap.get(url).service(bioRequest,bioResponse);
            }else{
                bioResponse.write("404 - Not Found");
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new BioTomcat().start();
    }

}
