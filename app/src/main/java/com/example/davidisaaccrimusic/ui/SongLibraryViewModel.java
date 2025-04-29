package com.example.davidisaaccrimusic.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.davidisaaccrimusic.backend.SongHelper;
import com.example.davidisaaccrimusic.ui.playlist.SongItem.SongItem;
import com.example.davidisaaccrimusic.ui.playlist.SongList.SongList;

import java.util.List;

public class SongLibraryViewModel extends ViewModel {

    private final MutableLiveData<List<SongList>> playList = new MutableLiveData<>();
    private final MutableLiveData<SongList> recentsPlaylist = new MutableLiveData<>();

    private SongHelper songHelper;

    public SongLibraryViewModel(){
    }

    public SongLibraryViewModel(SongHelper songHelper){
        this.songHelper = songHelper;
    }


    public void setPlayList(List<SongList> list){
        playList.setValue(list);
    }

    public LiveData<List<SongList>> getPlaylists() {
        return playList;
    }


    public void addToRecent(SongItem song){
        songHelper.addToRecents(song.getId());
        recentsPlaylist.setValue(songHelper.getRecents());
    }

    public void addToFavorite(long song_id){
        songHelper.addToFavorites(song_id);
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
