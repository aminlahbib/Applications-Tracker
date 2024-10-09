package com.example.berwerbungstrackingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class NoteDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "notes.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "notes";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_PLACE = "place";
    private static final String COLUMN_ESTABLISHMENT = "establishment";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_REMARKS = "remarks";

    public NoteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_STATUS + " TEXT, " +
                COLUMN_PLACE + " TEXT, " +
                COLUMN_ESTABLISHMENT + " TEXT, " +
                COLUMN_TYPE + " TEXT, " +
                COLUMN_REMARKS + " TEXT)";

        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // CRUD operations

    public long addNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, note.getTitle());
        values.put(COLUMN_DATE, note.getDate());
        values.put(COLUMN_STATUS, note.getStatus());
        values.put(COLUMN_PLACE, note.getPlace());
        values.put(COLUMN_ESTABLISHMENT, note.getEstablishment());
        values.put(COLUMN_TYPE, note.getType());
        values.put(COLUMN_REMARKS, note.getRemarks());

        long id = db.insert(TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public List<Note> getAllNotes() {
        List<Note> noteList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_DATE + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Note note = createNoteFromCursor(cursor);
                noteList.add(note);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return noteList;
    }

    public List<Note> getRecentNotes(int limit) {
        List<Note> noteList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_DATE + " DESC LIMIT " + limit;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Note note = createNoteFromCursor(cursor);
                noteList.add(note);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return noteList;
    }

    public List<Note> searchNotesByTitle(String title) {
        List<Note> noteList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME +
                " WHERE " + COLUMN_TITLE + " LIKE '%" + title + "%'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Note note = createNoteFromCursor(cursor);
                noteList.add(note);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return noteList;
    }
    // Update an existing note in the database
    public int updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, note.getTitle());
        values.put(COLUMN_DATE, note.getDate());
        values.put(COLUMN_STATUS, note.getStatus());
        values.put(COLUMN_PLACE, note.getPlace());
        values.put(COLUMN_ESTABLISHMENT, note.getEstablishment());
        values.put(COLUMN_TYPE, note.getType());
        values.put(COLUMN_REMARKS, note.getRemarks());
        int result = db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(note.getId())});
        db.close();
        return result;
    }
    private Note createNoteFromCursor(Cursor cursor) {
        Note note = new Note();
        note.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
        note.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
        note.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
        note.setStatus(cursor.getString(cursor.getColumnIndex(COLUMN_STATUS)));
        note.setPlace(cursor.getString(cursor.getColumnIndex(COLUMN_PLACE)));
        note.setEstablishment(cursor.getString(cursor.getColumnIndex(COLUMN_ESTABLISHMENT)));
        note.setType(cursor.getString(cursor.getColumnIndex(COLUMN_TYPE)));
        note.setRemarks(cursor.getString(cursor.getColumnIndex(COLUMN_REMARKS)));
        return note;
    }
    // Delete a note from the database
    public void deleteNote(int noteId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(noteId)});
        db.close();
    }
}
