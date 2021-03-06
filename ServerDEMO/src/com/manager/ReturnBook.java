package com.manager;

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
 * @description 归还图书
 * @date 2020/7/13 12:00
 */

public class ReturnBook extends HttpServlet {


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
        String back = "B12";

        // 接受数据
        String user_id = request.getParameter("user_id");
        String session = request.getParameter("session");

        // 检测是否缺参数和SQL注入
        back += MissParameter.allNotNullEmpty(user_id, session);
        back += SQLInjection.SQLInjectionTest(user_id, session);

        if ("B12".equals(back)) {
            // 不缺参数，开始登录 back = ""
            back += service.sessionLogin(user_id, session);

            if ("B12A0300".equals(back)) {
                // 如果登录成功 开始查询 back = "B12A0300"

                // 登陆成功 验证管理身份
                back += service.verifyManager(user_id);

                if ("B12A0300A0500".equals(back)) {

                    // 接受参数
                    String book_id = request.getParameter("book_id");
                    // 接收归还用户ID
                    String return_id = request.getParameter("return_id");

                    // 检测是否缺参数和SQL注入
                    back += MissParameter.allNotNullEmpty(book_id, return_id);
                    back += SQLInjection.SQLInjectionTest(book_id, return_id);

                    if ("B12A0300A0500".equals(back)) {
                        // 参数合法，开始查询 back = "B12A0300A0500"

                        // 查询并获取orderBook返回值
                        back += service.returnBook(book_id, return_id);
                    }
                }
            }
        }

        // 返回信息到客户端
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // TODO return code B12
        System.out.println("B12:"+back);

        out.print("{[recode:"+back+"]};");
        out.flush();
        out.close();
    }

}
