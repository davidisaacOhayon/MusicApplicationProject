package com.example.davidisaaccrimusic.ui.playlist;



import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.davidisaaccrimusic.R;
import com.example.davidisaaccrimusic.ui.SongLibraryViewModel;
import com.example.davidisaaccrimusic.ui.playlist.SongItem.SongItem;
import com.example.davidisaaccrimusic.ui.playlist.SongList.SongList;
import com.example.davidisaaccrimusic.ui.playlist.SongList.SongListAdapter;

import java.util.List;


public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlayListViewHolder> {
    List<SongList> songLists;
    private SongLibraryViewModel sharedViewModel;

    public PlaylistAdapter(List<SongList> songLibrary, SongLibraryViewModel sharedViewModel) {
        this.songLists = songLibrary;
        this.sharedViewModel = sharedViewModel;
    }


    @NonNull
    @Override
    public PlayListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_song_list, parent, false);
        return new PlayListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayListViewHolder holder, int position) {

        // Iterate through each songList
        SongList songList = songLists.get(position);
        // Set title for each fragment of each Song list (Suggested, Trending, etc.)
        holder.title.setText(songList.getTitle());

        if ( sharedViewModel == null){
            Log.e("PlaylistAdapter", "sharedViewModel is null at position: " + position);
            return;
            // Initiate Song List Adapter and pass each individual song item

        }

        SongListAdapter songListAdapter = new SongListAdapter(songList.getSongs(), sharedViewModel);
        // Instantiate Recycler View
        holder.recyclerView.setAdapter(songListAdapter);


        // Configure recycler view layout
        holder.recyclerView.setLayoutManager(
                new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false)
        );

        holder.recyclerView.setAdapter(new SongListAdapter(songList.getSongs(), sharedViewModel));
    }

    @Override
    public int getItemCount() {
        return songLists.size();
    }

    class PlayListViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        RecyclerView recyclerView;

        public PlayListViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.song_list_title);
            recyclerView = itemView.findViewById(R.id.song_list_recycler);
        }
    }

}
