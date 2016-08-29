//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.videolan.libvlc.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import org.videolan.libvlc.util.AndroidUtil;

public class HWDecoderUtil {
    public static final boolean HAS_WINDOW_VOUT = AndroidUtil.isGingerbreadOrLater();
    private static final HWDecoderUtil.DecoderBySOC[] sBlacklistedDecoderBySOCList;
    private static final HWDecoderUtil.DecoderBySOC[] sDecoderBySOCList;
    private static final HWDecoderUtil.AudioOutputBySOC[] sAudioOutputBySOCList;
    private static final HashMap<String, String> sSystemPropertyMap;

    public HWDecoderUtil() {
    }

    public static HWDecoderUtil.Decoder getDecoderFromDevice() {
        HWDecoderUtil.DecoderBySOC[] var0 = sBlacklistedDecoderBySOCList;
        int var1 = var0.length;

        int var2;
        HWDecoderUtil.DecoderBySOC decBySOC;
        String prop;
        for(var2 = 0; var2 < var1; ++var2) {
            decBySOC = var0[var2];
            prop = getSystemPropertyCached(decBySOC.key);
            if(prop != null && prop.contains(decBySOC.value)) {
                return decBySOC.dec;
            }
        }

        if(AndroidUtil.isJellyBeanMR2OrLater()) {
            return HWDecoderUtil.Decoder.ALL;
        } else {
            if(AndroidUtil.isHoneycombOrLater()) {
                var0 = sDecoderBySOCList;
                var1 = var0.length;

                for(var2 = 0; var2 < var1; ++var2) {
                    decBySOC = var0[var2];
                    prop = getSystemPropertyCached(decBySOC.key);
                    if(prop != null && prop.contains(decBySOC.value)) {
                        return decBySOC.dec;
                    }
                }
            }

            return HWDecoderUtil.Decoder.UNKNOWN;
        }
    }

    public static HWDecoderUtil.AudioOutput getAudioOutputFromDevice() {
        if(!AndroidUtil.isGingerbreadOrLater()) {
            return HWDecoderUtil.AudioOutput.AUDIOTRACK;
        } else {
            HWDecoderUtil.AudioOutputBySOC[] var0 = sAudioOutputBySOCList;
            int var1 = var0.length;

            for(int var2 = 0; var2 < var1; ++var2) {
                HWDecoderUtil.AudioOutputBySOC aoutBySOC = var0[var2];
                String prop = getSystemPropertyCached(aoutBySOC.key);
                if(prop != null && prop.contains(aoutBySOC.value)) {
                    return aoutBySOC.aout;
                }
            }

            return HWDecoderUtil.AudioOutput.ALL;
        }
    }

    private static String getSystemPropertyCached(String key) {
        String prop = (String)sSystemPropertyMap.get(key);
        if(prop == null) {
            prop = getSystemProperty(key, "none");
            sSystemPropertyMap.put(key, prop);
        }

        return prop;
    }

    private static String getSystemProperty(String key, String def) {
        try {
            ClassLoader e = ClassLoader.getSystemClassLoader();
            Class SystemProperties = e.loadClass("android.os.SystemProperties");
            Class[] paramTypes = new Class[]{String.class, String.class};
            Method get = SystemProperties.getMethod("get", paramTypes);
            Object[] params = new Object[]{key, def};
            return (String)get.invoke(SystemProperties, params);
        } catch (Exception var7) {
            return def;
        }
    }

