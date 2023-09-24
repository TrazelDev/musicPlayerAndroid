package com.example.musicplayer;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddSongs extends Fragment {

    private SongUpload uploadSongsFragment;
    private SongViewing viewSongsFragment;
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

        // creating all of the child fragments that we need:
        if(uploadSongsFragment == null) {uploadSongsFragment = new SongUpload(); }
        if(viewSongsFragment == null) {viewSongsFragment = new SongViewing(); }


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
                replaceChildFragment(viewSongsFragment);
            }
        });

        replaceChildFragment(viewSongsFragment);

        return fragmentView;
    }


    private void replaceChildFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.songManipulation, fragment);
        transaction.commit();
    }

}