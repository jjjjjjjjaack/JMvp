package framework.mvp1.base.net;


import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jjjjjjjjaack.jmvp.R;
import com.jjjjjjjjaack.jmvp.app.MyApplication;

import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import framework.mvp1.base.bean.FToken;
import framework.mvp1.base.exception.NeedLoginException;
import framework.mvp1.base.exception.NetException;
import framework.mvp1.base.util.FTokenUtils;
import framework.mvp1.base.util.LanguageUtils;
import framework.mvp1.base.util.ToolUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;


/**
 * Created by lzj on 2017/6/7.
 */

public class BaseRXNetApi {

    public static final String TAG = "BaseRXNetApi";

    public static final String ORGIN_KEY = "-SAFG-";
    public static final String NETAPI = "oaq4x0ou1h0bfch7lvvhxzh43zuefmtq";
    public static final String SYS_INFO_KEY = "CityFurniture";
    public static final String SYS_INFO_VALUE = "CityFurniture";
    public static final String Isapp = "1";
    public static final String Appt = "android";
    public static final String AContetType = "application/json";

    public enum RXExecuteType {GET, POST, MUTLI_POST, SIGN_UPLOAD}

    private static OkHttpClient okHttpClient;  //非常有必要，要不此类还是可以被new，但是无法避免反射，好恶心

    public static OkHttpClient createClient() {
        if (okHttpClient == null) {
            synchronized (BaseRXNetApi.class) {
                if (okHttpClient == null) {
                    OkHttpClient.Builder B = new OkHttpClient().newBuilder();
                    B.writeTimeout(1200, TimeUnit.SECONDS);
                    B.readTimeout(1200, TimeUnit.SECONDS);
                    B.connectTimeout(1200, TimeUnit.SECONDS);
                    B.sslSocketFactory(SSLSocketClient.getSSLSocketFactory(), SSLSocketClient.getX509TrustManager());
                    B.hostnameVerifier(SSLSocketClient.getHostnameVerifier());
//                    B.dns(new ApiDNS());
                    okHttpClient = B.build();
                    return okHttpClient;
                }
            }
        }
        return okHttpClient;
    }

    /**
     * * 构建一般get请求Req
     *
     * @param API_METHOD
     * @param baseRequest
     * @return
     */
    public static final Request doGetReq(String API_METHOD, BaseRequest baseRequest) {
        String url = "";
        if (!API_METHOD.contains("http")) {
            url = NET_URL.getInstance().getUrl(API_METHOD);
        } else {
            url = API_METHOD;
        }
        if (baseRequest != null) {
            Map<String, Object> mapParams = baseRequest.bulitReqMap();
            if (mapParams != null) {
                for (Map.Entry<String, Object> entry : mapParams.entrySet()) {
                    if (url.indexOf("?") == -1) {
                        url += ("?" + entry.getKey() + "=" + entry.getValue());
                    } else {
                        url += ("&" + entry.getKey() + "=" + entry.getValue());
                    }
                }
            }
            String apis = getApiStr2(mapParams);
            Log.i(TAG, "发送请求，路径：" + API_METHOD + " ,参数: " + apis);
        }
        Request.Builder requestBuilder = new Request.Builder().url(url);
        //可以省略，默认是GET请求
        requestBuilder.method("GET", null);
        String userToken = "";
        try {
            FToken token = FTokenUtils.getToken(MyApplication.getApp(), false);
            userToken = token.getToken();
        } catch (NeedLoginException e) {
        }
        String language = LanguageUtils.getAppLanguage(MyApplication.getApp());
        Request request = requestBuilder.addHeader("Appverion", Isapp).addHeader("Token", userToken).addHeader("Accept", "application/json").
                addHeader(SYS_INFO_KEY, SYS_INFO_VALUE).addHeader("Appt", Appt).addHeader("Language", language).addHeader("Nowversion", MyApplication.getApp().currentVersionName).build();
        return request;
    }

