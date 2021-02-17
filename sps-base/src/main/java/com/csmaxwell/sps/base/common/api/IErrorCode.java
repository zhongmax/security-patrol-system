package com.csmaxwell.sps.base.common.api;

/**
 * 封装API错误码
 * Created by maxwell on 2021/1/27.
 */
public interface IErrorCode {
    long getCode();

    String getMessage();
}
