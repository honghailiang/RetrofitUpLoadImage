package com.hhl.jtt.bean;

/**
 * Created by Administrator on 2017/3/20.
 */

public class BaseBean {

    /**
     * resData : {"status":"0","message":"失败","data":"发送失败"}
     * token : null
     */

    private ResDataBean resData;
    private Object token;

    public ResDataBean getResData() {
        return resData;
    }

    public void setResData(ResDataBean resData) {
        this.resData = resData;
    }

    public Object getToken() {
        return token;
    }

    public void setToken(Object token) {
        this.token = token;
    }

    public static class ResDataBean {
        /**
         * status : 0
         * message : 失败
         * data : 发送失败
         */

        private String status;
        private String message;
        private String data;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
}
