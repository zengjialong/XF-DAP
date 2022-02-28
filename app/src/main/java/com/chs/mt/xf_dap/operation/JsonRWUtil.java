package com.chs.mt.xf_dap.operation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.chs.mt.xf_dap.datastruct.DataStruct;
import com.chs.mt.xf_dap.datastruct.Define;
import com.chs.mt.xf_dap.datastruct.MacCfg;
import com.chs.mt.xf_dap.encrypt.SeffFileCipherUtil;
import com.chs.mt.xf_dap.filemanger.common.FileUtil;
import com.chs.mt.xf_dap.bean.JsonDataSt;
import com.chs.mt.xf_dap.bean.CarBrands;
import com.chs.mt.xf_dap.bean.CarTypes;
import com.chs.mt.xf_dap.bean.Company;
import com.chs.mt.xf_dap.bean.DSP_Data;
import com.chs.mt.xf_dap.bean.DSP_DataInfo;
import com.chs.mt.xf_dap.bean.DSP_MACData;
import com.chs.mt.xf_dap.bean.DSP_MusicData;
import com.chs.mt.xf_dap.bean.DSP_OutputData;
import com.chs.mt.xf_dap.bean.DSP_SingleData;
import com.chs.mt.xf_dap.bean.MacTypes;
import com.chs.mt.xf_dap.bean.MacsAgentName;
import com.chs.mt.xf_dap.bean.SEFF_File;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;

public class JsonRWUtil {

	public  static String getAppInfo(Context context) {
 		try {
 			String pkName = context.getPackageName();
 			return pkName;
 		} catch (Exception e) {
 			
 		}
 		return null;
 	}

