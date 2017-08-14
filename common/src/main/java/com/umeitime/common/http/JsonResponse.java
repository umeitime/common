package com.umeitime.common.http;

import java.io.Serializable;

public class JsonResponse<T> implements Serializable {
    public int code;
    public String msg;
    public T data;
}