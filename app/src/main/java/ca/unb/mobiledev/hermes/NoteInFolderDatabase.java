package ca.unb.mobiledev.hermes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;

public class NoteInFolderDatabase extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "notesdb";
    private static final String DATABASE_TABLE = "noteinfoldertable";

    //Column names for DB table
    private static final String KEY_NOTE_ID = "note_id";
    private static final String KEY_FOLDER_ID = "folder_id";

    public NoteInFolderDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Creating the table and columns
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createDb = "CREATE TABLE " + DATABASE_TABLE + " ("
                + KEY_NOTE_ID + " INTEGER NOT  NULL, "
                + KEY_FOLDER_ID + " INTEGER NOT NULL, "
                + "PRIMARY KEY (" + KEY_NOTE_ID + ", " + KEY_FOLDER_ID + ")"
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

    //Adds a note to a folder in DB
    public long addNoteToFolder(String note_id, String folder_id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(KEY_NOTE_ID, note_id);
        v.put(KEY_FOLDER_ID, folder_id);

        long ID = db.insert(DATABASE_TABLE, null, v);
        return ID;
    }

    // gets all note IDs within folder specified by ID
    public String[] getNoteIdsForFolder(String folderID) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] query = new String[] {KEY_NOTE_ID};
        Cursor cursor = db.query(DATABASE_TABLE, query, KEY_FOLDER_ID + "=?", new String[] {folderID}, null, null, null, null);
        cursor.moveToFirst();

        List<String> listIDs = new ArrayList();
        do {
            listIDs.add(cursor.getString(0));
        } while (cursor.moveToNext() != false);

        return (String[]) listIDs.toArray();
    }

/*
    public void deleteNote(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE,KEY_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }*/
}
