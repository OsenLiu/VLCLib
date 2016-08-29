//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.videolan.libvlc.util;

import android.net.Uri;
import android.os.Build.VERSION;
import java.io.File;

public class AndroidUtil {
    public AndroidUtil() {
    }

    public static boolean isFroyoOrLater() {
        return VERSION.SDK_INT >= 8;
    }

    public static boolean isGingerbreadOrLater() {
        return VERSION.SDK_INT >= 9;
    }

    public static boolean isHoneycombOrLater() {
        return VERSION.SDK_INT >= 11;
    }

    public static boolean isHoneycombMr1OrLater() {
        return VERSION.SDK_INT >= 12;
    }

    public static boolean isICSOrLater() {
        return VERSION.SDK_INT >= 14;
    }

    public static boolean isJellyBeanOrLater() {
        return VERSION.SDK_INT >= 16;
    }

    public static boolean isJellyBeanMR1OrLater() {
        return VERSION.SDK_INT >= 17;
    }

    public static boolean isJellyBeanMR2OrLater() {
        return VERSION.SDK_INT >= 18;
    }

    public static boolean isKitKatOrLater() {
        return VERSION.SDK_INT >= 19;
    }

    public static boolean isLolliPopOrLater() {
        return VERSION.SDK_INT >= 21;
    }

    public static File URItoFile(String URI) {
        return URI == null?null:new File(Uri.decode(URI).replaceFirst("file://", ""));
    }

    public static String PathToURI(String path) {
        if(path == null) {
            throw new NullPointerException("Cannot convert null path!");
        } else {
            return Uri.fromFile(new File(path)).toString();
        }
    }
}
