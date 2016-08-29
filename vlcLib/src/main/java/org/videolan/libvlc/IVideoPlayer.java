//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.videolan.libvlc;

import android.view.Surface;

public interface IVideoPlayer {
    void setSurfaceLayout(int var1, int var2, int var3, int var4, int var5, int var6);

    int configureSurface(Surface var1, int var2, int var3, int var4);

    void eventHardwareAccelerationError();
}
