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
 * @description 登记借阅
 * @date 2020/7/13 11:44
 */

public class BorrowBook extends HttpServlet {

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
        String back = "B11";

        // 接受数据
        String user_id = request.getParameter("user_id");
        String session = request.getParameter("session");

        // 检测是否缺参数和SQL注入
        back += MissParameter.allNotNullEmpty(user_id, session);
        back += SQLInjection.SQLInjectionTest(user_id, session);

        if ("B11".equals(back)) {
            // 不缺参数，开始登录 back = ""
            back += service.sessionLogin(user_id, session);

            if ("B11A0300".equals(back)) {
                // 如果登录成功 开始查询 back = "B11A0300"

                // 登陆成功 验证管理身份
                back += service.verifyManager(user_id);

                if ("B11A0300A0500".equals(back)) {

                    // 接受书籍ID
                    String book_id = request.getParameter("book_id");
                    // 接收借阅用户ID
                    String borrow_id = request.getParameter("borrow_id");

                    // 检测是否缺参数和SQL注入
                    back += MissParameter.allNotNullEmpty(book_id, borrow_id);
                    back += SQLInjection.SQLInjectionTest(book_id, borrow_id);

                    if ("B11A0300A0500".equals(back)) {
                        // 参数合法，开始查询 back = "B11A0300A0500"

                        // 查询并获取orderBook返回值
                        back += service.borrowBook(book_id, borrow_id);
                    }
                }
            }
        }

        // 返回信息到客户端
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // TODO return code B11
        System.out.println("B11:"+back);

        out.print("{[recode:"+back+"]};");
        out.flush();
        out.close();
    }

}
