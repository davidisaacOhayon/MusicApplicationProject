package com.example.davidisaaccrimusic.backend;

import android.provider.BaseColumns;

import java.util.stream.BaseStream;

public class SongContract {

    private SongContract( ) {}

    public static class SongEntry implements BaseColumns {
        public static final String TABLE_NAME = "songs";
        public static final String COLUMN_NAME_TITLE = "_title";
        public static final String COLUMN_NAME_GENRE = "_genre";
        public static final String COLUMN_NAME_ARTIST = "_artist";


    }

    public static class RecentsEntry implements BaseColumns{

        public static final String TABLE_NAME = "recents";
        public static final String COLUMN_NAME_SONG_ID = "_song_id";
    }


    public static class FavoritesPlayList implements BaseColumns {

        public static final String TABLE_NAME = "favorites_playlist";

        public static final String COLUMN_NAME_SONG_ID = "_song_id";
    }
}
