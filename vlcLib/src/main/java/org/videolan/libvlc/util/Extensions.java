//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.videolan.libvlc.util;

import java.util.Arrays;
import java.util.HashSet;

public class Extensions {
    public static final HashSet<String> VIDEO = new HashSet();
    public static final HashSet<String> AUDIO = new HashSet();
    public static final HashSet<String> SUBTITLES = new HashSet();
    public static final HashSet<String> PLAYLIST = new HashSet();

    public Extensions() {
    }

    static {
        String[] videoExtensions = new String[]{".3g2", ".3gp", ".3gp2", ".3gpp", ".amv", ".asf", ".avi", ".divx", ".drc", ".dv", ".f4v", ".flv", ".gvi", ".gxf", ".ismv", ".iso", ".m1v", ".m2v", ".m2t", ".m2ts", ".m4v", ".mkv", ".mov", ".mp2", ".mp2v", ".mp4", ".mp4v", ".mpe", ".mpeg", ".mpeg1", ".mpeg2", ".mpeg4", ".mpg", ".mpv2", ".mts", ".mtv", ".mxf", ".mxg", ".nsv", ".nut", ".nuv", ".ogm", ".ogv", ".ogx", ".ps", ".rec", ".rm", ".rmvb", ".tod", ".ts", ".tts", ".vob", ".vro", ".webm", ".wm", ".wmv", ".wtv", ".xesc"};
        String[] audioExtensions = new String[]{".3ga", ".a52", ".aac", ".ac3", ".adt", ".adts", ".aif", ".aifc", ".aiff", ".amr", ".aob", ".ape", ".awb", ".caf", ".dts", ".flac", ".it", ".m4a", ".m4b", ".m4p", ".mid", ".mka", ".mlp", ".mod", ".mpa", ".mp1", ".mp2", ".mp3", ".mpc", ".mpga", ".oga", ".ogg", ".oma", ".opus", ".ra", ".ram", ".rmi", ".s3m", ".spx", ".tta", ".voc", ".vqf", ".w64", ".wav", ".wma", ".wv", ".xa", ".xm"};
        String[] subtitlesExtensions = new String[]{".idx", ".sub", ".srt", ".ssa", ".ass", ".smi", ".utf", ".utf8", ".utf-8", ".rt", ".aqt", ".txt", ".usf", ".jss", ".cdg", ".psb", ".mpsub", ".mpl2", ".pjs", ".dks", ".stl", ".vtt"};
        String[] playlistExtensions = new String[]{".m3u", ".asx", ".b4s", ".pls", ".xspf"};
        VIDEO.addAll(Arrays.asList(videoExtensions));
        AUDIO.addAll(Arrays.asList(audioExtensions));
        SUBTITLES.addAll(Arrays.asList(subtitlesExtensions));
        PLAYLIST.addAll(Arrays.asList(playlistExtensions));
    }
}
