package framework.mvp1.base.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.math.BigDecimal;

public class CachePathUtil {


    public static final String doMainName = "BluetoothCurtain";
    public static final String smvideo_url = File.separator + "oosmvideo" + File.separator + "sv" + File.separator;
    public static final String smvideop_url = File.separator + "oosmvideo" + File.separator + "svp" + File.separator;

    /***
     * 获取缓存路径
     *
     * @return
     */
    public static File getCachePathFile(String path) {
        String status = Environment.getExternalStorageState();
        File dir = null;
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            dir = new File(Environment.getExternalStorageDirectory()
                    + File.separator + doMainName + File.separator + path);
            if (!dir.exists()) {
                dir.mkdirs();
            }

        } else {
            dir = new File(Environment.getDownloadCacheDirectory()
                    + File.separator + doMainName + File.separator + path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }
        return dir;
    }

    /**
     * 计算当前缓存大小
     *
     * @param context
     * @return
     * @throws Exception
     */
    public static String getTotalCacheSize(Context context) throws Exception {
        long cacheSize = getFolderSize(getCachePathFile(""));
        return getFormatSize(cacheSize);
    }

    //遍历指定路径文件夹下面所有文件
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            // return size + "Byte";
            return "0K";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }

    //清除缓存
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

}
