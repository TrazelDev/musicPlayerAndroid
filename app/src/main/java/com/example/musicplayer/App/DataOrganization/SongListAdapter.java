package com.example.musicplayer.App.DataOrganization;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.musicplayer.App.DataOrganization.Song;
import com.example.musicplayer.R;

import java.util.ArrayList;

public class SongListAdapter extends ArrayAdapter<Song> {
    private final Context context;
    private final ArrayList<Song> songs;
    private View.OnClickListener imageButtonClickListener; // Add this

    public SongListAdapter(Context context, ArrayList<Song> songs) {
        super(context, R.layout.song_list_item, songs);
        this.context = context;
        this.songs = songs;
    }

    public void setOnImageButtonClickListener(View.OnClickListener listener) {
        this.imageButtonClickListener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.song_list_item, parent, false);
        }

        Song currentSong = songs.get(position);

        TextView songNameTextView = listItemView.findViewById(R.id.itemSongName);
        ImageView songImageView = listItemView.findViewById(R.id.itemSongImage);
        ImageButton itemPlayIcon = listItemView.findViewById(R.id.itemPlayIcon); // Add this

        songNameTextView.setText(currentSong.getSongName());

        // Use Glide to load and set the image
        Glide.with(context)
                .load(currentSong.getImageUri()) // Assuming you have an image URI in your Song class
                .placeholder(R.drawable.default_album_cover) // Placeholder image
                .error(R.drawable.default_album_cover) // Error image
                .into(songImageView);

        // Set click listener for the ImageButton
        itemPlayIcon.setOnClickListener(imageButtonClickListener); // Add this

        return listItemView;
    }
}

