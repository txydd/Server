package com.txy.server.servlets;

import com.google.gson.JsonObject;
import com.txy.server.jdbc.Derby;
import com.txy.server.utils.AES;
import com.txy.server.utils.RSA;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.crypto.SecretKey;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.*;


public class UploadServlet extends HttpServlet {

    //上传文件接口
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SimpleDateFormat df1 = new SimpleDateFormat("yyyyMMdd");
        String time1 = df1.format(new Date());
        String publicKeyStr = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwATptj0176s6LwtOP/u4EJmUY6FDKLwd\n" +
                "qowqMyea2dazgoy9QhuWyPnVn5xTIYPbC7ykyGEj5TQ+5GkM/poST93A7oafdZwTZ3S8DAg3DqGO\n" +
                "kk4b1hyFAR6iP1lqlHFmpg7esW3OADDTJcsc/Unu/4MD8PYx+hxevFzqHg1Fv6BOKgEQZ4WS2I8/\n" +
                "250on4CaAZvISXQ1ySTM0jQjH+tpdnboCIZhDhzZAa5JOA3XjBscyX7a8fNh3Vfk5t6HlL9zXN8R\n" +
                "NQ6cqdSMacxHILW99e6WYmvVDRz84HocMuzEWVrxD6DvEDFxiy8AqcESt84TMy7oo9BJPxalMwH4\n" +
                "Fe6fawIDAQAB";
        File path = new File("D:\\upload\\" + time1);
        if (!path.exists()) {
            path.mkdir();
        }
        try {
            PublicKey publicKey = RSA.string2PublicKey(publicKeyStr);
            String a=request.getHeader("SID");
            String b=request.getHeader("Signature");
            System.out.println("upload的doPost的a为"+a);
            Boolean t=RSA.verify(a,publicKey,b);
            if(!t){
                response.sendError(403);
            }
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            List<FileItem> list = upload.parseRequest(request);
            FileItem item = list.get(0);
            long length = item.getSize();
            System.out.println("名字为" + item.getFieldName());
            InputStream in = item.getInputStream();
            UUID uuid= UUID.randomUUID();
            String UUID=uuid.toString();
            System.out.println("UUID为"+UUID);
            String filePath = path.getPath() + File.separator + UUID;
            String size = getPrintSize(length);
            String name = item.getFieldName();
            String type = name.substring(name.lastIndexOf(".")+1);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = df.format(new Date());
            String address = "D:\\upload\\" + time1 +"\\" +UUID;


            String aesKeyStr = AES.genKeyAES();
            SecretKey aesKey = AES.loadKeyAES(aesKeyStr);
            AES.encryptFile(in,filePath,aesKey);
            //AES.decryptedFile("D:\\upload\\999","D:\\upload\\语音999.jpg",aesKey);
            byte[] publicEncrypt = RSA.publicEncrypt(aesKeyStr.getBytes(), publicKey);
            //公钥加密AES秘钥后的内容Base64编码
            String password = RSA.byte2Base64(publicEncrypt);
            Derby.insert(UUID,size,type,name,time,address,password);

            /*FileOutputStream out = new FileOutputStream(filePath);
            //创建一个缓冲区
            byte buffer[] = new byte[1024];
            //判断输入流中的数据是否已经读完的标识
            int len = 0;
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            //关闭输入流
            in.close();
            //关闭输出流
            out.close();*/

            System.out.println("文件位置为" + filePath);
            PrintWriter out1 = response.getWriter();
            out1.print(UUID);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取元数据接口
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String a=request.getHeader("SID");
        String b=request.getHeader("Signature");
        System.out.println("upload的doget的a为"+a);
        System.out.println("upload的doget的b为"+b);
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

        String uuid=request.getParameter("UUID");
        System.out.println("uuid为"+uuid);
        JsonObject obj= Derby.getMessage(uuid);
        System.out.println(obj.toString());
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(obj.toString());


    }

    //计算文件大小类
    public static String getPrintSize(long size) {
        if (size < 1024) {
            return String.valueOf(size) + "B";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            return String.valueOf(size) + "KB";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            size = size * 100;
            return String.valueOf((size / 100)) + "." + String.valueOf((size % 100)) + "MB";
        } else {
            size = size * 100 / 1024;
            return String.valueOf((size / 100)) + "." + String.valueOf((size % 100)) + "GB";
        }
    }
}
