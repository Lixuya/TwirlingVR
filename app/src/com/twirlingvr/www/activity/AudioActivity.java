package com.twirlingvr.www.activity;

import android.os.Bundle;
import android.util.Log;

import com.twirlingvr.www.R;
import com.twirlingvr.www.player.OpenMXPlayer;
import com.twirlingvr.www.utils.Constants;

import javax.microedition.khronos.egl.EGLConfig;

/**
 * Created by 谢秋鹏 on 2016/6/8.
 */
public class AudioActivity extends GvrActivity implements GvrView.StereoRenderer {
    private float[] headView;
    //    private float[] modelPosition;
    private float[] headRotation;
    private float[] headRotationEular;
    private OpenMXPlayer openMXPlayer = null;
    private boolean toggle = false;
    private String TAG = "AudioActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_audio);
        GvrView gvrView = (GvrView) findViewById(R.id.cardboard_view);
        gvrView.setRenderer(this);
//        gvrView.setTransitionViewEnabled(true);
//        gvrView.setRestoreGLStateEnabled(false);
        setGvrView(gvrView);
        openMXPlayer = new OpenMXPlayer();
        headView = new float[16];
        headRotation = new float[4];
        headRotationEular = new float[3];
    }

    @Override
    public void onNewFrame(HeadTransform headTransform) {
        Log.i(TAG, "onNewFrame");
        headTransform.getHeadView(headView, 0);
        // Update the 3d audio engine with the most recent head rotation.
        headTransform.getQuaternion(headRotation, 0);
        headTransform.getEulerAngles(headRotationEular, 0);
        Log.i("angle", "eular = " + headRotationEular[0] + ", " + headRotationEular[1] + ", " + headRotationEular[2]);
        if (openMXPlayer != null) {
            openMXPlayer.setMetadata(headRotationEular);
        }
    }

    @Override
    public void onDrawEye(Eye eye) {
        Log.i(TAG, "onDrawEye");
    }

    @Override
    public void onFinishFrame(Viewport viewport) {
        Log.i(TAG, "onFinishFrame");
    }

    @Override
    public void onSurfaceChanged(int i, int i1) {
        Log.i(TAG, "onSurfaceChanged");
    }

    @Override
    public void onCardboardTrigger() {
        Log.i(TAG, "onCardboardTrigger");
        super.onCardboardTrigger();
    }

    @Override
    public void onSurfaceCreated(EGLConfig eglConfig) {
        Log.i(TAG, "onSurfaceCreated");
        openMXPlayer.setDataSource(Constants.URL_AAC);
        if (toggle == false) {
            openMXPlayer.play();
            toggle = true;
        } else {
            openMXPlayer.pause();
            toggle = false;
        }
    }

    @Override
    public void onRendererShutdown() {

    }
}
