package com.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ghuilrei
 * @version V1.0
 * @description 防止SQL注入
 * @date 2020/7/10 23:18
 */

public class SQLInjection {
    static String reg = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|"
            + "(\\b(select|update|and|or|delete|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute)\\b)";

    /** 表示忽略大小写 **/
    static Pattern sqlPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);

    public static String SQLInjectionTest(String ... msgs) {
        for (String s : msgs) {
            if (isSqlValid(s)) {
                return "T0001";
            }

            // TODO missParameter 遍历
            System.out.println("SQLInjectionTest 遍历" + s);
        }
        return "";
    }

    public static boolean isSqlValid(String str) {
        str = str==null ? "" : str;
        Matcher matcher = sqlPattern.matcher(str);
        if (matcher.find()) {
            //获取非法字符：or
            System.out.println("参数存在非法字符，请确认："+matcher.group());
            return true;
        }
        return false;
    }
}
