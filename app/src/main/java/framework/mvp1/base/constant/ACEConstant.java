package framework.mvp1.base.constant;

public class ACEConstant {

    public static final String PG = "";

    /**
     * 全局倒计时缓存key
     */
    public static String ACE_COMM_COUNTDOWN_KEY = PG + "ACE_COMM_COUNTDOWN_KEY";
    /**
     * 缓存datasocket最后一次登录id
     */
    public static String ACE_DATASOCKET_BINDACCOUNTID = PG + "ACE_DATASOCKET_BINDACCOUNTID";

    /**
     * 保存登录令牌信息的key
     */
    public static String ACE_FTOKEN_KEY = PG + "ACE_FTOKEN_KEY";


    /**
     * 保存用户信息的key
     */
    public static String ACE_USERINFO_KEY = PG + "ACE_USERINFO_KEY";

    /**
     * 当前app保存的语言类型id
     */
    public static String CURRENTLANGUAGE_ID = PG + "CURRENTLANGUAGE_ID";


    /**
     * PJSIP 账号 域名 密码
     */
    public static String ACE_PJSIP_ACCOUNT_NAME = PG + "ACE_PJSIP_ACCOUNT_NAME";
    public static String ACE_PJSIP_ACCOUNT_REG = PG + "ACE_PJSIP_ACCOUNT_REG";
    public static String ACE_PJSIP_ACCOUNT_PASSWORD = PG + "ACE_PJSIP_ACCOUNT_PASSWORD";


    public final static String SIM_CACHE = PG + "SWLINK_SIM_CACHE";//通话默认SIM


    public final static String ACR_FOLLOW_TIP_TODAY = PG + "ACR_FOLLOW_TIP_TODAY";//跟进提示框今天是否再提醒

}
