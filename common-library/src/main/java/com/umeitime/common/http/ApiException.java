package com.umeitime.common.http;


import com.umeitime.common.base.BaseCommonValue;

//ApiException.java
public class ApiException extends RuntimeException {
    public int mErrorCode;

    public ApiException(int errorCode, String errorMessage) {
        super(errorMessage);
        mErrorCode = errorCode;
    }

    /**
     * 判断是否是token失效
     *
     * @return 失效返回true, 否则返回false;
     */
    public boolean isTokenExpried() {
        return mErrorCode == BaseCommonValue.TOKEN_EXPRIED;
    }
}
