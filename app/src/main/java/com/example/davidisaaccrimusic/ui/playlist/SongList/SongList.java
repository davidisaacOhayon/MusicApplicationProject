package com.example.davidisaaccrimusic.ui.playlist.SongList;

import com.example.davidisaaccrimusic.ui.playlist.SongItem.SongItem;

import java.util.ArrayList;
import java.util.List;

public class SongList{


    public List<SongItem> songs = new ArrayList<>();

    public String list_title;


    public SongList(String title){
        this.list_title = title;
    }

    public SongList(String title, List<SongItem> songs){
        this.songs = songs;
        this.list_title = title;
    }
    public void add(SongItem newSong){
        songs.add(newSong);
    }

    public SongItem get(int index){
        return songs.get(index);
    }

    public List<SongItem> getSongs(){
        return songs;
    }
    public String getTitle(){
        return this.list_title;
    }
}
