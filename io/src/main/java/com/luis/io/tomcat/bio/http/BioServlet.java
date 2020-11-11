package com.luis.io.tomcat.bio.http;

/**
 * @author luis
 * @date 2020/11/11
 */
public abstract class BioServlet {
    public void service(BioRequest bioRequest, BioResponse bioResponse) throws Exception{
        if("GET".equalsIgnoreCase(bioRequest.getMethod())){
            doGet(bioRequest,bioResponse);
        }else{
            doPost(bioRequest,bioResponse);
        }
    }

    protected abstract void doGet(BioRequest bioRequest, BioResponse bioResponse) throws Exception;

    protected abstract void doPost(BioRequest bioRequest, BioResponse bioResponse) throws Exception;

}
