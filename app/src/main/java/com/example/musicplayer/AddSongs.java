package com.example.musicplayer;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class AddSongs extends Fragment {

    private SongUploadFragment uploadSongsFragment;
    private SongListFragment songListFragment;
    private ImageButton uploadSongScreen;
    private ImageButton viewSongsScreen;

    private static final int PICK_FILE_REQUEST = 1;

    public AddSongs() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_add_songs, container, false);

        // Create instances of the child fragments
        if (uploadSongsFragment == null) { uploadSongsFragment = new SongUploadFragment(); } // { uploadSongsFragment = new SongUploadFragment(); }
        if (songListFragment == null) { songListFragment = new SongListFragment(); }

        uploadSongScreen = fragmentView.findViewById(R.id.uploadSongButton);
        uploadSongScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceChildFragment(uploadSongsFragment);
            }
        });

        viewSongsScreen = fragmentView.findViewById(R.id.viewSongs);
        viewSongsScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceChildFragment(songListFragment);
            }
        });

        // Initially replace with the uploadSongsFragment
        replaceChildFragment(songListFragment);

        return fragmentView;
    }

    private void replaceChildFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.songManipulation, fragment);
        transaction.addToBackStack(null); // add to back stack
        transaction.commit();
    }

}