    /***
     *  * 构建一般post请求Req
     * @param API_METHOD
     * @param baseRequest
     * @return
     */
    public static final Request doPostReq(String API_METHOD, BaseRequest baseRequest) {
        String url = "";
        if (!API_METHOD.contains("http")) {
            url = NET_URL.getInstance().getUrl(API_METHOD);
        } else {
            url = API_METHOD;
        }
        String params_str ="";
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        FormBody.Builder builder = new FormBody.Builder();
        if (baseRequest != null) {
            Map<String, Object> mapParams = baseRequest.bulitReqMap();
            if (mapParams != null) {
                for (Map.Entry<String,Object> entry:mapParams.entrySet()){
                    if(entry.getValue()!=null) {
                        builder.add(entry.getKey(), entry.getValue().toString());
                        params_str+=(entry.getKey()+":"+entry.getValue()+",");
                    }
                }
            }
        }
        String userToken = "";
        try {
            FToken token = FTokenUtils.getToken(MyApplication.getApp(), false);
            userToken = token.getToken();
            builder.add("token",userToken);
            params_str+=("token"+":"+userToken);
        } catch (NeedLoginException e) {
        }
        requestBuilder.post(builder.build());
        String language = LanguageUtils.getAppLanguage(MyApplication.getApp());
        requestBuilder.addHeader("Appverion", Isapp).addHeader("Token", userToken).addHeader("Accept", "application/json").
                addHeader(SYS_INFO_KEY, SYS_INFO_VALUE).addHeader("Appt", Appt).addHeader("Language", language).addHeader("Nowversion", MyApplication.getApp().currentVersionName);
        Request request = requestBuilder.build();
        Log.i(TAG,"post->"+url+":{"+params_str+"}");
        return request;
    }

    /***
     *  * 构建包含媒体post请求Req
     * @param API_METHOD
     * @param baseRequest
     * @return
     */
    public static final Request doMutliPostReq(String API_METHOD, BaseRequest baseRequest) {
        String url = "";
        if (!API_METHOD.contains("http")) {
            url = NET_URL.getInstance().getUrl(API_METHOD);
        } else {
            url = API_METHOD;
        }
        MultipartBody.Builder mutipartBuild = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (baseRequest != null) {
            Map<String, Object> mapParams = baseRequest.bulitReqMap();
            if (mapParams != null) {
//                String value = JSONObject.toJSONString(mapParams);
//                mutipartBuild.addFormDataPart("data", value);
                for (String key : mapParams.keySet()) {
                    try {
                        mutipartBuild.addFormDataPart(key, ((String) mapParams.get(key)) == null ? "" : (String) mapParams.get(key));
                    } catch (Exception e) {

                    }
                }
            }
            if (baseRequest.baseMulitRequests != null) {
                for (BaseMulitRequest r : baseRequest.baseMulitRequests) {
                    if (r.file == null || !r.file.exists()) {
                        continue;
                    }
                    mutipartBuild.addFormDataPart(r.key, r.file.getName(), RequestBody.create(MediaType.parse(r
                            .contentType), r.file));
                }
            }
        }
        //构建请求体
        RequestBody requestBody = mutipartBuild.build();
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);// 添加URL地址
        requestBuilder.post(requestBody);

