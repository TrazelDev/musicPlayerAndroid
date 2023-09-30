package com.example.musicplayer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class AddSongToPlayListFragment extends Fragment {


    public AddSongToPlayListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View fragmentView = inflater.inflate(R.layout.fragment_add_song_to_play_list, container, false);

        ListView listView = fragmentView.findViewById(R.id.PlaylistView);

        // Create a list of Playlist objects
        ArrayList<Playlist> playlistList = new ArrayList<>();
        playlistList.add(new Playlist(R.drawable.default_album_cover, "Playlist 1"));
        playlistList.add(new Playlist(R.drawable.default_album_cover, "Playlist 2"));
        // Add more playlists as needed

        // Create a custom adapter and set it to the ListView
        PlaylistAdapter adapter = new PlaylistAdapter(requireContext(), playlistList);
        // listView.setAdapter(adapter);

        return fragmentView;
    }
}