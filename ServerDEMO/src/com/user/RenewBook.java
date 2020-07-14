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
 * @description 续借书籍
 * @date 2020/7/12 22:15
 */

public class RenewBook extends HttpServlet {


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
        String back = "B07";

        // 接受数据
        String user_id = request.getParameter("user_id");
        String session = request.getParameter("session");

        // 检测是否缺参数和SQL注入
        back += MissParameter.allNotNullEmpty(user_id, session);
        back += SQLInjection.SQLInjectionTest(user_id, session);

        if ("B07".equals(back)) {
            // 不缺参数，开始登录 back = "B07"
            back += service.sessionLogin(user_id, session);

            if ("B07A0300".equals(back)) {
                // 如果登录成功 开始查询 back = "B07A0300"

                // 接受参数
                String book_id = request.getParameter("book_id");

                // 检测是否缺参数和SQL注入
                back += MissParameter.allNotNullEmpty(book_id);
                back += SQLInjection.SQLInjectionTest(book_id);

                if ("B07A0300".equals(back)) {
                    // 参数合法，开始查询 back = "A0300"

                    // 查询并获取orderBook返回值
                    back += service.renewBook(book_id, user_id);
                }
            }
        }

        // 返回信息到客户端
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // TODO return code B07
        System.out.println("B07:"+back);

        out.print("{[recode:"+back+"]};");
        out.flush();
        out.close();
    }

}
