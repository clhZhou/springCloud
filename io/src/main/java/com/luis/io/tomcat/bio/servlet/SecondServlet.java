package com.luis.io.tomcat.bio.servlet;

import com.luis.io.tomcat.bio.http.BioRequest;
import com.luis.io.tomcat.bio.http.BioResponse;
import com.luis.io.tomcat.bio.http.BioServlet;

/**
 * @author luis
 * @date 2020/11/11
 */
public class SecondServlet extends BioServlet {
    @Override
    protected void doGet(BioRequest bioRequest, BioResponse bioResponse) throws Exception {
        this.doPost(bioRequest,bioResponse);
    }

    @Override
    protected void doPost(BioRequest bioRequest, BioResponse bioResponse) throws Exception {
        bioResponse.write("This is Second Servlet");
    }
}
