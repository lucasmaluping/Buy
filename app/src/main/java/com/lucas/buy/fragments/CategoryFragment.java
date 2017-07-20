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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.lucas.buy.R;
import com.lucas.buy.adapters.MusicListAdapter;
import com.lucas.buy.domain.Mp3Info;
import com.lucas.buy.services.AudioService;
import com.lucas.buy.utils.FindSongs;

import java.util.List;

/**
 * Created by 111 on 2017/7/7.
 */

public class CategoryFragment extends Fragment implements View.OnClickListener{
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

        Intent intent = new Intent();
        intent.setClass(this.getActivity(), AudioService.class);
        getActivity().startService(intent);
        getActivity().bindService(intent, conn, Context.BIND_AUTO_CREATE);

//        view.findViewById(R.id.top_layout_right_ImageView).                                                  //切换至我的音乐Fragment
//                setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mListener.onMyMusicFragmentInteraction(AppConstant.PlayerMsg.BACK_TO_MAIN_FRAGMENT);
//            }
//        });

        finder.setListAdpter(MyActivity.getApplicationContext(),
                mp3Infos, (ListView) view.findViewById(R.id.music_list));


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((ViewGroup) view.getParent()).removeView(view);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
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
