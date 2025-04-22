package com.example.davidisaaccrimusic.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.davidisaaccrimusic.ui.playlist.SongList.SongList;

import java.util.List;

public class SongLibraryViewModel extends ViewModel {

    private final MutableLiveData<List<SongList>> playList = new MutableLiveData<>();


    public void setPlayList(List<SongList> list){
        playList.setValue(list);
    }

    public LiveData<List<SongList>> getPlaylists() {
        return playList;
    }


}
