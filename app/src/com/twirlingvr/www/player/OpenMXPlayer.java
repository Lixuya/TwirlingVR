package com.twirlingvr.www.player;
/*
** OpenMXPlayer - Freeware audio player library for Android
** Copyright (C) 2009 - 2014 Radu Motisan, radu.motisan@gmail.com
**
** This file is a part of "OpenMXPlayer" open source library.
**
** OpenMXPlayer is free software; you can redistribute it and/or modify
** it under the terms of the GNU Lesser General Public License as published
** by the Free Software Foundation; either version 3 of the License,
** or (at your option) any later version.
**
** This program is distributed in the hope that it will be useful,
** but WITHOUT ANY WARRANTY; without even the implied warranty of
** MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
** GNU Lesser General Public License for more details.
**
** You should have received a copy of the GNU Lesser General Public License
** along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.os.Handler;
import android.util.Log;

import com.twirling.audio.AudioProcess;

import java.io.IOException;
import java.nio.ByteBuffer;

public class OpenMXPlayer implements Runnable {
    public final String LOG_TAG = "OpenMXPlayer";
    private static final int FRAME_LENGTH = 512;
    private MediaExtractor extractor;
    private MediaCodec codec;
    private AudioTrack audioTrack;
    private float[] metadataP = null;
    private PlayerEvents events = null;
    private PlayerStates state = new PlayerStates();
    private String sourcePath = null;
    private int sourceRawResId = -1;
    private Context mContext;
    private boolean stop = false;
    private AudioProcess audioProcess = null;
    Handler handler = new Handler();
    private SurroundAudio daa = null;
    String mime = null;
    int sampleRate = 0, channels = 0, bitrate = 0;
    long presentationTimeUs = 0, duration = 0;

    public void setEventsListener(PlayerEvents events) {
        this.events = events;
    }

    public OpenMXPlayer() {
        audioProcess = new AudioProcess();
        daa = new SurroundAudio(audioProcess);
    }

    public OpenMXPlayer(PlayerEvents events) {
        setEventsListener(events);
    }

    /**
     * For live streams, duration is 0
     *
     * @return
     */
    public boolean isLive() {
        return (duration == 0);
    }

    /**
     * set the data source, a file path or an url, or a file descriptor, to play encoded audio from
     *
     * @param src
     */
    public void setDataSource(String src) {
        sourcePath = src;
    }

    public void setDataSource(Context context, int resid) {
        mContext = context;
        sourceRawResId = resid;
    }

    public void play() {
        if (state.get() == PlayerStates.STOPPED) {
            stop = false;
            new Thread(this).start();
        }
        if (state.get() == PlayerStates.READY_TO_PLAY) {
            state.set(PlayerStates.PLAYING);
            syncNotify();
        }
    }

    /**
     * Call notify to control the PAUSE (waiting) state, when the state is changed
     */
    public synchronized void syncNotify() {
        notify();
    }

    public void stop() {
        stop = true;
        clearSource();
    }

    public void pause() {
        state.set(PlayerStates.READY_TO_PLAY);
    }

    public void seek(long pos) {
        Log.w("seek", pos + "");
        extractor.seekTo(pos, MediaExtractor.SEEK_TO_CLOSEST_SYNC);
    }

    public void seek(int percent) {
        long pos = percent * duration / 100;
        seek(pos);
    }


    /**
     * A pause mechanism that would block current thread when pause flag is set (READY_TO_PLAY)
     */
    public synchronized void waitPlay() {
        // if (duration == 0) return;
        while (state.get() == PlayerStates.READY_TO_PLAY) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
        // profileId
        audioProcess.Init(11, FRAME_LENGTH, 3, 44100);
        // extractor gets information about the stream
        extractor = new MediaExtractor();
        // try to set the source, this might fail
        try {
            if (sourcePath != null) {
                extractor.setDataSource(sourcePath);
            }
            if (sourceRawResId != -1) {
                AssetFileDescriptor fd = mContext.getResources().openRawResourceFd(sourceRawResId);
                extractor.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getDeclaredLength());
                fd.close();
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "exception:" + e.getMessage());
            if (events != null) handler.post(new Runnable() {
                @Override
                public void run() {
                    events.onError();
                }
            });
            return;
        }
        // Read track header
        readTrackHeader();
        //
        ByteBuffer[] codecInputBuffers = codec.getInputBuffers();
        ByteBuffer[] codecOutputBuffers = codec.getOutputBuffers();
        // configure AudioTrack
        int channelConfiguration = channels == 1 ? AudioFormat.CHANNEL_OUT_MONO : AudioFormat.CHANNEL_OUT_STEREO;
        int minSize = AudioTrack.getMinBufferSize(sampleRate, channelConfiguration, AudioFormat.ENCODING_PCM_16BIT);
        //
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, channelConfiguration,
                AudioFormat.ENCODING_PCM_16BIT, minSize, AudioTrack.MODE_STREAM);
        // start playing, we will feed the AudioTrack later
        audioTrack.play();
        //
        extractor.selectTrack(0);
        // start decoding
        startDecoding(codecInputBuffers, codecOutputBuffers);
    }

    private void readTrackHeader() {
        MediaFormat format = null;
        try {
            format = extractor.getTrackFormat(0);
            mime = format.getString(MediaFormat.KEY_MIME);
            sampleRate = format.getInteger(MediaFormat.KEY_SAMPLE_RATE);
            channels = format.getInteger(MediaFormat.KEY_CHANNEL_COUNT);
            // if duration is 0, we are probably playing a live stream
            duration = format.getLong(MediaFormat.KEY_DURATION);
            bitrate = format.getInteger(MediaFormat.KEY_BIT_RATE);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Reading format parameters exception:" + e.getMessage());
            // don't exit, tolerate this error, we'll fail later if this is critical
        }
        Log.d(LOG_TAG, "Track info: mime:" + mime + " sampleRate:" + sampleRate + " channels:" + channels + " bitrate:" + bitrate + " duration:" + duration);
        // check we have audio content we know
        if (format == null || !mime.startsWith("audio/")) {
            if (events != null) handler.post(new Runnable() {
                @Override
                public void run() {
                    events.onError();
                }
            });
            return;
        }
        // create the actual decoder, using the mime to select
        try {
            codec = MediaCodec.createDecoderByType(mime);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // check we have a valid codec instance
        if (codec == null) {
            if (events != null) handler.post(new Runnable() {
                @Override
                public void run() {
                    events.onError();
                }
            });
            return;
        }
        //state.set(PlayerStates.READY_TO_PLAY);
        if (events != null) handler.post(new Runnable() {
            @Override
            public void run() {
                events.onStart(mime, sampleRate, channels, duration);
            }
        });
        //
        codec.configure(format, null, null, 0);
        codec.start();
    }

    private void startDecoding(ByteBuffer[] codecInputBuffers, ByteBuffer[] codecOutputBuffers) {
        final long kTimeOutUs = 1000;
        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
        boolean sawInputEOS = false;
        boolean sawOutputEOS = false;
        int noOutputCounter = 0;
        int noOutputCounterLimit = 10;
        state.set(PlayerStates.PLAYING);
        while (!sawOutputEOS && noOutputCounter < noOutputCounterLimit && !stop) {
            // pause implementation
            waitPlay();
            noOutputCounter++;
            // read a buffer before feeding it to the decoder
            if (!sawInputEOS) {
                int inputBufIndex = codec.dequeueInputBuffer(kTimeOutUs);
                if (inputBufIndex >= 0) {
                    ByteBuffer dstBuf = codecInputBuffers[inputBufIndex];
                    int sampleSize = extractor.readSampleData(dstBuf, 0);
                    if (sampleSize < 0) {
                        Log.d(LOG_TAG, "saw input EOS. Stopping playback");
                        sawInputEOS = true;
                        sampleSize = 0;
                    } else {
                        presentationTimeUs = extractor.getSampleTime();
                        final int percent = (duration == 0) ? 0 : (int) (100 * presentationTimeUs / duration);
                        if (events != null) handler.post(new Runnable() {
                            @Override
                            public void run() {
                                events.onPlayUpdate(percent, presentationTimeUs / 1000, duration / 1000);
                            }
                        });
                    }
                    codec.queueInputBuffer(inputBufIndex, 0, sampleSize, presentationTimeUs, sawInputEOS ? MediaCodec.BUFFER_FLAG_END_OF_STREAM : 0);
                    if (!sawInputEOS) {
                        extractor.advance();
                    }
                } else {
                    Log.e(LOG_TAG, "inputBufIndex " + inputBufIndex);
                }
            } // !sawInputEOS
            // decode to PCM and push it to the AudioTrack player
//            decode();
            int res = codec.dequeueOutputBuffer(info, kTimeOutUs);
            if (res >= 0) {
                if (info.size > 0) noOutputCounter = 0;
                int outputBufIndex = res;
                ByteBuffer buf = codecOutputBuffers[outputBufIndex];
                final byte[] chunk = new byte[info.size];
                buf.get(chunk);
                buf.clear();
                int loopNum = chunk.length / 2 / channels / FRAME_LENGTH;
                // TODO
                short[] audio = daa.byte2Short(chunk, loopNum);
                audio = daa.convertToAtmos(audio, channels);
                // 播放
                if (chunk.length > 0) {
                    audioTrack.write(audio, 0, FRAME_LENGTH * 2 * loopNum);
//                    audioTrack.write(chunk2, 0, chunk2.length);
                    Log.d(LOG_TAG, "chunk.length." + chunk.length);
                    /*if(this.state.get() != PlayerStates.PLAYING) {
                        if (events != null) handler.post(new Runnable() { @Override public void run() { events.onPlay();  } });
            			state.set(PlayerStates.PLAYING);
                	}*/

                }
                codec.releaseOutputBuffer(outputBufIndex, false);
                if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                    Log.d(LOG_TAG, "saw output EOS.");
                    sawOutputEOS = true;
                }
            } else if (res == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                codecOutputBuffers = codec.getOutputBuffers();
                Log.d(LOG_TAG, "output buffers have changed.");
            } else if (res == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                MediaFormat oformat = codec.getOutputFormat();
                Log.d(LOG_TAG, "output format has changed to " + oformat);
            } else {
                Log.d(LOG_TAG, "dequeueOutputBuffer returned " + res);
            }
        }

        if (noOutputCounter >= noOutputCounterLimit) {
            if (events != null) handler.post(new Runnable() {
                @Override
                public void run() {
                    events.onError();
                }
            });
        } else {
            if (events != null) handler.post(new Runnable() {
                @Override
                public void run() {
                    events.onStop();
                }
            });
        }
    }

    // clear source and the other globals
    private void clearSource() {
        Log.d(LOG_TAG, "stopping...");
        if (codec != null) {
            codec.stop();
            codec.release();
            codec = null;
        }
        if (audioTrack != null) {
            audioTrack.flush();
            audioTrack.release();
            audioTrack = null;
        }
        sourcePath = null;
        sourceRawResId = -1;
        duration = 0;
        mime = null;
        sampleRate = 0;
        channels = 0;
        bitrate = 0;
        presentationTimeUs = 0;
        duration = 0;
        state.set(PlayerStates.STOPPED);
        stop = true;
    }

    public static String listCodecs() {
        String results = "";
        int numCodecs = MediaCodecList.getCodecCount();
        for (int i = 0; i < numCodecs; i++) {
            MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(i);

            // grab results and put them in a list
            String name = codecInfo.getName();
            boolean isEncoder = codecInfo.isEncoder();
            String[] types = codecInfo.getSupportedTypes();
            String typeList = "";
            for (String s : types) typeList += s + " ";
            results += (i + 1) + ". " + name + " " + typeList + "\n\n";
        }
        return results;
    }

    //
    public void setMetadata(float[] metadataP) {
        Log.w("metadata", "setMetadata");
        daa.setMetadata(metadataP);
    }
}