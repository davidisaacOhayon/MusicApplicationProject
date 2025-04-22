package com.example.davidisaaccrimusic.ui.playlist.SongItem;



public class SongItem{
    private String title;
    private int songId;
    private String genre;
    private String artist;



    public SongItem(String title, int songId, String genre, String artist){
        this.title = title;
        this.songId = songId;
        this.genre = genre;
        this.artist = artist;
    }
    public SongItem(String title, String genre, String artist){
        this.title = title;
        this.genre = genre;
        this.artist = artist;
    }


//  Getters
    public String getTitle(){
        return this.title;
    }

    public String getArtist(){
        return this.artist;
    }

    public int getId(){
        return songId;
    }

    public String getGenre(){
        return genre;
    }


}
