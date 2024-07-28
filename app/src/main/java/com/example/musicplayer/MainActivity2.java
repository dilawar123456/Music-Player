package com.example.musicplayer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {

    TextView name;
    MediaPlayer music;
    SeekBar seeker;
    ImageView play;
    ImageView next;
    ImageView prev;
    int position;
    boolean isplaying=false;
    ArrayList<File> songsarray;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        music.pause();
        music.release();
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        name=findViewById(R.id.txtName);
        play=findViewById(R.id.imgpause);
        seeker=findViewById(R.id.seekBar);
        next=findViewById(R.id.imgnext);
        prev=findViewById(R.id.imgprev);


        Intent intent=getIntent();
        String songname= intent.getStringExtra("name");
        position=intent.getIntExtra("position",0);
        Bundle bundle=intent.getExtras();
        songsarray=(ArrayList) bundle.getParcelableArrayList("songs_array");
        songname= songname.replace(".mp3","");
        name.setText(songname);
        Uri uri=Uri.parse(songsarray.get(position).toString());
        music= MediaPlayer.create(this,uri);
        music.start();
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isplaying==true)
                {
                    isplaying=false;
                    play.setImageResource(R.drawable.pausee);
                    music.start();
                }
                else
                {
                    isplaying=true;
                    play.setImageResource(R.drawable.playy);
                    music.pause();
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                music.pause();
                music.release();
                if(position<songsarray.size()-1) {
                    position++;
                }
                Uri uri=Uri.parse(songsarray.get(position).toString());
                music= MediaPlayer.create(MainActivity2.this,uri);
                name.setText(songsarray.get(position).getName());
                play.setImageResource(R.drawable.pausee);
                music.start();
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                music.pause();
                music.release();
                if(position>0)
                {
                    position--;
                }
                Uri uri=Uri.parse(songsarray.get(position).toString());
                music= MediaPlayer.create(MainActivity2.this,uri);
                name.setText(songsarray.get(position).getName());
                play.setImageResource(R.drawable.pausee);
                music.start();
            }
        });



        seeker.setMax(music.getDuration());
        seeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                music.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}