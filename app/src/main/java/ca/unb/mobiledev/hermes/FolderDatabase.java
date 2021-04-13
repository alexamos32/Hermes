package ca.unb.mobiledev.hermes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;

public class FolderDatabase extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "foldersdb";
    private static final String DATABASE_TABLE = "folderstable";

    //Column names for DB table
    private static final String KEY_ID = "id";
    private static final String KEY_PARENT_ID = "parent_id";
    private static final String KEY_NAME = "name";

    public FolderDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Creating the table and columns
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createDb = "CREATE TABLE " + DATABASE_TABLE + " ("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_PARENT_ID + " INTEGER,"
                + KEY_NAME + " TEXT"
                + " )";
        db.execSQL(createDb);
    }

    //Upgrade DB if older version exists
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion >= newVersion){
            return;
        }
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(db);
    }

    //Adds a folder to the DB
    public long addFolder(String name, int parentID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(KEY_NAME, name);
        v.put(KEY_PARENT_ID, parentID);

        //Insert folder into table
        long ID = db.insert(DATABASE_TABLE, null, v);
        return ID;
    }

    // Gets folder by ID
    public Folder getFolder(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] query = new String[] {KEY_ID, KEY_PARENT_ID, KEY_NAME};
        Cursor cursor = db.query(DATABASE_TABLE, query, KEY_ID + "=?", new String[] {String.valueOf(id)}, null, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
        }

        return new Folder(
                cursor.getInt(0),
                cursor.getInt(1),
                cursor.getString(2));
    }

    // Gets bases directory folders (no parent id)
    public List<Folder> getBaseDirectoryFolders() {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] query = new String[] {KEY_ID, KEY_PARENT_ID, KEY_NAME};
        Cursor cursor = db.query(DATABASE_TABLE, query, KEY_PARENT_ID + "= -1", null, null, null, null);

        List<Folder> folderList = new ArrayList();
        if(cursor.moveToFirst()){
            do {
                folderList.add(
                        new Folder(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2))
                );
            } while (cursor.moveToNext());
        }

        return folderList;
    }

    // Get folders by parent ID
    public List<Folder> getFolderByParent(long parentID) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] query = new String[] {KEY_ID, KEY_PARENT_ID, KEY_NAME};
        Cursor cursor = db.query(DATABASE_TABLE, query, KEY_PARENT_ID + "=?", new String[] {String.valueOf(parentID)}, null, null, null, null);

        List<Folder> folderList = new ArrayList();
        if(cursor.moveToFirst()){
            do {
                folderList.add(
                        new Folder(
                                cursor.getInt(0),
                                cursor.getInt(1),
                                cursor.getString(2))
                );
            } while (cursor.moveToNext());
        }

        return folderList;
    }
}
