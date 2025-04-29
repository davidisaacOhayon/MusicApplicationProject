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
        String CREATE_SONGS_TABLE =  "CREATE TABLE " + SongContract.SongEntry.TABLE_NAME + " (" +
                SongContract.SongEntry._ID + " INTEGER PRIMARY KEY, " +
                _title + " varchar, " +
                _artist + " varchar, " +
                _genre + " varchar)";

        String CREATE_RECENTS_TABLE = "CREATE TABLE " + SongContract.RecentsEntry.TABLE_NAME + " (" +
                SongContract.RecentsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SongContract.RecentsEntry.COLUMN_NAME_SONG_ID + " INTEGER, "
                + " FOREIGN KEY (" + SongContract.RecentsEntry.COLUMN_NAME_SONG_ID + ") "+
                "REFERENCES " + SongContract.SongEntry.TABLE_NAME + " (" + SongContract.SongEntry._ID + "));";

        String CREATE_FAVORITES_TABLE = "CREATE TABLE " + SongContract.FavoritesPlayList.TABLE_NAME + " (" +
                SongContract.FavoritesPlayList._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SongContract.FavoritesPlayList.COLUMN_NAME_SONG_ID + " INTEGER, "
                + " FOREIGN KEY (" + SongContract.FavoritesPlayList.COLUMN_NAME_SONG_ID + ") "+
                "REFERENCES " + SongContract.SongEntry.TABLE_NAME + " (" + SongContract.SongEntry._ID + "));";

        db.execSQL(CREATE_SONGS_TABLE);
        db.execSQL(CREATE_RECENTS_TABLE);
        db.execSQL(CREATE_FAVORITES_TABLE);

    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(dropTables());
        onCreate(db);
    }

    public void onDowngrade ( SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db, oldVersion, newVersion);
    }

    private String dropTables(){
        return "DROP TABLE IF EXISTS " + SongContract.SongEntry.TABLE_NAME + ";" + " DROP TABLE IF EXISTS " + SongContract.RecentsEntry.TABLE_NAME + ";";
    }

    public void initiateTables(boolean reset){
        SQLiteDatabase db = this.getWritableDatabase();

        if (reset){
            db.execSQL("DROP TABLE IF EXISTS " + SongContract.SongEntry.TABLE_NAME + ";");
            db.execSQL("DROP TABLE IF EXISTS " + SongContract.RecentsEntry.TABLE_NAME + ";");
            db.execSQL("DROP TABLE IF EXISTS " + SongContract.FavoritesPlayList.TABLE_NAME + ";");
        }
        onCreate(db);

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

    public void addToRecents(long song_id){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(SongContract.RecentsEntry.COLUMN_NAME_SONG_ID, song_id);

        db.insert(SongContract.RecentsEntry.TABLE_NAME, null ,values);

        db.close();

    }

    public void addToFavorites(long song_id){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(SongContract.FavoritesPlayList.COLUMN_NAME_SONG_ID, song_id);

        db.insert(SongContract.FavoritesPlayList.TABLE_NAME, null ,values);

        db.close();


    }

    public void removeFromFavorites(long song_id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(
                SongContract.FavoritesPlayList.TABLE_NAME,
                SongContract.FavoritesPlayList.COLUMN_NAME_SONG_ID + " = ?",
                new String[]{String.valueOf(song_id)}
        );
        db.close();
    }

    public boolean checkIfFavorited(long song_id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT 1 FROM " + SongContract.FavoritesPlayList.TABLE_NAME + " WHERE "
                + SongContract.FavoritesPlayList.COLUMN_NAME_SONG_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(song_id)});

        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

    public SongList getFavorites(){
        ArrayList<SongItem> songs = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String query = "SELECT s._id, s._title, s._genre, s._artist " +
                "FROM " + SongContract.FavoritesPlayList.TABLE_NAME + " r " +
                "INNER JOIN " + SongContract.SongEntry.TABLE_NAME + " s " +
                "ON r." + SongContract.FavoritesPlayList.COLUMN_NAME_SONG_ID + " = s." + SongContract.SongEntry._ID + " " +
                "ORDER BY r." + SongContract.FavoritesPlayList._ID + " DESC";

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(SongContract.SongEntry._ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(SongContract.SongEntry.COLUMN_NAME_TITLE));
                String artist = cursor.getString(cursor.getColumnIndexOrThrow(SongContract.SongEntry.COLUMN_NAME_ARTIST));
                String genre = cursor.getString(cursor.getColumnIndexOrThrow(SongContract.SongEntry.COLUMN_NAME_GENRE));
                songs.add(new SongItem(title, id, genre, artist));
            } while(cursor.moveToNext());
        }
        cursor.close();

        return new SongList("Recents", songs);

    }
    public SongList getRecents(){
        ArrayList<SongItem> songs = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String query = "SELECT s._id, s._title, s._genre, s._artist " +
                "FROM " + SongContract.RecentsEntry.TABLE_NAME + " r " +
                "INNER JOIN " + SongContract.SongEntry.TABLE_NAME + " s " +
                "ON r." + SongContract.RecentsEntry.COLUMN_NAME_SONG_ID + " = s." + SongContract.SongEntry._ID + " " +
                "ORDER BY r." + SongContract.RecentsEntry._ID + " DESC";

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(SongContract.SongEntry._ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(SongContract.SongEntry.COLUMN_NAME_TITLE));
                String artist = cursor.getString(cursor.getColumnIndexOrThrow(SongContract.SongEntry.COLUMN_NAME_ARTIST));
                String genre = cursor.getString(cursor.getColumnIndexOrThrow(SongContract.SongEntry.COLUMN_NAME_GENRE));
                songs.add(new SongItem(title, id, genre, artist));
            } while(cursor.moveToNext());
        }
        cursor.close();

        return new SongList("Recents", songs);

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