    public static void checkAndAddDB(Context mContext,String filePath){
    	//加到数据库存中
    	JsonRWUtil jsonRWOpt = null;
    	jsonRWOpt = new JsonRWUtil();
    	DSP_SingleData mDSP_SData=new DSP_SingleData();	    	
    	mDSP_SData = jsonRWOpt.LoadJsonLocal2DSP_DataInfo(mContext,filePath);
    	DSP_DataInfo dataInfo = mDSP_SData.Get_data_info();     
		SEFF_File seff_file = DataStruct.dbSEfFile_Table.find("file_path", filePath);
		int dot=filePath.lastIndexOf("/");
		String name=filePath.substring(dot+1);   
        //Log.e("test#",name);   
        name =name.substring(0, name.length()-Define.CHS_SEff_TYPE_L);  
		if(seff_file == null){
			DataStruct.dbSEfFile_Table.insert(new SEFF_File(
        		"file_id",//file_id
        		mDSP_SData.Get_fileType(),//file_type
        		name,//file_name
        		filePath,//file_path
				"0",//file_favorite
				"0",//file_love
				"200",//file_size
				mDSP_SData.Get_data_info().Get_data_upload_time(),//file_time
				"file_msg",//file_msg
				
				dataInfo.Get_data_user_name(),//data_user_name
				dataInfo.Get_data_machine_type(),//data_machine_type
				dataInfo.Get_data_car_type(),//data_car_type
				dataInfo.Get_data_car_brand(),//data_car_brand
				dataInfo.Get_data_group_name(),//data_group_name
				dataInfo.Get_data_upload_time(),//data_upload_time
				dataInfo.Get_data_eff_briefing(),//data_eff_briefing
				
				"0",//list_sel
				"0"//list_is_open
			));
		}
    }
    /**
     * 

     */
    public static boolean SaveSingleJson2Local(
    		Context context,
    		String fineName,
    		DSP_SingleData sdata
    	) {
    	boolean res = false;
		File cache = new File(Environment.getExternalStorageDirectory(),  MacCfg.AgentNAME);
		File destDir = new File(cache.toString()+
				"/"+MacCfg.Mac+
				"/"+"SoundEff"
				);
		if (!destDir.exists()) {
			try {
				destDir.mkdirs();
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("BUG 创建没有"+destDir.exists());
		}

		String filePath = destDir.toString()+        		
        		"/"+fineName+Define.CHS_SEff_TYPE;

        Gson gson = new Gson();
		try {
			FileWriter fw = new FileWriter(filePath);
		    PrintWriter out = new PrintWriter(fw);
		    out.write(gson.toJson(sdata));
		    out.println();
		    fw.close();
		    out.close();
		    //加密
		    if(Define.SEFFFILE_Encrypt){
		    	res = SeffFileCipherUtil.encrypt(filePath);
		    	checkAndAddDB(context,filePath);
		    }else{	
		    	res = true;
		    }		    
		} catch (Exception e) {
			res = false;
			e.printStackTrace();
		}
		return res;      
    }
    
    public static boolean SaveMACJson2Local(
    		Context context,
    		String fineName,
    		DSP_MACData sdata
    	) {
    	boolean res = false;
		File cache = new File(Environment.getExternalStorageDirectory(),  MacCfg.AgentNAME);
		File destDir = new File(cache.toString()+
				"/"+MacCfg.Mac+
				"/"+"SoundEff"
				);
		if (!destDir.exists()) {
			destDir.mkdirs();
		}

		String filePath = destDir.toString()+        		
        		"/"+fineName+Define.CHS_SEff_MAC_TYPE;
        System.out.println("BUG  filePath write:" + filePath);
        
        Gson gson = new Gson();
		try {
			FileWriter fw = new FileWriter(filePath);
		    PrintWriter out = new PrintWriter(fw);
		    out.write(gson.toJson(sdata));
		    out.println();
		    fw.close();
		    out.close();
		    //加密
		    if(Define.SEFFFILE_Encrypt){		    	
		    	res = SeffFileCipherUtil.encrypt(filePath);
		    	checkAndAddDB(context,filePath);
		    }else{	
		    	res = true;
		    }		    
		} catch (Exception e) {
			res = false;
			e.printStackTrace();
		}
		return res;      
    }
    

    //把单组文件转化为数据结构
    public static DSP_SingleData LoadJsonLocal2DataStruce(Context context,String filePathLocalString) {
    	DSP_SingleData sData=new DSP_SingleData();
    	boolean res = false;
    	int[] initsystem = new int[Define.IN_LEN];

		System.out.println("BUG loadMacDataJson2DataStruce:"+filePathLocalString);
		try {
    		 //解密
    		 if(Define.SEFFFILE_Encrypt){
    			 res = SeffFileCipherUtil.decrypt(filePathLocalString);
    			 if(!res){
    				 sData=null;
    				 return sData;
    			 }
    			 filePathLocalString+=Define.CIPHER_TEXT_SUFFIX;
    			 
    		 }
    		 System.out.println("BUG res:"+res); 
    		 System.out.println("BUG filePathLocalString:"+filePathLocalString);  
    		 InputStream in = new FileInputStream(filePathLocalString);        
             InputStreamReader is = new InputStreamReader(in, "UTF-8");  
             BufferedReader br = new BufferedReader(is);  
             String line;  
             StringBuilder builder = new StringBuilder();  
             while((line=br.readLine())!=null){  
                 builder.append(line);  
             }  
             is.close();
             br.close();
             
             //删除临时文件
             if(Define.SEFFFILE_Encrypt){
            	 File dir = new File(filePathLocalString);
     			if (dir.isFile()) {
     				dir.delete();
     			} else {
     				FileUtil.deleteDir(dir);
     			}
             }
             
             
 			JSONObject root = new JSONObject(builder.toString());  
 			System.out.println("BUG fileType:"+root.getString("fileType"));  
 			//CHS
 			JSONObject objchs = root.getJSONObject("chs");
 			sData.Set_chs(new Company(
				objchs.getString("company_name"),
				objchs.getString("company_tel"),
				objchs.getString("company_contacts"),
				objchs.getString("company_web"),
				objchs.getString("company_weixin"),
				objchs.getString("company_qq"),
				objchs.getString("company_briefing_en"),
				objchs.getString("company_briefing_zh"),
				objchs.getString("company_brand")
				));
 			//client
 			JSONObject objclient = root.getJSONObject("client");
 			sData.Set_client(new Company(
				objclient.getString("company_name"),
				objclient.getString("company_tel"),
				objclient.getString("company_contacts"),
				objclient.getString("company_web"),
				objclient.getString("company_weixin"),
				objclient.getString("company_qq"),
				objclient.getString("company_briefing_en"),
				objclient.getString("company_briefing_zh"),
				objclient.getString("company_brand")
				));
 			//client
 			JSONObject objdata_info = root.getJSONObject("data_info");
 			sData.Set_data_info(new DSP_DataInfo(
				objdata_info.getString("data_user_name"),
				objdata_info.getString("data_user_tel"),
				objdata_info.getString("data_user_mailbox"),
				objdata_info.getString("data_user_info"),
				objdata_info.getString("data_machine_type"),
				objdata_info.getString("data_car_type"),
				objdata_info.getString("data_car_brand"),
				objdata_info.getString("data_json_version"),
				objdata_info.getString("data_mcu_version"),
				objdata_info.getString("data_android_version"),
				objdata_info.getString("data_ios_version"),
				objdata_info.getString("data_pc_version"),
				objdata_info.getString("data_group_num"),
				objdata_info.getString("data_group_name"),
				objdata_info.getString("data_eff_briefing"),
				objdata_info.getString("data_upload_time"),
				objdata_info.getInt("data_encryption_byte"),
				objdata_info.getInt("data_encryption_bool"),
				objdata_info.getInt("data_head_data")
				));			 			
 			//
 			DSP_Data dsp_Data = new DSP_Data();			
 			JSONObject objdata = root.getJSONObject("data");
 			//用户组名字
 			if(sData.Get_data_info().Get_data_json_version().equals("CHS-JSON_V1.00")){ 	
 				int[] initgroup_name = new int[16];
 				JSONArray Arraygroup_name = objdata.getJSONArray("group_name"); 
 				int len = Arraygroup_name.length();
 				if(len > 16){
 					len = 16;
 				}
 				if(len>0){ 				
 	 				for(int i=0;i<len;i++){  	
 	 					initgroup_name[i] = Arraygroup_name.getInt(i)&0xff;			
 	 	 			}  
 	 			}
 	 			dsp_Data.Set_group_name(initgroup_name);
 			}
 			//Music
 			DSP_MusicData musicData = new DSP_MusicData();
 			
 			JSONObject objmusic = objdata.getJSONObject("music");
 			JSONArray arraymusic = objmusic.getJSONArray("music");  
 			int[] initDatamusic = new int[Define.IN_LEN];
 			System.out.println("BUG seff arraymusic.length():"+arraymusic.length());
 			int mch = arraymusic.length();
 			if(mch > Define.MAX_CH){
 				mch = Define.MAX_CH;
 			}
 			if(mch>0){ 		
 				for(int i=0;i<mch;i++){  					
 					JSONArray lan = arraymusic.getJSONArray(i);  
 					System.out.println("BUG seff arraymusic.[].length():"+lan.length());
 					int cnt = lan.length();
 					if(cnt > Define.IN_LEN){
 						cnt = Define.IN_LEN;
 					}					
 					for(int j=0;j<cnt;j++){
 						initDatamusic[j]=lan.getInt(j)&0xff;
 					} 
 	 				musicData.SetMusicData(i, initDatamusic);
 	 			}  
 			}
 			
 			dsp_Data.Set_DSP_MusicData(musicData);
 			//Output
 			DSP_OutputData outputData = new DSP_OutputData();
 			
 			JSONObject objoutput = objdata.getJSONObject("output");
 			JSONArray arrayoutput = objoutput.getJSONArray("output");  
 			int[][] initDataoutput = new int[Define.MAX_CH][Define.OUT_LEN];
 			System.out.println("BUG seff arrayoutput.length():"+arrayoutput.length());
			 int och = arrayoutput.length();
			 if(och > Define.MAX_CH){
				 och = Define.MAX_CH;
			 }
			 if(och>0){
				 for(int i=0;i<och;i++){
 					JSONArray lan = arrayoutput.getJSONArray(i);  
 					System.out.println("BUG seff arrayoutput[?].length():"+lan.length());
					 int cnt = lan.length();
					 if(cnt > Define.OUT_LEN){
						 cnt = Define.OUT_LEN;
					 }
					 for(int j=0;j<cnt;j++){
						 initDataoutput[i][j]=lan.getInt(j)&0xff;
					 }
 	 			}  
 			}
 			
 			for(int i=0;i<och;i++){
 				outputData.SetOutputData(i, initDataoutput[i]);
 			}			
 			dsp_Data.Set_DSP_OutputData(outputData);

 			//System
 			//Music
// 			DSP_SystemData systemData = new DSP_SystemData();
//
// 			JSONObject objsystem = objdata.getJSONObject("system");
// 			JSONArray arraypc_source_set = objsystem.getJSONArray("pc_source_set");
// 			JSONArray arraysystem_data = objsystem.getJSONArray("system_data");
// 			JSONArray arraysystem_spk_type = objsystem.getJSONArray("system_spk_type");
// 			JSONArray arrayout_led = objsystem.getJSONArray("out_led");
// 			JSONArray arraysound_delay_field = objsystem.getJSONArray("sound_delay_field");
// 			JSONArray arraysystem_group_name = objsystem.getJSONArray("system_group_name");
// 			JSONArray arrayeff_group_name = objsystem.getJSONArray("eff_group_name");
// 			JSONArray arraycur_password_data = objsystem.getJSONArray("cur_password_data");
//
//
// 			//pc_source_set
// 			if(arraypc_source_set.length()>0){
// 				for(int i=0;i<arraypc_source_set.length();i++){
// 					initsystem[i]=arraypc_source_set.getInt(i)&0xff;
// 	 			}
// 			}
// 			systemData.Set_pc_source_set(initsystem);
// 			//system_data
// 			if(arraysystem_data.length()>0){
// 				for(int i=0;i<arraysystem_data.length();i++){
// 					initsystem[i]=arraysystem_data.getInt(i)&0xff;
// 	 			}
// 			}
// 			systemData.Set_system_data(initsystem);
// 			//system_spk_type
// 			if(arraysystem_spk_type.length()>0){
// 				for(int i=0;i<arraysystem_spk_type.length();i++){
// 					initsystem[i]=arraysystem_spk_type.getInt(i)&0xff;
// 	 			}
// 			}
// 			systemData.Set_system_spk_type(initsystem);
// 			//out_led
// 			if(arrayout_led.length()>0){
// 				for(int i=0;i<arrayout_led.length();i++){
// 					initsystem[i]=arrayout_led.getInt(i)&0xff;
// 	 			}
// 			}
// 			systemData.Set_out_led(initsystem);
// 			//sound_delay_field
// 			if(arraysound_delay_field.length()>0){
// 				for(int i=0;i<arraysound_delay_field.length();i++){
// 					initsystem[i]=arraysound_delay_field.getInt(i)&0xff;
// 	 			}
// 			}
// 			systemData.Set_sound_delay_field(initsystem);
// 			//system_group_name
// 			if(arraysystem_group_name.length()>0){
// 				for(int i=0;i<arraysystem_group_name.length();i++){
// 					initsystem[i]=arraysystem_group_name.getInt(i)&0xff;
// 	 			}
// 			}
// 			systemData.Set_system_group_name(initsystem);
// 			//eff_group_name
// 			if(arrayeff_group_name.length()>0){
// 				for(int i=0;i<arrayeff_group_name.length();i++){
// 					initsystem[i]=arrayeff_group_name.getInt(i)&0xff;
// 	 			}
// 			}
// 			systemData.Set_eff_group_name(initsystem);
// 			//cur_password_data
// 			if(arraycur_password_data.length()>0){
// 				for(int i=0;i<arraycur_password_data.length();i++){
// 					initsystem[i]=arraycur_password_data.getInt(i)&0xff;
// 	 			}
// 			}
// 			systemData.Set_cur_password_data(initsystem);
//
// 			dsp_Data.Set_SystemData(systemData);
 			sData.Set_data(dsp_Data);
 			
 			builder=null;
 			System.out.println("BUG DSP_SingleData LoadJsonLocal2DataStruce OK !!!"+filePathLocalString);     
         } catch (UnsupportedEncodingException e) {  
        	 sData=null;
         	 System.out.println("BUG DSP_SingleData LoadJsonLocal2DataStruce  UnsupportedEncodingException"+filePathLocalString);  
             e.printStackTrace();  
         } catch (IOException e) {  
        	 sData=null;
         	 System.out.println("BUG DSP_SingleData LoadJsonLocal2DataStruce  IOException"+filePathLocalString);  
             e.printStackTrace();  
         }catch (JSONException e) {  
        	 sData=null; 
         	 System.out.println("BUG DSP_SingleData LoadJsonLocal2DataStruce  JSONException"+filePathLocalString);   
             e.printStackTrace();  
         }
    	return sData;
    }
    
    public static DSP_SingleData LoadJsonLocal2DSP_DataInfo(Context context,String filePathLocalString) {
    	DSP_SingleData sData=new DSP_SingleData();
    	 try {  
    		 if(Define.SEFFFILE_Encrypt){
    			 boolean res = SeffFileCipherUtil.decrypt(filePathLocalString);
    			 if(!res){
    				 return null;
    			 }
    			 filePathLocalString+=Define.CIPHER_TEXT_SUFFIX;
    			 
    		 }
    		 
    		 InputStream in = new FileInputStream(filePathLocalString);	        
             InputStreamReader is = new InputStreamReader(in, "UTF-8");  
             BufferedReader br = new BufferedReader(is);  
             String line;  
             StringBuilder builder = new StringBuilder();  
             while((line=br.readLine())!=null){  
                 builder.append(line);  
             }  
             is.close();
             br.close();
             
             //删除临时文件
             if(Define.SEFFFILE_Encrypt){
            	 File dir = new File(filePathLocalString);
     			if (dir.isFile()) {
     				dir.delete();
     			} else {
     				FileUtil.deleteDir(dir);
     			}
             }
              
 			JSONObject root = new JSONObject(builder.toString());  
 			
 			sData.Set_fileType(root.getString("fileType"));
 			//System.out.println("VOT fileType:"+root.getString("fileType"));  
 			
 			//data_info
 			JSONObject objdata_info = root.getJSONObject("data_info");
 			sData.Set_data_info(new DSP_DataInfo(
				objdata_info.getString("data_user_name"),
				objdata_info.getString("data_user_tel"),
				objdata_info.getString("data_user_mailbox"),
				objdata_info.getString("data_user_info"),
				objdata_info.getString("data_machine_type"),
				objdata_info.getString("data_car_type"),
				objdata_info.getString("data_car_brand"),
				objdata_info.getString("data_json_version"),
				objdata_info.getString("data_mcu_version"),
				objdata_info.getString("data_android_version"),
				objdata_info.getString("data_ios_version"),
				objdata_info.getString("data_pc_version"),
				objdata_info.getString("data_group_num"),
				objdata_info.getString("data_group_name"),
				objdata_info.getString("data_eff_briefing"),
				objdata_info.getString("data_upload_time"),
				objdata_info.getInt("data_encryption_byte"),
				objdata_info.getInt("data_encryption_bool"),
				objdata_info.getInt("data_head_data")
				));			 
 			builder=null;
			System.out.println("BUG DataInfo OK !!!"+filePathLocalString);    
         } catch (UnsupportedEncodingException e) {  
        	 sData=null;
         	 System.out.println("BUG DataInfo UnsupportedEncodingException"+filePathLocalString);    
             e.printStackTrace();  
         } catch (IOException e) {  
        	 sData=null;
         	 System.out.println("BUG DataInfo IOException"+filePathLocalString);   
             e.printStackTrace();  
         }catch (JSONException e) {  
        	 sData=null; 
         	 System.out.println("BUG DataInfo JSONException"+filePathLocalString);    
             e.printStackTrace();  
         }
    	 
    	return sData;
    }
    
    /*
     * Get sound effects File type
     * return 0:error,1:single File, 2:MacType
     */
    public static int getSEFFFileType(Context context,String filePathLocalString) {
    	int ret = 0;
    	 try {  
    		 if(Define.SEFFFILE_Encrypt){
    			 boolean res = SeffFileCipherUtil.decrypt(filePathLocalString);
    			 if(!res){
    				 return ret;
    			 }
    			 filePathLocalString+=Define.CIPHER_TEXT_SUFFIX;    			 
    		 }
    		 
    		 InputStream in = new FileInputStream(filePathLocalString);	        
             InputStreamReader is = new InputStreamReader(in, "UTF-8");  
             BufferedReader br = new BufferedReader(is);  
             String line;  
             StringBuilder builder = new StringBuilder();  
             while((line=br.readLine())!=null){  
                 builder.append(line);  
             }  
             is.close();
             br.close();
             
             //删除临时文件
             if(Define.SEFFFILE_Encrypt){
            	 File dir = new File(filePathLocalString);
     			if (dir.isFile()) {
     				dir.delete();
     			} else {
     				FileUtil.deleteDir(dir);
     			}
             }
              
 			JSONObject root = new JSONObject(builder.toString());  
 			
 			if(root.getString("fileType").equals(JsonDataSt.DSP_Single)){
 				return 1;
 			}else if(root.getString("fileType").equals(JsonDataSt.DSP_Complete)){
 				return 2;
 			}
 
 			builder=null;
			System.out.println("BUG DataInfo OK !!!"+filePathLocalString);    
			ret = 0;
         } catch (UnsupportedEncodingException e) {  
        	 ret = 0;
         	 System.out.println("BUG DataInfo UnsupportedEncodingException"+filePathLocalString);    
             e.printStackTrace();  
         } catch (IOException e) {  
        	 ret = 0;
         	 System.out.println("BUG DataInfo IOException"+filePathLocalString);   
             e.printStackTrace();  
         }catch (JSONException e) {  
        	 ret = 0;
         	 System.out.println("BUG DataInfo JSONException"+filePathLocalString);    
             e.printStackTrace();  
         }
    	 
    	 return ret;
    }

    //把整机文件转化为结构体保存
    public static DSP_MACData loadMacDataJson2DataStruce(Context context,String filePathLocalString) {

//    	boolean res = false;
//    	int[] temp = new int[DataStruct.OUT_LEN];
    	DSP_MACData MAC_Data = new DSP_MACData();
    	System.out.println("BUG loadMacDataJson2DataStruce:"+filePathLocalString);  
    	 try { 
    		 //解密
    		 if(Define.SEFFFILE_Encrypt){
    			 boolean res = SeffFileCipherUtil.decrypt(filePathLocalString);
    			 if(!res){
    				 return null;
    			 }
    			 filePathLocalString+=Define.CIPHER_TEXT_SUFFIX;
    			 
    		 }
    		 
    		 InputStream in = new FileInputStream(filePathLocalString);        
             InputStreamReader is = new InputStreamReader(in, "UTF-8");  
             BufferedReader br = new BufferedReader(is);  
             String line;  
             StringBuilder builder = new StringBuilder();  
             while((line=br.readLine())!=null){  
                 builder.append(line);  
             }  
             is.close();
             br.close();             
             
             //删除临时文件
             if(Define.SEFFFILE_Encrypt){
            	 File dir = new File(filePathLocalString);
     			if (dir.isFile()) {
     				dir.delete();
     			} else {
     				FileUtil.deleteDir(dir);
     			}
             }
             
             JSONObject root = new JSONObject(builder.toString());  
             System.out.println("BUG fileType:"+root.getString("fileType"));  
 			 //CHS
 			 JSONObject objchs = root.getJSONObject("chs");
 			 MAC_Data.Set_chs(new Company(
				objchs.getString("company_name"),
				objchs.getString("company_tel"),
				objchs.getString("company_contacts"),
				objchs.getString("company_web"),
				objchs.getString("company_weixin"),
				objchs.getString("company_qq"),
				objchs.getString("company_briefing_en"),
				objchs.getString("company_briefing_zh"),
				objchs.getString("company_brand")
				));
 			 //client
 			 JSONObject objclient = root.getJSONObject("client");
 			 MAC_Data.Set_client(new Company(
				objclient.getString("company_name"),
				objclient.getString("company_tel"),
				objclient.getString("company_contacts"),
				objclient.getString("company_web"),
				objclient.getString("company_weixin"),
				objclient.getString("company_qq"),
				objclient.getString("company_briefing_en"),
				objclient.getString("company_briefing_zh"),
				objclient.getString("company_brand")
				));
 			 //client
 			 JSONObject objdata_info = root.getJSONObject("data_info");
 			 MAC_Data.Set_data_info(new DSP_DataInfo(
				objdata_info.getString("data_user_name"),
				objdata_info.getString("data_user_tel"),
				objdata_info.getString("data_user_mailbox"),
				objdata_info.getString("data_user_info"),
				objdata_info.getString("data_machine_type"),
				objdata_info.getString("data_car_type"),
				objdata_info.getString("data_car_brand"),
				objdata_info.getString("data_json_version"),
				objdata_info.getString("data_mcu_version"),
				objdata_info.getString("data_android_version"),
				objdata_info.getString("data_ios_version"),
				objdata_info.getString("data_pc_version"),
				objdata_info.getString("data_group_num"),
				objdata_info.getString("data_group_name"),
				objdata_info.getString("data_eff_briefing"),
				objdata_info.getString("data_upload_time"),
				objdata_info.getInt("data_encryption_byte"),
				objdata_info.getInt("data_encryption_bool"),
				objdata_info.getInt("data_head_data")
				));			 			
 			//system
 			JSONObject objsystem = root.getJSONObject("system");
 			JSONArray arraypc_source_set = objsystem.getJSONArray("pc_source_set"); 
 			JSONArray arraysystem_data = objsystem.getJSONArray("system_data"); 
 			JSONArray arraysystem_spk_type = objsystem.getJSONArray("system_spk_type"); 
 			JSONArray arrayout_led = objsystem.getJSONArray("out_led"); 
 			JSONArray arraysound_delay_field = objsystem.getJSONArray("sound_delay_field"); 
 			JSONArray arraysystem_group_name = objsystem.getJSONArray("system_group_name"); 
 			JSONArray arrayeff_group_name = objsystem.getJSONArray("eff_group_name"); 
 			JSONArray arraycur_password_data = objsystem.getJSONArray("cur_password_data"); 
 			
 			//DSP_SystemData systemData = new DSP_SystemData();			
 			//pc_source_set
 			if(arraypc_source_set.length()>0){ 				
 				for(int i=0;i<arraypc_source_set.length();i++){  					
 					MAC_Data.system.pc_source_set[i]
 					= arraypc_source_set.getInt(i)&0xff;			
 	 			}  
 			}
 			//system_data
 			if(arraysystem_data.length()>0){ 				
 				for(int i=0;i<arraysystem_data.length();i++){  					
 					MAC_Data.system.system_data[i]
 							=arraysystem_data.getInt(i)&0xff;			
 	 			}  
 			}

 			//system_spk_type
 			if(arraysystem_spk_type.length()>0){ 				
 				for(int i=0;i<arraysystem_spk_type.length();i++){  					
 					MAC_Data.system.system_spk_type[i]
 							=arraysystem_spk_type.getInt(i)&0xff;			
 	 			}  
 			}

 			//out_led
 			if(arrayout_led.length()>0){ 				
 				for(int i=0;i<arrayout_led.length();i++){  					
 					MAC_Data.system.out_led[i]
 							=arrayout_led.getInt(i)&0xff;			
 	 			}  
 			}

 			//sound_delay_field
 			if(arraysound_delay_field.length()>0){ 				
 				for(int i=0;i<arraysound_delay_field.length();i++){  					
 					MAC_Data.system.sound_delay_field[i]
 							=arraysound_delay_field.getInt(i)&0xff;			
 	 			}  
 			}
 
 			//system_group_name
 			if(arraysystem_group_name.length()>0){ 				
 				for(int i=0;i<arraysystem_group_name.length();i++){  					
 					MAC_Data.system.system_group_name[i]
 							=arraysystem_group_name.getInt(i)&0xff;			
 	 			}  
 			}

 			//eff_group_name
 			if(arrayeff_group_name.length()>0){ 				
 				for(int i=0;i<arrayeff_group_name.length();i++){  					
 					MAC_Data.system.eff_group_name[i]
 							=arrayeff_group_name.getInt(i)&0xff;			
 	 			}  
 			}

 			//cur_password_data
 			if(arraycur_password_data.length()>0){ 				
 				for(int i=0;i<arraycur_password_data.length();i++){  					
 					MAC_Data.system.cur_password_data[i]
 							=arraycur_password_data.getInt(i)&0xff;			
 	 			}  
 			}
 			
 			//解释Data数据 暂时7组，0为当前组
 			JSONArray obj_data_Array = root.getJSONArray("data");
// 			DSP_DataMac mDSP_DataMac = new DSP_DataMac();
// 			DSP_MusicData musicData = new DSP_MusicData();	 
// 			DSP_OutputData outputData = new DSP_OutputData();	 
 			JSONObject data = null;
 			JSONArray Arraygroup_name = null;
 			JSONObject Object_music = null;
 			JSONObject Object_output = null;
 			JSONArray Array_music = null;
 			JSONArray Array_output = null;
 			
 			int len = 0;
 			int lenD = 0;
 			
 			int groupMax=obj_data_Array.length();
 			if(groupMax > (Define.MAX_GROUP+1)){
 				groupMax = (Define.MAX_GROUP+1);
 			}
 			if(groupMax > 0){ 				
 				for(int i = 0; i < groupMax; i++){  	
 					data = obj_data_Array.getJSONObject(i);  
 					if(data != null){
 						//获取用户组名字
 	 					Arraygroup_name = data.getJSONArray("group_name"); 
 	 					if(Arraygroup_name != null){
 	 						len = Arraygroup_name.length();
 	 						System.out.println("BUG MACSEFF Arraygroup_name.length()="+Arraygroup_name.length());
// 	 						System.out.println("BUG loadMacDataJson2DataStruce Arraygroup_name="+Arraygroup_name.toString());     
 	 	 	 				if(len > 16){
 	 	 	 					len = 16;
 	 	 	 				}
 	 	 	 				if(len>0){ 				
 	 	 	 	 				for(int j=0;j<len;j++){  	
 	 	 	 	 					MAC_Data.data[i].group_name[j] 
 	 	 	 	 						= Arraygroup_name.getInt(j)&0xff;			
 	 	 	 	 	 			}
 	 
 	 	 	 	 			}	
 	 					} 	 	 				 				
 	 	 				
 	 	 				//获取MUSIC 	 	 			
 	 					Object_music = data.getJSONObject("music"); 
 	 	 	 			if(Object_music != null){	 
 	 	 	 				Array_music = Object_music.getJSONArray("music");  	 	 	 				
	 	 	 	 			len = Array_music.length();
							System.out.println("BUG MACSEFF Array_music.length()="+Array_music.length());
							if(len > Define.MAX_CH){
	 	 	 					len = Define.MAX_CH;
	 	 	 				}
	 	 	 				for(int j=0;j<len;j++){  		
	 	 	 					JSONArray musicArray = Array_music.getJSONArray(j);
	 	 	 					lenD = musicArray.length();
								System.out.println("BUG MACSEFF Array_music.musicArray.length()="+musicArray.length());
	 	 	 	 				if(lenD > Define.IN_LEN){
	 	 	 	 					lenD = Define.IN_LEN;
	 	 	 	 				}
	 	 	 	 				for(int k=0;k<lenD;k++){
	 	 	 	 					MAC_Data.data[i].music.music[j][k]=musicArray.getInt(k)&0xff;
	 		 	 				}
	 	
	 	 	 				}
 	 	 	 			}
 	 	 	 			
 	 	 				
 	 	 				//获取Output 
 	 	 	 			String st="ss{";
 	 	 	 			Object_output = data.getJSONObject("output"); 
 	 	 	 			if(Object_output != null){
 	 	 	 				Array_output = Object_output.getJSONArray("output");  	 	 
	 	 	 	 			lenD = 0;
	 	 	 	 			len = Array_output.length();
							System.out.println("BUG MACSEFF Array_output.length()="+Array_output.length());
	 	 	 				if(len > Define.MAX_CH){
	 	 	 					len = Define.MAX_CH;
	 	 	 				}

	 	 	 				for(int j=0;j<len;j++){
	 	 	 					JSONArray outputArray = Array_output.getJSONArray(j);
	 	 	 					lenD = outputArray.length();
								System.out.println("BUG MACSEFF Array_output.outputArray.length()="+outputArray.length());
								if(lenD > Define.OUT_LEN){
	 	 	 	 					lenD = Define.OUT_LEN;
	 	 	 	 				}
//	 	 	 	 				st="ss{";
	 	 	 	 				for(int k=0;k<lenD;k++){
	 	 	 	 					MAC_Data.data[i].output.output[j][k] = outputArray.getInt(k)&0xff;
//	 	 	 	 					st+=(String.valueOf(MAC_Data.data[i].output.output[j][k])+",");
	 		 	 				}	 	 	
	 	 	 	 				//System.out.println("BUG loadMacDataJson2DataStruce st="+st);   
	 	 	 	 				//System.out.println("BUG loadMacDataJson2DataStruce output i="+i);  
	 	 	 				}
 	 	 	 			} 	 	 	 			
 					} 					
 				}				
 			}
 			//printDatad(MAC_Data);
 			//printData();
 			//res = true;
 			System.out.println("BUG loadMacDataJson2DataStruce OK !!!"+filePathLocalString);     
    	 } catch (UnsupportedEncodingException e) {  
        	 //res = false;
    		 MAC_Data = null;
         	 System.out.println("BUG loadMacDataJson2DataStruce  UnsupportedEncodingException"+filePathLocalString);  
             e.printStackTrace();  
         } catch (IOException e) {  
        	 //res = false;
        	 MAC_Data = null;
         	 System.out.println("BUG loadMacDataJson2DataStruce  IOException"+filePathLocalString);  
             e.printStackTrace();  
         }catch (JSONException e) {  
        	 //res = false;
        	 MAC_Data = null;
         	 System.out.println("BUG loadMacDataJson2DataStruce  JSONException"+filePathLocalString);   
             e.printStackTrace();  
         }
    	 return MAC_Data;
    }
    
    private void printData(){
    	String st="ss{";
    	for(int i=0;i<=Define.MAX_GROUP;i++){
    		System.out.println("BUG loadMacDataJson2DataStruce GGGGGG Group="+i);   
    		for(int j=0;j<Define.MAX_CH;j++){
    			System.out.println("BUG loadMacDataJson2DataStruce CCCCC channel="+j); 
    			st="ss{";
    			for(int k=0;k<Define.OUT_LEN;k++){
    				st+=(String.valueOf(DataStruct.RcvDeviceData.MAC_Data.data[i].output.output[j][k])+",");
        		}
    			System.out.println("BUG loadMacDataJson2DataStruce st="+st);
    		}
    	}
    }
    private void printDatad(DSP_MACData MAC_Data){
    	String st="ss{";
    	for(int i=0;i<=Define.MAX_GROUP;i++){
    		System.out.println("BUG loadMacDataJson2DataStruce GGGGGG Group="+i);   
    		for(int j=0;j<Define.MAX_CH;j++){
    			System.out.println("BUG loadMacDataJson2DataStruce CCCCC channel="+j); 
    			st="ss{";
    			for(int k=0;k<Define.OUT_LEN;k++){
    				st+=(String.valueOf(MAC_Data.data[i].output.output[j][k])+",");
        		}
    			System.out.println("BUG loadMacDataJson2DataStruce st="+st);
    		}
    	}
    }
    //打开目录的音效文件
    public void OpenSoundSEFFDir(Context context, String fileType) {
    	String filePath = Environment.getExternalStorageDirectory()+
			"/"+MacCfg.AgentNAME+
			"/"+MacCfg.Mac+
			"/"+"SoundEff"+
			"/"+"Single"+
			"/"+getAppInfo(context)+fileType+String.valueOf(1)+Define.CHS_SEff_TYPE;//
    	Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(filePath)), "text/*");
		context.startActivity(intent);
	}
    
    //分享音效文件，固定目录，参数音效文件名字,fileType 0:单纯组文件，1：整机文件
    public static void ShareSoundEFFData(Context context, String finename,int fileType) {
    	String filePath="";
    	
    	if(fileType == 0){
    		filePath = Environment.getExternalStorageDirectory()+
				"/"+MacCfg.AgentNAME+
				"/"+MacCfg.Mac+
				"/"+"SoundEff"+
				"/"+finename+Define.CHS_SEff_TYPE;//
	    	
    	}else if(fileType == 1){
    		filePath = Environment.getExternalStorageDirectory()+
				"/"+MacCfg.AgentNAME+
				"/"+MacCfg.Mac+
				"/"+"SoundEff"+
				"/"+finename+Define.CHS_SEff_MAC_TYPE;//
    	}
    	Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction("android.intent.action.SEND");  
		intent.setType("application/*");  //message audio video image application  java               
//		intent.putExtra(Intent.EXTRA_TEXT,"TEXT");
		//intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filePath)));
        intent.putExtra(Intent.EXTRA_STREAM,
                FileProvider.getUriForFile(context,
                        context.getApplicationContext().getPackageName() + ".provider", new File(filePath)));
        context.startActivity(intent);


    }
    
	//分享音效文件，参数音效文件路径
    public void ShareLocalSoundEFFData(Context context, String filePath) {
    	Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction("android.intent.action.SEND");  
		intent.setType("application/*");  //message audio video image application  java               
//		intent.putExtra(Intent.EXTRA_TEXT,"TEXT");
        intent.putExtra(Intent.EXTRA_STREAM,
                FileProvider.getUriForFile(context,
                        context.getApplicationContext().getPackageName() + ".provider", new File(filePath)));
		context.startActivity(intent);
	}

    public void parseVerderOptionJson(Context context){  
        try {  
            InputStreamReader is = new InputStreamReader(context.getAssets().open("VerderOptionV9.json"), "UTF-8");  
            BufferedReader br = new BufferedReader(is);  
            String line;  
            StringBuilder builder = new StringBuilder();  
            while((line=br.readLine())!=null){  
                builder.append(line);  
            }  
            is.close();
            br.close();
            
			JSONObject root = new JSONObject(builder.toString());  
			//System.out.println("VOT code:"+root.getString("code"));  
			//System.out.println("VOT message:"+root.getString("message"));  
			
			JSONObject objdata = root.getJSONObject("data");
			//System.out.println("VOT types:"+objdata.getString("types"));  
			
			JSONArray arraybrands = objdata.getJSONArray("brands");  
			JSONArray arraycartpyes = objdata.getJSONArray("cartpyes");  
			JSONArray arraymacs = objdata.getJSONArray("macs");  
			JSONArray arraymacsAgentName = objdata.getJSONArray("macsAgentName");  
			
			//System.out.println("VOT arraybrands.length():"+arraybrands.length());  
			//System.out.println("VOT arraycartpyes.length():"+arraycartpyes.length());  
			//System.out.println("VOT arraymacs.length():"+arraymacs.length()); 
			//List_CarBrands
			for(int i=0;i<arraybrands.length();i++){  
				JSONObject lan = arraybrands.getJSONObject(i);  
//				DataStruct.venderOption.List_CarBrands.add(new CarBrands(
//					lan.getString(CarBrands.T_CID),
//					lan.getString(CarBrands.T_NAME),
//					lan.getString(CarBrands.T_IMG_PATH)
//				));
				
				DataStruct.dbCarBrands_Table.insert(new CarBrands(
					lan.getString(CarBrands.T_CID),
					lan.getString(CarBrands.T_NAME),
					lan.getString(CarBrands.T_IMG_PATH)
    			));
			}  
			//arraycartpyes
			for(int i=0;i<arraycartpyes.length();i++){  
				JSONObject lan = arraycartpyes.getJSONObject(i);  
//				DataStruct.venderOption.List_CarTypes.add(new CarTypes(
//					lan.getString(CarTypes.T_CID),
//					lan.getString(CarTypes.T_BRAND_ID),
//					lan.getString(CarTypes.T_NAME),
//					lan.getString(CarTypes.T_IMG_PATH)
//				));
				
				DataStruct.dbCarTypes_Table.insert(new CarTypes(
					lan.getString(CarTypes.T_CID),
					lan.getString(CarTypes.T_BRAND_ID),
					lan.getString(CarTypes.T_NAME),
					lan.getString(CarTypes.T_IMG_PATH)
    			));
			} 
			
			//arraymacs
			for(int i=0;i<arraymacs.length();i++){  
				JSONObject lan = arraymacs.getJSONObject(i);  
//				DataStruct.venderOption.List_MacTypes.add(new MacTypes(
//					lan.getString(MacTypes.T_CID),
//					lan.getString(MacTypes.T_NAME)
//				));
				DataStruct.dbMacTypes_Table.insert(new MacTypes(
					lan.getString(MacTypes.T_CID),
					lan.getString(MacTypes.T_NAME)
    			));
			} 
			
			//macsAgentName
			for(int i=0;i<arraymacsAgentName.length();i++){  
				JSONObject lan = arraymacsAgentName.getJSONObject(i);  
				DataStruct.dbMacsAgentName_Table.insert(new MacsAgentName(
					lan.getString(MacsAgentName.T_CID),
					lan.getString(MacsAgentName.T_MID),
					lan.getString(MacsAgentName.T_AgentID),
					lan.getString(MacsAgentName.T_CNAME)
    			));
			} 
			
			System.out.println("VOT Update VerderOption OK !!!");  
//			System.out.println("VOT DataStruct.venderOption.List_CarBrands.size():"
//					+DataStruct.venderOption.List_CarBrands.size());  
//			System.out.println("VOT DataStruct.venderOption.List_CarTypes.size():"
//					+DataStruct.venderOption.List_CarTypes.size());  
//			System.out.println("VOT DataStruct.venderOption.List_MacTypes.size():"
//					+DataStruct.venderOption.List_MacTypes.size());  

        } catch (UnsupportedEncodingException e) {  
            // TODO Auto-generated catch block  
        	//System.out.println("VOT UnsupportedEncodingException");  
            e.printStackTrace();  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
        	//System.out.println("VOT IOException");  
            e.printStackTrace();  
        }catch (JSONException e) {  
            // TODO Auto-generated catch block  
        	//System.out.println("VOT JSONException");  
            e.printStackTrace();  
        }  
    }
    public static void parseLocalVerderOptionJson(Context context){
    	String appName=getAppInfo(context);
		File file = new File(Environment.getExternalStorageDirectory(),
				MacCfg.AgentNAME+"/"+MacCfg.Mac+"/"+DataStruct.venderOption.Get_lastversion()+".json");
		System.out.println("BUG  chs_file.toString"+file.toString());

        try {  
        	InputStreamReader is = new InputStreamReader(   
                    new FileInputStream(file), "UTF-8");  

            BufferedReader br = new BufferedReader(is);  
            String line;  
            StringBuilder builder = new StringBuilder();  
            while((line=br.readLine())!=null){  
                builder.append(line);  
            }  
            is.close();
            br.close();
            
			JSONObject root = new JSONObject(builder.toString());  
			//System.out.println("VOT code:"+root.getString("code"));  
			//System.out.println("VOT message:"+root.getString("message"));  
			
			JSONObject objdata = root.getJSONObject("data");
			//System.out.println("VOT types:"+objdata.getString("types"));  
			
			JSONArray arraybrands = objdata.getJSONArray("brands");  
			JSONArray arraycartpyes = objdata.getJSONArray("cartpyes");  
			JSONArray arraymacs = objdata.getJSONArray("macs");  
			JSONArray arraymacsAgentName = objdata.getJSONArray("macsAgentName");  
			
			//System.out.println("VOT arraybrands.length():"+arraybrands.length());  
			//System.out.println("VOT arraycartpyes.length():"+arraycartpyes.length());  
			//System.out.println("VOT arraymacs.length():"+arraymacs.length()); 
			//List_CarBrands
			for(int i=0;i<arraybrands.length();i++){  
				JSONObject lan = arraybrands.getJSONObject(i);  
//				DataStruct.venderOption.List_CarBrands.add(new CarBrands(
//					lan.getString(CarBrands.T_CID),
//					lan.getString(CarBrands.T_NAME),
//					lan.getString(CarBrands.T_IMG_PATH)
//				));
				
				DataStruct.dbCarBrands_Table.insert(new CarBrands(
					lan.getString(CarBrands.T_CID),
					lan.getString(CarBrands.T_NAME),
					lan.getString(CarBrands.T_IMG_PATH)
    			));
			}  
			//arraycartpyes
			for(int i=0;i<arraycartpyes.length();i++){  
				JSONObject lan = arraycartpyes.getJSONObject(i);  
//				DataStruct.venderOption.List_CarTypes.add(new CarTypes(
//					lan.getString(CarTypes.T_CID),
//					lan.getString(CarTypes.T_BRAND_ID),
//					lan.getString(CarTypes.T_NAME),
//					lan.getString(CarTypes.T_IMG_PATH)
//				));
				
				DataStruct.dbCarTypes_Table.insert(new CarTypes(
					lan.getString(CarTypes.T_CID),
					lan.getString(CarTypes.T_BRAND_ID),
					lan.getString(CarTypes.T_NAME),
					lan.getString(CarTypes.T_IMG_PATH)
    			));
			} 
			
			//arraymacs
			for(int i=0;i<arraymacs.length();i++){  
				JSONObject lan = arraymacs.getJSONObject(i);  
//				DataStruct.venderOption.List_MacTypes.add(new MacTypes(
//					lan.getString(MacTypes.T_CID),
//					lan.getString(MacTypes.T_NAME)
//				));
				DataStruct.dbMacTypes_Table.insert(new MacTypes(
					lan.getString(MacTypes.T_CID),
					lan.getString(MacTypes.T_NAME)
    			));
			} 
			
			//macsAgentName
			System.out.println("BUG  arraymacsAgentName.length():"+arraymacsAgentName.length());
			for(int i=0;i<arraymacsAgentName.length();i++){  
				JSONObject lan = arraymacsAgentName.getJSONObject(i);  
				DataStruct.dbMacsAgentName_Table.insert(new MacsAgentName(
					lan.getString(MacsAgentName.T_CID),
					lan.getString(MacsAgentName.T_MID),
					lan.getString(MacsAgentName.T_AgentID),
					lan.getString(MacsAgentName.T_CNAME)
    			));
//				System.out.println("BUG  arraymacsAgentName.T_CID():"+lan.getString(MacsAgentName.T_CID));
//				System.out.println("BUG  arraymacsAgentName.T_MID():"+lan.getString(MacsAgentName.T_MID));
//				System.out.println("BUG  arraymacsAgentName.T_AgentID():"+lan.getString(MacsAgentName.T_AgentID));
//				System.out.println("BUG  arraymacsAgentName.T_CNAME():"+lan.getString(MacsAgentName.T_CNAME));
			} 
			
			
			System.out.println("BUG Update VerderOption OK !!!");  
//			System.out.println("VOT DataStruct.venderOption.List_CarBrands.size():"
//					+DataStruct.venderOption.List_CarBrands.size());  
//			System.out.println("VOT DataStruct.venderOption.List_CarTypes.size():"
//					+DataStruct.venderOption.List_CarTypes.size());  
//			System.out.println("VOT DataStruct.venderOption.List_MacTypes.size():"
//					+DataStruct.venderOption.List_MacTypes.size());  

        } catch (UnsupportedEncodingException e) {  
            // TODO Auto-generated catch block  
        	//System.out.println("VOT UnsupportedEncodingException");  
            e.printStackTrace();  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
        	//System.out.println("VOT IOException");  
            e.printStackTrace();  
        }catch (JSONException e) {  
            // TODO Auto-generated catch block  
        	//System.out.println("VOT JSONException");  
            e.printStackTrace();  
        }  
    }

}
