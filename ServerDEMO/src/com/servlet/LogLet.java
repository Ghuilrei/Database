package com.servlet;
 
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.service.Service;
import com.tools.MD5;

public class LogLet extends HttpServlet{
 
	private static final long serialVersionUID = 9036889586892331384L;
 
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//接受客户端信息
		String userid = request.getParameter("userid");
		userid = new String(userid.getBytes("ISO-8859-1"),"UTF-8");
		String password = request.getParameter("password");
		password = new String(password.getBytes("ISO-8859-1"),"UTF-8");
		String ismanager = request.getParameter("manager");
		ismanager = new String(ismanager.getBytes("ISO-8859-1"),"UTF-8");
		System.out.println(userid + ":" + password + ":" + ismanager);
		
		//新建服务对象
		Service service = new Service();
		
		//验证处理
		boolean log = service.login(userid, password);
		if( log ){
			System.out.println("log success");
			//request.getSession().setAttribute("username", username);
		}else{
			System.out.println("log fail");
		}

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
		String guess = MD5.getMD5String(date+userid);
		boolean rcd = service.session(guess, userid, date);

		//返回信息到客户端
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");

		PrintWriter out = response.getWriter();
		if( log ){
			if ( rcd ) {
				out.print(guess);
			}
		}else{
			out.print("false");
		}
		out.flush();
		out.close();
	}
 
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
 
	}
 
}