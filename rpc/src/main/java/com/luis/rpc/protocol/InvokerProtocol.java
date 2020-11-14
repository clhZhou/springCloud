package com.luis.rpc.protocol;

import lombok.Data;

import java.io.Serializable;

/**
 * @author luis
 * @date 2020/11/12
 */
public class InvokerProtocol implements Serializable {

    //服务名
    private String className;
    //方法名
    private String methodName;
    //形参列表
    private Class<?> [] parames;
    //实参列表
    private Object[] values;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParames() {
        return parames;
    }

    public void setParames(Class<?>[] parames) {
        this.parames = parames;
    }

    public Object[] getValues() {
        return values;
    }

    public void setValues(Object[] values) {
        this.values = values;
    }
}
