package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.service.Service;
import com.session.MySessionContext;
import com.tools.MD5;

public class ConLet extends HttpServlet{

    private static final long serialVersionUID = 9036889586892331384L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String guess = request.getParameter("guess");
        guess = new String(guess.getBytes("ISO-8859-1"),"UTF-8");
        String userid = request.getParameter("userid");
        userid = new String(userid.getBytes("ISO-8859-1"),"UTF-8");

        //新建服务对象
        Service service = new Service();

        //返回信息到客户端
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        //验证处理
        boolean logwithoutpd = service.loginwithoutpd(guess, userid);
        if( logwithoutpd ){
            System.out.println("logwithoutpd success");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
            String new_guess = MD5.getMD5String(date+userid);
            boolean rcd = service.updatasession(new_guess, userid, date);
            out.print("");
        }else{
            System.out.println("logwithoutpd fail");
            out.print("false");
        }

        out.flush();
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }

}