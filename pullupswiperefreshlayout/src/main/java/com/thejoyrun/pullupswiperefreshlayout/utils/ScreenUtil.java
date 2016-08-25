package com.thejoyrun.pullupswiperefreshlayout.utils;

import android.content.Context;
import android.view.WindowManager;

/**
 * Created by keven on 16/8/25.
 */

public class ScreenUtil {

    private static int   screenHeight;    // 高度
    private static int   screenWidth;    // 宽度

    public static int getScreenHeight(Context context) {
//		if (mContext == null) {
//			throw new RuntimeException("You need to be init.In Application.onCreate()");
//		}
        if (context == null){
            return 100;
        }
        if (screenHeight == 0) {
            screenHeight = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
                    .getHeight();
        }
        return screenHeight;
    }

    public static int getScreenWidth(Context context) {
//		if (mContext == null) {
//			throw new RuntimeException("You need to be init.In Application.onCreate()");
//		}
        if (context == null){
            return 100;
        }
        if (screenWidth == 0) {
            screenWidth = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
                    .getWidth();
        }
        return screenWidth;
    }
}
