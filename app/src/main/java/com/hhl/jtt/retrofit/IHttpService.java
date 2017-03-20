package com.hhl.jtt.retrofit;

import com.hhl.jtt.bean.BaseBean;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

/**
 * Created by DELL on 2017/3/20.
 * retrofit接口对象，
 */

public interface IHttpService {

    /**
     * 图片提交
     * @param params
     * @return
     */
    @Multipart
    @POST("file/upLoad.do")
    Call<BaseBean> upLoadAgree(@PartMap Map<String, RequestBody> params);
}
