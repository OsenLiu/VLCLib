//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.videolan.libvlc.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Locale;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.util.AndroidUtil;

public class VLCUtil {
    public static final String TAG = "VLC/LibVLC/Util";
    private static String errorMsg = null;
    private static boolean isCompatible = false;
    private static VLCUtil.MachineSpecs machineSpecs = null;
    private static final int EM_386 = 3;
    private static final int EM_MIPS = 8;
    private static final int EM_ARM = 40;
    private static final int EM_X86_64 = 62;
    private static final int EM_AARCH64 = 183;
    private static final int ELF_HEADER_SIZE = 52;
    private static final int SECTION_HEADER_SIZE = 40;
    private static final int SHT_ARM_ATTRIBUTES = 1879048195;
    private static final String[] CPU_archs = new String[]{"*Pre-v4", "*v4", "*v4T", "v5T", "v5TE", "v5TEJ", "v6", "v6KZ", "v6T2", "v6K", "v7", "*v6-M", "*v6S-M", "*v7E-M", "*v8"};

    public VLCUtil() {
    }

    public static String getErrorMsg() {
        return errorMsg;
    }

    public static boolean hasCompatibleCPU(Context context) {
        if(errorMsg == null && !isCompatible) {
            File lib = searchLibrary(context.getApplicationInfo());
            if(lib == null) {
                return true;
            } else {
                VLCUtil.ElfData elf = readLib(lib);
                if(elf == null) {
                    Log.e("VLC/LibVLC/Util", "WARNING: Unable to read libvlcjni.so; cannot check device ABI!");
                    Log.e("VLC/LibVLC/Util", "WARNING: Cannot guarantee correct ABI for this build (may crash)!");
                    return true;
                } else {
                    String CPU_ABI = Build.CPU_ABI;
                    String CPU_ABI2 = "none";
                    if(VERSION.SDK_INT >= 8) {
                        try {
                            CPU_ABI2 = (String)Build.class.getDeclaredField("CPU_ABI2").get((Object)null);
                        } catch (Exception var70) {
                            ;
                        }
                    }

                    boolean elfHasX86 = elf.e_machine == 3 || elf.e_machine == 62;
                    boolean elfHasArm = elf.e_machine == 40 || elf.e_machine == 183;
                    boolean elfHasMips = elf.e_machine == 8;
                    boolean elfIs64bits = elf.is64bits;
                    Log.i("VLC/LibVLC/Util", "ELF ABI = " + (elfHasArm?"arm":(elfHasX86?"x86":"mips")) + ", " + (elfIs64bits?"64bits":"32bits"));
                    Log.i("VLC/LibVLC/Util", "ELF arch = " + elf.att_arch);
                    Log.i("VLC/LibVLC/Util", "ELF fpu = " + elf.att_fpu);
                    boolean hasNeon = false;
                    boolean hasFpu = false;
                    boolean hasArmV6 = false;
                    boolean hasArmV7 = false;
                    boolean hasMips = false;
                    boolean hasX86 = false;
                    boolean is64bits = false;
                    float bogoMIPS = -1.0F;
                    int processors = 0;
                    if(!CPU_ABI.equals("x86") && !CPU_ABI2.equals("x86")) {
                        if(!CPU_ABI.equals("x86_64") && !CPU_ABI2.equals("x86_64")) {
                            if(!CPU_ABI.equals("armeabi-v7a") && !CPU_ABI2.equals("armeabi-v7a")) {
                                if(!CPU_ABI.equals("armeabi") && !CPU_ABI2.equals("armeabi")) {
                                    if(CPU_ABI.equals("arm64-v8a") || CPU_ABI2.equals("arm64-v8a")) {
                                        hasNeon = true;
                                        hasArmV6 = true;
                                        hasArmV7 = true;
                                        is64bits = true;
                                    }
                                } else {
                                    hasArmV6 = true;
                                }
                            } else {
                                hasArmV7 = true;
                                hasArmV6 = true;
                            }
                        } else {
                            hasX86 = true;
                            is64bits = true;
                        }
                    } else {
                        hasX86 = true;
                    }

                    FileReader fileReader = null;
                    BufferedReader br = null;

                    label937: {
                        boolean line;
                        try {
                            fileReader = new FileReader("/proc/cpuinfo");
                            br = new BufferedReader(fileReader);

                            while(true) {
                                String frequency;
                                if((frequency = br.readLine()) == null) {
                                    break label937;
                                }

                                if(!hasArmV7 && frequency.contains("AArch64")) {
                                    hasArmV7 = true;
                                    hasArmV6 = true;
                                }

                                if(!hasArmV7 && frequency.contains("ARMv7")) {
                                    hasArmV7 = true;
                                    hasArmV6 = true;
                                }

                                if(!hasArmV7 && !hasArmV6 && frequency.contains("ARMv6")) {
                                    hasArmV6 = true;
                                }

                                if(frequency.contains("clflush size")) {
                                    hasX86 = true;
                                }

                                if(frequency.contains("GenuineIntel")) {
                                    hasX86 = true;
                                }

                                if(frequency.contains("microsecond timers")) {
                                    hasMips = true;
                                }

                                if(!hasNeon && (frequency.contains("neon") || frequency.contains("asimd"))) {
                                    hasNeon = true;
                                }

                                if(!hasFpu && (frequency.contains("vfp") || frequency.contains("Features") && frequency.contains("fp"))) {
                                    hasFpu = true;
                                }

                                if(frequency.startsWith("processor")) {
                                    ++processors;
                                }

                                if(bogoMIPS < 0.0F && frequency.toLowerCase(Locale.ENGLISH).contains("bogomips")) {
                                    String[] var75 = frequency.split(":");

                                    try {
                                        bogoMIPS = Float.parseFloat(var75[1].trim());
                                    } catch (NumberFormatException var69) {
                                        bogoMIPS = -1.0F;
                                    }
                                }
                            }
                        } catch (IOException var72) {
                            var72.printStackTrace();
                            errorMsg = "IOException whilst reading cpuinfo flags";
                            isCompatible = false;
                            line = false;
                        } finally {
                            if(br != null) {
                                try {
                                    br.close();
                                } catch (IOException var66) {
                                    ;
                                }
                            }

                            if(fileReader != null) {
                                try {
                                    fileReader.close();
                                } catch (IOException var65) {
                                    ;
                                }
                            }

                        }

                        return line;
                    }

                    if(processors == 0) {
                        processors = 1;
                    }

                    if(elfHasX86 && !hasX86) {
                        errorMsg = "x86 build on non-x86 device";
                        isCompatible = false;
                        return false;
                    } else if(elfHasArm && hasX86) {
                        errorMsg = "ARM build on x86 device";
                        isCompatible = false;
                        return false;
                    } else if(elfHasMips && !hasMips) {
                        errorMsg = "MIPS build on non-MIPS device";
                        isCompatible = false;
                        return false;
                    } else if(elfHasArm && hasMips) {
                        errorMsg = "ARM build on MIPS device";
                        isCompatible = false;
                        return false;
                    } else if(elf.e_machine == 40 && elf.att_arch.startsWith("v7") && !hasArmV7) {
                        errorMsg = "ARMv7 build on non-ARMv7 device";
                        isCompatible = false;
                        return false;
                    } else {
                        if(elf.e_machine == 40) {
                            if(elf.att_arch.startsWith("v6") && !hasArmV6) {
                                errorMsg = "ARMv6 build on non-ARMv6 device";
                                isCompatible = false;
                                return false;
                            }

                            if(elf.att_fpu && !hasFpu) {
                                errorMsg = "FPU-enabled build on non-FPU device";
                                isCompatible = false;
                                return false;
                            }
                        }

                        if(elfIs64bits && !is64bits) {
                            errorMsg = "64bits build on 32bits device";
                            isCompatible = false;
                        }

                        float var74 = -1.0F;
                        fileReader = null;
                        br = null;
                        String var76 = "";

                        try {
                            fileReader = new FileReader("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq");
                            br = new BufferedReader(fileReader);
                            var76 = br.readLine();
                            if(var76 != null) {
                                var74 = Float.parseFloat(var76) / 1000.0F;
                            }
                        } catch (IOException var67) {
                            Log.w("VLC/LibVLC/Util", "Could not find maximum CPU frequency!");
                        } catch (NumberFormatException var68) {
                            Log.w("VLC/LibVLC/Util", "Could not parse maximum CPU frequency!");
                            Log.w("VLC/LibVLC/Util", "Failed to parse: " + var76);
                        } finally {
                            if(br != null) {
                                try {
                                    br.close();
                                } catch (IOException var64) {
                                    ;
                                }
                            }

                            if(fileReader != null) {
                                try {
                                    fileReader.close();
                                } catch (IOException var63) {
                                    ;
                                }
                            }

                        }

                        errorMsg = null;
                        isCompatible = true;
                        machineSpecs = new VLCUtil.MachineSpecs();
                        machineSpecs.hasArmV6 = hasArmV6;
                        machineSpecs.hasArmV7 = hasArmV7;
                        machineSpecs.hasFpu = hasFpu;
                        machineSpecs.hasMips = hasMips;
                        machineSpecs.hasNeon = hasNeon;
                        machineSpecs.hasX86 = hasX86;
                        machineSpecs.is64bits = is64bits;
                        machineSpecs.bogoMIPS = bogoMIPS;
                        machineSpecs.processors = processors;
                        machineSpecs.frequency = var74;
                        return true;
                    }
                }
            }
        } else {
            return isCompatible;
        }
    }

