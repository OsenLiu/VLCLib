//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.videolan.libvlc.media;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.media.MediaFormat;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.MediaController;
import android.widget.MediaController.MediaPlayerControl;
import java.io.InputStream;
import java.util.Map;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;

public class VideoView extends SurfaceView implements MediaPlayerControl {
    private static LibVLC sLibVLC;

    public VideoView(Context context) {
        super(context);
    }

    public VideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public VideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initLibVLC() throws Throwable {
        sLibVLC = new LibVLC();
    }

    @TargetApi(14)
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
    }

    @TargetApi(14)
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
    }

    public int resolveAdjustedSize(int desiredSize, int measureSpec) {
        return getDefaultSize(desiredSize, measureSpec);
    }

    public void setVideoPath(String path) throws Throwable {
        this.initLibVLC();
        new Media(sLibVLC, path);
    }

    public void setVideoURI(Uri uri) throws Throwable {
        this.initLibVLC();
        new Media(sLibVLC, uri);
    }

    @TargetApi(21)
    public void setVideoURI(Uri uri, Map<String, String> headers) throws Throwable {
        this.setVideoURI(uri);
    }

    public void addSubtitleSource(InputStream is, MediaFormat format) {
    }

    public void setMediaController(MediaController controller) {
    }

    public void setOnPreparedListener(OnPreparedListener l) {
    }

    public void setOnCompletionListener(OnCompletionListener l) {
    }

    public void setOnErrorListener(OnErrorListener l) {
    }

    @TargetApi(17)
    public void setOnInfoListener(OnInfoListener l) {
    }

    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }

    public boolean onTrackballEvent(MotionEvent ev) {
        return super.onTrackballEvent(ev);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    public void start() {
    }

    public void pause() {
    }

    public void stopPlayback() {
    }

    @TargetApi(8)
    public void suspend() {
    }

    public void resume() {
    }

    public int getDuration() {
        return -1;
    }

    public int getCurrentPosition() {
        return 0;
    }

    public void seekTo(int msec) {
    }

    public boolean isPlaying() {
        return false;
    }

    public int getBufferPercentage() {
        return 0;
    }

    public boolean canPause() {
        return false;
    }

    public boolean canSeekBackward() {
        return false;
    }

    public boolean canSeekForward() {
        return false;
    }

    @TargetApi(18)
    public int getAudioSessionId() {
        return 0;
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
