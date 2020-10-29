package com.luis.queueplugin.rabbitmq.util;

import java.util.ResourceBundle;

/**
 * @author luis
 */
public class ResourceUtil {
    private static final ResourceBundle resourceBundle;

    static{
        resourceBundle = ResourceBundle.getBundle("rabbitmq");
    }

    public static String getKey(String key){
        return resourceBundle.getString(key);
    }

    public static void main(String[] args) {
        System.out.println(getKey("rabbitmq.uri"));
    }

}
