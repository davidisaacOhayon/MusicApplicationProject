package com.example.davidisaaccrimusic.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.davidisaaccrimusic.backend.SongHelper;
import com.example.davidisaaccrimusic.ui.playlist.SongItem.SongItem;
import com.example.davidisaaccrimusic.ui.playlist.SongList.SongList;

import java.util.ArrayList;
import java.util.List;

public class SongLibraryViewModel extends ViewModel {

    private final MutableLiveData<List<SongList>> playList = new MutableLiveData<>();
    private final MutableLiveData<SongList> recentsPlaylist = new MutableLiveData<>();



    private SongHelper songHelper;
    private boolean isInitialized = false;

    public SongLibraryViewModel(){
    }

    public SongLibraryViewModel(SongHelper songHelper){
        this.songHelper = songHelper;
    }

    public void loadPlaylistData(SongHelper dbHelper, boolean forceRefresh) {
        // Only initialize once unless forced
        if (isInitialized && !forceRefresh) {
            return;
        }

        this.songHelper = dbHelper;
        
        // Create a new list each time to avoid duplicates
        List<SongList> freshSongLibrary = new ArrayList<SongList>();

        // Fill up Playlists
        SongList suggestedList = dbHelper.getSongsRandom(5, "Suggested");
        SongList trendingList = dbHelper.getSongsRandom(5, "Trending");
        SongList recentList = dbHelper.getRecents();

        // Include playlists into song library
        freshSongLibrary.add(suggestedList);
        freshSongLibrary.add(trendingList);
        freshSongLibrary.add(recentList);


        // Set the new list
        playList.setValue(freshSongLibrary);
        
        // Update recents playlist
        recentsPlaylist.setValue(recentList);
        
        isInitialized = true;
    }

    public void setPlayList(List<SongList> list){
        playList.setValue(list);
    }

    public void setSongHelper(SongHelper songHelper){
        this.songHelper = songHelper;
    }

    public int size(){
        return this.playList.getValue().size();
    }

    public LiveData<List<SongList>> getPlaylists() {
        return playList;
    }

    public SongList getPlaylist(int index){
        return playList.getValue().get(index);
    }


    public void updateRecents(){

        List<SongList> currentPlaylist = this.playList.getValue();
        this.recentsPlaylist.setValue(songHelper.getRecents());
        SongList recents = this.recentsPlaylist.getValue();

        for (int i = 0; i < currentPlaylist.size() ; i++){

            if ("Recents".equals(currentPlaylist.get(i).getTitle())){
                currentPlaylist.set(i, recents);

                this.playList.setValue(currentPlaylist);
                System.out.println(this.playList.getValue().get(i).getSongs().get(0).getTitle());
                break;
            }
        }
    }
    public void addToRecent(SongItem song){
        songHelper.addToRecents(song.getId());
        recentsPlaylist.setValue(songHelper.getRecents());
    }

    public void addToFavorite(long song_id){
        songHelper.addToFavorites(song_id);
    }

    public SongList getFavorites(){
        List<SongItem> favorites = songHelper.getFavorites().getSongs();
        return songHelper.getFavorites();
    }

    public void removeFromFavorite(long song_id){
        songHelper.removeFromFavorites(song_id);
    }

    public boolean checkIfFavorited(long song_id){
        return songHelper.checkIfFavorited((song_id));
    }

    public LiveData<SongList> getRecentsList(){
        return recentsPlaylist;
    }


}
