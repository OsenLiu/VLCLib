//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.videolan.libvlc;

import android.net.Uri;

import java.io.FileDescriptor;

public class Media extends VLCObject {
    private static final String TAG = "LibVLC/Media";
    private static final int PARSE_STATUS_INIT = 0;
    private static final int PARSE_STATUS_PARSING = 1;
    private static final int PARSE_STATUS_PARSED = 2;
    private String mMrl = null;
    private MediaList mSubItems = null;
    private int mParseStatus = 0;
    private String[] mNativeMetas = null;
    private Media.Track[] mNativeTracks = null;
    private long mDuration;
    private int mState = 0;
    private int mType = 0;

    private static Media.Track createAudioTrackFromNative(String codec, String originalCodec, int id, int profile, int level, int bitrate, String language, String description, int channels, int rate) {
        return new Media.AudioTrack(codec, originalCodec, id, profile, level, bitrate, language, description, channels, rate);
    }

    private static Media.Track createVideoTrackFromNative(String codec, String originalCodec, int id, int profile, int level, int bitrate, String language, String description, int height, int width, int sarNum, int sarDen, int frameRateNum, int frameRateDen) {
        return new Media.VideoTrack(codec, originalCodec, id, profile, level, bitrate, language, description, height, width, sarNum, sarDen, frameRateNum, frameRateDen);
    }

    private static Media.Track createSubtitleTrackFromNative(String codec, String originalCodec, int id, int profile, int level, int bitrate, String language, String description, String encoding) {
        return new Media.SubtitleTrack(codec, originalCodec, id, profile, level, bitrate, language, description, encoding);
    }

    public Media(LibVLC libVLC, String path) {
        this.nativeNewFromPath(libVLC, path);
        this.mMrl = this.nativeGetMrl();
        this.mType = this.nativeGetType();
    }

    public Media(LibVLC libVLC, Uri uri) {
        this.nativeNewFromLocation(libVLC, uri.toString());
        this.mMrl = this.nativeGetMrl();
        this.mType = this.nativeGetType();
    }

    public Media(LibVLC libVLC, FileDescriptor fd) {
        this.nativeNewFromFD(libVLC, fd);
        this.mMrl = this.nativeGetMrl();
        this.mType = this.nativeGetType();
    }

    protected Media(MediaList ml, int index) {
        if(ml != null && !ml.isReleased()) {
            if(!ml.isLocked()) {
                throw new IllegalStateException("MediaList should be locked");
            } else {
                this.nativeNewFromMediaList(ml, index);
                this.mMrl = this.nativeGetMrl();
                this.mNativeMetas = this.nativeGetMetas();
                this.mType = this.nativeGetType();
            }
        } else {
            throw new IllegalArgumentException("MediaList is null or released");
        }
    }

    protected synchronized Event onEventNative(int eventType, long arg1, long arg2) {
        switch(eventType) {
            case 0:
                if(this.mNativeMetas == null) {
                    this.mNativeMetas = this.nativeGetMetas();
                } else {
                    int id = (int)arg1;
                    if(id >= 0 && id < 25) {
                        this.mNativeMetas[id] = this.nativeGetMeta(id);
                    }
                }
            case 1:
            case 4:
            default:
                break;
            case 2:
                this.mDuration = this.nativeGetDuration();
                break;
            case 3:
                this.postParse();
                break;
            case 5:
                this.mState = this.nativeGetState();
        }

        return new Event(eventType);
    }

    public synchronized String getMrl() {
        return this.mMrl;
    }

    public synchronized long getDuration() {
        return this.mDuration;
    }

    public synchronized int getState() {
        return this.mState;
    }

    public synchronized MediaList subItems() {
        if(this.isReleased()) {
            throw new IllegalStateException("Media is released");
        } else {
            if(this.mSubItems == null) {
                this.mSubItems = new MediaList(this);
            }

            this.mSubItems.retain();
            return this.mSubItems;
        }
    }

    private synchronized void postParse() {
        if(this.isReleased()) {
            throw new IllegalStateException("Media is released");
        } else {
            if((this.mParseStatus & 1) != 0 && (this.mParseStatus & 2) == 0) {
                this.mParseStatus &= -2;
                this.mParseStatus |= 2;
                this.mNativeTracks = this.nativeGetTracks();
                this.mNativeMetas = this.nativeGetMetas();
                if(this.mNativeMetas != null && this.mNativeMetas.length != 25) {
                    throw new IllegalStateException("native metas size doesn\'t match");
                }

                this.mDuration = this.nativeGetDuration();
                this.mState = this.nativeGetState();
                this.mType = this.nativeGetType();
            }

        }
    }

    public synchronized boolean parse(int flags) {
        if((this.mParseStatus & 3) == 0) {
            this.mParseStatus |= 1;
            if(this.nativeParse(flags)) {
                this.postParse();
                return true;
            }
        }

        return false;
    }

    public synchronized boolean parse() {
        return this.parse(2);
    }

    public synchronized boolean parseAsync(int flags) {
        if((this.mParseStatus & 3) == 0) {
            this.mParseStatus |= 1;
            return this.nativeParseAsync(flags);
        } else {
            return false;
        }
    }

    public synchronized boolean parseAsync() {
        return this.parseAsync(2);
    }

    public synchronized boolean isParsed() {
        return (this.mParseStatus & 2) != 0;
    }

