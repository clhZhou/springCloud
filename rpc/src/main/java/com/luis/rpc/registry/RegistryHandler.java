package com.luis.rpc.registry;

import com.luis.rpc.protocol.InvokerProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author luis
 * @date 2020/11/12
 */
public class RegistryHandler extends ChannelInboundHandlerAdapter {

    // 1.根据一个包名将所有符合条件的class全部扫描处理出来，放到一个容器中
    // 如果是分布式，就读配置文件
    // 2.给每一个对应的class起一个唯一的名字，作为服务名称，保存到一个容器中
    // 3.当客户端连接过来时候，就会获取协议内容 InvokerProtocol的对象
    // 4.要去注册好的容器中找到服务号条件的服务
    // 5.通过远程调用Provider得到返回结果，并回复给客户端


    private List<String> classNames = new ArrayList<String>();
    private Map<String,Object> registryMap = new HashMap<String,Object>();
    public RegistryHandler() {
        scannerClass("com.luis.rpc.provider");
        doRegistry();
    }

    //正常是扫描配置文件，这里是扫描本地class


    private void scannerClass(String packageName) {
        URL url = this.getClass().getClassLoader().getResource(packageName.replaceAll("\\.","/"));
        File classPath = new File(url.getFile());
        for(File file:classPath.listFiles()){
            if(file.isDirectory()){
                scannerClass(packageName +"."+file.getName());
            }else{
                classNames.add(packageName +"." +file.getName().replace(".class",""));
            }
        }
    }

    private void doRegistry() {
        if(classNames.isEmpty()){
            return;
        }
        for (String className: classNames) {
            try {
                Class<?> clazz = Class.forName(className);
                Class<?> i = clazz.getInterfaces()[0];
                String serviceName = i.getName();
                //这里是放网络路径，从配置文件中读取
                //在调用时候再去解析，现在使用反射调用
                registryMap.put(serviceName,clazz.newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 客户端建立链接回调
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

    }

    /**
     * 客户端发生异常调用
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Object result = new Object();
        InvokerProtocol invokerProtocol = (InvokerProtocol) msg;
        if(registryMap.containsKey(invokerProtocol.getClassName())){
            Object service = registryMap.get(invokerProtocol.getClassName());
            Method method = service.getClass().getMethod(invokerProtocol.getMethodName(),invokerProtocol.getParames());
            result = method.invoke(service,invokerProtocol.getValues());
        }
        ctx.write(result);
        ctx.flush();
        ctx.close();
    }
}
