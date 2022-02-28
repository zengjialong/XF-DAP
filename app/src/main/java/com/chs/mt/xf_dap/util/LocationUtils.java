package com.chs.mt.xf_dap.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.chs.mt.xf_dap.datastruct.Define;
import com.chs.mt.xf_dap.datastruct.MacCfg;
import com.chs.mt.xf_dap.operation.mOKhttpUtil;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.ProtocolException;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.LOCATION_SERVICE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class LocationUtils {
    private static LocationManager locationManager;
    static LocationUtils locationUtils;
    private static Context mcontext;
    private static String CurrentName = "";
    private Location location = null;
    private int count = 0;
    //定义一个ConnectivityManager对象

    private ConnectivityManager mConnectivityManager = null;

    //定义一个NetworkInfo对象

    private NetworkInfo mActiveNetInfo = null;

    private String dLng;
    private String dLat;
    private static Timer timer;

    public LocationUtils() {
        this.mcontext = mcontext;
    }

    public LocationUtils(Context mcontext) {
        this.mcontext = mcontext;
    }


    public static LocationUtils getInstance() {
        if (locationUtils == null) {
            locationUtils = new LocationUtils();
        }
        return locationUtils;
    }








    @SuppressLint("MissingPermission")
    public String getLocations(Context context) {
        String strLocation = "";
        this.mcontext = context;
        String name = "";
        //获取系统的服务，


        if (ActivityCompat.checkSelfPermission(mcontext, Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mcontext, Manifest.permission.ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED
                ) {
            Toast.makeText(context, "定位权限关闭，无法获取地理位置", Toast.LENGTH_SHORT).show();

            // location = locationManager.getLastKnownLocation(provider);

            //new GetCityNameByGeocoder(context,geocoder,weatherRecycler).execute(location);
            //System.out.println("BUG 没有开定位权限");
        }
        try {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            //创建一个criteria对象
         //   System.out.println("BUG 没有开定位权限3");
            Criteria criteria = new Criteria();
            //要求低耗电
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);//低精度，如果设置为高精度，依然获取不了location。
            criteria.setAltitudeRequired(false);//不要求海拔
            criteria.setBearingRequired(false);//不要求方位
            //criteria.setCostAllowed(true);//允许有花费
            criteria.setPowerRequirement(Criteria.POWER_LOW);//低功耗
            String provider = locationManager.getBestProvider(criteria, true);

            //location = getLastKnownLocation(); // 通过GPS获取位置
         //   System.out.println("BUG 值为" + location);

            int i = 0;
//            if (location == null) {
//               while (i < 10) {
////                    i++;
//                  location = locationManager.getLastKnownLocation(provider); // 通过GPS获取位置
////                    System.out.println("BUG MMP别SHU哦" + location);
////                    //Thread.sleep(3000);
//               }
//            }
         //  System.out.println("BUG 没有开定位权限4");
//            if (location != null) {
//                System.out.println("BUG =-=-=-=值2323434343");
//       //  name = convertAddress(mcontext, location.getLatitude(), location.getLongitude());
//                mOKhttpUtil.registerErrorLogInformation(mcontext,"");
//         //       locationManager.requestLocationUpdates(provider, 20, 1, locationListener);
//              //  showLocation(location);
//               // MacCfg.addressName=name;
//               // Toast.makeText(mcontext,"location获取这里的值为"+name,Toast.LENGTH_SHORT).show();
//             //   System.out.println("BUG =-=-=-=值231213231321312323");
//
//
//            } else {
             // setIp();
                //System.out.println("BUG =-=-=-=值23123");

                mOKhttpUtil.registerErrorLogInformation(mcontext,"");
              //  System.out.println("BUG =-=-=-=值");
              //  Toast.makeText(mcontext,"API获取这里的值为"+MacCfg.addressName,Toast.LENGTH_SHORT).show();
//                Toast.makeText(context, "当前无法获取到位置", Toast.LENGTH_SHORT).show();
//                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 20, 1, locationListener);
//                System.out.println("BUG 没有开定位权限6" + locationManager.getLastKnownLocation(provider));
//                name = "";
//                MacCfg.addressName = name;
            //}
        } catch (SecurityException e) {
            System.out.println("BUG 错误了");
           // Toast.makeText(mcontext,"location这里是错误的获取这里的值为"+e,Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("BUG 错误了123");
            e.printStackTrace();
        }

        return name;

    }

    @SuppressLint("MissingPermission")
    public Location getNetWorkLocation(Context context) {
        Location location = null;
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        return location;
    }

    @SuppressLint("MissingPermission")
    private Location getLastKnownLocation() {
        locationManager = (LocationManager) mcontext.getSystemService(LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        //getProviders(true);
        //getProviders(true);
        Location bestLocation = null;
        //System.out.println("BUG 这里的值为" + providers);
        for (String provider : providers) {
            Location l =null;
          //  if (isPermissionGranted("ACCESS_FINE_LOCATION",)) {
              l= locationManager.getLastKnownLocation(provider);
            //}
         //   System.out.println("BUG 这里的值为----" + l);
            if (l != null) {
               // if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                    // Found best last known location: %s", l);
                    bestLocation = l;
                   // System.out.println("BUG 这里的值为!!!!!!" + providers);
                //}
            }
        }


        return bestLocation;
    }

    public static boolean isPermissionGranted(String permission, Context c) {
        //int res = ContextCompat.checkSelfPermission(context, permission);
        return (ContextCompat.checkSelfPermission(c, permission) == PackageManager.PERMISSION_GRANTED);
    }




    /**
     * LocationListern监听器
     * 参数：地理位置提供器、监听位置变化的时间间隔、位置变化的距离间隔、LocationListener监听器
     */
    private final LocationListener locationListener = new LocationListener() {

        public void onLocationChanged(Location location) {

//            System.out.println("BUG 只进来一次");
//            Log.v("BUG", "Provider now is disabled.." + location);
//            MacCfg.addressName = showLocation(location);
//            if(MacCfg.addressName.equals("")){
//                mOKhttpUtil.registerErrorLogInformation(mcontext,"");
//            }
        }

        public void onProviderDisabled(String provider) {
            Log.i("Tobin", "Provider now is disabled..");
            System.out.println("BUG 只进来二次");
            //   location = locationManager.getLastKnownLocation(provider); // 通过GPS获取位置
            System.out.println("BUG 只进来二次" + location);
        }

        public void onProviderEnabled(String provider) {
            System.out.println("BUG 只进来三次");
            Log.i("Tobin", "Provider now is enabled..");
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            System.out.println("BUG 只进来四次");
        }
    };

    @SuppressLint("MissingPermission")
    public String showLocation(Location location) {
        String name = CurrentName;
//        if (!dLng.equals("") && !dLat.equals("")) {
//            CurrentName = convertAddress(mcontext, Double.parseDouble(dLat), Double.parseDouble(dLng));
//        } else {
        CurrentName = convertAddress(mcontext, location.getLatitude(), location.getLongitude());
        if (location == null) {
            CurrentName = "";
            Toast.makeText(mcontext, "无法获取到位置信息", Toast.LENGTH_SHORT).show();
            setEnter();
        } else {
//            if (name.equals(CurrentName)) {
//                if (location != null) {
            MacCfg.addressName = CurrentName;
            System.out.println("BUG进入之类来就是"+MacCfg.addressName);




            locationManager.removeUpdates(locationListener);
            Intent intentw = new Intent();
            intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
            intentw.putExtra("msg", Define.BoardCast_FlashUI_ADDRESS);
            intentw.putExtra("txtAddress", CurrentName);
            mcontext.sendBroadcast(intentw);
            //    }
            //  }
        }

        return CurrentName;
    }

    public static void setEnter() {

        System.out.println("BUG 老是进入这里来");
        if (timer == null) {
            timer = new Timer();
        }
//        if(timer!=null) {
        timer.schedule(new TimerTask() {
            private int count;

            @Override
            public void run() {
                try {
                    count++;
                    Intent intentw = new Intent();
                    intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                    intentw.putExtra("msg", Define.BoardCast_FlashUI_ADDRESS);
                    intentw.putExtra("txtAddress", CurrentName);
                    mcontext.sendBroadcast(intentw);


                    System.out.println("BUG MyTimerTask 结束了！！");
                    // System.gc();
                    //   timer.cancel();
                    if (count > 2) {
                        timer.cancel();
                    }

                    //System.gc();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 20000);
        // }
    }

    public void stopTimer() {
        System.out.println("BUG 定时器停止了1111" + timer);
        if (timer != null) {
            System.out.println("BUG 定时器停止了");
            timer.cancel();
            timer = null;

        }

    }


    /**
     * @param latitude  经度
     * @param longitude 纬度
     * @return 详细位置信息 GeoCoder是基于后台backend的服务，因此这个方法不是对每台设备都适用。
     * <p>
     * (这里非常消耗手机的内存)
     */
    public static String convertAddress(Context context, double latitude, double longitude) {
        Geocoder mGeocoder = new Geocoder(context, Locale.getDefault());
        StringBuilder mStringBuilder = new StringBuilder();
        System.out.println("BUG =-=-=-=34234234234234");
        try {
            List<Address> mAddresses = mGeocoder.getFromLocation(latitude, longitude, 1);
            if (!mAddresses.isEmpty()) {
                Address address = mAddresses.get(0);

                String name = "";

                mStringBuilder.append(address.getCountryCode() + address.getCountryName()).append(", ").append(address.getAdminArea()).append(", ").append(address.getLocality());

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("BUG 当前定===位未" );
        return mStringBuilder.toString();
    }


    @SuppressLint("MissingPermission")
    private void setIp() {
        System.out.println("BUG 当前的额的进来");
        mConnectivityManager = (ConnectivityManager) mcontext.getSystemService(CONNECTIVITY_SERVICE);//获取系统的连接服务
        mActiveNetInfo = mConnectivityManager.getActiveNetworkInfo();//获取网络连接的信息
        if (mActiveNetInfo == null) {
            myDialog();
        } else{
            setUpInfo();
    }
        System.out.println("BUG 当前的额的IP"+setUpInfo());
        if(setUpInfo()!=null){
            Ip2Location(setUpInfo());
        }

    }

    private boolean checkPermission(Context context, permission permName) {
        int perm = context.checkCallingOrSelfPermission("android.permission." + permName.toString());
        return perm == PERMISSION_GRANTED;
    }

    private enum permission {
        ACCESS_COARSE_LOCATION,
        ACCESS_FINE_LOCATION
    }

    public String setUpInfo()

    {

        String IP = null;
        //如果是WIFI网络

        if (mActiveNetInfo.getType() == ConnectivityManager.TYPE_WIFI)

        {

            //  nameTextView.setText("网络类型：WIFI");

            IP = getLocalIpAddress(mcontext);

        }

        //如果是手机网络
        else if (mActiveNetInfo.getType() == ConnectivityManager.TYPE_MOBILE)

        {

//                    nameTextView.setText("网络类型：手机");
//
//                  ipTextView.setText("IP地址："+getLocalIPAddress());
            IP =getLocalIpAddress();

        } else

        {

//                   nameTextView.setText("网络类型：未知");
//
//                  ipTextView.setText("IP地址：");

        }

        System.out.println("BUG的到的I");
        return IP;

    }

    public static String getLocalIpAddress(Context context) {
        try {

            WifiManager wifiManager = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int i = wifiInfo.getIpAddress();
            System.out.println("BUG =-=-=值为"+i);
            return int2ip(i);
        } catch (Exception ex) {
            return " 获取IP出错鸟!!!!请保证是WIFI,或者请重新打开网络!\n" + ex.getMessage();
        }
        // return null;
    }

    /**
     * 将ip的整数形式转换成ip形式
     *
     * @param ipInt
     * @return
     */
    public static String int2ip(int ipInt) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipInt & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 24) & 0xFF);
        return sb.toString();
    }


    public static String getLocalIpAddress() {
          try {
                   for (Enumeration<NetworkInterface> en = NetworkInterface
                                              .getNetworkInterfaces(); en.hasMoreElements(); ) {
                           NetworkInterface intf = en.nextElement();
                          for (Enumeration<InetAddress> enumIpAddr = intf
                                                            .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                                    InetAddress inetAddress = enumIpAddr.nextElement();
                                if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                                              return inetAddress.getHostAddress().toString();
                                           }
                               }
                         }
             } catch (SocketException ex) {
                   //Log.e("WifiPreference IpAddress", ex.toString());
             }
            return null;
    }


    //显示对话框

    private void myDialog()

    {

        AlertDialog mDialog = new AlertDialog.Builder(mcontext)

                .setTitle("注意")

                .setMessage("当前网络不可用，请检查网络！")

                .setPositiveButton("确定", new DialogInterface.OnClickListener()

                {


                    @Override

                    public void onClick(DialogInterface dialog, int which)

                    {

                        // TODO Auto-generated method stub

                        //关闭对话框

                        dialog.dismiss();

                        //结束Activity


                    }

                })

                .create();//创建这个对话框

        mDialog.show();//显示这个对话框
    }

    public static JSONObject Ip2Location(String ip) {
        JSONObject jsonObject = null;

        String urlStr = "http://ip-api.com/json/" + ip + "?fields=520191&lang=en";
        try {
            URL url = new URL(urlStr);
            HttpURLConnection  urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(5000);//读取超时
            urlConnection.setConnectTimeout(5000); // 连接超时
            urlConnection.setDoInput(true);
            urlConnection.setUseCaches(false);
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                //找到服务器的情况下,可能还会找到别的网站返回html格式的数据
                InputStream is = urlConnection.getInputStream();

                BufferedReader buff = new BufferedReader(new InputStreamReader(is, "UTF-8"));//注意编码，会出现乱码
                StringBuilder builder = new StringBuilder();
                String line = null;
                while ((line = buff.readLine()) != null) {
                    builder.append(line);
                }
                buff.close();//内部会关闭InputStream
                urlConnection.disconnect();

                String res = builder.toString();

                System.out.println("BUG 获取到的值为"+res);
           //   Log.i(TAG, "Ip2Location: res -- "+res);
//                if (StringUtils.isJSONString(res)){
//                    jsonObject = new JSONObject(res);
//                }
            }


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}


