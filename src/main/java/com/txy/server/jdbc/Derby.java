package com.txy.server.jdbc;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.sql.*;
//数据库操作类
public class Derby {
    //元数据插入
    public static void insert(String uuid,String size,String type,String originalName,String createTime,String address,String secretKey){
        String driver = "org.apache.derby.jdbc.EmbeddedDriver";//在derby.jar里面
        String dbName="EmbeddedDB";
        String dbURL = "jdbc:derby:" + dbName + ";create=true;territory=zh_CN;user=root;password=root";// create=true表示当数据库不存在时就创建它
        PreparedStatement pt = null;
        try {
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(dbURL);//启动嵌入式数据库
            String sql="insert into fileUpload(UUID,SIZE,TYPE,ORIGINALNAME,CREATETIME,ADDRESS,SECRETKEY) values(?,?,?,?,?,?,?)";
            pt=conn.prepareStatement(sql);
            pt.setString(1,uuid);
            pt.setString(2,size);
            pt.setString(3,type);
            pt.setString(4,originalName);
            pt.setString(5,createTime);
            pt.setString(6,address);
            pt.setString(7,secretKey);
            int count = pt.executeUpdate();



        } catch(Exception e){
            e.printStackTrace();
        }
    }

    //元数据搜索
    public static JsonObject getMessage(String uuid){
        String driver = "org.apache.derby.jdbc.EmbeddedDriver";//在derby.jar里面
        String dbName="EmbeddedDB";
        String dbURL = "jdbc:derby:" + dbName + ";create=true;territory=zh_CN;user=root;password=root";// create=true表示当数据库不存在时就创建它
        PreparedStatement pt = null;
        JsonObject obj=new JsonObject();
        try {
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(dbURL);//启动嵌入式数据库
            String sql="select * from fileUpload where UUID=?";
            pt=conn.prepareStatement(sql);
            pt.setString(1,uuid);
            ResultSet s = pt.executeQuery();
            while(s.next()) {
                String size = s.getString(2);
                String type = s.getString(3);
                String name = s.getString(4);
                String time = s.getString(5);
                String address = s.getString(6);
                String password = s.getString(7);
                obj.addProperty("size",size);
                obj.addProperty("type",type);
                obj.addProperty("name",name);
                obj.addProperty("time",time);
                obj.addProperty("address",address);
                obj.addProperty("password",password);
            }


        } catch(Exception e){
            e.printStackTrace();
        }
        return obj;
    }

    //最新10条数据搜索
    public static JsonArray getList(){
        String driver = "org.apache.derby.jdbc.EmbeddedDriver";//在derby.jar里面
        String dbName="EmbeddedDB";
        String dbURL = "jdbc:derby:" + dbName + ";create=true;territory=zh_CN;user=root;password=root";// create=true表示当数据库不存在时就创建它
        PreparedStatement pt = null;
        JsonArray jsonArray=new JsonArray();

        try {
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(dbURL);//启动嵌入式数据库
            String sql="select * from fileUpload order by DATE(CREATETIME) desc fetch first 10 rows only ";
            pt=conn.prepareStatement(sql);

            ResultSet s = pt.executeQuery();

            while(s.next()) {
                String uuid=s.getString(1);
                String size = s.getString(2);
                String type = s.getString(3);
                String name = s.getString(4);
                String time = s.getString(5);
                String address = s.getString(6);
                String password = s.getString(7);
                JsonObject obj=new JsonObject();
                obj.addProperty("uuid",uuid);
                obj.addProperty("size",size);
                obj.addProperty("type",type);
                obj.addProperty("name",name);
                obj.addProperty("time",time);
                obj.addProperty("address",address);
                obj.addProperty("password",password);
                jsonArray.add(obj);
            }


        } catch(Exception e){
            e.printStackTrace();
        }
        return jsonArray;

    }


    //测试main类
    public static void main(String[] args) {
        String driver = "org.apache.derby.jdbc.EmbeddedDriver";//在derby.jar里面
        String dbName="EmbeddedDB";
        String dbURL = "jdbc:derby:" + dbName + ";create=true;territory=zh_CN;user=root;password=root";// create=true表示当数据库不存在时就创建它
        try {
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(dbURL);//启动嵌入式数据库
            Statement st = conn.createStatement();
            //st.execute("alter table fileUpload alter column SECRETKEY set data type VARCHAR(1000)");
            //st.execute("create table fileUpload (UUID VARCHAR(100) NOT NULL,SIZE VARCHAR(100) NOT NULL,TYPE VARCHAR(100) NOT NULL,ORIGINALNAME VARCHAR(100) NOT NULL,CREATETIME VARCHAR(100) NOT NULL,ADDRESS VARCHAR(100) NOT NULL,SECRETKEY VARCHAR(100) NOT NULL)");//创建file表
            //st.executeUpdate("insert into fileTest(UUID,SIZE,TYPE,ORIGINALNAME,CREATETIME,ADDRESS,SECRETKEY) values ('test','100kb','图纸','测试文件','2020-4-2','C:/TXY/test','%685eku')");//插入一条数据
            ResultSet rs = st.executeQuery("select * from fileUpload");//读取刚插入的数据
            while(rs.next()){
                String id = rs.getString(1);
                String size=rs.getString(2);
                String name = rs.getString(4);
                System.out.println("uuid="+id+"size="+size+";name="+name);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}