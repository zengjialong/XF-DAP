package com.chs.mt.xf_dap.operation;

import android.content.Context;
import android.content.Intent;

import com.chs.mt.xf_dap.datastruct.DataStruct;
import com.chs.mt.xf_dap.datastruct.Define;
import com.chs.mt.xf_dap.datastruct.MacCfg;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Administrator on 2017/12/8.
 */

public class mOKhttpUtil {
    private static OkHttpClient mOkHttpClient = new OkHttpClient();
    private static Context context = null;


    public static final MediaType FORM_CONTENT_TYPE
            = MediaType.parse("text/plain;charset=utf-8");

    //Http Get
    public static void registerErrorLogInformation( Context mcontext,String e){
        String URL="http://ip-api.com/json/?lang=zh-CN";
        //  http://ip-api.com/json/?lang=zh-CN
        context=mcontext;
        mOkHttpClient = new OkHttpClient();
        RequestBody body = RequestBody.create(FORM_CONTENT_TYPE,e);
        // System.out.println("BUG ===9090==\t");
        FormEncodingBuilder builder = new FormEncodingBuilder();
//        builder.add("AgentID","29");
//        builder.add("softtype","2");
//        builder.add("content",String.valueOf(e));


        final Request request = new Request.Builder()
                .url(URL)
                .post(builder.build())
                .build();

        try {


            //new call
            Call call = mOkHttpClient.newCall(request);
            //请求加入调度
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Intent intente = new Intent();
                    intente.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                    intente.putExtra("msg", Define.BoardCast_FlashUI_ADDRESS_ERRORDialog);
                    intente.putExtra("txtAddress", MacCfg.addressName);
                    context.sendBroadcast(intente);
                }

                @Override
                public void onResponse(final Response response) throws IOException {
                    String res = response.body().string();
                    try {
                        if(MacCfg.DATAERROR!=true) {
                            JSONObject root = new JSONObject(res);
                            MacCfg.addressName = root.getString("country") + "," + root.getString("regionName") + "," + root.getString("city");
                            Intent intentw = new Intent();
                            intentw.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                            intentw.putExtra("msg", Define.BoardCast_FlashUI_ADDRESS);
                            intentw.putExtra("txtAddress", MacCfg.addressName);
                            context.sendBroadcast(intentw);

                            Intent intente = new Intent();
                            intente.setAction("android.intent.action.CHS_Broad_FLASHUI_BroadcastReceiver");
                            intente.putExtra("msg", Define.BoardCast_FlashUI_ADDRESS_ERRORDialog);
                            intente.putExtra("txtAddress", MacCfg.addressName);
                            context.sendBroadcast(intente);
                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    //  System.out.println("BUG 当前我出现的错误为\t"+ response.message()+"\t\t"+res);

                }
            });
        }catch (Exception e1){

            e1.printStackTrace();
        }
    }


    private static void parseJson2_DSP_AI(String rootSt){
        try {
            JSONObject root = new JSONObject(rootSt);
            DataStruct.mDSPAi.code = root.getString("code");
            DataStruct.mDSPAi.message = root.getString("message");

            JSONObject objdata = root.getJSONObject("data");

            DataStruct.mDSPAi.AgentID = objdata.getString("AgentID");
            DataStruct.mDSPAi.softtype = objdata.getString("softtype");
            DataStruct.mDSPAi.macName = objdata.getString("macName");
//            DataStruct.mDSPAi.macID = objdata.getString("macID");
            DataStruct.mDSPAi.ip = objdata.getString("ip");
            DataStruct.mDSPAi.ctime = objdata.getString("ctime");

            DataStruct.mDSPAi.Ad_Status = objdata.getString("Ad_Status");
            if(DataStruct.mDSPAi.Ad_Status.equals("1")){
                DataStruct.mDSPAi.Ad_Title = objdata.getString("Ad_Title");
                DataStruct.mDSPAi.Ad_Image_Path = objdata.getString("Ad_Image_Path");
                DataStruct.mDSPAi.Ad_URL = objdata.getString("Ad_URL");
                DataStruct.mDSPAi.Ad_Close_URL = objdata.getString("Ad_Close_URL");
                DataStruct.mDSPAi.AdID = objdata.getString("AdID");
            }

            DataStruct.mDSPAi.Upgrade_Status = objdata.getString("Upgrade_Status");
            if(DataStruct.mDSPAi.Upgrade_Status.equals("1")){
                DataStruct.mDSPAi.Upgrade_Instructions = objdata.getString("Upgrade_Instructions");
                DataStruct.mDSPAi.Upgrade_Latest_Version = objdata.getString("Upgrade_Latest_Version");
                DataStruct.mDSPAi.Upgrade_URL = objdata.getString("Upgrade_URL");
            }
            //System.out.println("BUG parseJson2_DSP_AI -OK!");
            printMsg_DSPAI();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            System.out.println("BUG parseJson2_DSP_AI -JSONException");
            e.printStackTrace();
        }
    }

    private static void printMsg_DSPAI(){
        System.out.println("BUG DataStruct.mDSPAi.code="+DataStruct.mDSPAi.code);
        System.out.println("BUG DataStruct.mDSPAi.message="+DataStruct.mDSPAi.message);

        System.out.println("BUG DataStruct.mDSPAi.AgentID="+DataStruct.mDSPAi.AgentID);
        System.out.println("BUG DataStruct.mDSPAi.softtype="+DataStruct.mDSPAi.softtype);
        System.out.println("BUG DataStruct.mDSPAi.macName="+DataStruct.mDSPAi.macName);
        System.out.println("BUG DataStruct.mDSPAi.ip="+DataStruct.mDSPAi.ip);
        System.out.println("BUG DataStruct.mDSPAi.ctime="+DataStruct.mDSPAi.ctime);

        System.out.println("BUG DataStruct.mDSPAi.Ad_Status="+DataStruct.mDSPAi.Ad_Status);
        System.out.println("BUG DataStruct.mDSPAi.AdID="+DataStruct.mDSPAi.AdID);
        System.out.println("BUG DataStruct.mDSPAi.Ad_Title="+DataStruct.mDSPAi.Ad_Title);
        System.out.println("BUG DataStruct.mDSPAi.Ad_Image_Path="+DataStruct.mDSPAi.Ad_Image_Path);
        System.out.println("BUG DataStruct.mDSPAi.Ad_URL="+DataStruct.mDSPAi.Ad_URL);
        System.out.println("BUG DataStruct.mDSPAi.Ad_Close_URL="+DataStruct.mDSPAi.Ad_Close_URL);

        System.out.println("BUG DataStruct.mDSPAi.Upgrade_Status="+DataStruct.mDSPAi.Upgrade_Status);
        System.out.println("BUG DataStruct.mDSPAi.Upgrade_Instructions="+DataStruct.mDSPAi.Upgrade_Instructions);
        System.out.println("BUG DataStruct.mDSPAi.Upgrade_Latest_Version="+DataStruct.mDSPAi.Upgrade_Latest_Version);
        System.out.println("BUG DataStruct.mDSPAi.Upgrade_URL="+DataStruct.mDSPAi.Upgrade_URL);

    }

    public static void getURL(String URL){
        //创建一个Request
        System.out.println("BUG  getURL URL="+URL);
        final Request request = new Request.Builder()
                .url(URL)
                .build();
        //new call
        Call call = mOkHttpClient.newCall(request);
        //请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                //String htmlStr =  response.body().string();
                //System.out.println("BUG  getURL htmlStr="+htmlStr);
            }
        });

    }
}
