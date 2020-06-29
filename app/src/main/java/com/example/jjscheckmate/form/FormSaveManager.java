package com.example.jjscheckmate.form;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONObject;

public class FormSaveManager {
    private static final String DB_FORM="forms.db";
    private static final String TABLE_FORMS="forms";
    private static final int DB_VERSION=1;

    private static FormSaveManager formSaveManager=null;
    private SQLiteDatabase mDatabase=null;

    Context mContext=null;

    private FormSaveManager(Context context){
        mContext=context;
        mDatabase=mContext.openOrCreateDatabase(DB_FORM,mContext.MODE_PRIVATE,null);
        mDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS "+TABLE_FORMS+
                        "("+"_id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        "json TEXT, "+
                        "time TEXT);"
        );
    }

    public static FormSaveManager getInstance(Context context){
        if(formSaveManager==null){
            formSaveManager=new FormSaveManager(context);
        }
        return formSaveManager;
    }
    public Cursor query(String[] colums, String selection, String[] selectArgs, String groupBy, String having, String orderBy){
        return mDatabase.query(TABLE_FORMS,colums,selection,selectArgs,groupBy,having,orderBy);
    }

    public long insert(ContentValues addRowValue){  // ContentValues ?
        return mDatabase.insert(TABLE_FORMS,null,addRowValue);
    }
    public int update( ContentValues updateRowValue,String whereClause,String[] whereArgs ){
        return mDatabase.update( TABLE_FORMS, updateRowValue, whereClause, whereArgs );
    }

    public int delete( String whereClause,
                       String[] whereArgs ){
        return mDatabase.delete( TABLE_FORMS,
                whereClause, whereArgs);
    }

    public boolean isJsonExist(int id) {
        boolean isExist = false;

        String[] columns = new String[]{"_id"};
        String selection = "_id=?"; // ? 를 붙여야하나
        String[] selectionArgs = new String[]{String.valueOf(id)};

        Cursor cursor = formSaveManager.query(columns, selection, selectionArgs, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                isExist = true;
                Log.d("mawang","FormSaveManager isJsonExist -cursor = "+cursor);
                Log.d("mawang","FormSaveManager isJsonExist -cursor.moveToNext = "+cursor.moveToNext());
                // 딱 한번 도나본데?
                break;
            }
            cursor.close();
        }
        return isExist;
    }

    public long save(int id,JSONObject jsonObject,String time){
        boolean isExist=false;
        long isSave;

        isExist = isJsonExist(id); // form_id
        String selection = "_id=?";
        String[] selectionArgs = new String[]{String.valueOf(id)};

        ContentValues addValue = new ContentValues(); // key,value 형태
        addValue.put("json", jsonObject.toString());
        addValue.put("time", time);

        if (isExist) {
            isSave = update(addValue, selection, selectionArgs);
        } else {
            isSave = insert(addValue);
        }
        Log.d("mawang","FormSaveManager save - isSave = "+isSave);

        return isSave; // 왜?
    }

    public JSONObject load(int id){
        JSONObject jsonObject=null;

        String[] columns=new String[]{"_id","json"};
        String selection="_id=?";
        String[] selectionArgs=new String[]{String.valueOf(id)};

        Cursor cursor=query(columns,selection,selectionArgs,null,null,null);

        if(cursor!=null){
            while(cursor.moveToNext()){ // ?
                try{
                    jsonObject=new JSONObject(cursor.getString(1));
                    //1개만 가져옴,,  테스트용,,,
                    // 왜? 뭘 1개만 가져오는데?
                    break;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            cursor.close();
        }

        return jsonObject;
    }
}
