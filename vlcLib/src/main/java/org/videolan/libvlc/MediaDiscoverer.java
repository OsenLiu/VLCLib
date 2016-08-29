//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.videolan.libvlc;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.MediaList;
import org.videolan.libvlc.VLCObject;
import org.videolan.libvlc.VLCObject.Event;

public class MediaDiscoverer extends VLCObject {
    private static final String TAG = "LibVLC/MediaDiscoverer";
    private MediaList mMediaList;

    public MediaDiscoverer(LibVLC libVLC, String name) {
        this.nativeNew(libVLC, name);
    }

    public boolean start() {
        if(this.isReleased()) {
            throw new IllegalStateException("MediaDiscoverer is released");
        } else {
            return this.nativeStart();
        }
    }

    public void stop() {
        if(this.isReleased()) {
            throw new IllegalStateException("MediaDiscoverer is released");
        } else {
            this.nativeStop();
        }
    }

    protected Event onEventNative(int event, long arg1, long arg2) {
        return null;
    }

    public synchronized MediaList getMediaList() {
        if(this.isReleased()) {
            throw new IllegalStateException("MediaDiscoverer is released");
        } else {
            if(this.mMediaList == null) {
                this.mMediaList = new MediaList(this);
            }

            this.mMediaList.retain();
            return this.mMediaList;
        }
    }

    protected void onReleaseNative() {
        if(this.mMediaList != null) {
            this.mMediaList.release();
        }

        this.nativeRelease();
    }

    private native void nativeNew(LibVLC var1, String var2);

    private native void nativeRelease();

    private native boolean nativeStart();

    private native void nativeStop();
}
