package framework.mvp1.base.util;

import android.text.InputFilter;
import android.text.Spanned;

public class InputSpaceFilter implements InputFilter {
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        // 判断是否是空格
        if (source.equals(" "))
            return "";
        return null;
    }
}
