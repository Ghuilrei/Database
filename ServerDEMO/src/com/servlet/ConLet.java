package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;

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
        MySessionContext myc= MySessionContext.getInstance();
        HttpSession sess = myc.getSession(sessionid);

        //返回信息到客户端
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.print(sess.getServletContext());
        out.flush();
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO 自动生成的方法存根

    }

}