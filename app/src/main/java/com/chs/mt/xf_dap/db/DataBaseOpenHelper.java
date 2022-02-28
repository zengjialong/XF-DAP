package com.chs.mt.xf_dap.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseOpenHelper extends SQLiteOpenHelper {
	private static DataBaseOpenHelper mInstance = null;
	/** ���ݿ����� **/
	private static final String DATABASE_NAME  = "CHS_DataBase.db";
	public static final String TABLE_SEff_Data = "t_seffData";
	public static final String TABLE_SEff_FilE = "t_seffFile";
	public static final String TABLE_LoginSM  = "t_login_sm";

	public static final String TABLE_SEff_FilE_RECENTLY = "t_seffFile_recently";
	/** ���ݿ�汾�� **/
	private static final int DATABASE_VERSION = 2;
	/**���ݿ�SQL��� ���һ����**/
    private static final String SEFF_DATA_TABLE_CREATE = "CREATE TABLE t_seffData (id integer primary key autoincrement,"
			+"cid TEXT,"
			+"uid TEXT,"
			+"highData TEXT,"
			+"version TEXT,"
			+"type TEXT,"
			+"ctime TEXT,"
			+"brand TEXT,"
			+"macModel TEXT,"
			+"details TEXT,"
			+"effName TEXT"				
			+");";
    private static final String SEFF_FILE_TABLE_CREATE = "CREATE TABLE t_seffFile (id integer primary key autoincrement,"
			+"file_id TEXT,"
			+"file_type TEXT,"
			+"file_name TEXT,"
			+"file_path TEXT,"
			+"file_favorite TEXT,"
			+"file_love TEXT,"
			+"file_size TEXT,"
			+"file_time TEXT,"
			+"file_msg TEXT,"
			+"data_user_name TEXT,"
			+"data_machine_type TEXT,"
			+"data_car_type TEXT,"
			+"data_car_brand TEXT,"
			+"data_group_name TEXT,"
			+"data_upload_time TEXT,"
			+"data_eff_briefing TEXT,"
			+"list_sel TEXT,"
			+"list_is_open TEXT"
			+");";
    private static final String SEFF_FILE_RECENTLY_TABLE_CREATE = "CREATE TABLE t_seffFile_recently (id integer primary key autoincrement,"
			+"file_id TEXT,"
			+"file_type TEXT,"
			+"file_name TEXT,"
			+"file_path TEXT,"
			+"file_favorite TEXT,"
			+"file_love TEXT,"
			+"file_size TEXT,"
			+"file_time TEXT,"
			+"file_msg TEXT,"
			+"data_user_name TEXT,"
			+"data_machine_type TEXT,"
			+"data_car_type TEXT,"
			+"data_car_brand TEXT,"
			+"data_group_name TEXT,"
			+"data_upload_time TEXT,"
			+"data_eff_briefing TEXT,"
			+"list_sel TEXT,"
			+"list_is_open TEXT"
			+");";

    private static final String LoginSM_TABLE_CREATE = "CREATE TABLE t_login_sm (id integer primary key autoincrement,"
			+"name TEXT,"
			+"password TEXT,"
			+"re_password TEXT,"
			+"auto_login TEXT,"
			+"recently TEXT"
			+");"; 
    /**ɾ��һ�ű��SQL���**/
    private static final String SEFF_DATA_TABLE_DELETE = "DROP TABLE t_seffData";
    private static final String SEFF_FILE_TABLE_DELETE = "DROP TABLE t_seffFile";
    private static final String SEFF_FILE_RECENTLY_TABLE_DELETE = "DROP TABLE t_seffFile_recently";
	// ���췽��������
	public DataBaseOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	/**����ģʽ**/
    static synchronized DataBaseOpenHelper getInstance(Context context) {
		if (mInstance == null) {
		    mInstance = new DataBaseOpenHelper(context);
		}
		return mInstance;
    }
    /**����������ӱ�**/
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SEFF_DATA_TABLE_CREATE);
		db.execSQL(SEFF_FILE_TABLE_CREATE);
		db.execSQL(SEFF_FILE_RECENTLY_TABLE_CREATE);
		
		db.execSQL(LoginSM_TABLE_CREATE);
		
	}
	
	// ���·���
	/**�����õ���ǰ���ݿ�İ汾��Ϣ ��֮ǰ���ݿ�İ汾��Ϣ   �����������ݿ�**/
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		db.execSQL("EROP TABLE IF EXISTS t_seffData");
//		db.execSQL("EROP TABLE IF EXISTS t_seffFile");
//		db.execSQL("EROP TABLE IF EXISTS t_seffFile_recently");
//		
//		db.execSQL("EROP TABLE IF EXISTS t_login_sm");
		onCreate(db);
	}
	
	/**
     * ɾ�����ݿ�
     * @param context
     * @return
     */
    public boolean deleteDatabase(Context context) {
    	return context.deleteDatabase(DATABASE_NAME);
    }
    
    
    
	
	
	
	
	
	
	
}
