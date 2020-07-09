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
        String stuid = service.loginwithoutpd(guess, userid);
        switch (stuid) {
            case "false":
                // FIXME System.out.println
                System.out.println("ConLet:42:logwithoutpd false");
                out.print("false");
                break;
            case "null":
                // FIXME System.out.println
                System.out.println("ConLet:46:logwithoutpd null");
                out.print("false");
                break;
            case "err":
                // FIXME System.out.println
                System.out.println("ConLet:50:logwithoutpd err");
                out.print("false");
                break;
            default:
                // FIXME System.out.println
                System.out.println("ConLet:54:logwithoutpd success");
                //设置日期格式
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                // new Date()为获取当前系统时间，也可使用当前时间戳
                String date = df.format(new Date());
                String new_guess = MD5.getMD5String(date + userid);
                boolean rcd = service.session(new_guess, userid, date);
                // FIXME System.out.println
                System.out.println("ConLet:62:rcd:"+rcd);
                out.print("");
                break;
        }

        out.flush();
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }

}