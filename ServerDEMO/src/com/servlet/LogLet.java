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

public class LogLet extends HttpServlet{
 
	private static final long serialVersionUID = 9036889586892331384L;
	public MySessionContext myc= MySessionContext.getInstance();
 
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
		
		//返回信息到客户端
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");

		PrintWriter out = response.getWriter();
		if( log ){
			response.setCharacterEncoding("UTF=8");
			response.setContentType("text/html;charset=UTF-8");
			//使用request对象的getSession()获取session，如果session不存在则创建一个
			HttpSession session = request.getSession();
			//将数据存储到session中
			session.setAttribute("StuID", userid);
			//获取session的Id
			String sessionId = session.getId();

			myc.addSession(session);

			//判断session是不是新创建的
			if (session.isNew()) {
				out.print(session);
				System.out.println("session创建成功，session的id是："+sessionId);
			}else {
				out.print(session);
				System.out.println("服务器已经存在该session了，session的id是："+sessionId);
			}
		}else{
			out.print("false");
		}
		out.flush();
		out.close();
	}
 
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO 自动生成的方法存根
 
	}
 
}