package com.lucas.buy.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.lucas.buy.R;

/**
 * Created by 111 on 2017/7/18.
 */

public class AudioService extends Service implements MediaPlayer.OnCompletionListener {
    MediaPlayer player;

    private final IBinder binder = new AudioBinder();
    @Override
    public IBinder onBind(Intent arg0) {
        return binder;
    }
    /**
     * 当Audio播放完的时候触发该动作
     */
    @Override
    public void onCompletion(MediaPlayer player) {
        stopSelf();//结束了，则结束Service
    }

    //在这里我们需要实例化MediaPlayer对象
    public void onCreate(){
        super.onCreate();
        //我们从raw文件夹中获取一个应用自带的mp3文件
        player = MediaPlayer.create(this, R.raw.xiaobaobei);
        player.setOnCompletionListener(this);
    }

    /**
     * 该方法在SDK2.0才开始有的，替代原来的onStart方法
     */
    public int onStartCommand(Intent intent, int flags, int startId){
        if(!player.isPlaying()){
            player.start();
        }
        return START_STICKY;
    }

    public void onDestroy(){
        //super.onDestroy();
        if(player.isPlaying()){
            player.stop();
        }
        player.release();
    }

    //为了和Activity交互，我们需要定义一个Binder对象
    public class AudioBinder extends Binder {

        //返回Service对象
        public AudioService getService(){
            return AudioService.this;
        }
    }

    //后退播放进度
    public void haveFun(){
        if(player.isPlaying() && player.getCurrentPosition()>2500){
            player.seekTo(player.getCurrentPosition()-2500);
        }
    }
}
