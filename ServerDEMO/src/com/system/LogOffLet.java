package com.system;

import com.service.Service;
import com.tools.MissParameter;
import com.tools.SQLInjection;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Ghuilrei
 * @version V1.0
 * @description 退出登录
 * @date 2020/7/12 16:40
 */

public class LogOffLet extends HttpServlet {

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

        String userId = request.getParameter("user_id");
        String session = request.getParameter("session");

        back = MissParameter.allNotNullEmpty(userId, session);
        back += SQLInjection.SQLInjectionTest(userId, session);

        if (back.isEmpty()) {
            back += service.sessionLogin(userId, session);
            if (back.equals("A0300"))  {
                back += service.logOff(userId) + "B03";
            }
        } else {
            back += "B03";
        }

        // 返回信息到客户端
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // TODO return code B03
        System.out.println("B03:"+back);

        out.print("{[recode:"+back+"]}");
        out.flush();
        out.close();
    }
}
