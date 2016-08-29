//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.videolan.libvlc;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import java.util.ArrayList;

public class EventHandler {
    public static final int MediaMetaChanged = 0;
    public static final int MediaParsedChanged = 3;
    public static final int MediaPlayerBuffering = 259;
    public static final int MediaPlayerPlaying = 260;
    public static final int MediaPlayerPaused = 261;
    public static final int MediaPlayerStopped = 262;
    public static final int MediaPlayerEndReached = 265;
    public static final int MediaPlayerEncounteredError = 266;
    public static final int MediaPlayerTimeChanged = 267;
    public static final int MediaPlayerPositionChanged = 268;
    public static final int MediaPlayerVout = 274;
    public static final int MediaPlayerESAdded = 276;
    public static final int MediaPlayerESDeleted = 277;
    public static final int CustomMediaListItemAdded = 8194;
    public static final int CustomMediaListItemDeleted = 8195;
    public static final int CustomMediaListItemMoved = 8196;
    public static final int HardwareAccelerationError = 12288;
    private ArrayList<Handler> mEventHandler = new ArrayList();
    private static EventHandler mInstance;

    EventHandler() {
    }

    public static EventHandler getInstance() {
        if(mInstance == null) {
            mInstance = new EventHandler();
        }

        return mInstance;
    }

    public void addHandler(Handler handler) {
        if(!this.mEventHandler.contains(handler)) {
            this.mEventHandler.add(handler);
        }

    }

    public void removeHandler(Handler handler) {
        this.mEventHandler.remove(handler);
    }

    public void callback(int event, Bundle b) {
        b.putInt("event", event);

        for(int i = 0; i < this.mEventHandler.size(); ++i) {
            Message msg = Message.obtain();
            msg.setData(b);
            ((Handler)this.mEventHandler.get(i)).sendMessage(msg);
        }

    }
}
