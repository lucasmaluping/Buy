package com.lucas.buy.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.lucas.buy.R;

import java.io.File;
import java.io.IOException;

/**
 * Created by 111 on 2017/7/18.
 */

public class AudioService extends Service implements MediaPlayer.OnCompletionListener {
    private static final String TAG = "AudioService";
    MediaPlayer player;
    private boolean isStop ;
    private boolean isPause = false;
    private boolean isPrepared = false;


//    private Handler handler;
    private Messenger messenger;

    private Thread seekThread;

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
        Log.i(TAG,"....onCompletion");
//        stopSelf();//结束了，则结束Service
        isStop = true;
    }

    //在这里我们需要实例化MediaPlayer对象
    public void onCreate(){
        super.onCreate();
        //我们从raw文件夹中获取一个应用自带的mp3文件
//        player = MediaPlayer.create(this, R.raw.xiaobaobei);
        player = new MediaPlayer();
        String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        Log.i(TAG,"....path:" + absolutePath);
        try {
            player.setDataSource(absolutePath + "/tencent/QQfile_recv/xiaobaobei.mp3");
//            player.prepare();
            player.prepareAsync();
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    isPrepared = true;
                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        }
        player.setOnCompletionListener(this);



    }

    /**
     * 该方法在SDK2.0才开始有的，替代原来的onStart方法
     * 返回值规定此startservice是哪种类型，粘性的还是非粘性的
     *          START_STICKY:粘性的，遇到异常停止后重新启动，并且intent=null
     *          START_NOT_STICKY:非粘性，遇到异常停止不会重启
     *          START_REDELIVER_INTENT:粘性的，重新启动，并且将Context传递的信息intent传递
     */
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.i(TAG,"...onStartCommand");
//        if(!player.isPlaying()){
//            player.start();
//        }
//        return START_STICKY;//粘性的
        messenger = (Messenger) intent.getExtras().get("handler");

        return START_NOT_STICKY;
    }

    public void start() {
        if (isPause) {
            isPause = false;
        }
        if (isStop && !isPrepared) {
            try {
                player.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        isStop = false;
        if (player != null && !player.isPlaying()) {
            player.start();
            //是否循环播放
            player.setLooping(false);
//            Log.i(TAG,"...Dur:" + player.getDuration() + "...current:" + player.getCurrentPosition());
            Message msg = new Message();
            msg.what = 1;
            msg.obj = player.getDuration();
//            handler.sendMessage(msg);
            try {
                messenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            seekThread = new Thread(new SeekBarThread());
            seekThread.start();


        }
    }

    public void pause() {
        if (player != null && player.isPlaying()) {
            player.pause();
            isPause = true;
//            try {
//                seekThread.wait();
//                isPause = true;
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
    }

    public void stop() {
        if(player!=null && player.isPlaying()) {
            player.stop();
            isStop = true;
            isPrepared = false;
//            player.reset();
        }
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
    public void haveFun(int pos){
//        if(player.isPlaying() && player.getCurrentPosition()>2500){
//            player.seekTo(player.getCurrentPosition()-2500);
//        }

        if (player.isPlaying() && player != null) {
            player.seekTo(pos);
        }

    }


    /**
     * 发送当前媒体的播放进度
     */
    private class SeekBarThread implements Runnable {

        @Override
        public void run() {
            while (player != null && isStop == false) {
                // 将SeekBar位置设置到当前播放位置
//                seekbar.setProgress(mediaPlayer.getCurrentPosition());
//                Log.i(TAG,".....current:" + player.getCurrentPosition());
                Message message = new Message();
                message.what = 0;
                message.obj = player.getCurrentPosition();
//                handler.sendMessage(message);
                try {
                    messenger.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                try {
                    // 每100毫秒更新一次位置
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

    }
}
