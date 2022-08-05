package framework.mvp1.base.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class CallPhoneUtil {
    public static void callPhone(Context context, String phoneNum) {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            Uri data = Uri.parse("tel:" + phoneNum);
            intent.setData(data);
            context.startActivity(intent);
        }catch (Exception e){
        }
    }
}