    public static VLCUtil.MachineSpecs getMachineSpecs() {
        return machineSpecs;
    }

    @TargetApi(9)
    private static File searchLibrary(ApplicationInfo applicationInfo) {
        String[] libraryPaths;
        if((applicationInfo.flags & 1) != 0) {
            String lib = System.getProperty("java.library.path");
            libraryPaths = lib.split(":");
        } else {
            libraryPaths = new String[1];
            if(AndroidUtil.isGingerbreadOrLater()) {
                libraryPaths[0] = applicationInfo.nativeLibraryDir;
            } else {
                libraryPaths[0] = applicationInfo.dataDir + "/lib";
            }
        }

        if(libraryPaths[0] == null) {
            Log.e("VLC/LibVLC/Util", "can\'t find library path");
            return null;
        } else {
            String[] var3 = libraryPaths;
            int var4 = libraryPaths.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                String libraryPath = var3[var5];
                File var7 = new File(libraryPath, "libvlcjni.so");
                if(var7.exists() && var7.canRead()) {
                    return var7;
                }
            }

            Log.e("VLC/LibVLC/Util", "WARNING: Can\'t find shared library");
            return null;
        }
    }

    private static VLCUtil.ElfData readLib(File file) {
        RandomAccessFile in = null;

        try {
            in = new RandomAccessFile(file, "r");
            VLCUtil.ElfData e = new VLCUtil.ElfData();
            VLCUtil.ElfData var3;
            if(!readHeader(in, e)) {
                var3 = null;
                return var3;
            } else {
                switch(e.e_machine) {
                    case 3:
                    case 8:
                    case 62:
                    case 183:
                        var3 = e;
                        return var3;
                    case 40:
                        in.close();
                        in = new RandomAccessFile(file, "r");
                        if(!readSection(in, e)) {
                            var3 = null;
                            return var3;
                        } else {
                            in.close();
                            in = new RandomAccessFile(file, "r");
                            if(!readArmAttributes(in, e)) {
                                var3 = null;
                                return var3;
                            }

                            var3 = e;
                            return var3;
                        }
                    default:
                        var3 = null;
                        return var3;
                }
            }
        } catch (FileNotFoundException var20) {
            var20.printStackTrace();
            return null;
        } catch (IOException var21) {
            var21.printStackTrace();
            return null;
        } finally {
            try {
                if(in != null) {
                    in.close();
                }
            } catch (IOException var19) {
                ;
            }

        }
    }

    private static boolean readHeader(RandomAccessFile in, VLCUtil.ElfData elf) throws IOException {
        byte[] bytes = new byte[52];
        in.readFully(bytes);
        if(bytes[0] != 127 || bytes[1] != 69 || bytes[2] != 76 || bytes[3] != 70 || bytes[4] != 1 && bytes[4] != 2) {
            Log.e("VLC/LibVLC/Util", "ELF header invalid");
            return false;
        } else {
            elf.is64bits = bytes[4] == 2;
            elf.order = bytes[5] == 1?ByteOrder.LITTLE_ENDIAN:ByteOrder.BIG_ENDIAN;
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            buffer.order(elf.order);
            elf.e_machine = buffer.getShort(18);
            elf.e_shoff = buffer.getInt(32);
            elf.e_shnum = buffer.getShort(48);
            return true;
        }
    }

    private static boolean readSection(RandomAccessFile in, VLCUtil.ElfData elf) throws IOException {
        byte[] bytes = new byte[40];
        in.seek((long)elf.e_shoff);

        for(int i = 0; i < elf.e_shnum; ++i) {
            in.readFully(bytes);
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            buffer.order(elf.order);
            int sh_type = buffer.getInt(4);
            if(sh_type == 1879048195) {
                elf.sh_offset = buffer.getInt(16);
                elf.sh_size = buffer.getInt(20);
                return true;
            }
        }

        return false;
    }

    private static boolean readArmAttributes(RandomAccessFile in, VLCUtil.ElfData elf) throws IOException {
        byte[] bytes = new byte[elf.sh_size];
        in.seek((long)elf.sh_offset);
        in.readFully(bytes);
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(elf.order);
        if(buffer.get() != 65) {
            return false;
        } else {
            while(buffer.remaining() > 0) {
                int start_section = buffer.position();
                int length = buffer.getInt();
                String vendor = getString(buffer);
                if(vendor.equals("aeabi")) {
                    while(true) {
                        label58:
                        while(buffer.position() < start_section + length) {
                            int start = buffer.position();
                            byte tag = buffer.get();
                            int size = buffer.getInt();
                            if(tag != 1) {
                                buffer.position(start + size);
                            } else {
                                while(true) {
                                    while(true) {
                                        if(buffer.position() >= start + size) {
                                            continue label58;
                                        }

                                        int tag1 = getUleb128(buffer);
                                        if(tag1 == 6) {
                                            int arch = getUleb128(buffer);
                                            elf.att_arch = CPU_archs[arch];
                                        } else if(tag1 == 27) {
                                            getUleb128(buffer);
                                            elf.att_fpu = true;
                                        } else {
                                            tag1 %= 128;
                                            if(tag1 != 4 && tag1 != 5 && tag1 != 32 && (tag1 <= 32 || (tag1 & 1) == 0)) {
                                                getUleb128(buffer);
                                            } else {
                                                getString(buffer);
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        return true;
                    }
                }
            }

            return true;
        }
    }

    private static String getString(ByteBuffer buffer) {
        StringBuilder sb = new StringBuilder(buffer.limit());

        while(buffer.remaining() > 0) {
            char c = (char)buffer.get();
            if(c == 0) {
                break;
            }

            sb.append(c);
        }

        return sb.toString();
    }

    private static int getUleb128(ByteBuffer buffer) {
        int ret = 0;

        byte c;
        do {
            ret <<= 7;
            c = buffer.get();
            ret |= c & 127;
        } while((c & 128) > 0);

        return ret;
    }

    public static byte[] getThumbnail(LibVLC libVLC, Uri uri, int i_width, int i_height) {
        Media media = new Media(libVLC, uri);
        byte[] bytes = getThumbnail(media, i_width, i_height);
        media.release();
        return bytes;
    }

    public static byte[] getThumbnail(LibVLC libVLC, String path, int i_width, int i_height) {
        Media media = new Media(libVLC, path);
        byte[] bytes = getThumbnail(media, i_width, i_height);
        media.release();
        return bytes;
    }

    public static byte[] getThumbnail(Media media, int i_width, int i_height) {
        media.addOption(":no-audio");
        media.addOption(":no-spu");
        media.addOption(":no-osd");
        return nativeGetThumbnail(media, i_width, i_height);
    }

    private static native byte[] nativeGetThumbnail(Media var0, int var1, int var2);

    private static class ElfData {
        ByteOrder order;
        boolean is64bits;
        int e_machine;
        int e_shoff;
        int e_shnum;
        int sh_offset;
        int sh_size;
        String att_arch;
        boolean att_fpu;

        private ElfData() {
        }
    }

    public static class MachineSpecs {
        public boolean hasNeon;
        public boolean hasFpu;
        public boolean hasArmV6;
        public boolean hasArmV7;
        public boolean hasMips;
        public boolean hasX86;
        public boolean is64bits;
        public float bogoMIPS;
        public int processors;
        public float frequency;

        public MachineSpecs() {
        }
    }
}
