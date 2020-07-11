package com.servlet;
 
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import com.service.Service;
import com.tools.MissParameter;

/**
 * @description 操作数据库
 * @author Ghuilrei
 * @date 2020/7/9 17:38
 * @version V1.0
 */

public class RegLet extends HttpServlet{

	/** 操作数据库 **/
	Service service;
 
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		doPost(request, response);
	}
 
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

		// 实例化
		service = new Service();

		// 返回值
		String back = "";

		// 用户ID
		String userId = request.getParameter("userid");
		// 用户名
		String userName = request.getParameter("username");
		// 用户密码
		String password = request.getParameter("password");



		back = MissParameter.allIsNotNull(userId, userName, password);

		if (back.isEmpty()) {
			back = service.regester(userId, userName, password);
		} else {
			back = back + "03";
		}

		// 返回信息到客户端
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.print(back);
		out.flush();
		out.close();
	}
 
}