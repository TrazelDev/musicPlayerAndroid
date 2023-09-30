package com.example.musicplayer.App.DataOrganization;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.musicplayer.App.DataOrganization.Playlist;
import com.example.musicplayer.R;

import java.util.ArrayList;

public class PlaylistAdapter extends ArrayAdapter<Playlist> {
    private Context context;
    private ArrayList<Playlist> playlists;

    public PlaylistAdapter(Context context, ArrayList<Playlist> playlists) {
        super(context, 0, playlists);
        this.context = context;
        this.playlists = playlists;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Playlist playlist = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.playlist_item, parent, false);
        }

        // Lookup views within item layout
        Button playlistButton = convertView.findViewById(R.id.playlistButton);
        ImageView playlistImage = convertView.findViewById(R.id.playlistImage);
        TextView playlistName = convertView.findViewById(R.id.playlistName);

        // Populate the data into the template view using the data object
        playlistName.setText(playlist.getName());
        // Set the image (you can use a library like Glide for efficient image loading)

        // Set an OnClickListener for the playlistButton
        playlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click here, e.g., get the playlist name
                String playlistName = playlist.getName();
                // Do something with the playlist name
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }
}
