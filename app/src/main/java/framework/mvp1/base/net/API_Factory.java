package framework.mvp1.base.net;



import framework.mvp1.base.annotation.AnnBaseReq;
import framework.mvp1.base.exception.NetException;
import framework.mvp1.base.util.ToolUtils;
import rx.Observable;

import static framework.mvp1.base.net.BaseRXNetApi.RXExecuteType.MUTLI_POST;

public class API_Factory {
//    /**
//     * @param req
//     * @return
//     */
//    public static Observable<BaseResponse> API_POST_DOCOMM(BaseRequest req) {
//        return BaseRXNetApi.rx_doExecuteQuick(NET_URL.POST_DOCOMM, BaseRXNetApi.RXExecuteType.POST, req,
//                BaseResponse.class);
//    }

    /**
     * 注解方式
     * @return
     */
    public static Observable ANN_API_POST_DOCOMM(BaseRequest req) {
        //这个类是否使用了AnnotationClass这个注解

        boolean hasAnnotation = req.getClass().isAnnotationPresent(AnnBaseReq.class);
        if (hasAnnotation) {
            //获取到AnnotationClass对象
            AnnBaseReq annBaseReq = req.getClass().getAnnotation(AnnBaseReq.class);
            String API_METHOD = annBaseReq.API_METHOD();
            BaseRXNetApi.RXExecuteType type = annBaseReq.RXExecuteType();
            Class clazz = annBaseReq.RESPONSE_CLASS();
            req.needEncode = annBaseReq.needEncode();
            req.needHandleResponse = annBaseReq.needHandleResponse();
            if (ToolUtils.isNull(API_METHOD)) {
                return ANN_BulitErro();
            }
            if (type == MUTLI_POST) {
                return BaseRXNetApi.rx_doExecuteMedia(API_METHOD, req, clazz);
            } else {
                return BaseRXNetApi.rx_doExecuteQuick(API_METHOD, type, req, clazz);
            }
        } else {
            return ANN_BulitErro();
        }
    }



    public static Observable ANN_BulitErro() {
        return Observable.error(new NetException("参数注解异常"));
    }

    /**
     *
     * @return
     */
    public static Observable ANN_BulitParNull() {
        return Observable.error(new NetException(""));
    }


//    /**
//     * 获取app版本
//     *
//     * @param req
//     * @return
//     */
//    public static Observable<BaseResponse> API_GET_APPVERSION(GET_APPVERSION_REQ req) {
//        return BaseRXNetApi.rx_doExecuteQuick(NET_URL.GET_APPVERSION_DATA, BaseRXNetApi.RXExecuteType.POST, req,
//                BaseResponse.class);
}
