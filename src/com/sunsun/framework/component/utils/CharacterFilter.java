
package com.sunsun.framework.component.utils;

import java.util.regex.PatternSyntaxException;

/**
 * 描述:特殊字符过滤
 * 
 */
public class CharacterFilter {

    public static String FileNameFilter(String str) throws PatternSyntaxException {
        // 只允许字母和数字
        // String regEx = "[^a-zA-Z0-9]";
        // 清除掉所有特殊字符
        // String regEx =
        // "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        str = str.replace(" ", "");
        // String regEx = "[`~*|':'\\[\\]<>/?]";
        // Pattern p = Pattern.compile(regEx);
        // Matcher m = p.matcher(str);
        return str.replaceAll("[\\\\/*?<>:\"|]", "").trim();
    }
}
