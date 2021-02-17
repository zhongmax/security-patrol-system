package com.csmaxwell.sps.base.common.exception;

import com.csmaxwell.sps.base.common.api.IErrorCode;

/**
 * 断言处理类，用于抛出各种API异常
 * Created by maxwell on 2021/1/27.
 */
public class Asserts {

    public static void fail(String message) {
        throw new ApiException(message);
    }

    public static void fail(IErrorCode errorCode) {
        throw new ApiException(errorCode);
    }

}