        String userToken = "";
        try {
            FToken token = FTokenUtils.getToken(MyApplication.getApp(), false);
            userToken = token.getToken();
        } catch (NeedLoginException e) {
        }
        String language = LanguageUtils.getAppLanguage(MyApplication.getApp());
        Request request = requestBuilder.addHeader("Appverion", Isapp).addHeader("Token", userToken).addHeader("Accept", "application/json").
                addHeader(SYS_INFO_KEY, SYS_INFO_VALUE).addHeader("Appt", Appt).addHeader("Language", language).addHeader("Nowversion", MyApplication.getApp().currentVersionName).build();
        return request;
    }


    /***
     *  * 构建当个文件post请求Req
     * @param API_METHOD
     * @param baseRequest
     * @return
     */
    public static final Request doUploadPostReq(String API_METHOD, final BaseRequest baseRequest) {
        String url = "";
        if (!API_METHOD.contains("http")) {
            url = NET_URL.getInstance().getUrl(API_METHOD);
        } else {
            url = API_METHOD;
        }
        //构建请求体
//        RequestBody requestBody = mutipartBuild.build();
        RequestBody requestFile =
                RequestBody.create(MediaType.parse(baseRequest.signMulitRequest.contentType), baseRequest.signMulitRequest.file);
        FileRequestBody requestBody = new FileRequestBody(requestFile, new FileRequestBody.LoadingListener() {
            @Override
            public void onProgress(long currentLength, long contentLength) {
                //获取上传的比例
//                Log.d(TAG, currentLength + "/" + contentLength);
                if (baseRequest.signMulitRequest.signLoadingListener != null) {
                    baseRequest.signMulitRequest.signLoadingListener.onProgress(currentLength, contentLength);
                }
            }
        });
        MultipartBody.Builder mutipartBuild = new MultipartBody.Builder().setType(MultipartBody.FORM);
        mutipartBuild.addFormDataPart(baseRequest.signMulitRequest.key, baseRequest.signMulitRequest.file.getName(), requestBody);
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);// 添加URL地址
        requestBuilder.post(mutipartBuild.build());

        String userToken = "";
        try {
            FToken token = FTokenUtils.getToken(MyApplication.getApp(), false);
            userToken = token.getToken();
        } catch (NeedLoginException e) {
        }
        String language = LanguageUtils.getAppLanguage(MyApplication.getApp());
        Request request = requestBuilder.addHeader("Appverion", Isapp).addHeader("Token", userToken).addHeader("Accept", "application/json").
                addHeader(SYS_INFO_KEY, SYS_INFO_VALUE).addHeader("Appt", Appt).addHeader("Language", language).addHeader("Nowversion", MyApplication.getApp().currentVersionName).build();
        return request;
    }

    /**
     * @param API_METHOD
     * @param type
     * @param req
     * @param clazz
     * @return
     */
    public static final <K extends BaseResponse> Observable<K> rx_doExecuteQuick(final String API_METHOD, final
    RXExecuteType
            type, final BaseRequest req, final Class<K> clazz) {
        return Observable.create(new Observable.OnSubscribe<K>() {
            @Override
            public void call(final Subscriber<? super K> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onStart();
                    OkHttpClient client = createClient();
                    Request request = null;
                    switch (type) {
                        case GET:
                            request = doGetReq(API_METHOD, req);
                            break;
                        case POST:
                            request = doPostReq(API_METHOD, req);
                            break;
                    }
                    Call call = client.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            subscriber.onError(new NetException(NET_CODE.C_403, MyApplication.getApp().getString(R.string.BaseRXNetApi_string)));
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String bodyStr = response.body().string();
//                            bodyStr = DecipheringValue(bodyStr, key);
//                            Log.i(TAG, API_METHOD + ":" + bodyStr);
                            K baseResponse = null;
                            try {
                                baseResponse = clazz.newInstance();
                            } catch (Exception e) {
                                e.printStackTrace();
                                subscriber.onError(new NetException(NET_CODE.C_M3, ToolUtils.isNull(baseResponse
                                        .msg) ? MyApplication.getApp().getString(R.string.BaseRXNetApi_string1) : baseResponse.msg));
                                return;
                            }
                            if (!req.needHandleResponse) {
                                baseResponse.orgin = bodyStr;
                                baseResponse.code = NET_CODE.C_200;
                                subscriber.onNext(baseResponse);
                                subscriber.onCompleted();
                                return;
                            }
                            baseResponse.fromJSON(bodyStr);
                            Log.i(TAG + "_rel", API_METHOD + ":" + bodyStr);
                            switch (baseResponse.code) {
                                case NET_CODE.C_200:
//                                case NET_CODE.C_501:
                                    subscriber.onNext(baseResponse);
                                    subscriber.onCompleted();
                                    break;
                                case NET_CODE.C_403:
                                    subscriber.onError(new NetException(baseResponse.code, ToolUtils.isNull(baseResponse
                                            .msg) ? MyApplication.getApp().getString(R.string.BaseRXNetApi_string2) : baseResponse.msg,baseResponse));
                                    break;
                                default:
                                    subscriber.onError(new NetException(baseResponse.code, ToolUtils.isNull(baseResponse
                                            .msg) ? MyApplication.getApp().getString(R.string.BaseRXNetApi_string1) : baseResponse.msg,baseResponse));
                                    break;
                            }
                        }
                    });
                }

            }
        });

    }

