package framework.mvp1.base.net;

import java.io.File;

/**
 * Created by Administrator on 2017/7/12 0012.
 */

public class BaseMulitRequest {
    /**
     * 媒体类型
     */
    public String contentType = "";
    /**
     * key
     */
    public String key;
    public File file;

    public BaseMulitRequest(String key, File file, String contentType) {
        this.contentType = contentType;
        this.key = key;
        this.file = file;
    }
}
