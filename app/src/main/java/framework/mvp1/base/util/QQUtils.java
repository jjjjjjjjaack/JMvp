package framework.mvp1.base.util;

import static framework.mvp1.base.net.NET_CODE.C_200;
import static framework.mvp1.base.net.NET_CODE.C_501;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import org.greenrobot.eventbus.EventBus;

import framework.mvp1.base.bean.FToken;
import framework.mvp1.base.exception.NetException;
import framework.mvp1.base.f.BasePresent;
import framework.mvp1.base.net.BaseResponse;

//public class QQUtils {
//
//    private QQUtils() {
//        mTencent = Tencent.createInstance("101873962", MyApplication.getApp());
//    }
//
//    private static QQUtils util;
//    private Tencent mTencent;
//    private String token;
//    private String expires_in;
//    private String uniqueCode;
//
//    //同步代码快的demo加锁，安全高效
//    public static QQUtils getInstance() {
//        if (util == null) {
//            synchronized (QQUtils.class) {
//                if (util == null) {
//                    util = new QQUtils();
//                }
//            }
//        }
//        return util;
//    }
//
//
//    public void permissionGranted() {
//        Tencent.setIsPermissionGranted(true);
//    }
//
//    private Context context;
//    private Integer actionType;//0登陆 1绑定
//
//    public void doLogin(Context context, int actionType) {
//        this.context = context;
//        this.token = "";
//        this.expires_in = "";
//        this.uniqueCode = "";
//        this.actionType = new Integer(actionType);
//        LoadingUtils.getLoadingUtils().showLoadingView(context);
//        mTencent.login((Activity) context, "all", loginListener);
//    }
//
//    //授权登录监听（最下面是返回结果）
//    private IUiListener loginListener = new IUiListener() {
//        @Override
//        public void onComplete(Object o) {
//            JSONObject p = JSONObject.parseObject(o.toString());
//            uniqueCode = p.getString("openid"); //QQ的openid
//            token = p.getString("access_token");
//            expires_in = p.getString("expires_in");
//            //获取QQ返回的用户信息（里面的参数直接照搬，别乱改，上面也是）
//            QQToken qqtoken = mTencent.getQQToken();
//            mTencent.setOpenId(uniqueCode);
//            mTencent.setAccessToken(token, expires_in);
//            UserInfo info = new UserInfo(MyApplication.getApp(), qqtoken);
//            info.getUserInfo(new IUiListener() {
//                @Override
//                public void onComplete(Object o) {
//                    Log.e("abc", "2222===" + o.toString());
//                    String jsonString = o.toString();
//                    JSONObject p = JSONObject.parseObject(jsonString);
//                    String nickname = p.getString("nickname");
//                    String sexStr = p.getString("sex");
//                    String headImg = p.getString("figureurl");
//                    if (context == null) {
//                        return;
//                    }
//                    if (actionType.intValue() == 0) {
//                        BasePresent.doStaticCommRequest(context, new REQ_Factory.POST_DO_LOGIN_BYQQ_REQ(uniqueCode, nickname, headImg),
//                                false, true,
//                                new BasePresent.DoCommRequestInterface<PostQQLoginResponse, PostQQLoginResponse>() {
//                                    @Override
//                                    public void doStart() {
//
//                                    }
//
//                                    @Override
//                                    public PostQQLoginResponse doMap(PostQQLoginResponse postQQLoginResponse) {
//                                        return postQQLoginResponse;
//                                    }
//
//                                    @Override
//                                    public void onSuccess(PostQQLoginResponse baseResponse) throws Exception {
//                                        LoadingUtils.getLoadingUtils().hideLoadingView(context);
////                                        if (baseResponse.code == C_200) {//登陆
//                                        FToken fToken = FToken.fromJSONAuto(baseResponse.datas, FToken.class);
//                                        FTokenUtils.saveToken(fToken);
//                                        VpMainAct.openMainAct(context);
////                                        } else if (baseResponse.code == C_501) {//绑定手机
////                                            BindPhoneAct.openActByQQ(context, baseResponse);
////                                        }
//                                    }
//
//                                    @Override
//                                    public void onError(Throwable e) {
//                                        LoadingUtils.getLoadingUtils().hideLoadingView(context);
//                                        if (e instanceof NetException) {
//                                            NetException netException = (NetException) e;
//                                            if (netException.netCode == C_501) {
//                                                try {
//
//                                                    BindPhoneAct.openActByQQ(context, (PostQQLoginResponse) netException.baseResponse);
//                                                    EventBus.getDefault().post(new CEventUtils.SplashJumpEvent());
//                                                } catch (Exception ex) {
//                                                }
//                                            }
//                                        }
//                                    }
//                                });
//                    } else if (actionType.intValue() == 1) {
//                        BasePresent.doStaticCommRequest(context,
//                                new REQ_Factory.POST_DO_BIND_THIRDACCOUNT_REQ("2", uniqueCode),
//                                true, true,
//                                new BasePresent.DoCommRequestInterface<BaseResponse, BaseResponse>() {
//                                    @Override
//                                    public void doStart() {
//
//                                    }
//
//                                    @Override
//                                    public BaseResponse doMap(BaseResponse baseResponse) {
//                                        return baseResponse;
//                                    }
//
//                                    @Override
//                                    public void onSuccess(BaseResponse baseResponse) throws Exception {
//                                        EventBus.getDefault().post(new CEventUtils.BindTirdAccountEvent());
//                                    }
//
//                                    @Override
//                                    public void onError(Throwable e) {
//                                    }
//                                }
//                        );
//                    }
//                }
//
//                @Override
//                public void onError(UiError uiError) {
//                    Log.i("aaaa", "asdasd");
//                }
//
//                @Override
//                public void onCancel() {
//                    Log.i("aaaa", "asdasd");
//                }
//
//                @Override
//                public void onWarning(int i) {
//                    Log.i("aaaa", "asdasd");
//                }
//            });
//        }
//
//        @Override
//        public void onError(UiError uiError) {
//            Log.e("abc", uiError.errorMessage);
//            T.showShort(context, uiError.errorMessage);
//        }
//
//        @Override
//        public void onCancel() {
//
//        }
//
//        @Override
//        public void onWarning(int i) {
//
//        }
//    };
//
//    public void activityResult(int requestCode, int resultCode, Intent data) {
//        mTencent.onActivityResultData(requestCode, resultCode, data, loginListener);
//        if (requestCode == Constants.REQUEST_API) {
//            if (resultCode == Constants.REQUEST_QQ_SHARE ||
//                    resultCode == Constants.REQUEST_QZONE_SHARE ||
//                    resultCode == Constants.REQUEST_OLD_SHARE) {
//                mTencent.handleResultData(data, loginListener);
//            }
//        }
//    }
//
//}
