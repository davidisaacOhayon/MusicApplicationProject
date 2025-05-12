package com.example.davidisaaccrimusic.ui.userplaylist;


import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.davidisaaccrimusic.R;
import com.example.davidisaaccrimusic.SongMenuActivity;
import com.example.davidisaaccrimusic.backend.SongHelper;
import com.example.davidisaaccrimusic.ui.SongLibraryViewModel;
import com.example.davidisaaccrimusic.ui.playlist.PlaylistAdapter;
import com.example.davidisaaccrimusic.ui.playlist.SongItem.SongItem;
import com.example.davidisaaccrimusic.ui.playlist.SongList.SongList;
import com.example.davidisaaccrimusic.ui.playlist.SongList.SongListAdapter;

import java.util.List;


public class UserPlaylistAdapter extends RecyclerView.Adapter<UserPlaylistAdapter.UserPlayListViewHolder> {


    private SongLibraryViewModel sharedViewModel;

    public UserPlaylistAdapter(SongLibraryViewModel sharedViewModel){
        this.sharedViewModel = sharedViewModel;
    }


    @NonNull
    @Override
    public UserPlaylistAdapter.UserPlayListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_playlist_item, parent, false);
        return new UserPlaylistAdapter.UserPlayListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserPlaylistAdapter.UserPlayListViewHolder holder, int position) {

        SongList favoriteList = sharedViewModel.getFavorites();
        // Iterate through each songList
        SongItem song = favoriteList.get(position);
        // Set title for each fragment of each Song list (Suggested, Trending, etc.)
        holder.title.setText(song.getTitle());

        holder.artist.setText(song.getArtist());


        int img_id = holder.itemView.getContext().getResources().getIdentifier(
                "cover_" + song.getId(), "drawable", holder.itemView.getContext().getPackageName()
        );


        holder.cover.setImageResource(img_id);

        holder.itemView.setOnClickListener( v -> {
            // Create new intent to allow each music item to redirect to SongMenuActivity
            Intent intent = new Intent(v.getContext(), SongMenuActivity.class);

            // Establish dbhelper object
            SongHelper dbHelper = new SongHelper(v.getContext());

            dbHelper.addToRecents(song.getId());
            sharedViewModel.updateRecents();

            // Return each attribute of song item class
            intent.putExtra("title", song.getTitle());
            intent.putExtra("genre", song.getGenre());
            intent.putExtra("artist", song.getArtist());
            intent.putExtra("id", song.getId());
            // start a new intent with the data returned being passed on to the new activity
            v.getContext().startActivity(intent);

        });
        if ( sharedViewModel == null){
            Log.e("PlaylistAdapter", "sharedViewModel is null at position: " + position);
            return;
            // Initiate Song List Adapter and pass each individual song item
        }
    }


    @Override
    public int getItemCount() {
        return sharedViewModel.getFavorites().getSongs().size();
    }





    class UserPlayListViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        TextView artist;

        ImageView cover;


        public UserPlayListViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.song_title);
            artist = itemView.findViewById(R.id.song_artist);
            cover = itemView.findViewById(R.id.song_image);

        }
    }
}
