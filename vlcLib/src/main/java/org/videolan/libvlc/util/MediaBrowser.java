//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.videolan.libvlc.util;

import android.net.Uri;
import java.util.ArrayList;
import java.util.Iterator;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaDiscoverer;
import org.videolan.libvlc.MediaList;
import org.videolan.libvlc.VLCObject.Event;

public class MediaBrowser {
    private static final String TAG = "LibVLC/util/MediaBrowser";
    private static final String[] DISCOVERER_LIST = (String[])(new String[]{"upnp"});
    private final LibVLC mLibVlc;
    private final ArrayList<MediaDiscoverer> mMediaDiscoverers = new ArrayList();
    private final ArrayList<Media> mDiscovererMediaArray = new ArrayList();
    private MediaList mBrowserMediaList;
    private Media mMedia;
    private MediaBrowser.EventListener mEventListener;
    private boolean mAlive;
    private final org.videolan.libvlc.VLCObject.EventListener mBrowserMediaListEventListener = new org.videolan.libvlc.VLCObject.EventListener() {
        public void onEvent(Event event) {
            if(MediaBrowser.this.mEventListener != null) {
                org.videolan.libvlc.MediaList.Event mlEvent = (org.videolan.libvlc.MediaList.Event)event;
                switch(mlEvent.type) {
                    case 512:
                        MediaBrowser.this.mEventListener.onMediaAdded(mlEvent.index, mlEvent.media);
                    case 513:
                    case 515:
                    default:
                        break;
                    case 514:
                        MediaBrowser.this.mEventListener.onMediaRemoved(mlEvent.index, mlEvent.media);
                        break;
                    case 516:
                        MediaBrowser.this.mEventListener.onBrowseEnd();
                }

            }
        }
    };
    private final org.videolan.libvlc.VLCObject.EventListener mDiscovererMediaListEventListener = new org.videolan.libvlc.VLCObject.EventListener() {
        public void onEvent(Event event) {
            if(MediaBrowser.this.mEventListener != null) {
                org.videolan.libvlc.MediaList.Event mlEvent = (org.videolan.libvlc.MediaList.Event)event;
                int index = -1;
                MediaBrowser var4;
                switch(mlEvent.type) {
                    case 512:
                        var4 = MediaBrowser.this;
                        synchronized(MediaBrowser.this) {
                            boolean found = false;
                            Iterator var6 = MediaBrowser.this.mDiscovererMediaArray.iterator();

                            while(true) {
                                if(var6.hasNext()) {
                                    Media media = (Media)var6.next();
                                    if(!media.getMrl().equals(mlEvent.media.getMrl())) {
                                        continue;
                                    }

                                    found = true;
                                }

                                if(!found) {
                                    MediaBrowser.this.mDiscovererMediaArray.add(mlEvent.media);
                                    index = MediaBrowser.this.mDiscovererMediaArray.size() - 1;
                                }
                                break;
                            }
                        }

                        if(index != -1) {
                            MediaBrowser.this.mEventListener.onMediaAdded(index, mlEvent.media);
                        }
                    case 513:
                    case 515:
                    default:
                        break;
                    case 514:
                        var4 = MediaBrowser.this;
                        synchronized(MediaBrowser.this) {
                            index = MediaBrowser.this.mDiscovererMediaArray.indexOf(mlEvent.media);
                            if(index != -1) {
                                MediaBrowser.this.mDiscovererMediaArray.remove(index);
                            }
                        }

                        if(index != -1) {
                            MediaBrowser.this.mEventListener.onMediaRemoved(index, mlEvent.media);
                        }
                        break;
                    case 516:
                        MediaBrowser.this.mEventListener.onBrowseEnd();
                }

            }
        }
    };

    public MediaBrowser(LibVLC libvlc, MediaBrowser.EventListener listener) {
        this.mLibVlc = libvlc;
        this.mLibVlc.retain();
        this.mEventListener = listener;
        this.mAlive = true;
    }

    private synchronized void reset() {
        Iterator var1 = this.mMediaDiscoverers.iterator();

        while(var1.hasNext()) {
            MediaDiscoverer md = (MediaDiscoverer)var1.next();
            md.release();
        }

        this.mMediaDiscoverers.clear();
        this.mDiscovererMediaArray.clear();
        if(this.mMedia != null) {
            this.mMedia.release();
            this.mMedia = null;
        }

        if(this.mBrowserMediaList != null) {
            this.mBrowserMediaList.release();
            this.mBrowserMediaList = null;
        }

    }

    public synchronized void release() {
        this.reset();
        if(!this.mAlive) {
            throw new IllegalStateException("MediaBrowser released more than one time");
        } else {
            this.mLibVlc.release();
            this.mAlive = false;
        }
    }

    public synchronized void changeEventListener(MediaBrowser.EventListener eventListener) {
        this.reset();
        this.mEventListener = eventListener;
    }

    private void startMediaDiscoverer(String discovererName) {
        MediaDiscoverer md = new MediaDiscoverer(this.mLibVlc, discovererName);
        this.mMediaDiscoverers.add(md);
        MediaList ml = md.getMediaList();
        ml.setEventListener(this.mDiscovererMediaListEventListener);
        ml.release();
        md.start();
    }

    public synchronized void discoverNetworkShares() {
        this.reset();
        String[] var1 = DISCOVERER_LIST;
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            String discovererName = var1[var3];
            this.startMediaDiscoverer(discovererName);
        }

    }

    public synchronized void discoverNetworkShares(String discovererName) {
        this.reset();
        this.startMediaDiscoverer(discovererName);
    }

    public synchronized void browse(String path) {
        Media media = new Media(this.mLibVlc, path);
        this.browse(media);
        media.release();
    }

    public synchronized void browse(Uri uri) {
        Media media = new Media(this.mLibVlc, uri);
        this.browse(media);
        media.release();
    }

    public synchronized void browse(Media media) {
        media.retain();
        this.reset();
        this.mBrowserMediaList = media.subItems();
        this.mBrowserMediaList.setEventListener(this.mBrowserMediaListEventListener);
        media.parseAsync(1);
        this.mMedia = media;
    }

    public synchronized int getMediaCount() {
        return this.mBrowserMediaList != null?this.mBrowserMediaList.getCount():this.mDiscovererMediaArray.size();
    }

    public synchronized Media getMediaAt(int index) {
        if(index >= 0 && index < this.getMediaCount()) {
            Media media = this.mBrowserMediaList != null?this.mBrowserMediaList.getMediaAt(index):(Media)this.mDiscovererMediaArray.get(index);
            media.retain();
            return media;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public interface EventListener {
        void onMediaAdded(int var1, Media var2);

        void onMediaRemoved(int var1, Media var2);

        void onBrowseEnd();
    }
}
