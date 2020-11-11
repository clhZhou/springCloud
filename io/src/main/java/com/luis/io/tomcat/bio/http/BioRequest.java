package com.luis.io.tomcat.bio.http;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author luis
 * @date 2020/11/11
 */
public class BioRequest {
    private String url;
    private String method;

    public BioRequest(InputStream inputStream) throws IOException {
        String content = "";
        byte[] buff = new byte[1024];
        int len = 0;
        if ((len = inputStream.read(buff)) > 0) {
            content = new String(buff, 0, len);
        }
        System.out.println(content);

        String line = content.split("\\n")[0];
        String[] arr = line.split("\\s");
        this.method = arr[0];
        this.url = arr[1].split("\\?")[0];
    }

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }
}
