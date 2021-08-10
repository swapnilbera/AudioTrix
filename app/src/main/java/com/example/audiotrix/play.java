package com.example.audiotrix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class play extends AppCompatActivity {
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaplayer.pause();
        mediaplayer.release();
        updateseek.interrupt();
    }

    TextView textview;
ImageView play,next,prev;
ArrayList<File> songs;
MediaPlayer mediaplayer;
SeekBar seekbar;
Thread thread;
int position;
Thread updateseek;
Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        prev=findViewById(R.id.imageView3);
        next=findViewById(R.id.imageView5);
        play=findViewById(R.id.imageView);
        textview=findViewById(R.id.textView);
        textview.setSelected(true);
        seekbar=findViewById(R.id.seekBar);
         intent=getIntent();
        Bundle bundle=intent.getExtras();
        songs=(ArrayList) bundle.getParcelableArrayList("songs");
        String text=intent.getStringExtra("current song");
        textview.setText(text);
        position=intent.getIntExtra("position",0);
        Uri uri=Uri.parse(songs.get(position).toString());
       mediaplayer = MediaPlayer.create(this, uri);
        mediaplayer.start();
       seekbar.setMax(mediaplayer.getDuration());
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //if(fromUser){
                   // mediaplayer.seekTo(progress);
               // }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaplayer.seekTo(seekBar.getProgress());

            }
        });
        updateseek = new Thread() {
            @Override
            public void run() {
                int currentposition=0;
                try{
                   while (currentposition<mediaplayer.getDuration()){
                       currentposition=mediaplayer.getCurrentPosition();
                       seekbar.setProgress(currentposition);
                       sleep(800);
                   }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        updateseek.start();




    }
    public void pause(View view) {
        if(mediaplayer.isPlaying()){
            play.setImageResource(R.drawable.play);
            mediaplayer.pause();

        }
        else{
            play.setImageResource(R.drawable.pause);
            mediaplayer.start();
        }
    }



    public void prev(View view) {
        mediaplayer.stop();
        mediaplayer.release();
        if(position!=0){
            position=position-1;
        }
        else{
            position= songs.size()-1;
        }
        play.setImageResource(R.drawable.pause);
        Uri uri=Uri.parse(songs.get(position).toString());
        mediaplayer = MediaPlayer.create(getApplicationContext(),uri);
        mediaplayer.start();
        seekbar.setMax(mediaplayer.getDuration());
        String textcontent=songs.get(position).getName().toString();
        textview.setText(textcontent);


    }

    public void next(View view) {
        mediaplayer.stop();
        mediaplayer.release();
        if(position!= songs.size()-1){
            position=position+1;
        }
        else{
            position= 0;
        }
        play.setImageResource(R.drawable.pause);
        Uri uri=Uri.parse(songs.get(position).toString());
        mediaplayer = MediaPlayer.create(getApplicationContext(),uri);
        mediaplayer.start();
        seekbar.setMax(mediaplayer.getDuration());
        String textcontent=songs.get(position).getName().toString();
        textview.setText(textcontent);

    }


}