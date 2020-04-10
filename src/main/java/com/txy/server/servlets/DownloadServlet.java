package com.txy.server.servlets;
import com.google.gson.JsonObject;
import com.txy.server.jdbc.Derby;
import com.txy.server.utils.RSA;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.security.PublicKey;

public class DownloadServlet extends HttpServlet {

    //下载文件接口
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
        String a=request.getHeader("SID");
        System.out.println("download的doGet的a为"+a);
        String b=request.getHeader("Signature");
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
            }

        String uuid = request.getParameter("UUID");
        JsonObject obj=Derby.getMessage(uuid);
        String address=obj.get("address").getAsString();
        System.out.println("地址为"+address);


        //response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName,"UTF-8"));
        response.setCharacterEncoding("UTF-8");

        //获取要下载文件的绝对路径

        ServletOutputStream out = response.getOutputStream();
        InputStream in = new FileInputStream(address);
        int len = 0;
        byte[] buffer = new byte[1024];
        while((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
        in.close();
        out.close();
        }catch(Exception e){
            response.sendError(410);
            e.printStackTrace();
        }


    }
}
