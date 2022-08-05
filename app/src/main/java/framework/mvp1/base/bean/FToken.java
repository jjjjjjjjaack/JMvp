package framework.mvp1.base.bean;

/**
 * 登录令牌，只能使用这个类缓存用户登录信息
 */
public class FToken extends BaseBean {
    //登录令牌
    private String token;
    //用户ID
    private String id;
    //

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
