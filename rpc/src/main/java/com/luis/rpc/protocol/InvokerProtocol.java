package com.luis.rpc.protocol;

import lombok.Data;

import java.io.Serializable;

/**
 * @author luis
 * @date 2020/11/12
 */
@Data
public class InvokerProtocol implements Serializable {

    //服务名
    private String className;
    //方法名
    private String method;
    //形参列表
    private Class<?> [] prames;
    //实参列表
    private Object[] values;
}