    public synchronized int getType() {
        return this.mType;
    }

    public synchronized int getTrackCount() {
        return this.mNativeTracks != null?this.mNativeTracks.length:0;
    }

    public synchronized Media.Track getTrack(int idx) {
        return this.mNativeTracks != null && idx >= 0 && idx < this.mNativeTracks.length?this.mNativeTracks[idx]:null;
    }

    public synchronized String getMeta(int id) {
        return id >= 0 && id < 25?(this.mNativeMetas != null?this.mNativeMetas[id]:null):null;
    }

    public synchronized void addOption(String option) {
        this.nativeAddOption(option);
    }

    protected void onReleaseNative() {
        if(this.mSubItems != null) {
            this.mSubItems.release();
        }

        this.nativeRelease();
    }

    private native void nativeNewFromPath(LibVLC var1, String var2);

    private native void nativeNewFromLocation(LibVLC var1, String var2);

    private native void nativeNewFromFD(LibVLC var1, FileDescriptor var2);

    private native void nativeNewFromMediaList(MediaList var1, int var2);

    private native void nativeRelease();

    private native boolean nativeParseAsync(int var1);

    private native boolean nativeParse(int var1);

    private native String nativeGetMrl();

    private native int nativeGetState();

    private native String nativeGetMeta(int var1);

    private native String[] nativeGetMetas();

    private native Media.Track[] nativeGetTracks();

    private native long nativeGetDuration();

    private native int nativeGetType();

    private native void nativeAddOption(String var1);

    public static class SubtitleTrack extends Media.Track {
        public final String encoding;

        private SubtitleTrack(String codec, String originalCodec, int id, int profile, int level, int bitrate, String language, String description, String encoding) {
            super(2, codec, originalCodec, id, profile, level, bitrate, language, description);
            this.encoding = encoding;
        }
    }

    public static class VideoTrack extends Media.Track {
        public final int height;
        public final int width;
        public final int sarNum;
        public final int sarDen;
        public final int frameRateNum;
        public final int frameRateDen;

        private VideoTrack(String codec, String originalCodec, int id, int profile, int level, int bitrate, String language, String description, int height, int width, int sarNum, int sarDen, int frameRateNum, int frameRateDen) {
            super(1, codec, originalCodec, id, profile, level, bitrate, language, description);
            this.height = height;
            this.width = width;
            this.sarNum = sarNum;
            this.sarDen = sarDen;
            this.frameRateNum = frameRateNum;
            this.frameRateDen = frameRateDen;
        }
    }

    public static class AudioTrack extends Media.Track {
        public final int channels;
        public final int rate;

        private AudioTrack(String codec, String originalCodec, int id, int profile, int level, int bitrate, String language, String description, int channels, int rate) {
            super(0, codec, originalCodec, id, profile, level, bitrate, language, description);
            this.channels = channels;
            this.rate = rate;
        }
    }

    public abstract static class Track {
        public final int type;
        public final String codec;
        public final String originalCodec;
        public final int id;
        public final int profile;
        public final int level;
        public final int bitrate;
        public final String language;
        public final String description;

        private Track(int type, String codec, String originalCodec, int id, int profile, int level, int bitrate, String language, String description) {
            this.type = type;
            this.codec = codec;
            this.originalCodec = originalCodec;
            this.id = id;
            this.profile = profile;
            this.level = level;
            this.bitrate = bitrate;
            this.language = language;
            this.description = description;
        }

        public static class Type {
            public static final int Unknown = -1;
            public static final int Audio = 0;
            public static final int Video = 1;
            public static final int Text = 2;

            public Type() {
            }
        }
    }

    public static class Parse {
        public static final int ParseLocal = 0;
        public static final int ParseNetwork = 1;
        public static final int FetchLocal = 2;
        public static final int FetchNetwork = 4;

        public Parse() {
        }
    }

    public static class State {
        public static final int NothingSpecial = 0;
        public static final int Opening = 1;
        public static final int Buffering = 2;
        public static final int Playing = 3;
        public static final int Paused = 4;
        public static final int Stopped = 5;
        public static final int Ended = 6;
        public static final int Error = 7;
        public static final int MAX = 8;

        public State() {
        }
    }

    public static class Meta {
        public static final int Title = 0;
        public static final int Artist = 1;
        public static final int Genre = 2;
        public static final int Copyright = 3;
        public static final int Album = 4;
        public static final int TrackNumber = 5;
        public static final int Description = 6;
        public static final int Rating = 7;
        public static final int Date = 8;
        public static final int Setting = 9;
        public static final int URL = 10;
        public static final int Language = 11;
        public static final int NowPlaying = 12;
        public static final int Publisher = 13;
        public static final int EncodedBy = 14;
        public static final int ArtworkURL = 15;
        public static final int TrackID = 16;
        public static final int TrackTotal = 17;
        public static final int Director = 18;
        public static final int Season = 19;
        public static final int Episode = 20;
        public static final int ShowName = 21;
        public static final int Actors = 22;
        public static final int AlbumArtist = 23;
        public static final int DiscNumber = 24;
        public static final int MAX = 25;

        public Meta() {
        }
    }

    public static class Type {
        public static final int Unknown = 0;
        public static final int File = 1;
        public static final int Directory = 2;
        public static final int Disc = 3;
        public static final int Stream = 4;
        public static final int Playlist = 5;

        public Type() {
        }
    }
}
