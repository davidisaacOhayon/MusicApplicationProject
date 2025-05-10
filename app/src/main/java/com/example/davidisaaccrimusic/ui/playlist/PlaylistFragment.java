package com.example.davidisaaccrimusic.ui.playlist;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.davidisaaccrimusic.R;
import com.example.davidisaaccrimusic.backend.SongHelper;
import com.example.davidisaaccrimusic.ui.SongLibraryViewModel;
import com.example.davidisaaccrimusic.ui.playlist.SongItem.SongItem;
import com.example.davidisaaccrimusic.ui.playlist.SongList.SongList;
import com.example.davidisaaccrimusic.ui.playlist.SongList.SongListFragment;

import java.util.ArrayList;
import java.util.List;

public class PlaylistFragment extends Fragment {

    private PlaylistViewModel mViewModel;

    private SongLibraryViewModel sharedViewModel;





    public static PlaylistFragment newInstance() {
        return new PlaylistFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_playlist, container, false);

        // Instantiate SongHelper for DB access
        SongHelper dbHelper = new SongHelper(requireContext());

        // Ensure database is full (note: Reset boolean clears DB before init, set to true only during database changes)
        dbHelper.initiateTables(false);


        // instantiate ViewModel
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SongLibraryViewModel.class);

        // initialize playlist data
        sharedViewModel.loadPlaylistData(dbHelper, true);
        // Set global song library

        //
        RecyclerView recyclerView = view.findViewById(R.id.playlist_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new PlaylistAdapter(sharedViewModel));

        return view;

    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PlaylistViewModel.class);

    }



}