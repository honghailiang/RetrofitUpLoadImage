package com.hhl.jtt.retrofit;

import retrofit2.Response;

/**
 * Created by DELL on 2017/3/20.
 * Retrofit的回到接口，由Activity实现
 */

public interface RetrofitCallBack<T> {
    /**
     * 成功回调
     * @param response  返回数据
     * @param method    回调方法标志
     */
    void onResponse(Response<T> response, int method);
    /**
     * 失败回调
     * @param response  返回数据
     * @param method    回调方法标志
     */
    void onFailure(Response<T> response, int method);
}
