package com.manager;

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
 * @description 管理员-管理书籍
 * @date 2020/7/10 14:59
 */

public class Book extends HttpServlet {

    /** 操作数据库方法集合 **/
    Service service;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        service = new Service();

        // 返回值
        String back = "B10";

        // 获取操作 UP or DOWN
        String operate = request.getParameter("operate");
        // 用户ID
        String userId = request.getParameter("user_id");
        // 用户session
        String session = request.getParameter("session");
//        // 用户身份（管理员 or 学生）
//        String isManager = request.getParameter("ismanager");

        // 检测是否合法
        back += MissParameter.allNotNullEmpty(operate, userId, session);
        back += SQLInjection.SQLInjectionTest(operate, userId, session);

        if ("B10".equals(back)) {
            // 参数合法 开始登陆
            back += service.sessionLogin(userId, session);

            if ("B10A0300".equals(back)) {
                // 登陆成功 验证管理身份
                back += service.verifyManager(userId);

                if ("B10A0300A0500".equals(back)) {
                    // 验证管理员成功 添加书籍
                    if ("up".equals(operate)) {
                        // 添加书籍操作
                        String categoryID = request.getParameter("category_id");
                        String book_name = new String(request.getParameter("book_name").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                        String author = new String(request.getParameter("author").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                        String price = request.getParameter("price");
                        String press = new String(request.getParameter("press").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                        String public_date = request.getParameter("public_date");
                        String remain = request.getParameter("remain");

                        // 检测是否合法
                        back += MissParameter.allNotNullEmpty(categoryID, book_name, author, price, press, public_date, remain);
                        back += SQLInjection.SQLInjectionTest(categoryID, book_name, author, price, press, public_date, remain);

                        if ("B10A0300A0500".equals(back)) {
                            // 参数合法
                            back += service.addBook(categoryID, book_name, author, price, press, public_date, remain);
                        }
                    } else if ("down".equals(operate)){
                        // 删除书籍操作
                        String book_id = request.getParameter("book_id");


                        // 检测是否合法
                        back += MissParameter.allNotNullEmpty(book_id);
                        back += SQLInjection.SQLInjectionTest(book_id);

                        if ("B10A0300A0500".equals(back)) {
                            // 参数合法
                            back += service.delBook(book_id);
                        }
                    }
                }
            }
        }

        // 返回信息到客户端
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // TODO return code B10
        System.out.println("B10:"+back);

        out.print("{[recode:"+back+"]}");
        out.flush();
        out.close();
    }

}
