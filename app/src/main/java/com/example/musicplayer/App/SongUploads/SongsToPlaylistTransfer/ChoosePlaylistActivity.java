package com.example.musicplayer.App.SongUploads.SongsToPlaylistTransfer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.musicplayer.App.DataOrganization.Database;
import com.example.musicplayer.App.DataOrganization.Playlist;
import com.example.musicplayer.App.DataOrganization.PlaylistAdapter;
import com.example.musicplayer.R;

import java.util.ArrayList;

public class ChoosePlaylistActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_playlist);

        thisButton = findViewById(R.id.buttonForCheck);
        ListView listView = findViewById(R.id.PlaylistView);

        thisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) 
            {
                // Create an Intent to return the selected playlist to the calling activity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectedPlaylist", "abc");
                setResult(RESULT_OK, resultIntent);

                // Finish the activity to return to the calling activity
                finish();
            }
        });

        ArrayList<String> songs = new ArrayList<String>();
        songs.add("hello my world");
        songs.add("hello to this world");

        m_db = new Database();
        // m_db.createPlayList(songs);


        ArrayList<Playlist> playlistList = new ArrayList<>();


        PlaylistAdapter playlistAdapter = new PlaylistAdapter(this, playlistList);

        listView.setAdapter(playlistAdapter);
    }

    private Button thisButton;
    private Database m_db;
}