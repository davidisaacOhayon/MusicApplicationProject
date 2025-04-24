package com.example.davidisaaccrimusic;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
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


public class SongMenuActivity extends AppCompatActivity {

    static MediaPlayer mediaPlayer;
    static Button playBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songmenu);
        Intent intent = getIntent();
        playBtn = findViewById(R.id.play_button);

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
    }


}