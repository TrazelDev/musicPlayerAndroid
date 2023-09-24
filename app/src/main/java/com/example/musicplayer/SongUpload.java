package com.example.musicplayer;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SongUpload extends Fragment {
    Button uploadPicture;
    Button uploadSong;

    public SongUpload() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View fragmentView = inflater.inflate(R.layout.fragment_song_upload, container, false);


        // this is something that we call as helper and when it is finished it calls the function
        // for saving the file in the firebase
        // URI (Uniform Resource Identifier) - similar to a computer file path this is the structure of it:
        // scheme:[//authority]path[?query][#fragment]
        ActivityResultLauncher<String> mGetContent = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri ->
                {
                    if (uri != null)
                    {
                        uploadFile(uri);
                    }
                }
        );

        uploadPicture = fragmentView.findViewById(R.id.uploadPicture);
        uploadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();

                if(activity != null)
                {
                    mGetContent.launch("image/*"); // accepting picture files
                }
            }
        });

        uploadSong = fragmentView.findViewById(R.id.uploadSong);
        uploadSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();

                if(activity != null)
                {
                    mGetContent.launch("audio/mpeg"); // accepting mp3 files
                }
            }
        });

        return fragmentView;
    }

    private void uploadFile(Uri fileUri)
    {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference fileRef = storageRef.child("uploads/" + fileUri.getLastPathSegment());

        fileRef.putFile(fileUri)
                .addOnSuccessListener(taskSnapshot -> {
                    Toast.makeText(getActivity(), "uploaded song successfully", Toast.LENGTH_LONG).show();
                })
                .addOnFailureListener(exception -> {
                    Toast.makeText(getActivity(), "failed to upload song", Toast.LENGTH_LONG).show();
                });
    }
}