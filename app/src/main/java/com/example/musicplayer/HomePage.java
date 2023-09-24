package com.example.musicplayer;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

public class HomePage extends Fragment {

    private static final String TAG = "PlaySongsFragment";
    private StorageReference songRef;
    private SongPlayer songPLayer;
    private TextView songMaxLength;
    private TextView songCurrLength;
    private Handler songTimeDalayHandler;
    private Runnable updateSongCurrentPoint;
    private int songCurrentTime = 0;
    private SeekBar songSeekBar;
    private ImageView circularButton;
    private int isPlaying = 0;
    public HomePage()
    {
        // Required empty public constructor
    }

    // function that is used for setup that is not related to user's input inside of this fragment:
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // setRetainInstance(true);

        // creating a new SongPlayer ( class that is responsible for playing the music that is stored inside of the storage database
        // inside of the firebase )
        songPLayer = new SongPlayer();

        // setting up the next song that is going to played
        String songFileName = "NEFFEX - Fear [Copyright Free] No.73.mp3";
        songPLayer.setNextSong(songFileName);

    }


    // function that is used for setup that is used for setup of things that user is going to interact with like for example setup of
    // event listener or find object of the xml by their id
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View fragmentView = inflater.inflate(R.layout.fragment_play_songs, container, false);

        // find the xml object of time of the song from both sides of the of the seekbar:
        songMaxLength = fragmentView.findViewById(R.id.songMaxLength);
        songCurrLength = fragmentView.findViewById(R.id.songCurrLength);

        // creating the button for pausing and playing the music:
        circularButton =  fragmentView.findViewById(R.id.play_pause_icon);
        circularButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (isPlaying % 2 == 0) circularButton.setImageResource(R.drawable.white_pause_icon);
                else circularButton.setImageResource(R.drawable.white_play_icon);

                if(isPlaying == 0) playSong();
                else songPLayer.pauseOrStartMusic();

                isPlaying += 1;
            }
        });

        // defining the seekbar and how it will change the song once the user moves it:
        songSeekBar = fragmentView.findViewById(R.id.songSeekBar);
        songSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser)
                {
                    songCurrentTime = progress;
                    songPLayer.changeSongTimePoint(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return fragmentView; // Return the inflated fragmentView, not a new one
    }

    // when the fragment is becoming visible again:
    @Override
    public void onStart()
    {
        super.onStart();
    }

    /*
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Initialization that depends on the activity
    }
    */

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause()
    {
        super.onPause();

        if(isPlaying != 0)
        {
            stopPeriodicUpdates();
        }


    }

    @Override
    public void onResume()
    {
        super.onResume();

        // if the song was playing in the background when the user switched to another fragment:
        if(songPLayer.isSongPlaying())
        {
            // because the song was playing when the user switched fragment we set the right icon:
            circularButton.setImageResource(R.drawable.white_pause_icon);

            setMaxSongLength();
            songCurrentTime = songPLayer.getCurrentSongPosition();
            songSeekBar.setProgress(songCurrentTime);
            updateCurrSongPoint();
            return;
        }

        // checking if the music was played before but was stopped now:
        if(songCurrentTime != 0)
        {
            setMaxSongLength();
            songCurrLength.setText(songPLayer.getFormatSongDuration(songCurrentTime * 1000));
            updateCurrSongPoint();
        }
    }


    private void playSong()
    {
        songPLayer.startPlayingSong();

        // delaying setting the length of the song because the server needs some time to process
        // that the song is start to play
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run()
            {
                int duration = setMaxSongLength();
                songSeekBar.setMax(duration / 1000);

                updateCurrSongPoint();
            }
        }, 1000);
    }

    private void updateCurrSongPoint()
    {
        songTimeDalayHandler = new Handler();
        updateSongCurrentPoint = new Runnable()
        {
            @Override
            public void run()
            {
                if (songPLayer.isSongPlaying())
                {
                    songCurrentTime++;
                    songCurrLength.setText(songPLayer.getFormatSongDuration(songCurrentTime * 1000));
                    songSeekBar.setProgress(songCurrentTime);
                }
                songTimeDalayHandler.postDelayed(this, 1000); // Schedule the runnable to run again after 1 second
            }
        };

        // starting the function
        songTimeDalayHandler.postDelayed(updateSongCurrentPoint, 1); // Schedule the runnable to run again after 1 second
    }
    private void stopPeriodicUpdates()
    {
        // Stop the periodic execution by removing callbacks
        songTimeDalayHandler.removeCallbacks(updateSongCurrentPoint);
    }

    private int setMaxSongLength()
    {
        int duration = songPLayer.getDuration(); // this is in milliseconds
        songMaxLength.setText(songPLayer.getFormatSongDuration(duration));

        return duration;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
