package com.txy.server.utils;

import com.google.gson.JsonObject;
import com.txy.server.jdbc.Derby;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.security.*;
import java.util.Random;
import java.util.Scanner;

//AES加密工具类
public class AES {
    //生成AES秘钥，然后Base64编码
    public static String genKeyAES() throws Exception{
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        SecretKey key = keyGen.generateKey();
        String base64Str = byte2Base64(key.getEncoded());
        return base64Str;
    }

    //将Base64编码后的AES秘钥转换成SecretKey对象
    public static SecretKey loadKeyAES(String base64Key) throws Exception{
        byte[] bytes = base642Byte(base64Key);
        SecretKeySpec key = new SecretKeySpec(bytes, "AES");
        return key;
    }

    //字节数组转Base64编码
    public static String byte2Base64(byte[] bytes){
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(bytes);
    }

    //Base64编码转字节数组
    public static byte[] base642Byte(String base64Key) throws IOException{
        BASE64Decoder decoder = new BASE64Decoder();
        return decoder.decodeBuffer(base64Key);
    }


    //测试main类
    public static void main(String[] args) throws Exception{
        Derby.getList();


    }
    //加密
    public static void encryptFile(InputStream fis,String encryptedFileName,SecretKey key){

        try {
           // FileInputStream fis = new FileInputStream(fileName);
            FileOutputStream fos = new FileOutputStream(encryptedFileName);

           /* //秘钥自动生成
            KeyGenerator keyGenerator=KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            Key key=keyGenerator.generateKey();
            byte[] keyValue=key.getEncoded();
            fos.write(keyValue);//记录输入的加密密码的消息摘要
            SecretKeySpec encryKey= new SecretKeySpec(keyValue,"AES");//加密秘钥
            Cipher cipher = Cipher.getInstance("AES/CFB/PKCS5Padding");
            cipher.init(cipher.ENCRYPT_MODE, encryKey,iv);
            byte[] ivValue=new byte[16];
            Random random = new Random(System.currentTimeMillis());
            random.nextBytes(ivValue);
            IvParameterSpec iv = new IvParameterSpec(ivValue);//获取系统时间作为IV */

            fos.write("MyFileEncryptor".getBytes());//文件标识符
            //fos.write(ivValue);	//记录IV
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            CipherInputStream cis=new CipherInputStream(fis, cipher);

            byte[] buffer=new byte[1024];
            int n=0;
            while((n=cis.read(buffer))!=-1){
                fos.write(buffer,0,n);
            }
            cis.close();
            fos.close();
        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    //解密
    public static void decryptedFile(String encryptedFileName,String decryptedFileName,SecretKey key){

        try {
            FileInputStream fis = new FileInputStream(encryptedFileName);
            FileOutputStream fos = new FileOutputStream(decryptedFileName);

            byte[] fileIdentifier=new byte[15];

           /* byte[] keyValue=new byte[16];
            fis.read(keyValue);//读记录的文件加密密码的消息摘要*/
            fis.read(fileIdentifier);
            if(new String (fileIdentifier).equals("MyFileEncryptor")){
                //SecretKeySpec key= new SecretKeySpec(keyValue,"AES");
               /* byte[] ivValue= new byte[16];
                fis.read(ivValue);//获取IV值
                IvParameterSpec iv= new IvParameterSpec(ivValue);
                Cipher cipher = Cipher.getInstance("AES/CFB/PKCS5Padding");
                cipher.init(cipher.DECRYPT_MODE, key,iv);*/
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
                CipherInputStream cis= new CipherInputStream(fis, cipher);
                byte[] buffer=new byte[1024];
                int n=0;
                while((n=cis.read(buffer))!=-1){
                    fos.write(buffer,0,n);
                }
                cis.close();
                fos.close();
                System.out.println("解密成功");
            }else{
                System.out.println("不是我的加密文件");
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }




}
