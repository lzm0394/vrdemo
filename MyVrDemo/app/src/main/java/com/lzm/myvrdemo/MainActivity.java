package com.lzm.myvrdemo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;

import com.google.vr.sdk.widgets.video.VrVideoEventListener;
import com.google.vr.sdk.widgets.video.VrVideoView;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private VrVideoView vrVideoView;
    private MyTask task;
    private SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vrVideoView = (VrVideoView) findViewById(R.id.vr_video_view);
        vrVideoView.setInfoButtonEnabled(false);
        seekBar = (SeekBar) findViewById(R.id.seek_bar);

        vrVideoView.setDisplayMode(VrVideoView.DisplayMode.FULLSCREEN_STEREO);

        initEvent();
        task = new MyTask();
        task.execute();


    }

    boolean isPlaying = false;

    private void initEvent() {

        vrVideoView.setEventListener(new VrVideoEventListener() {

            @Override
            public void onLoadSuccess() {
                long duration = vrVideoView.getDuration();
                seekBar.setMax((int) duration);
            }

            @Override
            public void onClick() {
                if (isPlaying) {
                    vrVideoView.pauseVideo();
                } else {
                    vrVideoView.playVideo();
                }
                isPlaying = !isPlaying;

            }

            @Override
            public void onNewFrame() {

                long currentPosition = vrVideoView.getCurrentPosition();
                seekBar.setProgress((int) currentPosition);
            }

            @Override
            public void onCompletion() {

                isPlaying = true;
                seekBar.setProgress(0);
                vrVideoView.seekTo(0);
                //停止视频
                vrVideoView.pauseVideo();

            }
        });


    }


    class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            VrVideoView.Options options = new VrVideoView.Options();
//            options.inputFormat = VrVideoView.Options.FORMAT_HLS;
//            options.inputType = VrVideoView.Options.FORMAT_DEFAULT;

            options.inputFormat = VrVideoView.Options.FORMAT_DEFAULT;
            options.inputType = VrVideoView.Options.FORMAT_DEFAULT;
            try {
                vrVideoView.loadVideoFromAsset("1459173141_37_3840HD.mp4", options);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        vrVideoView.shutdown();
        if (task != null) {
            if (task.isCancelled()) {
                task.cancel(true);
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        vrVideoView.pauseRendering();
    }

    @Override
    protected void onResume() {
        super.onResume();
        vrVideoView.resumeRendering();
    }
}
