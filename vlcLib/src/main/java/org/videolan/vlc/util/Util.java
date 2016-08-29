package org.videolan.vlc.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by Osen on 2015/7/31.
 */
public class Util {

    public static boolean close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
                return true;
            } catch (IOException e) {
                return false;
            }
        } else {
            return false;
        }
    }
}
