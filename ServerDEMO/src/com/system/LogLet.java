package com.system;
 
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.service.Service;
import com.tools.MissParameter;
import com.tools.SQLInjection;

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
		back = MissParameter.allNotNullEmpty(kind);
		if (back.isEmpty()) {

			switch (kind) {
				// Session登陆
				case "session":
					// 用户ID
					String user_id = request.getParameter("user_id");
					// session
					String session = request.getParameter("session");
					back = MissParameter.allNotNullEmpty(user_id, session);
					back += SQLInjection.SQLInjectionTest(user_id, session);
					if (back.isEmpty()) {
						// 获取返回值
						back = sessionLogin(user_id, session);
					} else {
						back = back + "B0102";
					}
					break;
				// 用户名-密码登陆
				case "password":
					// 用户手机号
					String phone = request.getParameter("phone");
					// 密码
					String password = request.getParameter("password");

					back = MissParameter.allNotNullEmpty(phone, password);
					back += SQLInjection.SQLInjectionTest(phone, password);
					if (back.isEmpty()) {
						// 获取返回值
						back = passwordLogin(phone, password);
					} else {
						back = back + "B0103";
					}
					break;
				default:
					break;
			}
		} else {
			back = back + "B0101";
		}

		// 返回信息到客户端
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		// TODO return code B01
		System.out.println("B01:"+back);

		out.print("{[recode:"+back+"]}");
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
		String result = service.sessionLogin(userid, session);
		if ("A0300".equals(result)) {
			return "{[session:" + service.updateSession("-1", userid) + "]," + service.UserBase(userid, "-1")+"}";
		} else {
			return result;
		}
	}

	/**
	 * @description Password登陆
	 * @param phone String：用户手机号
	 * @param password String：密码
	 * @return 返回结果
	 */
	public String passwordLogin(String phone, String password){
		String result = service.passwordLogin(phone, password);
		if ("A0200".equals(result)) {
			return "{[session:" + service.updateSession(phone, "-1") + "]," + service.UserBase("-1", phone)+"}";
		} else {
			return result;
		}
	}
 
}