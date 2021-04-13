package ca.unb.mobiledev.hermes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;

public class NoteDatabase extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "notesdb";
    private static final String DATABASE_TABLE = "notestable";

    //Column names for DB table
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";
    private static final String KEY_REM_TIME = "rem_time";
    private static final String KEY_REM_DATE = "rem_date";



    public NoteDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    //Creating the table and columns
    @Override
    public void onCreate(SQLiteDatabase db) {
         String createDb = "CREATE TABLE " + DATABASE_TABLE + " ("
                 + KEY_ID + " INTEGER PRIMARY KEY,"
                 + KEY_TITLE + " TEXT,"
                 + KEY_CONTENT + " TEXT,"
                 + KEY_DATE + " TEXT,"
                 + KEY_TIME + " TEXT,"
                 + KEY_REM_TIME + " TEXT,"
                 + KEY_REM_DATE + " TEXT"
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

    //Adds a note to the DB
    public long addNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(KEY_TITLE, note.getTitle());
        v.put(KEY_CONTENT, note.getContent());
        v.put(KEY_DATE, note.getDate());
        v.put(KEY_TIME, note.getTime());
        v.put(KEY_REM_TIME, note.getRemTime());
        v.put(KEY_REM_DATE, note.getRemDate());

        //Insert note into table
        long ID = db.insert(DATABASE_TABLE, null, v);
        return ID;
    }

    public Note getNote(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] query = new String[] {KEY_ID, KEY_TITLE, KEY_CONTENT, KEY_DATE, KEY_TIME, KEY_REM_TIME, KEY_REM_DATE};
        Cursor cursor = db.query(DATABASE_TABLE, query, KEY_ID + "=?", new String[] {String.valueOf(id)}, null, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
        }

        return new Note(
                Long.parseLong(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6));

    }


    public List<Note> getAllNotes() {
        List<Note> allNotes = new ArrayList<>();
        String query = "SELECT * FROM " + DATABASE_TABLE + " ORDER BY " + KEY_ID+ " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){
            do{
                Note note = new Note();
                note.setId(Long.parseLong(cursor.getString(0)));
                note.setTitle(cursor.getString(1));
                note.setContent(cursor.getString(2));
                note.setDate(cursor.getString(3));
                note.setTime(cursor.getString(4));
                note.setRemTime(cursor.getString(5));
                note.setRemDate(cursor.getString(6));
                allNotes.add(note);
            } while (cursor.moveToNext());
        }

        return allNotes;
    }

    public int editNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        Log.d("Edited", "Edited Title: -> "+ note.getTitle() + "\n ID -> " + note.getId());
        content.put(KEY_TITLE,note.getTitle());
        content.put(KEY_CONTENT,note.getContent());
        content.put(KEY_DATE,note.getDate());
        content.put(KEY_TIME,note.getTime());
        content.put(KEY_REM_TIME, note.getRemTime());
        content.put(KEY_REM_DATE, note.getRemDate());
        return db.update(DATABASE_TABLE, content,KEY_ID+ "=?" , new String[] {String.valueOf(note.getId())} );

    }

    public void deleteNote(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE,KEY_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }
}
