package com.lucas.buy.fragments;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lucas.buy.R;
import com.lucas.buy.activities.AnimActivity;
import com.lucas.buy.activities.BroadCaseTestActivity;
import com.lucas.buy.activities.EditLineActivity;
import com.lucas.buy.activities.OtherActivity;
import com.lucas.buy.activities.RecyclerActivity;
import com.lucas.buy.activities.SelfActivity;
import com.lucas.buy.activities.SelfRecyclerActiviy;
import com.lucas.buy.activities.VoiceActivity;
import com.lucas.buy.activities.WakeUpActivity;
import com.lucas.buy.adapters.MusicListAdapter;
import com.lucas.buy.domain.Mp3Info;
import com.lucas.buy.interfaces.TestCallBack;
import com.lucas.buy.services.AudioService;
import com.lucas.buy.utils.FindSongs;
import com.lucas.buy.utils.VolleyUtil;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 111 on 2017/7/7.
 */

public class CategoryFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "CategoryFragment";
    private View view;

    private FindSongs finder;                                              //查找歌曲的类的实例
    private Activity MyActivity;
    private List<Mp3Info> mp3Infos;
    private MusicListAdapter musicListAdapter;
    private OnFragmentInteractionListener mListener;

    private AudioService audioService;

    //使用ServiceConnection来监听Service状态的变化
    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            audioService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            //这里我们实例化audioService,通过binder来实现
            audioService = ((AudioService.AudioBinder)binder).getService();

        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyActivity = getActivity();
        finder = new FindSongs();
        mp3Infos = finder.getMp3Infos(MyActivity.getContentResolver());
        musicListAdapter = new MusicListAdapter(MyActivity.getApplicationContext(), mp3Infos);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        finder = new FindSongs();
        view = inflater.inflate(R.layout.category_layout, container, false);
        ButterKnife.bind(this, view);

        initView(view, savedInstanceState);

        Intent intent = new Intent();
        intent.setClass(this.getActivity(), AudioService.class);
        getActivity().startService(intent);
        getActivity().bindService(intent, conn, Context.BIND_AUTO_CREATE);


//        finder.setListAdpter(MyActivity.getApplicationContext(),
//                mp3Infos, (ListView) view.findViewById(R.id.music_list));


        return view;
    }

    private void initView(View view, Bundle savedInstanceState) {
        Button btn = (Button) view.findViewById(R.id.btn_go);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), OtherActivity.class);
                startActivity(intent);
            }
        });

        view.findViewById(R.id.btn_anim).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AnimActivity.class);
                startActivity(intent);
            }
        });

        view.findViewById(R.id.stop_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                audioService.stop();
            }
        });

        view.findViewById(R.id.to_recycler).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RecyclerActivity.class);
                startActivity(intent);
            }
        });

        Button b = (Button)view.findViewById(R.id.to_self);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SelfActivity.class);
                startActivity(intent);
            }
        });
        b.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.i(TAG,".....action:" + motionEvent.getAction() + "...." + motionEvent.toString());
//                return true;//返回true，说明事件已经被onTouch消费掉了
                return false;
            }
        });

        final TextView tv = (TextView)view.findViewById(R.id.show_info);

        TestCallBack testCallBack = new TestCallBack() {
            @Override
            public void success(String info) {
                tv.setText(info);
            }

            @Override
            public void error() {

            }
        };
        VolleyUtil.getInstance().setTestListener(testCallBack);
        VolleyUtil.getInstance().getUserInfo("lucas");

        view.findViewById(R.id.to_my).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), com.lucas.buy.activities.MyActivity.class);
                startActivity(intent);
            }
        });

        view.findViewById(R.id.to_self_recycler).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SelfRecyclerActiviy.class);
                startActivity(intent);
            }
        });

        view.findViewById(R.id.to_edit_line).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditLineActivity.class);
                startActivity(intent);
            }
        });

        view.findViewById(R.id.to_broadcast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BroadCaseTestActivity.class);
                startActivity(intent);
            }
        });

        view.findViewById(R.id.to_voice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), VoiceActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((ViewGroup) view.getParent()).removeView(view);

    }


    @Override @OnClick(R.id.to_voice_wakeup)
    public void onClick(View v) {
        int id = v.getId();

        Intent intent = new Intent(getActivity(), WakeUpActivity.class);
        startActivity(intent);




//        Intent intent = new Intent();
//        intent.setClass(this.getActivity(), AudioService.class);
//        if(id == R.id.btn_start){
//            //启动Service，然后绑定该Service，这样我们可以在同时销毁该Activity，看看歌曲是否还在播放
//            getActivity().startService(intent);
//            getActivity().bindService(intent, conn, Context.BIND_AUTO_CREATE);
////            finish();
//        }else if(id == R.id.btn_end){
//            //结束Service
//            getActivity().unbindService(conn);
//            getActivity().stopService(intent);
////            finish();
//        }
//        else if(id == R.id.btn_fun){
//            audioService.haveFun();
//        }
    }

    public interface OnFragmentInteractionListener {
        public void onMyMusicFragmentInteraction(int msg);

        public void onMyMusicFragmentInteraction(int msg, int position);      //这个方法要在MainActivity中再次重写一遍</span><span style="color:#222222;">
    }


}
