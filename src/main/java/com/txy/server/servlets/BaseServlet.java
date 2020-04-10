package com.txy.server.servlets;

import com.txy.server.utils.RSA;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.PublicKey;
import java.util.List;

//测试servlet,无意义
public class BaseServlet extends HttpServlet {
    private static final long serialVersionUID=1L;
    @Override
   /* protected void service(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException,IOException {
        httpServletResponse.setContentType("text/plain");
        PrintWriter printWriter=httpServletResponse.getWriter();
        printWriter.write("Hello txy");
    }*/
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException,IOException{
        String a=httpServletRequest.getHeader("SID");
        String b=httpServletRequest.getHeader("Signature");
        Boolean t=false;
        try{
        String publicKeyStr="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwATptj0176s6LwtOP/u4EJmUY6FDKLwd\n" +
                "qowqMyea2dazgoy9QhuWyPnVn5xTIYPbC7ykyGEj5TQ+5GkM/poST93A7oafdZwTZ3S8DAg3DqGO\n" +
                "kk4b1hyFAR6iP1lqlHFmpg7esW3OADDTJcsc/Unu/4MD8PYx+hxevFzqHg1Fv6BOKgEQZ4WS2I8/\n" +
                "250on4CaAZvISXQ1ySTM0jQjH+tpdnboCIZhDhzZAa5JOA3XjBscyX7a8fNh3Vfk5t6HlL9zXN8R\n" +
                "NQ6cqdSMacxHILW99e6WYmvVDRz84HocMuzEWVrxD6DvEDFxiy8AqcESt84TMy7oo9BJPxalMwH4\n" +
                "Fe6fawIDAQAB";

        PublicKey publicKey = RSA.string2PublicKey(publicKeyStr);
        t=RSA.verify(a,publicKey,b);}
        catch(Exception e){
            e.printStackTrace();
        }

        System.out.println("a为"+a);
        System.out.println("b为"+b);
        if(!t){
            httpServletResponse.sendError(403);
        }
        PrintWriter out = httpServletResponse.getWriter();
        out.println(t);

    }
}
