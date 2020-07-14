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
 * @description 查询历史借阅
 * @date 2020/7/12 20:00
 */

public class CheckHistory extends HttpServlet {

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
        String back = "B06";
        String info = "";

        // 接受数据
        String user_id = request.getParameter("user_id");
        String session = request.getParameter("session");

        // 检测是否缺参数或SQL注入
        back += MissParameter.allNotNullEmpty(user_id, session);
        back += SQLInjection.SQLInjectionTest(user_id, session);

        if ("B06".equals(back)) {
            // 检测通过
            back += service.sessionLogin(user_id, session);
            if ("B06A0300".equals(back)) {
                // 登陆成功

                // 查询结果数组
                String[] re;

                // 查询
                re = service.checkHistory(user_id);

                // 获取checkBook返回值
                back += re[0];

                if ("B06A0300A1000".equals(back)) {
                    // 如果查询成功
                    info = re[1];
                    for (int i = 2; i < re.length; ++i) {
                        info += ";" + re[i];
                    }
                }
            }
        }


        // 返回信息到客户端
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // TODO return code B06
        System.out.println("B06:"+back);

        out.print("{[recode:"+back+"]};"+info);
        out.flush();
        out.close();
    }

}
