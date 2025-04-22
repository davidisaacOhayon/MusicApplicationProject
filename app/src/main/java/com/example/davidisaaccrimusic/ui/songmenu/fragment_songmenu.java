package com.example.davidisaaccrimusic.ui.songmenu;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.davidisaaccrimusic.R;

public class fragment_songmenu extends Fragment {

    private FragmentSongmenuViewModel mViewModel;

    public static fragment_songmenu newInstance() {
        return new fragment_songmenu();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fragment_songmenu, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FragmentSongmenuViewModel.class);
        // TODO: Use the ViewModel
    }

}