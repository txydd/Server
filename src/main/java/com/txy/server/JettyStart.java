package com.txy.server;

import com.txy.server.servlets.BaseServlet;
import com.txy.server.servlets.DownloadServlet;
import com.txy.server.servlets.ListServlet;
import com.txy.server.servlets.UploadServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.server.Server;

import javax.servlet.Servlet;
import java.net.InetSocketAddress;

//Jetty启动类
public class JettyStart {
    private static String host;
    private static String port;
    public static void main(String[] args) throws Exception{
        host="127.0.0.1";
        port="8899";
        InetSocketAddress address=new InetSocketAddress(host,Integer.parseInt(port));
        Server server=new Server(address);
        ServletContextHandler handler=new ServletContextHandler();
        handler.addServlet(BaseServlet.class,"/base");
        handler.addServlet(UploadServlet.class,"/upload");
        handler.addServlet(DownloadServlet.class,"/download");
        handler.addServlet(ListServlet.class,"/list");
        server.setHandler(handler);
        server.start();
    }
}
