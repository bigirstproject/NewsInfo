
package com.sunsun.framework.component.utils;

import java.util.HashMap;

/**
 * 描述: 友盟有关工具
 * 
 */
public class UmengUtil {
    public static final String REQUEST_FAIL_0 = "request_fail_0";

    public static final String REQUEST_FAIL_1 = "request_fail_1";

    public static final String REQUEST_FAIL_2 = "request_fail_2";

    public static final String REQUEST_FAIL_5 = "request_fail_5";

    public static final String REQUEST_FAIL_10 = "request_fail_10";

    public static final String REQUEST_FAIL_50 = "request_fail_50";

    public static final String REQUEST_FAIL_99 = "request_fail_99";

    public static final String REQUEST_FAIL_100 = "request_fail_100";

    public static final String EVENT_ID_REQUEST_FAIL_COUNT = "request_fail_count";

    public static HashMap<String, String> errorMap = null;

    public static void resetErrorMap() {
        errorMap = new HashMap<String, String>();
    }

    public static void addErrorMap(String mes) {
        if (errorMap == null) {
            errorMap = new HashMap<String, String>();
        }

        if (errorMap.containsKey(mes)) {
            int count = Integer.valueOf(errorMap.get(mes));

            errorMap.remove(mes);
            errorMap.put(mes, String.valueOf(++count));
        } else {
            errorMap.put(mes, "1");
        }
    }
}
