package com.example.musicplayer.App.DataOrganization;

import android.net.Uri;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class Database
{
    public Database()
    {
        m_realTimeDB = FirebaseDatabase.getInstance("https://musicplayer-54264-default-rtdb.europe-west1.firebasedatabase.app/");
        m_realTimeDBRef = m_realTimeDB.getReference();
        m_storageDB = FirebaseStorage.getInstance();
        m_storageRef = m_storageDB.getReference();
    }

    public void uploadNewSong(String songName, String songLen, boolean hasPic, Uri songUri, Uri picUri,
                              android.content.Context context)
    {
        String songUId = addSongRealTime(songName, songLen, hasPic);
        addSongStorage(songUri, picUri, context, hasPic, songUId);
    }


    /**
     * function that is uploading the song to the real time database
     */
    private String addSongRealTime(String songName, String songLen, boolean hasPic)
    {
        DatabaseReference songsRef = m_realTimeDBRef.child("Songs");
        String songUId = songsRef.push().getKey(); // creating a unique id for this specific song
        // uploading the name and length of the song:
        DatabaseReference thisSongRef = songsRef.child(songUId);
        thisSongRef.child("length").setValue(songLen);
        thisSongRef.child("name").setValue(songName);
        thisSongRef.child("hasPicture").setValue(hasPic);

        String accountUId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // getting the unique id that the user is getting when he creates an account
        DatabaseReference userAccountRef = m_realTimeDBRef.child("Users").child(accountUId); // getting a ref to the users account
        userAccountRef.child("SongIds").child(songUId).setValue(true); // the true means the user owns the song


        return songUId;
    }

    /**
     * function is uploading the song and the picture of it to the song
     */
    private void addSongStorage(Uri songUri, Uri picUri, android.content.Context context, boolean hasPic,
                                String songUId)
    {
        String useUniqueId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        m_storageRef.child("Songs/" + songUId).putFile(songUri)
                .addOnSuccessListener(taskSnapshot ->
                {
                    Toast.makeText(context, "uploaded song successfully", Toast.LENGTH_LONG).show();
                })
                .addOnFailureListener(exception ->
                {
                    Toast.makeText(context, "failed to upload song", Toast.LENGTH_SHORT).show();
                });


        if (hasPic)
        {
            m_storageRef.child("Pics/" + songUId).putFile(picUri)
                    .addOnSuccessListener(imageUploadTask ->
                    {
                        Toast.makeText(context, "Uploaded image successfully", Toast.LENGTH_LONG).show();


                    })
                    .addOnFailureListener(imageUploadException -> {
                        Toast.makeText(context, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    });
        }
    }


    public interface SongFetchListener {
        void onFetchComplete(List<Song> songs);
        void onFetchError(String error);
    }

    public void fetchSongs(SongFetchListener fetchListener) {
        DatabaseReference songsRef = m_realTimeDBRef.child("Songs");

        songsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Song> songs = new ArrayList<>();

                for (DataSnapshot songSnapshot : dataSnapshot.getChildren()) {
                    String songId = songSnapshot.getKey();
                    String songName = songSnapshot.child("name").getValue(String.class);
                    boolean hasPicture = songSnapshot.child("hasPicture").getValue(Boolean.class);

                    Song song = new Song(songId, songName, hasPicture);
                    songs.add(song);
                }

                fetchListener.onFetchComplete(songs);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                fetchListener.onFetchError(databaseError.getMessage());
            }
        });
    }

    public Task<Uri> retrievePictureFile(String songId)
    {
        StorageReference pictureRef = m_storageRef.child("Pics/" + songId);

        return pictureRef.getDownloadUrl();
    }


    private FirebaseDatabase m_realTimeDB; // the firebase real time database
    private DatabaseReference m_realTimeDBRef; // reference to the root of the real time data base
    private FirebaseStorage m_storageDB;
    private StorageReference m_storageRef;
}