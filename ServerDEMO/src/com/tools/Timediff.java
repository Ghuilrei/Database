package com.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @version V1.0
 * @author Ghuilrei
 * @className: Timediff
 * @packageName: com.tools
 * @description: 计算时间差
 * @date 2020-07-02 09:17
 **/

public class Timediff {
    /**
     * @description:
     * @param begin 字符串：开始时间
     * @param end 字符串：结束时间
     * @return 返回长整形，单位秒
     */
    public static long timediff(String begin, String end){
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long between = -1;
        try {
            java.util.Date begin1=dfs.parse(begin);
            java.util.Date end1 = dfs.parse(end);
            between = (end1.getTime()-begin1.getTime())/1000;
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("Exception On Timediff");
        }
        return between;
    }
}