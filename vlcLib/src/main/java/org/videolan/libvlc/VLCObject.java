//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.videolan.libvlc;

import android.os.Handler;
import android.os.Looper;
import java.lang.ref.WeakReference;

public abstract class VLCObject {
    private static final String TAG = "LibVLC/VlcObject";
    private VLCObject.EventListener mEventListener = null;
    private Handler mHandler = null;
    private int mNativeRefCount = 1;
    private long mInstance = 0L;

    public VLCObject() {
    }

    public synchronized boolean isReleased() {
        return this.mNativeRefCount == 0;
    }

    public final synchronized boolean retain() {
        if(this.mNativeRefCount > 0) {
            ++this.mNativeRefCount;
            return true;
        } else {
            return false;
        }
    }

    public final void release() {
        int refCount = -1;
        synchronized(this) {
            if(this.mNativeRefCount == 0) {
                return;
            }

            if(this.mNativeRefCount > 0) {
                refCount = --this.mNativeRefCount;
            }

            if(refCount == 0) {
                this.setEventListener((VLCObject.EventListener)null);
            }
        }

        if(refCount == 0) {
            this.nativeDetachEvents();
            synchronized(this) {
                this.onReleaseNative();
            }
        }

    }

    public final synchronized void setEventListener(VLCObject.EventListener listener) {
        if(this.mHandler != null) {
            this.mHandler.removeCallbacksAndMessages((Object)null);
        }

        this.mEventListener = listener;
        if(this.mEventListener != null && this.mHandler == null) {
            this.mHandler = new Handler(Looper.getMainLooper());
        }

    }

    protected abstract VLCObject.Event onEventNative(int var1, long var2, long var4);

    protected abstract void onReleaseNative();

    private synchronized void dispatchEventFromNative(int eventType, long arg1, long arg2) {
        if(!this.isReleased()) {
            VLCObject.Event event = this.onEventNative(eventType, arg1, arg2);
            if(event != null && this.mEventListener != null && this.mHandler != null) {
                this.mHandler.post(new VLCObject.EventRunnable(this.mEventListener, event));
            }

        }
    }

    private native void nativeDetachEvents();

    private Object getWeakReference() {
        return new WeakReference(this);
    }

    private static void dispatchEventFromWeakNative(Object weak, int eventType, long arg1, long arg2) {
        VLCObject obj = (VLCObject)((WeakReference)weak).get();
        if(obj != null) {
            obj.dispatchEventFromNative(eventType, arg1, arg2);
        }

    }

    private static class EventRunnable implements Runnable {
        private final VLCObject.EventListener listener;
        private final VLCObject.Event event;

        private EventRunnable(VLCObject.EventListener listener, VLCObject.Event event) {
            this.listener = listener;
            this.event = event;
        }

        public void run() {
            this.listener.onEvent(this.event);
        }
    }

    public interface EventListener {
        void onEvent(VLCObject.Event var1);
    }

    public static class Event {
        public final int type;

        protected Event(int type) {
            this.type = type;
        }
    }

    public static class Events {
        public static final int MediaMetaChanged = 0;
        public static final int MediaSubItemAdded = 1;
        public static final int MediaDurationChanged = 2;
        public static final int MediaParsedChanged = 3;
        public static final int MediaStateChanged = 5;
        public static final int MediaSubItemTreeAdded = 6;
        public static final int MediaListItemAdded = 512;
        public static final int MediaListItemDeleted = 514;
        public static final int MediaListEndReached = 516;
        public static final int MediaDiscovererStarted = 1280;
        public static final int MediaDiscovererEnded = 1281;

        public Events() {
        }
    }
}
