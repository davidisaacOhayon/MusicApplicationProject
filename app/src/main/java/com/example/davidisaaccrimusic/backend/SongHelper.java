package com.example.davidisaaccrimusic.backend;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.example.davidisaaccrimusic.ui.playlist.SongItem.SongItem;
import com.example.davidisaaccrimusic.ui.playlist.SongList.SongList;

import java.util.ArrayList;

public class SongHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "songlibrary.db";

    private final String _title = SongContract.SongEntry.COLUMN_NAME_TITLE;
    private final String _artist = SongContract.SongEntry.COLUMN_NAME_ARTIST;
    private final String _genre = SongContract.SongEntry.COLUMN_NAME_GENRE;

    public SongHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL(createTables());
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(dropTables());
        onCreate(db);
    }

    public void onDowngrade ( SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db, oldVersion, newVersion);
    }

    private String createTables(){
        return "CREATE TABLE " + SongContract.SongEntry.TABLE_NAME + " (" +
                SongContract.SongEntry._ID + " INTEGER PRIMARY KEY, " +
                _title + " varchar, " +
                _artist + " varchar, " +
                _genre + " varchar)";
    }

    private String dropTables(){
        return "DROP TABLE IF EXISTS " + SongContract.SongEntry.TABLE_NAME;
    }

    public void initiateTables(boolean reset){
        SQLiteDatabase db = this.getWritableDatabase();

        if (reset){
            db.execSQL(dropTables());
            onCreate(db);
        }

        if (getSongs().isEmpty()){
            this.insertSong(new SongItem("Ready Or Not", "Rock", "Infraction"));
            this.insertSong(new SongItem("Majestic Vision", "PianoIdk", "Infraction"));
            this.insertSong(new SongItem("Test Song", "Rock", "idklol"));
            this.insertSong(new SongItem("Test Song", "Rock", "idklol"));
            this.insertSong(new SongItem("Test Song", "Rock", "idklol"));
        }
    }

    // For Simulation Purposes, the Suggested, Trending other playlists will be randomized.
    public SongList getSongsRandom(int no, String pTitle){
        SongList songList = new SongList(pTitle);

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + SongContract.SongEntry.TABLE_NAME + " ORDER BY RANDOM() LIMIT ? ";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(no)});

        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(_title));
            String artist = cursor.getString(cursor.getColumnIndexOrThrow(_artist));
            String genre = cursor.getString(cursor.getColumnIndexOrThrow(_genre));
            songList.add(new SongItem(title, (int) id, genre, artist));
        }
        cursor.close();
        return songList;

    }

    public long insertSong(SongItem song){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(_title, song.getTitle());
        values.put(_artist, song.getArtist());
        values.put(_genre, song.getGenre());

        long id = db.insert(SongContract.SongEntry.TABLE_NAME, null, values);

        return id;
    }

    public ArrayList<SongItem> getSongs(){
        ArrayList<SongItem> songs = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String[] projection = {
                BaseColumns._ID,
                _title,
                _artist,
                _genre
        };

        String sortOrder = _title + " ASC";

        Cursor cursor = db.query(SongContract.SongEntry.TABLE_NAME,projection,null,null,null,null,sortOrder);

        while(cursor.moveToNext()){
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(_title));
            String artist = cursor.getString(cursor.getColumnIndexOrThrow(_artist));
            String genre = cursor.getString(cursor.getColumnIndexOrThrow(_genre));
            SongItem song = new SongItem(title, (int)id, genre, artist);
            songs.add(song);
        }
        cursor.close();

        return songs;
    }

    public SongItem getSongById(long id){
        SQLiteDatabase db = this.getWritableDatabase();

        String[] projection = {
                BaseColumns._ID,
                _title,
                _artist,
                _genre
        };

        String selection = BaseColumns._ID + " = ?";
        String[] selectionArgs = {Long.toString(id)};

        String sortOrder = _title + " ASC";

        Cursor cursor = db.query(SongContract.SongEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);

        SongItem song = null;
        while(cursor.moveToNext()){
            long songId = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(_title));
            String artist = cursor.getString(cursor.getColumnIndexOrThrow(_artist));
            String genre = cursor.getString(cursor.getColumnIndexOrThrow(_genre));
            song = new SongItem(title, (int)songId, genre, artist);
        }
        cursor.close();

        return song;
    }








}
