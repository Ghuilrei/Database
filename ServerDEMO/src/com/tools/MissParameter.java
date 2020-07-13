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
     * @return 错误码：“0002” 或 成功：“”
     */
    public static String allNotNullEmpty(String ... msgs) {

        for (String s : msgs) {
            if (s == null || s.isEmpty() || s.equals(emp)) {
                return "T0002";
            }

            // TODO missParameter 遍历
            System.out.println("allNotNullEmpty 遍历" + s);
        }
        return "";
    }


    /**
     * @description 判断传入字符串是否不全为空或空字符串
     * @param msgs 需要检测的字符串们
     * @return 错误码：“0003” 或 成功：“”
     */
    public static String allNotNull (String ... msgs) {

        for (String s : msgs) {
            if (s == null) {
                return "T0003";
            }

            // TODO missParameter 遍历
            System.out.println("allNotNull 遍历" + s);
        }
        return "";
    }


}
