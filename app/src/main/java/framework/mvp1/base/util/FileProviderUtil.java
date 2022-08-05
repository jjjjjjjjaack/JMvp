package framework.mvp1.base.util;

import android.content.Context;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import java.io.File;

/**
 * @ProjectName: FAZIntermediary
 * @Package: framework.mvp1.base.util
 * @ClassName: FileProviderUtil
 * @Description: java类作用描述
 * @Author: liys
 * @CreateDate: 2021/6/23 8:59
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/6/23 8:59
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class FileProviderUtil {

    public static Uri getUri(Context context, File file) {
        Uri fileUri = null;
        if (Build.VERSION.SDK_INT >= 24) {
            fileUri = FileProvider.getUriForFile(context,  "com.jjjjjjjjaack.jmvp.fileprovider", file);
        } else {
            fileUri = Uri.fromFile(file);
        }
        return fileUri;
    }

}


