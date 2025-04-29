package com.example.davidisaaccrimusic.ui.playlist.SongList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.davidisaaccrimusic.R;
import com.example.davidisaaccrimusic.ui.SongLibraryViewModel;
import com.example.davidisaaccrimusic.ui.playlist.PlaylistAdapter;
import com.example.davidisaaccrimusic.ui.playlist.SongItem.SongItem;

import java.util.ArrayList;
import java.util.List;

public class SongListFragment extends Fragment {


    private SongLibraryViewModel sharedViewModel;

    private List<SongItem> songs;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.songlist_item, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.song_list_recycler);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SongLibraryViewModel.class);

        sharedViewModel.getPlaylists().observe(getViewLifecycleOwner(), songLists -> {
            if (songLists != null) {
                recyclerView.setAdapter(new PlaylistAdapter(songLists, sharedViewModel)); // one row per SongList
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        mViewModel = new ViewModelProvider(this).get(SongListViewModel.class);
        // TODO: Use the ViewModel
    }

    private LiveData<List<SongList>> getSongs(){
        return sharedViewModel.getPlaylists();
    }

}