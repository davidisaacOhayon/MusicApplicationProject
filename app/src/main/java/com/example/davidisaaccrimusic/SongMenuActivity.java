package com.example.davidisaaccrimusic;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.davidisaaccrimusic.backend.SongHelper;
import com.example.davidisaaccrimusic.ui.SongLibraryViewModel;


public class SongMenuActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    private boolean isPrepared = false;
    private Button playBtn;
    private Button favBtn;

    private SongLibraryViewModel sharedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Instnatiate Song helper
        SongHelper songhelper = new SongHelper(this);
        // instantiate view model
        sharedViewModel = new ViewModelProvider(this).get(SongLibraryViewModel.class);
        // set song helper of current view model object
        sharedViewModel.setSongHelper(songhelper);
        super.onCreate(savedInstanceState);
        // Set activity layout
        setContentView(R.layout.activity_songmenu);
        // retrieve intent data
        Intent intent = getIntent();
        playBtn = findViewById(R.id.play_button);
        favBtn = findViewById(R.id.favorite_button);

        // Reset Song player if song already playing

        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        // Retrieve data through intent
        String title = intent.getStringExtra("title");
        String artist = intent.getStringExtra("artist");
        String genre = intent.getStringExtra("genre");
        // Parse song id into an integer
        int id = intent.getIntExtra("id", -1);
        // retrieve song file name
        String file_id = "song_" + String.valueOf(id);

        // select respective elements from layout
        TextView titleView = findViewById(R.id.songmenu_title);
        TextView titleArtist = findViewById(R.id.songmenu_artist);
        TextView titleGenre = findViewById(R.id.songmenu_genre);
        ImageView imgBox = findViewById(R.id.song_image);

        // retrieve song cover photo and set it
        int img_id = getResources().getIdentifier("cover_" + id, "drawable", getPackageName());
        imgBox.setImageResource(img_id);

        titleView.setText(title);
        titleArtist.setText(artist);
        titleGenre.setText(" • " + genre);

        // Retrieve song file
        int resId = getResources().getIdentifier(file_id, "raw", getPackageName());
        // Create media player and load in song file directory
        mediaPlayer = MediaPlayer.create(this , resId );

        try {
            // Prepare media player asynchronously to prevent memory issues
            mediaPlayer.setDataSource(getResources().openRawResourceFd(resId));
            // Once media player receives file location
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){

                // Run once mediaplayer loads data source
                @Override
                public void onPrepared(MediaPlayer mp){
                    isPrepared = true;
                    mediaPlayer.start();
                }
            });
            mediaPlayer.prepareAsync();
        }catch (Exception e){

        }
        mediaPlayer.start();


        // Click listener for play button
        playBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                // if song is already playing
                if (mediaPlayer.isPlaying()){
                    // change symbol to play button
                    playBtn.setBackground(ContextCompat.getDrawable(SongMenuActivity.this, R.drawable.playbutton));
                    // pause song
                    mediaPlayer.pause();
                }else{
                    // change symbol to pause button
                    playBtn.setBackground(ContextCompat.getDrawable(SongMenuActivity.this, R.drawable.pausebutton));
                    // play song
                    mediaPlayer.start();
                }
            }

        });

        // Initialize FavBtn
        if(sharedViewModel.checkIfFavorited((long)id)){
            favBtn.setBackground(ContextCompat.getDrawable(SongMenuActivity.this, R.drawable.unfavoritebutton));
        }

        // On click listener for favourite button
        favBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // If song is already favorited
                if (sharedViewModel.checkIfFavorited((long)id)){

                    favBtn.setBackground(ContextCompat.getDrawable(SongMenuActivity.this, R.drawable.favoritebutton));
                    // remove song from favourite table
                    sharedViewModel.removeFromFavorite((long)id);

                // If song isnt favorited
                }else{
                    favBtn.setBackground(ContextCompat.getDrawable(SongMenuActivity.this, R.drawable.unfavoritebutton));
                    // add song to favourite table
                    sharedViewModel.addToFavorite((long)id);

                }
            }
        });
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (mediaPlayer != null){
            try {
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                // release media player of data source
                mediaPlayer.release();
                // destroy media player
                mediaPlayer = null;
                // media player is not loaded with file
                isPrepared = false;
            }catch (Exception e ){
                e.printStackTrace();
            }
        }
    }



}