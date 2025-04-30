package com.example.davidisaaccrimusic.ui.userplaylist;

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
import com.example.davidisaaccrimusic.backend.SongHelper;
import com.example.davidisaaccrimusic.ui.SongLibraryViewModel;
import com.example.davidisaaccrimusic.ui.playlist.PlaylistAdapter;

public class UserPlaylistFragment extends Fragment {

    private UserPlaylistViewModel mViewModel;

    private SongLibraryViewModel sharedViewModel;

    public static UserPlaylistFragment newInstance() {
        return new UserPlaylistFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_playlist, container, false);

        SongHelper songhelper = new SongHelper(requireContext());

        // instantiate ViewModel
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SongLibraryViewModel.class);

        RecyclerView recyclerView = view.findViewById(R.id.favorite_list_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new UserPlaylistAdapter(sharedViewModel));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(UserPlaylistViewModel.class);
        // TODO: Use the ViewModel
    }

}