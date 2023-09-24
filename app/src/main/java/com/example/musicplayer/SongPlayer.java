package com.example.musicplayer;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

public class SongPlayer
{
    public SongPlayer()
    {
        // creating the firebase storage
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        mediaPlayer = new MediaPlayer();

        // setting the attributes (optional)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            mediaPlayer.setAudioAttributes(audioAttributes);
        }
        else
        {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }
    }

    public void setNextSong(String songFileName)
    {
        // creating a reference for a specific song:
        songRef = storageRef.child(songFileName);
    }

    public void startPlayingSong()
    {
        try
        {
            // Prepare the MediaPlayer with the song URL
            songRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    try {
                        mediaPlayer.setDataSource(uri.toString());
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    } catch (IOException e) {
                        Log.e(TAG, "Error setting data source: " + e.getMessage());
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.e(TAG, "Error getting download URL: " + exception.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error playing song: " + e.getMessage());
        }
    }

    public void pauseOrStartMusic()
    {
        if (mediaPlayer.isPlaying())
        {
            mediaPlayer.pause();
            playbackPosition = mediaPlayer.getCurrentPosition(); // storing the current position of the song
        }
        else
        {
            mediaPlayer.seekTo(playbackPosition); // setting the song in the second that we paused previously
            mediaPlayer.start();
        }
    }

    public int getDuration()
    {
        if(mediaPlayer != null)
        {
            int durationInMilliSecs = mediaPlayer.getDuration();

            Log.d(TAG, "the length of the song in milliSeconds is: " + durationInMilliSecs);

            return durationInMilliSecs;
        }

        return 0;
    }
    public static String getFormatSongDuration(int milliseconds)
    {
        return String.format("%01d:%02d", (milliseconds / 60000) % 60, (milliseconds / 1000) % 60);
    }

    public boolean isSongPlaying()
    {
        return mediaPlayer.isPlaying();
    }

    public void changeSongTimePoint(int newTimePoint)
    {
        if(mediaPlayer != null && mediaPlayer.isPlaying())
        {
            mediaPlayer.seekTo(newTimePoint * 1000);
        }
    }

    public int getCurrentSongPosition()
    {
        return mediaPlayer.getCurrentPosition() / 1000;
    }

    private StorageReference songRef;
    private MediaPlayer mediaPlayer;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private int playbackPosition = 0; // contains the position of the song when it is resumed
    private static final String TAG = "musicPlayer";

}