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
}
