package com.twirling.SDTL.player;
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
import android.util.Log;

import com.orhanobut.logger.Logger;
import com.twirling.audio.AudioProcess;

import java.io.IOException;
import java.nio.ByteBuffer;

public class OpenMXPlayer implements Runnable {
    public final String LOG_TAG = "OpenMXPlayer";
    private static final int FRAME_LENGTH = 512;
    private int profileId = 11;
    private MediaExtractor extractor;
    private MediaCodec codec;
    private AudioTrack audioTrack;
    private PlayerStates state = new PlayerStates();
    private String sourcePath = null;
    private int sourceRawResId = -1;
    private Context mContext;
    private boolean stop = false;
    private AudioProcess audioProcess = null;

    private SurroundAudio daa = null;
    String mime = null;
    int sampleRate = -1,
            channels = -1,
            bitrate = -1;
    public long presentationTimeUs = 0,
            duration = 0;

    public SurroundAudio getDaa() {
        return daa;
    }

    public OpenMXPlayer() {
        audioProcess = new AudioProcess();
        daa = new SurroundAudio(audioProcess);
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
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
        if (extractor == null) {
            return;
        }
        extractor.seekTo(pos, MediaExtractor.SEEK_TO_CLOSEST_SYNC);
        syncNotify();
    }

    public void seek(float percent) {
        long pos = (long) (percent * duration / 100);
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
        // extractor gets infor
        // mation about the stream
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
            return;
        }
        // Read track header
        readTrackHeader();
        //
        audioProcess.Init(profileId, FRAME_LENGTH, channels, sampleRate);
        audioProcess.Set(false, 0, false, false, 1.0f);
        // configure AudioTrack
        int minSize = AudioTrack.getMinBufferSize(sampleRate,
                AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT);
        //
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate,
                AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT,
                minSize,
                AudioTrack.MODE_STREAM);
        Log.w(LOG_TAG, "audioTrack " + sampleRate + " " + minSize);
        // start playing, we will feed the AudioTrack later
        audioTrack.play();
        extractor.selectTrack(0);
        //
        startDecoding();
    }


    private void readTrackHeader() {
        MediaFormat format = null;
        int count = -1,
                buffSize = -1,
                profile = -1,
                adt = -1,
                mask = -1,
                level = -1;
        String channelCount = "";
        try {
            count = extractor.getTrackCount();
            format = extractor.getTrackFormat(0);
            mime = format.getString(MediaFormat.KEY_MIME);
//            mime = "audio/x-wav";
            sampleRate = format.getInteger(MediaFormat.KEY_SAMPLE_RATE);
            channels = format.getInteger(MediaFormat.KEY_CHANNEL_COUNT);
            // if duration is 0, we are probably playing a live stream
            duration = format.getLong(MediaFormat.KEY_DURATION);
            buffSize = format.getInteger(MediaFormat.KEY_MAX_INPUT_SIZE);
            bitrate = format.getInteger(MediaFormat.KEY_BIT_RATE);
//            format.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, 65541);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Reading format parameters exception:" + e.getMessage());
        }
        try {
            adt = format.getInteger(MediaFormat.KEY_IS_ADTS);
        } catch (Exception e) {
            Log.e(LOG_TAG, "adt exception:" + e.getMessage());
        }
        try {
            profile = format.getInteger(MediaFormat.KEY_AAC_PROFILE);
        } catch (Exception e) {
            Log.e(LOG_TAG, "profile exception:" + e.getMessage());
        }
        try {
            channelCount = format.getString(MediaFormat.KEY_AAC_MAX_OUTPUT_CHANNEL_COUNT);
        } catch (Exception e) {
            Log.e(LOG_TAG, "channelCount exception:" + e.getMessage());
        }
        try {
            level = format.getInteger(MediaFormat.KEY_FLAC_COMPRESSION_LEVEL);
        } catch (Exception e) {
            Log.e(LOG_TAG, "level exception:" + e.getMessage());
        }
        try {
            mask = format.getInteger(MediaFormat.KEY_CHANNEL_MASK);
        } catch (Exception e) {
            Log.e(LOG_TAG, "mask exception:" + e.getMessage());
        }
        Log.d(LOG_TAG, "Track info: mime:" + mime
                + " count:" + count
                + " sampleRate:" + sampleRate
                + " channels:" + channels
                + " duration:" + duration
                + " buffSize:" + buffSize
                + " bitrate:" + bitrate
                + " adt:" + adt
                + " profile:" + profile
                + " channelCount:" + channelCount
                + " level:" + level
                + " mask:" + mask);
        // check we have audio content we know
        if (format == null || !mime.startsWith("audio/")) {
            return;
        }

        // create the actual decoder, using the mime to select
        try {
            codec = MediaCodec.createDecoderByType(mime);
//            codec = MediaCodec.createByCodecName("OMX.google.raw.decoder");
//            codec = MediaCodec.createByCodecName("OMX.google.aac.decoder");
//            codec = MediaCodec.createByCodecName("OMX.SEC.aac.enc");
//            codec = MediaCodec.createByCodecName("OMX.qcom.audio.decoder.aac");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // check we have a valid codec instance
        if (codec == null) {
            return;
        }
        state.set(PlayerStates.READY_TO_PLAY);
        //
        codec.configure(format, null, null, 0);
        codec.start();
        //
        daa.setChannels(channels);
        // profileId
        Log.d(LOG_TAG, "AudioProcess: " + sourcePath
                + " profileId:" + profileId);
    }

    private void startDecoding() {
        // start decoding
        ByteBuffer[] codecInputBuffers = codec.getInputBuffers();
        ByteBuffer[] codecOutputBuffers = codec.getOutputBuffers();
        //
        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
        //
        final long kTimeOutUs = 1000;
        boolean sawInputEOS = false;
        boolean sawOutputEOS = false;
        int noOutputCounter = 0;
        int noOutputCounterLimit = 10;
        state.set(PlayerStates.PLAYING);
        //
        while (!sawOutputEOS && noOutputCounter < noOutputCounterLimit && !stop) {
            // pause implementation
            waitPlay();
            noOutputCounter++;
            Logger.w("noOutputCounter " + noOutputCounter + " " + info.size);
            // read a buffer before feeding it to the decoder
            readBuffer(sawInputEOS, codecInputBuffers, kTimeOutUs, info);
            // decode to PCM and push it to the AudioTrack player
            int res = codec.dequeueOutputBuffer(info, kTimeOutUs);
//            info.set(info.offset, info.size, info.presentationTimeUs, info.flags);
            Logger.e("res " + info.size + " " + res);
            //
            if (res >= 0) {

                if (info.size > 0) {
                    noOutputCounter = 0;
                }
                int outputBufIndex = res;
                ByteBuffer buf = codecOutputBuffers[outputBufIndex];
                Log.i(LOG_TAG, "buf capacity " + buf.capacity() + " info.size " + info.size);
                final byte[] chunk = new byte[info.size];
                //
                int deno = 2 * channels * FRAME_LENGTH;
                int chucksize = ((int) Math.ceil(info.size / (float) deno)) * deno;
                byte[] chunk2 = new byte[chucksize];
                //
                buf.get(chunk);
                buf.clear();
                System.arraycopy(chunk, 0, chunk2, 0, chunk.length);
                //
                Log.w(LOG_TAG, "chunksize " + chunk.length
                        + " chunk2size " + chunk2.length
                        + " diffsize " + (chunk2.length - chunk.length));
                int loopNum = chucksize / deno;
                Log.w(LOG_TAG, "loopNum " + loopNum);
                // short 双声道
                float writeSize = chucksize / channels / 2 * 2;
                float infoSize = info.size / channels / 2 * 2;
                float diff = (chucksize - chunk.length) / channels / 2 * 2;
                Log.w(LOG_TAG, "writeSize " + writeSize
                        + " infoSize " + infoSize
                        + " diff " + diff);
                // TODO
                short[] audio = daa.byte2Short(chunk2, loopNum);
                daa.setAudioPlayTime(presentationTimeUs / 1000f / 1000f);
                daa.audioProcess(audio);

                // 播放
                if (chunk.length > 0) {
                    audioTrack.write(audio, 0, (int) infoSize);
//                    audioTrack.write(chunk, 0, chunk.length);
                }
                if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                    Log.d(LOG_TAG, "end while");
                    sawOutputEOS = true;
                }
                try {
                    codec.releaseOutputBuffer(outputBufIndex, false);
                } catch (Exception e) {
                    return;
                }
            } else if (res == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                codecOutputBuffers = codec.getOutputBuffers();
                Log.d(LOG_TAG, "output buffers have changed.");
            } else if (res == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                MediaFormat oformat = codec.getOutputFormat();
                Log.d(LOG_TAG, "output format has changed to " + oformat);
            } else if (res == MediaCodec.INFO_TRY_AGAIN_LATER) {
                Log.d(LOG_TAG, "INFO_TRY_AGAIN_LATER" + res);
            } else {
                Log.d(LOG_TAG, "dequeueOutputBuffer returned " + res);
            }
        }
        clearSource();
    }

    // clear source and the other globals
    public void clearSource() {
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
        audioProcess.Release();
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

    private void readBuffer(boolean sawInputEOS, ByteBuffer[] codecInputBuffers, long kTimeOutUs, MediaCodec.BufferInfo info) {
        if (sawInputEOS) {
            return;
        }
        int inputBufIndex = codec.dequeueInputBuffer(kTimeOutUs);
        Log.d(LOG_TAG, "inputBufIndex " + inputBufIndex);
        if (inputBufIndex >= 0) {
            ByteBuffer dstBuf = codecInputBuffers[inputBufIndex];
            Log.i(LOG_TAG, "dstBuf capacity " + dstBuf.capacity() + " offset " + info.offset);
            // TODO
            int sampleSize = extractor.readSampleData(dstBuf, info.offset);
            int index = extractor.getSampleTrackIndex();
            Log.w(LOG_TAG, "sampleSize " + sampleSize
                    + " index " + index);
            //
            if (sampleSize < 0) {
                Log.d(LOG_TAG, "saw input EOS. Stopping playback");
                sawInputEOS = true;
                sampleSize = 0;
            } else {
                presentationTimeUs = extractor.getSampleTime();
                Log.v(LOG_TAG, "presentationTimeUs " + presentationTimeUs);
                int percent = (duration == 0) ? 0 : (int) (100 * presentationTimeUs / duration);
            }
            codec.queueInputBuffer(inputBufIndex,
                    0,
                    sampleSize,
                    presentationTimeUs,
                    sawInputEOS ? MediaCodec.BUFFER_FLAG_END_OF_STREAM : 0);
            if (!sawInputEOS) {
                extractor.advance();
            }
        } else {
            Log.e(LOG_TAG, "inputBufIndex " + inputBufIndex);
        }
    }

    private void samsungAdapter() {

    }
}