    static {
        sBlacklistedDecoderBySOCList = new HWDecoderUtil.DecoderBySOC[]{new HWDecoderUtil.DecoderBySOC("ro.product.board", "msm8916", HWDecoderUtil.Decoder.NONE), new HWDecoderUtil.DecoderBySOC("ro.product.board", "MSM8225", HWDecoderUtil.Decoder.NONE), new HWDecoderUtil.DecoderBySOC("ro.product.board", "hawaii", HWDecoderUtil.Decoder.NONE)};
        sDecoderBySOCList = new HWDecoderUtil.DecoderBySOC[]{new HWDecoderUtil.DecoderBySOC("ro.product.brand", "SEMC", HWDecoderUtil.Decoder.NONE), new HWDecoderUtil.DecoderBySOC("ro.board.platform", "msm7627", HWDecoderUtil.Decoder.NONE), new HWDecoderUtil.DecoderBySOC("ro.board.platform", "omap3", HWDecoderUtil.Decoder.OMX), new HWDecoderUtil.DecoderBySOC("ro.board.platform", "rockchip", HWDecoderUtil.Decoder.OMX), new HWDecoderUtil.DecoderBySOC("ro.board.platform", "rk29", HWDecoderUtil.Decoder.OMX), new HWDecoderUtil.DecoderBySOC("ro.board.platform", "msm7630", HWDecoderUtil.Decoder.OMX), new HWDecoderUtil.DecoderBySOC("ro.board.platform", "s5pc", HWDecoderUtil.Decoder.OMX), new HWDecoderUtil.DecoderBySOC("ro.board.platform", "montblanc", HWDecoderUtil.Decoder.OMX), new HWDecoderUtil.DecoderBySOC("ro.board.platform", "exdroid", HWDecoderUtil.Decoder.OMX), new HWDecoderUtil.DecoderBySOC("ro.board.platform", "sun6i", HWDecoderUtil.Decoder.OMX), new HWDecoderUtil.DecoderBySOC("ro.board.platform", "exynos4", HWDecoderUtil.Decoder.MEDIACODEC), new HWDecoderUtil.DecoderBySOC("ro.board.platform", "omap4", HWDecoderUtil.Decoder.ALL), new HWDecoderUtil.DecoderBySOC("ro.board.platform", "tegra", HWDecoderUtil.Decoder.ALL), new HWDecoderUtil.DecoderBySOC("ro.board.platform", "tegra3", HWDecoderUtil.Decoder.ALL), new HWDecoderUtil.DecoderBySOC("ro.board.platform", "msm8660", HWDecoderUtil.Decoder.ALL), new HWDecoderUtil.DecoderBySOC("ro.board.platform", "exynos5", HWDecoderUtil.Decoder.ALL), new HWDecoderUtil.DecoderBySOC("ro.board.platform", "rk30", HWDecoderUtil.Decoder.ALL), new HWDecoderUtil.DecoderBySOC("ro.board.platform", "rk31", HWDecoderUtil.Decoder.ALL), new HWDecoderUtil.DecoderBySOC("ro.board.platform", "mv88de3100", HWDecoderUtil.Decoder.ALL), new HWDecoderUtil.DecoderBySOC("ro.hardware", "mt83", HWDecoderUtil.Decoder.ALL)};
        sAudioOutputBySOCList = new HWDecoderUtil.AudioOutputBySOC[]{new HWDecoderUtil.AudioOutputBySOC("ro.product.brand", "Amazon", HWDecoderUtil.AudioOutput.OPENSLES)};
        sSystemPropertyMap = new HashMap();
    }

    private static class AudioOutputBySOC {
        public final String key;
        public final String value;
        public final HWDecoderUtil.AudioOutput aout;

        public AudioOutputBySOC(String key, String value, HWDecoderUtil.AudioOutput aout) {
            this.key = key;
            this.value = value;
            this.aout = aout;
        }
    }

    private static class DecoderBySOC {
        public final String key;
        public final String value;
        public final HWDecoderUtil.Decoder dec;

        public DecoderBySOC(String key, String value, HWDecoderUtil.Decoder dec) {
            this.key = key;
            this.value = value;
            this.dec = dec;
        }
    }

    public static enum AudioOutput {
        OPENSLES,
        AUDIOTRACK,
        ALL;

        private AudioOutput() {
        }
    }

    public static enum Decoder {
        UNKNOWN,
        NONE,
        OMX,
        MEDIACODEC,
        ALL;

        private Decoder() {
        }
    }
}
