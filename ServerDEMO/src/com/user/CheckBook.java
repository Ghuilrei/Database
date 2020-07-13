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
 * @description 用户查询书籍
 * @date 2020/7/11 19:05
 */

public class CheckBook extends HttpServlet {

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
        String back = "B04";
        String info = "";

        // 接受数据
        String user_id = request.getParameter("user_id");
        String session = request.getParameter("session");

        // 检测是否缺参数
        back += MissParameter.allNotNullEmpty(user_id, session);
        back += SQLInjection.SQLInjectionTest(user_id, session);

        if ("B04".equals(back)) {
            // 不缺参数，开始登录 back = "B04"
            back = service.sessionLogin(user_id, session);

            if ("B04A0300".equals(back)) {
                // 如果登录成功 开始查询 back = "B04A0300"

                // 接受参数
                String book_id = request.getParameter("book_id");
                String category_name = new String(request.getParameter("category_name").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                String book_name = new String(request.getParameter("book_name").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                String author = new String(request.getParameter("author").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                String press = new String(request.getParameter("press").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                String public_date = request.getParameter("public_date");

                // 检测是否缺参数
                back += MissParameter.allNotNull(book_id, category_name, book_name, author, press, public_date);
                back += SQLInjection.SQLInjectionTest(book_id, category_name, book_name, author, press, public_date);

                if ("B04A0300".equals(back)) {
                    // 不缺参数，开始查询 back = "B04A0300"

                    // 任意查询数据处理
                    book_id = book_id.isEmpty() ? "-1" : book_id;
                    category_name = category_name.isEmpty() ? "-1" : category_name;
                    book_name = book_name.isEmpty() ? "-1" : book_name;
                    author = author.isEmpty() ? "-1" : author;
                    press = press.isEmpty() ? "-1" : press;
                    public_date = public_date.isEmpty() ? "2000-01-01" : public_date;

                    // 查询结果数组
                    String[] re;

                    // 查询
                    re = service.checkBook(book_id, category_name, book_name, author, press, public_date);

                    // 获取checkBook返回值
                    back += re[0];

                    if ("B04A0300A0800".equals(back)) {
                        // 如果查询成功
                        info = re[1];
                        for (int i = 2; i < re.length; ++i) {
                            info += ";"+re[i];
                        }
                    }
                }
            }
        }

        // 返回信息到客户端
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // TODO return code B04
        System.out.println("B04:"+back);

        out.print("{[recode:"+back+"];"+info+"");
        out.flush();
        out.close();
    }
}
