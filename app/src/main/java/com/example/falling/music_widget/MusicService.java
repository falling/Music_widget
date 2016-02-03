package com.example.falling.music_widget;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;


/**
 * Created by falling on 2016/2/3.
 */
public class MusicService extends Service {
    public static final String STATE = "state";
    public static final String MUSIC_NAME = "MusicName";
    int count;
    int resids[] = new int[]{R.raw.qilixiang,R.raw.talkingbody,R.raw.xianggelila};
    //歌名信息
    String[] MusicNames = new String[]{"七里香","Talking Body","香格里拉"};
    private MediaPlayer mMediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        count = 0;
        mMediaPlayer = MediaPlayer.create(this,resids[count]);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            if (TextUtils.equals(intent.getStringExtra("action"), "上一首")) {
                mMediaPlayer.stop();
                count = count - 1 < 0 ? resids.length-1 : count - 1;
                mMediaPlayer = MediaPlayer.create(this,resids[count]);
                mMediaPlayer.start();
            } else if (TextUtils.equals(intent.getStringExtra("action"), "下一首")) {
                mMediaPlayer.stop();
                count = count + 1 >= resids.length ? 0 : count + 1;
                mMediaPlayer = MediaPlayer.create(this,resids[count]);
                mMediaPlayer.start();
            } else if (TextUtils.equals(intent.getStringExtra("action"), "开始")) {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                } else {
                    mMediaPlayer.start();
                }
            }

            /**
             * 以广播的形式，将歌曲名称，和播放或者停止的状态 传回 widget。
             */
            Intent intent_back = new Intent(this, MusicWidget.class);
            intent_back.setAction(MusicWidget.SERVICE_MESSAGE);
            //歌曲名称信息
            intent_back.putExtra(MUSIC_NAME,MusicNames[count]);
            //播放状态信息
            intent_back.putExtra(STATE,isPlay());
            sendBroadcast(intent_back);
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public boolean isPlay(){
        return mMediaPlayer.isPlaying();
    }
}
