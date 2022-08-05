package com.jjjjjjjjaack.jmvp.app.net;

import framework.mvp1.base.annotation.AnnBaseReq;
import framework.mvp1.base.net.BaseRXNetApi;
import framework.mvp1.base.net.BaseRequest;

/**
 * 请求接口基类
 */
public class REQ_Factory {

    @AnnBaseReq(API_METHOD = "http://api.bq04.com/apps/latest/" + "", RXExecuteType = BaseRXNetApi.RXExecuteType.GET, needHandleResponse = false)
    public static class POST_CHECK_APPVERSION_REQ extends BaseRequest {
        public POST_CHECK_APPVERSION_REQ(String api_token) {
            this.api_token = api_token;
        }

        public String api_token;
    }

}
