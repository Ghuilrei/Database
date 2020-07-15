package com.system;
 
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import com.service.Service;
import com.tools.MissParameter;
import com.tools.SQLInjection;
import sun.nio.cs.ISO_8859_2;

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
		String back = "B02";

		// 用户ID
		String phone = request.getParameter("phone");
		// 用户名
		String userName = new String(request.getParameter("user_name").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
		// 用户密码
		String password = request.getParameter("password");
		// 用户身份
		String ismanager = request.getParameter("is_manager");



		back += MissParameter.allNotNullEmpty(phone, userName, password, ismanager);
		back += SQLInjection.SQLInjectionTest(phone, userName, password, ismanager);

		if ("B02".equals(back)) {
			back += service.register(phone, userName, password, ismanager);
		}

		// 返回信息到客户端
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		// TODO return code B02
		System.out.println("B02:"+back);

		out.print("{[recode:"+back+"]};");
		out.flush();
		out.close();
	}
 
}