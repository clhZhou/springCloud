package com.luis.io.tomcat.bio.http;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author luis
 * @date 2020/11/11
 */
public class BioResponse {

    private OutputStream outputStream;
    public BioResponse(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void write(String s) throws IOException {
        //构造http协议，并遵循http协议
        //状态码200
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("HTTP/1.1 200 OK\n")
                .append("Content-Type: text/html;\n")
                .append("\r\n")
                .append(s);
        outputStream.write(stringBuilder.toString().getBytes());
    }
}
