//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.videolan.libvlc;

import android.util.SparseArray;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaDiscoverer;
import org.videolan.libvlc.VLCObject;

public class MediaList extends VLCObject {
    private static final String TAG = "LibVLC/MediaList";
    private int mCount = 0;
    private final SparseArray<Media> mMediaArray = new SparseArray();
    private boolean mLocked = false;

    private void init() {
        this.lock();
        this.mCount = this.nativeGetCount();

        for(int i = 0; i < this.mCount; ++i) {
            this.mMediaArray.put(i, new Media(this, i));
        }

        this.unlock();
    }

    public MediaList(LibVLC libVLC) {
        this.nativeNewFromLibVlc(libVLC);
        this.init();
    }

    protected MediaList(MediaDiscoverer md) {
        if(md.isReleased()) {
            throw new IllegalArgumentException("MediaDiscoverer is not native");
        } else {
            this.nativeNewFromMediaDiscoverer(md);
            this.init();
        }
    }

    protected MediaList(Media m) {
        if(m.isReleased()) {
            throw new IllegalArgumentException("Media is not native");
        } else {
            this.nativeNewFromMedia(m);
            this.init();
        }
    }

    private synchronized Media insertMediaFromEvent(int index) {
        ++this.mCount;

        for(int media = this.mCount - 1; media >= index; --media) {
            this.mMediaArray.put(media + 1, this.mMediaArray.valueAt(media));
        }

        Media var3 = new Media(this, index);
        this.mMediaArray.put(index, var3);
        return var3;
    }

    private synchronized Media removeMediaFromEvent(int index) {
        --this.mCount;
        Media media = (Media)this.mMediaArray.get(index);
        if(media != null) {
            media.release();
        }

        for(int i = index; i < this.mCount; ++i) {
            this.mMediaArray.put(i, this.mMediaArray.valueAt(i + 1));
        }

        return media;
    }

    protected synchronized MediaList.Event onEventNative(int eventType, long arg1, long arg2) {
        if(this.mLocked) {
            throw new IllegalStateException("already locked from event callback");
        } else {
            this.mLocked = true;
            MediaList.Event event = null;
            boolean index = true;
            Media media;
            int index1;
            switch(eventType) {
                case 512:
                    index1 = (int)arg1;
                    if(index1 != -1) {
                        media = this.insertMediaFromEvent(index1);
                        event = new MediaList.Event(eventType, media, index1);
                    }
                case 513:
                case 515:
                default:
                    break;
                case 514:
                    index1 = (int)arg1;
                    if(index1 != -1) {
                        media = this.removeMediaFromEvent(index1);
                        event = new MediaList.Event(eventType, media, index1);
                    }
                    break;
                case 516:
                    event = new MediaList.Event(eventType, (Media)null, -1);
            }

            this.mLocked = false;
            return event;
        }
    }

    public synchronized int getCount() {
        return this.mCount;
    }

    public synchronized Media getMediaAt(int index) {
        if(index >= 0 && index < this.getCount()) {
            Media media = (Media)this.mMediaArray.get(index);
            media.retain();
            return media;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public void onReleaseNative() {
        for(int i = 0; i < this.mMediaArray.size(); ++i) {
            Media media = (Media)this.mMediaArray.get(i);
            if(media != null) {
                media.release();
            }
        }

        this.nativeRelease();
    }

    private synchronized void lock() {
        if(this.mLocked) {
            throw new IllegalStateException("already locked");
        } else {
            this.mLocked = true;
            this.nativeLock();
        }
    }

    private synchronized void unlock() {
        if(!this.mLocked) {
            throw new IllegalStateException("not locked");
        } else {
            this.mLocked = false;
            this.nativeUnlock();
        }
    }

    protected synchronized boolean isLocked() {
        return this.mLocked;
    }

    private native void nativeNewFromLibVlc(LibVLC var1);

    private native void nativeNewFromMediaDiscoverer(MediaDiscoverer var1);

    private native void nativeNewFromMedia(Media var1);

    private native void nativeRelease();

    private native int nativeGetCount();

    private native void nativeLock();

    private native void nativeUnlock();

    public static class Event extends org.videolan.libvlc.VLCObject.Event {
        public final Media media;
        public final int index;

        protected Event(int type, Media media, int index) {
            super(type);
            this.media = media;
            this.index = index;
        }
    }
}
