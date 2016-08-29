//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.videolan.libvlc;

import android.os.Build.VERSION;
import android.util.Log;
import android.view.Surface;
import java.util.ArrayList;
import java.util.Iterator;
import org.videolan.libvlc.util.HWDecoderUtil;
import org.videolan.libvlc.util.HWDecoderUtil.AudioOutput;

public class LibVLC extends VLCObject {
    private static final String TAG = "VLC/LibVLC";
    private static LibVLC.OnNativeCrashListener sOnNativeCrashListener;

    public native void attachSurface(Surface var1, IVideoPlayer var2);

    public native void detachSurface();

    public native void attachSubtitlesSurface(Surface var1);

    public native void detachSubtitlesSurface();

    public LibVLC(ArrayList<String> options) throws Throwable {
        loadNative();
        boolean setAout = true;
        boolean setVout = true;
        boolean setChroma = true;
        if(options != null) {
            Iterator hwAout = options.iterator();

            while(hwAout.hasNext()) {
                String option = (String)hwAout.next();
                if(option.startsWith("--aout=")) {
                    setAout = false;
                }

                if(option.startsWith("--vout=")) {
                    setVout = false;
                }

                if(option.startsWith("--androidsurface-chroma")) {
                    setChroma = false;
                }

                if(!setAout && !setVout && !setChroma) {
                    break;
                }
            }
        }

        if(setAout || setVout || setChroma) {
            if(options == null) {
                options = new ArrayList();
            }

            if(setAout) {
                AudioOutput hwAout1 = HWDecoderUtil.getAudioOutputFromDevice();
                if(hwAout1 == AudioOutput.OPENSLES) {
                    options.add("--aout=opensles");
                } else {
                    options.add("--aout=android_audiotrack");
                }
            }

            if(setVout) {
                if(HWDecoderUtil.HAS_WINDOW_VOUT) {
                    options.add("--vout=androidwindow");
                } else {
                    options.add("--vout=androidsurface");
                }
            }

            if(setChroma) {
                options.add("--androidsurface-chroma");
                options.add("RV32");
            }
        }

        this.nativeNew(options != null?(String[])options.toArray(new String[options.size()]):null);
        this.setEventHandler(EventHandler.getInstance());
    }

    public LibVLC() throws Throwable {
        this((ArrayList)null);
    }

    public native void setSurface(Surface var1);

    public native String version();

    public native String compiler();

    public native String changeset();

    public static native void sendMouseEvent(int var0, int var1, int var2, int var3);

    private native void setEventHandler(EventHandler var1);

    private native void detachEventHandler();

    protected Event onEventNative(int eventType, long arg1, long arg2) {
        return null;
    }

    protected void onReleaseNative() {
        this.nativeRelease();
        this.detachEventHandler();
    }

    public static void setOnNativeCrashListener(LibVLC.OnNativeCrashListener l) {
        sOnNativeCrashListener = l;
    }

    private static void onNativeCrash() {
        if(sOnNativeCrashListener != null) {
            sOnNativeCrashListener.onNativeCrash();
        }

    }

    public native int setWindowSize(int var1, int var2);

    private native void nativeNew(String[] var1);

    private native void nativeRelease();

    private void loadNative() throws Throwable {
        if(VERSION.SDK_INT >= 10) {
            try {
                if(VERSION.SDK_INT <= 12) {
                    System.loadLibrary("anw.10");
                } else if(VERSION.SDK_INT <= 13) {
                    System.loadLibrary("anw.13");
                } else if(VERSION.SDK_INT <= 17) {
                    System.loadLibrary("anw.14");
                } else if(VERSION.SDK_INT <= 20) {
                    System.loadLibrary("anw.18");
                } else {
                    System.loadLibrary("anw.21");
                }
            } catch (Throwable ex) {

                Log.w("VLC/LibVLC", "Unable to load the anw library: " + ex);
                throw ex;
            }

            try {
                if(VERSION.SDK_INT <= 10) {
                    System.loadLibrary("iomx.10");
                } else if(VERSION.SDK_INT <= 13) {
                    System.loadLibrary("iomx.13");
                } else if(VERSION.SDK_INT <= 17) {
                    System.loadLibrary("iomx.14");
                } else if(VERSION.SDK_INT <= 18) {
                    System.loadLibrary("iomx.18");
                } else if(VERSION.SDK_INT <= 19) {
                    System.loadLibrary("iomx.19");
                }
            } catch (Throwable var4) {
                if(VERSION.SDK_INT <= 15) {
                    Log.w("VLC/LibVLC", "Unable to load the iomx library: " + var4);
                }
            }
        }

        try {
            System.loadLibrary("vlcjni");
        } catch (UnsatisfiedLinkError var1) {
            Log.e("VLC/LibVLC", "Can\'t load vlcjni library: " + var1);
            throw var1;
//            System.exit(1);
        } catch (SecurityException var2) {
            Log.e("VLC/LibVLC", "Encountered a security issue when loading vlcjni library: " + var2);
            throw  var2;
//            System.exit(1);
        }

    }

    public interface OnNativeCrashListener {
        void onNativeCrash();
    }
}
