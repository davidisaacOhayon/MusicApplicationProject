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
        SongHelper songhelper = new SongHelper(this);
        sharedViewModel = new ViewModelProvider(this).get(SongLibraryViewModel.class);
        sharedViewModel.setSongHelper(songhelper);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songmenu);
        Intent intent = getIntent();
        playBtn = findViewById(R.id.play_button);
        favBtn = findViewById(R.id.favorite_button);

        // Reset Song player

        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        String title = intent.getStringExtra("title");
        String artist = intent.getStringExtra("artist");
        String genre = intent.getStringExtra("genre");
        int id = intent.getIntExtra("id", -1);
        String file_id = "song_" + String.valueOf(id);

        TextView titleView = findViewById(R.id.songmenu_title);
        TextView titleArtist = findViewById(R.id.songmenu_artist);
        TextView titleGenre = findViewById(R.id.songmenu_genre);
        ImageView imgBox = findViewById(R.id.song_image);

        int img_id = getResources().getIdentifier("cover_" + id, "drawable", getPackageName());

        imgBox.setImageResource(img_id);

        titleView.setText(title);
        titleArtist.setText(artist);
        titleGenre.setText(" • " + genre);

        int resId = getResources().getIdentifier(file_id, "raw", getPackageName());

        mediaPlayer = MediaPlayer.create(this , resId );

        try {
            mediaPlayer.setDataSource(getResources().openRawResourceFd(resId));
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){
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

        playBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                if (mediaPlayer.isPlaying()){
                    playBtn.setBackground(ContextCompat.getDrawable(SongMenuActivity.this, R.drawable.playbutton));
                    mediaPlayer.pause();
                }else{
                    playBtn.setBackground(ContextCompat.getDrawable(SongMenuActivity.this, R.drawable.pausebutton));
                    mediaPlayer.start();
                }
            }

        });

        // Initialize FavBtn
        if(sharedViewModel.checkIfFavorited((long)id)){
            favBtn.setBackground(ContextCompat.getDrawable(SongMenuActivity.this, R.drawable.unfavoritebutton));
        }

        favBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // If song is already favorited
                if (sharedViewModel.checkIfFavorited((long)id)){

                    favBtn.setBackground(ContextCompat.getDrawable(SongMenuActivity.this, R.drawable.favoritebutton));

                    sharedViewModel.removeFromFavorite((long)id);

                // If song isnt favorited
                }else{
                    favBtn.setBackground(ContextCompat.getDrawable(SongMenuActivity.this, R.drawable.unfavoritebutton));

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
                mediaPlayer.release();
                mediaPlayer = null;
                isPrepared = false;
            }catch (Exception e ){
                e.printStackTrace();
            }
        }
    }



}