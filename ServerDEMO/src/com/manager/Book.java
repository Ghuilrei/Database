package com.manager;

import com.service.Service;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
        String back = "";

        // 获取操作 UP or DOWN
        String operate = request.getParameter("operate");
        // 用户ID
        String userId = request.getParameter("userid");
        // 用户身份（管理员 or 学生）
        String isManager = request.getParameter("ismanager");

    }

}
