package framework.mvp1.base.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @ProjectName: FAZIntermediary
 * @Package: framework.mvp1.base.util
 * @ClassName: DownloadUtil
 * @Description: java类作用描述
 * @Author: liys
 * @CreateDate: 2021/4/28 17:41
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/28 17:41
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class DownloadUtil {

    private static DownloadUtil downloadUtil;
    private final OkHttpClient okHttpClient;

    public static DownloadUtil get() {
        if (downloadUtil == null) {
            downloadUtil = new DownloadUtil();
        }
        return downloadUtil;
    }

    private DownloadUtil() {
        okHttpClient = new OkHttpClient();
    }

    /**
     * @param url 下载连接
     * @param fileName 保存文件名
     * @param saveDir 储存下载文件的SDCard目录
     * @param listener 下载监听
     */
    public void download(final String url, final String fileName, final String saveDir, final OnDownloadListener listener) {
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败
                listener.onDownloadFailed();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                // 储存下载文件的目录
//                String savePath = isExistDir(saveDir);
                String savePath = CachePathUtil.getCachePathFile(saveDir).getPath();
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = null;
                    if(ToolUtils.isNull(fileName)) {
                        file =  new File(savePath, getNameFromUrl(url));
                    }else{
                        file = new File(savePath,fileName);
                    }
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        // 下载中
                        listener.onDownloading(progress);
                    }
                    fos.flush();
                    // 下载完成
                    listener.onDownloadSuccess(file);
                } catch (Exception e) {
                    listener.onDownloadFailed();
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
    }

//    /**
//     * @param saveDir
//     * @return
//     * @throws IOException
//     * 判断下载目录是否存在
//     */
//    private String isExistDir(String saveDir) throws IOException {
//        // 下载位置
//        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//
//            File downloadFile = new File(Environment.getExternalStorageDirectory(), saveDir);
//            if (!downloadFile.mkdirs()) {
//                downloadFile.createNewFile();
//            }
//            String savePath = downloadFile.getAbsolutePath();
//            Log.e("savePath",savePath);
//            return savePath;
//        }
//        return null;
//    }

    /**
     * @param url
     * @return
     * 从下载连接中解析出文件名
     */
    private String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public interface OnDownloadListener {
        /**
         * 下载成功
         */
        void onDownloadSuccess(File str);

        /**
         * @param progress
         * 下载进度
         */
        void onDownloading(int progress);

        /**
         * 下载失败
         */
        void onDownloadFailed();
    }
}
