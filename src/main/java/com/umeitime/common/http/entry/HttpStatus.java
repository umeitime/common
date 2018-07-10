package com.umeitime.common.http.entry;

import com.google.gson.annotations.SerializedName;
import com.umeitime.common.base.BaseCommonValue;

public class HttpStatus {
    @SerializedName("code")
    private int code;
    @SerializedName("msg")
    private String msg;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return msg;
    }

    /**
     * API是否请求失败
     *
     * @return 失败返回true, 成功返回false
     */
    public boolean isCodeInvalid() {
        return code != BaseCommonValue.WEB_RESP_CODE_SUCCESS&&code != 201;
    }
}
