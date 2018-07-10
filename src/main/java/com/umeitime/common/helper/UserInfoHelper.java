package com.umeitime.common.helper;

import android.database.Cursor;

import com.umeitime.common.AppContext;

/**
 * Created by hujunwei on 2018/4/17.
 */

public class UserInfoHelper {
    public static int getUserId(){
        int uid = 0;
        Cursor cursor = null;
        try {
            cursor = AppContext.getInstance().getApplicationContext().getContentResolver().query(DBContract.UserEntry.CONTENT_URI, null, null, null, null);
            cursor.moveToFirst();
            uid = cursor.getInt(1);
        }catch (Exception e){
        } finally {
            if(cursor!=null)
            cursor.close();
        }
        return uid;
    }
}
