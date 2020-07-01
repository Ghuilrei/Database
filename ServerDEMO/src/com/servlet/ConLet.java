package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.service.Service;
import com.session.MySessionContext;

public class ConLet extends HttpServlet{

    private static final long serialVersionUID = 9036889586892331384L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String sessionid = request.getParameter("sessionid");
        sessionid = new String(sessionid.getBytes("ISO-8859-1"),"UTF-8");

        HttpSession sess = myc.getSession(sessionid);

        System.out.println("SessionID :" + sessionid);

        Enumeration<String> attrs = sess.getAttributeNames();
        // 遍历attrs中的
        while(attrs.hasMoreElements()){
        // 获取session键值
            String name = attrs.nextElement().toString();
            // 根据键值取session中的值
            Object vakue = sess.getAttribute(name);
            // 打印结果
            System.out.println("------" + name + ":" + vakue +"--------\n");

        }

        //返回信息到客户端
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        System.out.println(sess.getServletContext());
        out.print(sess.getServletContext());
        out.flush();
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO 自动生成的方法存根

    }

}