//    //Unicode转中文方法
//    private static String unicodeToCn(String unicode) {
//        /** 以 \ u 分割，因为java注释也能识别unicode，因此中间加了一个空格*/
//        String[] strs = unicode.split("\\\\u");
//        String returnStr = "";
//        // 由于unicode字符串以 \ u 开头，因此分割出的第一个字符是""。
//        for (int i = 1; i < strs.length; i++) {
//            returnStr += (char) Integer.valueOf(strs[i], 16).intValue();
//        }
//        return returnStr;
//    }

    /**
     * @param API_METHOD
     * @param type
     * @param req
     * @return
     */
    public static final Observable<String> rx_doExecuteQuick_str(final String API_METHOD, final RXExecuteType
            type, final BaseRequest req) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onStart();
                    OkHttpClient client = createClient();
                    Request request = null;
                    switch (type) {
                        case GET:
                            request = doGetReq(API_METHOD, req);
                            break;
                        case POST:
                            request = doPostReq(API_METHOD, req);
                            break;
                    }
                    Call call = client.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            subscriber.onError(e);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String bodyStr = response.body().string();
                            subscriber.onNext(bodyStr);
                            subscriber.onCompleted();
                        }
                    });
                }
            }
        });

    }


    /**
     * 包含多媒体上传
     *
     * @param API_METHOD
     * @param req
     * @return
     */
    public static final <K extends BaseResponse> Observable<K> rx_doExecuteMedia(
            final String API_METHOD,
            final BaseRequest req, final Class<K> clazz) {
        return Observable.create(new Observable.OnSubscribe<K>() {
            @Override
            public void call(final Subscriber<? super K> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onStart();
                    OkHttpClient client = createClient();
                    Request request = doMutliPostReq(API_METHOD, req);
                    Call call = client.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            subscriber.onError(new NetException(NET_CODE.C_403, MyApplication.getApp().getString(R.string.BaseRXNetApi_string)));
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String bodyStr = response.body().string();
                            String key = "" ;
                            Log.i(TAG, API_METHOD + ":" + bodyStr);
                            K baseResponse = null;
                            try {
                                baseResponse = clazz.newInstance();
                            } catch (Exception e) {
                                e.printStackTrace();
                                subscriber.onError(new NetException(NET_CODE.C_M3, ToolUtils.isNull(baseResponse
                                        .msg) ? MyApplication.getApp().getString(R.string.BaseRXNetApi_string1) : baseResponse.msg));
                                return;
                            }
                            baseResponse.fromJSON(bodyStr);
                            switch (baseResponse.code) {
                                case NET_CODE.C_200:
                                    subscriber.onNext(baseResponse);
                                    subscriber.onCompleted();
                                    break;
                                case NET_CODE.C_403:
                                    subscriber.onError(new NetException(baseResponse.code, ToolUtils.isNull(baseResponse
                                            .msg) ? MyApplication.getApp().getString(R.string.BaseRXNetApi_string2) : baseResponse.msg));
                                    break;
                                default:
                                    subscriber.onError(new NetException(baseResponse.code, ToolUtils.isNull(baseResponse
                                            .msg) ? MyApplication.getApp().getString(R.string.BaseRXNetApi_string1) : baseResponse.msg));
                                    break;
                            }
                        }
                    });

                }
            }
        });
    }

    /**
     * 包含多媒体上传
     *
     * @param API_METHOD
     * @param req
     * @return
     */
    public static final <K extends BaseResponse> Observable<K> rx_doSignExecuteMedia(
            final String API_METHOD,
            final BaseRequest req, final Class<K> clazz) {
        return Observable.create(new Observable.OnSubscribe<K>() {
            @Override
            public void call(final Subscriber<? super K> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onStart();
                    OkHttpClient client = createClient();
                    Request request = doUploadPostReq(API_METHOD, req);
                    Call call = client.newCall(request);
                    req.signMulitRequest.nowCall = call;
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String bodyStr = response.body().string();
                            K baseResponse = null;
                            try {
                                baseResponse = clazz.newInstance();
                            } catch (Exception e) {
                                e.printStackTrace();
                                subscriber.onError(new NetException(NET_CODE.C_M3, ToolUtils.isNull(baseResponse
                                        .msg) ? MyApplication.getApp().getString(R.string.BaseRXNetApi_string1) : baseResponse.msg));
                                return;
                            }
                            baseResponse.fromJSON(bodyStr);
                            switch (baseResponse.code) {
                                case NET_CODE.C_200:
                                    subscriber.onNext(baseResponse);
                                    subscriber.onCompleted();
                                    break;
                                case NET_CODE.C_401:
                                    subscriber.onError(new NetException(baseResponse.code, ToolUtils.isNull(baseResponse
                                            .msg) ? MyApplication.getApp().getString(R.string.BaseRXNetApi_string2) : baseResponse.msg));
                                    break;
                                default:
                                    subscriber.onError(new NetException(baseResponse.code, ToolUtils.isNull(baseResponse
                                            .msg) ? MyApplication.getApp().getString(R.string.BaseRXNetApi_string1) : baseResponse.msg));
                                    break;

                            }
                        }
                    });

                }
            }
        });
    }


    /**
     * 上传日志file型文件
     *
     * @param req
     */
    public static final void doExecuteErrorLog(final BaseRequest req) {
        OkHttpClient client = createClient();
        Request request = doMutliPostReq("index/errorFile", req);
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bodyStr = response.body().string();
            }
        });
    }


    /**
     * 映射得到排序的成员变量字符串
     *
     * @param hashmap
     * @return
     */
    public static String getApiStr2(Map hashmap) {
        if (hashmap != null) {
            StringBuilder result = new StringBuilder();

            Map<String, Object> map = new TreeMap<String, Object>(new Comparator<String>() {

                @Override
                public int compare(String lhs, String rhs) {
                    return lhs.compareTo(rhs);
                }

            });
            map.putAll(hashmap);

            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (entry.getValue() != null) {
                    if (result.length() > 0)
                        result.append("&");

                    result.append(entry.getKey());
                    result.append("=");
                    result.append(entry.getValue());
                }
            }
            return result.toString();
        } else {
            return "";
        }
    }

    public static boolean isBase64(String str) {
        String base64Pattern = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
        return Pattern.matches(base64Pattern, str);
    }

}
