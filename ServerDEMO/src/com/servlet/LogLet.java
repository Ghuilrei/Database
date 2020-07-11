package com.servlet;
 
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.service.Service;
import com.tools.MissParameter;

/**
 * @description 登陆
 * @author Ghuilrei
 * @date 2020/7/9 17:38
 * @version V1.0
 */

public class LogLet extends HttpServlet{

	/** 操作数据库 **/
	Service service;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		doPost(request, response);
	}
 
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

		 service = new Service();

		// 返回值
		String back = "";

		// 获取登陆类型
		String kind = request.getParameter("kind");
		// 用户ID
		String userId = request.getParameter("userid");
		back = MissParameter.allIsNotNull(kind, userId);
		if (back.isEmpty()) {
			// TODO kind:
			System.out.println("kind:"+kind);

			switch (kind) {
				// Session登陆
				case "session":
					String session = request.getParameter("session");
					back = MissParameter.allIsNotNull(session);
					if (back.isEmpty()) {
						// 获取返回值
						back = sessionLogin(userId, session);
					} else {
						back = back + "02";
					}
					break;
				// 用户名-密码登陆
				case "password":
					String password = request.getParameter("password");

					// TODO password:
					System.out.println("password:" + password);

					back = MissParameter.allIsNotNull(password);
					if (back.isEmpty()) {
						// 获取返回值
						back = passwordLogin(userId, password);
					} else {
						back = back + "03";
					}
					break;
				default:
					break;
			}
		} else {
			back = back + "01";
		}

		// TODO LogLet返回值：
		System.out.println("LogLet返回值："+back);

		// 返回信息到客户端
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println(back);
		out.print("hello!");
		out.flush();
		out.close();
	}

	/**
	 * @description Session登陆
	 * @param userid String：用户ID
	 * @param session String：Session
	 * @return 返回结果
	 * 	登陆成功："{[userId:***],[name:***]}"
	 * 	登陆失败：错误码
	 */
	public String sessionLogin(String userid, String session){
		return service.sessionLogin(userid, session);
	}

	/**
	 * @description Password登陆
	 * @param userid String：用户ID
	 * @param password String：密码
	 * @return 返回结果
	 * 	登陆成功："{[userId:***],[name:***]}"
	 * 	登陆过期："Login expired."
	 * 	登陆失败："Login error."
	 */
	public String passwordLogin(String userid, String password){
		String result = service.passwordLogin(userid, password);
		if ("false".equals(result)) {
			result = "Login error.";
		} else {
			service.updataSession(userid);
		}
		return result;
	}
 
}