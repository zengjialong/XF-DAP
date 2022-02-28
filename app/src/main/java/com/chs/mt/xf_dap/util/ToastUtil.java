package com.chs.mt.xf_dap.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    private static Toast mToast;
    public static void showShortToast(Context context , String content) {
        if (mToast == null) {
            mToast = Toast.makeText(context, "", Toast.LENGTH_LONG);
        }
        mToast.setText(content);
        mToast.show();
    }


}
