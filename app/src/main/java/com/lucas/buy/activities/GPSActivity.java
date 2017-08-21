package com.lucas.buy.activities;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.GnssStatus;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.lucas.buy.R;
import com.lucas.buy.receivers.ProximityReceiver;

import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by 111 on 2017/8/14.
 */

public class GPSActivity extends BaseActivity {
    private static final String TAG = "GPSActivity";
    @BindView(R.id.gps_tv)
    TextView tv_show;
    @BindView(R.id.gps_tv_state)
    TextView tv_state;
    @BindView(R.id.gps_tv_state_two)
    TextView tv_state_two;
    @BindView(R.id.gps_tv_num)
    TextView tv_gps_num;
    @BindView(R.id.gps_satellite_info)
    TextView tv_gps_satellite_info;

    @Override
    protected void handler(Message msg) {

    }

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.gps_layout);
        ButterKnife.bind(this);
        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> allProviders = lm.getAllProviders();
        for (String p : allProviders) {
            Log.i(TAG, "...provider:" + p);
        }

        //定义固定点的经纬度，地理围栏
        double longitude = 108.295356;
        double latitude = 22.859195;
        float radius = 10;     //定义半径，米
        Intent intent = new Intent(this, ProximityReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, -1, intent, 0);//打开一个广播组件
        lm.addProximityAlert(latitude, longitude, radius, -1, pi);//-1表示永不过期

        //判断GPS是否可用
        if (!isGpsAble(lm)) {
            openGPS2();
        } else {
            Toast.makeText(this, "GPS is enable!", Toast.LENGTH_SHORT).show();
            //从GPS获取最近的定位信息
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                Toast.makeText(this, "permission is not enable!", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Toast.makeText(this, "permission is enable!", Toast.LENGTH_SHORT).show();
                Location lc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                updateShow(lc);
                //设置间隔两秒获得一次GPS定位信息
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 8, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        // 当GPS定位信息发生改变时，更新定位
                        updateShow(location);
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                        switch (status) {
                            //GPS状态为可见时
                            case LocationProvider.AVAILABLE:
                                Log.i(TAG, "当前GPS状态为可见状态");
                                tv_state.setText("当前GPS状态为可见状态");
                                break;
                            //GPS状态为服务区外时
                            case LocationProvider.OUT_OF_SERVICE:
                                Log.i(TAG, "当前GPS状态为服务区外状态");
                                tv_state.setText("当前GPS状态为服务区外状态");
                                break;
                            //GPS状态为暂停服务时
                            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                                Log.i(TAG, "当前GPS状态为暂停服务状态");
                                tv_state.setText("当前GPS状态为暂停服务状态");
                                break;
                        }
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                        // 当GPS LocationProvider可用时，更新定位
                        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    Activity#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for Activity#requestPermissions for more details.
                            return;
                        }
                        updateShow(lm.getLastKnownLocation(provider));
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                        updateShow(null);
                    }
                });

               // GnssStatus.Callback
                lm.registerGnssStatusCallback(new GnssStatus.Callback() {
                    @Override
                    public void onStarted() {
                        super.onStarted();
                        Log.i(TAG,"...onStarted()...");
                    }

                    @Override
                    public void onStopped() {
                        super.onStopped();
                        Log.i(TAG,"...onStopped()...");
                    }

                    @Override
                    public void onFirstFix(int ttffMillis) {
                        super.onFirstFix(ttffMillis);
                        Log.i(TAG,"...onFirstFix()...ttffMillis:" + ttffMillis);
                    }

                    @Override
                    public void onSatelliteStatusChanged(GnssStatus status) {
                        super.onSatelliteStatusChanged(status);
                        Log.i(TAG,"...onSatelliteStatusChanged...status:" + status.toString());
//                        switch (status) {
//                            case GnssStatus.
//                        }
                    }
                });
                //绑定监听状态,GpsStatus监听
                lm.addGpsStatusListener(new GpsStatus.Listener() {
                    @Override
                    public void onGpsStatusChanged(int event) {
                        switch (event) {
                            //第一次定位
                            case GpsStatus.GPS_EVENT_FIRST_FIX:
                                Log.i(TAG, "第一次定位");
                                tv_state_two.setText("第一次定位");
                                break;
                            //卫星状态改变
                            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                                Log.i(TAG, "卫星状态改变");
                                tv_state_two.setText("卫星状态改变");
                                //获取当前状态
                                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    Activity#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for Activity#requestPermissions for more details.
                                    return;
                                }
                                GpsStatus gpsStatus = lm.getGpsStatus(null);
                                //获取卫星颗数的默认最大值
                                int maxSatellites = gpsStatus.getMaxSatellites();
                                //创建一个迭代器保存所有卫星
                                Iterator<GpsSatellite> iters = gpsStatus.getSatellites().iterator();
                                int count = 0;



                                while (iters.hasNext() && count <= maxSatellites) {
                                    GpsSatellite s = iters.next();
                                    if(count == 0) {
                                        float azimuth = s.getAzimuth();//方位
                                        float elevation = s.getElevation();//高度
                                        int prn = s.getPrn();//噪声
                                        float snr = s.getSnr();//信噪比
                                        boolean b = s.hasAlmanac();//年历
                                        boolean b1 = s.hasEphemeris();//星历
                                        StringBuilder sb = new StringBuilder();
                                        sb.append("...azinuth方位：:" + azimuth + "\n");
                                        sb.append("...elevation高度：:" + elevation + "\n");
                                        sb.append("...prn噪声:" + prn);
                                        sb.append("...snr信噪比:" + snr + "\n");
                                        sb.append("...almanac年历：" + b);
                                        sb.append("...ephemeris星历：" + b1 + "\n");
                                        Log.i(TAG,"....info:" + count + ":..." + sb.toString());
                                        tv_gps_satellite_info.setText(sb.toString());
                                    }
                                    count++;
                                }
                                System.out.println("搜索到："+count+"颗卫星");
                                tv_gps_num.setText("搜索到："+count+"颗卫星");
                                break;
                            //定位启动
                            case GpsStatus.GPS_EVENT_STARTED:
                                Log.i(TAG, "定位启动");
                                tv_state_two.setText("定位启动");
                                break;
                            //定位结束
                            case GpsStatus.GPS_EVENT_STOPPED:
                                Log.i(TAG, "定位结束");
                                tv_state_two.setText("定位结束");
                                break;
                        }
                    };
                });
            }
        }

    }








    //定义一个更新显示的方法
    private void updateShow(Location location) {
        if (location != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("当前的位置信息：\n");
            sb.append("经度：" + location.getLongitude() + "\n");
            sb.append("纬度：" + location.getLatitude() + "\n");
            sb.append("高度：" + location.getAltitude() + "\n");
            sb.append("速度：" + location.getSpeed() + "\n");
            sb.append("方向：" + location.getBearing() + "\n");
            sb.append("定位精度：" + location.getAccuracy() + "\n");
            tv_show.setText(sb.toString());
        } else tv_show.setText("");
    }



    private boolean isGpsAble(LocationManager lm){
        return lm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)?true:false;
    }

    //打开位置信息设置页面让用户自己设置
    private void openGPS2(){
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent,0);
    }
}
