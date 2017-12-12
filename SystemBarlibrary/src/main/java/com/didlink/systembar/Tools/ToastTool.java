package com.didlink.systembar.Tools;

import android.content.Context;
import android.widget.Toast;

public class ToastTool {
    public static void showNativeShortToast(Context context, String tips) {
        Toast.makeText(context, tips, Toast.LENGTH_SHORT).show();
    }

    public static void showNativeLongToast(Context context, String tips) {
        Toast.makeText(context, tips, Toast.LENGTH_LONG).show();
    }


}
