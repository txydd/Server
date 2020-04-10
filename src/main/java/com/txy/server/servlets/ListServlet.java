package com.txy.server.servlets;

import com.google.gson.JsonArray;
import com.txy.server.jdbc.Derby;
import com.txy.server.utils.RSA;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.PublicKey;

public class ListServlet  extends HttpServlet {
    //获取最新10条数据接口
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String a=request.getHeader("SID");
        String b=request.getHeader("Signature");
        System.out.println("list的doget的a为"+a);
        System.out.println("list的doget的b为"+b);
        String publicKeyStr = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwATptj0176s6LwtOP/u4EJmUY6FDKLwd\n" +
                "qowqMyea2dazgoy9QhuWyPnVn5xTIYPbC7ykyGEj5TQ+5GkM/poST93A7oafdZwTZ3S8DAg3DqGO\n" +
                "kk4b1hyFAR6iP1lqlHFmpg7esW3OADDTJcsc/Unu/4MD8PYx+hxevFzqHg1Fv6BOKgEQZ4WS2I8/\n" +
                "250on4CaAZvISXQ1ySTM0jQjH+tpdnboCIZhDhzZAa5JOA3XjBscyX7a8fNh3Vfk5t6HlL9zXN8R\n" +
                "NQ6cqdSMacxHILW99e6WYmvVDRz84HocMuzEWVrxD6DvEDFxiy8AqcESt84TMy7oo9BJPxalMwH4\n" +
                "Fe6fawIDAQAB";
        try{
            PublicKey publicKey = RSA.string2PublicKey(publicKeyStr);
            Boolean t=RSA.verify(a,publicKey,b);
            if(!t){
                response.sendError(403);
                System.out.println("验签出问题了");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        JsonArray jsonArray= Derby.getList();
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(jsonArray.toString());
    }
}
