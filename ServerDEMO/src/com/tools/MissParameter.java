package com.tools;

/**
 * @author Ghuilrei
 * @version V1.0
 * @description 检测是否缺少参数
 * @date 2020/7/10 13:15
 */

public class MissParameter {

    /** 空字符串MD5值 **/
    private static final String emp = "7ea86d0e17519e9741e530a47a647f5e";

    /**
     * @description 判断传入字符串是否全不空/空字符串
     * @param msgs 需要检测的字符串们
     * @return 05:
     */
    public static String allIsNotNull(String ... msgs) {

        // TODO msgs 的数量
        System.out.println("msgs的数量:"+msgs.length);

        for (String s : msgs) {
            if (s == null || s.isEmpty() || s.equals(emp)) {
                return "05";
            }

            // TODO missParameter 遍历
            System.out.println("missParameter 遍历" + s);
        }
        return "";
    }
}
