package framework.mvp1.base.net;

import java.io.File;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/7/12 0012.
 */

public class SignMulitRequest {
    /**
     * 媒体类型
     */
    public String contentType = "";
    /**
     * key
     */
    public String key;
    public File file;

    public SignLoadingListener signLoadingListener;

    public Call nowCall;

    public interface SignLoadingListener {
        void onProgress(long currentLength, long contentLength);
    }

    public SignMulitRequest(String key, File file, String contentType, SignLoadingListener signLoadingListener) {
        this.contentType = contentType;
        this.key = key;
        this.file = file;
        this.signLoadingListener = signLoadingListener;
    }
}
