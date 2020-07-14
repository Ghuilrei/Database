package com.user;

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
 * @description 查询个人信息
 * @date 2020/7/12 22:28
 */

public class CheckUser extends HttpServlet {

    /** 操作数据库 **/
    Service service;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        service = new Service();

        // 返回的数据
        String back = "B08";
        String info = "";

        // 接受数据
        String user_id = request.getParameter("user_id");
        String session = request.getParameter("session");

        // 检测是否缺参数
        back += MissParameter.allNotNullEmpty(user_id, session);
        back += SQLInjection.SQLInjectionTest(user_id, session);

        if ("B08".equals(back)) {
            // 不缺参数，开始登录 back = "B08"
            back += service.sessionLogin(user_id, session);

            if ("B08A0300".equals(back)) {
                // 如果登录成功 开始查询 back = "B08A0300"
                info = service.checkUser(user_id);
            }
        }

        // 返回信息到客户端
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // TODO return code B08
        System.out.println("B08:"+back);

        out.print("{[recode:"+back+"]};"+info);
        out.flush();
        out.close();
    }
}
