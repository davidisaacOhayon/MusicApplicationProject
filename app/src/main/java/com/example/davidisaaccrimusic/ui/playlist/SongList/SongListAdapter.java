package com.example.davidisaaccrimusic.ui.playlist.SongList;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.davidisaaccrimusic.R;
import com.example.davidisaaccrimusic.SongMenuActivity;
import com.example.davidisaaccrimusic.backend.SongHelper;
import com.example.davidisaaccrimusic.ui.SongLibraryViewModel;
import com.example.davidisaaccrimusic.ui.playlist.SongItem.SongItem;

import java.util.List;

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.SongViewHolder> {
    List<SongItem> songs;
    private SongLibraryViewModel sharedViewmodel;

    public SongListAdapter(List<SongItem> songs, SongLibraryViewModel sharedViewmodel) {
        this.songs = songs;
        this.sharedViewmodel = sharedViewmodel;
    }


    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_item, parent, false);
        return new SongViewHolder(view);
    }


    // Configures for every single Song Item
    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        // Get respective song item at position
        SongItem song = songs.get(position);
        // Set the title and artist text on the layout item (song_item.xml)
        holder.title.setText(song.getTitle());
        holder.artist.setText(song.getArtist());

        // Instantiate an OnClickListener for every item
        holder.itemView.setOnClickListener( v -> {
            // Create new intent to allow each music item to redirect to SongMenuActivity
            Intent intent = new Intent(v.getContext(), SongMenuActivity.class);

            // Establish dbhelper object
            SongHelper dbHelper = new SongHelper(v.getContext());

            dbHelper.addToRecents(song.getId());
            sharedViewmodel.updateRecents();

            // Return each attribute of song item class
            intent.putExtra("title", song.getTitle());
            intent.putExtra("genre", song.getGenre());
            intent.putExtra("artist", song.getArtist());
            intent.putExtra("id", song.getId());
            // start a new intent with the data returned being passed on to the new activity
            v.getContext().startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    class SongViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView artist;
        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.song_item_title);
            artist = itemView.findViewById(R.id.song_item_artist);
        }
    }
}