package framework.mvp1.base.util;

import android.content.Context;


import com.jjjjjjjjaack.jmvp.app.MyApplication;

import framework.mvp1.base.bean.FToken;
import framework.mvp1.base.constant.ACEConstant;
import framework.mvp1.base.exception.NeedLoginException;

public class FTokenUtils {

    /**
     * 获取登录令牌
     * @param context
     * @param flag
     */
    public static FToken getToken(Context context, boolean flag) throws NeedLoginException {
        FToken token = JnCache.getCache(context, ACEConstant.ACE_FTOKEN_KEY);
        if (token == null || ToolUtils.isNull(token.getToken())) {
            if (flag) {
//                context.startActivity(new Intent(context, LoginAct.class));
            }
            throw new NeedLoginException();
        }
        return token;
    }

    /**
     * 存储登录令牌
     * @return
     */
    public static void saveToken(FToken fToken) {
        JnCache.saveCache(MyApplication.getApp(), ACEConstant.ACE_FTOKEN_KEY, fToken);
    }
    /**
     * 清除登录令牌
     */
    public static void clearToken(Context context) {
        JnCache.removeCache(context, ACEConstant.ACE_FTOKEN_KEY);
    }

    /***
     * 注销账号
     * 清除所关联数据
     * @param context
     */
    public static void doLogout(Context context){
        clearToken(context);
//        FCacheUtils.clearUserInfo(context);
//        FCacheUtils.clearDefaultFamilyID(context);
//        FCacheUtils.clearDefaultHostDeviceSN(context);
    }


}
