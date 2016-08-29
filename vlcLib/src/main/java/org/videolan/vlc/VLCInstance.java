/*****************************************************************************
 * VLCInstance.java
 *****************************************************************************
 * Copyright © 2011-2014 VLC authors and VideoLAN
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston MA 02110-1301, USA.
 *****************************************************************************/

package org.videolan.vlc;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.util.VLCUtil;

public class VLCInstance {
    public final static String TAG = "VLC/Util/VLCInstance";

    private static LibVLC sLibVLC = null;
    private static MediaPlayer sMediaPlayer = null;

    /** A set of utility functions for the VLC application */
    public synchronized static LibVLC get(Context context) throws Throwable {
        if (sLibVLC == null) {
            Thread.setDefaultUncaughtExceptionHandler(new VLCCrashHandler());

            final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
            if(!VLCUtil.hasCompatibleCPU(context)) {
                Log.e(TAG, VLCUtil.getErrorMsg());
                throw new IllegalStateException("LibVLC initialisation failed: " + VLCUtil.getErrorMsg());
            }

            sLibVLC = new LibVLC(VLCOptions.getLibOptions(pref));
            LibVLC.setOnNativeCrashListener(new LibVLC.OnNativeCrashListener() {
                @Override
                public void onNativeCrash() {
//                    Intent i = new Intent(context, NativeCrashActivity.class);
//                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    i.putExtra("PID", android.os.Process.myPid());
//                    context.startActivity(i);
                }
            });
        }
        return sLibVLC;
    }

    public static synchronized MediaPlayer getMainMediaPlayer(Context context) throws Throwable {
        if (sMediaPlayer == null) {
            if (sLibVLC == null)
                get(context);
            sMediaPlayer = new MediaPlayer(sLibVLC);
        }
        return sMediaPlayer;
    }

    public static synchronized void restart(Context context, SharedPreferences pref) throws Throwable {
        if (sLibVLC != null) {
            if (sMediaPlayer != null) {
                sMediaPlayer.release();
                sMediaPlayer = null;
            }
            sLibVLC.release();
            sLibVLC = new LibVLC(VLCOptions.getLibOptions(pref));
        }
    }

    public static synchronized void release() throws IllegalStateException {
        if (sLibVLC != null) {
            if (sMediaPlayer != null) {
                sMediaPlayer.release();
                sMediaPlayer = null;
            }
            sLibVLC.release();
            sLibVLC=null;
        }
    }

    public static synchronized boolean testCompatibleCPU(Context context) {
        if (sLibVLC == null && !VLCUtil.hasCompatibleCPU(context)) {
//            final Intent i = new Intent(context, CompatErrorActivity.class);
//            context.startActivity(i);
            return false;
        } else
            return true;
    }
}
