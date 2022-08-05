package framework.mvp1.base.exception;

/**
 * Created by lzj on 2017/6/14.
 * <p>
 * 没登录异常
 */
public class ViewnullException extends Exception {
    public ViewnullException() {
        super();
        // TODO Auto-generated constructor stub
    }

    public ViewnullException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
        // TODO Auto-generated constructor stub
    }

    public ViewnullException(String detailMessage) {
        super(detailMessage);
        // TODO Auto-generated constructor stub
    }

    public ViewnullException(Throwable throwable) {
        super(throwable);
        // TODO Auto-generated constructor stub
    }
}
