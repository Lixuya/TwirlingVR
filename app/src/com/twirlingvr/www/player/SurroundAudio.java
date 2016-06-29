package com.twirlingvr.www.player;

import com.twirling.audio.AudioProcess;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * 解决问题：立体声变为全景声，大帧转为小帧处理
 * Created by 谢秋鹏 on 2016/6/6.
 */
public class SurroundAudio {
    private static SurroundAudio ins = null;
    private static final int FRAME_LENGTH = 512;
    private int chunkSize = 0;
    private int loopNum = 0;
    private AudioProcess audioProcess = null;
    private float pitch = 0;
    private float yaw = 0;
    private int channels = 0;
    private float[] metadata = null;

    private SurroundAudio() {
    }

    public static SurroundAudio getIns(AudioProcess audioProcess) {
        if (ins == null) {
            ins = new SurroundAudio(audioProcess);
        }
        return ins;
    }

    public void setChannels(int channels) {
        this.channels = channels;
    }

    public SurroundAudio(AudioProcess audioProcess) {
        this.audioProcess = audioProcess;
    }

    //
    public short[] convertToAtmos(short[] audioFlat) {
        // TODO
        short[] audioOutputBufShort = new short[FRAME_LENGTH * 2 * loopNum];
        int n_acc = 0;
        int n_acc_out = 0;
        int ii = 0;
        if (metadata == null) {
            return audioOutputBufShort;
        }
        // 0度方向
        float[] audioInput = new float[FRAME_LENGTH * channels];
        float[] audioOutput = new float[FRAME_LENGTH * 2];
        for (int loopi = 0; loopi < loopNum; loopi++) {
            for (ii = 0; ii < FRAME_LENGTH * channels; ii++) {
                audioInput[ii] = audioFlat[n_acc++];
            }
//            Log.i("angle", "eular = " + yaw + ", " + pitch);
//            for (int i = 0; i < metadata.length; i++) {
//                Log.w("angle", metadata[i] + "");
//            }
            audioProcess.Process(yaw, pitch, audioInput, audioOutput, metadata);
            float postgain = 0.5f;
            for (ii = 0; ii < FRAME_LENGTH * 2; ii++) {
                audioOutputBufShort[n_acc_out++] = (short) (audioOutput[ii] * postgain);
            }
        }
        return audioOutputBufShort;
    }

    // byte2short
    public short[] byte2Short(byte[] chunk, int loopNum) {
        //
        chunkSize = chunk.length;
        this.loopNum = loopNum;
        ByteBuffer fulldata = ByteBuffer.allocate(chunkSize);
        fulldata.put(chunk);
        fulldata.order(ByteOrder.LITTLE_ENDIAN);

        // ok now we can create an array of shorts (16 bit data) and load it
        // we are stereo 16 bit, so each sample is 2 bytes
        short[] sounddata = new short[chunkSize / 2];

        // copy data from ByteBuffer into our short buffer. Short buffer is used
        // to load the AudioTrack object
        int totalsamples = chunkSize / 2;
        for (int counter1 = 0; counter1 < totalsamples; counter1++) {
            sounddata[counter1] = fulldata.getShort(counter1 * 2);
        }
        return sounddata;
    }

    public byte[] shortToByte(short[] shorts) {
        int datasize = shorts.length;
        ByteBuffer fulldata = ByteBuffer.allocate(datasize * 2);
        byte[] bytes = new byte[datasize * 2];
        fulldata.put(bytes);
        fulldata.order(ByteOrder.LITTLE_ENDIAN);

        for (int i = 0; i < datasize * 2; i++) {
            bytes[i] = fulldata.get(i);
        }
        return bytes;
    }

    public float[] setMetadata(float[][] metadata, float playtime) {

        int i, c, n;
        float azi, elv, r;
        // output
        int metadataLen = metadata[0].length;
        int MetadataIndex = metadataLen - 1;
        float[] metadataThisFrame = new float[MetadataIndex];
        float interplateFactor = 1.0f;
        //
//        float playtime = 0f;
//        (float) audioBufferPosition / sampleRate;
        for (i = 0; i < metadataLen; i++) {
            if (playtime <= metadata[i][0]) {
                MetadataIndex = i;
                if (i == 0)
                    interplateFactor = 1.0f;
                else {
                    interplateFactor = (playtime - metadata[i - 1][0]) /
                            (metadata[i][0] - metadata[i - 1][0]);
                }
                break;
            }
        }
        n = 0;
        //channel objectNum
        for (c = 0; c < channels; c++) {
            //metadata二维数组
            if (MetadataIndex == 0) {
                r = metadata[MetadataIndex][1 + c * 3];
                //r = 1; //for test.
                azi = (float) (metadata[MetadataIndex][2 + c * 3] * Math.PI / 180.0f);
                elv = (float) (metadata[MetadataIndex][3 + c * 3] * Math.PI / 180.0f);
            } else {
                r = (metadata[MetadataIndex][1 + c * 3] *
                        interplateFactor + metadata[MetadataIndex - 1][1 + c * 3] *
                        (1 - interplateFactor));
                //r = 1; //for test.
                azi = (float) ((metadata[MetadataIndex][2 + c * 3] * interplateFactor +
                        metadata[MetadataIndex - 1][2 + c * 3] * (1 - interplateFactor)) * Math.PI / 180.0f);
                elv = (float) ((metadata[MetadataIndex][3 + c * 3] * interplateFactor +
                        metadata[MetadataIndex - 1][3 + c * 3] * (1 - interplateFactor)) * Math.PI / 180.0f);
            }
            //metadata output
            metadataThisFrame[n++] = r;
            metadataThisFrame[n++] = azi;
            metadataThisFrame[n++] = elv;
        }
        this.metadata = metadataThisFrame;
        return metadataThisFrame;
    }

    public void setGyroscope(float[] gyroscope) {
        yaw = -gyroscope[1] + (float) Math.PI;
        pitch = gyroscope[0];
    }

}
