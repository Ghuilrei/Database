package com.user;

import com.service.Service;
import com.tools.MissParameter;
import com.tools.SQLInjection;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * @author Ghuilrei
 * @version V1.0
 * @description 更新个人信息
 * @date 2020/7/12 22:45
 */

public class UpdateUser extends HttpServlet {

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
        String back = "B09";

        // 接受数据
        String user_id = request.getParameter("user_id");
        String session = request.getParameter("session");

        // 检测是否缺参数
        back += MissParameter.allNotNullEmpty(user_id, session);
        back += SQLInjection.SQLInjectionTest(user_id, session);

        if ("B09".equals(back)) {
            // 不缺参数，开始登录 back = ""
            back += service.sessionLogin(user_id, session);

            if ("B09A0300".equals(back)) {
                // 如果登录成功 开始查询 back = "B0901A0300"
                String user_name = new String(request.getParameter("user_name").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                String sex = new String(request.getParameter("sex").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                String age = new String(request.getParameter("age").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                String phone = request.getParameter("phone");

                // 参数非法检测
                back += MissParameter.allNotNullEmpty(user_name, sex, age, phone);
                back += SQLInjection.SQLInjectionTest(user_name, sex, age, phone);

                if ("B09A0300".equals(back)) {
                    // 参数合法
                    back += service.updateUser(user_id, user_name, sex, age, phone);
                }
            }
        }
        // 返回信息到客户端
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // TODO return code B08
        System.out.println("B09:"+back);

        out.print("{[recode:"+back+"]};");
        out.flush();
        out.close();
    }

}
