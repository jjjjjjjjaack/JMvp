package framework.mvp1.base.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.alibaba.fastjson.JSONObject;

//public class WeChatUtils {
//
//    //SendMessageToWX.Req.WXSceneSession分享到对话:
//    //SendMessageToWX.Req.WXSceneTimeline分享到朋友圈
//    //SendMessageToWX.Req.WXSceneFavorite分享到收藏:
//
//    // APP_ID 替换为你的应用从官方网站申请到的合法appID
//    public static final String APP_ID = "wxb857e0603b58736b";
//    public static final String APP_SECRET = "11d0438b014bd15419902903abc1edaf";
//    // IWXAPI 是第三方app和微信通信的openApi接口
//    public IWXAPI api;
//
//    private WeChatUtils() {
//    }
//
//    private static WeChatUtils util;
//
//    //同步代码快的demo加锁，安全高效
//    public static WeChatUtils getInstance() {
//        if (util == null) {
//            synchronized (WeChatUtils.class) {
//                if (util == null) {
//                    util = new WeChatUtils();
//                    util.regToWx();
//                }
//            }
//        }
//        return util;
//    }
//
//    /**
//     * @param
//     * @return
//     * @description
//     * @author admin
//     * @time 2021/4/1 9:46
//     */
//    public void regToWx() {
//        // 通过WXAPIFactory工厂，获取IWXAPI的实例
//        api = WXAPIFactory.createWXAPI(MyApplication.getApp(), APP_ID, true);
//        // 将应用的appId注册到微信
//        api.registerApp(APP_ID);
//        //建议动态监听微信启动广播进行注册到微信
//        MyApplication.getApp().registerReceiver(new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                // 将该app注册到微信
//                api.registerApp(APP_ID);
//            }
//        }, new IntentFilter(ConstantsAPI.ACTION_REFRESH_WXAPP));
//    }
//
//    /**
//     * @param actionType 0登陆 1绑定
//     * @return
//     * @description
//     * @author jiejack
//     * @time 2022/4/16 12:27 下午
//     */
//    public void loginWx(int actionType) {
//        // send oauth request
//        final SendAuth.Req req = new SendAuth.Req();
//        req.scope = "snsapi_userinfo";
//        req.state = "wechat_sdk_demo_test";
//
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("actionType", actionType);
////        req.extData = jsonObject.toJSONString();
//        req.transaction = jsonObject.toJSONString();
//        api.sendReq(req);
//    }
//
//
//}
