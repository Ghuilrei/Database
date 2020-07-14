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
		String back = "B01";
		// 返回信息
		String info = "";

		// 获取登陆类型
		String kind = request.getParameter("kind");

		// 检测参数是否合法
		back += MissParameter.allNotNullEmpty(kind);
		back += SQLInjection.SQLInjectionTest(kind);

		if ("B01".equals(back)) {
			// 参数合法

			switch (kind) {
				// Session登陆
				case "session":
					// 用户ID
					String user_id = request.getParameter("user_id");
					// session
					String session = request.getParameter("session");
					back += MissParameter.allNotNullEmpty(user_id, session);
					back += SQLInjection.SQLInjectionTest(user_id, session);
					if ("B01".equals(back)) {
						back += "01";
						// 获取返回值
						info = sessionLogin(user_id, session);
						if ("A0302".equals(info) || "A0301".equals(info)) {
							back += info;
							info = "";
						}
					}
					break;
				// 用户名-密码登陆
				case "password":
					// 用户手机号
					String phone = request.getParameter("phone");
					// 密码
					String password = request.getParameter("password");

					back += MissParameter.allNotNullEmpty(phone, password);
					back += SQLInjection.SQLInjectionTest(phone, password);
					if ("B01".equals(back)) {
						back += "02";
						// 获取返回值
						info = passwordLogin(phone, password);
						if ("A0202".equals(info) || "A0201".equals(info)) {
							back += info;
							info = "";
						}
					}
					break;
				default:
					break;
			}
		}

		// 返回信息到客户端
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		// TODO return code B01
		System.out.println("B01:"+back);

		out.print("{[recode:"+back+"]};"+info);
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